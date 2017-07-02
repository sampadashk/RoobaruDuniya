package com.samiapps.kv.roobaruduniya;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
 * Created by KV on 2/7/17.
 */

public class FavFragment extends Fragment {
    private FirebaseDatabase firebaseDbase;
    private DatabaseReference dReference;
    private DatabaseReference mReference;
    private imgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru = new ArrayList<RoobaruDuniya>();
    ArrayList<String> kList;


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    ArrayList<String> childkey;
    public static final String TAG = FavFragment.class.getName();
    // ProgressBar pgbar;
    // TextView noDraftText;

    public FavFragment() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDbase = FirebaseDatabase.getInstance();

        // dbaseReference = firebaseDtabase.getReference().child("user").child(uid);
        mReference = firebaseDbase.getReference().child("messages");
        kList = new ArrayList<>();
        FavDb favRef = new FavDb(getContext());
        SQLiteDatabase db = favRef.getReadableDatabase();
        Cursor c = favRef.getKey(db);
        if (c.getCount() != 0) {

            while (c.moveToNext()) {

                String key = c.getString(c.getColumnIndex(RoobaruContract.COLUMN_KEY));
                displayFav(key);
                kList.add(key);
            }
        }


    }

    private void displayFav(String key) {
        mReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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


    //setHasOptionsMenu(true);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        imageAdapter = new imgAdapter(rubaru, getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mRecycleView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new imgAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                RoobaruDuniya item = rubaru.get(position);
                String key=kList.get(position);
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


        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("actck","stop");
        rubaru.clear();

    }

}
