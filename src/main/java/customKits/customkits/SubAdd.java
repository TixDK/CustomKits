package customKits.customkits;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static customKits.customkits.kitCommand.editKit;
import static customKits.customkits.kitCommand.kitMenuHolder;

public class SubAdd implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null){
            return;
        }

        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }


        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        String invName = player.getOpenInventory().getTitle();
        String invStrip = ChatColor.stripColor(invName);
        String[] part = invStrip.split(":");
        if(invStrip.contains("Editing kit")){
            event.setCancelled(true);
            String kit = part[1].trim();
            int rowAddSlot = kitMenuHolder.get(kit);
            int rowAdd = 0;
            if(rowAddSlot == 1) rowAdd = 8; if(rowAddSlot == 2) rowAdd = 17; if(rowAddSlot == 3) rowAdd = 26; if(rowAddSlot == 4) rowAdd = 35; if(rowAddSlot == 5) rowAdd = 44; if(rowAddSlot == 6) rowAdd = 53;
            int rowMinus = rowAdd - 1;
            if(slot == rowAdd){
                if(slot == 53) return;
                int newAddSlot = rowAddSlot + 1;
                kitMenuHolder.put(kit, newAddSlot);
                editKit(newAddSlot, player, kit);
            }
            if(slot == rowMinus){
                if(slot == 7) return;
                int newAddSlot = rowAddSlot - 1;
                kitMenuHolder.put(kit, newAddSlot);
                editKit(newAddSlot, player, kit);
            }
        }

        if(invStrip.contains("Preview")){
            event.setCancelled(true);
        }



    }


}
