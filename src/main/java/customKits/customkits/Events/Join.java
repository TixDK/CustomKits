package customKits.customkits.Events;

import customKits.customkits.manager.UpdateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

import static customKits.customkits.Extra.stopDrag.plugin;
import static customKits.customkits.manager.UpdateManager.nyVersion;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(UpdateManager.isNewUpdateAvailable()){
            if(event.getPlayer().isOp() && plugin.getConfig().getBoolean("update-message")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fNew version is available v" + nyVersion));
            }
        }
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.contains("language")) {
            if (player.isOp()) {
                Bukkit.getLogger().info("[CustomKits] PROBLEM!!!!!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fNew update to the config file, you need to reload &6/ckit reload"));
            }
        } else {
            Bukkit.getLogger().info("[CustomKits] No problem with the config file!!!!!");
        }
    }
}
