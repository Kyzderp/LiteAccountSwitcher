package io.github.kyzderp.liteaccountswitcher.gui;

import io.github.kyzderp.liteaccountswitcher.UserFileAccessor;
import io.github.kyzderp.liteaccountswitcher.struct.AccountManager;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

public class KeyInfoGui extends GuiScreen
{
	private UserFileAccessor userfile;
	private AccountManager accManager;

	public KeyInfoGui(UserFileAccessor userFile, AccountManager accountManager)
	{
		this.userfile = userFile;
		this.accManager = accountManager;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 30, "Done"));
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled && button.id == 0)
			this.mc.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (keyCode == Keyboard.KEY_ESCAPE)
			this.mc.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "What is the \"Key\"?", this.width / 2, this.height / 2 - 100, 16777215);

		String[] lines = {"Your key is the \"password\" that is used to encrypt your login",
				"credentials. Think of it as an extra layer of security so that someone",
				"who finds the files on your computer won't be able to immediately steal",
				"your accounts. When you add a new account to the user list, the current",
				"key you have entered will be used as the password for that account.",
				"Logging into any accounts afterwards will require their respective keys."
		};
		int y = 0;
		for (String line: lines)
		{
			this.drawString(this.fontRenderer, line, this.width / 2 - 180, this.height / 2 - 70 + y, 16777215);
			y += 14;
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
