package com.laposte.gae;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
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
import com.laposte.gae.server.C2DMHelper;
import com.laposte.gae.server.StorageHelper;

public class LetterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final String LETTER_TABLE = "Letter";
		
	protected String extractParam(String query, String param) {
		String value = null;
		try {
			int index = query.indexOf(param);
			if(index != -1) {
			    int idxQuoteStart = query.indexOf("'", index);
			    int idxQuoteEnd = query.indexOf("'", idxQuoteStart+1);
			    value = query.substring(idxQuoteStart+1, idxQuoteEnd);							
			}
			if(value != null)
				value = URLDecoder.decode(value, "UTF-8");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=utf-8");
			
		    String queryString = req.getQueryString();   		    
		    String letterId  = extractParam(queryString, "id");
		    String nSender   = extractParam(queryString, "sender");
		    String nReceiver = extractParam(queryString, "receiver");			
		    
		    System.out.println("GET /letter?" + queryString + " -> id="+letterId+", sender="+nSender+", receiver="+nReceiver);
		    
			ServletOutputStream outStream = resp.getOutputStream();				
			//outStream.println("OK, got your GET");		
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
			JSONArray jsonArray = new JSONArray();
			for (Entity result : query.asIterable()) {
				String identifier   = (String) result.getProperty("identifier");
				String nameSender   = (String) result.getProperty("nameSender");
				String nameReceiver = (String) result.getProperty("nameReceiver");
				if(identifier.equals(letterId) || nameSender.equals(nSender) || nameReceiver.equals(nReceiver)) {
					JSONObject json = new JSONObject();
					json.put("identifier", identifier);
					json.put("nameSender", nameSender);
					json.put("nameReceiver", nameReceiver);
					json.put("addrReceiver", result.getProperty("addrReceiver"));
					json.put("state", result.getProperty("state"));
					json.put("type", result.getProperty("type"));
					json.put("price", result.getProperty("price"));
					jsonArray.add(json);					
				}
			}
			outStream.println(URLEncoder.encode(jsonArray.toJSONString(), "UTF-8"));			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

		
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=utf-8");
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));				
			ServletOutputStream outStream = resp.getOutputStream();				
					
			String[] values = req.getParameterValues("content");
			if(values != null) {		
				for(int i=0; i<values.length; i++)			
					outStream.println(URLDecoder.decode(values[i], "UTF-8"));
				String data = URLDecoder.decode(values[0], "UTF-8");
				StorageHelper.store(LETTER_TABLE, "identifier", data);
				C2DMHelper.notify(data);
				
			}else {
				String message = null;				
				boolean gotNessage = false;
						
				while ((message = reader.readLine()) != null) {		
					String data = URLDecoder.decode(message, "UTF-8");
					String encoded = URLEncoder.encode(message, "UTF-8");
					String decoded = URLDecoder.decode(encoded, "UTF-8");
					outStream.println("Read- "+data);			
					System.out.println("Read-\n     Decoded: "+data+"\n Not Decoded: "+message+"\n     Encoded: "+encoded+"\n     Decoded: "+decoded);
					StorageHelper.store(LETTER_TABLE, "identifier", data);
					C2DMHelper.notify(data);
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
	
	public void doPut(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String queryString = req.getQueryString();   
		    System.out.println("doPut: query " + queryString);
		    String letterId  = extractParam(queryString, "id"); 
		    BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
		    ServletOutputStream outStream = resp.getOutputStream();	
		    String[] values = req.getParameterValues("content");
			if(values != null) {		
				for(int i=0; i<values.length; i++) {			
					outStream.println(values[i]);
					StorageHelper.updateLetter(letterId, values[i]);
				}							
			}else {
				String message = null;				
				boolean gotNessage = false;
						
				while ((message = reader.readLine()) != null) {		
					outStream.println("Updating with "+message);	
					String data = URLDecoder.decode(message, "UTF-8");
					StorageHelper.updateLetter(letterId, data);
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
	
	/** Delete all letters */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
			for (Entity result : query.asIterable()) {
				datastore.delete(result.getKey());
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
