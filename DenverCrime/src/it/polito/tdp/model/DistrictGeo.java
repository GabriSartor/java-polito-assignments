package it.polito.tdp.model;

import com.javadocmd.simplelatlng.LatLng;

public class DistrictGeo {
	private int districtId;
	private LatLng districtCenter;
	public DistrictGeo(int districtId, LatLng districtCenter) {
		super();
		this.districtId = districtId;
		this.districtCenter = districtCenter;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	public LatLng getDistrictCenter() {
		return districtCenter;
	}
	public void setDistrictCenter(LatLng districtCenter) {
		this.districtCenter = districtCenter;
	}

}
