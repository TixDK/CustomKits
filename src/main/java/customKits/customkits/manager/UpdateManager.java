package customKits.customkits.manager;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class UpdateManager {

    public static final String DownloadURL = "https://github.com/TixDK/CustomKits/releases/latest/download/CustomKits.jar";

    public static void update(){
        try {
            PluginDescriptionFile desc = Bukkit.getPluginManager().getPlugin("CustomKits").getDescription();
            String pluginName = desc.getName();
            Path PluginFile = Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder().toPath().getParent().resolve(pluginName + ".jar");
            Path downloadPath = PluginFile.getParent().resolve("temp_download.jar");

            DownloadFile(new URL(DownloadURL), downloadPath);
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));

            Files.delete(PluginFile);
            Files.move(downloadPath, PluginFile);

            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void DownloadFile(URL url, Path path) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (InputStream in = connection.getInputStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            connection.disconnect();
        }
    }


    public static boolean isNewUpdateAvailable() {
        try {
            String apiUrl = "https://api.github.com/repos/TixDK/CustomKits/releases/latest";
            String currentVersion = Bukkit.getPluginManager().getPlugin("CustomKits").getDescription().getVersion();

            JsonObject json = new Gson().fromJson(new URL(apiUrl).openStream().toString(), JsonObject.class);
            String latestVersion = json.get("tag_name").getAsString();

            return isNewerVersion(latestVersion, currentVersion);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isNewerVersion(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
            int part1 = Integer.parseInt(parts1[i]);
            int part2 = Integer.parseInt(parts2[i]);
            if (part1 > part2) {
                return true;
            } else if (part1 < part2) {
                return false;
            }
        }

        return parts1.length > parts2.length;
    }
}