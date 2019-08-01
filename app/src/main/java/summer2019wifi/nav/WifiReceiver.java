// https://stackoverflow.com/questions/21391395/get-ssid-when-wifi-is-connected

package summer2019wifi.nav;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
    protected String BSSID = "None";
    protected  String NetworkName = "None";

    @Override
    public void onReceive(Context context, Intent intent) {

              Log.d("START", "checking for connection");

           if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
               NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
               if (ConnectivityManager.TYPE_WIFI == info.getType()){
                   if (info != null && info.isConnected()){
                       Log.d("CONNECT", "info is connected");
                       WifiManager wManager = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
                       WifiInfo winfo = wManager.getConnectionInfo();
                       setBSSID(winfo.getBSSID());
                       setNetworkName(winfo.getSSID());
               }else{
                       Log.d("Fail2", "Wifi not same");
                   }
           }else{
                   Log.d("Fail", "Connection failed");
               }
        }else{
               Log.d("Fail3", "connection failed");
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
