package momen.shahen.com.gps_cloudbaseddonationsystemproject;


/**
 * Created by fci on 31/01/18.
 */

public class Profile_hosp_data {
    String name,email, city_name;

    public Profile_hosp_data(String name, String email, String city_name) {
        this.name = name;
        this.email = email;
        this.city_name = city_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
