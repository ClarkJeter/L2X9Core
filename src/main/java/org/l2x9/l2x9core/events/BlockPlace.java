package org.l2x9.l2x9core.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.l2x9.l2x9core.Main;
import org.l2x9.l2x9core.util.Utils;

public class BlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (Main.getPlugin().getConfigBoolean("IllegalBlock-Place.Enabled")) {
            switch (event.getBlock().getType()) {
                case BEDROCK:
                    event.setCancelled(true);
                    Utils.sendMessage(player, Main.getPlugin().getConfig().getString("IllegalBlock-Place.Bedrock"));
                    event.getPlayer().getInventory().getItemInMainHand().setType(null);
                    alertDiscord(player, event.getBlock());
                    break;
                case ENDER_PORTAL_FRAME:
                    if (!(player.getInventory().getItemInMainHand().getType() == Material.EYE_OF_ENDER) || (!(player.getInventory().getItemInOffHand().getType() == Material.EYE_OF_ENDER))) {
                        event.setCancelled(true);
                        Utils.sendMessage(player, Main.getPlugin().getConfig().getString("IllegalBlock-Place.Bedrock.End_Portal_Frame"));
                        event.getPlayer().getInventory().getItemInMainHand().setType(null);
                        alertDiscord(player, event.getBlock());
                    }
                    break;
                case BARRIER:
                    event.setCancelled(true);
                    Utils.sendMessage(player, Main.getPlugin().getConfig().getString("IllegalBlock-Place.Barrier"));
                    event.getPlayer().getInventory().getItemInMainHand().setType(null);
                    alertDiscord(player, event.getBlock());
                    break;
                case MOB_SPAWNER:
                    event.setCancelled(true);
                    Utils.sendMessage(player, Main.getPlugin().getConfig().getString("IllegalBlock-Place.Mob_Spawner"));
                    event.getPlayer().getInventory().getItemInMainHand().setType(null);
                    alertDiscord(player, event.getBlock());
                    break;
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.BEDROCK) {
            event.setCancelled(true);
        }
    }

    private void alertDiscord(Player player, Block block) {
        if (Main.getPlugin().discordWebhook.alertsEnabled()) {
            if (Main.getPlugin().getConfigBoolean("AlertSystem.IllegalBlockPlace")) {
                Main.getPlugin().discordWebhook.setContent(Main.getPlugin().getPingRole() + "[IllegalBlockPlaceAttempt] block type " + block.getType() + " by " + player.getName());
                Main.getPlugin().discordWebhook.execute();
            }
        }
    }
}