package com.laposte.gae;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class DirectoryServlet extends HttpServlet {
	
	public static final String LETTER_TABLE = "Letter";
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reqUrl = req.getRequestURL().toString();
	    String queryString = req.getQueryString();   // d=789
	    System.out.println("query: " + queryString);
	    int index = -1;
	    int idxQuoteStart = -1;
	    int idxQuoteEnd = -1;
	    
	    index = queryString.indexOf("nameSender=");
	    String nSender = "";
	    if(index != -1) {
	    	idxQuoteStart = queryString.indexOf("\"", index);
		    idxQuoteEnd = queryString.indexOf("\"", idxQuoteStart+1);
		    nSender = queryString.substring(idxQuoteStart+1, idxQuoteEnd);	
	    }
	    
	    index = queryString.indexOf("nameReceiver=");
	    String nReceiver = "";
	    if(index != -1) {
	    	idxQuoteStart = queryString.indexOf("\"", index);
		    idxQuoteEnd = queryString.indexOf("\"", idxQuoteStart+1);
		    nReceiver = queryString.substring(idxQuoteStart+1, idxQuoteEnd);	
	    }
	    
	    index = queryString.indexOf("nameFactor=");
	    String nFactor = "";
	    if(index != -1) {
	    	idxQuoteStart = queryString.indexOf("\"", index);
		    idxQuoteEnd = queryString.indexOf("\"", idxQuoteStart+1);	
		    nFactor = queryString.substring(idxQuoteStart+1, idxQuoteEnd);
	    }
	    
		ServletOutputStream outStream = resp.getOutputStream();				
		//outStream.println("OK, got your GET");		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
		PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
		JSONArray jsonArray = new JSONArray();
		
		for (Entity result : query.asIterable()) {
			String nameSender = (String) result.getProperty("nameSender");
			if(nameSender.equals(nSender)) {
				JSONObject json = new JSONObject();
				json.put("identifier", result.getProperty("identifier"));
				json.put("nameSender", result.getProperty("nameSender"));
				json.put("nameReceiver", result.getProperty("nameReceiver"));
				json.put("addrReceiver", result.getProperty("addrReceiver"));
				json.put("state", result.getProperty("state"));
				jsonArray.add(json);
			} else {
				String nameReceiver = (String) result.getProperty("nameReceiver");
				if(nameReceiver.equals(nReceiver)) {
					JSONObject json = new JSONObject();
					json.put("identifier", result.getProperty("identifier"));
					json.put("nameSender", result.getProperty("nameSender"));
					json.put("nameReceiver", result.getProperty("nameReceiver"));
					json.put("addrReceiver", result.getProperty("addrReceiver"));
					json.put("state", result.getProperty("state"));
					jsonArray.add(json);
				} else {
					String nameFactor = (String) result.getProperty("nameFactor");
					if(nameFactor.equals(nFactor)) {
						JSONObject json = new JSONObject();
						json.put("identifier", result.getProperty("identifier"));
						json.put("nameSender", result.getProperty("nameSender"));
						json.put("nameReceiver", result.getProperty("nameReceiver"));
						json.put("addrReceiver", result.getProperty("addrReceiver"));
						json.put("state", result.getProperty("state"));
						jsonArray.add(json);
					}
				}
			}
		}
		outStream.println(jsonArray.toJSONString());
	}

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));				
			ServletOutputStream outStream = resp.getOutputStream();				
					
			String[] values = req.getParameterValues("content");
			if(values != null) {		
				for(int i=0; i<values.length; i++)			
					outStream.println(values[i]);
				//StorageManager.store(values[0]);
				JSONObject obj = (JSONObject) JSONValue.parse(values[0]);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Entity client = new Entity("client");
				client.setProperty("name", obj.get("name"));
				client.setProperty("address", obj.get("address"));
				datastore.put(client);
				
			}else {
				String message = null;				
				boolean gotNessage = false;
						
				while ((message = reader.readLine()) != null) {				
					outStream.println("Read "+message);				
					gotNessage = true;				
				}
						
				if (!gotNessage)				
					outStream.println("Got no message");
				outStream.println(req.getParameterNames().toString());
				for (Enumeration params = req.getParameterNames() ; params.hasMoreElements() ;) {         
					outStream.println(params.nextElement().toString());     
				}			
				outStream.println("getContentLength: " + req.getContentLength());				
				outStream.println("getContentType: " + req.getContentType());
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
