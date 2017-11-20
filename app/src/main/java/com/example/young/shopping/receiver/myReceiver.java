package com.example.young.shopping.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.young.shopping.Detail;
import com.example.young.shopping.MainActivity;
import com.example.young.shopping.R;

import java.io.InputStream;

/**
 * Created by Young on 2017/10/27.
 */

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Bitmap pic = BitmapFactory.decodeByteArray(bundle.getByteArray("pic"), 0, bundle.getByteArray("pic").length);
        String message = bundle.getString("name")+"仅售"+bundle.getString("price");
        builder.setContentTitle("新商品热卖")
                .setTicker("您有一条新消息")
                .setContentText(message)
                .setSmallIcon(bundle.getInt("id"))
                .setAutoCancel(true)
                .setLargeIcon(pic);
        Intent mIntent = new Intent(context, Detail.class);
        bundle.remove("pic");
        mIntent.putExtras(bundle);
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(mPendingIntent);
        Notification notify = builder.build();
        manager.notify(0, notify);
    }
}