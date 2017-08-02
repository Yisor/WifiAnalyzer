package com.komect.wifi;

/**
 * Created by lsl on 2017/8/1.
 */

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

public class WifiAdmin {
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiLock mWifiLock;


    // 构造器
    public WifiAdmin(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }


    // 打开WIFI
    public void openWifi(Context context) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        else if (mWifiManager.getWifiState() == 2) {
            Toast.makeText(context, "Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
        }
    }


    // 关闭WIFI
    public void closeWifi(Context context) {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
        else if (mWifiManager.getWifiState() == 1) {
            Toast.makeText(context, "Wifi已经关闭，不用再关了", Toast.LENGTH_SHORT).show();
        }
        else if (mWifiManager.getWifiState() == 0) {
            Toast.makeText(context, "Wifi正在关闭，不用再关了", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "请重新关闭", Toast.LENGTH_SHORT).show();
        }
    }


    // 检查当前WIFI状态
    public void checkState(Context context) {
        if (mWifiManager.getWifiState() == 0) {
            Toast.makeText(context, "Wifi正在关闭", Toast.LENGTH_SHORT).show();
        }
        else if (mWifiManager.getWifiState() == 1) {
            Toast.makeText(context, "Wifi已经关闭", Toast.LENGTH_SHORT).show();
        }
        else if (mWifiManager.getWifiState() == 2) {
            Toast.makeText(context, "Wifi正在开启", Toast.LENGTH_SHORT).show();
        }
        else if (mWifiManager.getWifiState() == 3) {
            Toast.makeText(context, "Wifi已经开启", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "没有获取到WiFi状态", Toast.LENGTH_SHORT).show();
        }
    }


    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }


    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }


    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }


    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }


    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }


    static final String SECURITY_NONE = "无";
    static final String SECURITY_WEP = "WEP";
    static final String SECURITY_PSK = "PSK";
    static final String SECURITY_EAP = "EAP";


    public String getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(
                WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }


    public void startScan(Context context) {
        mWifiManager.startScan();
        //得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        Log.d("TAG", "startScan: " + mWifiList.size());
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        if (mWifiList == null) {
            if (mWifiManager.getWifiState() == 3) {
                Toast.makeText(context, "当前区域没有无线网络", Toast.LENGTH_SHORT).show();
            }
            else if (mWifiManager.getWifiState() == 2) {
                Toast.makeText(context, "wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return sortByLevel(mWifiList);
    }


    //将搜索到的wifi根据信号从强到弱进行排序
    private List<ScanResult> sortByLevel(List<ScanResult> list) {
        ScanResult temp;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).level > list.get(j).level)    //level属性即为强度
                {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        return list;
    }


    /**
     * 判断wifi是否为2.4G
     */
    public static boolean is24GHz(int freq) {
        return freq > 2400 && freq < 2500;
    }


    /**
     * 判断wifi是否为5G
     */
    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }


    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }


    // 得到MAC地址
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }


    // 得到接入点的BSSID
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }


    // 得到IP地址
    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }


    // 得到连接的ID
    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }


    // 得到连接的SSID
    public String getSSID() {
        return (mWifiInfo == null) ? "unknown ssid" : mWifiInfo.getSSID();
    }


    public int getLinkSpeed() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getLinkSpeed();
    }


    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }


    // 添加一个网络并连接
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
    }


    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }


    public void removeWifi(int netId) {
        disconnectWifi(netId);
        mWifiManager.removeNetwork(netId);
    }


    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }
}
