package io.github.kyzderp.liteaccountswitcher.gui;

import io.github.kyzderp.liteaccountswitcher.struct.AccountInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class UserlistSelectionEntry implements GuiListExtended.IGuiListEntry 
{
	private final Minecraft mc;
	private final UserlistSelectionList containingListSel;
	private final AccountInfo account;
	private static final ResourceLocation SERVER_SELECTION_BUTTONS = new ResourceLocation("textures/gui/server_selection.png");
	private long lastClickTime = 0;

	public UserlistSelectionEntry(UserlistSelectionList listWorldSelIn, AccountInfo account)
	{
		this.account = account;
		this.containingListSel = listWorldSelIn;
		this.mc = Minecraft.getMinecraft();
	}

	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
	{
		int color = 8421504;
		if (this.account.status.equals("Premium"))
			color = 6618980;
		else if (this.account.status.equals("Invalid Credentials") 
				|| this.account.status.equals("Error")
				|| this.account.status.equals("Wrong Key"))
			color = 16737380;

		this.mc.fontRenderer.drawString(this.account.username, x + 32 + 3, y + 1, 16777215);
		this.mc.fontRenderer.drawString(this.account.login, x + 32 + 3, y + this.mc.fontRenderer.FONT_HEIGHT + 3, 8421504);
		this.mc.fontRenderer.drawString(this.account.status, x + 32 + 3, y + this.mc.fontRenderer.FONT_HEIGHT + this.mc.fontRenderer.FONT_HEIGHT + 3, color);
		int notesWidth = this.mc.fontRenderer.getStringWidth(this.account.notes);
		this.mc.fontRenderer.drawString(this.account.notes, x + 265 - notesWidth, y + this.mc.fontRenderer.FONT_HEIGHT + this.mc.fontRenderer.FONT_HEIGHT + 3, 8421504);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.mc.gameSettings.touchscreen || isSelected)
		{
			this.mc.getTextureManager().bindTexture(SERVER_SELECTION_BUTTONS);
			Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			int k1 = mouseX - x;
			int l1 = mouseY - y;

			// Play button
			if (k1 < 32 && k1 > 16)
			{
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
			}
			else
			{
				Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
			}

			if (this.containingListSel.canMoveUp(this, slotIndex))
			{
				if (k1 < 16 && l1 < 16)
				{
					Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
				}
				else
				{
					Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
				}
			}

			if (this.containingListSel.canMoveDown(this, slotIndex))
			{
				if (k1 < 16 && l1 > 16)
				{
					Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
				}
				else
				{
					Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
				}
			}
		}
	}

	/**
	 * Called when the mouse is clicked within this entry. Returning true means that something within this entry was
	 * clicked and the list should not be dragged.
	 */
	public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
	{
		if (relativeX <= 32)
		{
			if (relativeX < 32 && relativeX > 16)
			{
				this.containingListSel.selectEntry(slotIndex);
				this.containingListSel.runSelected();
				return true;
			}

			if (relativeX < 16 && relativeY < 16 && this.containingListSel.canMoveUp(this, slotIndex))
			{
				this.containingListSel.moveEntryUp(this, slotIndex, GuiScreen.isShiftKeyDown());
				return true;
			}

			if (relativeX < 16 && relativeY > 16 && this.containingListSel.canMoveDown(this, slotIndex))
			{
				this.containingListSel.moveEntryDown(this, slotIndex, GuiScreen.isShiftKeyDown());
				return true;
			}
		}

		this.containingListSel.selectEntry(slotIndex);

		if (Minecraft.getSystemTime() - this.lastClickTime < 250L)
		{
			this.containingListSel.selectEntry(slotIndex);
			this.containingListSel.runSelected();
		}

		this.lastClickTime = Minecraft.getSystemTime();
		return true;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
	{
	}

	public void setSelected(int what, int is, int dis)
	{
	}

	public AccountInfo getAccount()
	{
		return this.account;
	}

	public void delete()
	{
		this.containingListSel.deleteEntry(this);
	}

	public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
		// TODO Auto-generated method stub
		
	}
}
