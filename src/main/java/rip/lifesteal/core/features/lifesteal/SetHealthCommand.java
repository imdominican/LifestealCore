package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.helpers.command.ArgType;
import com.github.cyberryan1.cybercore.helpers.command.CyberCommand;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.HealthUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHealthCommand extends CyberCommand {

    public SetHealthCommand() {
        super(
                "sethealth",
                Settings.SET_HEALTH_PERMISSION.string(),
                Settings.PERMS_DENIED.coloredString(),
                Settings.PREFIX.coloredString() + "&sUsage: &p/sethealth (player) (health)"
        );
        register( true );

        setDemandPermission( true );
        setMinArgs( 2 );
        setArgType( 0, ArgType.ONLINE_PLAYER );
        setArgType( 1, ArgType.INTEGER );
    }

    @Override
    public List<String> tabComplete( CommandSender sender, String args[] ) {
        return List.of();
    }

    @Override
    public boolean execute( CommandSender sender, String args[] ) {
        final Player player = Bukkit.getPlayer( args[0] );
        final int newHealth = Integer.parseInt( args[1] );

        if ( newHealth < Settings.LIFESTEAL_MIN_HEALTH.getFloat() / 2.0 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't set a player's health to less than " +
                    Settings.LIFESTEAL_MIN_HEALTH.getFloat() / 2.0 + " hearts" );
        }

        else if ( newHealth > Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't set a player's health to more than " +
                    Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 + " hearts" );
        }

        else {
            HealthUtils.setMaxHealth( player, newHealth );
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou set &p" + player.getName() + "&s's health to &p" + newHealth );
        }

        return true;
    }

    @Override
    public void sendInvalidDoubleArg( CommandSender sender, String arg ) {
        CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou must give a decimal number of hearts" );
    }
}