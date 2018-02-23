package com.afomic.yearbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Comment;
import com.afomic.yearbook.util.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by afomic on 12/18/17.
 *
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context mContext;
    private ArrayList<Comment> mComments;
    public CommentAdapter(Context context, ArrayList<Comment> comments){
        mContext=context;
        mComments=comments;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_comment,parent,false);
        return new CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment=mComments.get(position);
        holder.commentTextView.setText(comment.getComment());
        holder.posterNameTextView.setText(comment.getPosterName());
        GlideApp.with(mContext)
                .load(comment.getPosterPictureUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.posterIcon);
        String time= DateUtils.getRelativeTimeSpanString(comment.getCommentTime()).toString();
        holder.timeTextView.setText(time);

    }

    @Override
    public int getItemCount() {
        if(mComments==null){
            return 0;
        }
        return mComments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder{
        CircleImageView posterIcon;
        TextView posterNameTextView,commentTextView,timeTextView;
        public CommentHolder(View itemView) {
            super(itemView);
            posterNameTextView=itemView.findViewById(R.id.tv_poster_name);
            posterIcon=itemView.findViewById(R.id.imv_poster_image);
            commentTextView=itemView.findViewById(R.id.tv_comment);
            timeTextView=itemView.findViewById(R.id.tv_post_time);
        }
    }
}
