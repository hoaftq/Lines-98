package thbt.webng.com.game.option;

import thbt.webng.com.game.util.StorageUtil;

public final class GameOptionsManager {
    private static final String CONFIG_FILE_NAME = "Config";
    private static GameOptions currentGameOptions = loadGameOptions();

    public static GameOptions getCurrentGameOptions() {
        return currentGameOptions;
    }

    public static void setCurrentGameOptions(GameOptions gameInfo) {
        currentGameOptions = gameInfo;
        StorageUtil.save(currentGameOptions, CONFIG_FILE_NAME);
    }

    private static GameOptions loadGameOptions() {
        return StorageUtil.<GameOptions>load(CONFIG_FILE_NAME).orElse(new GameOptions());
    }
}
