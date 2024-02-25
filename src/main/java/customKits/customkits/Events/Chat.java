package customKits.customkits.Events;

import customKits.customkits.Extra.FormatTime;
import customKits.customkits.language.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import static customKits.customkits.CommandHolder.kitCommand.editCooldown;
import static customKits.customkits.CommandHolder.kitCommand.kitCooldown;

public class Chat implements Listener {


    String prefix = LanguageManager.langConfig("Settings.Basics.Prefix");
    String StopEditing = LanguageManager.langConfig("Settings.Messages.Stop-Editing");
    String ChangedCooldown = LanguageManager.langConfig("Settings.Messages.Changed-Cooldown");

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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + StopEditing + kit));
            } else{
                event.setCancelled(true);
                Long newTime = (long) FormatTime.parseTime(message);
                kitCooldown.put(kit, newTime);
                editCooldown.remove(playerID);
                String EditKit = ChangedCooldown.replace("{kit}", kit);
                String EditKitCooldown = EditKit.replace("{cooldown}", FormatTime.formatTime(newTime));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + EditKitCooldown));
            }
        }
    }
}
