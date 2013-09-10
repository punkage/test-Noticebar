package com.kit.SDK;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class KitPopupDialog extends Dialog{
	private final Context mContext;
	
	public KitPopupDialog(Context context) {
	    super(context);
	    mContext=context;
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	public void showAtLocation(int x,int y)
	{
	    WindowManager.LayoutParams wmlp = getWindow().getAttributes();
	    wmlp.gravity = Gravity.TOP | Gravity.LEFT;
	    wmlp.x = x;
	    wmlp.y = y;
	    show();
	}
	public void showAsDropDown(View view)
	{
	    float density = mContext.getResources().getDisplayMetrics().density;
	    WindowManager.LayoutParams wmlp = getWindow().getAttributes();
	    int[] location = new int[2];
	    view.getLocationInWindow(location);
	    wmlp.gravity = Gravity.TOP | Gravity.LEFT;
	    wmlp.x = location[0]+(int)(view.getWidth()/density);
	    wmlp.y = location[1]+(int)(view.getHeight()/density);
	    show();
	}

}