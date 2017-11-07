package project3.obj;

import utils.Generator;

public class Customer {
	public int ID;
	public String Name;
	public int Age;
	public int CountryCode;
	public float Salary;
	private static int _ID = 1;
	public Customer(int iD, String name, int age, int countryCode, float salary) {
		super();
		ID = iD;
		Name = name;
		Age = age;
		CountryCode = countryCode;
		Salary = salary;
	}

	public static Customer genRandomCustomer()
	{
		int ID = _ID;
		String Name = Generator.genRandomString(10, 20);
		int Age = Generator.genRanInt(10, 70);
		int CountryCode = Generator.genRanInt(1, 10);
		float Salary = Generator.genRandomFloat(100, 10000);
		Customer cus = new Customer(ID, Name, Age, CountryCode, Salary);
		_ID++;
		return cus;
	}
	@Override
	public String toString() {
		String SEPARATOR = ",";
		return this.ID + SEPARATOR +
				this.Name  + SEPARATOR +
				this.Age  + SEPARATOR +
				this.CountryCode + SEPARATOR +
				this.Salary;
				
	}
}
