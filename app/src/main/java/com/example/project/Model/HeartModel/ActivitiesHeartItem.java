package com.example.project.Model.HeartModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ActivitiesHeartItem{

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("customHeartRateZones")
	private List<Object> customHeartRateZones;

	@SerializedName("heartRateZones")
	private List<HeartRateZonesItem> heartRateZones;

	@SerializedName("value")
	private String value;

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setCustomHeartRateZones(List<Object> customHeartRateZones){
		this.customHeartRateZones = customHeartRateZones;
	}

	public List<Object> getCustomHeartRateZones(){
		return customHeartRateZones;
	}

	public void setHeartRateZones(List<HeartRateZonesItem> heartRateZones){
		this.heartRateZones = heartRateZones;
	}

	public List<HeartRateZonesItem> getHeartRateZones(){
		return heartRateZones;
	}

	public void setValue(String value){
		this.value = value;
	}

	public String getValue(){
		return value;
	}

	@Override
 	public String toString(){
		return 
			"ActivitiesHeartItem{" + 
			"dateTime = '" + dateTime + '\'' + 
			",customHeartRateZones = '" + customHeartRateZones + '\'' + 
			",heartRateZones = '" + heartRateZones + '\'' + 
			",value = '" + value + '\'' + 
			"}";
		}
}