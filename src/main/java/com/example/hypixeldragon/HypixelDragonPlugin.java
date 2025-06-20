package com.example.hypixeldragon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HypixelDragonPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("HypixelDragon Plugin Enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawnhdrag") && sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();

            if (!world.getEnvironment().equals(World.Environment.THE_END)) {
                player.sendMessage("§cYou must be in The End to spawn the Hypixel-style dragon!");
                return true;
            }

            Location spawnLoc = player.getLocation();
            EnderDragon dragon = (EnderDragon) world.spawnEntity(spawnLoc, EntityType.ENDER_DRAGON);

            dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
            dragon.setHealth(1000);
            dragon.setCustomName("§5✯ Hypixel Dragon ✯");
            dragon.setCustomNameVisible(true);

            world.spawnParticle(Particle.SMOKE_LARGE, spawnLoc, 100, 2, 2, 2, 0.05);
            world.spawnParticle(Particle.TOTEM, spawnLoc, 60);

            player.sendMessage("§aSpawned Hypixel-style Ender Dragon!");
            return true;
        }
        return false;
    }
}
