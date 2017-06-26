package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by KV on 23/6/17.
 */

public class ArticleDetail extends AppCompatActivity {
    int pos;
    RoobaruDuniya artsel;
    ImageView ivw;
    TextView tvtitle;
    TextView tvcontent;
    TextView txtName,txtStatus;
    ImageView imgProfile;
    // ArrayList<RoobaruDuniya> rbd;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Intent intent=getIntent();
        pos=intent.getIntExtra("position",-1);
        Log.d("checkpos",""+pos);
        artsel=(RoobaruDuniya)intent.getSerializableExtra(HomeFragment.TAG);
        txtName = (TextView) findViewById(R.id.name);
        txtStatus = (TextView) findViewById(R.id.user_status);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        ivw=(ImageView) findViewById(R.id.display_image);
        tvtitle=(TextView)findViewById(R.id.post_title);
        tvcontent=(TextView)findViewById(R.id.post_con);

        Glide.with(this).load(artsel.getPhoto()).into(ivw);
        tvtitle.setText(artsel.getTitle());
        tvcontent.setText(artsel.getContent());


        // Log.d("chkobj",""+artsel);



    }
}
