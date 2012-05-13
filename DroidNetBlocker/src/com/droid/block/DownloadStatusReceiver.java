package com.droid.block;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DownloadStatusReceiver extends BroadcastReceiver {
	public static boolean installed;
	 public void onReceive(Context context, Intent intent) {
		 	
		 if(DroidNetBlockerActivity.downloading>0)
		 {
		 	  DroidNetBlockerActivity.downloading--;
		 
		 	  if(DroidNetBlockerActivity.downloading==0)
		 	  {
		 		  
		 		  if(DroidNetBlockerActivity.exit || PlayStoreWidgetConfigure.exit)
		 		  {
		 			  
		 				final String[] selected_uid = new String[0];
		 				DroidNetBlockerActivity.exit =false;
		 			  	DroidNetBlockerActivity.blocked=false;
			  			DroidNetBlockerActivity.market = false;
			  			
				     	DroidNetBlockerActivity.runShell(context,0, selected_uid, false);
				     	if(PlayStoreWidgetConfigure.widget)
				     	{
				     		PlayStoreWidgetConfigure.widget= false;
				     		android.os.Process.killProcess(android.os.Process.myPid());
				     	}
				     	
		 		  }
		 	  }
			 
}	}
}
