package com.example.collegeselector.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel{

	@SerializedName("response")
	@Expose
	private boolean response;

	@SerializedName("admin_status")
	@Expose
	private boolean adminStatus;

	@SerializedName("_id")
	@Expose
	private String id;

	@SerializedName("token")
	@Expose
	private String token;

	@SerializedName("username")
	@Expose
	private String username;

	public void setResponse(boolean response){
		this.response = response;
	}

	public boolean isResponse(){
		return response;
	}

	public void setAdminStatus(boolean adminStatus){
		this.adminStatus = adminStatus;
	}

	public boolean isAdminStatus(){
		return adminStatus;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UserModel{" + 
			"response = '" + response + '\'' + 
			",admin_status = '" + adminStatus + '\'' + 
			",_id = '" + id + '\'' + 
			",token = '" + token + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}