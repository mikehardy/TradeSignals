package net.mikehardy.TradeSignals;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm not implemented yet", Toast.LENGTH_LONG).show();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return;
        }
        Intent i = new Intent(context, UpdateAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Milliseconds * Second * Minute
    }
//
//    public void cancelAlarm(Context context) {
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (am == null) {
//            return;
//        }
//        Intent intent = new Intent(context, UpdateAlarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        am.cancel(sender);
//    }
}