package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 12/8/17.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 13/3/17.
 */

public class categoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static ClickListener clickListener;
    Context context;
    private List<DisplayEvent> mList;

    public categoryAdapter(ArrayList<DisplayEvent> articles, Context context) {
        this.mList = articles;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, null);
        SimpleHeaderViewHolder mh = new SimpleHeaderViewHolder(v);
        return mh;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {


        final String sectionName = mList.get(listPosition).getHeader();

        ArrayList<HomeDisplay> singleSectionItems = mList.get(listPosition).getAllItemsInSection();
        Log.d("getarl",""+singleSectionItems.size());
        Log.d("getal",singleSectionItems.get(1).getTitle());
        Log.d("getal",singleSectionItems.get(0).getTitle());


        SimpleHeaderViewHolder smpholder=(SimpleHeaderViewHolder) holder;

        smpholder.headerText.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(context, singleSectionItems);

        smpholder.rcv.setHasFixedSize(true);
        smpholder.rcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        smpholder.rcv.setAdapter(itemListDataAdapter);




    }



    @Override
    public int getItemCount() {
        Log.d("cksi",""+mList.size());
        return mList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        categoryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageViewIcon;
        public TextView textView;

        public ItemViewHolder(View view) {
            super(view);

            this.imageViewIcon = (ImageView) view.findViewById(R.id.imgEditor);
            this.textView = (TextView) view.findViewById(R.id.editortxt);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public static class SimpleHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView headerText;
        public RecyclerView rcv;

        public SimpleHeaderViewHolder(View itemView) {
            super(itemView);
            this.headerText = (TextView) itemView.findViewById(R.id.header_id);
            this.rcv=(RecyclerView)itemView.findViewById(R.id.recycler_view_list);
        }
    }
}

