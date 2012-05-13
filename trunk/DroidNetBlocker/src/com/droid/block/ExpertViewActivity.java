package com.droid.block;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ExpertViewActivity extends Activity {
public static boolean all;
public static boolean unall;
private static boolean a_updated;
public static LinkedList <String> uidToBlock = new LinkedList <String>();
public static HashMap <Integer, String >inst_apps = new HashMap <Integer, String >();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert);
        final Context ctx = this;
        ListView InternalList = (ListView) findViewById(R.id.applist);
        

      
        
        Button btn = (Button) findViewById(R.id.blocapp); 
        btn.getBackground().setColorFilter(new LightingColorFilter(0xFF12D412, 0xFF07360B));
        btn.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            		
	            		
	            		if(all && !unall)
	            		{	
	            			if(inst_apps.size()==0)
	            			{
	            				getApps(ctx);
	            			}
	            			
	            			Iterator<Integer> It = inst_apps.keySet().iterator();
	            	           int i = 0;
	            	          Integer [] keys = new Integer [inst_apps.size()];
	            	           while (It.hasNext())
	            	           {
	            	        	   keys[i] = It.next();
	            	        	   i++;
	            	           }
	            	        
	            	           LinkedList <String> uids = new LinkedList <String> ();
	            	          
	            	          for(int j=0;j<keys.length;j++)
	            	          {
	            	        	  if(!(uids.contains(keys[j].toString())))
	            	        	  {
	            	        	  uids.add(keys[j].toString());
	            	        	  }
	            	          }
	            	           
	            	      
	            	          
	            	          saveRules(ctx, uids);
	            	          DroidNetBlockerActivity.loadSettings(ctx, 2);
	            	         // add rules to iptables
	            	         
	            	         
	            	         
	            			 String KillMsg ="Block all selected apps";
	            			 Toast.makeText(getApplication(), KillMsg,
	 	            									Toast.LENGTH_SHORT).show();
	 	          	       
	            		}
	            		else
	            		{
	            			if(unall)
	            			{	
	            				 	LinkedList <String> uids = new LinkedList<String>();
	            				 	uidToBlock.clear();
	            				 	saveRules(ctx, uids);
	            				 	DroidNetBlockerActivity.loadSettings(ctx, 2);
	            				 	String KillMsg ="All  apps are unblocked now";
	            				 	Toast.makeText(getApplication(), KillMsg,
	     	            									Toast.LENGTH_SHORT).show();
	            			}
	            			else
	            			{
	            				
			            		 
			            		 	saveRules(ctx,uidToBlock);
			          	        	String KillMsg ="Block selected apps";
			          	        	DroidNetBlockerActivity.loadSettings(ctx, 2);
			          	        	Toast.makeText(getApplication(), KillMsg,
			            									Toast.LENGTH_SHORT).show();
	            		}
	            		}
	            		}

	            	}
	         );
        
        Button btn2 = (Button) findViewById(R.id.selecfall); 
        btn2.getBackground().setColorFilter(new LightingColorFilter(0xFF12D412, 0xFF07360B));
        
        btn2.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            					unall= false;
	            					all=true;
	            					
	    	            			if(inst_apps.size()==0)
	    	            			{
	    	            			 getApps(ctx);
	    	            			}
	    	            		
		            		       AppAdapter adapter = new AppAdapter (ctx, inst_apps,true);
		            		       ListView InternalList = (ListView) findViewById(R.id.applist);
		            		       InternalList.setAdapter(adapter);
		            		     
				          	       String KillMsg ="Select all apps";
				          	       Toast.makeText(getApplication(), KillMsg,
				          	       Toast.LENGTH_SHORT).show();
				            		}

	            	}
	         );
        
        Button btn3 = (Button) findViewById(R.id.deselecfall); 
        btn3.getBackground().setColorFilter(new LightingColorFilter(0xFF12D412, 0xFF07360B));
        
        btn3.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            		
	            		
	            			if(inst_apps.size()==0)
	            			{
	            				getApps(ctx);
	            			}
	            			
		            		       AppAdapter adapter = new AppAdapter (ctx, inst_apps,false);
		            		       ListView InternalList = (ListView) findViewById(R.id.applist);
		            		       InternalList.setAdapter(adapter);
		            		       uidToBlock.clear();
				          	       String KillMsg ="Deselect all apps";
				          	       unall=true;
				          	       all=false;
				          	       Toast.makeText(getApplication(), KillMsg,
				            	   Toast.LENGTH_SHORT).show();
				            		}

	            	}
	         );
        
        Button btn4 = (Button) findViewById(R.id.update); 
        btn4.getBackground().setColorFilter(new LightingColorFilter(0xFF12D412, 0xFF07360B));
        
        btn4.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            		
	            		
	            				getApps(ctx);
	            			
		            		       AppAdapter adapter = new AppAdapter (ctx, inst_apps,false);
		            		       ListView InternalList = (ListView) findViewById(R.id.applist);
		            		       InternalList.setAdapter(adapter);
				          	       String KillMsg =" List of installed Apps updated";
				          	    
				          	       Toast.makeText(getApplication(), KillMsg,
				            	   Toast.LENGTH_SHORT).show();
				            		}

	            	}
	         );
        
       
		if(inst_apps.size()==0)
		{
			 getApps(ctx);
		}
	
       AppAdapter adapter = new AppAdapter (this, inst_apps,all);
       InternalList.setAdapter(adapter);
	} 
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		 
		 
		 	menu.add(0, 0, 0, "Start View").setIcon(android.R.drawable.ic_menu_view);
	    	menu.add(0, 1, 0, "Network Monitor").setIcon(android.R.drawable.ic_menu_view);
	    	return true;
		 
		 
	 }
	 
	 @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	    	
	    	final MenuItem item_start = menu.getItem(0);
	    	final MenuItem item_netmon = menu.getItem(1);
	    	item_start.setIcon(android.R.drawable.ic_menu_view);
	    	item_start.setTitle("Start View");
	    	item_netmon.setIcon(android.R.drawable.ic_menu_view);
	    	item_netmon.setTitle("Network Monitor");
	    	return super.onPrepareOptionsMenu(menu);
	    	
	    	
	    }
	 
	 @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    	
	    	switch (item.getItemId()) {
	    	case 0:
	    		finish();
	    		return true;
	    	case 1:
	    		finish();
	    		startNetMon();
	    		return true;
	    	}
	    	return false;
	    }
	

	 
	 private void startNetMon()
	    {
	    	Intent intent = new Intent();
	    	intent.setClass(this, NetMonitorActivity.class);
			startActivity(intent);
	    }
	 
	 /*
	  * This method saves finded uids of installed apps in preferences
	  */
	private static void saveUIDs (Context ctx)
	{
		
		final SharedPreferences prefs = ctx.getSharedPreferences(DroidNetBlockerActivity.PREF, 0);
    	final Editor edit = prefs.edit();
    	final StringBuilder uid = new StringBuilder();
    	
		for (int id:inst_apps.keySet())
		{
			if (uid.length() != 0)
    		{
    			uid.append('$');
    		}
    			uid.append(String.valueOf(id));
    			uid.append('§');
    			uid.append(inst_apps.get(id));
    			uid.append('§');
			}	
		
		edit.putString(DroidNetBlockerActivity.INST_APPS,uid.toString());
		edit.commit();
	}
	
	/*
	 * This method saved iptables rules in preferences
	 */
	private void saveRules(Context ctx, LinkedList <String> uids)
	
	{
		
		final SharedPreferences prefs = ctx.getSharedPreferences(DroidNetBlockerActivity.PREF, 0);
    	final Editor edit = prefs.edit();
    	final StringBuilder sel_uid = new StringBuilder();
    	
    	for(String uid:uids){
    		
    		
    		if (sel_uid.length() != 0)
    		{
    			sel_uid.append('$');
    		}
    			sel_uid.append(uid);
			}	
	edit.putString(DroidNetBlockerActivity.PREFS_UID,sel_uid.toString());
	
	if(all && !unall)
	{	
		
		String all1 = "true";
    	edit.putString(DroidNetBlockerActivity.PREFS_ALL, all1);

	}
	
	if(unall && !all)
	{
		String unall1 = "false";
    	edit.putString(DroidNetBlockerActivity.PREFS_ALL, unall1);
	}
	if(!unall && !all)
	{
		String unall1 = "ff";
    	edit.putString(DroidNetBlockerActivity.PREFS_ALL, unall1);
	}
	

	edit.commit();
	
}
	private void loadRules (Context ctx)
	{
		
		final SharedPreferences p = ctx.getSharedPreferences(DroidNetBlockerActivity.PREF, 0);
    	final String sUid = p.getString(DroidNetBlockerActivity.PREFS_UID, "");
    	String all1 = p.getString(DroidNetBlockerActivity.PREFS_ALL, "");
    	if(all1.compareTo("true")==0 )
		{ 
    		all = true;
    		unall=false;
		}
    	if(all1.compareTo("false")==0)
    	{
    		all = false;
    		unall=true;
    	}
	}
	
	
	protected static void getApps (Context ctx)
	{
		HashMap <Integer, String > appsInfo = new HashMap<Integer, String>();
		
		try {
			 PackageManager pkgman = ctx.getPackageManager();
			 final List<ApplicationInfo> apps = pkgman.getInstalledApplications(0);
			 boolean b= false;
			 int u=0;
				
			 for (final ApplicationInfo app : apps) {
				b= (PackageManager.PERMISSION_GRANTED == pkgman.checkPermission(Manifest.permission.INTERNET, app.packageName));
			
				if(!(appsInfo.containsKey(app.uid)) && b );
				
				{
					if(app.uid != DroidNetBlockerActivity.UID)
					{
					
					u++;
					appsInfo.put(app.uid, pkgman.getApplicationLabel(app).toString());
					}
					
				}
			 }

			}
			catch (Exception e) {
				System.out.println("Failure getApps");
				}
		a_updated = true;
		inst_apps = appsInfo;
		saveUIDs(ctx);

	}
	
	
	
	
	public class AppAdapter extends BaseAdapter
    {
		private Context mContext;
		private HashMap <Integer, String> aMap;
		private int length;
		private Integer [] keys;
		private boolean m_check;
		public AppAdapter(Context context, HashMap<Integer, String>appMap, boolean checked)
        {
        
		   m_check =checked;
           mContext = context;
           aMap = appMap;
           length = aMap.size();
           Iterator<Integer> It = appMap.keySet().iterator();
           int i = 0;
           keys = new Integer [length];
           while (It.hasNext())
           {
        	   keys[i] = It.next();
        	   i++;
           }
           java.util.Arrays.sort( keys );
        }
		public int getCount() {
			return length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			AppBlockView sv;
			
			sv = new AppBlockView (mContext, position , aMap, keys,m_check);
			
			
			return sv;
		}	
		
    }
	public static class AppBlockView extends TableLayout 
	{
		private TableRow ConnectionRow;
		private TextView APP;
		private TextView UID;
		private CheckBox cb;
		
		public AppBlockView(final Context context, int position, HashMap <Integer, String> appMap, Integer[] keys, boolean checked) {
			super(context);
			
			
			this.setColumnStretchable(1, true);
            this.setOrientation(VERTICAL);
            ConnectionRow = new TableRow(context);
            final String uid1 = keys[position].toString();
           
            UID = new TextView(context);
            UID.setGravity(Gravity.LEFT);
            UID.setPadding(3, 3, 3, 3);
            UID.setWidth(100);
            UID.setText(keys[position].toString() );
			
			APP = new TextView(context);
			APP.setGravity(Gravity.LEFT);
			APP.setPadding(3, 3, 3, 3);
			APP.setWidth(100);
			APP.setText(appMap.get(keys[position]));
			
			cb=new CheckBox(context);
			if(checked)
			{
				cb.setChecked(true);
				
				
			}
		
			if(uidToBlock.contains(uid1) )
				{ 
					
					cb.setChecked(true);
				}
				
			cb.setOnCheckedChangeListener (new OnCheckedChangeListener()
			{
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			    {
			    	
			        if ( isChecked )
			        {
			        	if(!(uidToBlock.contains(uid1)))
			        	{
			        	
			        		uidToBlock.add(uid1);
			        		
			        	
			        	}
			        	if(unall==true || all ==true)
			        	{
			        		unall = false;
			        		all= false;
			        	}

			        }
			        else
			        {
			        	uidToBlock.remove(uid1);
			        	
			        	
			        	if(unall==true || all ==true)
			        	{
			        		
			        		unall = false;
			        		all= false;
			        	}
			        }

			    }
			});
				
			
			cb.setGravity(Gravity.LEFT);
			cb.setPadding(3, 3, 3, 3);
			cb.setWidth(100);
			cb.setEnabled(true);
			
			addView(ConnectionRow);
			ConnectionRow.addView(UID);
			ConnectionRow.addView(APP);
			ConnectionRow.addView(cb);
			
			if(position % 2 == 0)
	     		setBackgroundColor(0x800404B4);
	     	else
	     		setBackgroundColor(0x802E2EFE);
		}
		
	

		
	}
	
}