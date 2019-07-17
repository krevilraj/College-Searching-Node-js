package com.example.collegeselector.helper;


import com.example.collegeselector.model.CollegeModel;
import com.example.collegeselector.model.CollegeReview;
import com.example.collegeselector.model.Data;
import com.example.collegeselector.model.ImageModel;
import com.example.collegeselector.model.StatusModel;
import com.example.collegeselector.model.UserDetail;
import com.example.collegeselector.model.UserModel;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Krevilraj on 4/8/2018.
 */

public class CollegeAPI {
    public static final String base_url = "http://192.168.100.14:8080/";
    public static final String url = base_url + "api/";
    public static final String cache_control = "Cache-Control:no-cache";

    public static PostService postService = null;

    public static PostService getService() {


        if (postService == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService = retrofit.create(PostService.class);

        }
        return postService;
    }

    public interface PostService {

        @FormUrlEncoded
        @POST
        Call<UserModel> getUser(@Url String url,
                                 @Field("email") String email,
                                 @Field("password") String password);

        @FormUrlEncoded
        @POST
        Call<StatusModel> add_user(@Url String url,
                                   @Field("name") String name,
                                   @Field("username") String username,
                                   @Field("email") String email,
                                   @Field("repassword") String passwordConf,
                                   @Field("password") String password);


        @FormUrlEncoded
        @POST
        Call<StatusModel> update_user(@Url String url,
                                        @Field("name") String name,
                                        @Field("email") String email,
                                        @Field("phone") String phone,
                                        @Field("password") String password,
                                        @Field("_id") String _id,
                                        @Field("token") String token);


        @FormUrlEncoded
        @POST
        Call<UserDetail> get_user_detail(@Url String url,
                                         @Field("token") String token,
                                         @Field("username") String username,
                                         @Field("_id") String _id);

        @FormUrlEncoded
        @POST
        Call<CollegeModel> search_data(@Url String url,
                                            @Field("query") String query);

        @FormUrlEncoded
        @POST
        Call<StatusModel> comment_post(@Url String url,
                                         @Field("username") String username,
                                         @Field("description") String description,
                                         @Field("_id") String id,
                                         @Field("forum_id") String forum_id,
                                         @Field("token") String token);

        @FormUrlEncoded
        @POST
        Call<StatusModel> send_contact(@Url String url,
                                         @Field("name") String name,
                                         @Field("email") String email,
                                         @Field("phone") String phone,
                                         @Field("message") String message);

        @FormUrlEncoded
        @POST
        Call<StatusModel> add_college(@Url String url,
                                       @Field("college_name") String title,
                                       @Field("phone") String phone,
                                       @Field("image") String image,
                                       @Field("lat") String lat,
                                       @Field("lon") String lon,
                                       @Field("location") String location,
                                       @Field("content") String description,
                                       @Field("price") String price,
                                       @Field("token") String token);

        @FormUrlEncoded
        @POST
        Call<StatusModel> send_review(@Url String url,
                                       @Field("college_id") String college_id,
                                       @Field("user") String user,
                                       @Field("review") String review,
                                       @Field("rating") Float rating,
                                       @Field("token") String token);

        @FormUrlEncoded
        @POST
        Call<StatusModel> update_college(@Url String url,
                                       @Field("college_name") String title,
                                       @Field("phone") String phone,
                                       @Field("image") String image,
                                       @Field("lat") String lat,
                                       @Field("lon") String lon,
                                       @Field("location") String location,
                                       @Field("description") String description,
                                       @Field("price") String price,
                                       @Field("token") String token,
                                       @Field("_id") String college_id);

        @FormUrlEncoded
        @POST
        Call<StatusModel> delete_college(@Url String url,
                                       @Field("_id") String college_id);

        @FormUrlEncoded
        @POST
        Call<Data> get_college_detail(@Url String url,
                                      @Field("token") String token,
                                      @Field("college_id") String college_id);

        @Multipart
        @POST("/upload")
        Call<ImageModel> uploadImage(@Part MultipartBody.Part image);


        @GET("/list_college")
        Call<CollegeModel> getCollege();

        @GET
        Call<List<CollegeReview>> get_review(@Url String url);

    }
}
