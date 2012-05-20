package com.laposte.gae;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.laposte.gae.server.StorageHelper;

public class C2DMServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
		
	protected String extractParam(String query, String param) {
		String value = null;
		try {
			int index = query.indexOf(param);
			if(index != -1) {
			    int idxQuoteStart = query.indexOf("'", index);
			    int idxQuoteEnd = query.indexOf("'", idxQuoteStart+1);
			    value = query.substring(idxQuoteStart+1, idxQuoteEnd);							
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String queryString = req.getQueryString();   // d=789		    
		    
		    String user  = extractParam(queryString, "name"); 
		    
		    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query("C2DM"));
			JSONArray jsonArray = new JSONArray();
			for (Entity result : query.asIterable()) {
				String name   = (String) result.getProperty("name");
				if(name.equals(user)) {
					JSONObject json = new JSONObject();
					json.put("name", user);
					json.put("regid", result.getProperty("regid"));
					jsonArray.add(json);					
				}
			}
			
			ServletOutputStream outStream = resp.getOutputStream();				
			//outStream.println("OK, got your GET");					
			outStream.println(jsonArray.toJSONString());			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	/** After Android client had registered to C2DM server and got a registration id, it post it here to be saved */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));				
			ServletOutputStream outStream = resp.getOutputStream();				
					
			String[] values = req.getParameterValues("content");
			if(values != null) {		
				for(int i=0; i<values.length; i++)			
					outStream.println(values[i]);
				StorageHelper.storeC2DMInfo(values[0]);
				
			}else {
				String message = null;				
				boolean gotNessage = false;
						
				while ((message = reader.readLine()) != null) {				
					outStream.println("Read "+message);			
					StorageHelper.storeC2DMInfo(message);
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
