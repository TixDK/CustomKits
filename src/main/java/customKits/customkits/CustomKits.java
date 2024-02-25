package customKits.customkits;

import customKits.customkits.CommandHolder.kitCommand;
import customKits.customkits.Events.Chat;
import customKits.customkits.Events.Join;
import customKits.customkits.Expressions.GetCooldown;
import customKits.customkits.Expressions.GetPlayerCooldown;
import customKits.customkits.Extra.*;

import customKits.customkits.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

import static customKits.customkits.CommandHolder.kitCommand.*;

public final class CustomKits extends JavaPlugin {

    public static Map<String, FileConfiguration> languageConfig = new HashMap<>();

    @Override
    public void onEnable() {
        loadData();
        saveDefaultConfig();
        LanguageManager.moveLanguageFiles();


        kitCommand executor = new kitCommand(this);
        getServer().getPluginManager().registerEvents(new stopDrag(this), this);
        getServer().getPluginManager().registerEvents(new SubAdd(), this);
        getServer().getPluginManager().registerEvents(new Join(), this);
        getServer().getPluginManager().registerEvents(new Chat(), this);

        getCommand("ckit").setExecutor(executor);

        new previewKit();
        new giveKit();
        new ForceKit();
        new backSlot();
        new ResetCooldown();
        GetCooldown.register();
        GetPlayerCooldown.register();



        
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


        File configFile = new File(dataFolder, "kits.yml");
        if (configFile.exists()) configFile.delete();

        File playerFile = new File(dataFolder, "playerData.yml");
        if (playerFile.exists()) playerFile.delete();

        try {
            configFile.createNewFile();
            playerFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (Map.Entry<String, ArrayList<ItemStack>> entry : kitHolder.entrySet()) {
            String kitName = entry.getKey();
            ArrayList<ItemStack> items = entry.getValue();
            config.set("kits." + kitName + ".items", items);

            int rows = kitMenuHolder.getOrDefault(kitName, 1);
            config.set("kits." + kitName + ".rows", rows);

            long time = kitCooldown.get(kitName);
            config.set("kits." + kitName + ".cooldown", time);


        }
        Bukkit.getLogger().info("[CustomKits] Saving player cooldown data.");
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
        for (Map.Entry<String, Map<UUID, Long>> entry : playerkitCooldown.entrySet()) {
            String kitName = entry.getKey();
            Map<UUID, Long> cooldownMap = entry.getValue();

            ConfigurationSection cooldownSection = playerConfig.createSection(kitName);
            for (Map.Entry<UUID, Long> cooldownEntry : cooldownMap.entrySet()) {
                cooldownSection.set(cooldownEntry.getKey().toString(), cooldownEntry.getValue());
            }
        }


        try {
            config.save(configFile);
            playerConfig.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(kitHolder.isEmpty()){
            if(configFile.exists()){
                configFile.delete();
            }
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

        File dataFile = new File(dataFolder, "playerData.yml");
        if(!dataFile.exists()){
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

        ConfigurationSection cooldownSection = config.getConfigurationSection("kits");
        if (cooldownSection != null) {
            for (String key : cooldownSection.getKeys(false)) {
                long time = config.getLong("kits." + key + ".cooldown");
                kitCooldown.put(key, time);
            }
        }

        Bukkit.getLogger().info("[CustomKits] Loading player cooldown data.");
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(dataFile);

        for (String kitName : playerConfig.getKeys(false)) {
            ConfigurationSection kitCooldowns = playerConfig.getConfigurationSection(kitName);
            if (kitCooldowns != null) {
                Map<UUID, Long> cooldownMap = new HashMap<>();
                for (String playerIdString : kitCooldowns.getKeys(false)) {
                    UUID playerId = UUID.fromString(playerIdString);
                    long cooldownTime = kitCooldowns.getLong(playerIdString);
                    cooldownMap.put(playerId, cooldownTime);
                }
                playerkitCooldown.put(kitName, cooldownMap);
            }
        }



    }









}
