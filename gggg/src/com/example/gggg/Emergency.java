package com.example.gggg;

import com.google.android.gms.maps.model.*;

public class Emergency {
	private double longitude;
	private double latitude;
	private String emergency;
	
	public Emergency(double la, double lo, String e){
		longitude = lo;
		latitude = la;
		emergency = e;
	}
	
	public LatLng getPosition(){
		LatLng m = new LatLng( latitude, longitude);
		return m;
		
	}
	
	public String getType(){
		return emergency;
	}

}
