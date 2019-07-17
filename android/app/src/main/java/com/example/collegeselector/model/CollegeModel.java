package com.example.collegeselector.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
/**
 * Awesome Pojo Generator
 * */
public class CollegeModel{
  @SerializedName("data")
  @Expose
  private List<Data> data;
  @SerializedName("response")
  @Expose
  private Boolean response;
  public void setData(List<Data> data){
   this.data=data;
  }
  public List<Data> getData(){
   return data;
  }
  public void setResponse(Boolean response){
   this.response=response;
  }
  public Boolean getResponse(){
   return response;
  }
}