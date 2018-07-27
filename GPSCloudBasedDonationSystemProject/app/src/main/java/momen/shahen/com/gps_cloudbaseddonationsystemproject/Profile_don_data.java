package momen.shahen.com.gps_cloudbaseddonationsystemproject;


/**
 * Created by fci on 31/01/18.
 */

public class Profile_don_data {
    String name, age, last_don, email, points;

    public Profile_don_data(String name, String age, String last_don, String email, String points) {
        this.name = name;
        this.age = age;
        this.last_don = last_don;
        this.email = email;
        this.points = points;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getLast_don() {
        return last_don;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }
}
