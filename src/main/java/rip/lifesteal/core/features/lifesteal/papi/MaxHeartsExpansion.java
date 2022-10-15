package rip.lifesteal.core.features.lifesteal.papi;

import rip.lifesteal.core.utils.HealthUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class MaxHeartsExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() { return "CyberRyan"; }

    @Override
    public @NotNull String getIdentifier() { return "LifestealCore"; }

    @Override
    public @NotNull String getVersion() { return "1.0.0"; }

    @Override
    public boolean persist() { return true; }

    @Override
    public String onRequest( OfflinePlayer player, @NotNull String params ) {
        if ( player.isOnline() == false ) { return ""; }
        if ( params.equalsIgnoreCase( "maxhearts" ) ) {
            return HealthUtils.getMaxHealth( player.getPlayer() ) + "";
        }

        return null;
    }
}
