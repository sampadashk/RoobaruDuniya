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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;

/**
 * Created by KV on 29/6/17.
 */

public class EditorArticleActivity extends AppCompatActivity{
    EditText title;
    EditText content;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;
    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    DatabaseReference dbPendingArticle;
    DatabaseReference publishedRef;
    ImageButton photoButton;
    int pos;
    String key;
    String writerId;
    RoobaruDuniya rbd;
    MenuItem approveButton;
    MenuItem saveEditorButton;
    MenuItem rejectButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static final int RC_PHOTO_PICK = 3;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_article);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");
        dbEditor=db.getReference("editor");
        dbRefUser = db.getReference("user");
        dbPendingArticle=db.getReference("pending");
        publishedRef=db.getReference("published");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
        Intent intent=getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key=intent.getStringExtra("Keypos");

            Log.d("keypos",key);

            Log.d("checkpos", "" + pos);
            rbd= (RoobaruDuniya) intent.getSerializableExtra(SentFragment.TAG);
            writerId=rbd.getuserId();
            title.setText(rbd.getTitle());
            content.setText(rbd.getContent());
            //draftPressed+=1;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        photoButton = (ImageButton) findViewById(R.id.photoPickerButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ckphtot","photoslected");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICK);

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        approveButton = (MenuItem)menu.findItem(R.id.approved);
        saveEditorButton = (MenuItem)menu.findItem(R.id.saveEditor);
        rejectButton=(MenuItem)menu.findItem(R.id.reject);
        return true;
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
                        saveEditorButton.setEnabled(true);

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


                    if ((s.toString().trim().length()) > 50) {
                        saveEditorButton.setEnabled(true);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        super.onStart();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        int id = menuItem.getItemId();
        switch (id)
        {
            case R.id.approved:
            {
                PendingClass pc=new PendingClass(true,true,TrialActivity.mUsername);
                dbPendingArticle.child(key).setValue(pc);
                addPublishedDatabase();
                //CHANGE VALUE OF ARTICLE STATUS IN USERDB TO PUBLISHED
                changeUserDB();
                //approveButton.setEnabled(false);
                saveEditorButton.setEnabled(false);
                rejectButton.setEnabled(false);
                close();


                break;
            }
            case R.id.reject:
            {
                PendingClass pc=new PendingClass(true,false,TrialActivity.mUsername);
                dbPendingArticle.child(key).setValue(pc);
                approveButton.setEnabled(false);
                saveEditorButton.setEnabled(false);
                //TODO REMOVE FROM DB
                close();
               break;
            }
            case R.id.saveEditor:
            {
                dbRefMsg.child(key).setValue(rbd);
                break;
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void addPublishedDatabase() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        Log.d("checkDate",dateString);
        publishedRef.child(key).child("dateCreated").setValue(dateString);
    }

    private void changeUserDB() {
        Log.d("writerId",writerId);
        dbRefUser.child(writerId).child("articleStatus").child(key).setValue("published");


    }

    private void close() {
        Intent intent=new Intent(this,TrialActivity.class);
        startActivity(intent);
    }
    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICK && resultcode == RESULT_OK) {
            final Uri SelectedImageUri = data.getData();
            if (content.toString() != null) {
                StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
                photoref.putFile(SelectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        try {
                            if (rbd != null) {
                                rbd.setPhoto(downloadUrl.toString());

                            }
                            saveEditorButton.setEnabled(true);


                        } catch (NullPointerException e) {
                            Log.d("exception", "" + e);
                        }
                    }
                });
                Toast.makeText(this, "Photo uploaded", Toast.LENGTH_LONG).show();


            }
        }
    }


}
