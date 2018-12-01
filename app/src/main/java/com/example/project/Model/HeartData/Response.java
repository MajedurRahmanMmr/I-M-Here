package com.example.project.Model.HeartData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("activities-heart")
	private List<ActivitiesHeartItem> activitiesHeart;

	public void setActivitiesHeart(List<ActivitiesHeartItem> activitiesHeart){
		this.activitiesHeart = activitiesHeart;
	}

	public List<ActivitiesHeartItem> getActivitiesHeart(){
		return activitiesHeart;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"activities-heart = '" + activitiesHeart + '\'' + 
			"}";
		}
}