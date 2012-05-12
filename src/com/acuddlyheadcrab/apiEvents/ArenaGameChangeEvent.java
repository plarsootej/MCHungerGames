package com.acuddlyheadcrab.apiEvents;

import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.MCHungerGames.HGplugin;
import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.apiEvents.parents.ArenaModifyEvent;


public class ArenaGameChangeEvent extends ArenaModifyEvent{
    private boolean ingame;
    
    public ArenaGameChangeEvent(String arenakey, boolean ingame) {
        super(arenakey, YMLKey.getArenaSubkey(arenakey, YMLKey.ARN_INGAME), ingame);
        setInGame(ingame);
        if(ingame)
            HGplugin.callSubEvent(this, new GameStartEvent(arenakey));
        else
            HGplugin.callSubEvent(this, new GameStopEvent(arenakey));
    }
    
    public boolean getInGame(){
        return ingame;
    }
    
    public void setInGame(boolean ingame){
        this.ingame = ingame;
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