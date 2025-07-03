package utils;

public class Item {
    private String name;
    private double price;
    private int quantity; // Represents current available quantity of this item instance for display

    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Methods to modify the quantity of *this* Item instance (primarily for combo box display logic)
    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }
    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return name + " (₱" + String.format("%.2f", price) + ", Stock: " + quantity + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}