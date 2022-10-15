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

public class ViewHealthCommand extends CyberCommand {

    public ViewHealthCommand() {
        super(
                "viewhealth",
                Settings.VIEW_HEALTH_PERMISSION.string(),
                Settings.PERMS_DENIED.coloredString(),
                Settings.PREFIX.coloredString() + "&sUsage: &p/viewhealth (player)"
        );
        register( true );

        setDemandPlayer( true );
        setDemandPermission( true );
        setMinArgs( 0 );
        setArgType( 0, ArgType.ONLINE_PLAYER );
    }

    @Override
    public List<String> tabComplete( CommandSender sender, String args[] ) {
        return List.of();
    }

    @Override
    public boolean execute( CommandSender sender, String args[] ) {
        Player target = ( Player ) sender;
        if ( args.length >= 1 ) { target = Bukkit.getPlayer( args[0] ); }
        final int maxHealth = HealthUtils.getMaxHealth( target );

        CoreUtils.sendMsg( sender, Settings.PREFIX.coloredString() + "&p" + target.getName() +
                " &scurrently has a maximum health of &p" + maxHealth + " &shearts" );

        return true;
    }
}