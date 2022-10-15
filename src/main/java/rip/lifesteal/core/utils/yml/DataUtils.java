package rip.lifesteal.core.utils.yml;

import com.github.cyberryan1.cybercore.managers.FileType;
import com.github.cyberryan1.cybercore.utils.yml.YMLReadEditTemplate;

public class DataUtils extends YMLReadEditTemplate {

    public DataUtils() {
        super.setYMLManager( new DataYmlManager( FileType.DATA ) );
    }

}