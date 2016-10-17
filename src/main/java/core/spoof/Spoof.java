package core.spoof;

import lombok.Data;

/**
 * Created by nitina on 10/15/16.
 */
@Data
public class Spoof {
    String apiName;

    public Spoof(String apiName){
        this.apiName = apiName;
    }

}
