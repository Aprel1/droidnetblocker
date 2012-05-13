package com.droid.block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

public class PlayStoreWidgetConfigure extends Activity {
	 

	private  static boolean reop=false;
	private static boolean backed = false;
	private boolean reopen = false;
	private String blocked;
	private static boolean waiting = false;
	private static boolean widget_run = false;
	
	private static boolean cl_home = false;
	private static boolean w_down = false;
	public static boolean exit = false;
	public static boolean widget = false;
	private static boolean run = false;
	
	

	@Override
	    public void onCreate(Bundle savedInstanceState) {
			widget_run=true;
	        super.onCreate(savedInstanceState);
	     
	        setContentView(R.layout.playstore);
	        blocked =  DroidNetBlockerActivity.loadSettings(this, 1);
	        
	      
	      
	        	startMarket(this);
	        
	        
  	        
	
	
	 }
	
	 
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
	    	   super.onActivityResult(requestCode, resultCode, data);
	    	   final String selected_uid[] = new String[0];
	    	   final Context ctx = this;
	    	 
	    	   if(DroidNetBlockerActivity.downloading>0)
	    	   {
	    		   backed=true;
	    		   reop = false;
	    		   exit = true;
	    		   w_down=true;
	    		   cl_home = true;
	    		   run=false;
	    		 
	    		  
	    	   }
	    	   
	    	   else
	    	   {
	    		   backed= false;
	        	   reopen = false;
	        	  
	        	   reop=false;
	        	  
	        	   
	               if(DroidNetBlockerActivity.blocked)
	               {
	            	   
	            	  
	            	   		cl_home= true;
	            	   		DroidNetBlockerActivity.blocked=false;
	            	   		DroidNetBlockerActivity.market = false;
	            	   		DroidNetBlockerActivity.runShell(this,0, selected_uid, false);
	            	   		Toast.makeText(ctx, "Market closed", Toast.LENGTH_LONG).show(); 
	            	   		DroidNetBlockerActivity.blocked=false;
	            	   		
	            	   
	            	   
	            	  
	               }
	               android.os.Process.killProcess(android.os.Process.myPid());
	    	   
	    	   }
	              
	               }
	               
	               
	              
	             
	 
	
	    
	 private void startMarket(Context ctx)
	    {
		 		
		 		
	    		final StringBuilder script = new StringBuilder();	
	    		cl_home=false;
	    		widget=true;
	    		w_down = false;
	    		exit=false;
	    		if(!run&& !DroidNetBlockerActivity.dnb_run)
	    		{
	    			
		    		final WaitingHomeThread whome = new WaitingHomeThread(ctx);
		    		whome.start();
	    		}
	    		try
	    		{
	    		int code;
	    		
	    		script.append("# RULES\n");
	    		
				Intent intent = new Intent(Intent.ACTION_VIEW); 
				intent.setData(Uri.parse("market://details?id=")); 
			
				
				
				startActivityForResult(intent, 1);
				
				
				script.append("iptables -F dnb_inp  \n");
				script.append("iptables -F dnb_out   \n");
				final StringBuilder res = new StringBuilder();
				code = DroidNetBlockerActivity.runScript(ctx, script.toString(), res);
			
				if ( code != 0) {
					String msg = res.toString();
					System.out.println("DroidNetBlocker     "+  msg);
				
				}
	    		
	    		}
	    		
	    		catch (Exception e) {
	    			System.out.println("ERROR Iptables!");
	    		
	    }
}
	
	 private static void waitForDownload(final Context ctx)
	   {
		
			
	   		final String[] selected_uid = new String[0];
	   		
	  	 	   
	  	 new Thread(new Runnable() {
	  		 
	  		  public void run() {
	  			while (DroidNetBlockerActivity.downloading>0 && !w_down)
	  			{
	  				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  			}
	  			
	  			if(!reop && !w_down)
	  			{
	  			
		  			
		  			DroidNetBlockerActivity.blocked=false;
			     	DroidNetBlockerActivity.market = false;
			     	DroidNetBlockerActivity.runShell(ctx,0, selected_uid, false);
			     	backed=false;
		     	
	  			}
		    
	  		  }  
	  		 }).start();
		 
		 
	  			  
	   }
		private static final class WaitingHomeThread extends Thread
		{
			private Context ctx;
			private Process proc;
			private boolean found;
			
			public WaitingHomeThread(Context context)
			{
				ctx = context;
				found = false;
			}
			
			  
			@Override
			public void run() {
				run = true;
				
				 proc = null;
				final String selected_uid[] = new String[0];
				try {
		    		
		    		
					proc = Runtime.getRuntime().exec( new String[] {"logcat" ,"-c"});
					proc = Runtime.getRuntime().exec( new String[] {"logcat"});
		    		
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					BufferedReader reader = null;
			    	reader = new BufferedReader(new InputStreamReader(
			    	proc.getInputStream() ));
			    	String line = null;
				try {
		    		
					while ((line = reader.readLine()) != null && !found && !cl_home){ 
						if(line.contains("Starting activity: Intent { act=android.intent.action.MAIN " +
								"cat=[android.intent.category.HOME] flg=0x10200000 cmp=com.android.launcher/.Launcher }")
								)
						{
							
							
							reop = false;
							final WaitingRestartThread whome = new WaitingRestartThread(ctx);
				    		whome.start();
				    		found = true;
				    		
							if(DroidNetBlockerActivity.downloading==0)
							{
								
								DroidNetBlockerActivity.blocked=false;
				            	DroidNetBlockerActivity.market = false;
				            	DroidNetBlockerActivity.runShell(ctx,0, selected_uid, false); 
				            
			            	
				    		
			            	
			            	
							}
							else
							{
								
								
								if(!waiting)
								{
									waiting = true;
									waitForDownload(ctx);

								}
								
								
								
							}
							destroy();
							
						}
						
						if(cl_home)
						{
							
							destroy();
							found = true;
						}
						
						if(line.contains("DownloadQueueImpl.startDownload: Download "))
						{
							
							DroidNetBlockerActivity.downloading++;
						
						}
						
						if(line.contains("New package installed")&&DroidNetBlockerActivity.downloading>0)
						{
							
							DroidNetBlockerActivity.downloading--;
							
						}
						if(line.contains(" InstallerTask.cancelCleanup: Cancel running installation")&&DroidNetBlockerActivity.downloading>0)
						{
							
							DroidNetBlockerActivity.downloading--;
							
						}
						
						
						
					}
					
				
				
			}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

			
				
				}
			}
			public synchronized void destroy() {
				if (proc != null) proc.destroy();
				proc = null;
			}
			
		}
		
		
		private static final class WaitingRestartThread extends Thread
		{
			
			private Process proc;
			private Context ctx;
			private boolean found; 
			
			public WaitingRestartThread(Context context)
			{
				ctx = context;
				found = false;
			}
		
			
			@Override
			public void run() {
				
				 proc = null;
				//final String selected_uid[] = new String[0];
				final StringBuilder script = new StringBuilder();	
				try {
		    		
		    		
					proc = Runtime.getRuntime().exec( new String[] {"logcat" ,"-c"});
					proc = Runtime.getRuntime().exec( new String[] {"logcat"});
		    		
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					BufferedReader reader = null;
			    	reader = new BufferedReader(new InputStreamReader(
			    	proc.getInputStream() ));
			    	String line = null;
				try {
		    		
					while ((line = reader.readLine()) != null && !found ){ 
						if(line.contains("Starting activity: Intent" +
								" { flg=0x10000000 cmp=com.droid.block/.PlayStoreWidgetConfigure") ||
								line.contains("Starting activity: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER]" +
										" flg=0x10200000 cmp=com.droid.block/.DroidNetBlockerActivity"))
						{
							reop = true;
							
							try
				    		{
					    		int code;
					    		
					    		script.append("# RULES\n");
								script.append("iptables -F dnb_inp  \n");
								script.append("iptables -F dnb_out   \n");
								final StringBuilder res = new StringBuilder();
								code = DroidNetBlockerActivity.runScript(ctx, script.toString(), res);
						
							if ( code != 0) {
								String msg = res.toString();
								System.out.println("DroidNetBlocker     "+  msg);
							
							}
				    		
				    		}
				    		
				    		catch (Exception e) {
				    			System.out.println("ERROR Iptables!");
				    		
				    }		
							
							final WaitingHomeThread whome = new WaitingHomeThread(ctx);
				    		whome.start();
				    		found=true;
			            	destroy();
			            
						}
						
						
					}
				
				
				
			}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

			
				
				}
			}
			public synchronized void destroy() {
				if (proc != null) proc.destroy();
				proc = null;
			}
			
		}

	     
	 
	 
}
