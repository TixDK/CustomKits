package customKits.customkits;

import customKits.customkits.CommandHolder.kitCommand;
import customKits.customkits.Events.Join;
import customKits.customkits.Extra.*;

import customKits.customkits.manager.UpdateManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static customKits.customkits.CommandHolder.kitCommand.kitHolder;
import static customKits.customkits.CommandHolder.kitCommand.kitMenuHolder;

public final class CustomKits extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[CustomKits] Eanbling customkits.");
        loadData();
        saveDefaultConfig();

        kitCommand executor = new kitCommand(this);
        getServer().getPluginManager().registerEvents(new stopDrag(this), this);
        getServer().getPluginManager().registerEvents(new SubAdd(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);

        getCommand("ckit").setExecutor(executor);

        new previewKit();
        new giveKit();
        new backSlot();

    }

    @Override
    public void onDisable() {
        dataSaving();
        Bukkit.getLogger().info("[CustomKits] Disabling customkits.");
    }

    public static void dataSaving() {
        Bukkit.getLogger().info("[CustomKits] Saving kits data.");
        File dataFolder = new File("plugins/CustomKits/KitsData");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(dataFolder, "kits.yml"));
        for (Map.Entry<String, ArrayList<ItemStack>> entry : kitHolder.entrySet()) {
            String kitName = entry.getKey();
            ArrayList<ItemStack> items = entry.getValue();
            config.set("kits." + kitName + ".items", items);

            int rows = kitMenuHolder.getOrDefault(kitName, 1);
            config.set("kits." + kitName + ".rows", rows);
        }

        try {
            config.save(new File(dataFolder, "kits.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadData() {
        Bukkit.getLogger().info("[CustomKits] Loading kits data");
        File dataFolder = new File("plugins/CustomKits/KitsData");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File file = new File(dataFolder, "kits.yml");
        if (!file.exists()) {
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection kitSection = config.getConfigurationSection("kits");
        if (kitSection != null) {
            for (String key : kitSection.getKeys(false)) {
                String kitName = key;
                List<?> itemStackList = kitSection.getList(key + ".items");
                if (itemStackList != null) {
                    ArrayList<ItemStack> itemList = new ArrayList<>();
                    for (Object item : itemStackList) {
                        if (item instanceof ItemStack) {
                            itemList.add((ItemStack) item);
                        }
                    }
                    kitHolder.put(kitName, itemList);
                } else {
                    Bukkit.getLogger().warning("[CustomKits] Failed to load kit '" + kitName + "'. ItemStack list is null.");
                }
            }
        } else {
            Bukkit.getLogger().warning("[CustomKits] Failed to load kits. 'kits' section not found in kits.yml.");
        }

        ConfigurationSection rowSection = config.getConfigurationSection("kits");
        if (rowSection != null) {
            for (String key : rowSection.getKeys(false)) {
                int rows = config.getInt("kits." + key + ".rows", 1);
                kitMenuHolder.put(key, rows);
            }
        }
    }


}
