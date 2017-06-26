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
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;
    DatabaseReference dbRefMsgList;
    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    int draftPressed=0;
    String userEmail;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    EditText title;
    EditText content;
    MenuItem draftButton;
    MenuItem saveButton;
    ImageButton photoButton;
    RoobaruDuniya rbd;
    String userPos;
    String key;
    String userId;
    User u;
    Boolean b;
    List<String> uids;

    private static final int RC_PHOTO_PICKER = 2;
    private ValueEventListener eventListener;
    final HashMap<String, Object> userMap = new HashMap<String, Object>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_article);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userPos="Blogger";


        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");
        dbRefMsgList = db.getReference("msg_list");
        dbEditor=db.getReference("editor");
        dbRefUser = db.getReference("user");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
      //  FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
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

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.toString().trim().length()) > 0)

                {
                    draftButton.setEnabled(true);
                    saveButton.setEnabled(true);

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
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.draft: {
                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userEmail, 1, 0);

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

                    if (u == null) {
                        u = new User(user.getDisplayName(), userEmail, "draft",userPos);
                    }

                    Boolean b1=uids.contains(userId);
                    if (b1) {

                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");
                    } else {
                        dbRefUser.child(userId).setValue(u);
                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");
                    }


                    Toast.makeText(this, "Draft saved", Toast.LENGTH_LONG).show();
                    draftButton.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.savecl: {
                break;
            }
            case R.id.publish: {
                draftButton.setEnabled(false);

                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userEmail, 0, 1);
                        key = dbRefMsg.push().getKey();
                        dbRefMsg.child(key).setValue(rbd);

                    } else {
                        rbd.setDraft(0);
                        rbd.setSent(1);
                        dbRefMsg.child(key).setValue(rbd);
                    }
                    if(u==null)
                    {
                        u=new User(user.getDisplayName(), userEmail,"sent",userPos);
                    }
                    else
                        u.setArticleStatus("sent");
                    Log.d("chkuse",u.getarticleStatus());
                    Boolean b2=uids.contains(userId);
                    if (b2) {
                        Log.d("pubbool",""+b2);
                        Log.d("pubkey",key);
                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");
                    } else {
                        dbRefUser.child(userId).setValue(u);
                    }








                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"Article sent!will be published once it gets approved by editor",Toast.LENGTH_LONG).show();
                break;

            }


        }
        return super.onOptionsItemSelected(item);
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
                            else {
                                rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), downloadUrl.toString(), user.getDisplayName().toString(), userEmail, 0, 0);

                            }
                            draftButton.setEnabled(true);
                            saveButton.setEnabled(true);
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

