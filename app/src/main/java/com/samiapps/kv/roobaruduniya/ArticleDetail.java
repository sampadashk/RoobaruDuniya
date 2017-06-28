package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by KV on 23/6/17.
 */

public class ArticleDetail extends AppCompatActivity {
    int pos;
    RoobaruDuniya artsel;
    String userEmail;
    String userName;
    String userProf;
    ImageView ivw;
    TextView tvtitle;
    TextView tvcontent;
    TextView txtName,txtStatus;
    ImageView imgProfile;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // ArrayList<RoobaruDuniya> rbd;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
   // FirebaseAuth.getInstance().
        Intent intent=getIntent();
        pos=intent.getIntExtra("position",-1);
        Log.d("checkpos",""+pos);
        artsel=(RoobaruDuniya)intent.getSerializableExtra(HomeFragment.TAG);
        userEmail=artsel.getEmail();
        userName=artsel.getUser();
       // Log.d("chkname",userName);
        userProf=artsel.getUserProfilePhoto();

        txtName = (TextView) findViewById(R.id.name);
        txtStatus = (TextView) findViewById(R.id.user_status);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        ivw=(ImageView) findViewById(R.id.display_image);
        tvtitle=(TextView)findViewById(R.id.post_title);
        tvcontent=(TextView)findViewById(R.id.post_con);

        Glide.with(this).load(artsel.getPhoto()).into(ivw);
        tvtitle.setText(artsel.getTitle());
        tvcontent.setText(artsel.getContent());
        txtName.setText(userName);
        Uri uri=Uri.parse(userProf);
        // Loading profile image
        Glide.with(this).load(uri)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);


        // Log.d("chkobj",""+artsel);



    }
}
