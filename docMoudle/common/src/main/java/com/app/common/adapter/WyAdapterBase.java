package com.app.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class WyAdapterBase<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mList;
    public WyAdapterBase(Context context,List arrayList){
        mContext = context;
        mList = arrayList;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    public List getList(){
        return mList;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
