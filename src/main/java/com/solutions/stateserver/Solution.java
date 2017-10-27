package com.solutions.stateserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Solution {

	private double latitude;
	private double longitude;
	private Map<String, ArrayList<JSONArray>> states;
    private static double PI = 3.14159265;
    private static double TWOPI = 2*PI;
    private 	ArrayList<String> res = new ArrayList<String>();
    
    public Solution() {
    }
    
    // Constructor
	public Solution(double longitude, double latitude) throws ParseException, IOException {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	/**
	 * Set Longitude variable
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Set Latitude Variable
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Get Longitude Variable
	 * @return
	 */
	public double getLongitude() {
		return this.longitude;
	}
	
	/**
	 * Get Latitude variable
	 * @return
	 */
	public double getLatitude() {
		return this.latitude;
	}
	
	/**
	 * Get State given latitude and longitude has been set
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public ArrayList<String> getState() throws ParseException, IOException {
		states = convertJsonToHashMap();
		for(String key : states.keySet()) {
			ArrayList<Double> lon_array = new ArrayList<>();
			ArrayList<Double> lat_array = new ArrayList<>();
			
			for(JSONArray lat_lon_pair : states.get(key)) {

				lon_array.add((Double) lat_lon_pair.get(0));
				lat_array.add((Double) lat_lon_pair.get(1));
			}
		//	System.out.println("For State : " + key);
			if(isCoordinateInsidePolygon(longitude, latitude, lat_array, lon_array)) {
				res.add(key);
			}
			//else continue;
		}
	//	System.out.println(res);
		return res;
	}
	
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @param lat_array
	 * @param lon_array
	 * @return
	 */
	private boolean isCoordinateInsidePolygon(double longitude, double latitude, ArrayList<Double> lat_array, ArrayList<Double> lon_array){       
		double angle=0;
		
		double lat_1, lon_1, lat_2, lon_2;
		
		int n = lat_array.size();
		
		/**
		 * The following algorithm creates two vectors from the given pair of latitude and longitude
		 * and each edge of the polygon created by the borders.
		 * 
		 * From the two vectors we can find the angle between them on a plane
		 * The sum of all the angles(in radians) in the polygon will be either above the value of PI or below the value of PI
		 * If it is greater than PI it exists inside the border
		 * Else it exists outside the border
		 */
		for (int i=0; i< n; i++) {
		    lat_1 = lat_array.get(i) - latitude; // Get y of vector 1 where y = latitude
		   // System.out.println("Lat 1 = " + lat_1);
		    lon_1 = lon_array.get(i) - longitude; // Get x of vector 1 where x = longitude
		   // System.out.println("Lon 1 = " + lon_1);

		    lat_2 = lat_array.get((i+1)%n) - latitude; // Get y of vector 2 where y = latitude
		   // System.out.println("Lat 2 = " + lat_2);

		    lon_2 = lon_array.get((i+1)%n) - longitude; // Get x of vector 2 where x = longitude
		   // System.out.println("Lon 2 = " + lon_2);

		    // Get the angle between the two vectors and get the sum of the angles
		    angle += getAngleBetweenVectors(lat_1, lon_1, lat_2, lon_2);  
		}
	   // System.out.println("Angle = " + angle);

		//If the angle is less than PI it does not exist in the polygon
		if (Math.abs(angle) < PI)
			return false;
		else
			return true;
		}

		/**
		 * Get the angle between the two vectors on a plane
		 * @param y1
		 * @param x1
		 * @param y2
		 * @param x2
		 * @return delta_theta
		 */
		
	private  double getAngleBetweenVectors(double y1, double x1, double y2, double x2){
		 
		  double theta_1 = Math.atan2(y1,x1); // The angle between vector 1 and the plane
		  double theta_2 = Math.atan2(y2,x2); // The angle between vector 2 and the plane
		  double delta_theta = theta_2 - theta_1; // The difference between the angles
		   
		  // If greater than the value of Pi, keep subtracting delta theta from 2PI
		  while (delta_theta > PI)
			  delta_theta -= TWOPI;
		  // If delta_thera is less than negative pi keep adding 2pi until it isn't
		  while (delta_theta < -PI)
		      delta_theta += TWOPI;

		   return(delta_theta);
		}

	/**
	 * Convert JSON to HashMap
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private Map<String, ArrayList<JSONArray>> convertJsonToHashMap() throws ParseException, IOException {
		// Initialize parser and jsonobject to read each line as an individual json
		JSONParser parser = new JSONParser();
		JSONObject object = new JSONObject();
		
		// Initialize a HashMap for the states
		Map<String, ArrayList<JSONArray>> states = new HashMap<>();
		// Read each line in file
		FileInputStream fstream = new FileInputStream("src/main/resources/states.json");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  object = (JSONObject) parser.parse(strLine);
		  String state = (String) object.get("state");
		  JSONArray border = (JSONArray) object.get("border");
		  states.put(state, border);
		}
		//Close the input stream
		br.close();

		// Return the hashmap
		return states;
	}
}
