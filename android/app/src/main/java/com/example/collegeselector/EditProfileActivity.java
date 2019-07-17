package com.example.collegeselector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.StatusModel;
import com.example.collegeselector.model.UserDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText mTxtfname;
    private EditText mTxtemail;
    private EditText mTxtpassword;
    private EditText mTxtrepassword;
    private Button mBtnregister;
    private EditText mTxtphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        mTxtfname = findViewById(R.id.txtfname);
        mTxtemail = findViewById(R.id.txtemail);
        mTxtpassword = findViewById(R.id.txtpassword);
        mTxtrepassword = findViewById(R.id.txtrepassword);
        mBtnregister = findViewById(R.id.btnregister);

        mBtnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_user();
            }
        });

        setData();
        mTxtphone = findViewById(R.id.txtphone);
    }

    private void update_user() {

        String name = mTxtfname.getText().toString();
        String email = mTxtemail.getText().toString();
        String password = mTxtpassword.getText().toString();
        String phone = mTxtphone.getText().toString();

        String url = CollegeAPI.base_url + "student/updateUser";
        SharedPreferences pref = getSharedPreferences("UserDetail", 0);
        String token = pref.getString("token", null);
        String user_id = pref.getString("user_id", null);

        final Call<StatusModel> userInfo = CollegeAPI.getService().update_user(url, name, email, phone, password,user_id,token);
        userInfo.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                StatusModel server_response = response.body();

                if (server_response.getResponse()) {
                    Toast.makeText(EditProfileActivity.this, "Success!!! Your profile has been updated", Toast.LENGTH_SHORT).show();

                } else {
                    Vibrator vibe = (Vibrator) EditProfileActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(EditProfileActivity.this, "Failed!!! " + server_response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void setData() {
        String s_url = CollegeAPI.base_url + "student/editUser";
        SharedPreferences pref = getSharedPreferences("UserDetail", 0);
        String token = pref.getString("token", null);
        String user = pref.getString("username", null);
        String user_id = pref.getString("user_id", null);
        final Call<UserDetail> postList = CollegeAPI.getService().get_user_detail(s_url, token, user, user_id);
        postList.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.body() != null) {
                    UserDetail item = response.body();
                    mTxtemail.setText(item.getEmail());
                    mTxtfname.setText(item.getName());
                    mTxtemail.setText(item.getEmail());
                    mTxtphone.setText(item.getPhone());

                } else {
                    Toast.makeText(EditProfileActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }
}
