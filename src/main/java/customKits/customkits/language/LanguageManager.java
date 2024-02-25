package customKits.customkits.language;

import customKits.customkits.CustomKits;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.*;

import static customKits.customkits.Extra.stopDrag.plugin;

public class LanguageManager implements Listener {

    public static void moveLanguageFiles() {
        File langFolder = new File("plugins/CustomKits/lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        copyFile("da.yml", langFolder);
        copyFile("en.yml", langFolder);
    }

    private static void copyFile(String fileName, File targetDirectory) {
        File resourceFile = new File(targetDirectory, fileName);
        if (!resourceFile.exists()) {
            try (InputStream inputStream = CustomKits.class.getResourceAsStream("/" + fileName);
                 OutputStream outputStream = new FileOutputStream(resourceFile)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void reloadLanguageFiles() {
        File langFolder = new File("plugins/CustomKits/lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        File[] langFiles = langFolder.listFiles();
        if (langFiles != null) {
            for (File langFile : langFiles) {
                if (langFile.getName().endsWith(".yml")) {
                    FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
                }
            }
        }
    }


    public static String langConfig(String path){
        String selectedLanguage = plugin.getConfig().getString("language");
        File langFolder = new File("plugins/CustomKits/lang");
        File langFile = new File(langFolder, selectedLanguage + ".yml");
        FileConfiguration langConfig = YamlConfiguration.loadConfiguration(langFile);
        return langConfig.getString(path);
    }
}
