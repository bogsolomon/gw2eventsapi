package ca.bsolomon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

public class GW2EventApiConn {
	
	public Map<String, String> eventIdToName = new HashMap<String, String>();

	private static String[] fireEleIDs = new String[]{"6B897FF9-4BA8-4EBD-9CEC-7DCFDA5361D8",
            "5E4E9CD9-DD7C-49DB-8392-C99E1EF4E7DF",
            "2C833C11-5CD5-4D96-A4CE-A74C04C9A278",
            "FCB42C06-547F-4DA2-904B-0098E60C47BC",
            "33F76E9E-0BB6-46D0-A3A9-BE4CDFC4A3A4"};
	
	private Map<String, String> eventState = new HashMap<String, String>();
	
	public void generateEventIds() {
		HttpClient httpclient = new DefaultHttpClient();
		
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{new TrustManager()}, null);
			
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			
			httpclient = new DefaultHttpClient(ccm, httpclient.getParams());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		
		HttpGet httppost = new HttpGet("https://api.guildwars2.com/v1/event_names.json");
		
		try {
	        // Add your data
	        HttpResponse response = httpclient.execute(httppost);

	        BufferedReader rd = new BufferedReader
	        		  (new InputStreamReader(response.getEntity().getContent()));
	        		    
	        String longline = "";
    		String line = "";
    		while ((line = rd.readLine()) != null) {
    			longline+=line;
    		}
    		JSONArray result = (JSONArray) JSONSerializer.toJSON( longline );
    		
    		for (int i=0;i< result.size();i++) {
    			JSONObject obj = result.getJSONObject(i);
    			
    			String eventId = obj.getString("id");
    			String name = obj.getString("name");
    			
    			eventIdToName.put(eventId, name);
    		}
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public void queryServer(int worldId, int mapId, List<String> eventIds) {
		HttpClient httpclient = new DefaultHttpClient();
		
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{new TrustManager()}, null);
			
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			
			httpclient = new DefaultHttpClient(ccm, httpclient.getParams());
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		
		HttpGet httppost = new HttpGet("http://api.guildwars2.com/v1/events.json?world_id="+worldId+"&map_id="+mapId);
		
		try {
	        // Add your data
	        HttpResponse response = httpclient.execute(httppost);

	        BufferedReader rd = new BufferedReader
	        		  (new InputStreamReader(response.getEntity().getContent()));
	        		    
	        String longline = "";
    		String line = "";
    		while ((line = rd.readLine()) != null) {
    			longline+=line;
    		}
    		JSONObject json = (JSONObject) JSONSerializer.toJSON( longline );
    		JSONArray result = json.getJSONArray("events");
    		
    		Date date = new Date();
    		
    		for (int i=0;i< result.size();i++) {
    			JSONObject obj = result.getJSONObject(i);
    			
    			String eventId = obj.getString("event_id");
    			String state = obj.getString("state");
    			
    			if (eventIds.contains(eventId)) {
    				if (eventState.containsKey(eventId)) {
    					if (!eventState.get(eventId).equals(state)) {
    						System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state);
    					}
    				} else {
    					System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state);
    				}
    				
    				eventState.put(eventId, state);
    			}
    		}
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }	
	}
	
	public static void main(String[] args) {
		
		GW2EventApiConn conn = new GW2EventApiConn();
		
		conn.generateEventIds();
		
		while (true) {
			conn.queryServer(1013, 35, Arrays.asList(fireEleIDs));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
