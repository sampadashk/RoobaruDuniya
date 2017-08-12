package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 12/8/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.samiapps.kv.roobaruduniya.DisplayEvent.EVENT_TYPE;
import static com.samiapps.kv.roobaruduniya.DisplayEvent.Header_TYPE;

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
        View view;
        switch (viewType) {
            case Header_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
                return new SimpleHeaderViewHolder(view);
            case EVENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editorcard, parent, false);
                return new ItemViewHolder(view);

        }

        return null;


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int listPosition) {


        DisplayEvent item = mList.get(listPosition);
        switch (item.getType()) {
            case Header_TYPE:
                ((SimpleHeaderViewHolder) holder).headerText.setText(item.getName());
                break;
            case EVENT_TYPE: {
                try {
                    HomeDisplay hm = item.getDescription();
                    ((ItemViewHolder) holder).textView.setText(hm.getTitle());
                    String image = hm.getPhoto();

                    if (image != null) {


                        Glide.with(context)
                                .load(image)
                                .into(((ItemViewHolder) holder).imageViewIcon);
                    }
                }
                catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
                break;

            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            DisplayEvent object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
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

        public SimpleHeaderViewHolder(View itemView) {
            super(itemView);
            this.headerText = (TextView) itemView.findViewById(R.id.header_id);
        }
    }
}

