package customKits.customkits.Events;

import customKits.customkits.Extra.FormatTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import static customKits.customkits.CommandHolder.kitCommand.editCooldown;
import static customKits.customkits.CommandHolder.kitCommand.kitCooldown;
import static customKits.customkits.Extra.stopDrag.plugin;

public class Chat implements Listener {


    String prefix = plugin.getConfig().getString("Settings.Basics.Prefix");


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();
        String message = event.getMessage();

        if(editCooldown.containsKey(playerID)){
            String kit = editCooldown.get(playerID);
            if(message.equalsIgnoreCase("cancel")){
                editCooldown.remove(playerID);
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDu afbrudte ændringen på kittet &6" + kit));
            } else{
                event.setCancelled(true);
                Long newTime = (long) FormatTime.parseTime(message);
                kitCooldown.put(kit, newTime);
                editCooldown.remove(playerID);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDu satte cooldownen til &6" + FormatTime.formatTime(newTime) + "&fpå kittet " + kit));
            }
        }
    }
}
