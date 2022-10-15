package rip.lifesteal.core.utils.yml;

import com.github.cyberryan1.cybercore.CyberCore;
import com.github.cyberryan1.cybercore.managers.FileType;
import com.github.cyberryan1.cybercore.managers.YmlManager;
import com.github.cyberryan1.cybercore.utils.CoreUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataYmlManager extends YmlManager {

    public FileType type;

    private String mainFileName;
    private String defaultFileName;
    private File mainFile;
    private File defaultFile;
    private FileConfiguration mainConfig;

    public DataYmlManager( FileType type ) {
        this.type = type;
        this.mainFileName = type.getFileName();
        this.mainFile = new File( CyberCore.getPlugin().getDataFolder(), this.mainFileName );
        this.defaultFileName = this.mainFileName.replace( ".yml", "_default.yml" );
        this.defaultFile = new File( CyberCore.getPlugin().getDataFolder(), this.defaultFileName );
    }

    @Override
    public void initialize() {
        // Save the main file if it doesn't exist
        if ( this.mainFile.exists() == false ) {
            CoreUtils.logInfo( "The " + this.mainFileName + " file was not found, so a new one is being created..." );

            // Creating the directory to the main file
            File mainFileDirectory = new File( this.mainFile.getParent() );
            mainFileDirectory.mkdirs();

            // Copy the default file to the main file
            try {
                this.mainFile.createNewFile();

                byte bytes[] = new byte[8192];
                InputStream fin = CyberCore.getPlugin().getResource( this.defaultFileName );
                FileOutputStream fout = new FileOutputStream( this.mainFile );

                int read = fin.read( bytes );
                while ( read >= 0 ) {
                    fout.write( bytes, 0, read );
                    read = fin.read( bytes );
                }

                fin.close();
                fout.close();
            } catch ( IOException e ) {
                CoreUtils.logError( "An error occurred while trying to create the " + this.mainFileName + " file; see below for details" );
                throw new RuntimeException( e );
            }
            CoreUtils.logInfo( "The " + this.mainFileName + " file was created successfully" );
        }

        // Using the main file
        else {
            CoreUtils.logInfo( "The " + this.mainFileName + " file was found, so it will be used" );
        }

        // Initialize the file configuration
        reloadConfiguration();
    }

    @Override
    public FileConfiguration getConfig() {
        if ( mainConfig == null ) { initialize(); }
        return mainConfig;
    }

    /**
     * Saves the configuration to the file
     */
    @Override
    public void saveConfig() {
        try {
            getConfig().save( this.mainFile );
        } catch ( IOException e ) {
            CoreUtils.logError( "An error occurred while trying to save the " + this.mainFileName + " file; see below for details" );
            throw new RuntimeException( e );
        }
    }

    /**
     * Reloads the configuration from the file
     */
    @Override
    public void reloadConfiguration() {
        this.mainConfig = YamlConfiguration.loadConfiguration( this.mainFile );
    }

    /**
     * @return The {@link FileType} used
     */
    @Override
    public FileType getFileType() { return type; }
}
