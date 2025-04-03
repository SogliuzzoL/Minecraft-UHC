package com.sogliuzzo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.sogliuzzo.Main;

public class EntityDamageListener implements Listener {
    private Main main;

    public EntityDamageListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        // Invincibilité
        if (event.getEntity() instanceof Player && main.time < main.invincibleTime) {
            event.setCancelled(true);
        }
    }

}
