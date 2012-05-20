package com.laposte.gae.server;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.laposte.gae.shared.ServerConfig;

public class StorageHelper {

	public static final String LETTER_TABLE = "Letter";
	public static final String FACTOR_TABLE = "Factor";
	
	protected static String getRandomFactor() {
		String name = "";
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		PreparedQuery query = datastore.prepare(new Query(FACTOR_TABLE));
		for (Entity factor : query.asIterable()) {
			name = (String) factor.getProperty("name");
			if(Math.random() > 0.5)
				break;
		}
		return name;
	}
	
	public static void store(String table, String rowId, String jsonString) {
		try {
			System.out.println("Storing data: " + jsonString);
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			String id = (String) obj.get("identifier");
			Entity letter = new Entity(table,  (String) obj.get(rowId));
			letter.setProperty("identifier",   id);
			letter.setProperty("nameSender",   (String) obj.get("nameSender"));
			letter.setProperty("nameReceiver", (String) obj.get("nameReceiver"));
			letter.setProperty("addrReceiver", (String) obj.get("addrReceiver"));	
			String state = "courrier nouvellement créé";
			if(obj.get("state")!=null)
				state = (String) obj.get("state");
			letter.setProperty("state", state);
			letter.setProperty("nameFactor", getRandomFactor());
			String price = "à définir par la poste";
			if(obj.get("price")!=null)
				price = (String) obj.get("price");
			letter.setProperty("price", price);
			String type = "inconnu";
			if(obj.get("type")!=null)
				type = (String) obj.get("type");
			letter.setProperty("type", type);
			datastore.put(letter);
			storeStateChange(id, state);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateLetter(String letterId, String data) {
		JSONObject obj = (JSONObject) JSONValue.parse(data);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
		for (Entity result : query.asIterable()) {
			String identifier = (String) result.getProperty("identifier");
			if(identifier.equals(letterId)) {
				if(obj.containsKey("state")) {
					String state = (String) obj.get("state");
					result.setProperty("state", state);
					StorageHelper.storeStateChange(letterId, state);
				}
				
				if(obj.containsKey("date"))
					result.setProperty("date", obj.get("date"));
				
				if(obj.containsKey("nameSender"))
					result.setProperty("nameSender", obj.get("nameSender"));
				
				if(obj.containsKey("nameReceiver"))
					result.setProperty("nameReceiver", obj.get("nameReceiver"));
				
				if(obj.containsKey("addrReceiver"))
					result.setProperty("addrReceiver", obj.get("addrReceiver"));
				
				if(obj.containsKey("type"))
					result.setProperty("type", obj.get("type"));
				
				if(obj.containsKey("price"))
					result.setProperty("price", obj.get("price"));
				
				datastore.put(result);
				break;
			}
		}
	}
	
	public static void storeC2DMInfo(String jsonString) {
		try {
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			ServerConfig.DeviceRegistrationID = (String) obj.get("regid");
			String user = (String) obj.get("name");
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity entity = new Entity("C2DM", user);
			entity.setProperty("regid", ServerConfig.DeviceRegistrationID);
			entity.setProperty("name", user);
			
			datastore.put(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void storeC2DMToken(int id, String jsonString) {
		try {
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			ServerConfig.DeviceRegistrationID = (String) obj.get("regid");
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity letter = new Entity("C2DM", id);
			letter.setProperty("regid", ServerConfig.DeviceRegistrationID);
			
			datastore.put(letter);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void storeStateChange(String letter, String state) {
		try {
			System.out.println("Storing new state for '"+letter+"': " + state);
			Date maDateAvecFormat=new Date(); 
			SimpleDateFormat dateStandard = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); 
			String date = dateStandard.format(maDateAvecFormat);
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Entity entity = new Entity(ServerConfig.HISTORY_TABLE, (int)(Math.random()*10000));
			entity.setProperty("letter", letter);
			entity.setProperty("state", state);
			entity.setProperty("date", date);
			
			datastore.put(entity);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String getLettersBy(String key, String value) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
		JSONArray jsonArray = new JSONArray();
		for (Entity result : query.asIterable()) {
			String attribute = (String) result.getProperty(key);
			if(attribute.equals(value)) {
				JSONObject json = new JSONObject();
				json.put("identifier", result.getProperty("identifier"));
				json.put("nameSender", result.getProperty("nameSender"));
				json.put("nameReceiver", result.getProperty("nameReceiver"));
				json.put("addrReceiver", result.getProperty("addrReceiver"));
				json.put("nameFactor", result.getProperty("nameFactor"));
				json.put("state", result.getProperty("state"));
				json.put("price", result.getProperty("price"));
				json.put("type", result.getProperty("type"));
				jsonArray.add(json);					
			}
		}
		return jsonArray.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String getFactors() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		PreparedQuery query = datastore.prepare(new Query(FACTOR_TABLE));
		JSONArray jsonArray = new JSONArray();
		for (Entity result : query.asIterable()) {
			JSONObject json = new JSONObject();
			json.put("identifier", result.getProperty("identifier"));
			json.put("name", result.getProperty("name"));
			jsonArray.add(json);
		}
		return jsonArray.toJSONString();
	}

	public static void storeFactorInfo(String jsonString) {
		try {
			System.out.println("Storing factor info: " + jsonString);
			JSONObject obj = (JSONObject) JSONValue.parse(jsonString);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			String id = (String) obj.get("identifier");
			Entity factor = new Entity(FACTOR_TABLE, id);
			factor.setProperty("identifier", id);
			factor.setProperty("name",   (String) obj.get("name"));
						
			datastore.put(factor);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
