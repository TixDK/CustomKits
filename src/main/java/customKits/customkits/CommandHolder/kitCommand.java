package customKits.customkits.CommandHolder;

import customKits.customkits.CustomKits;
import customKits.customkits.Extra.FormatTime;
import customKits.customkits.Extra.giveKit;
import customKits.customkits.language.LanguageManager;
import customKits.customkits.manager.UpdateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static customKits.customkits.Extra.previewKit.previewKitMenu;
import static customKits.customkits.Extra.stopDrag.plugin;
import static customKits.customkits.manager.UpdateManager.isNewUpdateAvailable;
import static customKits.customkits.manager.UpdateManager.nuVersion;


public class kitCommand implements CommandExecutor {

    public static HashMap<String, ArrayList<ItemStack>> kitHolder = new HashMap<>();
    public static HashMap<String, Integer> kitMenuHolder = new HashMap<>();
    public static HashMap<String, Long> kitCooldown = new HashMap<>();
    public static Map<String, Map<UUID, Long>> playerkitCooldown = new HashMap<>();

    public static Map<UUID, String> editCooldown = new HashMap<>();

    private final CustomKits plugin;

    public kitCommand(CustomKits plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args){
        //String imports


        String prefix = LanguageManager.langConfig("Settings.Basics.Prefix");
        String lackPermission = LanguageManager.langConfig("Settings.Kit-Creation.Lack-Permission");
        String createKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Create-Kit-Permission");
        String deleteKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Delete-Kit-Permission");
        String infoKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Info-Kit-Permission");
        String previewKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Preview-Kit-Permission");
        String editKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Edit-Kit-Permission");
        String updatePermission = LanguageManager.langConfig("Settings.Kit-Creation.Update-Permission");
        String giveKitPermission = LanguageManager.langConfig("Settings.Kit-Creation.Give-Kit-Permission");
        String reloadConfigPermission = LanguageManager.langConfig("Settings.Kit-Creation.Reload-Config-Permission");

        //Message imports
        String KitNotFound = LanguageManager.langConfig("Settings.Messages.Kit-Not-Found");
        String kitAlreadyExists = LanguageManager.langConfig("Settings.Messages.Kit-Already-Exists");
        String EmptyList = LanguageManager.langConfig("Settings.Messages.Empty-List");
        String CreateMessage = LanguageManager.langConfig("Settings.Messages.Create-Message");
        String DeleteMessage = LanguageManager.langConfig("Settings.Messages.Delete-Message");
        String ListMessage = LanguageManager.langConfig("Settings.Messages.List-Message");
        String EditCooldown = LanguageManager.langConfig("Settings.Messages.Edit-Cooldown");


        //Variabler
        Player player = (Player) sender;
        ItemStack[] content = player.getInventory().getContents();

        //Creation of kits
        if (args.length == 3 && args[0].equalsIgnoreCase("create") && !args[1].isEmpty()) {
            if (player.hasPermission(createKitPermission) && args[0].equals("create")) {
                if (!kitHolder.containsKey(args[1].toString())) {
                    ArrayList<ItemStack> itemHolder = new ArrayList<>();
                    String nameOfKit = args[1].toString();
                    for (ItemStack item : content) {
                        itemHolder.add(item);
                    }
                    String timeStr = args[2];
                    Long newTime = (long) FormatTime.parseTime(timeStr);
                    kitCooldown.put(nameOfKit, newTime);
                    kitHolder.put(nameOfKit, itemHolder);
                    kitMenuHolder.put(nameOfKit, 3);
                    String CreateKit = CreateMessage.replace("{kit}", nameOfKit);
                    String CreateKitCooldown = CreateKit.replace("{cooldown}", FormatTime.formatTime(newTime));

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + CreateKitCooldown));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + kitAlreadyExists));
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete") && !args[1].isEmpty()) {
            if (player.hasPermission(deleteKitPermission)){
                if(kitHolder.containsKey(args[1])){
                    String nameOfKit = args[1].toString();
                    kitHolder.remove(nameOfKit);
                    kitMenuHolder.remove(nameOfKit);
                    kitCooldown.remove(nameOfKit);
                    playerkitCooldown.remove(nameOfKit);
                    String DeleteKit = DeleteMessage.replace("{kit}", nameOfKit);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + DeleteKit));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + KitNotFound));
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + ListMessage + " " + kits));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + EmptyList));
            }
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("edit")){
            if(player.hasPermission(editKitPermission)){
                String kit = args[1];
                if(kitHolder.containsKey(kit)){
                    int rows = kitMenuHolder.get(kit);
                    if(kitHolder.containsKey(kit) && kitMenuHolder.containsKey(kit)){
                        if(args[2].equalsIgnoreCase("view")){
                            editKit(rows, player, kit);
                        } else if (args[2].equalsIgnoreCase("cooldown")){
                            UUID playerId = player.getUniqueId();
                            editCooldown.put(playerId, kit);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + EditCooldown + " &ccancel"));
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + KitNotFound));
                }
            }
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("give")){
            if(player.hasPermission(giveKitPermission)){
                String kit = args[2];
                String playerToGetKitString = args[1];
                Player playerToGetKit = Bukkit.getPlayer(playerToGetKitString);
                if(kitHolder.containsKey(kit)){
                    giveKit.directGiveKit(kit, playerToGetKit);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + KitNotFound));
                }
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("preview")){
            if(player.hasPermission(previewKitPermission)){
                String kit = args[1];
                if(kitHolder.containsKey(kit)){
                    previewKitMenu(kit, player);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + KitNotFound));
                }
            }
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission(reloadConfigPermission)){
                File CustomKitsFile = new File("plugins/CustomKits/config.yml");
                File configFile = new File(plugin.getDataFolder(), "config.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

                if (CustomKitsFile.exists() && !config.contains("language")) {
                    try {
                        Files.delete(CustomKitsFile.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (!CustomKitsFile.exists()) {
                    plugin.saveDefaultConfig();
                }

                plugin.reloadConfig();
                LanguageManager.reloadLanguageFiles();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6CustomKits&8] &fConfig reloaded successfully."));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + lackPermission));
            }
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("update")){
            if (sender.hasPermission(updatePermission)){
                if(UpdateManager.isNewUpdateAvailable()){
                    UpdateManager.update();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fUpdating to the newest version."));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &fYou already have the latest version of CustomKits"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  prefix + " " + lackPermission));
            }
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("version")){
            if (sender.hasPermission(infoKitPermission)){
                String status;
                if(isNewUpdateAvailable()){
                    status = "&c✘";
                } else {
                    status = "&a✔";
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fVersion: " + nuVersion));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fLatest version: " + status));
                sender.sendMessage(" ");

            }
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("help") && player.hasPermission(infoKitPermission)){
            Help(player, prefix);
        }
        if (args.length == 0 && player.hasPermission(infoKitPermission)){
            Help(player, prefix);
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
        mapMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cRemove"));
        paperMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Add"));
        List<String> mapLore = new ArrayList<>();
        mapLore.add(" ");
        mapLore.add(ChatColor.translateAlternateColorCodes('&', "&c│ &f-1 row"));
        mapMeta.setLore(mapLore);
        map.setItemMeta(mapMeta);
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6│ &f+1 row"));
        paperMeta.setLore(lore);
        paper.setItemMeta(paperMeta);
        editKitInv.setItem(rowMinus, map);
        editKitInv.setItem(rowAdd, paper);
        player.openInventory(editKitInv);
        }
    }

    public void Help(Player sender, String prefix){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  " &8&m----------&6 " + prefix + " &8&m----------"));
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fCreate kit: &6/ckit create <Name> <Cooldown>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fDelete kit: &6/ckit delete <Name>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fKits: &6/ckit list"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fEdit kit: &6/ckit edit <Name> <View|Cooldown>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fGive kit: &6/ckit give <Player> <Name>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6│ &fPreview kit: &6/ckit preview <Name>"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fReload config: &6/ckit reload"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fUpdate plugin: &6/ckit update"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',  "&6│ &fPlugin version: &6/ckit version"));
        sender.sendMessage(" ");
    }


}