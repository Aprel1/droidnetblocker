package com.droid.block;




import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class PlayStoreStartWidget extends AppWidgetProvider {
public static String MARKET_START_ACTION = "MarketStartAction";	


public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";

	@Override
	public void onReceive(final Context context, final Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
        	
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
			Intent intent1 = new Intent(context, PlayStoreWidgetConfigure.class);
			intent1.setFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
			context.startActivity(intent1);
			
 
        	}
       
        
	}
        
     


		@Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] ints) {
			
	        super.onUpdate(context, appWidgetManager, ints);
	        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
			Intent active = new Intent(context, PlayStoreStartWidget.class);
			active.setAction(ACTION_WIDGET_RECEIVER);
	
			PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		
			rv.setOnClickPendingIntent(R.id.button, actionPendingIntent);
	
			appWidgetManager.updateAppWidget(ints, rv);
			
	        }
}
