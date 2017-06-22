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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by KV on 21/6/17.
 */

public class DraftFragment extends Fragment {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;
    private DatabaseReference msgReference;
    private ValueEventListener msgListener;
    private ChildEventListener userListener;


    private imgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru = new ArrayList<RoobaruDuniya>();
    ArrayList<String> msgList = new ArrayList<String>();


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    ArrayList<String> childkey;
    public static final String TAG = DraftFragment.class.getName();

    public DraftFragment() {
        super();
    }

//    FirebaseRecyclerAdapter<RoobaruDuniya, BlogViewHolder> firebaseRecyclerAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDtabase = FirebaseDatabase.getInstance();
        dbaseReference = firebaseDtabase.getReference().child("user").child(uid).child("articleStatus");
        // dbaseReference = firebaseDtabase.getReference().child("user").child(uid);
        msgReference = firebaseDtabase.getReference().child("messages");
        Log.d("checkt", dbaseReference.toString());
        Log.d("msgkt", msgReference.toString());


        //setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        imageAdapter = new imgAdapter(rubaru, getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setAdapter(imageAdapter);

        return rootView;
    }

    private void checkMessages() {
        if (msgList.size() > 0) {
            Log.d("msgct", msgList.size() + "");


            for (String key : msgList) {
                //accessing those nodes whose key is equal to key in arraylist
                msgReference.orderByKey().equalTo(key).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                        Log.d("titleck", rbd.getTitle());
                        rubaru.add(rbd);
                        imageAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            /*    msgReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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
                */
            }
        }

    }


   /* public void onStart() {
        if (userListener == null) {
            userListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getValue().equals("draft")) {
                                msgList.add(ds.getKey());
                                Log.d("keyck", ds.getKey());
                            }

                        }
                        Log.d("alck", msgList.get(0).toString());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            dbaseReference.addListenerForSingleValueEvent(userListener);
        }

        Log.d("where", "I am here");
        checkMessages();

        super.onStart();
    }
    */
   public void onStart()
   {
       super.onStart();
       if(userListener==null)
       {
           userListener= new ChildEventListener() {
               //Adding keys in ArrayList msgList whose value is draft
               @Override
               public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   msgList.add(dataSnapshot.getKey());
                   Log.d("dbkey",dataSnapshot.getKey());
               }

               @Override
               public void onChildChanged(DataSnapshot dataSnapshot, String s) {

               }

               @Override
               public void onChildRemoved(DataSnapshot dataSnapshot) {

               }

               @Override
               public void onChildMoved(DataSnapshot dataSnapshot, String s) {

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           };
           dbaseReference.orderByValue().equalTo("draft").addChildEventListener(userListener);
       }
      /* dbaseReference.orderByValue().equalTo("draft").addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               msgList.add(dataSnapshot.getKey());
               Log.d("dbkey",dataSnapshot.getKey());


           }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }


       });
       */

       checkMessages();


   }

    public void onPause() {
        super.onPause();
       if (userListener != null) {
            dbaseReference.removeEventListener(userListener);
            userListener = null;
        }


    }


    @Override
    public void onResume() {
        super.onResume();
    }
}






