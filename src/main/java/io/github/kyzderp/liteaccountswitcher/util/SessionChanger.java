package io.github.kyzderp.liteaccountswitcher.util;

import java.lang.reflect.Field;

import com.mumfrey.liteloader.util.log.LiteLoaderLogger;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionChanger 
{
	public static void setSession(Session newSession) throws Exception 
	{
		Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
		try {
			Field session = null;

			for (Field field : mc.getDeclaredFields()) {
				if (field.getType().isInstance(newSession)) {
					session = field;
					LiteLoaderLogger.info("Attempting injection into session.");
				}
			}

			if (session == null) {
				throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
			}

			session.setAccessible(true);
			session.set(Minecraft.getMinecraft(), newSession);
			session.setAccessible(false);
		} catch (Exception exeption) {
			exeption.printStackTrace();
			throw exeption;
		}
	}
}
