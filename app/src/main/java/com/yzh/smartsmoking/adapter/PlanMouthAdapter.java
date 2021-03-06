/**
 *
 */
package com.yzh.smartsmoking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzh.smartsmoking.R;

import java.util.ArrayList;
import java.util.List;


public class PlanMouthAdapter extends BaseAdapter {
    private Context context;
    private int selectItem = -1;//选中项

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    private List<String> repairList;

    public PlanMouthAdapter(Context context) {
        this.context = context;
        this.repairList = new ArrayList<>();
    }

    public void setDatas(List<String> datas){
        this.repairList = datas;
        this.notifyDataSetChanged();
    }

    /**
     * 追加数据
     * @param datas
     */
    public void addDatas(List<String> datas){
        this.repairList.addAll(datas);
        this.notifyDataSetChanged();
    }

    /**
     * 删除数据源
     * @param position
     */
    public void deleteDatas(int position){
        this.repairList.remove(position);
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return repairList.size();
    }


    @Override
    public Object getItem(int position) {
        return repairList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_plan_mouth, parent, false);
            holder = new ViewHolder();
            holder.ll_item_plan_mouth = (LinearLayout) convertView.findViewById(R.id.ll_item_plan_mouth);
            holder.ll_item_mouth_choice_icon = (LinearLayout) convertView.findViewById(R.id.ll_item_mouth_choice_icon);
            holder.tv_item_plan_mouth = (TextView) convertView.findViewById(R.id.tv_item_plan_mouth);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_item_plan_mouth.setText(repairList.get(position));


        if (position == selectItem) {
            holder.ll_item_plan_mouth.setBackgroundResource(R.color.color_power_itemchoiced);
            holder.ll_item_mouth_choice_icon.setVisibility(View.VISIBLE);

        } else {
            holder.ll_item_plan_mouth.setBackgroundResource(R.color.white);
            holder.ll_item_mouth_choice_icon.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        private LinearLayout ll_item_plan_mouth;
        private LinearLayout ll_item_mouth_choice_icon;
        private TextView tv_item_plan_mouth;

    }

    //	public interface OnItemClickListener{
    //		void onClickListen(View view, int position);
    //	}

}
