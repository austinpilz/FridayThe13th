package com.AustinPilz.FridayThe13th.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class F13MenuItemClickedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private String hiddenData;
    private Material item;
    private Player player;
    private boolean cancelled;

    public F13MenuItemClickedEvent(Player player, String hiddenData, Material i) {
        super(false);
        this.hiddenData = hiddenData;
        this.item = i;
        this.player = player;
    }

    public String getHiddenData() {
        return this.hiddenData;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Material getItem() {
        return this.item;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean bln) {
        this.cancelled = bln;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
