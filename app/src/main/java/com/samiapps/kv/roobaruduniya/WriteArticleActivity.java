package com.samiapps.kv.roobaruduniya;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by KV on 18/6/17.
 */

public class WriteArticleActivity extends AppCompatActivity {
    private static final int RC_PROFILE_PICKER = 4;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;


    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    DatabaseReference dbPendingArticle;
    DatabaseReference publishedRef;
    Button writerDetail;
    String wName;

    int draftPressed = 0;
    String userEmail;
    String uStatus;
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
    Uri downloadProfileUrl;
    private StorageReference defaultPhoto;



    String userPos;
    String key;
    String userId;
    User u;
    Boolean b;
    int pos;
    List<String> uids;
    RoobaruDuniya artsel;
    ProgressBar progressBar;

    private static final int RC_PHOTO_PICKER = 2;
    private ValueEventListener eventListener;
    final HashMap<String, Object> userMap = new HashMap<String, Object>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_article);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userEmail = user.getEmail();
        userId = user.getUid();
        uStatus=TrialActivity.userStatus;
        firebaseStorage=FirebaseStorage.getInstance();
        defaultPhoto=firebaseStorage.getReference().child("default");
        //userPos = "Blogger";
        userPos=TrialActivity.userStatus;
        progressBar=(ProgressBar)findViewById(R.id.pbar);

        try {
            userProfile = user.getPhotoUrl().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");

        dbEditor = db.getReference("editor");
        dbRefUser = db.getReference("user");
        dbPendingArticle = db.getReference("pending");
        publishedRef=db.getReference("published");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
        //  FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
        Intent intent = getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key = intent.getStringExtra("Keypos");
            Log.d("keypos", key);

            Log.d("checkpos", "" + pos);
            rbd = (RoobaruDuniya) intent.getSerializableExtra(DraftFragment.TAG);
            title.setText(rbd.getTitle());
            content.setText(rbd.getContent());
            draftPressed += 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        /*
        dbEditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().equals(userEmail)) {
                        userPos = "Editor";
                        dbEditor.removeEventListener(this);
                        break;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        uids = new ArrayList<>();
        photoButton = (ImageButton) findViewById(R.id.photoPickerButton);
        writerDetail = (Button) findViewById(R.id.writer_detail);
        photoButton.setEnabled(false);

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

    public Dialog onCreateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_writer, null);
        final EditText writerName = (EditText) dialogView.findViewById(R.id.username);
        final ImageButton profilePhoto = (ImageButton) dialogView.findViewById(R.id.profilePhotoButton);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PROFILE_PICKER);
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        wName = writerName.getText().toString();
                        if(downloadProfileUrl!=null)
                        {
                            Log.d("checkdownload",downloadProfileUrl.toString());
                        }
                        if (rbd != null) {
                            rbd.setUser(wName);
                            if(downloadProfileUrl!=null)
                            {
                                rbd.setUserProfilePhoto(downloadProfileUrl.toString());
                            }

                            //TODO: SET user profile photo
                        } else if (rbd == null) {
                            rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, wName, null, null, 0, 0);
                            if(downloadProfileUrl!=null)
                            {
                                rbd.setUserProfilePhoto(downloadProfileUrl.toString());
                            }
                        }
                        if(downloadProfileUrl==null)
                        {
                            String add="firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
                            Uri defaultuserpicUrl= Uri.parse("https://" +add);
                            rbd.setUserProfilePhoto(defaultuserpicUrl.toString());
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void onStart() {
        try {
            if (uStatus.equals("editor")) {
                writerDetail.setVisibility(View.VISIBLE);
                writerDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog d = onCreateDialog();
                        d.show();

                    }
                });

                Log.d("writername", wName);
            }
        }catch (NullPointerException e) {
                e.printStackTrace();
            }




        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {
                if ((st.toString().trim().length()) > 0)

                {
                    if (rbd != null) {
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
                    if (rbd != null) {
                        rbd.setContent(s.toString());
                    }
                    draftButton.setEnabled(true);
                    photoButton.setEnabled(true);

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
        draftButton = (MenuItem) menu.findItem(R.id.draft);
        saveButton = (MenuItem) menu.findItem(R.id.savecl);
        publishButton = (MenuItem) menu.findItem(R.id.publish);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.draft: {
                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId, userProfile, 1, 0);

                    } else if (rbd != null) {
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

                    Boolean b1 = uids.contains(userId);
                    if (b1) {

                        dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");
                    } else {
                        if (u == null) {
                            u = new User(user.getDisplayName(), userEmail, "draft", userPos);
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

         //       try {


                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId, userProfile, 0, 1);
                        key = dbRefMsg.push().getKey();
                        dbRefMsg.child(key).setValue(rbd);

                    }
                    //when photo is selected
                else if (rbd != null) {
                        rbd.setDraft(0);
                        rbd.setSent(1);
                      //  Log.d("checkphoto", rbd.getPhoto());
                        if (key == null) {
                            key = dbRefMsg.push().getKey();

                        }

                        dbRefMsg.child(key).setValue(rbd);


                    }
                if(rbd.getPhoto()==null)
                {


                    //select random photo from storage and put in rbd object and firebase database

                   /* Random rand = new Random();
                    int value = rand.nextInt(4);
                    String st=value+".jpg";
                    Log.d("image",st);
                    defaultPhoto.child(st).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {

                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("imageuri",uri.toString());
                            rbd.setPhoto(uri.toString());
                            dbRefMsg.child(key).child("photo").setValue(rbd.getPhoto());
                        }
                    });
                    */



                }
                    if (userPos.equals("editor")) {
                        //TODO: publish editor
                        publishEditor();
                    } else {


                        if (u == null) {
                            u = new User(user.getDisplayName(), userEmail, "sent", userPos);
                        } else if (u != null) {
                            u.setArticleStatus("sent");
                        }
                        Log.d("chkuse", u.getarticleStatus());
                        Boolean b2 = uids.contains(userId);
                        if (b2) {
                            Log.d("pubbool", "" + b2);
                            Log.d("pubkey", key);
                            dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");
                        } else {
                            dbRefUser.child(userId).setValue(u);
                            dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");
                        }


                        //catch(Exception e){
                        //   e.printStackTrace();
                        //  }
                        PendingClass pending = new PendingClass(false, false, null);
                        Log.d("pendingKey", key);

                        dbPendingArticle.child(key).setValue(pending);
                        Toast.makeText(this, "Article sent!will be published once it gets approved by editor", Toast.LENGTH_LONG).show();
                    }




                saveNclose();
                break;

            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void publishEditor() {

      if (u == null) {
        u = new User(user.getDisplayName(), userEmail, "published", userPos);
    } else if(u!=null) {
        u.setArticleStatus("published");
    }
                    Log.d("chkuse", u.getarticleStatus());
    Boolean b2 = uids.contains(userId);
                    if (b2) {
        Log.d("pubbool", "" + b2);
        Log.d("pubkey", key);
        dbRefUser.child(userId).child("articleStatus").child(key).setValue("published");
    } else {
        dbRefUser.child(userId).setValue(u);
        dbRefUser.child(userId).child("articleStatus").child(key).setValue("published");
    }
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        Log.d("checkDate",dateString);
        publishedRef.child(key).child("dateCreated").setValue(dateString);
    }

    private void saveNclose() {
        draftPressed = 0;
        key = null;
        Intent inti = new Intent(this, TrialActivity.class);
        startActivity(inti);

    }

    protected void onPause() {
        super.onPause();
        if (eventListener != null) {
            dbRefUser.removeEventListener(eventListener);
            eventListener = null;
        }
    }

    private void checkUserDb() {
        //We are checking usernames in userdatabase and adding it in list
        if (eventListener == null) {
            eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();

                    for (DataSnapshot uid : iterable) {
                        String s = uid.getKey();
                        Log.d("ckk", s);
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
            Toast.makeText(this,"Please wait!Photo uploading to server",Toast.LENGTH_LONG).show();
            progressBar.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);

                StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
                photoref.putFile(SelectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        try {
                            if (rbd != null) {
                                rbd.setPhoto(downloadUrl.toString());

                            } else if (rbd == null) {
                                rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), downloadUrl.toString(), user.getDisplayName().toString(), userId, userProfile, 0, 0);

                            }
                            draftButton.setEnabled(true);

                          //  publishButton.setEnabled(true);
                        } catch (NullPointerException e) {
                            Log.d("exception", "" + e);
                        }
                    }
                });

              //TODO use progressbar

                    progressBar.setVisibility(View.GONE);


                Toast.makeText(this, "Photo uploaded", Toast.LENGTH_LONG).show();



        } else if (requestcode == RC_PROFILE_PICKER && resultcode == RESULT_OK) {
            final Uri SelectedProfileUri = data.getData();
            StorageReference picref = storageReference.child(SelectedProfileUri.getLastPathSegment());
            picref.putFile(SelectedProfileUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadProfileUrl = taskSnapshot.getDownloadUrl();
                }


            });






            Toast.makeText(this, "Photo uploaded", Toast.LENGTH_LONG).show();
        }
    }

    class AsyncUpload extends AsyncTask<Object, Object, Void>
    {

        @Override
        protected Void doInBackground(Object... params) {
             uploaddefaultImage();
            return null;
        }

        private void uploaddefaultImage() {
            Random rand = new Random();
            int value = rand.nextInt(4);
            String st=value+".jpg";
            Log.d("image",st);
            defaultPhoto.child(st).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {

                @Override
                public void onSuccess(Uri uri) {
                    Log.d("imageuri",uri.toString());
                    rbd.setPhoto(uri.toString());
                    dbRefMsg.child(key).child("photo").setValue(uri.toString());
                }
            });
        }
        @Override
        protected void onPostExecute(Void res)
        {
            super.onPostExecute(res);
            Toast.makeText(WriteArticleActivity.this,"Please wait!",Toast.LENGTH_LONG).show();
        }
    }
}

