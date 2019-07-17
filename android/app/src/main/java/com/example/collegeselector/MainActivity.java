package com.example.collegeselector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.collegeselector.fragment.LoginFragment;
import com.example.collegeselector.fragment.SignupFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnfragment;
    private Boolean status=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        btnfragment=findViewById(R.id.btngo);
        btnfragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent(MainActivity.this,ViewPagerActivity.class);
        startActivity(intent);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        if(status){
            LoginFragment firstFragment=new LoginFragment();
            fragmentTransaction.replace(R.id.fragmentcontainer,firstFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            btnfragment.setText("Register");
            status=false;
        }
        else{
            SignupFragment secondFragment =new SignupFragment();
            fragmentTransaction.replace(R.id.fragmentcontainer,secondFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            btnfragment.setText("Login");
            status=true;
        }

    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new  String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },0);
        }
    }
}
