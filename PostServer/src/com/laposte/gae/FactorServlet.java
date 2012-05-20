package com.laposte.gae;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class FactorServlet  extends HttpServlet {

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
		    String nFactor  = extractParam(queryString, "name"); 		
		    System.out.println("query: " + queryString + ", name="+nFactor);
		    String result = "";
		    if(nFactor != null)
		    	result = StorageHelper.getLettersBy("nameFactor", nFactor);
		    else {
		    	result = StorageHelper.getFactors();
		    }
			ServletOutputStream outStream = resp.getOutputStream();			
			outStream.println(URLEncoder.encode(result, "UTF-8"));			
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
				for(int i=0; i<values.length; i++) {			
					String data = URLDecoder.decode(values[i], "UTF-8");
					outStream.println(data);
					StorageHelper.storeFactorInfo(data);
				}
				
			}else {
				String message = null;				
				boolean gotNessage = false;
						
				while ((message = reader.readLine()) != null) {		
					String data = URLDecoder.decode(message, "UTF-8");
					String encoded = URLEncoder.encode(message, "UTF-8");
					String decoded = URLDecoder.decode(encoded, "UTF-8");
					outStream.println("Read- "+data);			
					System.out.println("Read-\n     Decoded: "+data+"\n Not Decoded: "+message+"\n     Encoded: "+encoded+"\n     Decoded: "+decoded);
					StorageHelper.storeFactorInfo(data);
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
	/** Delete all factors */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query(StorageHelper.FACTOR_TABLE));
			for (Entity result : query.asIterable()) {
				datastore.delete(result.getKey());
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
