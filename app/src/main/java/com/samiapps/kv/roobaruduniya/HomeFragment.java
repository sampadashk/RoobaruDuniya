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
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by KV on 20/6/17.
 */

public class HomeFragment extends Fragment {
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


    private CategoryAdapter catdapter;
    ArrayList<DisplayEvent> rubaru = new ArrayList<DisplayEvent>();
    ArrayList<String> keys = new ArrayList<>();


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;

    public static final String TAG = DraftFragment.class.getName();

    public HomeFragment() {
        super();
    }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
        column = calculateNoOfColumns(getContext());


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("messages");
        publishedRef = firebaseDtabase.getReference("published");
        categoryRef = firebaseDtabase.getReference("categories");
        getContext().registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //  Log.d("checkt",dbaseReference.toString());


        //setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        catdapter = new CategoryAdapter(rubaru, getContext());
        //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //sglm.setReverseLayout(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecycleView.setLayoutManager(linearLayoutManager);
        error = (TextView) rootView.findViewById(R.id.error);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        homeLayout = (LinearLayout) rootView.findViewById(R.id.main_container);


        mRecycleView.setAdapter(catdapter);

        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // mRecycleView.setHasFixedSize(true);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        checkPublishedKey();
        //displayArticles();



     catdapter.setOnItemClickListener(new CategoryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.d("ckpos",""+position);

                DisplayEvent item = rubaru.get(position);
                String key=keys.get(position);
                //  Log.d("keyselected",key);
                Intent intent = new Intent(getContext(), ArticleDetail.class);
                intent.putExtra("position", position);
                intent.putExtra("keySelected",key);
              //  Bundle b=new Bundle();
              //  b.putSerializable(ArticleDetail.TAG,item);
               // intent.putExtras(b);


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
        // Query query = publishedRef.orderByKey().limitToLast(30);
        categoryRef.child("रूबरू स्पॉटलाइट").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DisplayEvent e = new DisplayEvent();
                    e.setHeader("रूबरू स्पॉटलाइट");
                    //keys.add(null);
                    ArrayList<HomeDisplay> hmList=new ArrayList<HomeDisplay>();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        Log.d("ckk",key);
                        keys.add(key);
                        HomeDisplay hm = postSnapshot.getValue(HomeDisplay.class);
                       hmList.add(hm);
                        Log.d("ckv",hm.getTitle());




                        // displayArticles(postSnapshot.getKey());


                    }
                    e.setAllItemsInSection(hmList);
                    rubaru.add(e);
                    Log.d("ckrubaru",""+rubaru.size());

                    catdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        categoryRef.child("मैं घुमंतू").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    DisplayEvent e = new DisplayEvent("मैं घुमंतू", null, 0);
                    rubaru.add(e);
                    keys.add(null);

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        keys.add(key);
                        HomeDisplay hm = postSnapshot.getValue(HomeDisplay.class);
                        DisplayEvent ev = new DisplayEvent(null, hm, 1);
                        rubaru.add(ev);
                        catdapter.notifyDataSetChanged();


                        // displayArticles(postSnapshot.getKey());


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        // Log.d("actck","stop");


    }

    @Override
    public void onPause() {
        super.onPause();
        // Log.d("actck","pause");
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

        if(catdapter.getItemCount()==0)

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

            getContext().unregisterReceiver(MyReceiver);
            MyReceiver=null;
        }
        rubaru.clear();
        super.onDestroy();
    }

/*
    @Override
    public void onRefresh() {
        if (!isNetworkUp() && catdapter.getItemCount() == 0) {
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