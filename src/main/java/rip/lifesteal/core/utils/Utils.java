package rip.lifesteal.core.utils;

import nl.marido.deluxecombat.api.DeluxeCombatAPI;

public class Utils {

    /**
     * @return The current timestamp, in seconds.
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    /*
     * Manages the DeluxeCombatAPI instance
     */
    private static DeluxeCombatAPI combatApi = null;
    public static void setCombatApi( DeluxeCombatAPI api ) {
        combatApi = api;
    }
    public static DeluxeCombatAPI getCombatApi() {
        return combatApi;
    }
}