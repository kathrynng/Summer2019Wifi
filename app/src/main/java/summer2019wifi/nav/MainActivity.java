package summer2019wifi.nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
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

    private WifiReceiver wRec = new WifiReceiver();
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
            if (ConnectivityManager.TYPE_WIFI == info.getType()){
                if (info != null && info.isConnected()){



                    WifiInfo winfo = wManager.getConnectionInfo();
                    SSID = winfo.getSSID();
                    BSSID = winfo.getBSSID();
                    RSSI = winfo.getRssi();

                   list = wManager.getScanResults();
                   size = list.size();

                }
            }
        }

//        public List<ScanResult> getList(){
//            return list;
//        }
//
//        public int getSize(){
//            return size;
//        }
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
        final IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);

        Button buttonScan = findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                arraylist.clear();
                wManager.startScan();

                Toast.makeText(MainActivity.this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
                try
                {
                    size = size - 1;
                    while (size >= 0)
                    {
                        Log.d("Item",list.get(size).BSSID);
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put(ITEM_KEY, list.get(size).SSID + "  " + list.get(size).capabilities);

                        arraylist.add(item);
                        size--;
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                { }

                // String networkName =

                labelNetworks.setText(SSID + " " + BSSID + " " + RSSI);

            }
        });
    }

}

