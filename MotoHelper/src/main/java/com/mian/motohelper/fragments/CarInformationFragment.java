package com.mian.motohelper.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mian.motohelper.R;


/**
 * "愛車資訊"頁面的Fragment
 */
public class CarInformationFragment extends Fragment {


    public CarInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_information, container, false);
    }


}
