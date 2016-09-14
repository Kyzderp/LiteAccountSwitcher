package io.github.kyzderp.liteaccountswitcher.gui;

import io.github.kyzderp.liteaccountswitcher.UserFileAccessor;
import io.github.kyzderp.liteaccountswitcher.struct.AccountInfo;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import com.google.common.collect.Lists;

public class UserlistSelectionList extends GuiListExtended
{
	private final UserlistGui parentGui;
	private final List<UserlistSelectionEntry> entries = Lists.<UserlistSelectionEntry>newArrayList();
	/** Index to the currently selected entry */
	private int selectedIdx = -1;

	private UserFileAccessor userFile;

	public UserlistSelectionList(UserFileAccessor userFile, UserlistGui parentGui, 
			Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
	{
		super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
		this.userFile = userFile;
		this.parentGui = parentGui;
		this.refreshList();
	}

	public void refreshList()
	{
		this.userFile.loadUsers();
		this.entries.clear();
		for (AccountInfo account: this.userFile.getUserList())
			this.entries.add(new UserlistSelectionEntry(this, account));
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public UserlistSelectionEntry getListEntry(int index)
	{
		return (UserlistSelectionEntry)this.entries.get(index);
	}

	protected int getSize()
	{
		return this.entries.size();
	}

	protected int getScrollBarX()
	{
		return super.getScrollBarX() + 20;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth()
	{
		return super.getListWidth() + 50;
	}

	public void selectEntry(int idx)
	{
		this.selectedIdx = idx;
		this.parentGui.selectEntry(this.getSelectedEntry());
	}

	/**
	 * Returns true if the element passed in is currently selected
	 */
	protected boolean isSelected(int slotIndex)
	{
		return slotIndex == this.selectedIdx;
	}

	@Nullable
	public UserlistSelectionEntry getSelectedEntry()
	{
		return this.selectedIdx >= 0 
				&& this.selectedIdx < this.getSize() ? this.getListEntry(this.selectedIdx) : null;
	}

	public UserlistGui getParentGui()
	{
		return this.parentGui;
	}

	public void deleteEntry(UserlistSelectionEntry entry)
	{
		this.userFile.getUserList().remove(entry.getAccount());
		this.userFile.saveUsers();
		this.entries.remove(entry);
		this.refreshList();
	}

	public boolean canMoveUp(UserlistSelectionEntry entry, int slot)
	{
		return slot > 0;
	}

	public boolean canMoveDown(UserlistSelectionEntry entry, int slot)
	{
		return slot < this.entries.size() - 1;
	}

	public void runSelected() 
	{
		this.parentGui.runSelected();
	}

	/**
	 * Move the entry up in the list
	 * @param entry
	 * @param slotIndex
	 * @param shiftKeyDown
	 */
	public void moveEntryUp(UserlistSelectionEntry entry, int slotIndex, boolean shiftKeyDown) 
	{
		AccountInfo account = entry.getAccount();
		this.userFile.getUserList().remove(account);

		// Move it all the way to the top
		if (shiftKeyDown)
			this.userFile.getUserList().add(0, account);
		else // Move it up one only
			this.userFile.getUserList().add(slotIndex - 1, account);
		
		this.userFile.saveUsers();
		this.refreshList();
	}
	
	/**
	 * Move the entry down in the list
	 * @param entry
	 * @param slotIndex
	 * @param shiftKeyDown
	 */
	public void moveEntryDown(UserlistSelectionEntry entry, int slotIndex, boolean shiftKeyDown) 
	{
		AccountInfo account = entry.getAccount();
		this.userFile.getUserList().remove(account);

		// Move it all the way to the bottom
		if (shiftKeyDown)
			this.userFile.getUserList().add(this.userFile.getUserList().size(), account);
		else // Move it down one only
			this.userFile.getUserList().add(slotIndex + 1, account);
		
		this.userFile.saveUsers();
		this.refreshList();
	}
}
