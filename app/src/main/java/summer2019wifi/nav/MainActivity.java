package summer2019wifi.nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private WifiManager wManager;

    //private WifiReceiver wRec = new WifiReceiver();
    private String SSID, BSSID;
    private int RSSI;

    private List<ScanResult> list;
    private int size;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        private NetworkInfo info;

        @Override
        public void onReceive(Context context, Intent intent) {
            info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            Log.d("WinFo", info.toString());
            if (ConnectivityManager.TYPE_WIFI == info.getType()) {
                if (info != null && info.isConnected()) {

                    WifiInfo winfo = wManager.getConnectionInfo();
                    SSID = winfo.getSSID();
                    BSSID = winfo.getBSSID();
                    RSSI = winfo.getRssi();

                }
            }
        }
    };

    private BroadcastReceiver receiverScanner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                list = wManager.getScanResults();
                unregisterReceiver(this);
                Log.d("WinFo", String.valueOf(list.size()));
                size = list.size();


            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Load", "loaded");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView labelNetworks = findViewById(R.id.labelNetworks);
        final ListView networksList = findViewById(R.id.list);
        final String ITEM_KEY = "key";

        final ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
        final SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this,arraylist,R.layout.row ,new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        networksList.setAdapter(simpleAdapter);


        wManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registerReceiver(receiver, intentFilter); //displaying connected AP

                registerReceiver(receiverScanner, new IntentFilter((WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))); // display list

                wManager.startScan();
                arraylist.clear();

                labelNetworks.setText(SSID + " " + BSSID + " " + RSSI);

                Toast.makeText(MainActivity.this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
                try
                {
                    int counter = 0;

                    while (counter < size) {
                        Log.d("Winfo", list.get(counter).SSID + " " + list.get(counter).BSSID + list.get(counter).level);

                        HashMap<String, String> item = new HashMap<String, String>();
                        if (list.get(counter).SSID.equals("ubcsecure")) {
                            item.put(ITEM_KEY, list.get(counter).SSID + "  " + list.get(counter).BSSID + " " + list.get(counter).level);
                            arraylist.add(item);
                        }
                        counter++;
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                { Log.d("WinFo", e.toString());};


                handler.postDelayed(this, 5000);
            }
        }, 5000);

//        Button buttonScan = findViewById(R.id.buttonScan);
//        buttonScan.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                arraylist.clear();
//
//                labelNetworks.setText(SSID + " " + BSSID + " " + RSSI);
//
//                Toast.makeText(MainActivity.this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
//                try
//                {
//                    size = size - 1;
//
//                    while (size >= 0) {
//                        Log.d("Winfo", list.get(size).SSID + " " + list.get(size).BSSID);
//
//                        HashMap<String, String> item = new HashMap<String, String>();
//                        if (list.get(size).SSID.equals("ubcsecure")) {
//                            item.put(ITEM_KEY, list.get(size).SSID + "  " + list.get(size).BSSID + " " + list.get(size).level);
//                            arraylist.add(item);
//                        }
//                        size--;
//                        simpleAdapter.notifyDataSetChanged();
//                    }
//                }
//                catch (Exception e)
//                { Log.d("WinFo", e.toString());};
//
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }


}



