package rip.lifesteal.core.features.lifesteal.items;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.utils.CoreItemUtils;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import rip.lifesteal.core.utils.HealthUtils;
import rip.lifesteal.core.utils.settings.Settings;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeartItem implements Listener {

    private static final NamespacedKey shapedKey = new NamespacedKey( CyberCore.getPlugin(), "shaped_heart_item" );
    private static final NamespacedKey shapelessKey = new NamespacedKey( CyberCore.getPlugin(), "shapeless_heart_item" );

    /**
     * @return The heart item
     */
    public static ItemStack getItem() {
        ItemStack item = CoreItemUtils.createItem( Settings.HEART_MATERIAL.material(), Settings.HEART_NAME.coloredString() );

        if ( Settings.HEART_ENCHANT_GLOW.bool() ) {
            item.addUnsafeEnchantment( Enchantment.DURABILITY, 1 );
            item = CoreItemUtils.addItemFlags( item, ItemFlag.HIDE_ENCHANTS );
        }

        return CoreItemUtils.setItemLore( item, Settings.HEART_LORE.coloredArraylist() );
    }

    /**
     * @return The (shaped) heart recipe for crafting
     */
    public static ShapedRecipe getShapedRecipe() {
        final ItemStack item = getItem();

        ShapedRecipe recipe = new ShapedRecipe( shapedKey, item );
        recipe.shape( "GDG", "DND", "GDG" ); // G = gold block, D = diamond block, N = netherite block
        recipe.setIngredient( 'G', Material.GOLD_BLOCK );
        recipe.setIngredient( 'D', Material.DIAMOND_BLOCK );
        recipe.setIngredient( 'N', Material.NETHERITE_BLOCK );
        return recipe;
    }

    /**
     * @return The (shapeless) heart recipe for crafting
     */
    public static ShapelessRecipe getShapelessRecipe() {
        final ItemStack item = getItem();

        ShapelessRecipe recipe = new ShapelessRecipe( shapelessKey, item );
        recipe.addIngredient( Settings.HALF_HEART_MATERIAL.material() );
        recipe.addIngredient( Settings.HALF_HEART_MATERIAL.material() );
        return recipe;
    }

    /**
     * Initializes both the crafting recipes for this item
     */
    public static void initializeRecipes() {
        CyberCore.getPlugin().getServer().addRecipe( getShapedRecipe() );
        CyberCore.getPlugin().getServer().addRecipe( getShapelessRecipe() );
    }

    /**
     * Delets the crafting recipes for this item
     */
    public static void deleteRecipes() {
        CyberCore.getPlugin().getServer().removeRecipe( shapedKey );
        CyberCore.getPlugin().getServer().removeRecipe( shapelessKey );
    }

    // Ensuring that the half hearts provided for crafting a full heart are valid
    @EventHandler
    public void onItemPrepareCraft( PrepareItemCraftEvent event ) {
        if ( event.getRecipe() == null ) { return; }

        if ( event.getRecipe() instanceof ShapelessRecipe ) {
            ShapelessRecipe recipe = ( ShapelessRecipe ) event.getRecipe();
            if ( recipe.getKey().equals( shapelessKey ) ) {

                List<ItemStack> items = Stream.of( event.getInventory().getMatrix() )
                        .filter( Objects::nonNull )
                        .collect( Collectors.toList() );
                if ( items.size() != 2 ) {
                    event.getInventory().setResult( null );
                    return;
                }

                for ( ItemStack item : items ) {
                    if ( HalfHeartItem.isHalfHeart( item ) == false ) {
                        event.getInventory().setResult( null );
                        return;
                    }
                }
            }
        }
    }

    // On right click event to consume the item
    @EventHandler
    public void onPlayerInteract( PlayerInteractEvent event ) {
        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR ) { return; }
        if ( event.getItem() == null || event.getItem().isSimilar( getItem() ) == false ) { return; }

        final Player player = event.getPlayer();
        final ItemStack itemClicked = event.getItem();
        final int currentMaxHealth = HealthUtils.getMaxHealth( player );

        if ( currentMaxHealth >= ( Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 ) ) {
            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou have reached the maximum health of &p" +
                    ( Math.round( Settings.LIFESTEAL_MAX_HEALTH.getFloat() / 2.0 ) ) + "&s hearts" );
        }

        else {
            itemClicked.setAmount( itemClicked.getAmount() - 1 );
            HealthUtils.addMaxHealth( player, 1 );

            CoreUtils.sendMsg( player, Settings.PREFIX.coloredString() + "&sYou have gained &p1&s heart. You now have &p" +
                    HealthUtils.getMaxHealth( player ) + " &shearts" );
            player.playSound( player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1, 1f );
        }
    }
}