package utils;

public class Item {
    private String name;
    private double price;
    private int quantity;

    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // 🆕 ADD THIS METHOD
    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name + " | ₱" + price + " | " + quantity + " in stock";
    }
}
