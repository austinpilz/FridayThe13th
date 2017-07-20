package com.AustinPilz.FridayThe13th.Events;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
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
    private Arena arena;
    private boolean cancelled;

    public F13MenuItemClickedEvent(Player player, Arena a, String hiddenData, Material i) {
        super(false);
        this.hiddenData = hiddenData;
        this.item = i;
        this.player = player;
        this.arena = a;
    }

    /**
     * Returns the hidden data of the clicked item
     *
     * @return
     */
    public String getHiddenData() {
        return this.hiddenData;
    }

    /**
     * Returns the player who clicked the item
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the arena of the player who clicked the item
     *
     * @return
     */
    public Arena getArena() {
        return this.arena;
    }

    /**
     * Returns the material of the clicked item
     *
     * @return
     */
    public Material getItem() {
        return this.item;
    }

    /**
     * Returns if the event has been cancelled
     * @return
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets if the event has been cancelled
     * @param bln
     */
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
