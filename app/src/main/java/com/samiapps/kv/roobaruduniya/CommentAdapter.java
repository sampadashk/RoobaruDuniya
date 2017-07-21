package com.samiapps.kv.roobaruduniya;

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

/**
 * Created by KV on 12/7/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    private ArrayList<Comment> comments;
    CommentAdapter(Context context,ArrayList<Comment> comments)
    {
        this.comments=comments;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.comment_layout,parent,false);
        CommentAdapter.ViewHolder myviewholder=new CommentAdapter.ViewHolder(v);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment cmt=comments.get(position);
        String cmtName=cmt.getCommentorName();
        String commnt=cmt.getComment();
        String date=cmt.getDate();
        String imgCommentor=cmt.getuPhoto();
        holder.cName.setText(cmtName);
        holder.cmnt.setText(commnt);
        holder.dateTextView.setText(date);
        Glide.with(context)
                .load(imgCommentor)
                .into(holder.uPhotoView);

    }

    @Override
    public int getItemCount() {
        Log.d("checkcosize",""+comments.size());
        return comments.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cName;
        public TextView cmnt;
        public TextView dateTextView;
        public ImageView uPhotoView;

        public ViewHolder(View itemView) {
            super(itemView);
            cName=(TextView)itemView.findViewById(R.id.uc_name);
            cmnt=(TextView)itemView.findViewById(R.id.uc_comment);
            dateTextView=(TextView)itemView.findViewById(R.id.cmt_data);
            uPhotoView=(ImageView)itemView.findViewById(R.id.img_profile);
        }
    }
}
