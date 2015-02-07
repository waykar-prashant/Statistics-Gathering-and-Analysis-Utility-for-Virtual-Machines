package drs;

import org.apache.log4j.Logger;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.Task;

import Utility.LoadBalanceUtility;
import Utility.Util;

public class LoadUnbalance implements Runnable {
	
	public static float drsUpperThresholdCpuUsage=70;
	private int vmNo=1;
	private static final Logger log = Logger.getLogger(LoadUnbalance.class);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		for(int i=0; i<Util.hosts.length; i++){
			
			
			
			log.info("Before Unbalancing CPU Usage of vHost : "+Util.hosts[i].getName() +" : "+LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) );
			while(LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) < drsUpperThresholdCpuUsage)
			{
				
				try{


					com.vmware.vim25.mo.VirtualMachine vm = Util.getvHost(Util.hosts[i].getName()).getVms()[0];
					
					

					log.info("VM: "+vm.getName() +"   will be cloned and added to the  vHost : "+ Util.hosts[i].getName());

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
					String vmName = "Unbalance_VM_"+vmNo;

					Task task = vm.cloneVM_Task((Folder) vm.getParent(), vmName,
							cloneSpec);
					String status = task.waitForTask();

					if (status.equalsIgnoreCase(Task.SUCCESS)) 
					{
						log.info("Virtual Machine created successfully and placed  in vHost : ");
						
						vmNo++;

					}
					else
					{
						log.info("Virtual Machine creation failed");
						
					}
					
					Thread.sleep(20000);

				} catch (Exception e) {

					e.printStackTrace();
				}
				
			}
			
			log.info("After Unbalancing CPU Usage of vHost : "+Util.hosts[i].getName() +" : "+LoadBalanceUtility.getHostUsage(Util.getvHost(Util.hosts[i].getName())) );
		}
		
	}
	

}
