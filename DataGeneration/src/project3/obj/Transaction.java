package project3.obj;

import constants.Constants;
import utils.Generator;

public class Transaction {
	public int TransID;
	public int CustID;
	public float TransTotal;
	public int TransNumItems;
	public String TransDesc;
	private static int _TransID = 1;
	
	public Transaction(int transID, int custID, float transTotal,
			int transNumItems, String transDesc) {
		super();
		TransID = transID;
		CustID = custID;
		TransTotal = transTotal;
		TransNumItems = transNumItems;
		TransDesc = transDesc;
	}

	public static Transaction genRanTransaction()
	{
		int TransID = _TransID;
		int CustID = Generator.genRanInt(1, Constants.MAX_CUS_ID);
		float TransTotal = Generator.genRandomFloat(10, 1000);
		int TransNumItems = Generator.genRanInt(1, 10);
		String TransDesc = Generator.genRandomString(20, 50);
		Transaction tran = new Transaction(TransID, CustID, TransTotal, TransNumItems, TransDesc);
		_TransID++;
		return tran;
	}
	
	@Override
	public String toString() {
		String SEPARATOR = ",";
		return this.TransID + SEPARATOR +
				this.CustID  + SEPARATOR +
				this.TransTotal  + SEPARATOR +
				this.TransNumItems + SEPARATOR +
				this.TransDesc;
				
	}
}
