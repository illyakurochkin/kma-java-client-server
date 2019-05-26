package model;

public class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Product && ((Product) obj).name.equals(name);
    }

    @Override
    public String toString() {
        return "(name: " + name + ", price: " + price + ")";
    }
}
