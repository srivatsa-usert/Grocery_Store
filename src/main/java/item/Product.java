package item;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private int quantity;

    public Product() {
    }

    public Product(int id, String name, String description, double price,String imageUrl,int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Product get(int i) {
    	return this;
    }
    
    public int getQuantity() {
    	return this.quantity;
    }
}

