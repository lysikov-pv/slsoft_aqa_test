package tests;

public class Item {
    public String title;
    public String price;
    public String shopName;

    public Item(String title, String price, String shopName) {
        this.title = title;
        this.price = price;
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "Название: " + title +
                "\nЦена: " + price +
                "р.\nМагазин: " + shopName;
    }
}
