package com.laposte.gae;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.laposte.gae.shared.ServerConfig;

public class StateHistoryServlet extends HttpServlet {

	private static final long serialVersionUID = -75427338947214090L;
	
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
			
			String queryString = req.getQueryString();   		    		    
		    String letterId  = extractParam(queryString, "id");
		    ServletOutputStream outStream = resp.getOutputStream();				
					
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			Query query = new Query(ServerConfig.HISTORY_TABLE);
			query.addSort("date", SortDirection.ASCENDING);
			PreparedQuery pQuery = datastore.prepare(query);
			JSONArray jsonArray = new JSONArray();
			for (Entity result : pQuery.asIterable()) {
				String identifier   = (String) result.getProperty("letter");
				if(identifier.equals(letterId)) {
					JSONObject json = new JSONObject();
					json.put("letter", identifier);
					json.put("state", result.getProperty("state"));
					json.put("date", result.getProperty("date"));
					jsonArray.add(json);					
				}
			}
			outStream.println(URLEncoder.encode(jsonArray.toJSONString(), "UTF-8"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Delete all letters' state history */
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query(ServerConfig.HISTORY_TABLE));
			for (Entity result : query.asIterable()) {
				datastore.delete(result.getKey());
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
