package com.droid.block;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.LightingColorFilter;



public class DroidNetBlockerActivity extends Activity {
	private static final String SCRIPT_FILE = "droidnetblocker.sh";
	public static final String PREFS_BUTTON 	= "But_Pressed";
	public static final String PREFS_ALL 	= "SelAll_Pressed";
	public static final String PREFS_UID 	= "PREF_UID";
	public static final String GAPPS_UID 	= "GAPPS_UID";
	public static final String INST_APPS 	= "INST_APPS";
	public static final String WAIT_TIME 	= "WAIT_TIME";
	public static final String PREF 	= "DNBPreff";
	
	public static boolean blocked= false;
	public static boolean market = false;
	public static boolean backed = false;
	
	public static boolean widget_run = false;
	public static boolean widget_rest = false;
	public static boolean dnb_run=false;
	public static boolean reop=false;
	public static boolean exit = false;

	public static int downloading = 0;

	public static boolean cl_home = false;
	public static boolean w_down = false;
	private static boolean waiting=false;
	private static boolean run = false;
	public static int UID;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      
        cl_home = false;
      
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
        
        
        Button btn = (Button) findViewById(R.id.rbutton); 
        Button btn1 = (Button) findViewById(R.id.rbutton1); 
        Button btn2 = (Button) findViewById(R.id.rbutton2); 
        final Context ctx = this;
        TextView tv = (TextView)findViewById(R.id.is_block);
        if(blocked)
        {
        	
  	        tv.setText(String.valueOf("Gtalkservice is blocked"));
        }
        if(!blocked)
        {
        	 tv.setText(String.valueOf("Gtalkservice is not blocked"));
        }
        btn.getBackground().setColorFilter(new LightingColorFilter(0xFF12D412, 0xFF07360B));
        btn1.getBackground().setColorFilter(new LightingColorFilter(0xFFF50909, 0xFF460303));
        btn2.getBackground().setColorFilter(new LightingColorFilter(0xFF08A1FA, 0xFF073357));
        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        if(UID==0)
        {
        List<ApplicationInfo> packages = pm.getInstalledApplications(
                PackageManager.GET_META_DATA);
     
        loadSettings(ctx, 1);
        
        
        if(blocked)
        {
        	
  	        tv.setText(String.valueOf("Gtalkservice is blocked"));
        }
        if(!blocked)
        {
        	 tv.setText(String.valueOf("Gtalkservice is not blocked"));
        }
        //loop through the list of installed packages and see if the selected
        //app is in the list
        for (ApplicationInfo packageInfo : packages) {
        
        	
            if(packageInfo.packageName.equals("com.android.vending")){
               
                UID = packageInfo.uid;
             

            }
        }
        }
        btn.setOnClickListener(
	           	new OnClickListener(){
	           		
	            		public void onClick(View v) {
	            			String selected_uid[] = new String[0];
	            			
	            			runShell (ctx, 0,selected_uid, true);
	          	        // Display message
	            		
	          	        String msg ="GTalk Service blocked";
	          	        TextView tv = (TextView)findViewById(R.id.is_block);
	          	        tv.setText(String.valueOf("Gtalkservice is blocked"));
	          	        Toast.makeText(getApplication(), msg,
	            									Toast.LENGTH_SHORT).show();
	            		}

	            	}
	         );
        btn1.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            			String selected_uid[] = new String[0];
	            			runShell (ctx, 1, selected_uid,false);
	          	        // Display message
	          	        String msg =" GTalk Service unblocked";
	          	        TextView tv = (TextView)findViewById(R.id.is_block);
	          	        tv.setText(String.valueOf("Gtalkservice is not blocked"));
	          	        Toast.makeText(getApplication(), msg,
	            									Toast.LENGTH_SHORT).show();
	              		}

            	}
         );
        
        btn2.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            			
	            				startMarket(ctx);
	            			
	          	        // Display message
	            			market = true;
	          	        String msg ="Play Store started";
	          	        Toast.makeText(getApplication(), msg,
	            									Toast.LENGTH_SHORT).show();
	              		}

            	}
         );
     
    }
  
    /* Load Settings
     * b=0 --> settings loaded by reboot and iptables rules must be setted
     * b=1 --> "normal" loading, if app started
     * b=2 --> load list of installed app for expert view
     * 
     */
    public static String loadSettings(Context ctx, int b)
    {
    	
    	final SharedPreferences p = ctx.getSharedPreferences(DroidNetBlockerActivity.PREF, 0);
    	
    	
    	
    	final String iApp = p.getString(INST_APPS, "");
    	HashMap <Integer, String >inst_apps = new HashMap <Integer, String >();
    	if (iApp.length() > 0) {
    		
    		final StringTokenizer tk = new StringTokenizer(iApp, "$");
    		int tsize = tk.countTokens();
    		
    		for (int w=0; w<tsize; w++) {
    			
    	
    			final String inf = tk.nextToken();
    			final StringTokenizer tk1 = new StringTokenizer(inf, "§");
    			
    			
    			final String uid11 = tk1.nextToken();
    					
    			
    			final String pName = tk1.nextToken();
    				
    			
    		
    			
    			inst_apps.put(Integer.parseInt(uid11),pName );
    		}
    		
    		
    	}
    	if(inst_apps.size()>0)
    	{
    		
    		
    		ExpertViewActivity.inst_apps = inst_apps;
    	}
    	
    	
    	
    	
    	
    	
      	String gUID = p.getString(GAPPS_UID, "");
		
		if(gUID.compareTo("")!=0)
		{
			
		UID = Integer.parseInt(gUID);
		
		}
		
		
		final String sUid = p.getString(PREFS_UID, "");
    	String selected_uid[] = new String[0];
    	
    	if (sUid.length() > 0) {
    		final StringTokenizer t = new StringTokenizer(sUid, "$");
    		selected_uid = new String[t.countTokens()];
    		
    		for (int q=0; q<selected_uid.length; q++) {
				final String uid = t.nextToken();
				
						
					selected_uid[q] = uid;
					if(!(ExpertViewActivity.uidToBlock.contains(uid)))
					{
					ExpertViewActivity.uidToBlock.add(uid);
					}

		
			}
    		
    	}
    	
    
    	String all1 = p.getString(PREFS_ALL, "");
    	
    	if(all1.compareTo("true")==0 )
		{ 
    		ExpertViewActivity.all = true;
    		ExpertViewActivity.unall=false;
		}
    	if(all1.compareTo("false")==0)
    	{
    		ExpertViewActivity.all = false;
    		ExpertViewActivity.unall=true;
    	}
    	
    	if(all1.compareTo("ff")==0)
    	{
    		ExpertViewActivity.all = false;
    		ExpertViewActivity.unall=false;
    	}
    	
	    String bld = p.getString(PREFS_BUTTON, "");
	  
			if(bld.compareTo("true")==0 )
			{ 
				
				blocked=true;
				
			
				if(b==0)
				{
					
					runShell (ctx, 2, selected_uid, true);
				
				}
				
				
			}
			
			if(bld.compareTo("false")==0)
			{
				//flush
				blocked = false;
				
				if(b==0)
				{
					
					runShell (ctx, 2, selected_uid, false);
				
				}
				else
				{
					runShell (ctx, 1, selected_uid, false);
				}
			}
			if(bld.compareTo("")==0)
			{ 	
				// nothing saved --> flush
				blocked = false;
				
				runShell (ctx, 1, selected_uid, false);
				
			}
			
			
			if(b==2)
			{//uids --> expertview
				runShell (ctx, 3, selected_uid, false);
				
			}
			
			
			
	return bld;
    }
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   
    	   super.onActivityResult(requestCode, resultCode, data);
    	   final String selected_uid[] = new String[0];
    	   final Context ctx = this;
    	
    	   if(downloading>0)
    	   {
    		   backed=true;
    		   exit = true;
    		   w_down = true;
    		   run=false;
    		   cl_home= true;
    		   reop = false;
    		   
    	   }
    	   
    	   else
    	   {
    		   backed= false;
        	   reop=false;
        	  
               if(blocked)
               {
            	    cl_home= true;
            	   	blocked=false;
	            	market = false;
	            	runShell(this,0, selected_uid, false);
	                Toast.makeText(this, "Market closed", Toast.LENGTH_LONG).show(); blocked=false;
	            	
            	   
            	   
            	  
               }
    	   
    	   }
              
               }
               
               
    
    
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, 0, 0, "Expert View").setIcon(android.R.drawable.ic_menu_view);
    	menu.add(0, 1, 0, "Network Monitor").setIcon(android.R.drawable.ic_menu_view);
    	menu.add(0, 2, 0, "Exit").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    
    	return true;
    	
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	
    	final MenuItem item_expert = menu.getItem(0);
    	final MenuItem item_netmon = menu.getItem(1);
    	final MenuItem item_exit = menu.getItem(2);
    	item_expert.setIcon(android.R.drawable.ic_menu_view);
    	item_expert.setTitle("Expert View");
    	item_netmon.setIcon(android.R.drawable.ic_menu_view);
    	item_netmon.setTitle("Network Monitor");
    	item_exit.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
    	item_exit.setTitle("Exit");
    	return super.onPrepareOptionsMenu(menu);
    	
    	
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	switch (item.getItemId()) {
    	case 0:
    		startExpertView();
    		return true;
    		
    	case 1:
    		startNetMon();
    		return true;
    		
    	
    	case 2:
    		if(downloading==0)
    		{
    			android.os.Process.killProcess(android.os.Process.myPid());
    			return true;
    		}
    		else
    		{	
    			 reop = false;
    			 w_down = true;
    			 cl_home= true;
    			 exit = true;
    			 Intent i = new Intent(); 
    	         i.setAction(Intent.ACTION_MAIN); 
    	         i.addCategory(Intent.CATEGORY_HOME); 
    	         this.startActivity(i);
    		}
    		
    	}
    	return false;		
    }
    
    
    
    private void startExpertView()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, ExpertViewActivity.class);
		startActivity(intent);
    }
    
    private void startNetMon()
    {
    	Intent intent = new Intent();
    	intent.setClass(this, NetMonitorActivity.class);
		startActivity(intent);
    }
    
    
    
   
    
    private void startMarket(Context ctx)
    {
	 		
	 		
    		final StringBuilder script = new StringBuilder();	
    		cl_home=false;
    		dnb_run=true;
    		w_down = false;
    		exit=false;
    		if(!run)
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
			//DroidNetBlockerActivity.saveRules(ctx);
			
			
			startActivityForResult(intent, 1);
			
			
			script.append("iptables -F dnb_inp  \n");
			script.append("iptables -F dnb_out   \n");
			final StringBuilder res = new StringBuilder();
			code = runScript(ctx, script.toString(), res);
		
			if ( code != 0) {
				String msg = res.toString();
				System.out.println("DroidNetBlocker     "+  msg);
			
			}
    		
    		}
    		
    		catch (Exception e) {
    			System.out.println("ERROR Iptables!");
    		}
    		
    }
  
    
	
    public static void runShell(Context ctx, int btn, String [] uids, boolean gtalk) {
    	
    	final StringBuilder script = new StringBuilder();	
    	
    	try 
    	
		{
			int code;

			script.append("# RULES\n");
			script.append("" +
					"# Create  chains if necessary\n" +
					"iptables -L dnb_inp >/dev/null 2>/dev/null || iptables --new dnb_inp || exit \n" +
					"iptables -L dnb_inp_gtalk >/dev/null 2>/dev/null || iptables --new dnb_inp_gtalk || exit \n" +
					"iptables -L dnb_out >/dev/null 2>/dev/null || iptables --new dnb_out || exit \n" +
					"iptables -L dnb_out_gtalk >/dev/null 2>/dev/null || iptables --new dnb_out_gtalk || exit \n" +
					"iptables -L dnb_exp >/dev/null 2>/dev/null || iptables --new dnb_exp || exit \n" +
					"iptables -L INPUT | grep -q dnb_inp || iptables -A INPUT -j dnb_inp || exit \n" +
					"iptables -L INPUT | grep -q dnb_inp_gtalk || iptables -A INPUT -j dnb_inp_gtalk || exit \n" +
					"iptables -L OUTPUT | grep -q dnb_out || iptables -A OUTPUT -j dnb_out || exit \n" +
					"iptables -L OUTPUT | grep -q dnb_out_gtalk || iptables -A OUTPUT -j dnb_out_gtalk || exit \n" +
					"iptables -L OUTPUT | grep -q dnb_exp || iptables -A OUTPUT -j dnb_exp || exit \n");
			
			
					
			if(btn == 0 && !blocked)
			{
				if(gtalk)
				{
				
				script.append("iptables  -A dnb_inp -m state --state ESTABLISHED,RELATED -j ACCEPT").append(" || exit\n");
				script.append("iptables  -A dnb_inp -j DROP").append(" || exit\n");
			
				script.append("iptables  -A dnb_out -m owner --uid-owner ").append(UID).append("  -j DROP").append(" || exit\n");
				script.append("iptables  -A dnb_out_gtalk -p tcp --dport 5228 -j DROP").append(" || exit\n");
				script.append("iptables  -A dnb_inp_gtalk -p tcp --sport 5228 -j DROP").append(" || exit\n");
				
				blocked= true;
				saveRules(ctx);
				}
				else
				{
					script.append("iptables  -A dnb_inp -m state --state ESTABLISHED,RELATED -j ACCEPT").append(" || exit\n");
					script.append("iptables  -A dnb_inp -j DROP").append(" || exit\n");
				
					script.append("iptables  -A dnb_out -m owner --uid-owner ").append(UID).append("  -j DROP").append(" || exit\n");
					blocked= true;
					saveRules(ctx);
				}
			
			}
			
				if(btn==1)
				{
				
				script.append("iptables -F dnb_out \n");
				script.append("iptables -F dnb_inp \n");
				script.append("iptables -F dnb_out_gtalk \n");
				script.append("iptables -F dnb_inp_gtalk \n");
				
				blocked= false;
				saveRules(ctx);
			
				}
				if(btn==2)
				{
					//widget 
					
					if(blocked)
					{
					script.append("iptables  -A dnb_inp -m state --state ESTABLISHED,RELATED -j ACCEPT").append(" || exit\n");
					script.append("iptables  -A dnb_inp -j DROP").append(" || exit\n");
				
					script.append("iptables  -A dnb_out -m owner --uid-owner ").append(UID).append("  -j DROP").append(" || exit\n");
					
					}
					script.append("iptables  -F dnb_exp " ).append(" || exit\n");
					
					for(int l=0; l<uids.length; l++)
					{	
						
						String uid2= uids[l];

						script.append("iptables  -A dnb_exp -m owner --uid-owner ").append(uid2).append("  -j DROP").append(" || exit\n");
		
				}	
					
				}
				if(btn==3)
				{
					
						
						
						//delete all "old" rules
						script.append("iptables  -F dnb_exp " ).append(" || exit\n");
					
						for(int l=0; l<uids.length; l++)
						{	
							
							String uid2= uids[l];
							
							
						
							
							script.append("iptables  -A dnb_exp -m owner --uid-owner ").append(uid2).append("  -j DROP").append(" || exit\n");
			
					}
				}
				
				
				
				
			
			final StringBuilder res = new StringBuilder();
			code = runScript(ctx, script.toString(), res);
			
			if ( code != 0) {
				String msg = res.toString();
				System.out.println("DroidNetBlocker     "+  msg);
			
			}
		}
		catch (Exception e) {
		System.out.println("ERROR Iptables!");
		}
		
	}
    
    public static int runScript(Context ctx, String script, StringBuilder res) {
		
		final File file = new File(ctx.getDir("bin",0), SCRIPT_FILE);
		int timeout = 30000;
		final ScriptRunner runner = new ScriptRunner(file, script, res);
		runner.start();
		try {
			if (timeout > 0) {
				runner.join(timeout);
			} else {
				runner.join();
			}
			if (runner.isAlive()) {
				runner.interrupt();
				runner.join(150);
				runner.destroy();
				runner.join(50);
			}
		} catch (InterruptedException ex) {}
		return runner.exitcode;
		
		
	}

    public static void saveRules(Context ctx) {
    	final SharedPreferences prefs = ctx.getSharedPreferences(PREF, 0);
    	final Editor edit = prefs.edit();
    	String a= "false";

    	if(blocked)
    	{
    		a= "true";
    	}
    	edit.putString(PREFS_BUTTON, a);
    	if(UID!=0)
    	{
    		edit.putString(GAPPS_UID, String.valueOf(UID));
    	}
    	edit.commit();
    	
    	
    	
    	
    }
    /*
     * WaitingHomeThread reads log messages and waited for pressing of home button ---> then it starts WaitingRestartThread
     * 
     */
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
							"cat=[android.intent.category.HOME] flg=0x10200000 cmp=com.android.launcher/.Launcher }"))
					{
						
						
						reop = false;
						final WaitingRestartThread whome = new WaitingRestartThread(ctx);
			    		whome.start();
			    		found = true;
			    		
						if(downloading==0)
						{
							
						blocked=false;
		            	market = false;
		            	runShell(ctx,0, selected_uid, false); 
		            
		            	
			    		
		            	
		            	
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
						downloading++;
						
					}
					
					if(line.contains("New package installed")&&downloading>0)
					{
						
						downloading--;
						
					}
					if(line.contains(" InstallerTask.cancelCleanup: Cancel running installation")&&downloading>0)
					{
						
						
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
	
	
	/*
	 * WaitingRestartThread reads log messages and waited for re-open from DroidNetBlocker
	 */
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
					if(line.contains("Starting activity: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER]" +
							" flg=0x10200000 cmp=com.droid.block/.DroidNetBlockerActivity") ||
							line.contains("Starting activity: Intent" +
									" { flg=0x10000000 cmp=com.droid.block/.PlayStoreWidgetConfigure"))
					{
						reop = true;
						
						try
			    		{
				    		int code;
				    		
				    		script.append("# RULES\n");
							script.append("iptables -F dnb_inp  \n");
							script.append("iptables -F dnb_out   \n");
							final StringBuilder res = new StringBuilder();
							code = runScript(ctx, script.toString(), res);
					
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
	
	/*
	 * This thread wait until all downloads are finished. 
	 */
	  public static void waitForDownload(final Context ctx)
	   {
		   
		   
		
			   
	
	   	final String[] selected_uid = new String[0];
	   		
	   		new Thread(new Runnable() {
	  		 
	  		  public void run() {
		  			
		  	 	   
	  			while (downloading>0 && !w_down)
	  			{
	  				
	  				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  			}
	  			
	  			
	  			
	  		
	  			if(!reop && !w_down)
				{// if homescreen
	  				
		  				
		  			blocked=false;
		  			market = false;
			     	runShell(ctx,0, selected_uid, false);
			     	backed=false;
	  				}
				
	  		
	  			
	  		  
	  		  }  
	  		 }).start();
			 }	  

    private static final class ScriptRunner extends Thread {
    	
	    private final File file;
		private final String script;
		private final StringBuilder sb;
		public int exitcode = -1;
		private Process pr;
		
		public ScriptRunner(File file, String script, StringBuilder sb) {
			this.file = file;
			this.script = script;
			this.sb = sb;
		}
		
		@Override
		public void run() {
			try {
				
				file.createNewFile();
				
				final String abspath = file.getAbsolutePath();
				Runtime.getRuntime().exec("chmod 777 "+abspath).waitFor();
				
				final OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
				
				if (new File("/system/bin/sh").exists()) 
				{
					out.write("#!/system/bin/sh\n");
				}
				out.write(script);
				
				if (!script.endsWith("\n")) out.write("\n");
				
				out.write("exit\n");
				
				out.flush();
				out.close();
				
				pr = Runtime.getRuntime().exec("su -c "+abspath);
				
				final InputStream stdout = pr.getInputStream();
				
				final InputStream stderr = pr.getErrorStream();
				
				final byte buf[] = new byte[8192];
				int read = 0;
				
				while (true) {
					final Process localexec = pr;
					if (localexec == null) break;
					try 
					{
					
						this.exitcode = localexec.exitValue();
					} catch (IllegalThreadStateException ex) {
						
					}
					
					if (stdout.available() > 0) {
						read = stdout.read(buf);
						if (sb != null) sb.append(new String(buf, 0, read));
					}
			
					if (stderr.available() > 0) {
						read = stderr.read(buf);
						if (sb != null) sb.append(new String(buf, 0, read));
					}
					if (this.exitcode != -1) {
						break;
					}
				
					Thread.sleep(50);
				}
			} catch (InterruptedException ex) {
				if (sb != null) sb.append("\nTimeOut");
			} catch (Exception ex) {
				if (sb != null) sb.append("\n" + ex);
			} finally {
				destroy();
			}
		}
		
		public synchronized void destroy() {
			if (pr != null) pr.destroy();
			pr = null;
		}
   }
    
    
   
}