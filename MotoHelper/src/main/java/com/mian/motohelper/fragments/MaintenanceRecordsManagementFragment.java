package com.mian.motohelper.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mian.motohelper.R;

/**
 * "保養紀錄管理"頁面的Fragment
 */
public class MaintenanceRecordsManagementFragment extends Fragment {


    public MaintenanceRecordsManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maintenance_records_management, container, false);
    }


}
