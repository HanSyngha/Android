class BankAccount {
	double balance;
	public BankAccount(double balance){
		if(0>balance){
			IllegalArgumentException exception = new IllegalArgumentException("Unavailable balance.");
			throw exception;
		}
		this.balance = balance;
		System.out.printf("You have %.1f of balance\n",balance);
	}
	public void withdraw (double amount){
		if(0>amount){
			IllegalArgumentException exception = new IllegalArgumentException("Amount out of bound.");
			throw exception;
		}
		else if(amount > balance){
			IllegalArgumentException exception = new IllegalArgumentException("Amount exceeds balance.");
			throw exception;
		}
		balance = balance - amount;
		System.out.printf("Withdrwing %.1f amout.\nYou have %.1f of balance left\n",amount,balance);
	}
}

public class Main{
	public static void main(String[] args){
		try{
			BankAccount acct = new BankAccount(-10);
		} catch (IllegalArgumentException ex){
			ex.printStackTrace();
		}//Negative Balance
		BankAccount acct = new BankAccount(100); //Normal Balance
		try{
			acct.withdraw(200);
		}catch (IllegalArgumentException ex){
			ex.printStackTrace();
		}//withdraw amout exceeding balance	
		try{
			acct.withdraw(-200);
		}catch (IllegalArgumentException ex){
			ex.printStackTrace();
		}//withdraw negative amount
		try{
			acct.withdraw(50);
		}catch (IllegalArgumentException ex){
			ex.printStackTrace();
		}//Normal withdraw
	}
}
