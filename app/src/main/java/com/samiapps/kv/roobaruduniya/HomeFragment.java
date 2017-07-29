package com.samiapps.kv.roobaruduniya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by KV on 20/6/17.
 */

public class HomeFragment extends Fragment  {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;
    private DatabaseReference publishedRef;
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

    public static final String TAG=DraftFragment.class.getName();
    public HomeFragment() {
        super();
    }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        column=calculateNoOfColumns(getContext());


        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("messages");
        publishedRef=firebaseDtabase.getReference("published");
        getContext().registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        Log.d("checkt",dbaseReference.toString());






        //setHasOptionsMenu(true);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        imageAdapter=new imgAdapter(rubaru,getContext());
       //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
       //sglm.setReverseLayout(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),column);
        //to display in reverse order;setreverselayout(true)
        gridLayoutManager.setReverseLayout(true);

        mRecycleView.setLayoutManager(gridLayoutManager);
        error= (TextView) rootView.findViewById(R.id.error);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        homeLayout=(LinearLayout) rootView.findViewById(R.id.main_container);



        mRecycleView.setAdapter(imageAdapter);

        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // mRecycleView.setHasFixedSize(true);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        checkPublishedKey();
        //displayArticles();



           imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        RoobaruDuniya item = rubaru.get(position);
                        String key=keys.get(position);
                        Log.d("keyselected",key);
                        Intent intent = new Intent(getContext(), ArticleDetail.class);
                        intent.putExtra("position", position);
                        intent.putExtra("keySelected",key);
                        Bundle b=new Bundle();
                        b.putSerializable(ArticleDetail.TAG,item);
                        intent.putExtras(b);


                        //intent.putExtra("article",rd);

                        startActivity(intent);
                    }
                });





        // Spinner spinner = (Spinner) rootView.findViewById(R.id.profile_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout

// Specify the layout to use when the list of choices appears
        ;





       /* childkey=new ArrayList<String>();
        dbaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key=postSnapshot.getKey();
                    childkey.add(key);
                    Log.d("chkkey",key);
                }
                Log.d("keylist",childkey.get(0));



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        return rootView;
    }
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    private void checkPublishedKey() {
        //checking the published key in published node
        //to retrieve 30 nodes
        Query query = publishedRef.orderByKey().limitToLast(30);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        displayArticles(postSnapshot.getKey());


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayArticles(String key) {
        keys.add(key);
        dbaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
//                Log.d("titleck", rbd.getTitle());

                rubaru.add(rbd);

                mLoadingIndicator.setVisibility(View.INVISIBLE);

                imageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



      /*  dbaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("chkcount", String.valueOf(dataSnapshot.getChildrenCount()));
                // ArrayList<RoobaruDuniya> rubaru=new ArrayList<RoobaruDuniya>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    RoobaruDuniya rbd = postSnapshot.getValue(RoobaruDuniya.class);

                    Log.d("checktitle", rbd.getTitle());
                    rubaru.add(rbd);
                    imageAdapter.notifyDataSetChanged();
                    Log.d("rub", " " + rubaru);


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        */
    }

    public void onStart() {
        super.onStart();

    }



    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("actck","stop");


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("actck","pause");
    }
    public boolean isNetworkUp()
    {
        ConnectivityManager cm= (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
    private BroadcastReceiver MyReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent){
            if (!isNetworkUp()) {
                Log.d("hi","show");
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
                        } else error.setText(R.string.no_data);
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

            getContext().unregisterReceiver(MyReceiver);
            MyReceiver=null;
        }
        rubaru.clear();
        super.onDestroy();
    }

/*
    @Override
    public void onRefresh() {


        if (!isNetworkUp() && imageAdapter.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            error.setText(getString(R.string.error_no_network));
            error.setVisibility(View.VISIBLE);
        } else if (!isNetworkUp()) {
            swipeRefreshLayout.setRefreshing(false);
            error.setText(getString(R.string.error_no_network));
        } else {
            error.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            checkPublishedKey();
        }


    }
    */
}
