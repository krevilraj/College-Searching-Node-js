package com.example.collegeselector.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class UserDetail{
  @SerializedName("user_status")
  @Expose
  private Boolean user_status;
  @SerializedName("password")
  @Expose
  private String password;
  @SerializedName("phone")
  @Expose
  private String phone;
  @SerializedName("__v")
  @Expose
  private Integer __v;
  @SerializedName("name")
  @Expose
  private String name;
  @SerializedName("admin_status")
  @Expose
  private Boolean admin_status;
  @SerializedName("_id")
  @Expose
  private String _id;
  @SerializedName("email")
  @Expose
  private String email;
  @SerializedName("username")
  @Expose
  private String username;
  public void setUser_status(Boolean user_status){
   this.user_status=user_status;
  }
  public Boolean getUser_status(){
   return user_status;
  }
  public void setPassword(String password){
   this.password=password;
  }
  public String getPassword(){
   return password;
  }
  public void setPhone(String phone){
   this.phone=phone;
  }
  public String getPhone(){
   return phone;
  }
  public void set__v(Integer __v){
   this.__v=__v;
  }
  public Integer get__v(){
   return __v;
  }
  public void setName(String name){
   this.name=name;
  }
  public String getName(){
   return name;
  }
  public void setAdmin_status(Boolean admin_status){
   this.admin_status=admin_status;
  }
  public Boolean getAdmin_status(){
   return admin_status;
  }
  public void set_id(String _id){
   this._id=_id;
  }
  public String get_id(){
   return _id;
  }
  public void setEmail(String email){
   this.email=email;
  }
  public String getEmail(){
   return email;
  }
  public void setUsername(String username){
   this.username=username;
  }
  public String getUsername(){
   return username;
  }
}