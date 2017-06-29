package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    int pos;
    String key;
    RoobaruDuniya rbd;
    MenuItem approveButton;
    MenuItem saveEditorButton;
    MenuItem rejectButton;
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
        Intent intent=getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key=intent.getStringExtra("Keypos");
            Log.d("keypos",key);

            Log.d("checkpos", "" + pos);
            rbd= (RoobaruDuniya) intent.getSerializableExtra(SentFragment.TAG);
            title.setText(rbd.getTitle());
            content.setText(rbd.getContent());
            //draftPressed+=1;
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

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
                //CHANGE VALUE OF ARTICLE STATUS IN USERDB TO PUBLISHED
                changeUserDB();
                approveButton.setEnabled(false);
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
                close();
               break;
            }
            case R.id.saveEditor:
            {
                break;
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void changeUserDB() {

    }

    private void close() {
        Intent intent=new Intent(this,TrialActivity.class);
        startActivity(intent);
    }


}
