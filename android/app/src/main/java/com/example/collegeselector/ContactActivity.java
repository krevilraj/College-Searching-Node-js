package com.example.collegeselector;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.StatusModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private EditText mTxtfname;
    private EditText mTxtemail;
    private EditText mTxtphone;
    private EditText mTxtdesc;
    private Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mTxtfname = findViewById(R.id.txtfname);
        mTxtemail = findViewById(R.id.txtemail);
        mTxtphone = findViewById(R.id.txtphone);
        mTxtdesc = findViewById(R.id.txtdesc);
        mBtnSend = findViewById(R.id.btnsend);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    send_message();
                }

            }
        });
    }

    private void send_message() {
        String name = mTxtfname.getText().toString();
        String email = mTxtemail.getText().toString();
        String phone = mTxtphone.getText().toString();
        String message = mTxtdesc.getText().toString();

        String url = CollegeAPI.base_url + "sendContact";

        final Call<StatusModel> userInfo = CollegeAPI.getService().send_contact(url, name, email,phone, message);
        userInfo.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                StatusModel server_response = response.body();

                if (server_response.getResponse()) {
                    mTxtfname.setText("");
                    mTxtemail.setText("");
                    mTxtphone.setText("");
                    mTxtdesc.setText("");
                    Toast.makeText(ContactActivity.this, "Success!!! We will call you soon", Toast.LENGTH_SHORT).show();

                } else {
                    Vibrator vibe = (Vibrator) ContactActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(ContactActivity.this, "Failed!!! " + server_response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Toast.makeText(ContactActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private boolean validate() {
        boolean checkvalidate = true;
        if (TextUtils.isEmpty(mTxtfname.getText().toString())) {
            mTxtfname.setError(" Name is required");
            mTxtfname.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(mTxtphone.getText().toString())) {
            mTxtphone.setError("Phone details is required");
            mTxtphone.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(mTxtemail.getText().toString())) {
            mTxtemail.setError("Email details is required");
            mTxtemail.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(mTxtdesc.getText().toString())) {
            mTxtdesc.setError("Message details is required");
            mTxtdesc.requestFocus();
            checkvalidate = false;
        }

        return checkvalidate;

    }
}
