package com.mtf.citypicker;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mtf.citypicker.adapter.AreaAdapter;
import com.mtf.citypicker.db.GreenDaoUtils;
import com.mtf.citypicker.entity.Area;
import com.mtf.citypicker.green.AreaDao;

import java.util.List;

import static com.mtf.citypicker.R.id.listView;

/**
 * Created by mtf on 2017/10/19.
 */

public abstract class CityPicker implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int INDEX_TAB_PROVINCE = 0;//省份标志
    private static final int INDEX_TAB_CITY = 1;//城市标志
    private static final int INDEX_TAB_COUNTY = 2;//乡镇标志
    private int tabIndex = INDEX_TAB_PROVINCE; //默认是省份

    private int provinceIndex; //省份的下标
    private int cityIndex;//城市的下标
    private int countyIndex;//乡镇的下标

    private Context mContext;
    private View view;
    private ListView mListView;//城市列表
    private TextView tvProvince;//省
    private TextView tvCity;//市
    private TextView tvCounty;//县

    private List<Area> mAreaList;

    private AreaAdapter provinceAdapter;
    private AreaAdapter cityAdapter;
    private AreaAdapter countyAdapter;

    private AreaDao mAreaDao;


    private int selectedColor = android.R.color.holo_orange_light;//设置tab选中时颜色
    private int unSelectedColor = android.R.color.holo_blue_light;//设置tab未选择时颜色

    public CityPicker(Context context) {
        this.mContext = context;
        initViews();
        setProvinceData();
        updateTabsVisibility();
        updateTabsStyle();
    }

    private void initViews() {
        view = LayoutInflater.from(mContext).inflate(R.layout.city_picker, null);
        mListView = view.findViewById(listView);//城市列表
        tvProvince = view.findViewById(R.id.tv_province);
        tvCity = view.findViewById(R.id.tv_city);
        tvCounty = view.findViewById(R.id.tv_county);

        mListView.setOnItemClickListener(this);
        tvProvince.setOnClickListener(this);
        tvCity.setOnClickListener(this);
        tvCounty.setOnClickListener(this);
    }

    /**
     * 加载省级数据
     */
    private void setProvinceData() {
        provinceAdapter = getAreaData(0);
        mListView.setAdapter(provinceAdapter);
    }

    /**
     * 根据省份ID查找城市数据
     *
     * @param pid
     */
    private void setCityData(int pid) {
        cityAdapter = getAreaData(pid);
        mListView.setAdapter(cityAdapter);
    }

    /**
     * 根据城市ID查找区县数据
     *
     * @param pid
     */
    private void setCountyData(int pid) {
        countyAdapter = getAreaData(pid);
        mListView.setAdapter(countyAdapter);
    }

    /**
     * 根据PID查询城市数据
     *
     * @param pid
     * @return
     */
    private AreaAdapter getAreaData(int pid) {
        if (mAreaDao == null) {
            mAreaDao = GreenDaoUtils.getSingleton().getDaoSession().getAreaDao();
        }
        mAreaList = mAreaDao.queryBuilder().where(AreaDao.Properties.Pid.eq(pid)).list();
        return new AreaAdapter(mAreaList, mContext);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Area mArea = (Area) adapterView.getItemAtPosition(position);
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                tvProvince.setText(mArea.getName());
                setCityData(mArea.getId());
                if (!isEmpty(mAreaList)) {
                    tabIndex = INDEX_TAB_CITY;
                }
                //设置选择的省份下标
                provinceIndex = position;
                tvCity.setText(mContext.getString(R.string.select));
                tvCounty.setText(mContext.getString(R.string.select));
                break;
            case INDEX_TAB_CITY:
                tvCity.setText(mArea.getName());
                //设置选择的城市下标
                cityIndex = position;
                setCountyData(mArea.getId());
                if (!isEmpty(mAreaList)) {
                    tabIndex = INDEX_TAB_COUNTY;
                    tvCounty.setText(mContext.getString(R.string.select));
                } else {
                    SetCitySelectedListener(provinceAdapter.getItem(provinceIndex), mArea, null);
                }
                break;
            case INDEX_TAB_COUNTY:
                tvCounty.setText(mArea.getName());
                //设置选择的区县下标
                countyIndex = position;
                SetCitySelectedListener(provinceAdapter.getItem(provinceIndex), cityAdapter.getItem(cityIndex), mArea);
                break;
        }

        updateTabsVisibility();
        updateTabsStyle();
        updateListViewSelectItem();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_province) {
            tabIndex = INDEX_TAB_PROVINCE;
        } else if (i == R.id.tv_city) {
            tabIndex = INDEX_TAB_CITY;
        } else if (i == R.id.tv_county) {
            tabIndex = INDEX_TAB_COUNTY;
        }
        updateListViewSelectItem();
    }

    /**
     * 获得view
     *
     * @return
     */
    public View getView() {
        return view;
    }

    /**
     * 更新tab显示和隐藏
     */
    private void updateTabsVisibility() {
        tvProvince.setVisibility(isEmpty(provinceAdapter) ? View.GONE : View.VISIBLE);
        tvCity.setVisibility(isEmpty(cityAdapter) ? View.GONE : View.VISIBLE);
        tvCounty.setVisibility(isEmpty(countyAdapter) ? View.GONE : View.VISIBLE);
    }

    /**
     * 更新tab显示样式
     */
    private void updateTabsStyle() {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvProvince.setTextColor(mContext.getColor(selectedColor));
                    tvCity.setTextColor(mContext.getColor(unSelectedColor));
                    tvCounty.setTextColor(mContext.getColor(unSelectedColor));
                } else {
                    tvProvince.setTextColor(mContext.getResources().getColor(selectedColor));
                    tvCity.setTextColor(mContext.getResources().getColor(unSelectedColor));
                    tvCounty.setTextColor(mContext.getResources().getColor(unSelectedColor));
                }
                break;
            case INDEX_TAB_CITY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvProvince.setTextColor(mContext.getColor(unSelectedColor));
                    tvCity.setTextColor(mContext.getColor(selectedColor));
                    tvCounty.setTextColor(mContext.getColor(unSelectedColor));
                } else {
                    tvProvince.setTextColor(mContext.getResources().getColor(unSelectedColor));
                    tvCity.setTextColor(mContext.getResources().getColor(selectedColor));
                    tvCounty.setTextColor(mContext.getResources().getColor(unSelectedColor));
                }
                break;
            case INDEX_TAB_COUNTY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tvProvince.setTextColor(mContext.getColor(unSelectedColor));
                    tvCity.setTextColor(mContext.getColor(unSelectedColor));
                    tvCounty.setTextColor(mContext.getColor(selectedColor));
                } else {
                    tvProvince.setTextColor(mContext.getResources().getColor(unSelectedColor));
                    tvCity.setTextColor(mContext.getResources().getColor(unSelectedColor));
                    tvCounty.setTextColor(mContext.getResources().getColor(selectedColor));
                }
                break;
        }
    }

    /**
     * 更新ListView选中样式
     */
    private void updateListViewSelectItem() {
        switch (tabIndex) {
            case INDEX_TAB_PROVINCE:
                mListView.setAdapter(provinceAdapter);
                mListView.setSelection(provinceIndex);
                provinceAdapter.setSelectItem(provinceIndex);
                provinceAdapter.notifyDataSetChanged();
                break;
            case INDEX_TAB_CITY:
                mListView.setAdapter(cityAdapter);
                mListView.setSelection(cityIndex);
                cityAdapter.setSelectItem(cityIndex);
                cityAdapter.notifyDataSetChanged();
                break;
            case INDEX_TAB_COUNTY:
                mListView.setAdapter(countyAdapter);
                mListView.setSelection(countyIndex);
                countyAdapter.setSelectItem(countyIndex);
                countyAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 设置选择城市监听事件
     *
     * @param province
     * @param city
     * @param county
     */
    public abstract void SetCitySelectedListener(Area province, Area city, Area county);

    /**
     * 判断List是否为空(是否有数据)
     *
     * @param list
     * @return
     */
    private boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    private boolean isEmpty(AreaAdapter adapter) {
        return adapter == null || adapter.getCount() == 0;
    }

    /**
     * 设置tab选择时的颜色
     * @param selectedColor
     */
    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * 设置tab未选中时的颜色
     * @param unSelectedColor
     */
    public void setUnSelectedColor(int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
    }
}
