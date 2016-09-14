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

	private AccountInfo info;
	private boolean isNewUser;

	private String title;

	public EditUserGui(UserFileAccessor userFile, AccountManager accountManager)
	{
		this.userfile = userFile;
		this.accManager = accountManager;
		this.info = null;
		this.isNewUser = true;
		this.title = "Add New User";
	}

	public EditUserGui(UserFileAccessor userFile, AccountManager accountManager, AccountInfo info)
	{
		this.userfile = userFile;
		this.accManager = accountManager;
		this.info = info;
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
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, 130, "Hide/Show Password"));
		this.loginField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20);
		this.loginField.setFocused(true);
		this.passField = new GuiHiddenTextField(1, this.width / 2 - 100, 106, 200, 20);
		this.passField.setMaxStringLength(128);
		this.passField.setSecret(true);

		if (this.isNewUser)
		{
			this.loginField.setText("");
			this.passField.setText("");
		}
		else
		{
			this.loginField.setText(this.info.login);

			if (!this.info.password.isEmpty())
			{
				try {
					this.passField.setText(Encryption.decrypt(this.info.password));
				} catch (Exception e) {
					this.passField.setText("Wrong key!");
				}
			}
		}

		((GuiButton)this.buttonList.get(0)).enabled = !this.passField.getText().isEmpty() && this.passField.getText().split(":").length > 0 && !this.loginField.getText().isEmpty();
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
					this.userfile.getUserList().add(new AccountInfo(loginField.getText(), passField.getText()));
				} 
				else 
				{
					this.info.login = this.loginField.getText();
					if (this.passField.getText().isEmpty())
					{
						this.info.password = "";
						this.info.status = "Cracked";
					}
					else
					{
						this.info.password = Encryption.encrypt(passField.getText());
						this.info.status = "Unverified";
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
		this.passField.textboxKeyTyped(typedChar, keyCode);

		if (keyCode == 15)
		{
			this.loginField.setFocused(!this.loginField.isFocused());
			this.passField.setFocused(!this.passField.isFocused());
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
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 17, 16777215);
		this.drawString(this.fontRendererObj, "Username / Email", this.width / 2 - 100, 53, 10526880);
		this.drawString(this.fontRendererObj, "Password", this.width / 2 - 100, 94, 10526880);
		this.loginField.drawTextBox();
		this.passField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
