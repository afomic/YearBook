package com.afomic.yearbook.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.afomic.yearbook.ProfileDetailsActivity;
import com.afomic.yearbook.R;
import com.afomic.yearbook.data.Constant;
import com.afomic.yearbook.model.Profile;
import com.afomic.yearbook.util.GlideApp;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by afomic on 18-May-17.
 *
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private ArrayList<Object> ProfileList;
    public static final int TYPE_LABEL=102;

    public static final int TYPE_USER=103;


    public SearchAdapter(Context mContext, ArrayList<Object> ProfileList){
        this.mContext=mContext;
        this.ProfileList=ProfileList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_USER){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_user_search,parent,false);
            return new ViewHolder(v);
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_label,parent,false);
        return new LabelHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_USER){
            Profile profile=(Profile) ProfileList.get(position);
            ViewHolder myHolder=(ViewHolder)holder;
            myHolder.mName.setText(profile.getName());
            myHolder.mNickname.setText(profile.getNickName());
            GlideApp.with(mContext)
                    .load(profile.getPictureUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(myHolder.mUserLogo);

        }else {
            String label=(String) ProfileList.get(position);
            LabelHolder labelHolder=(LabelHolder) holder;
            labelHolder.labelTextView.setText(label);

        }

    }

    @Override
    public int getItemCount() {
        if(ProfileList==null){
            return 0;
        }
        return ProfileList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object=ProfileList.get(position);
        if(object instanceof String){
            return TYPE_LABEL;
        }

        return TYPE_USER;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView mUserLogo;
        TextView mName,mNickname;

        public ViewHolder(View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.tv_name);
            mUserLogo= itemView.findViewById(R.id.imv_user_logo);
            mNickname=itemView.findViewById(R.id.tv_nick_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           Intent intent=new Intent(mContext, ProfileDetailsActivity.class);
            Profile Profile=(Profile) ProfileList.get(getAdapterPosition());
            intent.putExtra(Constant.EXTRA_PROFILE,Profile);
            mContext.startActivity(intent);
        }
    }
    public class LabelHolder extends RecyclerView.ViewHolder{
        TextView labelTextView;
        public LabelHolder(View itemView) {
            super(itemView);
            labelTextView=itemView.findViewById(R.id.tv_search_label);

        }
    }
    public void unRegister(Context context){
        if(context==mContext){
            mContext=null;
        }
    }

}
