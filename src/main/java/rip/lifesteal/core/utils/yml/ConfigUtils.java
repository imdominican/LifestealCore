package rip.lifesteal.core.utils.yml;

import com.github.cyberryan1.cybercore.managers.FileType;
import com.github.cyberryan1.cybercore.managers.YmlManager;
import com.github.cyberryan1.cybercore.utils.yml.YMLReadTemplate;

public class ConfigUtils extends YMLReadTemplate {

    public ConfigUtils() {
        super.setYMLManager( new YmlManager( FileType.CONFIG ) );
    }

}