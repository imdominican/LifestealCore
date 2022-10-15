package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.helpers.command.ArgType;
import com.github.cyberryan1.cybercore.helpers.command.CyberCommand;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveHalfHeartCommand extends CyberCommand {

    public GiveHalfHeartCommand() {
        super(
                "givehalf",
                Settings.GIVE_HALF_PERMISSION.string(),
                Settings.PERMS_DENIED.coloredString(),
                Settings.PREFIX.coloredString() + "&sUsage: &p/givehalf (player) (amount)"
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
        final Player target = Bukkit.getPlayer( args[0] );
        final int amount = Integer.parseInt( args[1] );

        if ( amount <= 0 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't give less than one half heart" );
        }

        else if ( amount > 64 ) {
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou can't give more than a stack of half hearts at a time" );
        }

        else {
            LifestealUtils.giveHalfHearts( target, amount );
            CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou gave &p" + amount + "x &shalf hearts to &p" + target.getName() );
        }

        return true;
    }

    @Override
    public void sendInvalidIntegerArg( CommandSender sender, String arg ) {
        CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&sYou must give an integer number of hearts" );
    }
}