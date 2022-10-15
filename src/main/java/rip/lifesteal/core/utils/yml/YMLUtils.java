package rip.lifesteal.core.utils.yml;

public class YMLUtils {

    public static void initializeConfigs() {
        getConfigUtils().getYMLManager().initialize();
        getDataUtils().getYMLManager().initialize();
        getDataUtils().sendPathNotFoundWarns( false );
    }

    private static ConfigUtils configUtils = new ConfigUtils();
    public static ConfigUtils getConfigUtils() { return configUtils; }
    public static ConfigUtils getConfig() { return configUtils; }

    private static DataUtils dataUtils = new DataUtils();
    public static DataUtils getDataUtils() { return dataUtils; }
    public static DataUtils getData() { return dataUtils; }
}
