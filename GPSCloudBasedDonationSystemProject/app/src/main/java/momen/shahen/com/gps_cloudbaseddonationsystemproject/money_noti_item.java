package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class money_noti_item {
    private String name, body, time;

    public money_noti_item(String time, String body, String name) {
        this.name = name;
        this.body = body;
        this.time = time;
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
}
