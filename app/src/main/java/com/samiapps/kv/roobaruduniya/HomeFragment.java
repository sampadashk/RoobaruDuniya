package com.samiapps.kv.roobaruduniya;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by KV on 20/6/17.
 */

public class HomeFragment extends Fragment {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;


    private imgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru=new ArrayList<RoobaruDuniya>();


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    ArrayList<String> childkey;
    public static final String TAG=DraftFragment.class.getName();
    public HomeFragment() {
        super();
    }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("messages");
        Log.d("checkt",dbaseReference.toString());






        //setHasOptionsMenu(true);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);

        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        // mRecycleView.setHasFixedSize(true);

        dbaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("chkcount", String.valueOf(dataSnapshot.getChildrenCount()));
                // ArrayList<RoobaruDuniya> rubaru=new ArrayList<RoobaruDuniya>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    RoobaruDuniya rbd = postSnapshot.getValue(RoobaruDuniya.class);

                    Log.d("checktitle", rbd.getTitle());
                    rubaru.add(rbd);
                    Log.d("rub"," "+rubaru);



                }
                imageAdapter=new imgAdapter(rubaru,getContext());
                mRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2));
                mRecycleView.setItemAnimator(new DefaultItemAnimator());

                mRecycleView.setAdapter(imageAdapter);
              /*  imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {

                        RoobaruDuniya item = rubaru.get(position);
                        Intent intent = new Intent(getContext(), ArticleDetail.class);
                        intent.putExtra("position", position);
                        Bundle b=new Bundle();
                        b.putSerializable(articlePage.TAG,item);
                        intent.putExtras(b);


                        //intent.putExtra("article",rd);

                        startActivity(intent);
                    }
                });
                */

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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

    public void onStart() {
        super.onStart();
    }



    @Override
    public void onResume() {
        super.onResume();







    }
}
