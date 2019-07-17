package com.example.collegeselector.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeselector.R;
import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.StatusModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment implements View.OnClickListener {
    private EditText txtfname, txtemail, txtusername, txtpassword, txtrepassword;
    private Button btnregister;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        txtfname = view.findViewById(R.id.txtfname);
        txtemail = view.findViewById(R.id.txtemail);
        txtusername = view.findViewById(R.id.txtusername);
        txtpassword = view.findViewById(R.id.txtpassword);
        txtrepassword = view.findViewById(R.id.txtrepassword);

        btnregister = view.findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        if (validate()) {
            signup();
        }

    }

    private void signup() {

        String name = txtfname.getText().toString();
        String email = txtemail.getText().toString();
        String username = txtusername.getText().toString();
        String password = txtpassword.getText().toString();

        String url = CollegeAPI.base_url + "register";
        String passwordConf = password;
        final Call<StatusModel> userInfo = CollegeAPI.getService().add_user(url, name, username, email, passwordConf, password);
        userInfo.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                StatusModel server_response = response.body();

                if (server_response.getResponse()) {
                    txtfname.setText("");
                    txtemail.setText("");
                    txtpassword.setText("");
                    txtrepassword.setText("");
                    txtusername.setText("");
                    Toast.makeText(getActivity(), "Success!!! Now Swipe left login in", Toast.LENGTH_SHORT).show();

                } else {
                    Vibrator vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(getActivity(), "Failed!!! " + server_response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });


    }

    private boolean validate() {
        boolean checkvalidate = true;
        if (TextUtils.isEmpty(txtfname.getText().toString())) {
            txtfname.setError("First Name is required");
            txtfname.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtemail.getText().toString())) {
            txtemail.setError("Last Name is required");
            txtemail.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtusername.getText().toString())) {
            txtusername.setError("Username is required");
            txtusername.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtpassword.getText().toString())) {
            txtpassword.setError("Password is required");
            txtpassword.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtrepassword.getText().toString())) {
            txtrepassword.setError("Password is required");
            txtrepassword.requestFocus();
            checkvalidate = false;
        }
        String pwd = txtpassword.getText().toString();
        String repwd = txtrepassword.getText().toString();
        if (pwd.equals(repwd)) {
            checkvalidate = true;
        } else {
            txtpassword.setError("Invalid Match");
            txtrepassword.setError("Invalid Match");
            txtpassword.requestFocus();
            checkvalidate = false;
        }
        return checkvalidate;

    }
}
