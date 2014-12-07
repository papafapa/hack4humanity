package com.example.gggg;

import android.os.Bundle;
import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.*;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;
import android.view.View.OnClickListener;  
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MainActivity extends Activity {

	Button receiveBtn;
   Button sendBtn;
   EditText txtphoneNo;
   EditText txtMessage;
   double longitude;
   double latitude;
   Spinner spinner1;
   String Emergency;
   GoogleMap googleMap;
   Marker m1;
   public static ArrayList<Emergency> Emlist = new ArrayList<Emergency>();
   SmsReciever r;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);   
        
      googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

      spinner1 = (Spinner) findViewById(R.id.spinner1);
      List<String> list = new ArrayList<String>();
      list.add("Fire");
      list.add("Flood");
      list.add("Police");
      list.add("Medical");
       
      Emlist = new ArrayList<Emergency>();
      Emlist.add(new Emergency(41.0136, 28.9550, "Fire"));
      Emlist.add(new Emergency(40.8075, -73.9619, "Flood"));
      Emlist.add(new Emergency(40.6713, -73.9638, "Fire"));
      Emlist.add(new Emergency(40.7615, -73.9777, "Police"));
      Emlist.add(new Emergency(40.7484, -73.9857, "Medical"));
      
      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                   (this, android.R.layout.simple_spinner_item,list);
                    
      dataAdapter.setDropDownViewResource
                   (android.R.layout.simple_spinner_dropdown_item);
                    
      spinner1.setAdapter(dataAdapter);
       
      // Spinner item selection Listener  
      //addListenerOnSpinnerItemSelection();
      spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
       
      // Button click Listener 
      //addListenerOnButton();
      sendBtn = (Button) findViewById(R.id.btnSendSMS);
      txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
      
      googleMap.setMyLocationEnabled(true);

      sendBtn.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
        	 if(m1 != null){
        		 m1.remove(); 
        	 }       	 
        	getLocation(); 
            LatLng m = new LatLng(latitude, longitude);
            m1 = googleMap.addMarker(new MarkerOptions().position(m).title(Emergency));
            sendSMSMessage();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m,
                    10));
                      
         }
      });
      
      receiveBtn = (Button) findViewById(R.id.btnShowDangers);
      receiveBtn.setOnClickListener(new View.OnClickListener() {
    	  public void onClick(View view){
    		  for(int i = 0; i < Emlist.size(); i++){
    			  Emergency temp = Emlist.get(i);
    			  LatLng m = temp.getPosition();
    			  String em = temp.getType();
    			  googleMap.addMarker(new MarkerOptions().position(m).title(em));
    		  }
    	  }
      });
     
   }
   protected void sendSMSMessage() {
      Log.i("Send SMS", "");

      String phoneNo = txtphoneNo.getText().toString();
      String message = latitude + "," + longitude + '\n' + Emergency;
      System.out.println(message);
      
      try {
         SmsManager smsManager = SmsManager.getDefault();
         smsManager.sendTextMessage(phoneNo, null, message, null, null);
         Toast.makeText(getApplicationContext(), "SMS sent.",
         Toast.LENGTH_LONG).show();
      } catch (Exception e) {
         Toast.makeText(getApplicationContext(),
         "SMS faild, please try again.",
         Toast.LENGTH_LONG).show();
         e.printStackTrace();
      }
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
   
   public void getLocation(){
	   LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
	   Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	   
	   MyLocationListener ll = new MyLocationListener();
	   location = ll.getLocation();	   	 
   }
/*   
   public void addListenerOnButton() {
	   
	   sendBtn = (Button) findViewById(R.id.btnSendSMS);
       spinner1 = (Spinner) findViewById(R.id.spinner1);

       sendBtn.setOnClickListener(new OnClickListener() {

           @Override
           public void onClick(View v) {

               Toast.makeText(MainActivity.this,
                       "On Button Click : " + 
                       "\n" + String.valueOf(spinner1.getSelectedItem()) ,
                       Toast.LENGTH_LONG).show();
           }

       });

   }
 */    
	private class MyLocationListener implements LocationListener{
		
		public Location getLocation() {
			   LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
			   Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			// The minimum distance to change Updates in meters
				final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

				// The minimum time between updates in milliseconds
				final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

				
				try {
					LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					// getting GPS status
					boolean isGPSEnabled = locationManager
							.isProviderEnabled(LocationManager.GPS_PROVIDER);

					// getting network status
					boolean isNetworkEnabled = locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (!isGPSEnabled && !isNetworkEnabled) {
						// no network provider is enabled
					} else {
						//this.canGetLocation = true;
						// First get location from Network Provider
						if (isNetworkEnabled) {
							locationManager.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
							Log.d("Network", "Network");
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null) {
									latitude = location.getLatitude();
									longitude = location.getLongitude();
								}
							}
						}
						// if GPS Enabled get lat/long using GPS Services
						if (isGPSEnabled) {
							if (location == null) {
								locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER,
										MIN_TIME_BW_UPDATES,
										MIN_DISTANCE_CHANGE_FOR_UPDATES, new MyLocationListener());
								Log.d("GPS Enabled", "GPS Enabled");
								if (locationManager != null) {
									location = locationManager
											.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									if (location != null) {
										latitude = location.getLatitude();
										longitude = location.getLongitude();
									}
								}
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				return location;
			}
		
		public void onLocationChanged(Location location) {
		
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		public void onProviderEnabled(String provider) {
			
		}
		public void onProviderDisabled(String provider) {
			
		}
	}
	
	private class CustomOnItemSelectedListener implements OnItemSelectedListener {
		 
	    public void onItemSelected(AdapterView<?> parent, View view, int pos,
	            long id) {
	         
	       /* Toast.makeText(parent.getContext(), 
	                "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
	                Toast.LENGTH_LONG).show(); */
	    	Emergency = parent.getItemAtPosition(pos).toString();
	    }
	 
	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	        // TODO Auto-generated method stub
	 
	    }
	 
	}
   
}