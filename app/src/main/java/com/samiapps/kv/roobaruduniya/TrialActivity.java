package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class TrialActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 123;
    Uri photoUri;
    private View navHeader;
    private String[] activityTitles;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtStatus;
    private Toolbar toolbar;
    String uname;
    String uemail;
    private Handler mHandler;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String userStatus;

    // tags used to attach the fragments
    private static final String TAG=TrialActivity.class.getName();
    private static final String TAG_HOME = "home";
    private static final String TAG_FAV = "favorites";
    private static final String TAG_DRAFTS = "drafts";
    private static final String TAG_PUBLISHED = "published";
    private static final String TAG_SENT = "sent";
    public static String CURRENT_TAG = TAG_HOME;
    boolean shouldLoadHomeFragOnBackPress=true;
    FloatingActionButton fab;
    public static boolean isEditor;
    FirebaseDatabase firebaseDtabase;
    DatabaseReference dbEditor;
    NavigationView navigationView;
    MenuItem sentart;
    ActionBarDrawerToggle toggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"ONCREATE 1");
        setContentView(R.layout.activity_trial);
        //postponeEnterTransition();
        mUsername = ANONYMOUS;
        userStatus="Blogger";
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                // String s=firebaseAuth.getCurrentUser().getUid();
                if(user!=null)
                {
                    Log.d(TAG,"Signed in 3");


                    onSignedInInitialize(user.getDisplayName(),user.getEmail(),user.getPhotoUrl());

                    Toast.makeText(TrialActivity.this,"Welcome!",Toast.LENGTH_LONG).show();

                }
                else
                {
                    Log.d(TAG,"Signed in 2");
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),

                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()) ).setTheme(R.style.FullscreenTheme)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };



        firebaseDtabase=FirebaseDatabase.getInstance();
     //   Logger.Level debugLevel = Logger.Level.valueOf("DEBUG");


        dbEditor=firebaseDtabase.getReference("editor");
       // mAuth.addAuthStateListener(mAuthListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        //send email to roobaru.duniya@gmail.com
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email ="roobaru.duniya@gmail.com";
                Intent intent=new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:" +email));
              //  intent.putExtra(Intent.EXTRA_EMAIL,"roobaru.duniya@gmail.com");
                // intent.setType("text/html");

               // intent.putExtra(Intent.EXTRA_EMAIL,"roobaru.duniya@gmail.com");
              //  intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                //intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navmenu = navigationView.getMenu();
         sentart=navmenu.findItem(R.id.nav_sent);
       // loadNavHeader();

        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtStatus = (TextView) navHeader.findViewById(R.id.user_status);

        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


     /*   if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        */




    }



    private void checkEditor() {
        Log.d(TAG,"checkEditor 4");
        dbEditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot di:dataSnapshot.getChildren())
                {
                    if(di.getValue().equals(uemail))
                    {
                        Log.d("hey", (String) di.getValue());
                        isEditor=true;
                        userStatus="editor";
                        sentart.setTitle(R.string.editor_unpublished);
                        txtStatus.setText("Editor");

                        break;

                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("checkedit",""+isEditor);
        Log.d("checkstatus",userStatus);
    }

    private void loadNavHeader() {
        // name, website

        // loading header background image


        // Loading profile image
        txtStatus.setText(userStatus);
        if(photoUri!=null) {
            Glide.with(this).load(photoUri)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
      //  txtName.setText(uname);


    }


    @Override
    public void onBackPressed() {

       DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(drawer.isDrawerOpen(GravityCompat.START)==false){
            super.onBackPressed();
        }

        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                getSupportActionBar().setTitle(activityTitles[navItemIndex]);
                loadHomeFragment();
                return;
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            //noinspection SimplifiableIfStatement
            case R.id.write_blog: {
                Intent intent = new Intent(this, WriteArticleActivity.class);
                startActivity(intent);
                break;

            }
            case R.id.action_settings: {
                AuthUI.getInstance().signOut(this);
                return true;
            }



        }
        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestcode,int resultcode,Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode==RC_SIGN_IN)
        {
            if(resultcode==RESULT_OK)
            {
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();

            }
            else if(resultcode==RESULT_CANCELED)
            {
                Toast.makeText(this,"Signed OUT!",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG,"nav selected");
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navItemIndex=0;
            CURRENT_TAG = TAG_HOME;

        } else if (id == R.id.nav_favorites) {
            navItemIndex=1;
            CURRENT_TAG=TAG_FAV;

        } else if (id == R.id.nav_draft) {
            navItemIndex=2;
            CURRENT_TAG=TAG_DRAFTS;



        } else if (id == R.id.nav_published) {
            navItemIndex=3;
            CURRENT_TAG=TAG_PUBLISHED;

        } else if (id == R.id.nav_sent) {
            navItemIndex=4;
            CURRENT_TAG=TAG_SENT;

        } else if (id == R.id.nav_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        loadHomeFragment();
        return true;
    }

    private void loadHomeFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        checkEditor();
       // loadNavHeader();
        setToolbarTitle();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();
        invalidateOptionsMenu();



    }

    private void selectNavMenu() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"resume");
   /*    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {
            uname = user.getDisplayName();
            photoUri = user.getPhotoUrl();
            mHandler = new Handler();
            Log.d("cname", uname);
            Log.d("curi", photoUri.toString());
            loadNavHeader();
            checkEditor();
            if(isEditor)
            {
                sentart.setTitle(R.string.editor_unpublished);
                txtStatus.setText("Editor");
            }




        }
        */


        mAuth.addAuthStateListener(mAuthListener);

    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void onStart()
    {
        super.onStart();

        Log.d(TAG,"start");



    }


    private void onSignedInInitialize(String username,String email,Uri photo)
    {
        try {
            mUsername = username;
            uname = username;
            photoUri = photo;
            uemail = email;
            mHandler = new Handler();
            Log.d("cname", uname);
            Log.d("curi", photoUri.toString());



        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        checkEditor();

        loadNavHeader();

       HomeFragment homeFragment = new HomeFragment();

       getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment, CURRENT_TAG).commit();



    }
    private void onSignedOutCleanup()
    {
        mUsername=ANONYMOUS;
        userStatus="Blogger";



    }
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
    private Fragment getHomeFragment() {
        Log.d(TAG,"GETFrag");
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // fav
                FavFragment favFragment=new FavFragment();
                return favFragment;

            case 2:
                // draft fragment
                DraftFragment draftFragment = new DraftFragment();
                return draftFragment;
            case 3:
                // published fragment
                PublishedFragment publishedFragment = new PublishedFragment();
                return publishedFragment;

            case 4:
                // sent fragment
                SentFragment sentFragment = new SentFragment();
                return sentFragment;
            default:
                return new HomeFragment();
        }
    }
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
       toggle.onConfigurationChanged(newConfig);
    }
}
