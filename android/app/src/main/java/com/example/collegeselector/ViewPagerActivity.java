package com.example.collegeselector;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.collegeselector.adapter.ViewPagerAdapter;
import com.example.collegeselector.fragment.LoginFragment;
import com.example.collegeselector.fragment.SignupFragment;


public class ViewPagerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        tabLayout=findViewById(R.id.tabId);
        viewPager=findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment(),"Login");
        adapter.addFragment(new SignupFragment(),"Register");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
