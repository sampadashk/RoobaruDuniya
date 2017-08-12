/*package com.samiapps.kv.roobaruduniya;

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
 * Created by KV on 9/8/17.
 */

/*public class trialCateg {

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

  /*  public class HomeFragment extends Fragment {
        private FirebaseDatabase firebaseDtabase;
        private DatabaseReference dbaseReference;
        private DatabaseReference publishedRef;
        Snackbar snackbar;
        LinearLayout homeLayout;
        ProgressBar mLoadingIndicator;

        TextView error;
        int msg;
        int column;







        private imgAdapter imageAdapter1;
        private imgAdapter imageAdapter2;
        private imgAdapter imageAdapter3;
        private imgAdapter imageAdapter4;
        private imgAdapter imageAdapter5;
        private imgAdapter imageAdapter6;
        private imgAdapter imageAdapter7;
        private imgAdapter imageAdapter8;
        private imgAdapter imageAdapter9;
        private imgAdapter imageAdapter10;
        private imgAdapter imageAdapter11;
        private imgAdapter imageAdapter12;
        private imgAdapter imageAdapter13;
        private imgAdapter imageAdapter14;
        ArrayList<RoobaruDuniya> rubaruc1=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc2=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc3=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc4=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc5=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc6=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc7=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc8=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc9=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc10=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc11=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc12=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc13=new ArrayList<RoobaruDuniya>();
        ArrayList<RoobaruDuniya> rubaruc14=new ArrayList<RoobaruDuniya>();
        ArrayList<String> keys=new ArrayList<>();
        private RecyclerView rvw[];


        private RecyclerView mRecycleView1;
        private RecyclerView mRecycleView2;
        private RecyclerView mRecycleView3;
        private RecyclerView mRecycleView4;
        private RecyclerView mRecycleView5;
        private RecyclerView mRecycleView6;
        private RecyclerView mRecycleView7;
        private RecyclerView mRecycleView8;
        private RecyclerView mRecycleView9;
        private RecyclerView mRecycleView10;
        private RecyclerView mRecycleView11;
        private RecyclerView mRecycleView12;
        private RecyclerView mRecycleView13;
        private RecyclerView mRecycleView14;


        private RecyclerView.LayoutManager mLayoutManager;
        String uid;

        public static final String TAG=DraftFragment.class.getName();
        public HomeFragment() {
            super();
        }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;



        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // setRetainInstance(true);
            column=calculateNoOfColumns(getContext());


            uid= FirebaseAuth.getInstance().getCurrentUser().getUid();

            firebaseDtabase = FirebaseDatabase.getInstance();
            dbaseReference = firebaseDtabase.getReference().child("messages");
            publishedRef=firebaseDtabase.getReference("published");
            getContext().registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


            //  Log.d("checkt",dbaseReference.toString());






            //setHasOptionsMenu(true);

        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.front_page, container, false);
            mLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);

            mRecycleView1 = (RecyclerView) rootView.findViewById(R.id.category1);
            mRecycleView2 = (RecyclerView) rootView.findViewById(R.id.category2);
            mRecycleView3 = (RecyclerView) rootView.findViewById(R.id.category3);
            mRecycleView4 = (RecyclerView) rootView.findViewById(R.id.category4);
            mRecycleView5 = (RecyclerView) rootView.findViewById(R.id.category5);
            mRecycleView6 = (RecyclerView) rootView.findViewById(R.id.category6);
            mRecycleView7 = (RecyclerView) rootView.findViewById(R.id.category7);
            mRecycleView8 = (RecyclerView) rootView.findViewById(R.id.category8);
            mRecycleView9 = (RecyclerView) rootView.findViewById(R.id.category9);
            mRecycleView10 = (RecyclerView) rootView.findViewById(R.id.category10);
            mRecycleView11 = (RecyclerView) rootView.findViewById(R.id.category11);
            mRecycleView12 = (RecyclerView) rootView.findViewById(R.id.category12);
            mRecycleView13 = (RecyclerView) rootView.findViewById(R.id.category13);
            mRecycleView14 = (RecyclerView) rootView.findViewById(R.id.category14);
            imageAdapter1=new imgAdapter(rubaruc1,getContext());
            imageAdapter2=new imgAdapter(rubaruc2,getContext());
            imageAdapter3=new imgAdapter(rubaruc3,getContext());
            imageAdapter4=new imgAdapter(rubaruc4,getContext());
            imageAdapter5=new imgAdapter(rubaruc5,getContext());
            imageAdapter6=new imgAdapter(rubaruc6,getContext());
            imageAdapter7=new imgAdapter(rubaruc7,getContext());
            imageAdapter8=new imgAdapter(rubaruc8,getContext());
            imageAdapter9=new imgAdapter(rubaruc9,getContext());
            imageAdapter10=new imgAdapter(rubaruc10,getContext());
            imageAdapter11=new imgAdapter(rubaruc11,getContext());
            imageAdapter12=new imgAdapter(rubaruc12,getContext());
            imageAdapter13=new imgAdapter(rubaruc13,getContext());
            imageAdapter14=new imgAdapter(rubaruc14,getContext());
            //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            //sglm.setReverseLayout(true);
            LinearLayoutManager linLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
            //to display in reverse order;setreverselayout(true)
            linLayoutManager.setReverseLayout(true);
            linLayoutManager.setStackFromEnd(true);


            mRecycleView1.setLayoutManager(linLayoutManager);
            mRecycleView1.setItemAnimator(new DefaultItemAnimator());
            error= (TextView) rootView.findViewById(R.id.error);

            homeLayout=(LinearLayout) rootView.findViewById(R.id.main_container);



            mRecycleView1.setAdapter(imageAdapter1);
            mRecycleView2.setAdapter(imageAdapter2);
            mRecycleView3.setAdapter(imageAdapter3);
            mRecycleView4.setAdapter(imageAdapter4);
            mRecycleView5.setAdapter(imageAdapter5);
            mRecycleView6.setAdapter(imageAdapter6);
            mRecycleView7.setAdapter(imageAdapter7);
            mRecycleView8.setAdapter(imageAdapter8);
            mRecycleView9.setAdapter(imageAdapter9);
            mRecycleView10.setAdapter(imageAdapter10);
            mRecycleView11.setAdapter(imageAdapter11);
            mRecycleView12.setAdapter(imageAdapter12);
            mRecycleView13.setAdapter(imageAdapter13);
            mRecycleView14.setAdapter(imageAdapter14);

            // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            // mRecycleView.setHasFixedSize(true);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            checkPublishedKey();
            //displayArticles();

/*

           imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        RoobaruDuniya item = rubaru.get(position);
                        String key=keys.get(position);
                      //  Log.d("keyselected",key);
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
                */





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


   /*         return rootView;
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
                    error.setVisibility(View.GONE);

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
    /*    }

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
  /*  }
}
*/
