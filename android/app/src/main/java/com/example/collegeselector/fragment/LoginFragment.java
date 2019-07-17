package com.example.collegeselector.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeselector.DashboardActivity;
import com.example.collegeselector.R;
import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.StatusModel;
import com.example.collegeselector.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText txtemail,txtpassword;
    private Button btnlogin;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        txtemail=view.findViewById(R.id.txtemail);
        txtpassword=view.findViewById(R.id.txtpassword);
        btnlogin=view.findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(validate()){
            checkUser();
        }

    }
    private boolean validate(){
        boolean checkvalidate=true;
        if(TextUtils.isEmpty(txtemail.getText().toString())){
            txtemail.setError("Username is required");
            txtemail.requestFocus();
            checkvalidate=false;
        }
        if(TextUtils.isEmpty(txtpassword.getText().toString())){
            txtpassword.setError("Password is required");
            txtpassword.requestFocus();
            checkvalidate=false;
        }
        return  checkvalidate;

    }
    private void checkUser(){

        String url = CollegeAPI.base_url + "login";
        String email = txtemail.getText().toString();
        String password = txtpassword.getText().toString();
        Log.e("test",url);

        final Call<UserModel> userInfo = CollegeAPI.getService().getUser(url, email, password);
        userInfo.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel token = response.body();
                Log.e("test","u"+token.isResponse());
                if (!token.isResponse()) {
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                } else {
                    SharedPreferences pref = getActivity().getSharedPreferences("UserDetail", 0); // 0 - for private mode

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", token.getUsername());
                    editor.putString("token", token.getToken());
                    editor.putString("user_id", token.getId());

                    editor.apply();
                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    getActivity().startActivity(intent);
                }
                Toast.makeText(getActivity(), "Login Successful"+token.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Occured1", Toast.LENGTH_SHORT).show();

            }

        });

    }


}
