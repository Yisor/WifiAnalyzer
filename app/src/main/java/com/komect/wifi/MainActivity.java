package com.komect.wifi;

import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends CheckPermissionsActivity
        implements ConnectionClassManager.ConnectionClassStateChangeListener {

    public static final String TAG = "MainActivity";
    protected WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private TextView currentSsid;
    private TextView currentChannel;
    private TextView linkSpeed;
    private TextView downloadSpeed;

    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private static final String mURL = "http://image.elegantliving.ceconline.com/320000/320100/20110815_03_52.jpg";

    private int mTries = 0;
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
    }


    /*
    * 控件初始化
    * */
    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.wifi_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new RecyclerViewAdapter(mWifiList));
        currentSsid = (TextView) findViewById(R.id.current_ssid);
        currentChannel = (TextView) findViewById(R.id.current_channel);
        linkSpeed = (TextView) findViewById(R.id.link_speed);
        downloadSpeed = (TextView) findViewById(R.id.download_speed);

        // 下载速度测试
        mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        new DownloadImage().execute(mURL);

        mWifiAdmin = new WifiAdmin(MainActivity.this);
        currentSsid.setText(mWifiAdmin.getSSID());
        linkSpeed.setText(String.format("连接速度：%dMbps", mWifiAdmin.getLinkSpeed()));
        currentChannel.setText(String.format("信道：%d", WifiUtil.getCurrentChannel(this)));

        // 定时任务
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(mTimerTask, 0, 3000, TimeUnit.MILLISECONDS);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mConnectionClassManager.remove(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mConnectionClassManager.register(this);
    }


    /**
     * 下载任务
     */
    private class DownloadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDeviceBandwidthSampler.startSampling();
        }


        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                // Open a stream to download the image from our URL.
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];

                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while downloading image.");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            mDeviceBandwidthSampler.stopSampling();
            // Retry for up to 10 times until we find a ConnectionClass.
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++;
                new DownloadImage().execute(mURL);
            }
        }
    }

    /**
     * 定时任务
     */
    final TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            Log.d("Tlog", "run: ");
            if (mWifiAdmin != null) {
                mWifiAdmin.startScan(MainActivity.this);
                mWifiList = mWifiAdmin.getWifiList();
                if (mWifiList != null) {
                    Message msg = new Message();
                    msg.obj = mWifiList;
                    mHandler.sendMessage(msg);
                    Log.d("Tlog", "runing ");
                }
            }
        }
    };

    /**
     * 刷新列表
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter.updateData((List<ScanResult>) msg.obj);
        }
    };


    /**
     * The method that will be called when {@link ConnectionClassManager}
     * changes ConnectionClass.
     *
     * @param bandwidthState The new ConnectionClass.
     */
    @Override public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
        mConnectionClass = bandwidthState;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final double downloadKBitsPerSecond = mConnectionClassManager.getDownloadKBitsPerSecond();
                Log.d(TAG, "run: " + downloadKBitsPerSecond);
                if (downloadKBitsPerSecond < 100) {
                    downloadSpeed.setText(
                            String.format("下载速度：%.1f kb/s", mConnectionClassManager.getDownloadKBitsPerSecond()));
                } else {
                    final double downloadMPerSecond = downloadKBitsPerSecond / 1000;
                    downloadSpeed.setText(String.format("下载速度：%.1f m/s", downloadMPerSecond));
                }
            }
        });
    }
}
