package io.github.kyzderp.liteaccountswitcher;

import io.github.kyzderp.liteaccountswitcher.gui.UserlistGui;
import io.github.kyzderp.liteaccountswitcher.struct.AccountManager;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;

public class LiteModAccountSwitcher implements Tickable 
{
	private static LiteModAccountSwitcher instance;
	public static boolean ingame = false;
	private KeyBinding keyGui = new KeyBinding("key.accswitcher.config", Keyboard.KEY_P, "key.categories.liteaccountswitcher.gui");
	
	private UserFileAccessor userfile;
	private AccountManager accManager;

	public String getName() { return "Lite Account Switcher"; }
	public String getVersion() { return "1.2.0"; }

	public LiteModAccountSwitcher() 
	{
		instance = this;
	}

	public void init(File configPath) 
	{
		LiteLoader.getInput().registerKeyBinding(keyGui);
		this.userfile = new UserFileAccessor();
		this.accManager = new AccountManager();
	}

	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) 
	{
		// Allow switching ingame
		if (inGame && minecraft.currentScreen == null) 
		{
			if(keyGui.isPressed()) 
			{
				LiteModAccountSwitcher.ingame = true;
				minecraft.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
			}
		}

		// Allow switching in main menu and multiplayer menu
		if (minecraft.currentScreen instanceof GuiMainMenu 
				|| minecraft.currentScreen instanceof GuiMultiplayer) 
		{
			if (Keyboard.isKeyDown(keyGui.getKeyCode())) 
			{
				LiteModAccountSwitcher.ingame = false;
				minecraft.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
			}
		}
	}

	public static LiteModAccountSwitcher getInstance()
	{
		return instance;
	}
}