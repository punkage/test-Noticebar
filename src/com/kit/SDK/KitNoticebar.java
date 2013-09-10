package com.kit.SDK;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trendpickr.sample.R;


public class KitNoticebar extends Activity {

	  boolean overlapFlag = false;
	  Activity theActivity;
	  String theMissionKey;
	  Toast mToast;
	  
	  public void showNoticebar(Context _context, Activity _activity) {
		  showNoticebar(_context, _activity, null);
	  }
	  public void showNoticebar(Context _context, Activity _activity, String missionKey) {
		  
		  this.theActivity = _activity;
		  this.theMissionKey = missionKey;
		  
		  if(overlapFlag == false){ overlapFlag = true; } 
		  else {
			  Log.e("Kit","Noticebar exist!");
			  return;
		  } 		  

		  LayoutInflater inflater = (LayoutInflater) _context
     			    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     	
		  View layout = inflater.inflate(R.layout.kit_noticebar,
     			    (ViewGroup) _activity.findViewById(R.id.Kit_NB_RelativeLayout));
//		  layout.setBackgroundColor(Color.argb(220, 50, 50, 50));
		  layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mToast.cancel();
				KitManager.showCompleteScreen(theActivity, theMissionKey);
			}
		});
     	
		  ImageView image = (ImageView) layout.findViewById(R.id.Kit_NB_imageView1);
		  image.setImageResource(R.drawable.ic_launcher);
		  
		  TextView text1 = (TextView) layout.findViewById(R.id.Kit_NB_textView1);
		  text1.setText("Mission Complete!");
     	 
		  TextView text2 = (TextView) layout.findViewById(R.id.Kit_NB_textView2);
		  String completeStr="";
		  if(KitMissionContents.mName != null) completeStr = "[" + KitMissionContents.mName + "]";
		  if(KitMissionContents.mPopupTitle != null) completeStr += "\n" + KitMissionContents.mPopupTitle;
		  text2.setText(completeStr);
		  
		  TextView textR = (TextView) layout.findViewById(R.id.Kit_NB_textViewR);
		  textR.setText("Get now");
		  
		  mToast = new Toast(_context.getApplicationContext());
		  mToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 10);
		  mToast.setDuration(Toast.LENGTH_LONG);
		  mToast.setView(layout);         

		  mToast.show();
		  new Handler().postDelayed(new Runnable() {
			  	@Override
			  	public void run() { overlapFlag = false; }
		  }, Toast.LENGTH_LONG);
	  }
}
