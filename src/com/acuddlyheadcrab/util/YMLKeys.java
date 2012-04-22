package com.acuddlyheadcrab.util;

/** 
 * @author acuddlyheadcrab
 *
 * This is where all the config paths should lead to. 
 * That way, when a key here is edited, all statements that contain one of these enums point to the right config path.
 * If you're modifying this plugin's config, make sure to add/remove/edit the corresponding enum here!
 */

public enum YMLKeys {
    
    OPTIONS("Options."),
        OPS_DEBUG(OPTIONS.key().concat("Debug.")),
            OPS_DEBUG_ONCMD(OPS_DEBUG.key().concat("onCommand")),
            OPS_DEBUG_ONBLOCKCHANGE(OPS_DEBUG.key().concat("onBlockChange")),
        OPS_GMCANEDIT(OPTIONS.key().concat("Gamemakers_can_edit")),
        OPS_BLOCKPROT(OPTIONS.key().concat("BlockProtection.")),
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
            OPS_DURING_GAME(OPS_ARENAOPS.key().concat("During_games.")),
                OPS_DURGM_DISQUALONDISC(OPS_DURING_GAME.key().concat("disqualify_on_disconnect")),
                OPS_DURGM_NOTP(OPS_DURING_GAME.key().concat("noTeleporting")),
                OPS_DURGM_NOMOBS(OPS_DURING_GAME.key().concat("noMobSpawnInArena")),
            
    CURRENT_GAMES("Currentgames"),
    GAME_COUNT("Game_count"),
    ARENAS("Arenas."),
            ARN_CENTER_WRLD(".cornucopia.World"),
            ARN_CENTER_X(".cornucopia.x"),
            ARN_CENTER_Y(".cornucopia.y"),
            ARN_CENTER_Z(".cornucopia.z"),
            ARN_RADIUS(".Radius"),
            ARN_GMS(".gamemakers"),
            ARN_TRIBS(".tributes"),
            ARN_INGAME(".in_game"),
            ARN_INCOUNTDOWN(".in_countdown(DO_NOT_TOUCH)")
    ;
    
    private final String key;
    
    
    private YMLKeys(String configkey) {
        key = configkey;
    }
    
    public String key(){
        return key;
    }
}
