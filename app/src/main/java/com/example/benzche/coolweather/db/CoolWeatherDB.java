package com.example.benzche.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.benzche.coolweather.model.City;
import com.example.benzche.coolweather.model.County;
import com.example.benzche.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benz.che on 4/20/2016.
 */
public class CoolWeatherDB {

    public static final String DB_NAME = "Cool_Weather";
    public static final int VERSION = 1;
    private SQLiteDatabase db;
    private static CoolWeatherDB instance;

    public synchronized static CoolWeatherDB getInstance(Context context){
        if(instance==null){
            instance = new CoolWeatherDB(context);
        }
        return instance;
    }

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper helper = new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db = helper.getWritableDatabase();
    }

    public void saveProvince(Province province){
        if(province!=null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        Province province = new Province();
        if(cursor.moveToFirst()){
            do{
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void saveCity(City city){
        if(city!=null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City",null,values);
        }
    }

    public List<City> loadCities(){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,null,null,null,null,null);
        City city = new City();
        if(cursor.moveToFirst()){
            do{
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public void saveCounty(County county){
        if(county!=null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }

    public List<County> loadCounties(){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,null,null,null,null,null);
        County county = new County();
        if(cursor.moveToFirst()){
            do{
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                list.add(county);
            }while (cursor.moveToNext());
        }
        return  list;
    }




}
