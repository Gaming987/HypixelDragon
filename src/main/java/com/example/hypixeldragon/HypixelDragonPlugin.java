
package com.example.hypixeldragon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HypixelDragonPlugin extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("spawnhdrag").setExecutor(this);
        getLogger().info("HypixelDragon Plugin Enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("HypixelDragon Plugin Disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnhdrag")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("hypixeldragon.spawn")) {
                player.sendMessage("You don't have permission.");
                return true;
            }

            World world = player.getWorld();
            Location loc = world.getHighestBlockAt(player.getLocation()).getLocation().add(0, 5, 0);

            EnderDragon dragon = world.spawn(loc, EnderDragon.class);
            dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300.0);
            dragon.setHealth(300.0);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (dragon.isDead()) {
                        cancel();
                        return;
                    }
                    world.spawnParticle(Particle.DRAGON_BREATH, dragon.getLocation(), 100, 1, 1, 1, 0.1);
                    world.spawnParticle(Particle.SMOKE_LARGE, dragon.getLocation(), 50, 1, 1, 1, 0.05);
                    world.playSound(dragon.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.5f, 1.0f);
                }
            }.runTaskTimer(this, 0L, 40L);

            player.sendMessage("§6The Hypixel-style Ender Dragon has spawned!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) return;

        Location loc = event.getEntity().getLocation();
        World world = loc.getWorld();

        world.dropItemNaturally(loc, new ItemStack(Material.DRAGON_EGG, 1));
        world.dropItemNaturally(loc, new ItemStack(Material.ELYTRA, 1));
        world.dropItemNaturally(loc, new ItemStack(Material.NETHER_STAR, 2));

        world.spawnParticle(Particle.TOTEM, loc, 100, 1, 1, 1, 0.5);
        world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
    }
}
