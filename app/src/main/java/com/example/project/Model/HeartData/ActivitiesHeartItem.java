package com.example.project.Model.HeartData;

import com.google.gson.annotations.SerializedName;

public class ActivitiesHeartItem{

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("value")
	private Value value;

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setValue(Value value){
		this.value = value;
	}

	public Value getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"ActivitiesHeartItem{" + 
			"dateTime = '" + dateTime + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}