package comp7506.gpassignment;

import android.Manifest;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private IncomingCallMonitor callMonitor;
    private IntentFilter intentFilter;
    private TextView Header, CallListView, Header_History;
    private Button btn_Setting, btn_history;
    private ToggleButton btn_spam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start spam warning function
        checkPermission();
        callMonitor = new IncomingCallMonitor();
        intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");

        Header = (TextView) findViewById(R.id.Header);
        Header_History = (TextView) findViewById(R.id.Header_History);
        CallListView = (TextView) findViewById(R.id.CallListView);
        btn_Setting = (Button)findViewById(R.id.btn_Setting);
        btn_history = (Button)findViewById(R.id.btn_history);
        btn_spam = (ToggleButton) findViewById(R.id.btn_spam);

        Header.setText("Settings");
        Header_History.setVisibility(View.GONE);


        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header.setText("Settings");
                Header_History.setVisibility(View.GONE);
                CallListView.setText("");
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Header.setText("Unknown Call History");
                Header_History.setVisibility(View.VISIBLE);
                CallListView.setText("");
                PrintCallHistory();
            }
        });

        btn_spam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    try
                    {
                        registerReceiver(callMonitor, intentFilter);
                    }catch (Exception e){}
                } else {
                    // The toggle is disabled
                    try
                    {
                        unregisterReceiver(callMonitor);
                    }catch (Exception e){}
                }
            }
        });
        btn_spam.toggle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //method to register the call monitoring service
        //registerReceiver(callMonitor, intentFilter);
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

    private void PrintCallHistory(){
        int IncrementedValue = 0;

        //Retrieve unknown call information from SharedPreferences
        //Display the unknown Call History

        SharedPreferences CallHistory = PreferenceManager.getDefaultSharedPreferences(this);

        //The below hided codes are used to clear the data saved in SharedPreferences
        //SharedPreferences.Editor HistoryEditor = CallHistory.edit();
        //HistoryEditor.clear();
        //HistoryEditor.apply();

        while (!(CallHistory.getString("History" + IncrementedValue,"").equals(""))) {
            CallListView.append(CallHistory.getString("History" + IncrementedValue,"") + "\n\n");
           IncrementedValue++;
        }



    }

}
