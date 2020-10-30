package ec.redcode.tas.on.android.models;

/**
 * Created by User on 16/11/2017.
 */

public class MyFletesListState {
    private String code;
    private String title;

    public static final String ARG_FLETE_LIST_ID = "flete_list_id";

    public MyFletesListState(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public MyFletesListState() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
