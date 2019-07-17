package com.example.collegeselector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.model.ImageModel;
import com.example.collegeselector.model.StatusModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeAddActivity extends AppCompatActivity {
    private EditText txtname, txtlat, txtlon, txtdesc, txtfee, txtcontact, txtlocation;
    private Button btnadd;
    ImageView txtimage;
    private Button mCapture;
    private static final int CAMERA_REQUEST = 4321;
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    String ImageUrl;
    Bitmap bitmap;
    Boolean imageSet=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collegeadd);

        txtname = findViewById(R.id.txtname);
        txtlat = findViewById(R.id.txtlat);
        txtlon = findViewById(R.id.txtlon);
        txtimage = findViewById(R.id.college_image);
        txtdesc = findViewById(R.id.txtdesc);
        txtfee = findViewById(R.id.txtfee);
        txtcontact = findViewById(R.id.txtcontact);
        txtlocation = findViewById(R.id.txtlocation);


        btnadd = findViewById(R.id.btnsave);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (imageSet) {
                        SaveImage(bitmap);
                    } else {
                        Toast.makeText(CollegeAddActivity.this, "Please Select the college picture", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mCapture = findViewById(R.id.capture);
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSet = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CollegeAddActivity.this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }

                }
            }
        });
    }

    private void save() {
        String name = txtname.getText().toString();
        String phone = txtcontact.getText().toString();
        String lat = txtlat.getText().toString();
        String lon = txtlon.getText().toString();
        String location = txtlocation.getText().toString();
        String description = txtdesc.getText().toString();
        String price = txtfee.getText().toString();
        SharedPreferences pref = getSharedPreferences("UserDetail", 0);
        String token = pref.getString("token", null);


        String url = CollegeAPI.base_url + "student/addCollege";
        final Call<StatusModel> userInfo = CollegeAPI.getService().add_college(url, name, phone, ImageUrl, lat, lon, location, description, price, token);
        userInfo.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                StatusModel server_response = response.body();

                if (server_response.getResponse()) {
                    Toast.makeText(CollegeAddActivity.this, "Success!!! College has been uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(CollegeAddActivity.this, DashboardActivity.class);
                    startActivity(intent);

                } else {
                    Vibrator vibe = (Vibrator) CollegeAddActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(CollegeAddActivity.this, "Failed!!! " + server_response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Toast.makeText(CollegeAddActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private boolean validate() {
        boolean checkvalidate = true;
        if (TextUtils.isEmpty(txtname.getText().toString())) {
            txtname.setError(" Name is required");
            txtname.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtlat.getText().toString())) {
            txtlat.setError("Latitude details is required");
            txtlat.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtlon.getText().toString())) {
            txtlon.setError("Latitude details is required");
            txtlon.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtlocation.getText().toString())) {
            txtlocation.setError("Latitude details is required");
            txtlocation.requestFocus();
            checkvalidate = false;
        }

        if (TextUtils.isEmpty(txtdesc.getText().toString())) {
            txtdesc.setError("Description is required");
            txtdesc.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtfee.getText().toString())) {
            txtfee.setError("Enroll Fee is required");
            txtfee.requestFocus();
            checkvalidate = false;
        }
        if (TextUtils.isEmpty(txtcontact.getText().toString())) {
            txtcontact.setError("Contact detail is required");
            txtcontact.requestFocus();
            checkvalidate = false;
        }
        return checkvalidate;

    }

    private void SaveImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        try {
            File file = new File(CollegeAddActivity.this.getCacheDir(), "image.jpg");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

            final Call<ImageModel> call = CollegeAPI.getService().uploadImage(body);
            call.enqueue(new Callback<ImageModel>() {
                @Override
                public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                    if (response.isSuccessful()) {
                        ImageModel imageModel = response.body();

                        ImageUrl = imageModel.getImage();
                        save();
                    }

                }

                @Override
                public void onFailure(Call<ImageModel> call, Throwable t) {
                    Toast.makeText(CollegeAddActivity.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Log.i("test", "error");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            txtimage.setImageBitmap(bitmap);
            imageSet = true;
        }
    }
}
