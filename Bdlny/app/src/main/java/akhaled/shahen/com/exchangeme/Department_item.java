package akhaled.shahen.com.exchangeme;

/**
 * Created by fci on 04/03/18.
 */

public class Department_item {
    String Name;
    int Img;
    public Department_item(int img, String name) {
        Img = img;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public int getImg() {
        return Img;
    }
}
