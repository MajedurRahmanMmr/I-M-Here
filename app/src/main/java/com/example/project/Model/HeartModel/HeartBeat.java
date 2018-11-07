package com.example.project.Model.HeartModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HeartBeat{

	@SerializedName("activities-heart-intraday")
	private ActivitiesHeartIntraday activitiesHeartIntraday;

	@SerializedName("activities-heart")
	private List<ActivitiesHeartItem> activitiesHeart;

	public void setActivitiesHeartIntraday(ActivitiesHeartIntraday activitiesHeartIntraday){
		this.activitiesHeartIntraday = activitiesHeartIntraday;
	}

	public ActivitiesHeartIntraday getActivitiesHeartIntraday(){
		return activitiesHeartIntraday;
	}

	public void setActivitiesHeart(List<ActivitiesHeartItem> activitiesHeart){
		this.activitiesHeart = activitiesHeart;
	}

	public List<ActivitiesHeartItem> getActivitiesHeart(){
		return activitiesHeart;
	}

	@Override
 	public String toString(){
		return 
			"HeartBeat{" + 
			"activities-heart-intraday = '" + activitiesHeartIntraday + '\'' + 
			",activities-heart = '" + activitiesHeart + '\'' + 
			"}";
		}
}