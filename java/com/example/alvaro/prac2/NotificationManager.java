package com.example.alvaro.prac2;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

public class NotificationManager extends AppCompatActivity {

    private final static String CHANNEL_ID = "HOLA";
    private final static String GROUP_ID = "HOLA";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        //CreateGroupNotificacion();

        NotificationManagerCompat notManager = NotificationManagerCompat.from(this);
        notManager.notify(0, CreateButtonNotificacion().build());
    }

    private NotificationCompat.Builder CreateButtonNotificacion(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("actionID", "actionOne");
        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icons8_notas_musicales_52)
                .setContentTitle("HOLA MUNDO!")
                .setContentText("bla bla bla")
                .addAction(R.drawable.icons8_notas_musicales_52, "ACTION", pending)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return builder;
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel", android.app.NotificationManager.IMPORTANCE_DEFAULT);

            android.app.NotificationManager notManager = getSystemService(android.app.NotificationManager.class);
            notManager.createNotificationChannel(channel);
        }
    }
}
