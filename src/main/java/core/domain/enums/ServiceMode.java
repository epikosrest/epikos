package core.domain.enums;


/**
 * Created by nitina on 8/23/16.
 */

public enum ServiceMode {
    NORMAL("NORMAL"),SPOOF("SPOOF");
    String mode;
    ServiceMode(String mode){
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
