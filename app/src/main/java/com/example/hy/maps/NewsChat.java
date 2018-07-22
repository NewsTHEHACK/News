package com.example.hy.maps;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import adapter.Adapter;
import fragment.Chat_frag;
import fragment.News_frag;

public class NewsChat extends AppCompatActivity {
    private TabLayout tab;
    private ViewPager vp;
    private ArrayList<Fragment> data=new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_chat);
        tab=(TabLayout)findViewById(R.id.news_chat_tab);
        vp=(ViewPager)findViewById(R.id.news_chat_viewpager);
        News_frag fragment1 = new News_frag();
        Chat_frag fragment2 = new Chat_frag();
        data.add(fragment1);
        data.add(fragment2);
        list.add("News");
        list.add("Chat");
        tab.setupWithViewPager(vp);
        vp.setAdapter(new Adapter(getSupportFragmentManager(),data,list));

    }
}
