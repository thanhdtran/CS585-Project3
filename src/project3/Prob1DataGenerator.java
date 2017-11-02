package project3;

import constants.Constants;
import project3.obj.Customer;
import project3.obj.Transaction;
import utils.FileUtils;

public class Prob1DataGenerator {
	public static void writeObjs(Object[] objs, String fileout) {
		if (FileUtils.checkExist(fileout)) {
			FileUtils.openFileAppend(fileout);
		} else {
			FileUtils.openFile(fileout);
		}
		int i = 0;
		for (Object obj : objs) {
			FileUtils.writeToFile(obj.toString());
			if (i ++ % 1000 == 0) {
				FileUtils.flushToFile();
			}
		}
		FileUtils.flushToFile();
		FileUtils.closeFile();
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: Prob1DataGenerator.jar [customers_path] [transactions_path] "
					+ " [NUM_CUSTOMERS (optional)] "
					+ " [NUM_TRANSACTIONS (optional)]");
			System.exit(1);
		}
		String customersPath = args[0];
		String transactionsPath = args[1];
		
		if (args.length >= 4) {
			Constants.MAX_CUS_ID = Integer.parseInt(args[2]);
			Constants.MAX_TRANS_ID = Integer.parseInt(args[3]);
		}
		int BATCH_SIZE = 100;
		int NUM_CUS = Constants.MAX_CUS_ID;
		int NUM_TRANS = Constants.MAX_TRANS_ID;
		
		int cus_loop = NUM_CUS/BATCH_SIZE;
		int trans_loop = NUM_TRANS/BATCH_SIZE;
		
		//delete old files
		FileUtils.checkExistNDel(customersPath);
		FileUtils.checkExistNDel(transactionsPath);
		
		//generate customers:
		for (int i = 0; i < cus_loop; i++) {
			Customer[] cusList = new Customer[BATCH_SIZE]; 
			for (int j = 0; j < BATCH_SIZE; j++) {
				cusList[j] = Customer.genRandomCustomer();
			}
			writeObjs(cusList, customersPath);
			for (int j = 0; j < BATCH_SIZE; j++) {
				cusList[j] = null;
			}
			
		}
		System.gc();
		
		//generate transactions:
		for (int i = 0; i < trans_loop; i++) {
			Transaction[] transList = new Transaction[BATCH_SIZE]; 
			for (int j = 0; j < BATCH_SIZE; j++) {
				transList[j] = Transaction.genRanTransaction();
			}
			writeObjs(transList, transactionsPath);
			for (int j = 0; j < BATCH_SIZE; j++) {
				transList[j] = null;
			}
			
		}
		System.gc();
			
	}
}
