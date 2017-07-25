package com.example.yaoyan.customprocessbarrxjava;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.yaoyan.customprocessbarrxjava.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private CustomProcess customProcess;
    private ActivityMainBinding viewDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        customProcess = (CustomProcess) findViewById(R.id.process);
    }
}
