package com.acuddlyheadcrab.apiEvents;

import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.apiEvents.parents.ArenaEvent;



public class GameStartEvent extends ArenaEvent{
    private int countdown;
    
    public GameStartEvent(String arenakey) {
        super(arenakey);
    }
    
    public GameStartEvent(String arenakey, int countdown) {
        super(arenakey);
        setCountdown(countdown);
    }
    
    public boolean hasCountdown(){
        return countdown!=0;
    }
    
    public int getCountdown() {
        return countdown;
    }
    
    public void setCountdown(int countdown) {
        this.countdown = countdown;
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