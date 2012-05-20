package com.laposte.gae.client;

import org.json.simple.JSONObject;

public class FactorClient {

	public static String[] NAMES = new String[] {"Fabrice", "Emmanuel", "Chuck", "Marc", "François", "Benjamin", "Antoine", "Christophe"};
	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		HttpHelper.doDelete("factor");
		for(int i=0; i<NAMES.length; i++) {
			String id = ""+(int)(Math.random()*1000);
			//String name = RandomUser();
			String name = NAMES[i];
			JSONObject json = new JSONObject();
			json.put("identifier", id);
		    json.put("name", name);
		    HttpHelper.doPost("factor", json.toJSONString());
		    HttpHelper.doGet("factor?name='"+name+"'");			
		}		
	    //HttpHelper.doGet("factor?name='Marc'");
	    
	    HttpHelper.doGet("factor");
	}

	private static String RandomUser() {		
		int index = (int) (Math.random() * NAMES.length);
		return NAMES[index];
	}

}
