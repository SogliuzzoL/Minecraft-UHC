package com.sogliuzzo.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.sogliuzzo.Main;

public class BlockBreakListener implements Listener {

    private Main main;

    public BlockBreakListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemStack = new ItemStack(Material.AIR);
        Material playerItem = player.getItemInHand().getType();
        Random random = new Random();

        switch (block.getType()) {
            case IRON_ORE:
                if (!(playerItem == Material.STONE_PICKAXE || playerItem == Material.IRON_PICKAXE
                        || playerItem == Material.GOLD_PICKAXE || playerItem == Material.DIAMOND_PICKAXE)) {
                    return;
                }
                itemStack.setType(Material.IRON_INGOT);
                break;

            case GOLD_ORE:
                if (!(playerItem == Material.IRON_PICKAXE || playerItem == Material.DIAMOND_PICKAXE)) {
                    return;
                }
                itemStack.setType(Material.GOLD_INGOT);
                break;

            default:
                return;
        }

        main.world.dropItemNaturally(block.getLocation(), itemStack);
        player.giveExp(5);
        main.world.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, (float) random.nextDouble());
        block.setType(Material.AIR);
        event.setCancelled(true);
    }
}
