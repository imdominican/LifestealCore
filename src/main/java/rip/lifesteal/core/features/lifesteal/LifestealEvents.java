package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.LifestealCore;
import rip.lifesteal.core.utils.HealthUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LifestealEvents implements Listener {

    @EventHandler( priority = EventPriority.MONITOR )
    public void onEntityDamageByEntity( EntityDamageByEntityEvent event ) {
        if ( event.isCancelled() ) { return; }
        if ( event.getEntity() instanceof Player == false ) { return; }

        final Player victim = ( Player ) event.getEntity();

        if(LifestealCore.totem.contains(victim)) return;

        if ( victim.getHealth() - event.getFinalDamage() <= 0 ) {
            Player attacker = null;
            if ( event.getDamager() instanceof Player ) { attacker = ( Player ) event.getDamager(); }
            else if ( event.getDamager() instanceof Projectile ) {
                Projectile proj = ( Projectile ) event.getDamager();
                if ( proj.getShooter() instanceof Player ) { attacker = ( Player ) proj.getShooter(); }
            }

            if ( attacker == null || attacker.getGameMode() == GameMode.CREATIVE || attacker.getUniqueId().equals( victim.getUniqueId() ) ) { return; }

            if ( HealthUtils.getMaxHealth( victim ) <= Settings.LIFESTEAL_MIN_HEALTH.getFloat() / 2.0 ) {
                CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() + "&p" + victim.getName() + " &shas no more hearts to steal" );
            }

            else if ( HealthUtils.getMaxHealth( attacker ) >= Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 ) {
                CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() +
                        "&sYou are at the maximum amount of hearts, so you didn't gain any hearts" );
                CoreUtils.sendMsg( victim, Settings.PREFIX.coloredString() +
                        "&sYour attacker is at max health, so they didn't steal any hearts from you" );
            }

            else {
                HealthUtils.addMaxHealth( attacker, 1 );
                HealthUtils.removeMaxHealth( victim, 1 );

                CoreUtils.sendMsg( attacker, Settings.PREFIX.coloredString() + "&sYou killed &p" + victim.getName() +
                        " &sand gained &p1 heart" );
                CoreUtils.sendMsg( victim, Settings.PREFIX.coloredString() + "&sYou were killed by &p" + attacker.getName() +
                        " &sand lost &p1 heart" );
            }
        }
    }
}