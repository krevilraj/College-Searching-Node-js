package com.example.collegeselector.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class CollegeReview{
  @SerializedName("college_id")
  @Expose
  private String college_id;
  @SerializedName("review")
  @Expose
  private String review;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("rating")
  @Expose
  private Integer rating;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("user")
  @Expose
  private String user;
  public void setCollege_id(String college_id){
   this.college_id=college_id;
  }
  public String getCollege_id(){
   return college_id;
  }
  public void setReview(String review){
   this.review=review;
  }
  public String getReview(){
   return review;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setRating(Integer rating){
   this.rating=rating;
  }
  public Integer getRating(){
   return rating;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setUser(String user){
   this.user=user;
  }
  public String getUser(){
   return user;
  }
}