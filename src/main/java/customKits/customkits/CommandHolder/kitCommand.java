package customKits.customkits.CommandHolder;

import customKits.customkits.CustomKits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static customKits.customkits.Extra.previewKit.previewKitMenu;


public class kitCommand implements CommandExecutor {

    public static HashMap<String, ArrayList<ItemStack>> kitHolder = new HashMap<>();
    public static HashMap<String, Integer> kitMenuHolder = new HashMap<>();

    private final CustomKits plugin;

    public kitCommand(CustomKits plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        //String imports
        String prefix = plugin.getConfig().getString("Settings.Basics.Prefix");
        String createKitPermission = plugin.getConfig().getString("Settings.Kit-Creation.Create-Kit-Permission");
        String deleteKitPermission = plugin.getConfig().getString("Settings.Kit-Creation.Delete-Kit-Permission");
        String infoKitPermission = plugin.getConfig().getString("Settings.Kit-Creation.Info-Kit-Permission");
        String previewKitPermission = plugin.getConfig().getString("Settings.Kit-Creation.Preview-Kit-Permission");
        String editKitPermission = plugin.getConfig().getString("Settings.Kit-Creation.Edit-Kit-Permission");

        //Variabler
        Player player = (Player) sender;
        ItemStack[] content = player.getInventory().getContents();

        //Creation of kits
        if (args.length == 2 && args[0].equalsIgnoreCase("create") && !args[1].isEmpty()) {
            if (player.hasPermission(createKitPermission) && args[0].equals("create")) {
                if (!kitHolder.containsKey(args[1].toString())) {
                    ArrayList<ItemStack> itemHolder = new ArrayList<>();
                    String nameOfKit = args[1].toString();
                    for (ItemStack item : content) {
                        itemHolder.add(item);
                    }
                    kitHolder.put(nameOfKit, itemHolder);
                    kitMenuHolder.put(nameOfKit, 3);
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDu oprettede kittet &6&n" + nameOfKit));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDette kit findes allerede."));
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete") && !args[1].isEmpty()) {
            if (player.hasPermission(deleteKitPermission) && args[0].equals("delete")){
                if(kitHolder.containsKey(args[1])){
                    kitHolder.remove(args[1]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDu slettede kittet &6&n" + args[1]));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDette kit findes ikke."));
                }
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("list") && player.hasPermission(infoKitPermission)){
            if(!kitHolder.isEmpty()){
                ArrayList<String> kitList = new ArrayList<>();
                for(Map.Entry<String, ArrayList<ItemStack>> entry : kitHolder.entrySet()){
                    kitList.add(entry.getKey());
                }
                String kits = String.join(", ", kitList);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fFølgende kits: &6" + kits));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDer er på nuværende tidspunkt ingen kits."));
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("edit")){
            if(player.hasPermission(editKitPermission)){
                String kit = args[1];
                if(kitHolder.containsKey(kit)){
                    int rows = kitMenuHolder.get(kit);
                    if(kitHolder.containsKey(kit) && kitMenuHolder.containsKey(kit)){
                        editKit(rows, player, kit);
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDette kit findes ikke."));
                }
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("preview")){
            if(player.hasPermission(previewKitPermission)){
                String kit = args[1];
                if(kitHolder.containsKey(kit)){
                    previewKitMenu(kit, player);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fDette kit findes ikke."));
                }
            }
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            String reloadConfigPermission = plugin.getConfig().getString("Settings.Kit-Creation.Reload-Config-Permission");
            if(sender.hasPermission(reloadConfigPermission)){
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fConfig reloadet korrekt."));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fDu har ikke adgang til dette."));
            }
        }

        if (args.length == 0 && player.hasPermission(infoKitPermission)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  " &8&m----------&6 " + prefix + " &8&m----------"));
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fOpret kit: &6/ckit create <Navn>"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fSlet kit: &6/ckit delete <Navn>"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fSe alle kits: &6/ckit list"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fEdit kit: &6/ckit edit <Navn>"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fPreview kit: &6/ckit preview <Navn>"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fReload config: &6/ckit reload"));
            sender.sendMessage(" ");
        }
        return false;
    }

    public static void editKit(Integer rows, Player player, String kit){
        Inventory editKitInv = Bukkit.createInventory(null, rows * 9, ChatColor.translateAlternateColorCodes('&', "&fEditing kit: &6" + kit));
        int rowAdd = 0;
        if(rows == 1) rowAdd = 8;
        if(rows == 2) rowAdd = 17;
        if(rows == 3) rowAdd = 26;
        if(rows == 4) rowAdd = 35;
        if(rows == 5) rowAdd = 44;
        if(rows == 6) rowAdd = 53;
        int rowMinus = rowAdd - 1;
        int num = 0;
        ArrayList<ItemStack> kitItems = kitHolder.get(kit);
        for (ItemStack item : kitItems) {
            if (item != null) {
                editKitInv.setItem(num, item);
                num = num + 1;
            }

        ItemStack map = new ItemStack(Material.MAP);
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta mapMeta = map.getItemMeta();
        ItemMeta paperMeta = paper.getItemMeta();
        mapMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cFjern"));
        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Tilføj"));
        List<String> mapLore = new ArrayList<>();
        mapLore.add(" ");
        mapLore.add(ChatColor.translateAlternateColorCodes('&', "&c│ &f-1 række"));
        mapMeta.setLore(mapLore);
        map.setItemMeta(mapMeta);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6│ &f+1 række"));
        paperMeta.setLore(lore);
        paper.setItemMeta(paperMeta);
        editKitInv.setItem(rowMinus, map);
        editKitInv.setItem(rowAdd, paper);
        player.openInventory(editKitInv);
        }
    }

}