package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class blood_noti_item {
    private String docname, body, time, state, hosname;

    public blood_noti_item(String time, String body, String docname, String state, String hosname) {
        this.docname = docname;
        this.body = body;
        this.time = time;
        this.state = state;
        this.hosname = hosname;
    }

    public String getDocName() {
        return docname;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }

    public String getState() {
        return state;
    }

    public String getHosname() {
        return hosname;
    }
}
