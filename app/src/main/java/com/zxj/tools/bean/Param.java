package com.zxj.tools.bean;

/**
 * Created by zxj on 2015/11/17.
 */
public class Param {
    public Param()
    {
    }

    public Param(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    String key;
    String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
