package comp7506.gpassignment;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private IncomingCallMonitor callMonitor;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start spam warning function
        checkPermission();
        callMonitor = new IncomingCallMonitor();
        intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");

    }

    @Override
    protected void onResume() {
        super.onResume();
        //method to register the call monitoring service
        registerReceiver(callMonitor, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //method to unregister the call monitoring
        unregisterReceiver(callMonitor);
    }

    //function responsible for checking that the required permission are granted.
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

}
