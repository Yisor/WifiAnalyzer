package com.komect.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsl on 2017/8/2.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<ScanResult> resultList;


    public RecyclerViewAdapter(List<ScanResult> resultList) {
        this.resultList = resultList;
        this.resultList = (resultList != null ? resultList : new ArrayList<ScanResult>());
    }


    public void updateData(List<ScanResult> list) {
        this.resultList = list;
        notifyDataSetChanged();
    }


    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wifi_list_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        ScanResult scanResult = resultList.get(position);

        holder.wifiSsid.setText(scanResult.SSID);
        holder.macAddress.setText(String.format("MAC地址：%s", scanResult.BSSID));
        holder.frequency.setText(String.format("频率：%d MHz", scanResult.frequency));

        WifiManager.calculateSignalLevel(scanResult.level, 5);

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
        holder.wifiLevel.setText(String.format("信号强度：%d dBm", scanResult.level));
        holder.channel.setText(String.format("信道: %d", getChannelByFrequency(scanResult.frequency)));
        holder.encryption.setText(String.format("加密方式：%s", encrypTxt.toString()));
    }


    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override public int getItemCount() {
        return (this.resultList != null) ? this.resultList.size() : 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView wifiSsid;
        private TextView macAddress;
        private TextView frequency;
        private TextView wifiLevel;
        private TextView channel;
        private TextView encryption;


        public ViewHolder(View view) {
            super(view);
            wifiSsid = (TextView) view.findViewById(R.id.ssid);
            macAddress = (TextView) view.findViewById(R.id.mac_address);
            frequency = (TextView) view.findViewById(R.id.frequency);
            wifiLevel = (TextView) view.findViewById(R.id.wifi_level);
            channel = (TextView) view.findViewById(R.id.channel);
            encryption = (TextView) view.findViewById(R.id.encryption);
        }
    }


    /**
     * 根据频率获得信道
     */
    private int getChannelByFrequency(int frequency) {
        int channel = -1;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }
}
