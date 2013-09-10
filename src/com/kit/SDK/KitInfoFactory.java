package com.kit.SDK;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public class KitInfoFactory {

    protected final String kitHost = "http://54.249.121.246";
    protected final String kitShowMainPath = "Kit_FRONT/Kit-frontend/CI/front";
    protected final String kitShowIntroPath = "Kit_FRONT/Kit-frontend/CI/gate/";//intro ?????? 
    protected final String kitShowCompletePath = "Kit_FRONT/Kit-frontend/CI/mission/completeMission";//???????????? ??????.
    protected final String kitDonePath = "Kit_CMS/Kit-beta/CI/api/missionDone";//????????????/?????? ?????? ??? ????????????.
    String kitPath;
    
	protected static final String PREFS_FILE = "kit_prefs.xml";
    protected static final String PREFS_DEVICE_ID = "kit_uuid";
    protected static UUID uuid;
    private Context context;
    
    public KitInfoFactory(Context context) {
    	this.context = context;
    	
    	this.setDeviceUuid();
        
    } //DeviceUuidFactory-end if

    
    public String getKitURL() {
    	return getKitURL("main", null, null);
    }
    
    public String getKitURL(String type) {
    	return getKitURL(type, null, null);
    }    
    
    public String getKitURL(String type, String userExtID) {
    	return getKitURL(type, userExtID, null);
    }
    public String getKitURL(String type, String userExtID, String _missionKey) {
    	return getKitURL(type, userExtID, _missionKey, null);    	
    }
    
    @SuppressWarnings("unused")
    public String getKitURL(String type, String userExtID, String _missionKey, String _data) {
    	
    	if(type=="showIntro") kitPath = kitShowIntroPath;
    	else if(type=="showComplete") kitPath = kitShowCompletePath;
    	else if(type=="missionDone") kitPath = kitDonePath;
    	else kitPath = kitShowMainPath;
    	
    	//???????????? ?????? ????????? ???????????? ?????????????????? uuid, appVer?????????.
    	String appVer = this.getAppVer();
    	//String uuid = this.getDeviceUuid().toString();
    	String uuid = new String("9368214c3b5e0757c53168a8d97e4b2326e7bd9f");
    	String osVer, modelName, deviceName, operatorName, langCode, countryCode, timezone;
    	
		//TODO: Key?????????????????? ????????????.
		String keys[] = this.getKeys();
		String appKey = keys[0];
		String secretKey = keys[1];

		Uri.Builder paramBuilder = new Uri.Builder();// = Uri.parse("http://localhost").buildUpon();
		paramBuilder.appendQueryParameter("appVer", appVer);		
		paramBuilder.appendQueryParameter("uuid", uuid);
		if(type=="showIntro") {
			osVer = this.getOsVer();
			modelName = this.getModelName();
			deviceName = this.getDeviceName();
			operatorName = this.getSimOperatorName();
			langCode = this.getLangCode();
			countryCode = this.getCountryCode();
			timezone = this.getTimezone();
			
			paramBuilder.appendQueryParameter("os", "android");
			paramBuilder.appendQueryParameter("osVer", osVer);
			paramBuilder.appendQueryParameter("modelName", modelName);
			paramBuilder.appendQueryParameter("deviceName", deviceName);
			paramBuilder.appendQueryParameter("operatorName", operatorName);
			paramBuilder.appendQueryParameter("lang", langCode);
			paramBuilder.appendQueryParameter("country", countryCode);
			paramBuilder.appendQueryParameter("timezone", timezone);
			
		} else if(type=="missionDone") {
			paramBuilder.appendQueryParameter("data", _data);
		}
		if(userExtID!=null) {
			paramBuilder.appendQueryParameter("extID", userExtID);
		}
		if(_missionKey!=null) {
			paramBuilder.appendQueryParameter("missionKey", _missionKey);
		}
		String params = paramBuilder.build().toString();
		if(params.charAt(0) == '?') {
			params = params.substring(1);
		}
		Log.i("KitInfoFactory", "p : "+params);
		/***** ?appVer=1.2&uuid=xxx&... *****/
		
		KitCrypto kitCrypto = new KitCrypto(secretKey);
		String cryptedParams = "";
		try{
			cryptedParams = KitCrypto.encrypt(params);
		} catch(Exception e) {
			Log.e("KitCrypto", e.getMessage());
		}
		
		//p?????? +,/??? -,_??? ????????? ??????
		String replacedParams = cryptedParams.replace('+','-');
		replacedParams = replacedParams.replace('/','_');
		
		//already included encoding process
//		String encodedParams = KitCrypto.encodeURIcomponent(cryptedParams);
		
		Uri.Builder uribuilder = Uri.parse(kitHost).buildUpon();
		uribuilder.appendEncodedPath(kitPath);
		uribuilder.appendQueryParameter("appKey", appKey);
		uribuilder.appendQueryParameter("p", replacedParams);
		String kitURL = uribuilder.build().toString();
		
//		Log.i("kitURL",kitURL);
		return kitURL;
    }
    
    protected void setDeviceUuid() {
    	if( uuid ==null ) {
            synchronized (KitInfoFactory.class) {
                if( uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences( PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null );
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
                                uuid = (deviceId!=null) ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString() ).commit();
                    }
                }
            }
        }
    }
    protected UUID getDeviceUuid() { return uuid; }
    protected String getOsVer() { return System.getProperty("os.version"); }
    protected String getModelName() {return Build.MODEL; }
    protected String getDeviceName() {return Build.DEVICE; }
    protected String getSimOperatorName() {
		TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyMgr.getSimOperatorName() == null || telephonyMgr.getSimOperatorName().equals("")) {
			return "unknown";
		} else {
			return telephonyMgr.getSimOperatorName();
		}
	}
    protected String getAppVer() {
		String appVer="unknown";
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			appVer = i.versionName;
		} catch(NameNotFoundException e) { }
		return appVer;
	}
    protected String getLangCode() {
	    String languageCode;
    	Locale locale = Locale.getDefault();
    	if(locale!=null) {
    		languageCode= locale.getLanguage();
    	} else {
    		languageCode = context.getResources().getConfiguration().locale.getLanguage();
    	}
    	return languageCode;
    }
    protected String getCountryCode() {
	    String countryCode;
    	Locale locale = Locale.getDefault();
    	if(locale!=null) {
    		countryCode= locale.getCountry();
    	} else {
    		countryCode = context.getResources().getConfiguration().locale.getCountry();
    	}
    	return countryCode;
    }    
    @SuppressLint("SimpleDateFormat")
	protected String getTimezone() {
	     Locale locale = Locale.getDefault();
	     Calendar cal = Calendar.getInstance(locale);
	     String timezoneStr = new SimpleDateFormat("Z").format( cal.getTime() );
	     return timezoneStr;
    }
    protected String[] getKeys() {
    	String keys[] = new String[2];
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		    keys[0] = ai.metaData.getString("Kit_AppKey");
		    keys[1] = ai.metaData.getString("Kit_SecretKey");
		} catch (NameNotFoundException e) {
		    Log.e("Kit-data", "Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
		    Log.e("Kit-data", "Failed to load meta-data, NullPointer: " + e.getMessage());         
		}
		return keys;
    }
}