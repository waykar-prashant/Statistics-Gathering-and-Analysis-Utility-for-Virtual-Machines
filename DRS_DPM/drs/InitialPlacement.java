package drs;



import org.apache.log4j.Logger;

import Utility.LoadBalanceUtility;
import Utility.Util;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.Task;

public class InitialPlacement implements Runnable{
	private static final Logger log = Logger.getLogger(InitialPlacement.class);

	String selectedHost=null;
	float  selectedCpuUsage;
	float upperThresholdCpuUsage=70;
	private static int count =1;

	@Override
	public void run() {
		// TODO Auto-generated method stub



		log.info("-------Finding vHost with minimum CPU usage---------");

		for(int i=0; i<Util.hosts.length-1; i++)
			
		{
			//System.out.println(Util.hosts[i].getName() +" : "+LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) );
			//System.out.println(Util.hosts[i+1].getName() +" : "+LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i+1].getName())) );
			
			if(LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) < LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i+1].getName())))
			{
				if(LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) < upperThresholdCpuUsage)
				{
					selectedCpuUsage=LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName()));
					selectedHost=Util.hosts[i].getName();
				}
			}
			else
			{
				if(LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i+1].getName())) < upperThresholdCpuUsage)
				{
					selectedCpuUsage=LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i+1].getName()));
					selectedHost=Util.hosts[i+1].getName();
				}

				else
				{

					log.info("vHOST with minumum usage not found, cant add VM now.");
				}
			}	
			

		}
		
		log.info("Selected Host : "+ selectedHost+"   "+ " CPU Usage : " + selectedCpuUsage);


		// Now will add the new VM to the selected vHost

		try{


			com.vmware.vim25.mo.VirtualMachine vm = Util.getvHost(selectedHost).getVms()[0];
			
			

			log.info("VM: "+vm.getName() +"   will be cloned and added to the  vHost : "+ selectedHost);

			// need to write logic to take snapshot of the vHost before adding the VM
			Util.snapshot(vm);
			
			VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
			VirtualMachineRelocateSpec relocate = new VirtualMachineRelocateSpec();

			relocate.diskMoveType = "createNewChildDiskBacking";
			cloneSpec.setLocation(relocate);
			cloneSpec.setPowerOn(true);
			cloneSpec.setTemplate(false);
			cloneSpec.snapshot = vm.getCurrentSnapShot().getMOR();
						//cloneSpec.setSnapshot(null);
			String vmName = "New_VM_InitialPlacement"+count;

			Task task = vm.cloneVM_Task((Folder) vm.getParent(), vmName,
					cloneSpec);
			String status = task.waitForTask();

			if (status.equalsIgnoreCase(Task.SUCCESS)) 
			{
				log.info("Virtual Machine created successfully and placed  in vHost : "+ selectedHost);
				count++;

			}
			else
			{
				log.info("Virtual Machine creation failed");
				
			}
			

			// need to write logic to take snapshot of the vHost after adding the VM

		} catch (Exception e) {

			e.printStackTrace();
		}


	}

}
