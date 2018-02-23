package com.afomic.yearbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afomic.yearbook.R;
import com.afomic.yearbook.StatusDetailActivity;
import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.fragment.StatusDetailsFragment;
import com.afomic.yearbook.model.Status;
import com.afomic.yearbook.util.GlideApp;

import java.util.ArrayList;

/**
 * Created by afomic on 12/13/17.
 *
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusHolder> {
    private ArrayList<Status> mStatusList;
    private Context mContext;
    public StatusAdapter(Context context, ArrayList<Status> statusList){
        mContext=context;
        mStatusList=statusList;
    }
    @Override
    public StatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_status,parent,false);
        return new StatusHolder(view);
    }

    @Override
    public void onBindViewHolder(StatusHolder holder, int position) {
        Status item=mStatusList.get(position);
        holder.statusTitle.setText(item.getTitle());
        if(item.getType()==Status.Type.picture){
            holder.videoIcon.setVisibility(View.GONE);
            GlideApp.with(mContext)
                    .load(item.getFileUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.statusPreview);

        }else {
            holder.videoIcon.setVisibility(View.VISIBLE);
            GlideApp.with(mContext)
                    .load(item.getThumbnailUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(holder.statusPreview);
        }

    }

    @Override
    public int getItemCount() {
        if(mStatusList==null){
            return 0;
        }
        return mStatusList.size();
    }

    public class StatusHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView statusPreview,videoIcon;
        TextView statusTitle;
        public StatusHolder(View itemView) {
            super(itemView);
            statusPreview=itemView.findViewById(R.id.imv_status_preview);
            statusTitle=itemView.findViewById(R.id.tv_status_title);
            videoIcon=itemView.findViewById(R.id.imv_video_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext, StatusDetailActivity.class);
            intent.putExtra(Constant.EXTRA_POSITION,getAdapterPosition());
            mContext.startActivity(intent);
        }
    }
}
