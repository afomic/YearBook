package com.afomic.yearbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.afomic.yearbook.R;
import com.afomic.yearbook.model.Department;

import java.util.ArrayList;

/**
 * Created by afomic on 12/17/17.
 *
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Department> mDepartments;
    public SpinnerAdapter(Context context, ArrayList<Department> Departments){
        mContext=context;
        mDepartments=Departments;

    }
    @Override
    public int getCount() {
        if(mDepartments==null){
            return 0;
        }
        return mDepartments.size();
    }

    @Override
    public Department getItem(int position) {
        return mDepartments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DepartmentHolder holder;

        if(convertView==null){
            holder=new DepartmentHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_department,parent,false);
            holder.departmentTextView=convertView.findViewById(R.id.tv_deaprtment_name);
            convertView.setTag(holder);
        }else {
            holder=(DepartmentHolder)convertView.getTag();
        }
        Department department=getItem(position);
        holder.departmentTextView.setText(department.getName());
        return convertView;
    }
    public class DepartmentHolder{
        CheckedTextView departmentTextView;
    }
}
