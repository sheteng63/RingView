package com.example.sheteng.ringview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RingView mRingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRingView = (RingView) findViewById(R.id.ringview);
    }

    public void showRing(View view) {
        ArrayList<Float> acrs = new ArrayList<>();
        acrs.add(5f);
        acrs.add(4f);
        acrs.add(7f);
        acrs.add(8f);
        mRingView.initData(acrs);
    }

}
