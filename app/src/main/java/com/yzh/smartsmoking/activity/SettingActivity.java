package com.yzh.smartsmoking.activity;

/**
 * Created by appadmin on 2017/8/30.
 * 设置页面
 */

//public class SettingActivity extends BaseActivity implements View.OnClickListener {
//    private HeaderRightImageView header_activity_setting;
//    private LinearLayout ll_setting_power, ll_daily_mouth;
//    private TextView tv_setting_power, tv_daily_mouth;
//
//    private AlertDialog powerSetDialog, planMouthDialog;
//    private TextView tvStepCancel, tvStepSure;
//    private TextView tvPlanMouthCancel, tvPlanMouthSure;
//
//    private SelectView selectview_current_power;
//
//    private ListView lv_power_setting; //功率设置自定义ListView
//    private PowerSettingAdapter powerSettingAdapter;
//    private List<String> powerValueList = new ArrayList<>();
//    private String[] strs = {"5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
//    private int powerClickPosition = -1;
//
//    private byte[] setPowerValue = new byte[4];
//    private boolean needRead = false;
//
//    private byte[] getPowerValue = {(byte) 0xf2,0x04,0x00,0x01,0x01, (byte) 0xf6};
//
//    public byte[] startSmoke = {(byte) 0xf2,0x01,0x00,0x01,0x01, (byte) 0xf3};
//    public byte[] stopSmoke = {(byte) 0xf2,0x01,0x00,0x01,0x00, (byte) 0xf2};
//
//
//    private SelfListview lv_plan_mouth; //计划吸烟口数 设置自定义ListView
//    private PlanMouthAdapter planMouthAdapter;
//    private List<String> planMouthList = new ArrayList<>();
//    private String[] mouthstrs = {"50", "100", "200"};
//    private int planMouthClickPosition = -1;
//
//    OutputStream mOutputStream;
//    InputStream mInputStream;
//    SerialPort sp;
//    //    SendThread mSendThread;
//    ReadThread mReadThread;
//
//    @Override
//    protected int getActivityContentId() {
//        return R.layout.activity_setting;
//    }
//
//    @Override
//    protected void initView() {
//        header_activity_setting = findViewByIds(R.id.header_activity_setting);
//        ll_setting_power = findViewByIds(R.id.ll_setting_power);
//        ll_setting_power.setOnClickListener(this);
//        ll_daily_mouth = findViewByIds(R.id.ll_daily_mouth);
//        ll_daily_mouth.setOnClickListener(this);
//        tv_setting_power = findViewByIds(R.id.tv_setting_power);
//        tv_setting_power.setText(PreferenceData.loadPowerValue(context) + " " + getResources().getString(R.string.smoke_power_unit));
//        tv_daily_mouth = findViewByIds(R.id.tv_daily_mouth);
//        tv_daily_mouth.setText(PreferenceData.loadCpbCountNum(context) + " " + getResources().getString(R.string.mouth));
//        selectview_current_power = findViewByIds(R.id.selectview_current_power);
//        selectview_current_power.setOnClickListener(this);
//
////        sp = getSerialPort();
////        mOutputStream = sp.getOutputStream();
////        mInputStream = sp.getInputStream();
//
//        mReadThread = new ReadThread();
//        mReadThread.start();
//
//
//        powerSetDialog = new AlertDialog.Builder(context).setView(R.layout.dialog_power_setting).create();
//        planMouthDialog = new AlertDialog.Builder(context).setView(R.layout.dialog_plan_mouth).create();
//        if (powerValueList.size() > 0) {
//            powerValueList.clear();
//        }
//        if (planMouthList.size() > 0) {
//            planMouthList.clear();
//        }
//        //功率设置数据源
//        for (int i = 0; i < strs.length; i++) {
//            powerValueList.add(strs[i]);
//        }
//
//        //吸烟口数数据源
//        for (int i = 0; i < mouthstrs.length; i++) {
//            planMouthList.add(mouthstrs[i]);
//        }
//
//    }
//
//    @Override
//    protected void initEvent() {
//        header_activity_setting.setOnHeaderClickListener(new HeaderRightImageView.clickHeaderListener() {
//            @Override
//            public void onClickLeftIcon() {
//                finish();
//            }
//
//            @Override
//            public void onClickRightIcon(View view) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void loadDatas() {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_setting_power:
//                showPowerSettingDialog();
//                break;
//
//            case R.id.ll_daily_mouth:
////                onSmokingMouthPicker();
//                showPlanMouthDialog();
//                break;
//
//            case R.id.selectview_current_power:
////                needRead = true;
////                writeSmokeData(getPowerValue);
//                break;
//        }
//    }
//
//
//    //show出 每天吸烟口数的Dialog
//    private void showPlanMouthDialog() {
//        planMouthDialog.show();
//        //设置dialog的样式属性
//        Window dialogView = planMouthDialog.getWindow();
//        int width = getResources().getDisplayMetrics().widthPixels;
//        WindowManager.LayoutParams lp = dialogView.getAttributes();
////                                dialogView.setBackgroundDrawableResource(android.R.color.transparent);
////                                dialogView.setBackgroundDrawable(ContextCompat.getDrawable(context,R.color.transparent));
//        dialogView.setBackgroundDrawable(new BitmapDrawable());
//        lp.width = width - 160;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        //        lp.height = height / 3;
//        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
//        //lp.x = 100;
////                            lp.y = -300;
//        dialogView.setAttributes(lp);
//
//        lv_plan_mouth = (SelfListview) planMouthDialog.findViewById(R.id.lv_plan_mouth_num);
//        planMouthAdapter = new PlanMouthAdapter(context);
//        lv_plan_mouth.setAdapter(planMouthAdapter);
//        planMouthAdapter.setDatas(planMouthList);
//
//
//        tvPlanMouthCancel = (TextView) planMouthDialog.findViewById(R.id.tv_dlg_plan_mouth_cancel);
//        tvPlanMouthSure = (TextView) planMouthDialog.findViewById(R.id.tv_dlg_plan_mouth_sure);
//
//        lv_plan_mouth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                planMouthClickPosition = position;
//                planMouthAdapter.setSelectItem(position);
//                planMouthAdapter.notifyDataSetChanged();
//            }
//        });
//
//        tvPlanMouthCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                planMouthDialog.dismiss();
//            }
//        });
//        tvPlanMouthSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //(关闭)确定
//                if (planMouthClickPosition != -1) {
//                    tv_daily_mouth.setText(planMouthList.get(planMouthClickPosition) + " " + getResources().getString(R.string.mouth));
//                    if (Integer.parseInt(planMouthList.get(planMouthClickPosition)) != PreferenceData.loadCpbCountNum(context)) {
////                        int va = Integer.parseInt(planMouthList.get(planMouthClickPosition));
////                        int va2 = PreferenceData.loadCpbCountNum(context);
//                        PreferenceData.saveCpbCountNum(context, Integer.parseInt(planMouthList.get(planMouthClickPosition)));
//
//                        EventBusMsg eventBusMsg = new EventBusMsg();
//                        eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_CQB_VALUE);
//                        EventBus.getDefault().post(eventBusMsg);
//
//
//                    }
//
//                    planMouthDialog.dismiss();
//                } else {
//                    ToastUtils.showTextToast(context, getResources().getString(R.string.toast_plan_mouth));
//                }
//
//            }
//        });
//        planMouthDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if (planMouthClickPosition != -1) {
//                    planMouthClickPosition = -1;
//                }
//            }
//        });
//    }
//
//    //show出 功率设置的Dialog
//    private void showPowerSettingDialog() {
//        powerSetDialog.show();
//        //设置dialog的样式属性
//        Window dialogView = powerSetDialog.getWindow();
//        int width = getResources().getDisplayMetrics().widthPixels;
//        WindowManager.LayoutParams lp = dialogView.getAttributes();
////                                dialogView.setBackgroundDrawableResource(android.R.color.transparent);
////                                dialogView.setBackgroundDrawable(ContextCompat.getDrawable(context,R.color.transparent));
//        dialogView.setBackgroundDrawable(new BitmapDrawable());
//        lp.width = width - 160;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        //        lp.height = height / 3;
//        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
//        //lp.x = 100;
////                            lp.y = -300;
//        dialogView.setAttributes(lp);
//
//        lv_power_setting = (ListView) powerSetDialog.findViewById(R.id.lv_power_setting);
//        powerSettingAdapter = new PowerSettingAdapter(context);
//        lv_power_setting.setAdapter(powerSettingAdapter);
//        powerSettingAdapter.setDatas(powerValueList);
//
//
//        tvStepCancel = (TextView) powerSetDialog.findViewById(R.id.tv_dlg_step_cancel);
//        tvStepSure = (TextView) powerSetDialog.findViewById(R.id.tv_dlg_step_sure);
//
//        lv_power_setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                powerClickPosition = position;
//                powerSettingAdapter.setSelectItem(position);
//                powerSettingAdapter.notifyDataSetChanged();
//            }
//        });
//
//        tvStepCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                powerSetDialog.dismiss();
//            }
//        });
//        tvStepSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //(关闭)确定
//                if (powerClickPosition != -1) {
////                    tv_setting_power.setText(powerValueList.get(powerClickPosition) + " " + getResources().getString(R.string.smoke_power_unit));
//                    int powerValue = Integer.parseInt(powerValueList.get(powerClickPosition));
//                    if (powerValue != PreferenceData.loadPowerValue(context)) {
////                        PreferenceData.savePowerValue(context, Integer.parseInt(powerValueList.get(powerClickPosition)));
//
////                        EventBusMsg eventBusMsg = new EventBusMsg();
////                        eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_POWER_VALUE);
////                        EventBus.getDefault().post(eventBusMsg);
//
//                        String s = Integer.toHexString(powerValue);
//                        if (s.length() == 1) {
//                            s = "0" + s;
//                        }
//                        byte[] bytes1 = FormatTransfer.hexStringToBytes(s);
////                        setPowerValue[0] = (byte) 0xf2;
//                        setPowerValue[0] = 0x02;
//                        setPowerValue[1] = 0x00;
//                        setPowerValue[2] = 0x01;
//                        setPowerValue[3] = bytes1[0];
//
//
//                        byte[] btPowerValue = new byte[6];
//                        System.arraycopy(setPowerValue, 0, btPowerValue, 1, setPowerValue.length);
//
//                        byte[] encryptValue = getEncryptValue(setPowerValue);
//                        btPowerValue[0] = (byte) 0xf2;
//                        btPowerValue[5] = encryptValue[encryptValue.length - 1]; //将异或数放在数组的第6位
//
////                        needRead = true;
//                        writeSmokeData(btPowerValue);
//
////                        mSendThread = new SendThread();
////                        mSendThread.start();
//                    }
//
//                    powerSetDialog.dismiss();
//                } else {
//                    ToastUtils.showTextToast(context, getResources().getString(R.string.toast_power_choice));
//                }
//            }
//        });
//        powerSetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                if (powerClickPosition != -1) {
//                    powerClickPosition = -1;
//                }
//            }
//        });
//    }
//
//    private void writeSmokeData(byte[] smokeData) {
//        try {
//            needRead = true;
//            mOutputStream.write(smokeData);
//            try {
//                Thread.sleep(500);
//                sendSmokeData(getPowerValue); //获取功率值
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        for (int i = 0; i < smokeData.length; i++) {
////            try {
////                mOutputStream.write(smokeData[i]);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }
//    }
//
//    private void sendSmokeData(byte[] getPowerData) {
//        try {
//            needRead = true;
//            mOutputStream.write(getPowerData);//获取功率值
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
////    public String m_strBaud = "";
////    public String m_strPort = "";
//
//    public SerialPort getSerialPort() {
//        if (sp == null) {
//
////            String str = "/dev/ttyMT1";
//            String str = "/dev/ttyS1";
//            int i = 115200;
////            int i = 57600;
////            m_strPort = str;
////            m_strBaud = i + "";
//            if ((str.length() == 0) || (i == -1))
//                throw new InvalidParameterException();
//            try {
//                sp = new SerialPort(new File(str), i, 0);
//            } catch (SecurityException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return sp;
//    }
//
//
////    private class ReadThread extends Thread {
////        @Override
////        public void run() {
////            super.run();
////            try {
////                while (needRead) {
////                    if (mInputStream == null)
////                        return;
////                    int count = 6;
////                    byte[] buffer = new byte[count];//4位帧控制,第4位是帧长度，
////                    int availableCount = 0;
////                    int offset;
////                    if (mInputStream.available() >= 4) {
////                        int nContnetCount;
////                        mInputStream.read(buffer, 0, 1);
////                        if (buffer[0] != (byte) 0xF2) // header is not correct, abandoned
////                        {
////                            continue;
////                        } else {
////                            mInputStream.read(buffer, 0, 2);
////                            if (buffer[0] != (byte) 0xF2) {
////                                if (buffer[0] == (byte) 0xF2 && buffer[1] == (byte) 0x04) {
////                                    mInputStream.read(buffer, 0, 2);
////                                    nContnetCount = (int) (buffer[1]) + 3;
////                                    offset = 5;
////                                } else {
////                                    continue;
////                                }
////                            } else {
////                                mInputStream.read(buffer, 0, 1);
////                                nContnetCount = (int) (buffer[0]) + 3;
////                                offset = 4;
////                            }
////
////                        }
////                        if (nContnetCount <= 0) {
////                            continue;
////                        }
////
////                        byte[] bufferContent = new byte[nContnetCount];//4位帧控制,第4位是帧长度，3位帧校验，其他为帧数据长度
////                        while (availableCount < nContnetCount + offset) {
////                            availableCount = mInputStream.available();
////                        }
////                        mInputStream.read(bufferContent, 0, nContnetCount);
////
////                        onDataReceived(bufferContent, nContnetCount);
////                    }
////                    Thread.sleep(10);
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////
////        }
////    }
//
//
////    private class SendThread extends Thread {
////        @Override
////        public void run() {
////            super.run();
//////            byte[] bCommand = new byte[]{0x25, 0x01, 0x01, 0x00};
////            int powerValue = PreferenceData.loadPowerValue(context);
////            byte[] bCommand = new byte[15];
////            bCommand[0] = (byte) 0xf2;
////            bCommand[1] = 0x31;
////            bCommand[2] = 0x00;
////            bCommand[3] = 0x0b;
////            bCommand[4] = 0x01;
//////            byte[] bCommand = new byte[]{(byte)0xF2, 0x31, 0x00, 0x0b, 0x01, (byte) (powerValue*10), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
////            byte[] powerBytes = FormatTransfer.toHH((short) (powerValue * 10));
////            System.arraycopy(powerBytes, 0, bCommand, 5, powerBytes.length); //将功率算出放在第6,第7位
////
////
////            byte[] byt = new byte[16];
////            System.arraycopy(bCommand, 0, byt, 0, bCommand.length);
////
////            byte[] encryptValue = getEncryptValue(bCommand);
////            byt[15] = encryptValue[encryptValue.length - 1]; //将异或数放在数组的第16位
////
////            try {
////                mOutputStream.write(byt);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////
////        }
////    }
//
//
//    private class ReadThread extends Thread {
//        @Override
//        public void run() {
//            super.run();
////            while (!isInterrupted()) {
////                ReadSerialData();
////            }
//            while (!isInterrupted()){
//                if (needRead) {
//                    ReadSerialData();
//                }
//            }
//        }
//    }
//
//
//    private int j = 0;
//    private void ReadSerialData() {
//        int size;
//        try {
//            byte[] buffer = new byte[]{0, 0, 0, 0, 0, 0};
//            if (mInputStream == null)
//                return;
//
//			/*
//             * if (i == 5) { i = 0; } while (i < 5) { buffer[i] = (byte)
//			 * mInputStream.read(); i++; }
//			 */
//            //Log.d("zhantaiming", "11111==");
//            while (j < 1) {
//                buffer[j] = (byte) mInputStream.read();
//                //Log.d("zhantaiming", "11111=="+dumpBytes(buffer[j],1));
//
//                if (((buffer[j] & 0xff)) != 0XF2) {
//                    return;
//                }
//                j++;
//            }
//            while (j < 2) {
//                buffer[j] = (byte) mInputStream.read();
//                //Log.d("zhantaiming", "11111=="+dumpBytes(buffer[j],1));
//
//                if (((int) (buffer[j] & 0xff)) != 0X05) {
//                    return;
//                }
//                j++;
//            }
//            while (j < 6) {
//                buffer[j] = (byte) mInputStream.read();
//                //Log.d("zhantaiming", "11111==");
//                j++;
//            }
//
//            if (j > 0)
//                onDataReceived(buffer, 6);
//            else {
//                j = 0;
//                return;
//            }
//
//            j = 0;
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
////    private void onDataReceived(byte[] buffer, int size) {
////
////        String str = FormatTransfer.BytesToString(buffer, size);
////        byte[] byrssi = new byte[]{buffer[2]};
////        Integer.parseInt(FormatTransfer.BytesToString(byrssi).trim(), 16);
////
////        byte[] bytes = new byte[]{buffer[4], buffer[5], buffer[6], buffer[7]};
////        final long cardNo = FormatTransfer.bytesToLong(bytes);
//
////    }
//
//
//    private void onDataReceived(final byte[] buffer, final int size) {
//
//        runOnUiThread(new Runnable() {
//
//            public void run() {
//
//                byte[] currentpower = new byte[1];
//                currentpower[0] = buffer[4];
//                int lenght = currentpower.length;
//                String powerValue = dumpBytes(currentpower, lenght);
//
//                selectview_current_power.setContent("当前功率" + "    " + powerValue + "W");
//
//                PreferenceData.savePowerValue(context, Integer.parseInt(powerValue));
//                tv_setting_power.setText(PreferenceData.loadPowerValue(context) + " " + getResources().getString(R.string.smoke_power_unit));
//
//
//                EventBusMsg eventBusMsg = new EventBusMsg();
//                eventBusMsg.setMsgType(EventBusMsg.MSG_REFRESH_POWER_VALUE);
//                EventBus.getDefault().post(eventBusMsg);
//
//                needRead = false;
//
//            }
//        });
//    }
//
//    public String dumpBytes(byte[] bytes, int size) {
//        int i;
//        StringBuffer sb = new StringBuffer();
//        String s;
//        for (i = 0; i < size; i++) {
//            /*
//			 * if (i % 32 == 0 && i != 0) { sb.append(" \n "); }
//			 */
//            int data = (int) (bytes[i] & 0xff);
//            //s = Integer.toHexString(data);
//            s = Integer.toString(data);
//            //if (s.length() < 2) {
//            //	s = "0" + s;
//            //}
//			/*
//			 * if (s.length() > 2) { s = s.substring(s.length() - 2); }
//			 */
//            sb.append(s);
//        }
//        return sb.toString();
//    }
//
//
//    //循环异或加密
//    public byte[] getEncryptValue(byte[] bytes) {
//        if (bytes == null) {
//            return null;
//        }
//        int len = bytes.length;
//        int key = 0xF2;
//        for (int i = 0; i < len; i++) {
//            bytes[i] = (byte) (bytes[i] ^ key);
//            key = bytes[i];
//        }
//        return bytes;
//    }
//
////    private class SendSettingThread extends Thread {
////        @Override
////        public void run() {
////            super.run();
////            byte b = FormatTransfer.toLH(regionNum)[0];
////            byte[] bCommand = new byte[]{0x29, 0x00, 0x01, b};
////            sendFilterData(bCommand);
////        }
////    }
//
//    public void closeSerialPort() {
//        if (this.sp != null) {
//            this.sp.close();
//            this.sp = null;
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (powerValueList != null) {
//            powerValueList.clear();
//        }
//
//        if (planMouthList != null) {
//            planMouthList.clear();
//        }
//
//
//        if (mReadThread != null) {
//            mReadThread.interrupt();
//            closeSerialPort();
//        }
////        if (mSendThread != null) {
////            mSendThread.interrupt();
////        }
//
//    }
//}
