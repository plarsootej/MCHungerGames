package com.acuddlyheadcrab.util;

/** 
 * @author acuddlyheadcrab
 *
 * This is where all the config paths should lead to. 
 * That way, when a key here is edited, all statements that contain one of these enums point to the right config path.
 * If you're modifying this plugin's config, make sure to add/remove/edit the corresponding enum here!
 */

public enum YMLKeys {
    
        UD_LASTLOGINTIME("LastLogin"),
        UD_INV("Inventory."),
            UD_INV_TIME(UD_INV.key()+"Time"),
            UD_INV_CONTENTS(UD_INV.key()+"Contents"),
            UD_INV_ARMOR(UD_INV.key()+"Armor"),
    
    CHESTITEMS_CHESTLOCS("Chest_Locs"),
    CHESTITEMS_ADDTO("Chest_reset_addto"),
    
    OPTIONS("Options."),
        OPS_DEBUG(OPTIONS.key().concat("Debug.")),
            OPS_DEBUG_ONCMD(OPS_DEBUG.key().concat("onCommand")),
            OPS_DEBUG_ONBLOCKCHANGE(OPS_DEBUG.key().concat("onBlockChange")),
            OPS_DEBUG_ONPLAYERJOIN(OPS_DEBUG.key().concat("onPlayerJoin")),
        OPS_GMCANEDIT(OPTIONS.key().concat("Gamemakers_can_edit")),
        OPS_BLOCKPROT(OPTIONS.key().concat("BlockProtection.")),
//         hehe.. mushleaf sounds funny
            OPS_BP_MUSHLEAF(OPS_BLOCKPROT.key().concat("InArenas")),
            OPS_BP_RULES(OPS_BLOCKPROT.key().concat("InArenas")),
                OPS_BP_RULES_BUILD(OPS_BLOCKPROT.key().concat("Building_enabled")),
                OPS_BP_RULES_BREAK(OPS_BLOCKPROT.key().concat("Breaking_enabled")),
                OPS_BP_RULES_CRAFT(OPS_BLOCKPROT.key().concat("Crafting_enabled")),
            OPS_BP_CONDIT(OPS_BLOCKPROT.key().concat("InArenas")),
                OPS_BP_CONDIT_INARENA(OPS_BLOCKPROT.key().concat("InArenas")),
                OPS_BP_CONDIT_INGAME(OPS_BLOCKPROT.key().concat("Only_while_ingame")),
                OPS_BP_CONDIT_OUTARENA(OPS_BLOCKPROT.key().concat("OutsideArenas")),
            OPS_BP_SHOWWARN(OPS_BLOCKPROT.key().concat("Show_warning_to_players")),
            
        OPS_NEARCHAT(OPTIONS.key().concat("NearbyTributeChat.")),
            OPS_NEARCHAT_ENABLED(OPS_NEARCHAT.key().concat("Enabled")),
            OPS_NEARCHAT_DISTANCES(OPS_NEARCHAT.key().concat("Distances.")),
                OPS_NEARCHAT_DISTS_CLEAR(OPS_NEARCHAT_DISTANCES.key().concat("Clear")),
                OPS_NEARCHAT_DISTS_DISEMBOIDED(OPS_NEARCHAT_DISTANCES.key().concat("Disembodied")),
                OPS_NEARCHAT_DISTS_GARBLED(OPS_NEARCHAT_DISTANCES.key().concat("Garbled")),
            
        OPS_ARENAOPS(OPTIONS.key().concat("Arena_Options.")),
            OPS_COUNTDOWNMSG(OPS_ARENAOPS.key().concat("Countdown_msg")),
            OPS_LOUNGETP(OPS_ARENAOPS.key().concat("TP_to_lounge.")),
                OPS_LOUNGETP_ONJOIN(OPS_LOUNGETP.key().concat("onTribJoin")),
                OPS_LOUNGETP_ONDEATH(OPS_LOUNGETP.key().concat("onTribDeath")),
                OPS_LOUNGETP_ONWIN(OPS_LOUNGETP.key().concat("onTribWin")),
            OPS_DURING_GAME(OPS_ARENAOPS.key().concat("During_games.")),
                OPS_DURGM_DISQUALONDISC(OPS_DURING_GAME.key().concat("disqualify_on_disconnect")),
                OPS_DURGM_NOTP(OPS_DURING_GAME.key().concat("noTeleporting")),
                OPS_DURGM_NOMOBS(OPS_DURING_GAME.key().concat("noMobSpawnInArena")),
                
    AUTO_GAMES(OPTIONS.key().concat("Auto_games.")),
        AG_ENABLED(AUTO_GAMES.key().concat("Enabled")),
        AG_AUTOJOIN(AUTO_GAMES.key().concat("Auto_join.")),
            AG_AUTOJOIN_ENABLED(AG_AUTOJOIN.key().concat("Enabled")),
            AG_AUTOJOIN_ARENA(AG_AUTOJOIN.key().concat("Arena")),
            AG_AUTOJOIN_JOINMSG(AG_AUTOJOIN.key().concat("Join_msg")),
        AG_STARTWHEN(AUTO_GAMES.key().concat("Start_when.")),
            AG_STARTWHEN_PLAYERCOUNT(AG_STARTWHEN.key().concat("Tribute_count")),
            AG_STARTWHEN_IDLEMINUTES(AG_STARTWHEN.key().concat("Idle_minutes")),
        AG_COUNTDOWN(AUTO_GAMES.key().concat("Countdown")),
            
    CURRENT_GAMES("Currentgames"),
    GAME_COUNT("Game_count"),
    
    ARENAS("Arenas."),
        ARN_SELF(""),
            ARN_CENTER_WRLD(".cornucopia.World"),
            ARN_CENTER_X(".cornucopia.x"),
            ARN_CENTER_Y(".cornucopia.y"),
            ARN_CENTER_Z(".cornucopia.z"),
            ARN_LOUNGE_WRLD(".lounge.World"),
            ARN_LOUNGE_X(".lounge.x"),
            ARN_LOUNGE_Y(".lounge.y"),
            ARN_LOUNGE_Z(".lounge.z"),
            ARN_LOUNGE_PITCH(".lounge.pitch"),
            ARN_LOUNGE_YAW(".lounge.yaw"),
            ARN_RADIUS(".Radius"),
            ARN_GMS(".gamemakers"),
            ARN_TRIBS(".tributes"),
            ARN_INGAME(".in_game"),
            ARN_INCOUNTDOWN(".in_countdown(DO_NOT_TOUCH)"),
            ARN_SPAWNLOCS(".SpawnLocations."),
                ARN_SPAWNLOCLIST(ARN_SPAWNLOCS.key().concat("List")),
                ARN_SPAWNLOC_ENABLED(ARN_SPAWNLOCS.key().concat("Enabled")),
    ;
    
    private String key;
    
    
    private YMLKeys(String configkey) {
        key = configkey;
    }
    
    public String key(){
        return key;
    }
    
    public void setKey(String newkey){
        key = newkey;
    }
    
    public static String getArenaSubkey(String arenakey, YMLKeys type){
        if(type==YMLKeys.ARN_SELF) return ARENAS.key()+arenakey;
        return YMLKeys.ARENAS.key()+arenakey+type.key();
    }
}
