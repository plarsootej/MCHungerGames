package com.acuddlyheadcrab.apiEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


public class TributeWinEvent extends PlayerEvent{
    private String arenakey;
    
    public TributeWinEvent(Player tribute, String arenakey) {
        super(tribute);
        setArenakey(arenakey);
    }
    
    public String getArenakey(){
        return arenakey;
    }
    
    public void setArenakey(String arenakey){
        this.arenakey = arenakey;
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