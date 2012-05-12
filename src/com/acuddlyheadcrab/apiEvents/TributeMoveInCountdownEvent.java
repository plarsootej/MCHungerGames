package com.acuddlyheadcrab.apiEvents;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;


public class TributeMoveInCountdownEvent extends PlayerMoveEvent{
    private static final HandlerList handlers = new HandlerList();
    private Location to;
    private Location from;
    private String arenakey;
    
    public TributeMoveInCountdownEvent(final Player player, final Location from, final Location to, final String  arenakey){
        super(player, from, to);
        setFrom(from);
        setTo(to);
        setArenakey(arenakey);
    }
    
    public Location getArenaFrom(){
        return from;
    }
    
    public void setArenaFrom(Location from){
        this.from = from;
    }
    
    @Override
    public Location getTo(){
        return to;
    }
    
    @Override
    public void setTo(Location to){
        this.to = to;
    }
    
    public String getArenakey(){
        return arenakey;
    }
    
    public void setArenakey(String arenakey){
        this.arenakey = arenakey;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
}