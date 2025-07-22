package dto;

import java.sql.Timestamp;

public class ProductDTO {
    private int productId;
    private String productName;
    private String brand;
    private String category;
    private String price; // Đã chuyển thành String
    private String size;
    private int quantity;
    private String description;
    private Timestamp createdAt;
    private String imageBase64; // Lưu chuỗi Base64 của ảnh

    public ProductDTO() {}

    public ProductDTO(int productId, String productName, String brand, String category,
                      String price, String size, int quantity, String description,
                      Timestamp createdAt, String imageBase64) {
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.description = description;
        this.createdAt = createdAt;
        this.imageBase64 = imageBase64;
    }

    // Getters và Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}
