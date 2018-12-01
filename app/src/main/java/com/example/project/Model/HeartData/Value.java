package com.example.project.Model.HeartData;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Value{

	@SerializedName("customHeartRateZones")
	private List<Object> customHeartRateZones;

	@SerializedName("heartRateZones")
	private List<HeartRateZonesItem> heartRateZones;

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

	@Override
 	public String toString(){
		return 
			"Value{" + 
			"customHeartRateZones = '" + customHeartRateZones + '\'' + 
			",heartRateZones = '" + heartRateZones + '\'' + 
			"}";
		}
}