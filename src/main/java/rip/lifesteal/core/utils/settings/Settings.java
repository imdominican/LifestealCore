package rip.lifesteal.core.utils.settings;

import com.github.cyberryan1.cybercore.utils.CoreUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A list of all settings from the config.yml file
 */
public enum Settings {

    // General Settings
    PREFIX( "general.prefix", "string" ),
    PRIMARY_COLOR( "general.primary-color", "string" ),
    SECONDARY_COLOR( "general.secondary-color", "string" ),
    PERMS_DENIED( "general.permission-denied-msg", "string" ),

    // Core Lifesteal Settings
    LIFESTEAL_MAX_HEALTH( "lifesteal.max-health", "float" ),
    LIFESTEAL_MIN_HEALTH( "lifesteal.min-health", "float" ),

    // Heart Item Settings
    HEART_MATERIAL( "heart.material", "material" ),
    HEART_NAME( "heart.name", "string" ),
    HEART_LORE( "heart.lore", "strlist" ),
    HEART_ENCHANT_GLOW( "heart.enchant-glow", "boolean" ),

    // Half Heart Item Settings
    HALF_HEART_MATERIAL( "half-heart.material", "material" ),
    HALF_HEART_NAME( "half-heart.name", "string" ),
    HALF_HEART_LORE( "half-heart.lore", "strlist" ),
    HALF_HEART_ENCHANT_GLOW( "half-heart.enchant-glow", "boolean" ),

    // Command Settings
    SET_HEALTH_PERMISSION( "sethealth.permission", "string" ),

    VIEW_HEALTH_PERMISSION( "viewhealth.permission", "string" ),

    HEALTH_ALL_PERMISSION( "healthall.permission", "string" ),

    HEART_PAY_PERMISSION( "heartpay.permission", "string" ),

    GIVE_HEART_PERMISSION( "giveheart.permission", "string" ),

    GIVE_HALF_PERMISSION( "givehalf.permission", "string" ),

    // Combat Log Settings
    COMBAT_LOG_TIME( "combat-log.time", "int" ),
    COMBAT_MSG_DELAY( "combat-log.message-delay", "int" ),

    ;

    private String path;
    private SettingsEntry value;
    Settings( String path, String valueType ) {
        this.path = path;
        this.value = new SettingsEntry( path, valueType );
    }

    public void reload() {
        this.value = new SettingsEntry( this.path, this.value.getValueType() );
    }

    public String getPath() { return this.path; }

    public SettingsEntry getValue() { return this.value; }

    public int integer() { return value.integer(); }

    public String string() { return value.string(); }

    public String coloredString() { return CoreUtils.getColored( value.string() ); }

    public float getFloat() { return value.getFloat(); }

    public double getDouble() { return value.getDouble(); }

    public long getLong() { return value.getLong(); }

    public boolean bool() { return value.bool(); }

    public Material material() { return value.material(); }

    public String[] stringlist() { return value.stringlist(); }

    public String[] coloredStringlist() {
        String[] toReturn = new String[ stringlist().length ];
        for ( int i = 0; i < stringlist().length; i++ ) {
            toReturn[i] = CoreUtils.getColored( stringlist()[i] );
        }
        return toReturn;
    }

    public List<String> arraylist() { return new ArrayList<>( Arrays.asList( stringlist() ) ); }

    public List<String> coloredArraylist() { return new ArrayList<>( Arrays.asList( coloredStringlist() ) ); }

}