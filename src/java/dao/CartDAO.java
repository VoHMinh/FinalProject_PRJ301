package dao;

import dto.CartItemDTO;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    
    public boolean addToCart(int userId, int productId, int quantity) {
        String checkSql = "SELECT cart_item_id, quantity FROM tblCartItems WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE tblCartItems SET quantity = ? WHERE cart_item_id = ?";
        String insertSql = "INSERT INTO tblCartItems(user_id, product_id, quantity) VALUES(?,?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setInt(1, userId);
            psCheck.setInt(2, productId);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    int cartItemId = rs.getInt("cart_item_id");
                    int existingQuantity = rs.getInt("quantity");
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {
                        psUpdate.setInt(1, existingQuantity + quantity);
                        psUpdate.setInt(2, cartItemId);
                        return psUpdate.executeUpdate() > 0;
                    }
                } else {
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.setInt(1, userId);
                        psInsert.setInt(2, productId);
                        psInsert.setInt(3, quantity);
                        return psInsert.executeUpdate() > 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Lấy danh sách mặt hàng trong giỏ hàng của user
    public List<CartItemDTO> getCartItemsByUser(int userId) {
        List<CartItemDTO> list = new ArrayList<>();
        String sql = "SELECT ci.cart_item_id, ci.user_id, ci.product_id, ci.quantity, " +
                     "       p.product_name, p.brand, p.price, p.image_base64 " +
                     "FROM tblCartItems ci " +
                     "JOIN tblProducts p ON ci.product_id = p.product_id " +
                     "WHERE ci.user_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItemDTO item = new CartItemDTO();
                    item.setCartItemId(rs.getInt("cart_item_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("product_name"));
                    item.setBrand(rs.getString("brand"));
                    // Lấy giá trị price dưới dạng String (vì cột price là NVARCHAR)
                    item.setPrice(rs.getString("price"));
                    item.setImageBase64(rs.getString("image_base64"));
                    list.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Phương thức xóa sản phẩm khỏi giỏ hàng của user
    public boolean removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM tblCartItems WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
