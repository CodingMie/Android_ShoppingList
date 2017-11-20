package com.example.young.shopping.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.young.shopping.Detail;
import com.example.young.shopping.MainActivity;
import com.example.young.shopping.NewAppWidget;
import com.example.young.shopping.R;

/**
 * Created by Young on 2017/10/27.
 */

public class DynamicReceiver extends BroadcastReceiver {
    @Override
    public void  onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("DynamicAction")) {
            Bundle bundle = intent.getExtras();
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder = new Notification.Builder(context);
            Bitmap pic = BitmapFactory.decodeByteArray(bundle.getByteArray("pic"), 0, bundle.getByteArray("pic").length);
            String message = bundle.getString("name")+"已添加到购物车";
            builder.setContentTitle("马上下单")
                    .setTicker("您有一条新消息")
                    .setContentText(message)
                    .setSmallIcon(bundle.getInt("id"))
                    .setAutoCancel(true)
                    .setLargeIcon(pic);
            Intent mIntent = new Intent(context, MainActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent,0);
            builder.setContentIntent(mPendingIntent);
            Notification notify = builder.build();
            manager.notify(0, notify);
        } else if (intent.getAction().equals("WidgetDynamic")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            Bundle bundle = intent.getExtras();
            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            String text = bundle.getString("name")+"已添加到购物车";
            updateViews.setTextViewText(R.id.appwidget_text, text);
            updateViews.setImageViewResource(R.id.appwidget_img, bundle.getInt("id"));
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widget,  pi);
            ComponentName me = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.updateAppWidget(me, updateViews);

        }
    }
}
