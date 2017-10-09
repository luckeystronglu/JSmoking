package com.yzh.smartsmoking.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.customview.selfheader.HeaderRightTextView;
import com.customview.view.CircleProgressBar;
import com.customview.view.SelfListview;
import com.libraryutils.FormatTransfer;
import com.libraryutils.ToastUtils;
import com.yzh.base.BaseActivity;
import com.yzh.base.PreferenceData;
import com.yzh.smartsmoking.R;
import com.yzh.smartsmoking.adapter.PlanMouthAdapter;
import com.yzh.smartsmoking.adapter.PowerSettingAdapter;
import com.yzh.smartsmoking.eventbus.EventBusMsg;

import org.winplus.serial.utils.SerialPort;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by appadmin on 2017/8/30.
 */

public class HomeSmokingActivity extends BaseActivity implements View.OnClickListener {
    private HeaderRightTextView header_home_activity;
    private CircleProgressBar cpbDemo;
    //    private int myProgress = 88;
    //    private WaveLoadingView waveLoadingView; //波浪图
//    private LinearLayout ll_current_power_setting;
//    private TextView tv_current_power_setting;
    private TextView tv_plan_mouth_setting, tv_current_powervalue;
    private ImageView iv_plan_mouth_setting, iv_current_powervalue;

    private AlertDialog powerSetDialog, planMouthDialog;
    private TextView tvStepCancel, tvStepSure;
    private TextView tvPlanMouthCancel, tvPlanMouthSure;


    private ListView lv_power_setting; //功率设置自定义ListView
    private PowerSettingAdapter powerSettingAdapter;
    private List<String> powerValueList = new ArrayList<>();
    private String[] strs = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    private int powerClickPosition = -1;

    OutputStream mOutputStream;
    InputStream mInputStream;
    SerialPort sp;
    //    SendThread mSendThread;
    ReadThread mReadThread;


    private byte[] wakeByte = {(byte) 0xA5};


    private byte[] setPowerValue = new byte[4];
    private boolean needRead = false;
    private byte[] getPowerValue = {(byte) 0xf2, 0x04, 0x00, 0x01, 0x01, (byte) 0xf6};
    public byte[] startSmoke = {(byte) 0xf2, 0x01, 0x00, 0x01, 0x01, (byte) 0xf3};
    public byte[] stopSmoke = {(byte) 0xf2, 0x01, 0x00, 0x01, 0x00, (byte) 0xf2};

    public byte[] lowVoltage = {(byte) 0xf2, 0x03, 0x00, 0x01, 0x01, (byte) 0xf1};
    public byte[] overCurrent = {(byte) 0xf2, 0x03, 0x00, 0x01, 0x02, (byte) 0xf2};


    private SelfListview lv_plan_mouth; //计划吸烟口数 设置自定义ListView
    private PlanMouthAdapter planMouthAdapter;
    private List<String> planMouthList = new ArrayList<>();
    private String[] mouthstrs = {"50", "100", "200"};
    private int planMouthClickPosition = -1;
    private boolean isCharging;
    private String Tag = HomeSmokingActivity.class.getName();

    PowerConnectionReceiver powerConnectionReceiver = null;

    @Override
    protected int getActivityContentId() {
        return R.layout.activity_main_home;
    }

    @Override
    protected void initView() {
        header_home_activity = findViewByIds(R.id.header_home_activity);
//        waveLoadingView = findViewByIds(R.id.wave_loading_view);
        tv_plan_mouth_setting = findViewByIds(R.id.tv_plan_mouth_setting);
        tv_current_powervalue = findViewByIds(R.id.tv_current_powervalue);
        iv_plan_mouth_setting = findViewByIds(R.id.iv_plan_mouth_setting);
        iv_plan_mouth_setting.setOnClickListener(this);
        iv_current_powervalue = findViewByIds(R.id.iv_current_powervalue);
        iv_current_powervalue.setOnClickListener(this);

        cpbDemo = (CircleProgressBar) findViewById(R.id.circleprogressbar_cpb_demo);

        try {
            sp = getSerialPort();
            mOutputStream = sp.getOutputStream();
            mInputStream = sp.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mReadThread = new ReadThread();
        mReadThread.start();


        powerSetDialog = new AlertDialog.Builder(context).setView(R.layout.dialog_power_setting).create();
        planMouthDialog = new AlertDialog.Builder(context).setView(R.layout.dialog_plan_mouth).create();

        if (powerValueList.size() > 0) {
            powerValueList.clear();
        }
        if (planMouthList.size() > 0) {
            planMouthList.clear();
        }
        //功率设置数据源
        for (int i = 0; i < strs.length; i++) {
            powerValueList.add(strs[i]);
        }

        //吸烟口数数据源
        for (int i = 0; i < mouthstrs.length; i++) {
            planMouthList.add(mouthstrs[i]);
        }


//        ll_current_power_setting = findViewByIds(ll_current_power_setting);
//        ll_current_power_setting.setOnClickListener(this);
//        tv_current_power_setting = findViewByIds(R.id.tv_current_power_setting);
//        tv_current_power_setting.setText("当前功率" + "    " + PreferenceData.loadPowerValue(context) + "W");
        tv_current_powervalue.setText(PreferenceData.loadPowerValue(context) + "");


    }

    @Override
    protected void onResume() {
        super.onResume();
        setCpbValue(); //设置自定义Progress的值
        powerConnectionReceiver = new PowerConnectionReceiver();
        IntentFilter intent = new IntentFilter();
//        intent.addAction(Intent.ACTION_POWER_CONNECTED);
//        intent.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intent.addAction("android.hardware.usb.action.USB_STATE");
        registerReceiver(powerConnectionReceiver, intent);

//        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = context.registerReceiver(null, ifilter);
//        //你可以读到充电状态,如果在充电，可以读到是usb还是交流电
//
//        // 是否在充电
//        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//         isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
//                status == BatteryManager.BATTERY_STATUS_FULL;

    }


    private void setCpbValue() {
        tv_plan_mouth_setting.setText(PreferenceData.loadCpbCountNum(context) + "");

        cpbDemo.setTopText(getResources().getString(R.string.today_smoking_mouth));
        cpbDemo.setBottomText(String.valueOf(PreferenceData.loadProgressValue(context)));
//        cpbDemo.setMax(PreferenceData.loadCpbCountNum(context));
//        cpbDemo.setBottomText(getResources().getString(R.string.cpb_plan) + " " + PreferenceData.loadCpbCountNum(context)); //计划吸烟口数
//        cpbDemo.setProgress(PreferenceData.loadProgressValue(context));
//        cpbDemo.setProgressDuration(5000);
//        cpbDemo.animationToProgress(0, myProgress);
    }

    @Override
    protected void initEvent() {
        header_home_activity.setonclickListener(new HeaderRightTextView.ClickHeaderListener() {
            @Override
            public void onClickLeftIcon() {
                finish();
            }

            @Override
            public void onClickRightText(View view) {
//                startActivity(new Intent(HomeSmokingActivity.this, SettingActivity.class));
            }
        });
    }


    //show出 每天吸烟口数的Dialog
    private void showPlanMouthDialog() {
        planMouthDialog.show();
        //设置dialog的样式属性
        Window dialogView = planMouthDialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams lp = dialogView.getAttributes();
//                                dialogView.setBackgroundDrawableResource(android.R.color.transparent);
//                                dialogView.setBackgroundDrawable(ContextCompat.getDrawable(context,R.color.transparent));
        dialogView.setBackgroundDrawable(new BitmapDrawable());
        lp.width = (int) (width * 0.8);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //        lp.height = height / 3;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        //lp.x = 100;
//                            lp.y = -300;
        dialogView.setAttributes(lp);

        lv_plan_mouth = (SelfListview) planMouthDialog.findViewById(R.id.lv_plan_mouth_num);
        planMouthAdapter = new PlanMouthAdapter(context);
        lv_plan_mouth.setAdapter(planMouthAdapter);
        planMouthAdapter.setDatas(planMouthList);


        tvPlanMouthCancel = (TextView) planMouthDialog.findViewById(R.id.tv_dlg_plan_mouth_cancel);
        tvPlanMouthSure = (TextView) planMouthDialog.findViewById(R.id.tv_dlg_plan_mouth_sure);

        lv_plan_mouth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                planMouthClickPosition = position;
                planMouthAdapter.setSelectItem(position);
                planMouthAdapter.notifyDataSetChanged();
            }
        });

        tvPlanMouthCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planMouthDialog.dismiss();
            }
        });
        tvPlanMouthSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(关闭)确定
                if (planMouthClickPosition != -1) {
                    tv_plan_mouth_setting.setText(planMouthList.get(planMouthClickPosition) + "");

//                    if (Integer.parseInt(planMouthList.get(planMouthClickPosition)) != PreferenceData.loadCpbCountNum(context)) {
//                        int va = Integer.parseInt(planMouthList.get(planMouthClickPosition));
//                        int va2 = PreferenceData.loadCpbCountNum(context);
                    PreferenceData.saveCpbCountNum(context, Integer.parseInt(planMouthList.get(planMouthClickPosition)));

//                        setCpbValue();
//                        EventBusMsg eventBusMsg = new EventBusMsg();
//                        eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_CQB_VALUE);
//                        EventBus.getDefault().post(eventBusMsg);


//                    }

                    planMouthDialog.dismiss();
                } else {
                    ToastUtils.showTextToast(context, getResources().getString(R.string.toast_plan_mouth));
                }

            }
        });
        planMouthDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (planMouthClickPosition != -1) {
                    planMouthClickPosition = -1;
                }
            }
        });
    }

    //show出 功率设置的Dialog
    private void showPowerSettingDialog() {
        powerSetDialog.show();
        //设置dialog的样式属性
        Window dialogView = powerSetDialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        WindowManager.LayoutParams lp = dialogView.getAttributes();
//                                dialogView.setBackgroundDrawableResource(android.R.color.transparent);
//                                dialogView.setBackgroundDrawable(ContextCompat.getDrawable(context,R.color.transparent));
        dialogView.setBackgroundDrawable(new BitmapDrawable());
        lp.width = (int) (width * 0.8);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        //        lp.height = height / 3;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        //lp.x = 100;
//                            lp.y = -300;
        dialogView.setAttributes(lp);

        lv_power_setting = (ListView) powerSetDialog.findViewById(R.id.lv_power_setting);
        powerSettingAdapter = new PowerSettingAdapter(context);
        lv_power_setting.setAdapter(powerSettingAdapter);
        powerSettingAdapter.setDatas(powerValueList);


        tvStepCancel = (TextView) powerSetDialog.findViewById(R.id.tv_dlg_step_cancel);
        tvStepSure = (TextView) powerSetDialog.findViewById(R.id.tv_dlg_step_sure);

        lv_power_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                powerClickPosition = position;
                powerSettingAdapter.setSelectItem(position);
                powerSettingAdapter.notifyDataSetChanged();
            }
        });

        tvStepCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerSetDialog.dismiss();
            }
        });
        tvStepSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(关闭)确定
                if (powerClickPosition != -1) {
                    int powerValue = Integer.parseInt(powerValueList.get(powerClickPosition));
                    if (powerValue != PreferenceData.loadPowerValue(context)) {

                        String s = Integer.toHexString(powerValue);
                        if (s.length() == 1) {
                            s = "0" + s;
                        }
                        byte[] bytes1 = FormatTransfer.hexStringToBytes(s);
//                        setPowerValue[0] = (byte) 0xf2;
                        setPowerValue[0] = 0x02;
                        setPowerValue[1] = 0x00;
                        setPowerValue[2] = 0x01;
                        setPowerValue[3] = bytes1[0];


                        byte[] btPowerValue = new byte[6];
                        System.arraycopy(setPowerValue, 0, btPowerValue, 1, setPowerValue.length);

                        byte[] encryptValue = getEncryptValue(setPowerValue);
                        btPowerValue[0] = (byte) 0xf2;
                        btPowerValue[5] = encryptValue[encryptValue.length - 1]; //将异或数放在数组的第6位

//                        needRead = true;
                        writeSmokeData(btPowerValue);

//                        mSendThread = new SendThread();
//                        mSendThread.start();
                    }

                    powerSetDialog.dismiss();
                } else {
                    ToastUtils.showTextToast(context, getResources().getString(R.string.toast_power_choice));
                }
            }
        });
        powerSetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (powerClickPosition != -1) {
                    powerClickPosition = -1;
                }
            }
        });
    }


    private void writeSmokeData(final byte[] smokeData) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    needRead = true;
                    mOutputStream.write(wakeByte);

                    Thread.sleep(50);
                    needRead = true;
                    mOutputStream.write(smokeData);

                    Thread.sleep(50);
                    sendSmokeData(getPowerValue); //获取功率值

                } catch (Exception e) {
                    e.printStackTrace();

                    if (mOutputStream == null) {
                        ToastUtils.showTextToast(context, "设置失败");
                    }
                }
            }
        }).start();

    }

    private void sendSmokeData(byte[] getPowerData) {
        try {
            needRead = true;
            mOutputStream.write(getPowerData);//获取功率值
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startSmokeData(byte[] startSmokeD) {

        try {
            needRead = true;
            mOutputStream.write(startSmokeD);//获取功率值
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public String m_strBaud = "";
//    public String m_strPort = "";

    public SerialPort getSerialPort() {
        if (sp == null) {

//            String str = "/dev/ttyMT1";
//            String str = "/dev/ttyS1";
            String str = "/dev/ttyS0";
            int i = 115200;
//            int i = 57600;
//            m_strPort = str;
//            m_strBaud = i + "";
            if ((str.length() == 0) || (i == -1))
                throw new InvalidParameterException();

            try {
                sp = new SerialPort(new File(str), i, 0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sp;
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
//            while (!isInterrupted()) {
//                ReadSerialData();
//            }
            while (!isInterrupted()) {
                if (needRead) {
                    ReadSerialData();
                }
            }
        }
    }


    private int j = 0;

    private void ReadSerialData() {
        int size;
        try {
            byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0};
            if (mInputStream == null)
                return;

			/*
             * if (i == 5) { i = 0; } while (i < 5) { buffer[i] = (byte)
			 * mInputStream.read(); i++; }
			 */
            //Log.d("zhantaiming", "11111==");
            while (j < 1) {
                buffer[j] = (byte) mInputStream.read();
                //Log.d("zhantaiming", "11111=="+dumpBytes(buffer[j],1));

                if (((buffer[j] & 0xff)) != 0XF2) {
                    return;
                }
                j++;
            }
            while (j < 2) {
                buffer[j] = (byte) mInputStream.read();
                //Log.d("zhantaiming", "11111=="+dumpBytes(buffer[j],1));

                if (((int) (buffer[j] & 0xff)) != 0X05) {
                    return;
                }
                j++;
            }
            while (j < 6) {
                buffer[j] = (byte) mInputStream.read();
                //Log.d("zhantaiming", "11111==");
                j++;
            }

            if (j > 0)
                onDataReceived(buffer, 6);
            else {
                j = 0;
                return;
            }

            j = 0;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //    private int redLightFlashCount = 0;
    private int caseOverCurrent = 0;
    private boolean isOverCurrent = false;
    private boolean isLowVoltage = false;

    private void onDataReceived(final byte[] buffer, final int size) {

        runOnUiThread(new Runnable() {

            public void run() {

                if (buffer == lowVoltage) {
                    isLowVoltage = true;
                    startSmokeData(stopSmoke); //停止吸烟
                    ToastUtils.showTextToast(context, getResources().getString(R.string.low_voltage_remind));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!isCharging) {
                                try {
                                    openRedLight(); //红灯亮
                                    Thread.sleep(250);
                                    closeRedLight(); //红灯灭
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else if (buffer == overCurrent) {
                    if (caseOverCurrent != 0) {
                        caseOverCurrent = 0;
                    }
                    ToastUtils.showTextToast(context, getResources().getString(R.string.atomizer_short_circuit));
                    startSmokeData(stopSmoke); //停止吸烟

//                    isOverCurrent = true;
//                    while (isOverCurrent){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (caseOverCurrent <= 5) {
                                try {
                                    caseOverCurrent++;
                                    openRedLight(); //红灯亮
                                    Thread.sleep(250);
                                    closeRedLight(); //红灯灭
                                    Thread.sleep(250);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

//                        if (caseOverCurrent == 5) {
//                            isOverCurrent = false;
//                        }
//                    }

                } else {


                    byte[] currentpower = new byte[1];
                    currentpower[0] = buffer[4];
                    int length = currentpower.length;
                    String powerValue = dumpBytes(currentpower, length);

                    tv_current_powervalue.setText(powerValue);

                    PreferenceData.savePowerValue(context, Integer.parseInt(powerValue));


                    needRead = false;
                }
            }
        });
    }

    public String dumpBytes(byte[] bytes, int size) {
        int i;
        StringBuffer sb = new StringBuffer();
        String s;
        for (i = 0; i < size; i++) {
            /*
             * if (i % 32 == 0 && i != 0) { sb.append(" \n "); }
			 */
            int data = (int) (bytes[i] & 0xff);
            //s = Integer.toHexString(data);
            s = Integer.toString(data);
            //if (s.length() < 2) {
            //	s = "0" + s;
            //}
            /*
			 * if (s.length() > 2) { s = s.substring(s.length() - 2); }
			 */
            sb.append(s);
        }
        return sb.toString();
    }


    //循环异或加密
    public byte[] getEncryptValue(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        int key = 0xF2;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }


    public void closeSerialPort() {
        if (this.sp != null) {
            this.sp.close();
            this.sp = null;
        }
    }


    //当GPIO口为输出的时候，通过以下的办法来控制高低电平
    private boolean openRedLight_backup() {   //红灯亮
        return RootCommand("echo 0 > /sys/bus/platform/drivers/usb20_otg/set_redLed_state");
    }

    public boolean closeRedLight_backup() {    //红灯灭
        return RootCommand("echo 1 > /sys/bus/platform/drivers/usb20_otg/set_redLed_state");
    }

    private boolean openGreenLight_back() {   //蓝灯亮
        return RootCommand("echo 0 > /sys/bus/platform/drivers/usb20_otg/set_greenLed_state");
    }

    public boolean closeGreenLight_backup() {    //蓝灯灭
        return RootCommand("echo 1 > /sys/bus/platform/drivers/usb20_otg/set_greenLed_state");
    }


    //下面的是执行的方法
    private boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }


    @Override
    public void onEventMainThread(EventBusMsg eventPackage) {
        switch (eventPackage.getMsgType()) {
            case EventBusMsg.MSG_REFRESH_HOME_DATE:
                PreferenceData.saveProgressValue(context, 0); //每天零点后,清零Progress
                cpbDemo.setBottomText(String.valueOf(PreferenceData.loadProgressValue(context)));
//                cpbDemo.setProgress(0);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //计划吸烟口数 设置 点击事件
            case R.id.iv_plan_mouth_setting:
                showPlanMouthDialog();
                break;

            case R.id.iv_current_powervalue:
                showPowerSettingDialog();
                break;
            //当前功率 设置 点击事件
        }
    }


//    private long pressTime; //退出键按下时间
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - pressTime < 2000)) {
//                this.finish();
//            }else {
//                Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
//                pressTime = System.currentTimeMillis();
//            }
//        }
//        return true;
//    }


    private long startSmokingTime; //开始吸烟时间
    private boolean isKeyUp = true;
    private boolean isDown = false;
    private boolean isSendStop = false;
    private boolean isGreenLightOn = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //音量减174,音量加175
        if (keyCode == 142) {
            Log.e(Tag, "key down");
            if (isCharging) {
                return false;
            }
            if (isLowVoltage) {
                return false;
            }
            if (!isGreenLightOn) {

                isGreenLightOn = true;
                //按下抽烟键，就亮绿灯
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        openGreenLight();
                        Log.e(Tag, "green light on");
                    }
                }).start();
            }
            //keyCode == 0
            if (isKeyUp && !isDown) {
                startSmokingTime = System.currentTimeMillis();
                Log.e(Tag, "onKeyDown currentTimeMillis: " + System.currentTimeMillis());
//                if (!isCharging) { //非充电状态下,才可以吸烟
                startSmokeData(startSmoke);
//                }

            }
            isKeyUp = false;
            isDown = true;

            if (System.currentTimeMillis() - startSmokingTime > 8000 && !isKeyUp && isDown) {
                if (!isSendStop) {
                    Log.e(Tag, "send stop smoke");
                    startSmokeData(stopSmoke); //停止吸烟命令
                    isSendStop = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!isKeyUp) {
                                    Log.e(Tag, "open green light");
                                    openGreenLight();
                                    Thread.sleep(500);
                                    Log.e(Tag, "close green light");
                                    closeGreenLight();
                                    Thread.sleep(500);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 142) {
            Log.e(Tag, "key up");
            if (isLowVoltage) {
                return false;
            }

            if (isCharging) {
                return false;
            }

            if (isGreenLightOn) {

                isGreenLightOn = false;
                //按下抽烟键，就亮绿灯
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        closeGreenLight();
                        Log.e(Tag, "green light off");
                    }
                }).start();
            }
            isDown = false;
            isKeyUp = true;
            isSendStop = false;
            Log.e("onKeyUp", "onKeyUp currentTimeMillis: " + System.currentTimeMillis());
            long between = System.currentTimeMillis() - startSmokingTime;
            startSmokeData(stopSmoke);
            Log.e("onKeyUp", "onKeyUp milliseconds: " + between);
            int pValue = PreferenceData.loadProgressValue(context);
//            if ((between <= 8000) && between > 500) {
//                int seconds = (int) between / 2000;
//                long remainder = between % 2000;
//                if (remainder != 0) {
//                    PreferenceData.saveProgressValue(context, pValue + seconds + 1);
//                } else {
//                    PreferenceData.saveProgressValue(context, pValue + seconds);
//                }
//                PreferenceData.saveProgressValue(context, pValue  + 1);
//            }
//            else
            if (between > 500) {
                PreferenceData.saveProgressValue(context, pValue + 1);
            }
//            else if (between < 2000) {
//                PreferenceData.saveProgressValue(context, pValue + 1);
//            }

//            cpbDemo.setProgress(PreferenceData.loadProgressValue(context));
            cpbDemo.setBottomText(String.valueOf(PreferenceData.loadProgressValue(context)));
            return true;

        }
        return false;
//        return super.onKeyUp(keyCode, event);
    }


    public class PowerConnectionReceiver extends BroadcastReceiver {

        @Override

        public void onReceive(Context context, Intent intent) {
            boolean connected = intent.getExtras().getBoolean("connected");
//            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
//            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ;
            isCharging = connected;
//            isCharging=false;
            if (isCharging) {
                isLowVoltage = false;
//                Toast.makeText(HomeSmokingActivity.this,"充电中",Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(HomeSmokingActivity.this,"充电结束",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(powerConnectionReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (powerValueList != null) {
            powerValueList.clear();
        }

        if (planMouthList != null) {
            planMouthList.clear();
        }


        if (mReadThread != null) {
            mReadThread.interrupt();
            closeSerialPort();
        }

//        if (mSendThread != null) {
//            mSendThread.interrupt();
//        }

    }


    private void openGreenLight() {
        File file = new File("/sys/bus/platform/drivers/usb20_otg/set_greenLed_state");
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write('0');

                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeGreenLight() {
        File file = new File("/sys/bus/platform/drivers/usb20_otg/set_greenLed_state");
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write('1');

                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeRedLight() {
        File file = new File("/sys/bus/platform/drivers/usb20_otg/set_redLed_state");
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write('1');

                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openRedLight() {
        File file = new File("/sys/bus/platform/drivers/usb20_otg/set_redLed_state");
        if (file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write('0');

                fos.flush();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
