package summer2019wifi.nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {
    protected String BSSID = "None";
    protected  String NetworkName = "None";

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()){
            WifiManager wManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            WifiInfo winfo = wManager.getConnectionInfo();
            setBSSID(winfo.getBSSID());
            setNetworkName(winfo.getSSID());
        }
    }

    public void setNetworkName(String networkName){
        this.NetworkName = networkName;
    }

    public void setBSSID(String BSSID){
        this.BSSID = BSSID;
    }

    public String getNetworkName(){
        return NetworkName;
    }

    public String getBSSID(){
        return BSSID;
    }
}
