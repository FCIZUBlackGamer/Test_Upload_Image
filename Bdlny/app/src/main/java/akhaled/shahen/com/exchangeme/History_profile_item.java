package akhaled.shahen.com.exchangeme;

/**
 * Created by fci on 04/03/18.
 */

public class History_profile_item {
    String Img, Name, Discription,type,price,capital,phone, date;

    public History_profile_item(String img, String name, String discription, String type, String price, String capital, String phone, String date) {
        this.Img = img;
        this.Name = name;
        this.Discription = discription;
        this.type = type;
        this.price = price;
        this.capital = capital;
        this.phone = phone;
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public String getDiscription() {
        return Discription;
    }

    public String getImg() {
        return Img;
    }

    public String getCapital() {
        return capital;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

}
