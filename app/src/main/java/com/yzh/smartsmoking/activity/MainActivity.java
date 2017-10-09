package com.yzh.smartsmoking.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customview.selfheader.HeaderRightImageView;
import com.customview.view.SelfDialogView;
import com.libraryutils.ToastUtils;
import com.yzh.base.BaseActivity;
import com.yzh.base.PreferenceData;
import com.yzh.smartsmoking.R;
import com.yzh.smartsmoking.eventbus.EventBusMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;



public class MainActivity extends BaseActivity implements View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    private HeaderRightImageView header_device_alarm_list;
    private TextView tv_home_time,tv_home_date,tv_home_week;

    private LinearLayout ll_main_smoking,ll_main_kugou,ll_main_setting;

    private SelfDialogView selfDialogView;
    private List<String> packageNameList = new ArrayList<>();

//    private ValueAnimator mClockAnimator;
//    private Calendar mCalendar;
//    private long mLastRecodeTimeMillis;
    private static final int TIME_REQUEST_CODE = 0x5555;


    @Override
    protected int getActivityContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        long time = System.currentTimeMillis();
        Date date = new Date(time);
//      SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日&HH:mm&E");
        String currentDate = sdf.format(date);
        String[] splitTime = currentDate.split("&");
        String strDate = splitTime[0];
        if (strDate.equals(PreferenceData.loadStrCurDate(context))) {

        }
        tv_home_time.setText(splitTime[1]);
        tv_home_date.setText(strDate);
        tv_home_week.setText(splitTime[2]);

        getPackageNameList(); //获取当前所有的包名

    }

    @Override
    protected void initView() {
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
        header_device_alarm_list = findViewByIds(R.id.header_main_activity);
        tv_home_time = findViewByIds(R.id.tv_home_time);
        tv_home_time.setOnClickListener(this);
        tv_home_date = findViewByIds(R.id.tv_home_date);
        tv_home_week = findViewByIds(R.id.tv_home_week);

        ll_main_smoking = findViewByIds(R.id.ll_main_smoking);
        ll_main_smoking.setOnClickListener(this);
        ll_main_kugou = findViewByIds(R.id.ll_main_kugou);
        ll_main_kugou.setOnClickListener(this);
        ll_main_setting = findViewByIds(R.id.ll_main_setting);
        ll_main_setting.setOnClickListener(this);

        selfDialogView = new SelfDialogView(context, R.style.my_self_dialog);

    }

    private void getPackageNameList() {
// 获取PackageManager对象
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(
                packageManager));

        if (packageNameList.size() > 0) {
            packageNameList.clear();
        }

        for (ResolveInfo reInfo : resolveInfos) {
//            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
            packageNameList.add(pkgName);
//            System.out.println("activityName---" + activityName + " pkgName---"
//                    + pkgName);
        }


    }

    @Override
    protected void initEvent() {
        header_device_alarm_list.setOnHeaderClickListener(new HeaderRightImageView.clickHeaderListener() {
            @Override
            public void onClickLeftIcon() {

            }

            @Override
            public void onClickRightIcon(View view) {
//                ToastUtils.showTextToast(context,"分享");

                selfDialogView.show();
                selfDialogView.setDialogTitleName("国庆放假通知");
                selfDialogView.setDialogDescContent("中秋国庆一起放!" + "\n" + "请注意假期安全");
                selfDialogView.setDialogDescColor(getResources().getColor(R.color.blue_status));
                selfDialogView.setDialogCancelText("点击取消");
                selfDialogView.setDialogSureText("确定了");
//                selfDialogView.setDialogTitleGrivity(Gravity.CENTER);
//                selfDialogView.setDialogTitleBackGround(getResources().getColor(R.color.yellow));
//                selfDialogView.setDividerLineColor(getResources().getColor(R.color.red));
//                selfDialogView.setDividerLineSize(2);

                selfDialogView.setOnSelfDialogCancelListener(new SelfDialogView.OnSelfDialogCancelListener() {
                    @Override
                    public void DialogCancelClickListener() {
                        selfDialogView.dismiss();
                        ToastUtils.showTextToast(context,"取消");
                    }
                });

                selfDialogView.setOnSelfDialogSureListener(new SelfDialogView.OnSelfDialogSureListener() {
                    @Override
                    public void DialogSureClickListener() {
                        selfDialogView.dismiss();
                        ToastUtils.showTextToast(context,"确定");
                    }
                });
            }
        });
    }

    @Override
    protected void loadDatas() {
        new TimeThread().start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }

//    private long pressTime; //退出键按下时间
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - pressTime < 2000)) {
//                this.finish();
//            }else {
//                ToastUtils.showTextToast(context,getResources().getString(R.string.press_once_more_login_out));
//                pressTime = System.currentTimeMillis();
//            }
//        }
//        return true;
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_main_setting: //设置
//                Intent mIn = new Intent();
//                ComponentName compN = new ComponentName("com.android.settings","com.android.settings.Settings");
//                mIn.setComponent(compN);
////                mIn.setAction("android.intent.action.VIEW");
//                mIn.setAction("android.intent.action.VIEW");
//                startActivity(mIn);
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setAction("android.settings.SETTINGS");
                startActivity(intent);

                break;


            case R.id.ll_main_kugou: //酷狗音乐
                Intent mIntent = new Intent(Intent.ACTION_MAIN);
                mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                ComponentName comp = new ComponentName("com.yzh.sikalesmoking","com.yzh.sikalesmoking.activity.other.MainNewActivity");
                ComponentName comp = new ComponentName("com.kugou.android","com.kugou.android.app.splash.SplashActivity");
//                ComponentName comp = new ComponentName("com.kugou.android","com.kugou.android.app.MediaActivity");
                mIntent.setComponent(comp);
//                mIntent.setAction("android.intent.action.VIEW");
                if (packageNameList.contains("com.kugou.android")) {
                    startActivity(mIntent);
                }else {
                    ToastUtils.showTextToast(context,getResources().getString(R.string.please_install_kugou_first));
                    // http://download.kugou.com/android.html
                }

                break;

            case R.id.ll_main_smoking: //抽烟
                startActivity(new Intent(MainActivity.this,HomeSmokingActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class TimeThread extends  Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = TIME_REQUEST_CODE;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            do{
//                try {
//                    Thread.sleep(1000);
//                    Message msg = new Message();
//                    msg.what = TIME_REQUEST_CODE;
//                    mHandler.sendMessage(msg);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }while (true);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_REQUEST_CODE:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日&HH:mm&E");
                    String currentDate = sdf.format(date);
                    String[] splitTime = currentDate.split("&");
                    String strCurDate = splitTime[0];
                    PreferenceData.saveStrCurDate(context,strCurDate);
                    tv_home_time.setText(splitTime[1]);
                    if (tv_home_date.getText().toString().equals(strCurDate)) {  //日期 为同一天

                    }else {
                        tv_home_date.setText(strCurDate);
                        EventBusMsg eventBusMsg = new EventBusMsg();
                        eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_HOME_DATE);
                        EventBus.getDefault().post(eventBusMsg);
                    }

                    tv_home_week.setText(splitTime[2]);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (packageNameList != null) {
            packageNameList.clear();
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}
