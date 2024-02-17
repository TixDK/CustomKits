package customKits.customkits.Events;

import customKits.customkits.manager.UpdateManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(UpdateManager.isNewUpdateAvailable()){
            if(event.getPlayer().isOp()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fDer er en ny version skriv &6/ckit update"));
            }
        }
    }
}
