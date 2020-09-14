package com.app.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.common.domain.SelectInfo;
import com.app.wayee.common.R;

import java.util.List;

public class SelectCitysAdapter extends WyAdapterBase<SelectInfo> {

    public SelectCitysAdapter(Context context, List<SelectInfo> arrayList) {
        super(context, arrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_list_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SelectInfo cityInfoData = mList.get(position);
        if(cityInfoData!=null){
            viewHolder.nameTV.setText(cityInfoData.getTitle());
            viewHolder.selectedIV.setVisibility(cityInfoData.isSelected()?View.VISIBLE:View.GONE);
        }
        return convertView;
    }

    static class ViewHolder{
        TextView nameTV;
        ImageView selectedIV;
        public ViewHolder(View v){
            nameTV = v.findViewById(R.id.cityName);
            selectedIV = v.findViewById(R.id.selectView);
        }
    }
}
