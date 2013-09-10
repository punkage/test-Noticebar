package com.kit.SDK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.trendpickr.sample.R;


@SuppressLint("ViewConstructor")
public class KitWebview extends RelativeLayout {

    private WebView kitWebView;
    final private Dialog popupDialog;
    private Activity currentActivity;
    private int prevOrientation;
    
    @SuppressLint("SetJavaScriptEnabled")
    public KitWebview(Context context, Activity activity) {
        super(context);
        this.currentActivity = activity;
        
		if(getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
			prevOrientation = Configuration.ORIENTATION_PORTRAIT;
		} else {
			prevOrientation = Configuration.ORIENTATION_LANDSCAPE;
		}
		
        popupDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        popupDialog.setContentView(R.layout.kit_webview);

        kitWebView = (WebView) popupDialog.findViewById(R.id.webviewActionView);
		kitWebView.setWebChromeClient(new WebChromeClient());
		kitWebView.setWebViewClient( new MyWebViewClient() );

        WebSettings kitWebSettings = kitWebView.getSettings();
		kitWebSettings.setJavaScriptEnabled(true);
		kitWebSettings.setDomStorageEnabled(true);
		kitWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//
		kitWebSettings.setAppCachePath(context.getCacheDir().getPath());
		kitWebSettings.setAllowFileAccess(true);//
		kitWebSettings.setAppCacheEnabled(true);//
		kitWebSettings.setBuiltInZoomControls(false);
		kitWebSettings.setSupportZoom(false);
		
        Button btnDismiss = (Button) popupDialog.findViewById(R.id.webview_closer);
        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupDialog.dismiss();
                if(prevOrientation==Configuration.ORIENTATION_LANDSCAPE){
                	currentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });

		
    }

    public void showKitWebview(String url) {
    	kitWebView.loadUrl(url);
    	
//      popupDialog.showAtLocation(50,50);
//    	popupDialog.showAsDropDown(findViewById(R.id.openpopup));
    	currentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	popupDialog.show();
    	
    }
    
    public WebView getTheWebView() {	return kitWebView; }

	private class MyWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i( "Kit", "page started: " + url );
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i( "Kit", "url loading: " + url );
			view.loadUrl(url);
	        return false;
		}
	}

	public int getScreenOrientation()
	{
	    return currentActivity.getResources().getConfiguration().orientation;
	}
}
