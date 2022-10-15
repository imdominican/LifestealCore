package rip.lifesteal.core.features.lifesteal;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.features.lifesteal.items.HalfHeartItem;
import rip.lifesteal.core.features.lifesteal.items.HeartItem;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class LifestealUtils {

    public static void giveHearts( Player target, int amount ) {
        ItemStack hearts = HeartItem.getItem();
        hearts.setAmount( amount );

        // Note: the amount of the item stacks returned in the hashmap are the amount of hearts that couldn't fit
        Map<Integer, ItemStack> itemsNoFit = target.getInventory().addItem( hearts );
        if ( itemsNoFit.size() != 0 ) {
            for ( ItemStack item : itemsNoFit.values() ) {
                target.getWorld().dropItemNaturally( target.getLocation(), item );
            }

            CoreUtils.sendMsg( target, Settings.PREFIX.coloredString() + "&sYou received &p" + amount +
                    "x &shearts, but your inventory is/became full, so some items were dropped on the ground!" );
        }

        else {
            CoreUtils.sendMsg( target, Settings.PREFIX.coloredString() + "&sYou received &p" + amount + "x &shearts" );
        }
    }

    public static void giveHalfHearts( Player target, int amount ) {
        ItemStack halfHearts = HalfHeartItem.getItem();
        halfHearts.setAmount( amount );

        Map<Integer, ItemStack> itemsNoFit = target.getInventory().addItem( halfHearts );
        if ( itemsNoFit.size() != 0 ) {
            for ( ItemStack item : itemsNoFit.values() ) {
                target.getWorld().dropItemNaturally( target.getLocation(), item );
            }

            CoreUtils.sendMsg( target, Settings.PREFIX.coloredString() + "&sYou received &p" + amount +
                    "x &shalf hearts, but your inventory is/became full, so some items were dropped on the ground!" );
        }

        else {
            CoreUtils.sendMsg( target, Settings.PREFIX.coloredString() + "&sYou received &p" + amount + "x &shalf hearts" );
        }
    }
}