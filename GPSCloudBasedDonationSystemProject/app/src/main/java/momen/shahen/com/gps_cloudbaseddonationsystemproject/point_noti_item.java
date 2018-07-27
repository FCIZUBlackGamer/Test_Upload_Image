package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class point_noti_item {
    private String name, body, time, spon;

    public point_noti_item(String time, String body, String spon, String name) {
        this.name = name;
        this.body = body;
        this.time = time;
        this.spon = spon;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getTime() {
        return time;
    }

    public String getSpon() {
        return spon;
    }
}
