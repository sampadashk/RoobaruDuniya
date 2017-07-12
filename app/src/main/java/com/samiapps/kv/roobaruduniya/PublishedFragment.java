package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.samiapps.kv.roobaruduniya.TrialActivity.activityTitles;
import static com.samiapps.kv.roobaruduniya.TrialActivity.navItemIndex;

/**
 * Created by KV on 21/6/17.
 */

public class PublishedFragment extends Fragment {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;
    private DatabaseReference msgReference;
    private ChildEventListener msgListener;
    private ChildEventListener userListener;


    private imgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubarup = new ArrayList<RoobaruDuniya>();
    ArrayList<String> keyList ;


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    ArrayList<String> childkey;
    public static final String TAG = PublishedFragment.class.getName();
    // ProgressBar pgbar;
    // TextView noDraftText;

    public PublishedFragment() {
        super();
    }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("user").child(uid).child("articleStatus");
        // dbaseReference = firebaseDtabase.getReference().child("user").child(uid);
        msgReference = firebaseDtabase.getReference().child("messages");
        keyList=new ArrayList<>();


        Log.d("checkt", dbaseReference.toString());
        Log.d("msgkt", msgReference.toString());
        Log.d("actchk","1");



        //setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        imageAdapter = new imgAdapter(rubarup, getContext());
        //   pgbar=(ProgressBar) rootView.findViewById(R.id.pbar);
        //  noDraftText=(TextView) rootView.findViewById(R.id.nodraftText);
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        Log.d("actchk","2");

        mRecycleView.setAdapter(imageAdapter);
        //FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

       // readmsgId();
        imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                RoobaruDuniya item = rubarup.get(position);
                Intent intent = new Intent(getContext(), ArticleDetail.class);
                String key=keyList.get(position);
                intent.putExtra("keySelected",key);
                intent.putExtra("position", position);
                Bundle b=new Bundle();
                b.putSerializable(ArticleDetail.TAG,item);
                intent.putExtras(b);


                //intent.putExtra("article",rd);

                startActivity(intent);
            }
        });






        return rootView;
    }
    //onActivity is called after oncreateview;we are calling readmsgId here,so that it doesnt display data multiple time as we are checking if the bundle is not null
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(activityTitles[navItemIndex]);
        if(savedInstanceState!=null)
        {

        }
        else {
            readmsgId();
        }

    }

    private void checkMessages(String key) {
        Log.d("actchk","4");
        keyList.add(key);


        msgReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                Log.d("titleck", rbd.getTitle());
                rubarup.add(rbd);
                imageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void readmsgId()
    {

        Log.d("actchk","3");

        dbaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Log.d("actchk", "dbchk");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("valck",ds.getValue()+"");
                        if (ds.getValue().equals("published")) {

                            //msgList.add(ds.getKey());
                            Log.d("keyck", ds.getKey());
                            checkMessages(ds.getKey());


                        }


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );






        // checkMessages();


    }
    public void onStart()
    {
        super.onStart();
        Log.d("actchk","5");
        //readmsgId();


    }
    public void onResume()
    {
        super.onResume();
        Log.d("actchk","6");

    }

    public void onPause() {

        Log.d("actchk","7");
        super.onPause();




    }
    public void onDestroy()
    {
        super.onDestroy();
        rubarup.clear();
    }
    public void onStop()
    {
        super.onStop();
        Log.d("actchk","8");

    }


}
