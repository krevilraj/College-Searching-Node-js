package com.example.collegeselector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeselector.adapter.ReviewAdapter;
import com.example.collegeselector.helper.CollegeAPI;
import com.example.collegeselector.helper.Helper;
import com.example.collegeselector.model.CollegeModel;
import com.example.collegeselector.model.CollegeReview;
import com.example.collegeselector.model.Data;
import com.example.collegeselector.model.StatusModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeDescriptionActivity extends AppCompatActivity {
    ImageView imgview;
    TextView txtname, txtlocation, txtdesc, txtphone, txtprice;
    Data detail;
    RatingBar ratingBar,userRatingBar;
    String college_id;
    private RecyclerView recyclerView;
    LinearLayoutManager manager;
    ReviewAdapter adapter;
    List<Data> items = new ArrayList<>();
    List<CollegeReview> review_item = new ArrayList<>();
    private Button mSubmit;
    private EditText mEdtReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collegedescription);
        imgview = findViewById(R.id.img_college);
        txtname = findViewById(R.id.tvName);
        txtlocation = findViewById(R.id.tvLocation);
        txtphone = findViewById(R.id.tvPhone);
        txtprice = findViewById(R.id.tvMoney);
        txtdesc = findViewById(R.id.tvDesc);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imgview.setImageResource(bundle.getInt("image"));
            txtname.setText(bundle.getString("name"));
            txtlocation.setText(bundle.getString("location"));
            txtdesc.setText(bundle.getString("desc"));

        }

        Bundle extras = getIntent().getExtras();

        if (savedInstanceState == null) {
            if (extras == null) {
                detail = new Data();
            } else {
                detail = (Data) getIntent().getSerializableExtra("college_description");
                setData();
            }
        } else {
            detail = (Data) savedInstanceState.getSerializable("college_description");
            setData();
        }

        recyclerView = findViewById(R.id.recyclerview);
        manager = new LinearLayoutManager(this);
        adapter = new ReviewAdapter(this, review_item);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        mEdtReview = findViewById(R.id.edtReview);
        mSubmit = findViewById(R.id.submit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mEdtReview.getText().toString())){
                    mEdtReview.setError("Must write review");
                    mEdtReview.requestFocus();
                }else{
                    send_review();
                }
            }
        });
    }

    private void send_review(){
        SharedPreferences pref = getSharedPreferences("UserDetail", 0);
        String token = pref.getString("token", null);
        String user = pref.getString("username", null);
        String review = mEdtReview.getText().toString();
        float rating = userRatingBar.getRating();


        Helper.logme(token,user,review,""+rating,detail.get_id());
        String url = CollegeAPI.base_url + "student/addReview";
        final Call<StatusModel> userInfo = CollegeAPI.getService().send_review(url, detail.get_id(), user, review, rating, token);
        userInfo.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                StatusModel server_response = response.body();

                if (server_response.getResponse()) {
                    mEdtReview.setText("");
                    Toast.makeText(CollegeDescriptionActivity.this, "Success!!! Your review has been posted", Toast.LENGTH_SHORT).show();
                    adapter.clear_data();
                    getReview();
                } else {
                    Vibrator vibe = (Vibrator) CollegeDescriptionActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);
                    Toast.makeText(CollegeDescriptionActivity.this, "Failed!!! " + server_response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Toast.makeText(CollegeDescriptionActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void setData() {
        college_id = detail.get_id();
        ratingBar = findViewById(R.id.ratingbar);
        userRatingBar = findViewById(R.id.user_ratingbar);
        txtname.setText(detail.getCollege_name());
        txtphone.setText(detail.getPhone());
        txtprice.setText("Nrs. " + detail.getPrice());
        txtlocation.setText(detail.getLocation());
        txtdesc.setText(detail.getDescription());
        getRating();
        getReview();
//        ratingBar.setRating(detail.getRating());


        String image_url = CollegeAPI.base_url + detail.getImage();


        Picasso.get().load(image_url).fit().centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgview);
        addListenerOnRatingBar();
    }

    private void getReview() {
        String review_url = "collegeReview/" + detail.get_id();
        Log.e("test1", review_url);
        final Call<List<CollegeReview>> postList = CollegeAPI.getService().get_review(review_url);
        postList.enqueue(new Callback<List<CollegeReview>>() {
            @Override
            public void onResponse(Call<List<CollegeReview>> call, Response<List<CollegeReview>> response) {
                if (response.body() != null) {
                    List<CollegeReview> s_items = response.body();
                    review_item.addAll(s_items);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CollegeDescriptionActivity.this, "Some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CollegeReview>> call, Throwable t) {
                Toast.makeText(CollegeDescriptionActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

            }

        });
    }

    public void addListenerOnRatingBar() {


        final String[] txtRatingValue = new String[1];

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue[0] = txtRatingValue[0];

            }
        });
    }

    private void getRating() {

        String url = CollegeAPI.base_url + "student/collegeDetail";
        SharedPreferences pref = getSharedPreferences("UserDetail", 0); // 0 - for private mode
        String token = pref.getString("token", null);


        final Call<Data> userInfo = CollegeAPI.getService().get_college_detail(url, token, college_id);
        userInfo.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.body() != null) {
                    Data college_detail = response.body();
                    ratingBar.setRating(college_detail.getRating());
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(CollegeDescriptionActivity.this, "Error Occured While Fetching", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
