package io.github.kyzderp.liteaccountswitcher.gui;

import io.github.kyzderp.liteaccountswitcher.UserFileAccessor;
import io.github.kyzderp.liteaccountswitcher.struct.AccountInfo;
import io.github.kyzderp.liteaccountswitcher.struct.AccountManager;
import io.github.kyzderp.liteaccountswitcher.util.Encryption;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class EditUserGui extends GuiScreen
{
	private UserFileAccessor userfile;
	private AccountManager accManager;

	private GuiHiddenTextField passField;
	private GuiTextField loginField;
	private GuiTextField notesField;

	private AccountInfo account;
	private boolean isNewUser;

	private String title;

	public EditUserGui(UserFileAccessor userFile, AccountManager accountManager)
	{
		this.userfile = userFile;
		this.accManager = accountManager;
		this.account = null;
		this.isNewUser = true;
		this.title = "Add New User";
	}

	public EditUserGui(UserFileAccessor userFile, AccountManager accountManager, AccountInfo account)
	{
		this.userfile = userFile;
		this.accManager = accountManager;
		this.account = account;
		this.isNewUser = false;
		this.title = "Edit User";
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		this.loginField.updateCursorCounter();
		this.passField.updateCursorCounter();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
	 * window resizes, the buttonList is cleared beforehand.
	 */
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Done"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, "Cancel"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 150, "Hide/Show Password"));
		
		this.loginField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 46, 200, 20);
		this.loginField.setFocused(true);
		
		this.notesField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 86, 200, 20);
		this.notesField.setMaxStringLength(18);
		
		this.passField = new GuiHiddenTextField(1, this.width / 2 - 100, 126, 200, 20);
		this.passField.setMaxStringLength(128);
		this.passField.setSecret(true);

		if (this.isNewUser)
		{
			this.loginField.setText("");
			this.passField.setText("");
			this.notesField.setText("");
		}
		else
		{
			this.loginField.setText(this.account.login);
			this.notesField.setText(this.account.notes);

			if (!this.account.password.isEmpty())
			{
				try {
					this.passField.setText(Encryption.decrypt(this.account.password));
				} catch (Exception e) {
					this.passField.setText("Wrong key!");
				}
			}
		}

		((GuiButton)this.buttonList.get(0)).enabled = !this.loginField.getText().isEmpty();
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.enabled)
		{
			if (button.id == 2)
			{
				this.passField.showText(!this.passField.isTextShowing());
			}
			else if (button.id == 1)
			{
				this.mc.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
			}
			else if (button.id == 0)
			{
				if(isNewUser) 
				{
					this.userfile.getUserList().add(new AccountInfo(this.loginField.getText(), 
							this.passField.getText(), this.notesField.getText()));
				} 
				else 
				{
					this.account.login = this.loginField.getText();
					this.account.notes = this.notesField.getText();
					if (this.passField.getText().isEmpty())
					{
						this.account.password = "";
						this.account.status = "Cracked";
					}
					else
					{
						this.account.password = Encryption.encrypt(passField.getText());
						this.account.status = "Unverified";
					}
				}
				this.userfile.saveUsers();
				this.mc.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));
			}
		}
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		this.loginField.textboxKeyTyped(typedChar, keyCode);
		this.notesField.textboxKeyTyped(typedChar, keyCode);
		this.passField.textboxKeyTyped(typedChar, keyCode);

		if (keyCode == Keyboard.KEY_TAB)
		{
			if (this.loginField.isFocused())
			{
				this.loginField.setFocused(false);
				this.notesField.setFocused(true);
				this.passField.setFocused(false);
			}
			else if (this.notesField.isFocused())
			{
				this.loginField.setFocused(false);
				this.notesField.setFocused(false);
				this.passField.setFocused(true);
			}
			else
			{
				this.loginField.setFocused(true);
				this.notesField.setFocused(false);
				this.passField.setFocused(false);
			}
		}

		if (keyCode == 28 || keyCode == 156)
			this.actionPerformed((GuiButton)this.buttonList.get(0));

		if (keyCode == Keyboard.KEY_ESCAPE)
			this.mc.displayGuiScreen(new UserlistGui(this.accManager, this.userfile));

		((GuiButton)this.buttonList.get(0)).enabled = !this.loginField.getText().isEmpty();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.passField.mouseClicked(mouseX, mouseY, mouseButton);
		this.loginField.mouseClicked(mouseX, mouseY, mouseButton);
		this.notesField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 17, 16777215);
		this.drawString(this.fontRendererObj, "Username / Email", this.width / 2 - 100, 33, 10526880);
		this.drawString(this.fontRendererObj, "Short Note", this.width / 2 - 100, 73, 10526880);
		this.drawString(this.fontRendererObj, "Password", this.width / 2 - 100, 113, 10526880);
		this.loginField.drawTextBox();
		this.notesField.drawTextBox();
		this.passField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
