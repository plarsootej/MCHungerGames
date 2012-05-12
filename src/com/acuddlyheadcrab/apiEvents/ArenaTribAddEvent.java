package com.acuddlyheadcrab.apiEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.acuddlyheadcrab.MCHungerGames.FileIO.YMLKey;
import com.acuddlyheadcrab.apiEvents.parents.ArenaModifyEvent;
import com.acuddlyheadcrab.apiEvents.parents.ArenaPlayerEvent;


public class ArenaTribAddEvent extends ArenaModifyEvent implements ArenaPlayerEvent{
    private Player new_trib;
    
    public ArenaTribAddEvent(String arenakey, Player new_trib) {
        super(arenakey, YMLKey.getArenaSubkey(arenakey, YMLKey.ARN_TRIBS), new_trib);
        setPlayer(new_trib);
    }
    
    @Override
    public Player getPlayer(){
        return new_trib;
    }
    
    @Override
    public void setPlayer(Player player){
        this.new_trib = player;
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