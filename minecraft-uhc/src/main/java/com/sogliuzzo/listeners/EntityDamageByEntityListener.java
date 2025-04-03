package com.sogliuzzo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sogliuzzo.Main;
import com.sogliuzzo.utils.GameStates;

public class EntityDamageByEntityListener implements Listener {
    private Main main;

    public EntityDamageByEntityListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Disabling PvP at the beginning of the game.
        if (main.gameState == GameStates.STARTED && event.getEntity() instanceof Player
                && event.getDamager() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
