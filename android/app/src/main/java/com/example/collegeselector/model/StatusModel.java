package com.example.collegeselector.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Awesome Pojo Generator
 * */
public class StatusModel{
  @SerializedName("response")
  @Expose
  private Boolean response;
  @SerializedName("message")
  @Expose
  private String message;
  public void setResponse(Boolean response){
   this.response=response;
  }
  public Boolean getResponse(){
   return response;
  }
  public void setMessage(String message){
   this.message=message;
  }
  public String getMessage(){
   return message;
  }
}