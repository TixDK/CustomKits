package customKits.customkits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static customKits.customkits.previewKit.*;

public class stopDrag implements Listener {

    static CustomKits plugin;

    public stopDrag(CustomKits plugin){
        stopDrag.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        String openInv = event.getInventory().getTitle();
        String colorCode = plugin.getConfig().getString("Settings.Basics.Preview-ColorCode");
        if(player != null && kit != null && openInv != null && openInv.equals(ChatColor.translateAlternateColorCodes('&', "&fPreview " + colorCode + kit))){
            event.setCancelled(true);
        }
    }
}