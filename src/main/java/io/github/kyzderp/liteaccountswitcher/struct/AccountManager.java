package io.github.kyzderp.liteaccountswitcher.struct;

import io.github.kyzderp.liteaccountswitcher.exception.FailedAuthenticationException;
import io.github.kyzderp.liteaccountswitcher.exception.FailedDecryptionException;
import io.github.kyzderp.liteaccountswitcher.util.Encryption;
import io.github.kyzderp.liteaccountswitcher.util.SessionChanger;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

public class AccountManager 
{
	public AuthenticationService authService;
	public MinecraftSessionService sessionService;
	public UUID uuid;
	public UserAuthentication auth;

	public AccountManager() 
	{
		this.uuid = UUID.randomUUID();
		this.authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
		this.auth = this.authService.createUserAuthentication(Agent.MINECRAFT);
		this.sessionService = this.authService.createMinecraftSessionService();
	}
	
	public boolean setUser(String username, String password) 
			throws FailedDecryptionException, FailedAuthenticationException 
	{
		String newPass = null;
		
		newPass = Encryption.decrypt(password);
		
		auth.logOut();
		auth.setUsername(username);
		auth.setPassword(newPass);
		try {
			auth.logIn();
			Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), auth.getAuthenticatedToken(), auth.getUserType().getName());
			SessionChanger.setSession(session);
			LiteLoaderLogger.info("New session username: " + session.getUsername());
		} catch (Exception e) {
			throw new FailedAuthenticationException();
		}
		return true;
	}

	public boolean setUserOffline(String username) 
	{
		this.auth.logOut();
		Session session = new Session(username, username, "0", "legacy");
		try {
			SessionChanger.setSession(session);
			LiteLoaderLogger.info("New session username: " + session.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
