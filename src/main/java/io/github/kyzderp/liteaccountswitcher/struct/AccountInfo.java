package io.github.kyzderp.liteaccountswitcher.struct;

import io.github.kyzderp.liteaccountswitcher.util.Encryption;

public class AccountInfo 
{
	public String login;
	public String password;
	public String username;
	
	public String status;
	public String notes;
	
	public AccountInfo(String login, String pass, String username, String status, String notes) 
	{
		this.login = login;
		this.password = pass;
		this.username = username;
		this.status = status;
		this.notes = notes;
	}

	public AccountInfo(String login, String pass, String username, String notes) 
	{
		this(login, pass, notes);
		this.username = username;
	}
	
	public AccountInfo(String login, String pass, String notes) 
	{
		this.login = login;
		this.username = login;
		this.status = "Unverified";
		this.notes = notes;
		
		if (pass.isEmpty())
		{
			this.password = "";
			this.status = "Cracked";
		}
		else
			this.password = Encryption.encrypt(pass);
	}
	
}