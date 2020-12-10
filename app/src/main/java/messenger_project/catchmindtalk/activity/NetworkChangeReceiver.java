package messenger_project.catchmindtalk.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {// connected to the internet

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                // connected to wifi
                Intent local = new Intent();
                local.setAction("receiver.to.activity.transfer");
                local.putExtra("wifi","wifi");
                context.sendOrderedBroadcast(local,null);

                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();

                Log.d("NetworkChangeReceiver","Receiver wifi##");


            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                // connected to the mobile provider's data plan
                Intent local = new Intent();
                local.setAction("receiver.to.activity.transfer");
                local.putExtra("wifi","mobile");
                context.sendOrderedBroadcast(local,null);

                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();


                Log.d("NetworkChangeReceiver","Receiver mobile##");


            }
        } else {
            // not connected to the internet
            Toast.makeText(context, "인터넷 연결 해제", Toast.LENGTH_SHORT).show();
        }

    }

}
