package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by KV on 23/6/17.
 */

public class ArticleDetail extends AppCompatActivity {
    int pos;
    public static final String TAG = ArticleDetail.class.getName();
    RoobaruDuniya artsel;
    RecyclerView cmtrecyclerV;
    ValueEventListener commentListener;
    ValueEventListener notificationListener;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView num_Of_likes;

    String userName;
    String userProf;
    ImageView ivw;
    TextView tvtitle;
    TextView tvcontent;
    TextView txtName, txtStatus;
    ImageView imgProfile;
    ImageButton sharedButton;
    String date;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FloatingActionButton favButton;
    DatabaseReference publishedRef;
    DatabaseReference msgReference;
    DatabaseReference notificationRef;
    Uri userPhoto;
    String keySel;
    boolean isFav;
    FirebaseDatabase db;
    int nLikes;
    int cNo;
    EditText commentEditText;
    TextView datetvw;
    Button sendCmt;
    ValueEventListener likeListener;
    ValueEventListener msgListener;
    ArrayList<Comment> commentList;
    CommentAdapter commentAdapter;
    private ShareActionProvider mShareActionProvider;
    // ArrayList<RoobaruDuniya> rbd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.detail_layout);


        commentEditText = (EditText) findViewById(R.id.w_comment);
        sendCmt = (Button) findViewById(R.id.send_comment);
        cmtrecyclerV = (RecyclerView) findViewById(R.id.list_comment);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaptool_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        sharedButton = (ImageButton) findViewById(R.id.share);
        txtName = (TextView) findViewById(R.id.name);
        txtStatus = (TextView) findViewById(R.id.user_status);
        num_Of_likes = (TextView) findViewById(R.id.num_likes);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        ivw = (ImageView) findViewById(R.id.display_image);
        tvtitle = (TextView) findViewById(R.id.post_title);
        tvcontent = (TextView) findViewById(R.id.post_con);
        favButton = (FloatingActionButton) findViewById(R.id.share_fab);
        datetvw = (TextView) findViewById(R.id.published_date);


        mAuth = FirebaseAuth.getInstance();
//      getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setLogo(R.drawable.roobaru_logo);
        userPhoto = mAuth.getCurrentUser().getPhotoUrl();
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        cmtrecyclerV.setLayoutManager(layoutManager);
        cmtrecyclerV.setHasFixedSize(false);

        cmtrecyclerV.setAdapter(commentAdapter);

        db = FirebaseDatabase.getInstance();

        publishedRef = db.getReference("published");
        notificationRef = db.getReference("notification");
        notificationRef.keepSynced(true);
        msgReference = db.getReference("messages");
        msgReference.keepSynced(true);




        // FirebaseAuth.getInstance().

        sharedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareintent = new Intent(Intent.ACTION_SEND);
                shareintent.setType("text/plain");


                // shareintent.putExtra(Intent.EXTRA_TEXT, artsel.getContent());
                shareintent.putExtra(android.content.Intent.EXTRA_SUBJECT, artsel.getTitle());
                shareintent.putExtra(android.content.Intent.EXTRA_TEXT, artsel.getContent());
                // shareintent.putExtra(Intent.EXTRA_TEXT, s1);
                // String sendmsg = s1 + uri;
                shareintent.putExtra(Intent.EXTRA_TEXT, artsel.getContent());
                startActivity(Intent.createChooser(shareintent, "Share using"));


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO ASYNC in msgreference
        Intent intent = getIntent();
        if (intent.getStringExtra("intentNotification") != null) {
            NotificationJson obj = (NotificationJson) intent.getSerializableExtra("NotificationObject");
            Log.d("intentchk", "notification");
            keySel = obj.getMsg_id();
            Log.d("chkrecikey", keySel);
            Log.d("chkurl", "https://roobaru-duniya-86f7d.firebaseio.com/messages/" + keySel);

            msgListener = msgReference.child(keySel).addValueEventListener(new ValueEventListener() {
                                                                               @Override
                                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                   artsel = dataSnapshot.getValue(RoobaruDuniya.class);
                                                                                   //updating UI when you have article
                                                                                   loadUI();
                                                                                   Log.d("artchktit", artsel.getTitle());
                                                                               }

                                                                               @Override
                                                                               public void onCancelled(DatabaseError databaseError) {
                                                                                   Log.d("dberror", databaseError.toString());
                                                                               }
                                                                           });






//                Log.d("titleck", rbd.getTitle());

        } else {
            pos = intent.getIntExtra("position", -1);
            //  Log.d("checkpos", "" + pos);
            artsel = (RoobaruDuniya) intent.getSerializableExtra(ArticleDetail.TAG);
            keySel = intent.getStringExtra("keySelected");
            loadUI();
            //   Log.d("keysel", keySel);
        }
        //Deleting comments;
        commentAdapter.setOnItemClickListener(new CommentAdapter.ClickListener()
        {

            @Override
            public void onItemClick(final int position, View v) {
                Comment c=commentList.get(position);
                //only give delete right to the person who wrote the comment;Than only delete button will be visisble
                if(c.commentorName.equals(TrialActivity.mUsername))
                {
                    ImageButton deleteButton= (ImageButton) v.findViewById(R.id.delete_button);
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Comment cmt=commentList.get(position);
                            commentList.remove(cmt);  // remove the item from list
                         commentAdapter.notifyItemRemoved(position);
                            //deleteing from firebase
                            publishedRef.child(keySel).child("comments").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                    int length = (int) dataSnapshot.getChildrenCount();
                                    int i = 0;
                                    // String[] sampleString = new String[length];
                                    //TODO DELETE
                                    while (i < position) {
                                        if(iterator.hasNext()) {
                                            iterator.next();
                                            i += 1;
                                        }
                                    }
                                    try {
                                        //get the key using iterator and than delete it

                                        String key = iterator.next().getKey();
                                        Log.d("keydel", key);
                                        dataSnapshot.child(key).getRef().removeValue();
                                        Toast.makeText(ArticleDetail.this,"Comment deleted",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    catch(NoSuchElementException e)
                                    {
                                        e.printStackTrace();
                                    }
                                    catch(Exception ee)
                                    {
                                        ee.printStackTrace();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    });

                }
            }
        });
        FavDb favRef = new FavDb(ArticleDetail.this);
        SQLiteDatabase sqldb = favRef.getReadableDatabase();
        Cursor cursor = favRef.queryKey(sqldb, keySel);
        if (cursor.getCount() > 0) {
            favButton.setImageResource(R.drawable.ic_favorite);
            isFav = true;
        }
        likeListener = publishedRef.child(keySel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    date = dataSnapshot.child("dateCreated").getValue().toString();
                    datetvw.setText(date);
                    Log.d("ckdate", date);
                    if (dataSnapshot.child("likes").exists()) {
                        String n_Likes = dataSnapshot.child("likes").getValue().toString();
                        nLikes = Integer.parseInt(n_Likes);


                        num_Of_likes.setText(n_Likes + "likes");
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Log.d("chkobj",""+artsel);


        if (commentListener == null) {
            commentListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

//clear the arraylist before display so that duplicate result is not printed
                    commentList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {


                            Comment c = ds.getValue(Comment.class);
                            commentList.add(c);
                            Log.d("checkcname", c.commentorName);
                            Log.d("checkcmt", c.comment);
                            commentAdapter.notifyDataSetChanged();
                        }


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            publishedRef.child(keySel).child("comments").addValueEventListener(commentListener);
        }


       /* publishedRef.child(keySel).child("comments").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        Comment c = ds.getValue(Comment.class);
                        commentList.add(c);
                        Log.d("checkcname", c.commentorName);
                        Log.d("checkcmt", c.comment);
                        commentAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendCmt.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEditText.getText().toString();
                String cName = TrialActivity.mUsername;
                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = sdf.format(date);
                try {
                    if (userPhoto == null) {

                        String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
                        userPhoto = Uri.parse("https://" + add);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


                Comment c = new Comment(cName, comment, dateString, userPhoto.toString());
                //commentList.add(c);
                publishedRef.child(keySel).child("comments").push().setValue(c);
                HashMap<String, String> notificationData = new HashMap<String, String>();
                notificationData.put("from", mAuth.getCurrentUser().getUid());
                notificationData.put("type", "comment");
                checkValue();


                notificationData.put("commentNo", Integer.toString(cNo + 1));
                notificationRef.child(artsel.getuserId()).child(keySel).setValue(notificationData);

                sendCmt.setEnabled(false);
                commentEditText.clearFocus();
                commentEditText.setText("");


            }
        });
    }
    public void loadUI()
    {
        if(artsel!=null) {

            collapsingToolbarLayout.setTitle(artsel.getTitle());


            userName = artsel.getUser();

            // Log.d("chkname",userName);
            userProf = artsel.getUserProfilePhoto();
            Glide.with(this).load(artsel.getPhoto()).into(ivw);
            tvtitle.setText(artsel.getTitle());
            tvcontent.setText(artsel.getContent());
            txtName.setText(userName);
            Uri uri = Uri.parse(userProf);
            // Loading profile image
            Glide.with(this).load(uri)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }

    }
    @Override
    public void onResume()
    {
        super.onResume();



           /* publishedRef.child(keySel).child("dateCreated").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        date = dataSnapshot.getValue().toString();
                        Log.d("ckdate", date);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */





            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FavDb favdbRef = new FavDb(ArticleDetail.this);
                    SQLiteDatabase db = favdbRef.getWritableDatabase();
                 /*   publishedRef.child(keySel).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue().toString();
                            nLikes = Integer.parseInt(value);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    */
                    if (isFav) {
                        favdbRef.deleteKey(db, keySel);
                        favButton.setImageResource(R.drawable.ic_favorite_border);
                        isFav = false;
                        if (nLikes > 0) {
                            nLikes -= 1;
                        }

                    } else {
                        favdbRef.insertKey(db, keySel);
                        favButton.setImageResource(R.drawable.ic_favorite);
                        HashMap<String, String> notificationData = new HashMap<String, String>();
                        notificationData.put("from", mAuth.getCurrentUser().getUid());
                        notificationData.put("type", "likes");
                        notificationRef.child(artsel.getuserId()).child(keySel).setValue(notificationData);
                        isFav = true;
                        nLikes += 1;
                    }
                    publishedRef.child(keySel).child("likes").setValue(nLikes);

                }
            });



    }
    public void onBackPressed() {
        // super.onBackPressed();

            super.onBackPressed();

            //  TrialActivity.shouldLoadHomeFragOnBackPress=false;
            // getFragmentManager().popBackStackImmediate();
            //  finish();
//        getSupportActionBar().setTitle(activityTitles[navItemIndex]);


        }


    private void checkValue() {
        notificationListener = notificationRef.child(artsel.getuserId()).child(keySel).child("commentNo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sres = dataSnapshot.getValue().toString();

                    cNo = Integer.parseInt(sres);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* if (notificationListener == null) {
            notificationListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String sres = (String) dataSnapshot.getValue();

                        cNo = Integer.parseInt(sres);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            notificationRef.child(artsel.getuserId()).child(keySel).child("commentNo").addValueEventListener(notificationListener);
        }
        */

    }

    /*
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
    */
    public void onDestroy() {
        super.onDestroy();
        commentList.clear();
        if (commentListener != null) {
            publishedRef.child(keySel).child("comments")
                    .removeEventListener(commentListener);
            commentListener = null;
        }
        if (notificationListener != null) {
            notificationRef.child(artsel.getuserId()).child(keySel).removeEventListener(notificationListener);
            notificationRef = null;
        }
        if (likeListener != null) {
            publishedRef.child(keySel).removeEventListener(likeListener);
            likeListener = null;
        }
        if (msgListener != null) {
            msgReference.child(keySel).removeEventListener(msgListener);
            msgListener = null;
        }
    }



    }



