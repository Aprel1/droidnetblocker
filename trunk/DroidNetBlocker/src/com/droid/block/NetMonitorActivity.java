package com.droid.block;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.droid.block.ExpertViewActivity.AppAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class NetMonitorActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.netmon); 
	     
        final Context ctx = this;
        Button btn = (Button) findViewById(R.id.update); 
        btn.getBackground().setColorFilter(new LightingColorFilter(0xFF0FF2EA, 0xFF0B9A96));
        
        btn.setOnClickListener(
	           	new OnClickListener(){
	            		public void onClick(View v) {
	            		
	            		
	            			 	ListView InternalList = (ListView) findViewById(R.id.networklist);
	            			 	LinkedList<NetInfo> infoList;
	            			 	infoList = getNetInf();
	            	           			 	

	            			 	NetAdapter adapter = new NetAdapter (ctx, infoList) ;
	            			 	InternalList.setAdapter(adapter);
				          	    String KillMsg =" Network List updated ";
				          	    
				          	       Toast.makeText(getApplication(), KillMsg,
				            	   Toast.LENGTH_SHORT).show();
				            		}

	            	}
	         );
        
        ListView InternalList = (ListView) findViewById(R.id.networklist);
        LinkedList<NetInfo> infoList;
	 	infoList = getNetInf();
       			 	

	 	NetAdapter adapter = new NetAdapter (ctx, infoList) ;
	 
	    
	    InternalList.setAdapter(adapter);
	    	 

        }
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		 
		 
		 	menu.add(0, 0, 0, "Start View").setIcon(android.R.drawable.ic_menu_view);
	    	menu.add(0, 1, 0, "Expert View").setIcon(android.R.drawable.ic_menu_view);
	    	return true;
		 
		 
	 }
	 
	 @Override
	    public boolean onPrepareOptionsMenu(Menu menu) {
	    	
	    	final MenuItem item_start = menu.getItem(0);
	    	final MenuItem item_expert = menu.getItem(1);
	    	item_start.setIcon(android.R.drawable.ic_menu_view);
	    	item_start.setTitle("Start View");
	    	item_expert.setIcon(android.R.drawable.ic_menu_view);
	    	item_expert.setTitle("Expert View");
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
	    		startExpertView();
	    		return true;
	    	}
	    	return false;
	    }
	
	
	 private void startExpertView()
	    {
	    	Intent intent = new Intent();
	    	intent.setClass(this, ExpertViewActivity.class);
			startActivity(intent);
	    }
	
	private LinkedList <NetInfo> getNetInf()
	{
		 HashMap<Integer, String> infoMap;// LinkedList<NetInfo>infList = new LinkedList<NetInfo> ();
		 if( ExpertViewActivity.inst_apps.size()==0)
	        {
	        	ExpertViewActivity.getApps(this);
	        }
	        infoMap = ExpertViewActivity.inst_apps;
	    
		Process rootProc = null;
		try {
			rootProc = Runtime.getRuntime().exec(new String [] {"su" , "-c" , "chmod 777 /proc -R "});
			}
		catch (IOException e1) {
		
		System.out.println("CHMOD Failure!!!!!");
		e1.printStackTrace();
		}
		
		LinkedList<NetInfo> netList = new LinkedList<NetInfo>();
		
		String remIp = null;
		String locIp = null;
		String state =  null;
		try {
			
			
			FileInputStream fstream = new FileInputStream("/proc/"+android.os.Process.myPid() +"/net/tcp6");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String progName = null;
			int iUID = 0;
			int count1=0;
			
			while ((strLine = br.readLine()) != null)   {
				if(count1>0)
				{
					String ipRemHex = strLine.substring(68,76);
					String ipHex=strLine.substring(30, 38);
					String status = strLine.substring(82,84);
		     		String rport = strLine.substring(77,81);
		     		String lport = strLine.substring(39,43);
		     		String uid = strLine.substring(124,129);
		     		
		     		if( Integer.parseInt(lport,16)==0)
		     		{
		     			locIp = getNetMask(ipHex) + ": *" ;
		     		}
		     		else
		     		{
		     		locIp = getNetMask(ipHex) + ":" + Integer.parseInt(lport,16) ;
		     		}
		     		if( Integer.parseInt(rport,16)==0)
		     		{
		     			remIp = getNetMask(ipRemHex) + ": *" ;
		     		}
		     		else
		     		{
		     		remIp = getNetMask(ipRemHex) + ":" + Integer.parseInt(rport,16);
		     		}
		     		state = stTranslate(status);
		     		try
	     			{
	     				iUID = Integer.parseInt(uid);	
	     			}
	     			catch(Exception e1)
	     			{
	     				System.out.println("PARSE FAILURE");
	     			}

	     			if(iUID==0)
	     			{
	     				 progName = "no info";
	     			}
	     			else
	     			{
	     				
	     						
	     				progName = infoMap.get(iUID);
	     			
	     						
	     				}
		     		
		     		NetInfo info = new NetInfo(uid ,progName, state, locIp,remIp);
		     		netList.add(info);
		     		
		     		
		     		
					
					
				}
				 count1++;
					
					
				}
			
			
		
		
				}
		catch (FileNotFoundException e1) {
	    
			e1.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	
	
         return netList;
	
	
		
		
		
	}
	public String stTranslate (String state)
	{
		if(state.compareTo("01")==0)
		{
			return "ESTABLISHED";
		}
		else if(state.compareTo("02")==0)
		{
			return "SYN_SENT";
		}
		else if(state.compareTo("03")==0)
		{
			return "SYN_RECV";
		}
		
		else if(state.compareTo("04")==0)
		{
			return "FIN_WAIT1";
		}
		
		else if(state.compareTo("05")==0)
		{
			return "FIN_WAIT2";
		}
		
		else if(state.compareTo("06")==0)
		{
			return "TIME_WAIT";
		}
		
		else if(state.compareTo("07")==0)
		{
			return "CLOSE";
		}
		
		else if(state.compareTo("08")==0)
		{
			return "CLOSE_WAIT";
		}
		else if(state.compareTo("09")==0)
		{
			return "LAST_ACK";
		}
		
		else if(state.compareTo("0A")==0)
		{
			return "LISTEN";
		}
		
		else if(state.compareTo("0B")==0)
		{
			return "CLOSING";
		}
		else 
		{
			return "UNKNOWN";
		}
		
		}
    public String getNetMask ( String ip)
    {	int length = ip.length();
   
    	 try {
			   
		      StringBuffer netmaskString = new StringBuffer("");
		  
		     int p1 = length;
		     
		      for (int i = 0; i < length ; i+=2) {
		    	  p1= p1-2;
		         String subString=ip.substring(p1,length-i);
		         int subSet=Integer.parseInt(subString,16);
		       
		         netmaskString.append(subSet+".");	
		        
		         
		      }
		     
		      return netmaskString.toString().substring(0,netmaskString.length()-1);
		 
		   } catch (Exception e) {
		   
		      return null;
		   }
    	
    	
    }
    
	    public String [] getIPAr ( HashMap<String, List <NetInfo>> infoMap)
		{
			
			Set <String> ipss = (Set<String>) infoMap.keySet();
			String [] ipAr = new String [ipss.size()];
				
			Iterator iter = ipss.iterator();
			int a = 0;
			while (iter.hasNext())
				
			   {
			   	ipAr[a] = (String) iter.next();
			   	a++;
			   }
	   
			return ipAr;
		}
	 public class NetAdapter extends BaseAdapter
	    {	
	    	Context mContext;
	    	int mCount;
	    
	    	LinkedList<NetInfo> infoList;
	    	
	        public NetAdapter(Context context, LinkedList<NetInfo> info)
	        {
	        	
	           mContext = context;
	           infoList= info;
	           
	        }
	        
	        public int getCount() {
				return infoList.size();
			}

			public Object getItem(int position) {
				
				return position;
			}

			public long getItemId(int position) {
				
				return position;
			}
			
			

			public View getView(int position, View convertView, ViewGroup parent) {
				
				NetworkDetailView sv;
			
				
				sv = new NetworkDetailView(mContext, position , infoList);
				
				
				return sv;
			}
	    	
	    }
	 
	 public class NetworkDetailView extends TableLayout 
	    {	
		 	private TableRow ConnectionRow;
	    	private TextView IP;
	     	private TextView PID;
	    	private TextView Status;
	    	
	    	
			public NetworkDetailView(Context context, int position,  LinkedList <NetInfo> infoList ) {
				
				super(context);
				this.setColumnStretchable(1, true);
	            this.setOrientation(VERTICAL);
	           
	           
	            String st = null;
	           
				ConnectionRow = new TableRow(context);
				IP = new TextView(context);
				PID = new TextView(context);
				Status = new TextView(context);
				
				
				IP.setGravity(Gravity.LEFT);
				IP.setPadding(3, 3, 3, 3);
				IP.setWidth(170);
				
				PID.setGravity(Gravity.LEFT);
				PID.setPadding(3, 3, 3, 3);
				PID.setWidth(50);
				
				
				
				Status.setGravity(Gravity.LEFT);
				Status.setPadding(3, 3, 3, 3);
				Status.setWidth(110);

				addView(ConnectionRow);
				
				String pidText = null;
				String uid = null;
				
				IP.setText("Remote IP " +infoList.get(position).getRemIp()
						+"  "+ "Local IP "+ infoList.get(position).getlocIP());

				
						st = infoList.get(position).getStatus();

						uid =infoList.get(position).getUID();
					
						if((uid.compareTo(String.valueOf(DroidNetBlockerActivity.UID)))==0)
						{
							pidText = "UID: " + uid +"  Google Play Store" ;
						
						}
						else
						{
						pidText = "UID: " + uid +" "+  infoList.get(position).getProcName() ;
						
						}
				
					

	
				
				
				PID.setText(pidText );	
				Status.setText(st);
				
				ConnectionRow.addView(IP);
				ConnectionRow.addView(PID);
				ConnectionRow.addView(Status);
				
				if(position % 2 == 0)
		     		setBackgroundColor(0x803FC95F);
		     	else
		     		setBackgroundColor(0x80329A4A);
			
			
			}
			
		
			
		
	    }
		 
	   
	 
	  public class NetInfo
	   {
		   private String protocol;
		   private String locIp;
		   private String remIp;
		   private String uid;
		   private String procName;
		   private String status;
		   
		   public NetInfo (String uid , String procName, String status, String locIp, String remIP )
		   {
			 
			  this.remIp = remIP;
			   this.uid = uid;
			   this.procName = procName;
			   this.status = status;
			   this.locIp=locIp;
			   
		   }
		
		   public String getUID ()
		   {
			   return uid;
		   }
		   public String getProcName ()
		   {
			   return procName;
		   }
		   public String getStatus ()
		   {
			   return status;
		   }
		   public String getlocIP()
		   {
			   return locIp;
		   }
		   
		   public String getRemIp()
		   {
			   return remIp;
		   }
		   
		
	   }
	


}
