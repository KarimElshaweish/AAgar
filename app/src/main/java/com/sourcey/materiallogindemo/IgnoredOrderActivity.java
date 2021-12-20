package com.sourcey.materiallogindemo;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sourcey.materiallogindemo.Adapter.IgnoredListAdapter;
import com.sourcey.materiallogindemo.RealTimeServices.IIgnoredServices;
import com.sourcey.materiallogindemo.databinding.ActivityIgnoredOrderBinding;

public class IgnoredOrderActivity extends AppCompatActivity implements IIgnoredServices {

    ActivityIgnoredOrderBinding ignoredOrderBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ignoredOrderBinding=ActivityIgnoredOrderBinding.inflate(getLayoutInflater());
        setContentView(ignoredOrderBinding.getRoot());
        ignoredOrderBinding.rv1.setHasFixedSize(true);
        ignoredOrderBinding.rv1.setLayoutManager(new LinearLayoutManager(this));
        IgnoredListAdapter listAdapter=new IgnoredListAdapter(Shared.ignoredList,this);
        ignoredOrderBinding.rv1.setAdapter(listAdapter);
        if(Shared.ignoredList.size()>0)
            ignoredOrderBinding.noOffer.setVisibility(View.GONE);
    }


    public void finish(View view) {
        finish();
    }

    @Override
    public void onListEmpty() {
        ignoredOrderBinding.noOffer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListHasData() {
        ignoredOrderBinding.noOffer.setVisibility(View.GONE);
    }
}
