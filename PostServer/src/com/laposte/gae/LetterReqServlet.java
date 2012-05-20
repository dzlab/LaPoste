package com.laposte.gae;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;

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

public class LetterReqServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String LETTER_TABLE = "Letter";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=utf-8");
		    
			ServletOutputStream outStream = resp.getOutputStream();				
			//outStream.println("OK, got your GET");		
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();		
			PreparedQuery query = datastore.prepare(new Query(LETTER_TABLE));
			JSONArray jsonArray = new JSONArray();
			for (Entity result : query.asIterable()) {
				String identifier   = (String) result.getProperty("identifier");
				String nameSender   = (String) result.getProperty("nameSender");
				String nameReceiver = (String) result.getProperty("nameReceiver");
				JSONObject json = new JSONObject();
				json.put("identifier", identifier);
				json.put("nameSender", nameSender);
				json.put("nameReceiver", nameReceiver);
				json.put("addrReceiver", result.getProperty("addrReceiver"));
				json.put("state", result.getProperty("state"));
				json.put("price", result.getProperty("price"));
				json.put("type", result.getProperty("type"));
				jsonArray.add(json);
			}
			outStream.println(URLEncoder.encode(jsonArray.toJSONString(), "UTF-8"));			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));				
			ServletOutputStream outStream = resp.getOutputStream();	
					
			String[] values = req.getParameterValues("content");
			if(values != null) {		
				JSONObject jsonReq = (JSONObject) JSONValue.parse(values[0]);
				
				String idLetter = (String) jsonReq.get("idLetter");				
				String nSender = (String) jsonReq.get("nameSender");			    
				String nReceiver = (String) jsonReq.get("nameReceiver");			    
			    String nFactor = (String) jsonReq.get("nameFactor");
			    			
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
							}else {
								String identifierLetter = (String) result.getProperty("identifier");
								if(identifierLetter.equals(idLetter)) {
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
				}
				outStream.println(jsonArray.toJSONString());
			
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
