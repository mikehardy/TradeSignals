package net.mikehardy.TradeSignals;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mike on 2/26/16.
 */
public class UpdateService extends Service
{
    UpdateAlarm alarm = new UpdateAlarm();
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        alarm.SetAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        alarm.SetAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
