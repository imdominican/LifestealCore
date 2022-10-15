package rip.lifesteal.core.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class HealthUtils {

    /**
     * Gets the player's maximum current health
     * @param player The player to get the maximum health of
     * @return The player's maximum hearts
     */
    public static int getMaxHealth( Player player ) {
        return ( int ) ( player.getAttribute( Attribute.GENERIC_MAX_HEALTH ).getValue() / 2 );
    }

    /**
     * Sets the player's maximum health
     * @param player The player to set the maximum health of
     * @param health The maximum amount of hearts
     */
    public static void setMaxHealth( Player player, int health ) {
        player.getAttribute( Attribute.GENERIC_MAX_HEALTH ).setBaseValue( health * 2.0 );
    }

    /**
     * Adds to the player's maximum health
     * @param player The player to add to the maximum health of
     * @param change The amount of hearts to add
     */
    public static void addMaxHealth( Player player, int change ) {
        setMaxHealth( player, getMaxHealth( player ) + change );
    }

    /**
     * Removes from the player's maximum health
     * @param player The player to remove from the maximum health of
     * @param change The amount of hearts to remove
     */
    public static void removeMaxHealth( Player player, int change ) {
        setMaxHealth( player, getMaxHealth( player ) - change );
    }
}