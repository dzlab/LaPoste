package com.laposte.gae.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.laposte.gae.shared.ServerConfig;

public class C2DMHelper {

	public static void notify(String jsonString) {
		try {
			String RegId = ServerConfig.DeviceRegistrationID;
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			String user = (String) obj.get("identifier");
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query("C2DM"));
			for (Entity result : query.asIterable()) {
				String name   = (String) result.getProperty("name");
				if(name.equals(user)) {					
					 RegId = (String) result.getProperty("regid");
				}
			}
			
		}catch(Exception e) {
			
		}
	}
}
