package com.mtf.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtf.citypicker.CityPicker;
import com.mtf.citypicker.entity.Area;

public class MainActivity extends AppCompatActivity {

    private TextView tvAddress;
    private LinearLayout contentCityPicker;
    private View addressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        contentCityPicker = (LinearLayout) findViewById(R.id.ll_cityPicker);

        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressView == null) {
                    getAddress();
                }
                contentCityPicker.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getAddress(){
        CityPicker cityPicker = new CityPicker(this) {
            @Override
            public void SetCitySelectedListener(Area province, Area city, Area county) {
                StringBuilder strCity = new StringBuilder();
                if (province != null) {
                    strCity.append(province.getName());
                }
                if (city != null){
                    strCity.append("-");
                    strCity.append(city.getName());
                }
                if (county != null){
                    strCity.append("-");
                    strCity.append(county.getName());
                }
                tvAddress.setText(strCity.toString());
                contentCityPicker.setVisibility(View.GONE);
            }
        };
        addressView = cityPicker.getView();
        contentCityPicker.addView(addressView);
    }
}
