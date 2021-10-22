package eg.gov.iti.jets.trip_pal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.RemoteViews;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class AlertDialogActivity extends AppCompatActivity {
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alert_dialog);


        Intent intent  = new Intent();
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationSound);

        startAlarmRingTone(ringtone);
        AlertDialog.Builder Builder = new AlertDialog.Builder(this)
                .setMessage("It's time to start "+ intent.getStringExtra("name"))
                .setTitle("Trip reminder")
                .setIcon(android.R.drawable.ic_lock_idle_alarm)
                .setPositiveButton("Start ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        stopAlarmRingTone(ringtone);
                        startActivity(newIntent);
                        finish();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopAlarmRingTone(ringtone);
                        alertDialog.dismiss();
                        finish();
                    }
                });

        alertDialog = Builder.create();
        alertDialog.show();

    }


    public void startAlarmRingTone(Ringtone r) {
        r.play();
    }

    public void stopAlarmRingTone(Ringtone r) {
        r.stop();
    }
}