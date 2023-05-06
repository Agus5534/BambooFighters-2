package io.github.agus5534.bamboofightersv2.utils.extra;

import io.github.agus5534.bamboofightersv2.utils.files.FileManager;
import io.github.agus5534.bamboofightersv2.utils.files.utils.JsonFile;

public class GeneralConfig {
    public static JsonFile json = FileManager.Config;

    public static boolean updateSha1 = json.getJsonObject().get("auto-update-sha1").getAsBoolean();
}
