package comp7506.gpassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by chia0 on 2/11/2017.
 * Modified by Jamie on 10/11/2017.
 */

public class IncomingCallMonitor extends BroadcastReceiver {

    private String number;

    int IncrementedValue = 0;


    @Override
    public void onReceive(final Context context, Intent intent) {

        try
        {
            //Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_SHORT).show();
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);


            //check for incoming call
            //https://developer.android.com/reference/android/telephony/TelephonyManager.html
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                //checkContactExist(context, number);
                if(!checkContactExist(context, number)) {
                    //Toast.makeText(context, "Unknown number detected: " + number, Toast.LENGTH_SHORT).show();
                    //Unknown number -> start an activity class that jamie will create.

                    //Save the time when incoming call is received
                    DateFormat df = new SimpleDateFormat("dd MMM yyyy (EEE)  HH:mm:ss");
                    df.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
                    String CallTime = df.format(Calendar.getInstance().getTime());

                    //Save the incoming call number and calling time using SharedPreferences, so the data can be retrieved even the app was closed and then restarted
                    SharedPreferences CallHistory = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor HistoryEditor = CallHistory.edit();

                    while (!(CallHistory.getString("History" + IncrementedValue,"").equals(""))) {
                        IncrementedValue++;
                    }
                    HistoryEditor.putString("History" + IncrementedValue, CallTime + "          " + number);
                    HistoryEditor.apply();


                    //delay to call "SpamActivity" after detecting incoming call, then the popup message will show on top of call screen
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(context, SpamActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            context.startActivity(i);
                        }
                    }, 800);

                }
                else{
                    // Toast.makeText(context, "Known number detected: " + number, Toast.LENGTH_SHORT).show();
                    // Known number - do nothing
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkContactExist(Context context, String number )
    {
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

}
