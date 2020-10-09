package com.app.yun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.app.yun.GreenDaoActivity;
import com.app.yun.R;
import com.app.yun.banner.BannerActivity;
import com.app.yun.banner.SPSearchActivity;
import com.app.yun.echart.EchartsActivity;
import com.app.yun.map.MapActivity;
import com.app.yun.map.MarkerDemo;


public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,  container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        Button openEcharts = getActivity().findViewById(R.id.openEcharts);
        com.came.viewbguilib.ButtonBgUi openBanner = getActivity().findViewById(R.id.openBanner);
        Button openSPSearch = getActivity().findViewById(R.id.openSPSearch);
        Button openMap = getActivity().findViewById(R.id.openMap);
        Button openMapMark = getActivity().findViewById(R.id.openMapMark);
        Button openGreenDao = getActivity().findViewById(R.id.openGreenDao);
        openEcharts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), EchartsActivity.class);
                startActivity(intent);
            }
        });
        openBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), BannerActivity.class);
                startActivity(intent);
            }
        });
        openSPSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), SPSearchActivity.class);
                startActivity(intent);
            }
        });
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        openMapMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), MarkerDemo.class);
                startActivity(intent);
            }
        });
        openGreenDao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), GreenDaoActivity.class);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}