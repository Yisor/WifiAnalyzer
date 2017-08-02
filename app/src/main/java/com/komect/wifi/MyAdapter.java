package com.komect.wifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import static com.komect.wifi.WifiUtil.getChannelByFrequency;

/**
 * Created by lsl on 2017/8/1.
 */
public class MyAdapter extends BaseAdapter {
    private static final String TAG = "MyAdapter";
    LayoutInflater inflater;
    List<ScanResult> list;
    public int level;


    public MyAdapter(Context context, List<ScanResult> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.wifi_list_item, null);
        ScanResult scanResult = list.get(position);
        TextView wifiSsid = (TextView) view.findViewById(R.id.ssid);
        TextView macAddress = (TextView) view.findViewById(R.id.mac_address);
        TextView frequency = (TextView) view.findViewById(R.id.frequency);
        TextView wifiLevel = (TextView) view.findViewById(R.id.wifi_level);
        TextView channel = (TextView) view.findViewById(R.id.channel);
        TextView encryption = (TextView) view.findViewById(R.id.encryption);

        wifiSsid.setText(scanResult.SSID);
        macAddress.setText(String.format("MAC地址：%s", scanResult.BSSID));
        frequency.setText(String.format("频率：%d MHz", scanResult.frequency));
        Log.i(TAG, "scanResult.SSID=" + scanResult);
        level = WifiManager.calculateSignalLevel(scanResult.level, 5);

        StringBuilder encrypTxt = new StringBuilder();
        String capabilities = scanResult.capabilities;
        if (capabilities.contains("WEP") || capabilities.contains("wep")) {
            encrypTxt.append("WEP ");
        }
        if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
            encrypTxt.append("WPA ");
        }
        if (capabilities.contains("WPA2") || capabilities.contains("wpa2")) {
            encrypTxt.append("WPA2 ");
        }
        wifiLevel.setText(String.format("信号强度：%d dBm", scanResult.level));
        channel.setText(String.format("信道: %d", getChannelByFrequency(scanResult.frequency)));
        encryption.setText(String.format("加密方式：%s", encrypTxt.toString()));

        //判断信号强度
        return view;
    }



}
