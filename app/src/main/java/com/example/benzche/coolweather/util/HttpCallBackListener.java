package com.example.benzche.coolweather.util;

/**
 * Created by benz.che on 4/20/2016.
 */
public interface HttpCallBackListener {
    public void onFinish(String response);

    public void onError(Exception e);
}
