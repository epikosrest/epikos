package core.error;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nitina on 6/2/16.
 */
@XmlRootElement(name = "EpikosError")
public class EpikosError {

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
