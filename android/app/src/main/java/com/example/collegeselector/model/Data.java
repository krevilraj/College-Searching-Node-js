package com.example.collegeselector.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Awesome Pojo Generator
 * */
public class Data implements Serializable {
  @SerializedName("image")
  @Expose
  private String image;
  @SerializedName("website")
  @Expose
  private String website;
  @SerializedName("college_name")
  @Expose
  private String college_name;
  @SerializedName("phone")
  @Expose
  private String phone;
  @SerializedName("price")
  @Expose
  private Float price;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("rating")
  @Expose
  private Float rating;
  @SerializedName("course")
  @Expose
  private String course;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("location")
  @Expose
  private String location;
  @SerializedName("lon")
  @Expose
  private Integer lon;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("lat")
  @Expose
  private Integer lat;
  public void setImage(String image){
   this.image=image;
  }
  public String getImage(){
   return image;
  }
  public void setWebsite(String website){
   this.website=website;
  }
  public String getWebsite(){
   return website;
  }
  public void setCollege_name(String college_name){
   this.college_name=college_name;
  }
  public String getCollege_name(){
   return college_name;
  }
  public void setPhone(String phone){
   this.phone=phone;
  }
  public String getPhone(){
   return phone;
  }
  public void setPrice(Float price){
   this.price=price;
  }
  public Float getPrice(){
   return price;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setRating(Float rating){
   this.rating=rating;
  }
  public Float getRating(){
   return rating;
  }
  public void setCourse(String course){
   this.course=course;
  }
  public String getCourse(){
   return course;
  }
  public void setLocation(String location){
   this.location=location;
  }
  public String getLocation(){
   return location;
  }
  public void setLon(Integer lon){
   this.lon=lon;
  }
  public Integer getLon(){
   return lon;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setLat(Integer lat){
   this.lat=lat;
  }
  public Integer getLat(){
   return lat;
  }
  public void setDescription(String description){
        this.description=description;
    }
  public String getDescription(){
        return description;
    }
}