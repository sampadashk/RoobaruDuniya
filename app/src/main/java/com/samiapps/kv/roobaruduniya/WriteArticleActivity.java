package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KV on 18/6/17.
 */

public class WriteArticleActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;

    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    DatabaseReference dbPendingArticle;

    int draftPressed=0;
    String userEmail;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    EditText title;
    EditText content;
    MenuItem draftButton;
    MenuItem saveButton;
    MenuItem publishButton;
    ImageButton photoButton;
    RoobaruDuniya rbd;
    String userProfile;


    String userPos;
    String key;
    String userId;
    User u;
    Boolean b;
    int pos;
    List<String> uids;
    RoobaruDuniya artsel;

    private static final int RC_PHOTO_PICKER = 2;
    private ValueEventListener eventListener;
    final HashMap<String, Object> userMap = new HashMap<String, Object>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_article);
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userEmail = user.getEmail();
        userId=user.getUid();
        userPos="Blogger";
        try {
            userProfile = user.getPhotoUrl().toString();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }






        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");

        dbEditor=db.getReference("editor");
        dbRefUser = db.getReference("user");
        dbPendingArticle=db.getReference("pending");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
      //  FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
        Intent intent=getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key=intent.getStringExtra("Keypos");
            Log.d("keypos",key);

            Log.d("checkpos", "" + pos);
            rbd= (RoobaruDuniya) intent.getSerializableExtra(DraftFragment.TAG);
            title.setText(rbd.getTitle());
            content.setText(rbd.getContent());
            draftPressed+=1;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        dbEditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().equals(userEmail)) {
                        userPos="Editor";
                        dbEditor.removeEventListener(this);
                        break;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        uids=new ArrayList<>();
        photoButton = (ImageButton) findViewById(R.id.photoPickerButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

            }
        });



    }

    public void onStart() {

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {
                if ((st.toString().trim().length()) > 0)

                {
                    if(rbd!=null)
                    {
                        rbd.setTitle(st.toString());
                        draftButton.setEnabled(true);

                    }


                }

            }




            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.toString().trim().length()) > 0)

                {
                    if(rbd!=null)
                    {
                        rbd.setContent(s.toString());
                    }
                    draftButton.setEnabled(true);

                    if ((s.toString().trim().length()) > 50) {
                        publishButton.setEnabled(true);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkUserDb();

        super.onStart();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_write, menu);
        draftButton = (MenuItem)menu.findItem(R.id.draft);
        saveButton = (MenuItem)menu.findItem(R.id.savecl);
        publishButton=(MenuItem)menu.findItem(R.id.publish);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.draft: {
                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId,userProfile,1, 0);

                    } else if(rbd!=null)
                    {
                        if (rbd.getDraft() == 0) {
                            rbd.setDraft(1);
                        }
                    }
                    if (draftPressed == 0) {
                        key = dbRefMsg.push().getKey();
                        dbRefMsg.child(key).setValue(rbd);
                        draftPressed += 1;
                    } else {
                        dbRefMsg.child(key).setValue(rbd);
                    }

                   /* if (u == null) {
                        u = new User(user.getDisplayName(), userEmail, "draft",userPos);
                    }
                    */

                    Boolean b1=uids.contains(userId);
                    if (b1) {

                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");
                    } else {
                        if (u == null) {
                            u = new User(user.getDisplayName(), userEmail, "draft",userPos);
                        }
                        dbRefUser.child(userId).setValue(u);
                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");
                    }


                    Toast.makeText(this, "Draft saved", Toast.LENGTH_LONG).show();
                    draftButton.setEnabled(false);
                    saveButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.savecl: {
                saveNclose();
                break;
            }
            case R.id.publish: {
                draftButton.setEnabled(false);

                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId,userProfile,0, 1);
                        key = dbRefMsg.push().getKey();
                        dbRefMsg.child(key).setValue(rbd);

                    } else if (rbd!=null){
                        rbd.setDraft(0);
                        rbd.setSent(1);
                        Log.d("checkphoto",rbd.getPhoto());
                        if(key==null) {
                            key = dbRefMsg.push().getKey();

                        }

                            dbRefMsg.child(key).setValue(rbd);


                    }
                    if(u==null)
                    {
                        u=new User(user.getDisplayName(), userEmail,"sent",userPos);
                    }
                    else {
                        u.setArticleStatus("sent");
                    }
                    Log.d("chkuse",u.getarticleStatus());
                    Boolean b2=uids.contains(userId);
                    if (b2) {
                        Log.d("pubbool",""+b2);
                        Log.d("pubkey",key);
                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");
                    } else {
                        dbRefUser.child(userId).setValue(u);
                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");
                    }








                } catch (Exception e) {
                    e.printStackTrace();
                }
                PendingClass pending=new PendingClass(false,false,null);
                Log.d("pendingKey",key);

                dbPendingArticle.child(key).setValue(pending);


                Toast.makeText(this,"Article sent!will be published once it gets approved by editor",Toast.LENGTH_LONG).show();
                saveNclose();
                break;

            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNclose() {
        draftPressed=0;
        key=null;
        Intent inti=new Intent(this,TrialActivity.class);
        startActivity(inti);

    }

    protected void onPause()
    {
        super.onPause();
        if(eventListener!=null)
        {
           dbRefUser.removeEventListener(eventListener);
            eventListener=null;
        }
    }

    private void checkUserDb() {
        //We are checking usernames in userdatabase and adding it in list
        if (eventListener == null) {
            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> iterable=dataSnapshot.getChildren();

                    for(DataSnapshot uid:iterable)
                    {
                       String s= uid.getKey();
                        Log.d("ckk",s);
                        uids.add(s);

                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };



            dbRefUser.addValueEventListener(eventListener);
        }

    }
    @Override
    public void onBackPressed() {
        saveNclose();

    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICKER && resultcode == RESULT_OK) {
            final Uri SelectedImageUri = data.getData();
            if (content.toString() != null) {
                StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
                photoref.putFile(SelectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        try {
                            if(rbd!=null) {
                                rbd.setPhoto(downloadUrl.toString());

                            }
                            else if(rbd==null) {
                                rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), downloadUrl.toString(), user.getDisplayName().toString(), userId,userProfile,0, 0);

                            }
                            draftButton.setEnabled(true);

                            publishButton.setEnabled(true);
                        } catch (NullPointerException e) {
                            Log.d("exception", "" + e);
                        }
                    }
                });
                Toast.makeText(this,"Photo uploaded",Toast.LENGTH_LONG).show();


            }
        }

    }
}

