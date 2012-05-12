package com.acuddlyheadcrab.apiEvents.parents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ArenaEvent extends Event implements Cancellable{
    private String arenakey;
    private boolean cancelled;
    
    public ArenaEvent(String arenakey) {
        setArenakey(arenakey);
    }

    private static final HandlerList handlers = new HandlerList();
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getArenakey() {
        return arenakey;
    }

    public void setArenakey(String arenakey) {
        this.arenakey = arenakey;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}