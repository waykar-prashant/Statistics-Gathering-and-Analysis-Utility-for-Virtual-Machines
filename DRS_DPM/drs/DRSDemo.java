package drs;

import java.util.Scanner;

import org.apache.log4j.Logger;

import Utility.Util;

public class DRSDemo {
	private static final Logger log = Logger.getLogger(DRSDemo.class);

	public static void main(String args[]){
		
		new Util();
		
		Util.displayInventory();
		
		while(true){
			
			System.out.println("Select an option: 1.Add VM  \n 2.Unbalance Hosts \n 3. Start DRS ");
			Scanner scanner = new Scanner(System.in);
			int choice = scanner.nextInt();

			switch(choice){

			case 1: 
				
				System.out.println("Adding VM.....");
				new Thread(new InitialPlacement()).start();
		
				break;
				
			case 2:
				System.out.println("Unbalance the Load .....");
				new Thread(new LoadUnbalance()).start();
				
				break;
				
			case 3:
				new Thread(new StartDRS()).start();
			
			}
			
			
			
		}
		
	}

}
