package com.example.asare.localboundservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;

import com.example.asare.localboundservice.MyService.MyLocalBinder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyService myService;
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Intent intent = new Intent(this, MyService.class);
            startService(intent);
            sendNotification();

    }

    private void sendNotification() {
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.weather);
        builder.setTicker("Service Running");
        builder.setTicker("Weather Description");
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
     //   builder.setWhen(System.currentTimeMillis());
     //   builder.setOngoing(true);
        Intent startIntent =  new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, notification);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Toast.makeText(this, "Binding...", Toast.LENGTH_SHORT).show();
        if (!isBound) {
            Intent myIntent = new Intent(this, MyService.class);
            bindService(myIntent, Connection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unbinding from LocalService
        Toast.makeText(this, "Unbinding...", Toast.LENGTH_SHORT).show();
        unbindService(Connection);
        isBound = false;
    }

    public void getDescription(View view){
        List<weatherList> weather = myService.getWeather();

        ArrayAdapter myAdapter = new CustomAdapter(this, weather);
        //   ArrayAdapter myArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, weather);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(myAdapter);
    }

    private ServiceConnection Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyLocalBinder binder = (MyLocalBinder) iBinder;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
}
