package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.helpers.command.ArgType;
import com.github.cyberryan1.cybercore.helpers.command.CyberCommand;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.HealthUtils;
import rip.lifesteal.core.utils.Utils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HeartPayCommand extends CyberCommand {

    private Map<UUID, Long> cooldown = new HashMap<>();

    public HeartPayCommand() {
        super(
                "payheart",
                Settings.HEART_PAY_PERMISSION.string(),
                Settings.PERMS_DENIED.coloredString(),
                Settings.PREFIX.coloredString() + "&sUsage: &p/heartpay (player) (amount)"
        );
        register( true );

        setDemandPlayer( true );
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
        final Player player = ( Player ) sender;
        final Player target = Bukkit.getPlayer( args[0] );
        final int amount = Integer.parseInt( args[1] );

        if ( cooldown.containsKey( player.getUniqueId() ) ) {
            final long TIME_REMAIN = 5 - ( Utils.getCurrentTimestamp() - cooldown.get( player.getUniqueId() ) );
            if ( TIME_REMAIN > 0 ) {
                final String timeRemaining = ( TIME_REMAIN == 1 ) ? ( "1 second" ) : ( TIME_REMAIN + " seconds" );
                CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sPlease wait &p" + timeRemaining +
                        " &sbefore using this command again" );
                return true;
            }

            cooldown.remove( player.getUniqueId() );
        }

        if ( target.getUniqueId().equals( player.getUniqueId() ) ) {
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou cannot pay yourself" );
        }

        else if ( amount < 1 ) {
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou must pay at least 1 heart" );
        }

        else if ( HealthUtils.getMaxHealth( target ) >= 30 ) {
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&p" + target.getName() +
                    " &sis already at max hearts and cannot receive more" );
        }

        else if ( HealthUtils.getMaxHealth( target ) + amount > 30 ) {
            final int MAX_CAN_ADD = 30 - HealthUtils.getMaxHealth( target );
            String amountString = ( MAX_CAN_ADD == 1 ) ? ( "1 heart" ) : ( MAX_CAN_ADD + " hearts" );
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou cannot give more than &p" +
                    amountString + " &sto &p" + target.getName() );
        }

        else if ( HealthUtils.getMaxHealth( player ) - amount < .5 ) {
            String amountString = ( amount == 1 ) ? ( "1 heart" ) : ( amount + " hearts" );
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou do not have enough hearts to pay" +
                    " &p" + amountString + " &sto &p" + target.getName() );
        }

        else {
            HealthUtils.addMaxHealth( target, amount );
            HealthUtils.removeMaxHealth( player, amount );

            target.playSound( target.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1 );
            player.playSound( player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1 );

            String amountString = ( amount == 1 ) ? ( "1 heart" ) : ( amount + " hearts" );
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou sent &p" + amountString + " &sto &p" + target.getName() );
            CoreUtils.sendMsg( target, Settings.PREFIX.coloredString() + "&sYou received &p" + amountString + " &sfrom &p" + player.getName() );
            cooldown.put( player.getUniqueId(), Utils.getCurrentTimestamp() );
        }

        return true;
    }
}
