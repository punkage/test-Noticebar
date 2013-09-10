package com.trendpickr.sample;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.kit.SDK.KitManager;
import com.kit.SDK.KitNoticebar;
import com.kit.SDK.KitWebview;

public class MainActivity extends Activity {
	
	Activity currentActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				
		
		//항상 이 지점에서 실행해줘야함.
	     KitManager.initIntro(getApplicationContext(),MainActivity.this,"1uUWf2","a8a8f7a57056bdf8a9a2bda664f4f677","");
	        
//	        <meta-data android:name="Kit_AppKey" android:value="1uUWf2" />
//	        <meta-data android:name="Kit_SecretKey" android:value="a8a8f7a57056bdf8a9a2bda664f4f677" />
//	     
//	     ActivityManager am = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
//	     ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//	     Log.i("topActivity", "CURRENT Activity ::" + cn.getShortClassName());//	   componentInfo.getPackageName();
		
	     //TODO: 가능하면 개발자 코딩 도움 없이, 현재 액티비티 컨텍스트를 알아내서 적용하면 좋으련만.
	     currentActivity = MainActivity.this;
	       
	     
		//키트 접속 버튼 클릭시.//////////////////////////////////////////////////
	    final ImageButton mainKitBtn = (ImageButton) findViewById(R.id.mainKitBtn);
	    mainKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	        	
	        	//Call by User's touch 
	    		KitManager.showKit(currentActivity);
	        }
	    });
	    //앱 첫 실행시. //////////////////////////////////////////////////
	    final Button firstKitBtn = (Button) findViewById(R.id.firstKitBtn);
	    firstKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {

	        	//실제 개발자가 사용하는 경우 없음.
				KitManager.showIntroScreen(currentActivity);
	        }
	    });
	    //키트 미션달성시 리워드 지급창.//////////////////////////////////////////////////
	    final Button completeKitBtn = (Button) findViewById(R.id.completeKitBtn);
	    completeKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {

	        	//Call programatically
	        	String thisMissionKey = "1uZHcN";
				KitManager.showCompleteScreen(currentActivity, thisMissionKey);
	        }
	    });
	    //키트 미션달성시.//////////////////////////////////////////////////

	    final Button achieveKitBtn = (Button) findViewById(R.id.achieveKitBtn);
	    achieveKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	//Call programatically

	     
	        	String thisMissionKey = "1uZHcN";
	        	Map<String, Object> sampleData = new HashMap<String, Object>();
	        	sampleData.put("level", "2");
				KitManager.missionDone(currentActivity, thisMissionKey, sampleData);
	        }
	    });
	    
	    final Button missionDoneKitBtn = (Button) findViewById(R.id.missionDoneKitBtn);
	    missionDoneKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	        	//Call programatically

	        	String thisMissionKey = "1uZHcN";
	        	Map<String, Object> sampleData = new HashMap<String, Object>();
	        	sampleData.put("level", "99");
				KitManager.missionCompleted(currentActivity, thisMissionKey, sampleData);
	        }
	    });
	    
	    //키트 알림배너 테스트용.//////////////////////////////////////////////////
	    final Button noticebarKitBtn = (Button) findViewById(R.id.noticebarKitBtn);
	    noticebarKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {

	        	KitNoticebar kitNb = new KitNoticebar();
	        	kitNb.showNoticebar(getApplicationContext(),currentActivity);
	        }
	    });	    
	    //키트 로그인 테스트용.//////////////////////////////////////////////////
	    final Button loginTestKitBtn = (Button) findViewById(R.id.loginTestKitBtn);
	    loginTestKitBtn.setOnClickListener(new Button.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	    		
	        	String kitURL = "http://54.249.121.246/Kit_FRONT/Kit-frontend/CI/login/signIn?userId=15";
	       		KitWebview kitPopup = new KitWebview(currentActivity, currentActivity);
	    		kitPopup.showKitWebview(kitURL);
	        }
	    });	    	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w("Kit", "options selected"+item.getItemId());
//		kitWebView.reload();
			
		return true;
	}
 	 
	
}
