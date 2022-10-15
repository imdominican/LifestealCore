package rip.lifesteal.core.features.lifesteal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import rip.lifesteal.core.LifestealCore;

public class TotemEvent implements Listener {

    @EventHandler
    public void onTotem(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            LifestealCore.totem.add((Player) event.getEntity());
            Bukkit.getScheduler().runTaskLater(LifestealCore.getPlugin(LifestealCore.class), () -> {
                    LifestealCore.totem.remove((Player) event.getEntity());
            }, 30L);
        }
    }

}