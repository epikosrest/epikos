package core.error;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by nitina on 6/2/16.
 */

@XmlRootElement(name = "EpikosError")
public class EpikosError {

    private String message;
    private int id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
