package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by KV on 12/8/17.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<HomeDisplay> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<HomeDisplay> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.editorcard, null);
        SingleItemRowHolder h = new SingleItemRowHolder(v);
        return h;
    }
    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        HomeDisplay singleItem = itemsList.get(i);
        Log.d("ckcard",singleItem.getTitle());

        holder.textView.setText(singleItem.getTitle());
        if(singleItem.getPhoto()!=null)
        {
            Glide.with(mContext).load(singleItem.getPhoto()).into(holder.imageViewIcon);
        }


       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        Log.d("cksi",""+itemsList.size());
        return itemsList.size();
    }
    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewIcon;
        public TextView textView;


        public SingleItemRowHolder(View view) {
            super(view);

            this.imageViewIcon = (ImageView) view.findViewById(R.id.imgEditor);
            this.textView = (TextView) view.findViewById(R.id.editortxt);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), textView.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}