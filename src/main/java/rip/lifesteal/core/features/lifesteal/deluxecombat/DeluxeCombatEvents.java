package rip.lifesteal.core.features.lifesteal.deluxecombat;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.HealthUtils;
import rip.lifesteal.core.utils.Utils;
import rip.lifesteal.core.utils.settings.Settings;
import rip.lifesteal.core.utils.yml.YMLUtils;
import nl.marido.deluxecombat.events.CombatlogEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeluxeCombatEvents implements Listener {

    private Map<OfflinePlayer, Long> combatLoggers = new HashMap<>();
    private Map<OfflinePlayer, Integer> health = new HashMap<>();

    private static List<String> offlineKilled = new ArrayList<>();

    public static void initialize() {
        if ( YMLUtils.getData().get( "offline-killed" ) == null ) { return; }
        offlineKilled = ( List<String> ) YMLUtils.getData().get( "offline-killed" );
    }

    public static void save() {
        YMLUtils.getData().set( "offline-killed", offlineKilled );
        YMLUtils.getData().save();
    }

    public static List<String> getOfflineKilled() {
        return offlineKilled;
    }

    @EventHandler
    public void onCombatLogEvent( CombatlogEvent event ) {
        combatLoggers.put( event.getCombatlogger(), Utils.getCurrentTimestamp() );
        health.put( event.getCombatlogger(), HealthUtils.getMaxHealth( event.getCombatlogger() ) );
    }

    @EventHandler
    public void onEntityDeathEvent( EntityDeathEvent event ) {
        if ( event.getEntity() instanceof Villager == false ) { return; }
        Villager entity = ( Villager ) event.getEntity();

        removeOldEntries();
        for ( OfflinePlayer key : combatLoggers.keySet() ) {
            if ( key.getName().equals( entity.getCustomName() ) ) {
                final Player attacker = entity.getKiller();

                if ( health.get( key ) <= Settings.LIFESTEAL_MIN_HEALTH.getFloat() / 2.0 ) {
                    CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() + "&p" + key.getName() + " &shas no more hearts to steal" );
                }

                else if ( HealthUtils.getMaxHealth( attacker ) >= Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 ) {
                    CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() +
                            "&sYou are at the maximum amount of hearts, so you didn't gain any hearts" );
                }

                else {
                    HealthUtils.addMaxHealth( attacker, 1 );
                    CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() + "&sYou killed &p" + key.getName() +
                            " &sand gained &p1 heart" );

                    offlineKilled.add( key.getUniqueId().toString() );
                }

                combatLoggers.remove( key );
                health.remove( key );
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event ) {
        if ( offlineKilled.contains( event.getPlayer().getUniqueId().toString() ) ) {
            HealthUtils.removeMaxHealth( event.getPlayer(), 1 );
            offlineKilled.remove( event.getPlayer().getUniqueId().toString() );

            Bukkit.getScheduler().runTaskLaterAsynchronously( CyberCore.getPlugin(), () -> {
                CoreUtils.sendMsg( event.getPlayer(), Settings.PREFIX.coloredString() + "&sYou were killed while offline, so you lost a heart" );
            }, Settings.COMBAT_MSG_DELAY.integer() * 20L );
        }
    }

    private void removeOldEntries() {
        final List<OfflinePlayer> keys = new ArrayList<>( combatLoggers.keySet() );
        for ( int index = keys.size() - 1; index >= 0; index-- ) {
            if ( Utils.getCurrentTimestamp() - combatLoggers.get( keys.get( index ) ) > Settings.COMBAT_LOG_TIME.integer() ) {
                combatLoggers.remove( keys.get( index ) );
                health.remove( keys.get( index ) );
            }
        }
    }
}