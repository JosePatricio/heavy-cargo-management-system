package ec.redcode.tas.on.android.models;

/**
 * Created by User on 17/11/2017.
 */

public class Zone {

    String name;
    String code;
    boolean isInteresting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isInteresting() {
        return isInteresting;
    }

    public void setInteresting(boolean interesting) {
        isInteresting = interesting;
    }
}
