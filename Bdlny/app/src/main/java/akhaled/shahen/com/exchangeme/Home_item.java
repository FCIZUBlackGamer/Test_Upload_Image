package akhaled.shahen.com.exchangeme;

/**
 * Created by fci on 04/03/18.
 */

public class Home_item {
    String Img, Name, Discription,user_email,type,price,capital,phone, date;

    public Home_item(String img, String name, String discription, String user_email, String type, String price, String capital, String phone, String date) {
        this.Img = img;
        this.Name = name;
        this.Discription = discription;
        this.user_email = user_email;
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

    public String getUser_email() {
        return user_email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }
}
