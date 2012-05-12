package com.acuddlyheadcrab.apiEvents.parents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;


public interface ArenaPlayerEvent extends Cancellable{
    
    public Player getPlayer();
    
    public void setPlayer(Player player);
    
}