package comp7506.gpassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by chia0 on 2/11/2017.
 */

public class IncomingCallMonitor extends BroadcastReceiver {

    private String number;
    @Override
    public void onReceive(Context context, Intent intent) {
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
                    //Intent i = new Intent(context, spamActivity.class);
                    //context.startActivity(i);
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
