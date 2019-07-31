package summer2019wifi.nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private WifiReceiver wRec = new WifiReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager WifiMan;
        WifiMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo Winfo;
        Winfo = WifiMan.getConnectionInfo();

        final TextView labelNetworks = findViewById(R.id.labelNetworks);

        Button buttonScan = findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                wRec.onReceive(getApplicationContext(),getIntent());
                String networkName = wRec.getNetworkName();
                String bssid = wRec.getBSSID();

                labelNetworks.setText(networkName + " " + bssid);
            }
        });
    }

}

