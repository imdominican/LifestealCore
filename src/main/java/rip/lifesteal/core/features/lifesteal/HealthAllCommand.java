package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.helpers.command.ArgType;
import com.github.cyberryan1.cybercore.helpers.command.CyberCommand;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HealthAllCommand extends CyberCommand {

    public HealthAllCommand() {
        super(
                "healthall",
                Settings.HEALTH_ALL_PERMISSION.string(),
                Settings.PERMS_DENIED.coloredString(),
                Settings.PREFIX.coloredString() + "&sUsage: &p/healthall (amount)"
        );
        register( true );

        setDemandPermission( true );
        setMinArgs( 1 );
        setArgType( 0, ArgType.INTEGER );
    }

    @Override
    public List<String> tabComplete( CommandSender sender, String args[] ) {
        return List.of();
    }

    @Override
    public boolean execute( CommandSender sender, String args[] ) {
        final int amount = Integer.parseInt( args[0] );

        if ( amount <= 0 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't give less than one heart" );
        }

        else if ( amount > 64 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't give more than a stack of hearts at a time" );
        }

        else {
            String amountString = ( amount == 1 ) ? ( "1 heart" ) : ( amount + " hearts" );
            CoreUtils.broadcast(
                    "",
                    Settings.PREFIX.coloredString() + "&sEveryone will receive &p" + amountString + "&s in 10 seconds! Make sure there's room in your inventory",
                    ""
            );

            for ( Player player : Bukkit.getOnlinePlayers() ) {
                player.playSound( player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, .1f );
            }

            Bukkit.getScheduler().runTaskLater( CyberCore.getPlugin(), () -> {
                for ( Player player : Bukkit.getOnlinePlayers() ) {
                    LifestealUtils.giveHearts( player, amount );
                    player.playSound( player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, .1f );
                }

                CoreUtils.broadcast(
                        "",
                        Settings.PREFIX.coloredString() + "&sEveryone received &p" + amountString,
                        ""
                );
            }, 200L );
        }

        return true;
    }
}