package drs;

import org.apache.log4j.Logger;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

import Utility.LoadBalanceUtility;
import Utility.Util;

public class StartDRS implements Runnable{

	public static float drsUpperThresholdCpuUsage=70;
	String newHostIp=null;
	private static final Logger log = Logger.getLogger(StartDRS.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try{
			while(true){

				log.info("--------------Strting DRS Scan through Data Center-----------");

				for(int i=0; i<Util.hosts.length; i++){

					if(LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) >= drsUpperThresholdCpuUsage){


						log.info("Unbalance host found-- vHost : "+ Util.hosts[i].getName() +" CPU Usage : "+LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())));
						DRS(Util.getvHost(Util.hosts[i].getName()));

					}

				}

				Thread.sleep(120000);
			}
		}
		catch (Exception e){

			log.info("Exception in DRS thread : "+e);

		}

	}

	public void DRS(HostSystem unbalancedHost){

		VirtualMachine vm=null;
		
		int i=0;


		try{

			log.info("-------Starting Load Balancing-------");
			
            if(newHostIp==null){
            	
			newHostIp=LoadBalanceUtility.addNewVhost();

			Thread.sleep(60000);    // delay for new vHost to come up
            }
            
			VirtualMachine vms[]=unbalancedHost.getVms();

			do{

				if(Util.migrateToAnotherHost(vms[i],newHostIp))
				{
					Thread.sleep(20000);   // giving enough time to migrate


					i++;
				}
			} while(LoadBalanceUtility.getHostUsage(unbalancedHost) >= drsUpperThresholdCpuUsage);

			log.info(unbalancedHost.getName() + " balanced by DRS. its Current cpu usage : " + LoadBalanceUtility.getHostUsage(unbalancedHost));


		}
		catch(Exception e){

			log.info("Exception while load balancing : "+ e);

		}


	}

}
