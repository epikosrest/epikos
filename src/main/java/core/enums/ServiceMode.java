package core.enums;


import lombok.Getter;

/**
 * Created by nitina on 8/23/16.
 */
@Getter
public enum ServiceMode {
    NORMAL("NORMAL"),SPOOF("SPOOF");
    String mode;
    ServiceMode(String mode){
        this.mode = mode;
    }

}
