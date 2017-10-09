package com.yzh.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.yzh.base.PreferenceData;

import java.util.Locale;

public class LanguageUtils {


	public static void initAppLanguage(Context context) {
	    if (context == null) {
	        return;
	    }
	    if(PreferenceData.loadAppLanguage(context)==0)
	    {
	    	 Configuration config = context.getResources().getConfiguration();
	 	    config.locale = Locale.getDefault();
	 	    context.getResources().updateConfiguration(config
	 	            , context.getResources().getDisplayMetrics());
	    }
	    else if(PreferenceData.loadAppLanguage(context)==0)
	   {
	    Configuration config = context.getResources().getConfiguration();
	    config.locale = Locale.SIMPLIFIED_CHINESE;
	    context.getResources().updateConfiguration(config
	            , context.getResources().getDisplayMetrics());
	   }else  if(PreferenceData.loadAppLanguage(context)==2)
	   {
		   Configuration config = context.getResources().getConfiguration();
		    config.locale = Locale.ENGLISH;
		    context.getResources().updateConfiguration(config
		            , context.getResources().getDisplayMetrics());
	   }
	}





}
