package com.samiapps.kv.roobaruduniya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by KV on 13/8/17.
 */

public class CategoryActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;

    private DatabaseReference publishedRef;
    private DatabaseReference categoryRef;
    Snackbar snackbar;
    LinearLayout homeLayout;
    ProgressBar mLoadingIndicator;

    TextView error;
    int msg;
    int column;







    private imgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru=new ArrayList<RoobaruDuniya>();
    ArrayList<String> keys=new ArrayList<>();


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    String category;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headlinelayout);

        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("messages");
        publishedRef=firebaseDtabase.getReference("published");
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView) findViewById(R.id.editor_recycleview);
        categoryRef = firebaseDtabase.getReference("categories");

        imageAdapter=new imgAdapter(rubaru,this);
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //sglm.setReverseLayout(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        //to display in reverse order;setreverselayout(true)



        mRecycleView.setLayoutManager(gridLayoutManager);

        error= (TextView) findViewById(R.id.error);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        homeLayout=(LinearLayout) findViewById(R.id.main_container);



        mRecycleView.setAdapter(imageAdapter);
        Intent intent=getIntent();
       category=intent.getStringExtra("articlecat");
        Log.d("categorycheck",category);
        setTitle(category);
        getArticleByCategory();
        imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                RoobaruDuniya item = rubaru.get(position);
                String key=keys.get(position);
                Intent intent = new Intent(CategoryActivity.this, ArticleDetail.class);
                intent.putExtra("position", position);
                intent.putExtra("keySelected",key);
                Bundle b=new Bundle();
                b.putSerializable(ArticleDetail.TAG,item);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // mRecycleView.setHasFixedSize(true);
        mLoadingIndicator.setVisibility(View.VISIBLE);
       registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void getArticleByCategory() {
      // Query q= categoryRef.child(category).orderByChild("timestamp").limitToFirst(15);

        categoryRef.child(category).orderByKey().limitToLast(20).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    //keys.add(null);


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        Log.d("ckk",key);
                        keys.add(key);
                       getMessageData(key);

                        error.setVisibility(View.GONE);

                        mLoadingIndicator.setVisibility(View.INVISIBLE);

                       // imageAdapter.notifyDataSetChanged();




                        // displayArticles(postSnapshot.getKey());


                    }





                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private void getMessageData(String key) {
       dbaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);

                 Log.d("titleck", rbd.getTitle());
                rubaru.add(rbd);
                imageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public boolean isNetworkUp()
    {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    private BroadcastReceiver MyReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent){
            if (!isNetworkUp()) {
                // Log.d("hi","show");
                snackbar = Snackbar.make(homeLayout,
                        getString(R.string.error_no_network),
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
            else
            {

                if (snackbar != null) snackbar.dismiss();
                emptyView();
            }
            //  snackbar.setActionTextColor(getResources().getColor(R.color.material_red_700));



        }
    };
    public void emptyView()
    {

        if(imageAdapter.getItemCount()==0)

        {
            if (!isNetworkUp()) {
                msg = R.string.error_no_network;
            }
            else {
                publishedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            error.setText(R.string.wait_msg);
                        } else
                            error.setText(R.string.no_data);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



            // error.setText(msg);
            error.setVisibility(View.VISIBLE);
        } else {

        }



    }





    @Override
    public void onDestroy() {
        if(MyReceiver!=null) {

            unregisterReceiver(MyReceiver);
            MyReceiver=null;
        }
        rubaru.clear();
        super.onDestroy();
    }

}