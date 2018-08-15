package com.example.hp.quick_rest;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Homefragment extends Fragment {

    private static final String TAG = "MainActivity";
    RecyclerViewAdapter adapter;
    RecyclerViewAdapter2 adapter2;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.mipmap.bannerff, R.drawable.bur2, R.drawable.soda};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Home");

        return inflater.inflate(R.layout.fragment_home,null);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
       getImages();
       getImages2();

        }
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.pie);
        mNames.add("Chicken Pot Pie");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.bur2);
        mNames.add("Chicken King Burger");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.fries);
        mNames.add("French Fries");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.pie);
        mNames.add("Chicken Pot Pie");




        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }
    private void getImages2(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.soda);
        mNames.add("Energy Drinks");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.juice);
        mNames.add("Water");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.water);
        mNames.add("Juices");

        mImageUrls.add("android.resource://com.example.hp.quick_rest/" + R.drawable.soda);
        mNames.add("Energy Drinks");




        initRecyclerView2();

    }
    private void initRecyclerView2(){
        Log.d(TAG, "initRecyclerView2: init recyclerview2");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter2 adapter = new RecyclerViewAdapter2(getActivity(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }






    
    private void init() {
        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);



        mPager = (ViewPager)getActivity(). findViewById(R.id.pager);
        CircleIndicator indicator = (CircleIndicator)getActivity(). findViewById(R.id.indicator);
        mPager.setAdapter(new Adapter(getActivity(), XMENArray));
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}
