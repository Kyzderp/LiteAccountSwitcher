package io.github.kyzderp.liteaccountswitcher.gui;

import io.github.kyzderp.liteaccountswitcher.LiteModAccountSwitcher;
import io.github.kyzderp.liteaccountswitcher.UserFileAccessor;
import io.github.kyzderp.liteaccountswitcher.exception.FailedAuthenticationException;
import io.github.kyzderp.liteaccountswitcher.exception.FailedDecryptionException;
import io.github.kyzderp.liteaccountswitcher.struct.AccountManager;
import io.github.kyzderp.liteaccountswitcher.util.Encryption;

import java.io.IOException;

import javax.annotation.Nullable;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class UserlistGui extends GuiScreen
{
	protected String title = "Select User";

	private GuiButton editButton;
	private GuiButton loginButton;
	private GuiButton newButton;
	private GuiButton deleteButton;
	private GuiButton offlineButton;

	private GuiTextField keyField;
	
	private String message = "";

	private UserlistSelectionList selectionList;

	private AccountManager accountManager;
	private UserFileAccessor userFile;
	
	public UserlistGui(AccountManager accountManager, UserFileAccessor userFile)
	{
		this.accountManager = accountManager;
		this.userFile = userFile;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		this.selectionList = new UserlistSelectionList(this.userFile, this, this.mc, 
				this.width, this.height, 32, this.height - 64, 36);
		this.postInit();
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		this.selectionList.handleMouseInput();
	}

	public void postInit()
	{
		this.loginButton = this.addButton(new GuiButton(1, this.width / 2 - 154, this.height - 52, 72, 20, "Login"));
		this.offlineButton = this.addButton(new GuiButton(3, this.width / 2 - 76, this.height - 52, 72, 20, "Offline Login"));
		this.newButton = this.addButton(new GuiButton(4, this.width / 2 - 154, this.height - 28, 72, 20, "New"));
		this.editButton = this.addButton(new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, "Edit"));
		this.deleteButton = this.addButton(new GuiButton(5, this.width / 2 + 4, this.height - 28, 72, 20, "Delete"));
		this.addButton(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, "Cancel"));

		this.loginButton.enabled = false;
		this.editButton.enabled = false;
		this.offlineButton.enabled = false;
		this.deleteButton.enabled = false;

		this.keyField = new GuiTextField(0, this.mc.fontRenderer, this.width / 2 + 29, this.height - 50, 100, 16);
		this.keyField.setFocused(true);
		this.keyField.setText(Encryption.currentKey);

		Encryption.setKey(this.keyField.getText());


		this.addButton(new GuiButton(6, this.width / 2 + 134, this.height - 52, 20, 20, "?"));
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			UserlistSelectionEntry selected = this.selectionList.getSelectedEntry();

			if (button.id == 1) // Login
			{
				this.onlineLogin();
			}

			else if (button.id == 2) // Edit
			{
				this.mc.displayGuiScreen(new EditUserGui(this.userFile, this.accountManager, selected.getAccount()));
			}

			else if (button.id == 3) // Login offline
			{
				this.offlineLogin();
			}

			else if (button.id == 4) // New
			{
				this.mc.displayGuiScreen(new EditUserGui(this.userFile, this.accountManager));
			}

			else if (button.id == 5) // Delete
			{
				selected.delete();
			}

			else if (button.id == 6) // Help
			{
				this.mc.displayGuiScreen(new KeyInfoGui(this.userFile, this.accountManager));
			}

			else if (button.id == 0) // Cancel
			{
				this.mc.displayGuiScreen(null);
			}
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.selectionList.drawScreen(mouseX, mouseY, partialTicks);
		this.keyField.drawTextBox();
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 20, 16777215);
		
		if (!this.message.isEmpty())
			this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 5, 6618980);
		
		this.drawString(this.fontRenderer, "Key", this.width / 2 + 5, this.height - 46, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.selectionList.mouseClicked(mouseX, mouseY, mouseButton);
		this.keyField.mouseClicked(mouseX, mouseY, mouseButton);

		Encryption.setKey(this.keyField.getText());
	}

	/**
	 * Called when a mouse button is released.
	 */
	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
		this.selectionList.mouseReleased(mouseX, mouseY, state);
	}

	public void selectEntry(@Nullable UserlistSelectionEntry entry)
	{
		if (entry == null)
			return;
		
		if (!entry.getAccount().password.isEmpty())
			this.loginButton.enabled = true;
		else
			this.loginButton.enabled = false;
		
		this.offlineButton.enabled = true;
		this.editButton.enabled = true;
		this.newButton.enabled = true;
		this.deleteButton.enabled = true;
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		this.keyField.textboxKeyTyped(typedChar, keyCode);

		Encryption.setKey(this.keyField.getText());

		if (keyCode == Keyboard.KEY_ESCAPE)
			mc.displayGuiScreen(null);
	}
	
	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	/**
	 * Attempt to log into the account in offline mode
	 */
	private void offlineLogin()
	{
		UserlistSelectionEntry selected = this.selectionList.getSelectedEntry();
		
		boolean success = this.accountManager.setUserOffline(selected.getAccount().username);
		if (!success)
		{
			selected.getAccount().status = "Error";
			this.message = "";
		}
		else
		{
			if (!selected.getAccount().status.equals("Premium"))
				selected.getAccount().status = "Cracked";
			this.message = "Offline mode login successful.";
			if (LiteModAccountSwitcher.ingame)
				this.message += " Please reconnect for changes to take effect.";
		}
		this.userFile.saveUsers();
	}
	
	/**
	 * Attempt to log into the account in premium mode
	 */
	private void onlineLogin()
	{
		UserlistSelectionEntry selected = this.selectionList.getSelectedEntry();
		
		try 
		{
			this.accountManager.setUser(selected.getAccount().login, selected.getAccount().password);
			selected.getAccount().status = "Premium";
			selected.getAccount().username = this.mc.getSession().getUsername();
			this.message = "Premium mode login successful.";
			if (LiteModAccountSwitcher.ingame)
				this.message += " Please reconnect for changes to take effect.";
		} 
		catch (FailedDecryptionException e) 
		{
			selected.getAccount().status = "Wrong Key";
			this.message = "";
		} 
		catch (FailedAuthenticationException e) 
		{
			selected.getAccount().status = "Invalid Credentials";
			this.message = "";
		}
		this.userFile.saveUsers();
	}

	/**
	 * Attempt to login to the selected account. Prioritizes online over offline.
	 */
	public void runSelected() 
	{
		UserlistSelectionEntry selected = this.selectionList.getSelectedEntry();
		if (!selected.getAccount().password.isEmpty())
			this.onlineLogin();
		else
			this.offlineLogin();
	}
}
