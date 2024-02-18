package customKits.customkits.Events;

import customKits.customkits.manager.UpdateManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static customKits.customkits.manager.UpdateManager.nyVersion;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.sendMessage("Nyeste version her!");
        if(UpdateManager.isNewUpdateAvailable()){
            if(event.getPlayer().isOp()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fFandt en ny version af CustomKits v" + nyVersion));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ahttps://github.com/TixDK/CustomKits/releases/latest/download/CustomKits.jar"));
            }
        }
    }
}
