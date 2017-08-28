package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by KV on 12/8/17.
 */

public class MainPage extends Fragment {
    RecyclerView mainrecycler;
    ArrayList<MainDisplay> al = new ArrayList<>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public MainPage() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_page, container, false);
        mainrecycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        // ArrayList<MainDisplay> al=new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.drawimage);
        // MainDisplay dp=new MainDisplay(getString(R.string.cat1),getResources().getResourceEntryName(R.drawable.spotlight));
        // al.add(dp);
        // MainDisplay dp=new MainDisplay(getString(R.string.cat1),R.drawable.spotlight);
        for (int i = 0; i < 14; i++) {
            Log.d("ckarr", "" + getResources().getIntArray(R.array.drawimage)[0]);

            MainDisplay dp = new MainDisplay((getResources().getStringArray(R.array.catgs)[i]), imgs.getResourceId(i, -1));
            al.add(dp);
        }
        MainAdapter mainAdapter = new MainAdapter(getContext(), al);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mainrecycler.setLayoutManager(gridLayoutManager);
        mainrecycler.setAdapter(mainAdapter);
        mainrecycler.setItemAnimator(new DefaultItemAnimator());
        mainAdapter.setOnItemClickListener(new MainAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                MainDisplay md = al.get(position);
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("articlecat", md.getText());
                startActivity(intent);


            }
        });


        return rootView;
    }
}
