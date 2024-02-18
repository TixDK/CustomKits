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

    public static void update() {
        try {
            PluginDescriptionFile desc = Bukkit.getPluginManager().getPlugin("CustomKits").getDescription();
            String pluginName = desc.getName();
            Path pluginFolder = Bukkit.getPluginManager().getPlugin(pluginName).getDataFolder().toPath().getParent();
            Path pluginFile = pluginFolder.resolve(pluginName + ".jar");
            Path downloadPath = pluginFolder.resolve("temp_download.jar");

            DownloadFile(new URL(DownloadURL), downloadPath);

            if (Files.exists(downloadPath)) {
                Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
                Files.deleteIfExists(pluginFile);
                Files.move(downloadPath, pluginFile);
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(pluginName));
            } else {
                Bukkit.getLogger().info("Failed to download new cersion of CustomKits");
            }

            Bukkit.getLogger().info("CK Path: " + downloadPath);
            Bukkit.getLogger().info("CK File: " + pluginFile);
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

    public static String nuVersion;
    public static String nyVersion;
    public static boolean isNewUpdateAvailable() {
        try {
            String apiUrl = "https://api.github.com/repos/TixDK/CustomKits/releases/latest";
            String currentVersion = Bukkit.getPluginManager().getPlugin("CustomKits").getDescription().getVersion();

            String token = "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";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
            String latestVersion = json.get("tag_name").getAsString();
            Bukkit.getLogger().info("Version: " + currentVersion + " Ny version " + latestVersion);
            nuVersion = currentVersion;
            nyVersion = latestVersion;
            return isNewerVersion(latestVersion, currentVersion);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean isNewerVersion(String version1, String version2) {
        Bukkit.getLogger().info("Comparing versions: " + version1 + " and " + version2);
        boolean isNewer = version1.compareTo(version2) > 0;
        Bukkit.getLogger().info("Is version1 newer than version2? " + isNewer);
        return isNewer;
    }



}
