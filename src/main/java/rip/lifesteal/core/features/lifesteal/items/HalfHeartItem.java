package rip.lifesteal.core.features.lifesteal.items;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreItemUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * Reresents the half heart item
 */
public class HalfHeartItem {

    private static final NamespacedKey itemKey = new NamespacedKey( CyberCore.getPlugin(), "half_heart_item" );

    /**
     * @return The half heart item
     */
    public static ItemStack getItem() {
        ItemStack item = CoreItemUtils.createItem( Settings.HALF_HEART_MATERIAL.material(), Settings.HALF_HEART_NAME.coloredString() );
        item = CoreItemUtils.setItemLore( item, Settings.HALF_HEART_LORE.coloredArraylist() );
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set( itemKey, PersistentDataType.BYTE, ( byte ) 1 );
        item.setItemMeta( meta );

        if ( Settings.HALF_HEART_ENCHANT_GLOW.bool() ) {
            item.addUnsafeEnchantment( Enchantment.DURABILITY, 1 );
            item = CoreItemUtils.addItemFlags( item, ItemFlag.HIDE_ENCHANTS );
        }
        return item;
    }

    /**
     * @param item The item to check
     * @return True if the item provided has the same NamespacedKey as a standard half heart item, false otherwise
     */
    public static boolean isHalfHeart( ItemStack item ) {
        if ( item == null ) { return false; }
        if ( item.getItemMeta() == null ) { return false; }
        return item.getItemMeta().getPersistentDataContainer().has( itemKey, PersistentDataType.BYTE );
    }
}