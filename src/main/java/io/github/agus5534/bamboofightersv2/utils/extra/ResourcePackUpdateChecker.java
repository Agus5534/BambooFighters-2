package io.github.agus5534.bamboofightersv2.utils.extra;

import io.github.agus5534.agusutils.utils.text.ChatFormatter;
import io.github.agus5534.bamboofightersv2.utils.files.utils.PropertyFile;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResourcePackUpdateChecker {
    private final String serverHash;
    private String urlHash;
    public ResourcePackUpdateChecker() {
        this.serverHash = Bukkit.getResourcePackHash();
    }

    private boolean isUpdated() throws IOException, NoSuchAlgorithmException {
        urlHash = this.sha1Code(Bukkit.getResourcePack()).toLowerCase();

        return serverHash.equalsIgnoreCase(urlHash);
    }

    public void forceUpdate() throws IOException, NoSuchAlgorithmException {
        if(this.isUpdated()) {
            Bukkit.broadcast(ChatFormatter.formatMiniMessage("<green>El ResourcePack está actualizado.</green>"));
            return;
        }

        Bukkit.broadcast(ChatFormatter.formatMiniMessage("<red>Se ha encontrado una nueva versión del Resource Pack!</red><gold>Reinicie el servidor para actualizar.</gold>"));
        Bukkit.getLogger().severe("Found new ResourcePack sha1: " + urlHash);

        if(GeneralConfig.updateSha1) {
            this.replaceServerProperties();
        }
    }

    public void update() throws IOException, NoSuchAlgorithmException {
        if(this.isUpdated()) {
            Bukkit.getLogger().info("ResourcePack is updated!");
            return;
        }

        Bukkit.broadcast(ChatFormatter.formatMiniMessage("<red>Se ha encontrado una nueva versión del Resource Pack!</red> <gold>Reinicie el servidor para actualizar.</gold>"));
        Bukkit.getLogger().severe("Found new ResourcePack sha1: " + urlHash);

        if(GeneralConfig.updateSha1) {
            this.replaceServerProperties();
        }
    }

    private String sha1Code(String fileUrl) throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = new URL(fileUrl).openStream();
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(inputStream, digest);
        byte[] bytes = new byte[1024];
        // read all file content
        while (digestInputStream.read(bytes) > 0);

        // digest = digestInputStream.getMessageDigest();
        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
    }

    /**
     * Convert an array of byte to hex String. <br/>
     * Each byte is covert a two character of hex String. That is <br/>
     * if byte of int is less than 16, then the hex String will append <br/>
     * a character of '0'.
     *
     * @param bytes array of byte
     * @return hex String represent the array of byte
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            if (value < 16) {
                // if value less than 16, then it's hex String will be only
                // one character, so we need to append a character of '0'
                sb.append("0");
            }
            sb.append(Integer.toHexString(value).toUpperCase());
        }
        return sb.toString();
    }

    private void replaceServerProperties() {
        try {
            var rootFile = new File(".");
            var propertiesFile = new File(rootFile.getAbsolutePath() + "/server.properties");
            var properties = new PropertyFile(propertiesFile);

            properties.setProperty("resource-pack-sha1", urlHash);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Failed to update server.properties with new resource pack sha1");
            e.printStackTrace();
        }
    }
}
