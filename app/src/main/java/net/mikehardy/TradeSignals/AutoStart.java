package net.mikehardy.TradeSignals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver
{
    private final UpdateAlarm alarm = new UpdateAlarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))  {
            alarm.setAlarm(context);
        }
    }
}