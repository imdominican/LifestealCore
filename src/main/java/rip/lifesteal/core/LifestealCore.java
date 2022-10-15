package rip.lifesteal.core;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreColorUtils;
import com.github.cyberryan1.cybercore.utils.VaultUtils;
import org.bukkit.entity.Player;
import rip.lifesteal.core.features.lifesteal.deluxecombat.DeluxeCombatEvents;
import rip.lifesteal.core.features.lifesteal.items.HeartItem;
import rip.lifesteal.core.features.lifesteal.papi.MaxHeartsExpansion;
import rip.lifesteal.core.utils.Utils;
import rip.lifesteal.core.utils.settings.Settings;
import rip.lifesteal.core.utils.yml.YMLUtils;
import nl.marido.deluxecombat.api.DeluxeCombatAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.lifesteal.core.features.lifesteal.*;

import java.util.ArrayList;
import java.util.List;

public final class LifestealCore extends JavaPlugin {

    public static List<Player> totem = new ArrayList<>();

    @Override
    public void onEnable() {
        // Initialize CyberCore and VaultUtils
        CyberCore.setPlugin( this );
        new VaultUtils();

        // Update/reload config files
        YMLUtils.initializeConfigs();

        // Setting primary and secondary colors
        CyberCore.setPrimaryColor( CoreColorUtils.getColored( Settings.PRIMARY_COLOR.string() ) );
        CyberCore.setSecondaryColor( CoreColorUtils.getColored( Settings.SECONDARY_COLOR.string() ) );

        initializeCommands();
        initializeEvents();
        initializeOther();
    }

    @Override
    public void onDisable() {
        // Delete the Heart Item recipes
        HeartItem.deleteRecipes();
        // Save the DeluxeCombat data
        DeluxeCombatEvents.save();
    }

    private void initializeCommands() {
        new GiveHeartCommand();
        new GiveHalfHeartCommand();
        new SetHealthCommand();
        new ViewHealthCommand();
        new HeartPayCommand();
        new HealthAllCommand();
    }

    private void initializeEvents() {
        this.getServer().getPluginManager().registerEvents( new HeartItem(), this );
        this.getServer().getPluginManager().registerEvents( new LifestealEvents(), this );
        this.getServer().getPluginManager().registerEvents( new DeluxeCombatEvents(), this );
    }

    private void initializeOther() {
        // Initialize the Heart Item recipes
        HeartItem.initializeRecipes();

        // Register the PlaceholderAPI expansion
        if ( Bukkit.getPluginManager().getPlugin( "PlaceholderAPI" ) != null ) {
            new MaxHeartsExpansion().register();
        }

        // Register the DeluxeCombatAPI
        if ( Bukkit.getPluginManager().getPlugin( "DeluxeCombat" ) != null ) {
            Utils.setCombatApi( new DeluxeCombatAPI() );
        }
        DeluxeCombatEvents.initialize();
    }
}