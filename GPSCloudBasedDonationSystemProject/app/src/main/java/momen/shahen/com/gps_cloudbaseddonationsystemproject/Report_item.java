package momen.shahen.com.gps_cloudbaseddonationsystemproject;

/**
 * Created by fci on 16/01/18.
 */

public class Report_item {
    private String name , time, photo_url;

    public Report_item(String time, String photo_url, String name) {
        this.name = name;
        this.time = time;
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
