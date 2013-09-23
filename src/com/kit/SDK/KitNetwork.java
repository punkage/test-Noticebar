package com.kit.SDK;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class KitNetwork {

	/*	public static String toJSON(Object object) {

		JSONObject jArrayFacebookData = new JSONObject();
	    JSONObject jObjectType = new JSONObject();
	
	    // put elements into the object as a key-value pair
	    jObjectType.put("type", "facebook_login");
	
	    jArrayFacebookData.put("system", jObjectType);
	
	    // 2nd array for user information
	    JSONObject jObjectData = new JSONObject();

	    // Create Json Object using Facebook Data
	    jObjectData.put("facebook_user_id", id);
	    jObjectData.put("first_name", first_name);
	
	    jArrayFacebookData.put("data", jObjectData);
	    return jArrayFacebookData.toString();

	}	    */
	//////////////////////////////////////////////////////////////////////////////////////////
	public static String toJSONstr(Object object) throws JSONException {
		return toJSON(object).toString();
	}
	
	@SuppressWarnings("rawtypes")
    public static Object toJSON(Object object) throws JSONException {
        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            
			Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable)object)) {
                json.put(value);
            }
            return json;
        } else {
            return object;
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Map<String, Object> getMap(JSONObject object, String key) throws JSONException {
        return toMap(object.getJSONObject(key));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
   
		Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }
 
    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }
    
	public static Map<String, Object> parseJSONtoMap(String jsonStr) {
		JSONObject jObj = null;
	    try {
	            jObj = new JSONObject(jsonStr);
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	    Map<String, Object> mapFromJSON;
	    try {
	    	mapFromJSON = toMap(jObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mapFromJSON = null;
		}
	    	return mapFromJSON;
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	public static HttpResponse sendJSONData(String url, String encoding, Map param){
		HttpClient client = new DefaultHttpClient(); 
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); 
        HttpResponse response = null; 
        try{ 
            HttpPost post = new HttpPost(url); 
            post.setHeader(HTTP.CONTENT_TYPE,"application/json");
            
            String jsonString = null;
            if(param != null){
             jsonString = jutil.getJSONObjectString(param);
          
            StringEntity se = new StringEntity(jsonString, encoding);
                post.setEntity(se);     
            }           
           
            response = client.execute(post);            
        }catch(Exception e){ 
         Log.e("Error", "sendJSONData", e);
        }  
        
        return response;
 }
	 */
	/*
	public static void requestAsyncGET(String url) {
		Log.w("Kit async","request started");
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {
		        Log.w("Kit async",response);
		    }
		});
	}
	*/
	
	private static class RequestGetData extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... url) {
			// TODO Auto-generated method stub
			Log.i("KitNetwork-url",url[0].toString());
			String responseStr = null;

		//TODO: ??????????????? ???????????? ???????????????. ????????????.
			DefaultHttpClient httpclient = new DefaultHttpClient();

			//TODO: ��������������� ������������ ���������������. ������������.
		    HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

		        public boolean retryRequest(
		                IOException exception, 
		                int executionCount,
		                HttpContext context) {
		            if (executionCount > 3) {
		                // Do not retry if over max retry count
		                return false;
		            }
		            if (exception instanceof InterruptedIOException) {
		                // Timeout
		                return false;
		            }
		            if (exception instanceof UnknownHostException) {
		                // Unknown host
		                return false;
		            }
		            if (exception instanceof ConnectException) {
		                // Connection refused
		                return false;
		            }
		            if (exception instanceof SSLException) {
		                // SSL handshake exception
		                return false;
		            }
		            HttpRequest request = (HttpRequest) context.getAttribute(
		                    ExecutionContext.HTTP_REQUEST);
		            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest); 
		            if (idempotent) {
		                // Retry if the request is considered idempotent 
		                return true;
		            }
		            return false;
		        }

		    };
		    httpclient.setHttpRequestRetryHandler(myRetryHandler);
		    
		    HttpGet httpGet = new HttpGet(url[0].toString());

		    try {
		        // Execute HTTP Get Request
		        HttpResponse response = httpclient.execute(httpGet);
		        
		        HttpEntity resEntityGet = response.getEntity();  
		        if (resEntityGet != null) {  
		            //do something with the response
		        	responseStr = EntityUtils.toString(resEntityGet);
		            Log.i("KitNetwork-responseStr",responseStr);
		        }
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    	e.printStackTrace();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    	e.printStackTrace();
		    }
		    
		    return responseStr;
		}
		
	}
	
	public static String requestGET(String url) {
		String resultData = null;
		try {
			resultData = new RequestGetData().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("KitNetwork-now",resultData.toString());
		return resultData;
	} 
	
	public static void requestPOST() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("id", "12345"));
	        nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity resEntity = response.getEntity();  
            if (resEntity != null) {    
                Log.i("KitNetwork",EntityUtils.toString(resEntity));
            }
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	} 
}
