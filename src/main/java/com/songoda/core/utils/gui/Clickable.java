package com.songoda.core.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Deprecated
public interface Clickable {

    void Clickable(Player player, Inventory inventory, ItemStack cursor, int slot, ClickType type);
}
