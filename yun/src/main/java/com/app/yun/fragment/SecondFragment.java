package com.app.yun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.app.yun.R;
import com.app.yun.echart.EchartsActivity;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second,  container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        Button button = getActivity().findViewById(R.id.openEcharts);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), EchartsActivity.class);
                startActivity(intent);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}