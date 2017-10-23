package com.mtf.citypicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mtf.citypicker.R;
import com.mtf.citypicker.entity.Area;

import java.util.List;

/**
 * Created by mtf on 2017/10/18.
 */

public class AreaAdapter extends BaseAdapter {
    private List<Area> mList;
    private Context mContext;

    public AreaAdapter(List<Area> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public AreaAdapter() {

    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Area getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holer;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item, parent, false);
            holer = new ViewHoler(convertView);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        holer.textView.setText(mList.get(position).getName());

        if (position == selectItem) {
            holer.textView.setEnabled(false);
        } else {
            holer.textView.setEnabled(true);
        }

        return convertView;
    }

    class ViewHoler {
        TextView textView;

        public ViewHoler(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.tv_city_item);
        }
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    private int selectItem = -1;
}

