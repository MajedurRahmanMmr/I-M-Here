package com.example.project.Model.HeartModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ActivitiesHeartIntraday{

	@SerializedName("datasetType")
	private String datasetType;

	@SerializedName("dataset")
	private List<DatasetItem> dataset;

	@SerializedName("datasetInterval")
	private int datasetInterval;

	public void setDatasetType(String datasetType){
		this.datasetType = datasetType;
	}

	public String getDatasetType(){
		return datasetType;
	}

	public void setDataset(List<DatasetItem> dataset){
		this.dataset = dataset;
	}

	public List<DatasetItem> getDataset(){
		return dataset;
	}

	public void setDatasetInterval(int datasetInterval){
		this.datasetInterval = datasetInterval;
	}

	public int getDatasetInterval(){
		return datasetInterval;
	}

	@Override
 	public String toString(){
		return 
			"ActivitiesHeartIntraday{" + 
			"datasetType = '" + datasetType + '\'' + 
			",dataset = '" + dataset + '\'' + 
			",datasetInterval = '" + datasetInterval + '\'' + 
			"}";
		}
}