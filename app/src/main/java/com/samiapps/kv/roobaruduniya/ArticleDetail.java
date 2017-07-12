package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    public static final String TAG=ArticleDetail.class.getName();
    RoobaruDuniya artsel;

    String userName;
    String userProf;
    ImageView ivw;
    TextView tvtitle;
    TextView tvcontent;
    TextView txtName,txtStatus;
    ImageView imgProfile;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FloatingActionButton favButton;
    String keySel;
    boolean isFav;
    private ShareActionProvider mShareActionProvider;
    // ArrayList<RoobaruDuniya> rbd;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
   // FirebaseAuth.getInstance().
        Intent intent=getIntent();
        pos=intent.getIntExtra("position",-1);
        Log.d("checkpos",""+pos);
        artsel=(RoobaruDuniya)intent.getSerializableExtra(ArticleDetail.TAG);
        keySel=intent.getStringExtra("keySelected");
        Log.d("keysel",keySel);


        userName=artsel.getUser();
       // Log.d("chkname",userName);
        userProf=artsel.getUserProfilePhoto();

        txtName = (TextView) findViewById(R.id.name);
        txtStatus = (TextView) findViewById(R.id.user_status);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        ivw=(ImageView) findViewById(R.id.display_image);
        tvtitle=(TextView)findViewById(R.id.post_title);
        tvcontent=(TextView)findViewById(R.id.post_con);
        favButton=(FloatingActionButton) findViewById(R.id.share_fab);
        FavDb favRef=new FavDb(ArticleDetail.this);
        SQLiteDatabase sqldb=favRef.getReadableDatabase();
        Cursor cursor=favRef.queryKey(sqldb,keySel);
        if(cursor.getCount()>0)
        {
            favButton.setImageResource(R.drawable.ic_favorite);
            isFav=true;
        }




        Glide.with(this).load(artsel.getPhoto()).into(ivw);
        tvtitle.setText(artsel.getTitle());
        tvcontent.setText(artsel.getContent());
        txtName.setText(userName);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavDb favdbRef=new FavDb(ArticleDetail.this);
                SQLiteDatabase db=favdbRef.getWritableDatabase();
                if(isFav)
                {
                    favdbRef.deleteKey(db,keySel);
                    favButton.setImageResource(R.drawable.ic_favorite_border);
                    isFav=false;
                }
                else
                {
                    favdbRef.insertKey(db,keySel);
                    favButton.setImageResource(R.drawable.ic_favorite);
                    isFav=true;
                }
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//TODO sharing in FB
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //create the sharing intent

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "here goes your share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, artsel.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, artsel.getContent());

        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }


}
