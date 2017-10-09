package com.yzh.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.libraryutils.DialogUtils;
import com.libraryutils.LogUtils;
import com.libraryutils.ToastUtils;
import com.libraryutils.WebServiceUtils;
import com.yzh.smartsmoking.R;
import com.yzh.smartsmoking.eventbus.EventBusMsg;
import com.yzh.utils.ScreenUtil;

import net.tsz.afinal.FinalDb.DbUpdateListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;


/**
 * LuckeyBaseFragment
 *
 * @author Tom
 * @date 2015/01/15 13:10
 */
public abstract class BaseFragment extends Fragment implements DbUpdateListener {


    private static final String TAG = BaseFragment.class.getName();

    protected Context context;
    protected DialogUtils dlgWaiting;
    protected static final int MSG_cannt_get_data = 2000;


    protected Handler mDlgWaitingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {

                    case MSG_cannt_get_data:
                        if (dlgWaiting.isShowing()) {
                            dlgWaiting.dismiss();
                            ToastUtils.showTextToast(getContext(), getResources().getString(R.string.network_error));
                        }
                        break;

                    default:
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕不变黑
        dlgWaiting = DialogUtils.createDialog(getActivity(),
                DialogUtils.REFRESH);
        dlgWaiting.setCanceledOnTouchOutside(false);
//        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(),container,false);
        EventBus.getDefault().register(this);
//        EventBus.getDefault().register(this);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //查找actionbar控件设置paddingtop
        BaseActivity activity = (BaseActivity) getActivity();
        //判断activity是否开启沉浸式通知栏
        if(activity.isOpenStatus()){
            View actionbarview = view.findViewById(R.id.actionbar);
            if(actionbarview != null){
                int heigth = ScreenUtil.getStateBarHeight(getActivity());
                actionbarview.setPadding(0, heigth, 0, 0);
            }
        }
        initView(view);
        initEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected void initView(View view){}

    protected void initEventListener(){}

    protected abstract int getContentView();

    /**
     * 查找页面布局ID所对应的控件对象，不用强制转换
     * @param resId
     * @param <T>
     * @return
     */
    protected  <T> T findViewByIds(int resId){
        return (T) getActivity().findViewById(resId);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreateView(inflater, container, savedInstanceState);
//        context = getActivity();
//        getActivity().getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕不变黑
//        dlgWaiting = DialogUtils.createDialog(getActivity(),
//                DialogUtils.REFRESH);
//        dlgWaiting.setCanceledOnTouchOutside(false);
//        EventBus.getDefault().register(this);
//        return null;
//    }

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
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStop() {
        logMessage("--->BaseFragment onStop()...");
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        logMessage("--->onDestroy()...");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }






    protected void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showToast(String msg, int duration) {
        Toast.makeText(getActivity(), msg, duration).show();
    }




    protected void doSocket() {
        dlgWaiting.show();
        mDlgWaitingHandler.sendEmptyMessageDelayed(MSG_cannt_get_data, App.WAITTING_SECOND);
    }



    protected void doSocketConnectServerFailed(int type, String jsonStr) {
    }



    // --------------------------------------监听网络变化
    // End-------------------------------------------
    // -------------------------------------call web service
    // start-------------------------------------/
    protected void callWebService(final Context context, boolean refreshFlag,
                                  String url, final int type) {
        WebServiceUtils.callWebService(context, refreshFlag, url,
                new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void failure(Context context, String callbackStr,
                                        AjaxStatus status) {
                        doSocketConnectServerFailed(type, callbackStr);
                    }

                    @Override
                    public void success(String url, String callbackStr,
                                        AjaxStatus status) {
                        // TODO Auto-generated method stub
                        onWebServiceReturn(type, url, callbackStr);
                    }

                });
    }

    protected void onWebServiceReturn(int type, String url, String returnMsg) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
            showToast("更新数据库失败");
        }
    }


    //截图 (分享)
    protected File takeScreenShot(){
        View vw = getActivity().getWindow().getDecorView();
        vw.setDrawingCacheEnabled(true);
        vw.buildDrawingCache();
        Bitmap bitmap = vw.getDrawingCache();

//        // 获取屏幕长和高
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        vw.destroyDrawingCache();

        //获取系统缓存文件
        File cacheFile = new File(getActivity().getCacheDir(),String.valueOf(System.currentTimeMillis()));

        FileOutputStream out=null;
        try {
            out = new FileOutputStream(cacheFile);
            b.compress(Bitmap.CompressFormat.PNG,90,out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cacheFile;

    }

    //分享
//    protected void showShare(String filePath) {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.setImagePath(filePath);//确保SDcard下面存在此张图片
//
//        oks.show(context);//启动分享
//    }

    // -------------------------------------call web service
    // end-------------------------------------/



    /**
     * ListView初始化列表刷新时的提示文本(Fragment)
     */
    protected void initFragmentListViewTipText(PullToRefreshListView pListView, boolean b) {
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



}
