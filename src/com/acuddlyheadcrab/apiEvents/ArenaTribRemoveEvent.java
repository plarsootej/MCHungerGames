package com.acuddlyheadcrab.apiEvents;

import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.apiEvents.parents.ArenaModifyEvent;


public class ArenaTribRemoveEvent extends ArenaModifyEvent{
    private String trib;
    
    public ArenaTribRemoveEvent(String arenakey, String trib) {
        super(arenakey, YMLKey.getArenaSubkey(arenakey, YMLKey.ARN_TRIBS), trib);
        setTrib(trib);
    }
    
    public String getTrib(){
        return trib;
    }
    
    public void setTrib(String player){
        this.trib = player;
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