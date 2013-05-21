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

public class SouthSunGW2EventApiConn {
	
	public Map<String, String> eventIdToName = new HashMap<String, String>();

	private static String[] fireEleIDs = new String[]{"659149D4-43EC-4DCB-A6BB-0B2D402B537B",
		"72F93CD8-94AC-4234-8D86-996CCAC76A46",
		"81F89AC2-4764-49CB-A4F1-8B7546201DC7",
		"FF71DE90-423B-4685-A343-83487A330C7A",
		"B5021202-8B05-40AF-97C6-9C623EAB2956",
		"7812A763-0AD1-4FD4-8E23-9DCB92A9E679",
		"7C37907C-61D2-460C-B47F-A699C8A7980D",
		"CB6ECEDE-722B-40B5-963C-EFF612CEBD46",
		"0AA86C2B-5D15-4C60-96C5-09C463E1E40A",
		"E48AEF1D-C988-4B4F-840E-7E44D712578E",
		"E3CA3AEC-13E4-48A5-AFC6-B8AA5A1D88B1",
		"81A07F7D-42F6-49EE-8E1C-97D418C43770",
		"B13EA882-065D-470A-8A9D-10A395373C68",
		"42B3E918-03F3-4070-8115-902773C13C0D",
		"5B7F1A45-27DB-45D5-803F-57B6FBB5DBE8",
		"AB17125F-A0D6-4103-99FD-FF236D03679A",
		"F91C5E5D-F654-43B3-9DEE-EE502A7C5E81",
		"4AB50247-DB57-4C6D-9922-7E4D4FFC1F58",
		"5C591A3E-900C-4B74-94CB-E2AD4E98B79A",
		"F6ECAE88-EE84-4B64-8A4E-0BFC3FABE486",
		"68CFE130-BB99-4297-B376-0B9E4E0B1E5B",
		"510DBF11-BF20-4E38-A763-BAD9A0307CB5",
		"D3720F92-42E1-4A62-95FF-0C1EBED69FE6",
		"C98B3487-A842-4DF3-B712-7520DA227DBE",
		"821557D1-CFEB-4D6D-9DDA-D4C17CE6794B",
		"F63CFAF5-14AB-4DB5-9FCC-4CCC5C8658BD",
		"C037718C-0300-4357-8071-C543B9C9BE57",
		"834B249D-8541-4DE9-B040-845C0A349303",
		"F6563016-3832-4EBA-82F1-535BFF96422D",
		"9716D99B-F059-4BED-B600-B48ACAFD7449",
		"FB70B463-05D7-4A55-857F-CD1E922E5B8D",
		"A2A1972E-0C50-4666-ACC7-0C5C0531BF31"};
	
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
    						if (eventId.equals("81F89AC2-4764-49CB-A4F1-8B7546201DC7")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Driftglass]");
    						} else if(eventId.equals("72F93CD8-94AC-4234-8D86-996CCAC76A46")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Kiel]");
    						} else if(eventId.equals("AB17125F-A0D6-4103-99FD-FF236D03679A")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [North East]");
    						}  else if(eventId.equals("4AB50247-DB57-4C6D-9922-7E4D4FFC1F58")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Drift Glass]");
    						} else if(eventId.equals("C98B3487-A842-4DF3-B712-7520DA227DBE")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [North East]");
    						} else if(eventId.equals("42B3E918-03F3-4070-8115-902773C13C0D")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Kiel]");
    						} else if(eventId.equals("5C591A3E-900C-4B74-94CB-E2AD4E98B79A")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Lion]");
    						} else if(eventId.equals("F6ECAE88-EE84-4B64-8A4E-0BFC3FABE486")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Owain Refuges]");
    						} else if(eventId.equals("5B7F1A45-27DB-45D5-803F-57B6FBB5DBE8")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Lion Defense]");
    						} else if(eventId.equals("68CFE130-BB99-4297-B376-0B9E4E0B1E5B")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Kiel Defense]");
    						} else if(eventId.equals("F91C5E5D-F654-43B3-9DEE-EE502A7C5E81")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Owain Skirm]");
    						} else if(eventId.equals("510DBF11-BF20-4E38-A763-BAD9A0307CB5")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Camp Karka Skirm]");
    						}  else if(eventId.equals("F63CFAF5-14AB-4DB5-9FCC-4CCC5C8658BD")) {
    							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   [Kiel Skirm]");
    						} else {
	    						System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" changed state from: "+ eventState.get(eventId) +" to :"+state+"   ["+eventId +"]");
    						}
    					}
    				} else {
    					if (eventId.equals("81F89AC2-4764-49CB-A4F1-8B7546201DC7")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Driftglass]");
						} else if(eventId.equals("72F93CD8-94AC-4234-8D86-996CCAC76A46")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Kiel]");
						} else if(eventId.equals("AB17125F-A0D6-4103-99FD-FF236D03679A")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [North East]");
						}  else if(eventId.equals("4AB50247-DB57-4C6D-9922-7E4D4FFC1F58")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Drift Glass]");
						} else if(eventId.equals("C98B3487-A842-4DF3-B712-7520DA227DBE")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [North East]");
						} else if(eventId.equals("42B3E918-03F3-4070-8115-902773C13C0D")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Kiel]");
						} else if(eventId.equals("5C591A3E-900C-4B74-94CB-E2AD4E98B79A")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Lion]");
						} else if(eventId.equals("F6ECAE88-EE84-4B64-8A4E-0BFC3FABE486")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Owain Refuges]");
						} else if(eventId.equals("5B7F1A45-27DB-45D5-803F-57B6FBB5DBE8")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Lion Defense]");
						} else if(eventId.equals("68CFE130-BB99-4297-B376-0B9E4E0B1E5B")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Kiel Defense]");
						} else if(eventId.equals("F91C5E5D-F654-43B3-9DEE-EE502A7C5E81")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Owain Skirm]");
						} else if(eventId.equals("510DBF11-BF20-4E38-A763-BAD9A0307CB5")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"    [Camp Karka Skirm]");
						}  else if(eventId.equals("F63CFAF5-14AB-4DB5-9FCC-4CCC5C8658BD")) {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   [Kiel Skirm]");
						} else {
							System.out.println(date.toString()+" - "+eventIdToName.get(eventId)+" initial state : "+state +"   ["+eventId +"]");
						}
    					
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
		
		SouthSunGW2EventApiConn conn = new SouthSunGW2EventApiConn();
		
		conn.generateEventIds();
		
		while (true) {
			conn.queryServer(1013, 873, Arrays.asList(fireEleIDs));
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
