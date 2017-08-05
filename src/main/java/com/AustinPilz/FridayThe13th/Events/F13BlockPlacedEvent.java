package com.AustinPilz.FridayThe13th.Events;

import com.AustinPilz.FridayThe13th.Components.Arena.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class F13BlockPlacedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;
    private String hiddenData;
    private Player player;
    private Block block;
    private Material originalMaterial;
    private boolean cancelled;

    public F13BlockPlacedEvent(Player player, Arena a, Block b, Material o, String hiddenData) {
        super(false);
        this.hiddenData = hiddenData;
        this.player = player;
        this.block = b;
        this.originalMaterial = o;
        this.arena = a;
    }

    /**
     * Returns the hidden data from the placed block
     *
     * @return
     */
    public String getHiddenData() {
        return this.hiddenData;
    }

    /**
     * Returns the bukkit player object who placed the block
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Block getBlock() {
        return this.block;
    }

    public Material getOriginalMaterial() {
        return this.originalMaterial;
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
