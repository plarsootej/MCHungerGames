package com.acuddlyheadcrab.apiEvents;

import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.apiEvents.parents.ArenaEvent;


public class GameStopEvent extends ArenaEvent{
    
    public GameStopEvent(String arenakey) {
        super(arenakey);
    }
    
    private static final HandlerList handlers = new HandlerList();
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}