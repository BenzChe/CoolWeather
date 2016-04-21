package com.example.benzche.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benzche.coolweather.R;
import com.example.benzche.coolweather.util.HttpCallBackListener;
import com.example.benzche.coolweather.util.HttpUtil;
import com.example.benzche.coolweather.util.Utility;

import org.w3c.dom.Text;

public class WeatherActivity extends AppCompatActivity {
    private LinearLayout weatherInfoLayout;
    private TextView cityNameText;
    private TextView publishText;

    private TextView weatherDespText;

    private TextView temp1Text;

    private TextView temp2Text;

    private TextView currentDateText;

    private Button switchCity;

    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText=(TextView)findViewById(R.id.city_name);
        publishText=(TextView)findViewById(R.id.publish_text);
        weatherDespText=(TextView)findViewById(R.id.weather_desp);
        temp1Text=(TextView)findViewById(R.id.temp1);
        temp2Text=(TextView)findViewById(R.id.temp2);
        currentDateText=(TextView)findViewById(R.id.current_date);
        switchCity=(Button)findViewById(R.id.switch_city);
        refreshWeather=(Button)findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            publishText.setText("Loading...");
            cityNameText.setVisibility(View.INVISIBLE);
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWeather();
        }
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
            }
        });
        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishText.setText("Loading...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode = prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
            }
        });
    }

    private void queryWeatherCode(String countyCode){
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address,"countyCode");
    }
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(address, "weatherCode");
    }

    private void queryFromServer(final String address, final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("Load Fail!");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        cityNameText.setText( prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText("Today" + prefs.getString("publish_time", "") + "publish");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
