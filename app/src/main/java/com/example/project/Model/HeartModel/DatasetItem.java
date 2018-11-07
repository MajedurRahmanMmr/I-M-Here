package com.example.project.Model.HeartModel;

import com.google.gson.annotations.SerializedName;

public class DatasetItem{

	@SerializedName("time")
	private String time;

	@SerializedName("value")
	private int value;

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setValue(int value){
		this.value = value;
	}

	public int getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"DatasetItem{" + 
			"time = '" + time + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}