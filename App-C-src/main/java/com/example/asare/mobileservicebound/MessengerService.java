package com.example.asare.mobileservicebound;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MessengerService extends Service {

    static final int JOB1 = 1;
    static final int JOB2 = 2;
    static final int JOB1_RESPONSE = 3;
    static final int JOB2_RESPONSE = 4;
    final String TAG = "IncomingHandler";

 private class IncomingHandler extends Handler{
        Message messageResponse;
        String messageSent;
        Bundle bundle = new Bundle();

        @Override
        public void handleMessage(Message msg){

            Log.d(TAG,"handleMessage");

            switch (msg.what){
                case JOB1:
                    Log.d(TAG,"JOB_1");
                    messageSent = "This is the first message from the service";
                    messageResponse = android.os.Message.obtain(null,JOB1_RESPONSE);
                    bundle.putString("response_message", messageSent);
                    messageResponse.setData(bundle);
                    try {
                        msg.replyTo.send(messageResponse);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                case JOB2:
                    Log.d(TAG,"JOB_2");
                    messageSent = "This is the second message from the service";
                    messageResponse = android.os.Message.obtain(null,JOB2_RESPONSE);
                    bundle.putString("response_message",messageSent);
                    messageResponse.setData(bundle);
                    try {
                        msg.replyTo.send(messageResponse);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;

                    default:
                        super.handleMessage(msg);
                        Log.d(TAG,"DEFAULT");
            }
        }
    }

    private Messenger mServiceMessenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service onBind");
        Toast.makeText(this, "Binding...", Toast.LENGTH_SHORT).show();
       return mServiceMessenger.getBinder();
    }
}
