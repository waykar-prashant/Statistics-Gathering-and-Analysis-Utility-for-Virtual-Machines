import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class GetStatistics {
	public static ServiceInstance si;
	public static HostSystem vmHost = null;

	public static void main(String[] args) throws IOException,
			InterruptedException {

		VirtualMachine vm;
		String hostName = null;
		String vmName = null;

		try {
			si = new ServiceInstance(new URL(ConstantUtil.VCENTER_URL),
					ConstantUtil.VCENTER_USERNAME,
					ConstantUtil.VCENTER_PASSWORD, true);

			if (args.length < 2) {
				if (args[0] != "" && args[0].length() > 0 && args[0] != null) {
					vmName = args[0];
				}
			}
			if (args.length == 2) {
				if (args[0] != "" && args[0].length() > 0 && args[0] != null) {
					vmName = args[0];
				}

				if (args[1] != "" && args[1].length() > 0 && args[1] != null) {
					hostName = args[1];
				}
			}
			GenerateLogs generateLogs = new GenerateLogs();
			generateLogs.setVmName(vmName);
			generateLogs.setHostName(hostName);
			generateLogs.start();
			generateLogs.join();
		} catch (Exception e) {
			System.out.println("Error in getStatistics" + e.getMessage());
		}
	}

}
