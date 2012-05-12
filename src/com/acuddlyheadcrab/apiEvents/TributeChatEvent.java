package com.acuddlyheadcrab.apiEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChatEvent;


public class TributeChatEvent extends PlayerChatEvent{
    private String arenakey;
    
    
    public TributeChatEvent(Player player, String message, String arenakey) {
        super(player, message);
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
}