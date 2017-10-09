package com.libraryutils;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by appadmin on 2017/6/16.
 */

public class ListViewRefreshUtils {
//    private Context context;
    /**
     * ListView设置更新时间(Fragment)
     * @param refreshView
     */
    public static void setUpdateTime(PullToRefreshBase refreshView) {
        String label = getStringDateNow();
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
    }


    /**
     * 获取现在时间
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    private static String getStringDateNow() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


//    /**
//     * ListView初始化列表刷新时的提示文本(Fragment)
//     */
//    public void initListViewTipText(PullToRefreshListView pListView, boolean b) {
//        // 设置上拉刷新文本
//        ILoadingLayout startLabels = pListView.getLoadingLayoutProxy(true, false);
//
//        startLabels.setPullLabel(context.getResources().getString(R.string.pull_down_refresh));
//        startLabels.setReleaseLabel(context.getResources().getString(R.string.release_refresh));
//        startLabels.setRefreshingLabel(context.getResources().getString(R.string.refreshing));
//
//        // 设置下拉刷新文本
//        ILoadingLayout endLabels = pListView.getLoadingLayoutProxy(false, true);
//        if (b) {
//            endLabels.setPullLabel(context.getResources().getString(R.string.pull_up_load_more));
//            endLabels.setReleaseLabel(context.getResources().getString(R.string.release_load_more));
//            endLabels.setRefreshingLabel(context.getResources().getString(R.string.last_page));
//        } else {
//            endLabels.setPullLabel(context.getResources().getString(R.string.pull_up_load_more));
//            endLabels.setReleaseLabel(context.getResources().getString(R.string.release_load_more));
//            endLabels.setRefreshingLabel(context.getResources().getString(R.string.loading_more));
//        }
//
//    }
}
