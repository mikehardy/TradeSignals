package net.mikehardy.TradeSignals;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;

/**
 * Created by mike on 2/26/16.
 */
public class AutoStart extends WakefulBroadcastReceiver
{
    UpdateAlarm alarm = new UpdateAlarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent i = new Intent("net.mikehardy.TradeSignals.START_ALARM");

            alarm.SetAlarm(context);
        }
    }
}