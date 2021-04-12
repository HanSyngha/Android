import java.util.*;
import java.util.ArrayList;

class Bank{
	ArrayList<Account> Accounts = new ArrayList<Account>();
	public Bank(double balance){
		addAccount(balance);
	}
	public void addAccount(double initialBalance){
		Accounts.add(new Account(initialBalance));
		System.out.printf("Account[%d] with %.1f balance added\n",(Accounts.size()-1),initialBalance);
	}
	public void deposit(int account, double amount){
		Account temp = (Account)Accounts.get(account);
		temp.deposit(amount);
		Accounts.set(account,temp);
		System.out.printf("Depoist %.1f amount to Account[%d], Balance: %.1f\n",amount,account,temp.getBalance());
	}
	public void withdraw(int account, double amount){
		Account temp = (Account)Accounts.get(account);
		temp.withdraw(amount);
		Accounts.set(account,temp);
		System.out.printf("Withdraw %.1f amount to Account[%d], Balance: %.1f\n",amount,account,temp.getBalance());
	}
	public double getBalance(int account){
		Account temp = (Account)Accounts.get(account);
		return temp.getBalance();
	}
}

class Account{
	double balance;
	public Account(double initialBalance){
		this.balance = initialBalance;
	}
	public void deposit(double amount){
		this.balance += amount;
	}
	public void withdraw(double amount){
		this.balance -= amount;
	}
	public double getBalance(){
		return this.balance;
	}
}

public class Main{
	public static void main(String[] args){
		Bank accounts = new Bank(1000);
		accounts.addAccount(500);
		accounts.addAccount(300);
		accounts.addAccount(0);
		accounts.deposit(3,200);
		System.out.printf("Account[%d] has balance of %.1f\n",3,accounts.getBalance(3));
		accounts.withdraw(2,100);
		System.out.printf("Account[%d] has balance of %.1f\n",2,accounts.getBalance(2));
	}
}
