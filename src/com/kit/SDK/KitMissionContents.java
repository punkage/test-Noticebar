package com.kit.SDK;

import java.util.Map;

//????????? ????????? ????????? ?????? ????????? ????????????.
public class KitMissionContents {
	public static String mName=null, mPopupTitle=null, kcoin=null, itemName=null, itemCount=null, itemImage=null;
	private static boolean isChecked = false;
	
	public static void setMissionContents(Map<String,Object> contents) {
		KitMissionContents.setChecked(false);
		KitMissionContents.mName = (String) contents.get("mName"); //?????????.
		KitMissionContents.mPopupTitle = (String) contents.get("mPopupTitle"); //?????? ?????????.
		KitMissionContents.kcoin = (String) contents.get("kcoin");//?????????-????????????.
		KitMissionContents.itemName = (String) contents.get("itemName");//?????????-????????????.
		KitMissionContents.itemCount = (String) contents.get("itemCount");//????????? ??????.
		KitMissionContents.itemImage = (String) contents.get("itemImage");//????????? ?????????.
		//TODO: ????????? ??????????????????, ',' ?????? ?????? ??????.
		
		
		
	}
	
	public static boolean isChecked() {
		return isChecked;
	}
	public static void setChecked(boolean isChecked) {
		KitMissionContents.isChecked = isChecked;
	}
}
