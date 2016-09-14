package io.github.kyzderp.liteaccountswitcher;

import io.github.kyzderp.liteaccountswitcher.struct.AccountInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

public class UserFileAccessor 
{
	private final File file = new File(LiteLoader.getCommonConfigFolder(), "users.json");
	private Gson gson;

	private List<AccountInfo> userlist;

	public UserFileAccessor()
	{
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.userlist = new ArrayList<AccountInfo>();

		if (!this.file.exists())
			this.saveUsers();
		else
			this.loadUsers();
	}

	/**
	 * Load user list from file
	 */
	public void loadUsers() 
	{
		this.userlist.clear();
		try
		{
			BufferedReader loader = new BufferedReader(new FileReader(this.file));
			JsonObject json = (JsonObject)new JsonParser().parse(loader);
			loader.close();
			Iterator<Entry<String, JsonElement>> iter = json.entrySet().iterator();
			while(iter.hasNext()) 
			{
				Entry<String, JsonElement> entry = iter.next();
				JsonObject element = (JsonObject)entry.getValue();

				if (element.has("Username") && element.has("Login") 
						&& element.has("Password") && element.has("Status")) 
				{
					String user = element.get("Username").getAsString();
					String login = element.get("Login").getAsString();
					String pass = element.get("Password").getAsString();
					String status = element.get("Status").getAsString();
					AccountInfo accData = new AccountInfo(login, pass, user, status);
					this.userlist.add(accData);
				}
			}
		} 
		catch (FileNotFoundException e)
		{
			LiteLoaderLogger.warning("Could not load " + this.file.getPath());
		} 
		catch (IOException e) 
		{
			LiteLoaderLogger.warning("Could not close resource " + this.file.getPath());
		}
	}

	/**
	 * Save user list to file
	 */
	public void saveUsers() 
	{
		JsonObject json = new JsonObject();
		int n = 1;

		for (AccountInfo accData : userlist) 
		{
			JsonObject obj = new JsonObject();
			obj.addProperty("Username", accData.username);
			obj.addProperty("Login", accData.login);
			obj.addProperty("Password", accData.password);
			obj.addProperty("Status", accData.status);
			json.add("" + n, obj);
			n++;
		}
		PrintWriter save;
		try 
		{
			save = new PrintWriter(new FileWriter(file));
			save.println(gson.toJson(json));
			save.close();
		} 
		catch (IOException e) 
		{
			LiteLoaderLogger.severe("Could not save users in " + this.file.getPath());
		}
	}

	public List<AccountInfo> getUserList()
	{
		return this.userlist;
	}
}
