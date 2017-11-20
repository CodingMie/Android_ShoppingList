package com.example.young.shopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        updateViews.setOnClickPendingIntent(R.id.widget,  pi);
        ComponentName me = new ComponentName(context, NewAppWidget.class);
        appWidgetManager.updateAppWidget(me, updateViews);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle bundle = intent.getExtras();
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        if (intent.getAction().equals("WidgetBroadcast")) {
            String text = bundle.getString("name") + "仅售" + bundle.getString("price") + "!";
            updateViews.setTextViewText(R.id.appwidget_text, text);
            updateViews.setImageViewResource(R.id.appwidget_img, bundle.getInt("id"));
            Intent i = new Intent(context, Detail.class);
            i.putExtras(bundle);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widget,  pi);
            ComponentName me = new ComponentName(context, NewAppWidget.class);
            appWidgetManager.updateAppWidget(me, updateViews);
        }
    }
}

