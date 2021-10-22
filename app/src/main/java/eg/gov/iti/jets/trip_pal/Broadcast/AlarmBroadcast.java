package eg.gov.iti.jets.trip_pal.Broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import eg.gov.iti.jets.trip_pal.AlertDialogActivity;
import eg.gov.iti.jets.trip_pal.MainActivity;
import eg.gov.iti.jets.trip_pal.R;
import eg.gov.iti.jets.trip_pal.database.TripEntity;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("date") != null) {

            Log.i("location", "onReceive: loc"+ intent.getStringExtra("location"));
            Intent alarmIntent = new Intent(context, AlertDialogActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            alarmIntent.putExtra("name", intent.getStringExtra("name"));
            alarmIntent.putExtra("location", intent.getStringExtra("location"));
            context.startActivity(alarmIntent);

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ intent.getStringExtra("location"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");


            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mapIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"alarmNotification")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alarm!!")
                    .setContentText("It's time to start "+ intent.getStringExtra("name"))
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_background, "let's start",
                            pendingIntent)
                    .addAction(R.drawable.ic_launcher_background, "cancel", null)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            notificationManagerCompat.notify(10,builder.build());

        }
    }
}
