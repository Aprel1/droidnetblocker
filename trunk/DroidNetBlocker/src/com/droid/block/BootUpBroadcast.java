package com.droid.block;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class BootUpBroadcast extends BroadcastReceiver{

 
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
	
		 
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
				// Start a new thread to enable the firewall
		
						String a=DroidNetBlockerActivity.loadSettings(context, 0);
						android.os.Process.killProcess(android.os.Process.myPid());

				
			}
		}
	}

