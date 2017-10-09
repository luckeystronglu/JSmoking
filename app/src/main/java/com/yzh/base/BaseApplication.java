package com.yzh.base;

import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.libraryutils.LogUtils;
import com.yzh.utils.LanguageUtils;

import net.tsz.afinal.FinalDb.DbUpdateListener;


/**
 * BaseApplication
 * @author Tom
 * @date 2014/11/24 11:40
 *
 */
public class BaseApplication extends MultiDexApplication implements DbUpdateListener {

	private  final String TAG = BaseApplication.class.getName();
	private static BaseApplication instance;
//	public BluetoothLeService mService;
	public  ServiceConnection mServiceConnection;
	public  String UUID;
	public  int CONNECTED = -1;
	public boolean isChangeLanguage=false;
	public boolean isNotFirstHaveStartSize = true;
	public int alarmFragmentIndex = -1;
	public String etClassName = "";
	@Override
	public void onCreate() {
		LogUtils.logMessage(TAG, "onCreate()...");
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//		SDKInitializer.initialize(this);
		//集成SMSSDK
		instance = this;
		//Crash日志
//		  CrashHandler crashHandler = CrashHandler.getInstance();
//	      crashHandler.init(getApplicationContext());
	      
//	      mServiceConnection = new ServiceConnection() {
//				public void onServiceConnected(ComponentName className, IBinder rawBinder) {
//					System.out.println("bluetooth service start");
//					try {
//						mService = ((BluetoothLeService.LocalBinder) rawBinder).getService();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//
//				public void onServiceDisconnected(ComponentName classname) {
//					System.out.println("bluetooth service come end");
//					mService = null;
//				}
//			};
//
//
//			Intent bindIntent = new Intent(this, BluetoothLeService.class);
//			startService(bindIntent);
//			bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);


			//不需要获取服务实例，不需要bind方式
//			Intent serviceIntent = new Intent(this, BleDataService.class);
//			startService(serviceIntent);


	}
	public static BaseApplication getInstance() {
		return instance;
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	//改变系统语言
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    Log.e("MyApplication","onConfigurationChanged");
	    super.onConfigurationChanged(newConfig);
	    LanguageUtils.initAppLanguage(getBaseContext());
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		try {
			Cursor cursor = db
					.rawQuery(
							"select count(1) as recordcount from sqlite_master where type ='table' and name ='SysUser'",
							null);
			if (cursor.moveToFirst()) {
				int recordcount = cursor.getInt(cursor
						.getColumnIndex("recordcount"));
				if (recordcount > 0) {
					db.execSQL("drop table SysUser");
				}
			}
			 cursor = db
					.rawQuery(
							"select count(1) as recordcount from sqlite_master where type ='table' and name ='SmokingData'",
							null);
			if (cursor.moveToFirst()) {
				int recordcount = cursor.getInt(cursor
						.getColumnIndex("recordcount"));
				if (recordcount > 0) {
					db.execSQL("drop table SmokingData");
				}
			}
			 cursor = db
						.rawQuery(
								"select count(1) as recordcount from sqlite_master where type ='table' and name ='SmokingLocationData'",
								null);
				if (cursor.moveToFirst()) {
					int recordcount = cursor.getInt(cursor
							.getColumnIndex("recordcount"));
					if (recordcount > 0) {
						db.execSQL("drop table SmokingLocationData");
					}
				}
			
		} catch (Exception e) {
			
		}
		
	}
}
