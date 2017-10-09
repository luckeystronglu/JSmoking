package com.yzh.base;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.libraryutils.DialogUtils;
import com.libraryutils.LogUtils;
import com.libraryutils.ToastUtils;
import com.yzh.smartsmoking.R;
import com.yzh.smartsmoking.eventbus.EventBusMsg;
import com.yzh.utils.ScreenUtil;

import net.tsz.afinal.FinalDb;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;


/**
 * LuckeyBaseActivity
 *
 * @author Tom
 * @date 2014/11/24 11:40
 */
public abstract class BaseActivity extends AppCompatActivity implements
        FinalDb.DbUpdateListener {

    private FragmentManager fragmentManager;
    private Fragment showFragment;

    private static final String TAG = BaseActivity.class.getName();

    protected Context context = this;
    protected DialogUtils dlgWaiting;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    boolean isExit;
    private static final int MSG_Exit_APP = 9999;
    protected static final int MSG_cannt_get_data = 2000;

//    protected BaseApplication baseApplication;

    protected static final int MSG_CANNOT_GET_REQUEST_DATA = 2025;
    protected Handler mDlgWaitingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {

                    case MSG_CANNOT_GET_REQUEST_DATA:
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                            ToastUtils.showTextToast(context, getResources().getString(R.string.network_error));
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    protected String getLogTAG() {
        return TAG;
    }

    protected void logMessage(String log) {
        LogUtils.logMessage(getLogTAG(), log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        Log.d("onLayout","layoutX: "+ getResources().getDimension(R.dimen.x480));
//        Log.d("onLayout","layoutY: "+ getResources().getDimension(R.dimen.y800));
        setContentView(getActivityContentId());
        /**
         * 初始化FragmentManger对象
         */

        fragmentManager = getSupportFragmentManager();
//        ScreenUtils.hideTitle(this);
        dlgWaiting = DialogUtils.createDialog(context, DialogUtils.REFRESH);
        dlgWaiting.setCanceledOnTouchOutside(false);
        registerNetworkChange();
        EventBus.getDefault().register(this);
        changeAppLanguage();
        App.addActivity(this);

//        baseApplication = (BaseApplication) this.getApplication();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /**
         * 沉浸式状态栏
         */
        if(isOpenStatus()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            //获得状态栏的高度
            int height = ScreenUtil.getStateBarHeight(this);
            if(height != -1){
                //设置Padding
                View view = findViewById(R.id.actionbar);
                if(view != null){
                    view.setPadding(0, height, 0, 0);
                }
            }
        }

        hideBottomUIMenu(); //隐藏虚拟按键

        initView();
        initEvent();
        loadDatas();
    }

    /**
     * 初始化
     */
    protected void initView(){

    }

    /**
     * 绑定监听
     */
    protected void initEvent(){

    }

    /**
     * 加载数据
     */
    protected void loadDatas(){

    }

    public void changeAppLanguage() {
        Locale myLocale;
        if (PreferenceData.loadAppLanguage(context) == 0) {
            myLocale = Locale.getDefault();
        } else if (PreferenceData.loadAppLanguage(context) == 1) {
            myLocale = Locale.SIMPLIFIED_CHINESE;
        } else {
            myLocale = Locale.ENGLISH;
        }
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    /**
     * 查找页面布局ID所对应的控件对象，不用强制转换
     * @param resId
     * @param <T>
     * @return
     */
    protected  <T> T findViewByIds(int resId){
        return (T) findViewById(resId);
    }

    /**
     * 获得activity显示的布局ID
     * @return
     */
    protected abstract int getActivityContentId();

    //管理fragment的显示与隐藏的方法
    protected void fragmentManager(int resId, Fragment fragment, String tag){
        //开始事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //隐藏正在显示的fragment对象
        if (showFragment != null){
            fragmentTransaction.hide(showFragment);
        }
        Fragment loadFragment = fragmentManager.findFragmentByTag(tag);
        if (loadFragment != null){
            fragmentTransaction.show(loadFragment);
        }else {
            loadFragment = fragment;
            fragmentTransaction.add(resId,fragment,tag);
        }
        showFragment = loadFragment;
        fragmentTransaction.commit();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    /**
     * 是否打开沉浸式状态栏
     * @return
     */
    public boolean isOpenStatus(){
        return true;
    }

//    public void onEventMainThread(SocketAppPacket eventPackage) {
//
//        String msg = "onEventMainThread收到了消息：" + eventPackage.getCommandId() + eventPackage.getCommandData();
//        LogUtils.logMessage("harvic", msg);
//
//    }



    public void onEventMainThread(EventBusMsg eventPackage) {


//        if (eventPackage.getMsgType() == EventBusMsg.MSG_Device_disconnect) {
//            ToastUtils.showTextToast(context, getResources().getString(R.string.bluetooth_disconnected));
//        } else if (eventPackage.getMsgType() == EventBusMsg.MSG_Device_connected) {
//            ToastUtils.showTextToast(context, getResources().getString(R.string.bluetooth_connected));
//        }
    }



    @Override
    protected void onDestroy() {
        logMessage("--->onDestroy()...");
        super.onDestroy();
        App.activityList.remove(this);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(myNetReceiver);
    }

    @Override
    protected void onPause() {
        logMessage("--->onPause()...");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        logMessage("--->onRestart()...");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        logMessage("--->onResume()...");
        super.onResume();
    }

    @Override
    protected void onStart() {
        logMessage("--->onStart()...");
        super.onStart();
    }

    @Override
    protected void onStop() {
        logMessage("--->onStop()...");
        super.onStop();
    }

    /**
     * Layout鍔ㄧ敾
     *
     * @return
     */
    protected LayoutAnimationController getLayoutAnimationController() {
        int duration = 500;
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    protected void startScaleAnimation(View view) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        set.addAnimation(scaleAnim);
        set.setDuration(200);
        view.startAnimation(set);
    }

    protected void showToast(int msg_resId) {
        showToast(msg_resId, Toast.LENGTH_SHORT);
    }

    protected void showToast(int msg_resId, int duration) {
        Toast.makeText(this, msg_resId, duration).show();
    }

    protected void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        Toast.makeText(this, msg, duration).show();
    }




    /**
     * 鏄剧ず鏃ユ湡Dialog
     **/
    protected void showDatePickerDialog(Date date, OnDateSetListener listener) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dlg = new DatePickerDialog(context, listener, year,
                month, day);
        dlg.show();
    }

    /**
     * 鏄剧ず鏃堕棿Dialog
     **/
    protected void showTimePickerDialog(Date date, OnTimeSetListener listener) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(date);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dlg = new TimePickerDialog(context, listener,
                hourOfDay, minute, true);
        dlg.show();
    }

    // -----------------------------------call socket
    // start-------------------------------------//
    protected void callSocketWebService(final int type, byte[] cmdData,
                                        final short api_code) {



    }

    private BroadcastReceiver MsgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int msg = intent.getIntExtra(App.Contants.MSGKEY, 0);
            Message(String.valueOf(msg));
        }
    };


    public void Message(String count) {
        // TODO Auto-generated method stub

    }

//    protected void doSocket(int type, byte[] paramData, short api_code,
//                            boolean refreshFlag) {
//        if (refreshFlag) {
//            dlgWaiting.show();
//        }
//        byte[] cmdData = SocketCommand.generateData(paramData, api_code);
//        callSocketWebService(type, cmdData, api_code);
//
//    }

    protected void doESocket() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_CANNOT_GET_REQUEST_DATA, App.WAITTING_SECOND);
    }

//    protected void doSocket(int type, String jsonStr, short api_code,
//                            boolean refreshFlag) {
//        if (refreshFlag) {
//            dlgWaiting.show();
//        }
//        byte[] cmdData = SocketCommand.generateData(jsonStr, api_code, false);
//        callSocketWebService(type, cmdData, api_code);
//    }

//    protected void doSocket(final int type, String jsonStr, short api_code) {
//        doSocket(type, jsonStr, api_code, false);
//    }

    protected void doSocketReturn(int type, String jsonStr) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void doSocketConnectServerFailed(int type, String jsonStr) {
    }

    protected void doSocketReadFailed(int type, String jsonStr) {
    }

    // -----------------------------------call socket
    // end-------------------------------------//
    /*------------------------------------鐩戝惉缃戠粶鍙樺寲 Start---------------------- */
    private void registerNetworkChange() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    OnNetworkConnected();
                } else {
                    OnNetworkDisconnected();
                }
            }
        }
    };

    protected void OnNetworkConnected() {

    }

    protected void OnNetworkDisconnected() {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
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


    // -------------------------------------call web service
    // end-------------------------------------/
//	关闭在其他页面连接的BluetoothGATT
//    protected void closeExistBluetoothGATT() {
//        BaseApplication.getInstance().mService.disconnect();
//        BaseApplication.getInstance().mService.close();
//    }




    /**
     * ListView初始化列表刷新时的提示文本(Activity)
     */
    protected void initActivityListViewTipText(PullToRefreshListView pListView, boolean b) {
        // 设置上拉刷新文本
        ILoadingLayout startLabels = pListView.getLoadingLayoutProxy(true,
                false);
        startLabels.setPullLabel(getResources().getString(R.string.pull_down_refresh));
        startLabels.setReleaseLabel(getResources().getString(R.string.release_refresh));
        startLabels.setRefreshingLabel(getResources().getString(R.string.refreshing));

        // 设置下拉刷新文本
        ILoadingLayout endLabels = pListView.getLoadingLayoutProxy(false, true);
        if (b) {
            endLabels.setPullLabel(getResources().getString(R.string.pull_up_load_more));
            endLabels.setReleaseLabel(getResources().getString(R.string.release_load_more));
            endLabels.setRefreshingLabel(getResources().getString(R.string.last_page));
        } else {
            endLabels.setPullLabel(getResources().getString(R.string.pull_up_load_more));
            endLabels.setReleaseLabel(getResources().getString(R.string.release_load_more));
            endLabels.setRefreshingLabel(getResources().getString(R.string.loading_more));
        }

    }


//    protected boolean isPushMessageActivityTop(){
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
//        return name.equals(PushMessageActivity.class.getName());
//    }

}
