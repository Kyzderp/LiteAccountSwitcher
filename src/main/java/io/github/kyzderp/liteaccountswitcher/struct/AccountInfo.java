package io.github.kyzderp.liteaccountswitcher.struct;

import io.github.kyzderp.liteaccountswitcher.util.Encryption;

public class AccountInfo 
{
	public String login;
	public String password;
	public String username;
	
	public String status;
	
	public AccountInfo(String login, String pass, String username, String status) 
	{
		this.login = login;
		this.password = pass;
		this.username = username;
		this.status = status;
	}

	public AccountInfo(String login, String pass, String username) 
	{
		this(login, pass);
		this.username = username;
	}
	
	public AccountInfo(String login, String pass) 
	{
		this.login = login;
		this.username = login;
		this.status = "Unverified";
		
		if (pass.isEmpty())
		{
			this.password = "";
			this.status = "Cracked";
		}
		else
			this.password = Encryption.encrypt(pass);
	}
	
}