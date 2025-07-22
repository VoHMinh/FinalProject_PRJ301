package dto;

public class InventoryDTO {
    private int size;      // cột size (int)
    private int quantity;  // cột quantity

    public InventoryDTO() {}
    public InventoryDTO(int size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
