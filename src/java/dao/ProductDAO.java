package dao;

import dto.InventoryDTO;
import dto.ProductDTO;
import utils.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Phương thức có hỗ trợ phân trang theo pageIndex và pageSize
    public List<ProductDTO> getProducts(String brand, String search, String category, int pageIndex, int pageSize) {
        List<ProductDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tblProducts WHERE 1=1 ");
        if (brand != null && !brand.isEmpty()) {
            sql.append(" AND brand = ? ");
        }
        if (search != null && !search.isEmpty()) {
            sql.append(" AND product_name LIKE ? ");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ? ");
        }
        sql.append(" ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (brand != null && !brand.isEmpty()) {
                ps.setString(idx++, brand);
            }
            if (search != null && !search.isEmpty()) {
                ps.setString(idx++, "%" + search + "%");
            }
            if (category != null && !category.isEmpty()) {
                ps.setString(idx++, category);
            }
            ps.setInt(idx++, (pageIndex - 1) * pageSize);
            ps.setInt(idx++, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductDTO p = new ProductDTO(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("brand"),
                            rs.getString("category"),
                            rs.getString("price"),
                            rs.getString("size"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getTimestamp("created_at"),
                            rs.getString("image_base64")
                    );
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ProductDTO getProductById(int productId) {
        String sql = "SELECT * FROM tblProducts WHERE product_id = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductDTO(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("brand"),
                            rs.getString("category"),
                            rs.getString("price"),
                            rs.getString("size"),
                            rs.getInt("quantity"),
                            rs.getString("description"),
                            rs.getTimestamp("created_at"),
                            rs.getString("image_base64")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addProduct(ProductDTO p) {
        String sql = "INSERT INTO tblProducts(product_name, brand, category, price, size, quantity, description, image_base64) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            // Sử dụng setNString cho các trường Unicode
            ps.setNString(1, p.getProductName());
            ps.setNString(2, p.getBrand());
            ps.setNString(3, p.getCategory());
            ps.setString(4, p.getPrice());
            ps.setNString(5, p.getSize());
            ps.setInt(6, p.getQuantity());
            ps.setNString(7, p.getDescription());
            ps.setNString(8, p.getImageBase64());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(ProductDTO p) {
        String sql = "UPDATE tblProducts SET product_name=?, brand=?, category=?, price=?, size=?, quantity=?, description=?, image_base64=? WHERE product_id=?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, p.getProductName());
            ps.setNString(2, p.getBrand());
            ps.setNString(3, p.getCategory());
            ps.setString(4, p.getPrice());
            ps.setNString(5, p.getSize());
            ps.setInt(6, p.getQuantity());
            ps.setNString(7, p.getDescription());
            ps.setNString(8, p.getImageBase64());
            ps.setInt(9, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM tblProducts WHERE product_id = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức kiểm tra xem sản phẩm đã tồn tại theo tên chưa (sử dụng setNString cho Unicode)
    public boolean productNameExists(String productName) {
        String sql = "SELECT product_id FROM tblProducts WHERE product_name = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy số lượng tồn kho của sản phẩm theo productId và mức size.
     *
     * @param productId Mã sản phẩm.
     * @param shoeSize Mức size (kiểu int) của sản phẩm.
     * @return Số lượng tồn kho, nếu không có thì trả về 0.
     */
    public int getInventoryQuantity(int productId, int shoeSize) {
        int quantity = 0;
        String sql = "SELECT quantity FROM tblProductInventory WHERE product_id = ? AND size = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, shoeSize);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public List<InventoryDTO> getInventoryByProductId(int productId) {
        List<InventoryDTO> list = new ArrayList<>();
        String sql = "SELECT size, quantity FROM tblProductInventory WHERE product_id = ?";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InventoryDTO inv = new InventoryDTO();
                    inv.setSize(rs.getInt("size"));
                    inv.setQuantity(rs.getInt("quantity"));
                    list.add(inv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateInventory(int productId, int size, int quantity) {
        // Nếu tồn tại bản ghi thì UPDATE, nếu không thì INSERT.
        // Ví dụ đơn giản: thực hiện DELETE cũ và INSERT mới (hoặc dùng MERGE)
        String deleteSql = "DELETE FROM tblProductInventory WHERE product_id = ? AND size = ?";
        String insertSql = "INSERT INTO tblProductInventory(product_id, size, quantity) VALUES(?,?,?)";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement psDel = conn.prepareStatement(deleteSql);
                PreparedStatement psIns = conn.prepareStatement(insertSql)) {
            psDel.setInt(1, productId);
            psDel.setInt(2, size);
            psDel.executeUpdate();

            psIns.setInt(1, productId);
            psIns.setInt(2, size);
            psIns.setInt(3, quantity);
            return psIns.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countProducts(String brand, String search, String category) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM tblProducts WHERE 1=1 ");
        if (brand != null && !brand.isEmpty()) {
            sql.append(" AND brand = ? ");
        }
        if (search != null && !search.isEmpty()) {
            sql.append(" AND product_name LIKE ? ");
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ? ");
        }
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (brand != null && !brand.isEmpty()) {
                ps.setString(idx++, brand);
            }
            if (search != null && !search.isEmpty()) {
                ps.setString(idx++, "%" + search + "%");
            }
            if (category != null && !category.isEmpty()) {
                ps.setString(idx++, category);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM tblProducts ORDER BY created_at DESC";
        try (Connection conn = DBUtils.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductDTO p = new ProductDTO(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("brand"),
                        rs.getString("category"),
                        rs.getString("price"), // Giá dưới dạng String
                        rs.getString("size"),
                        rs.getInt("quantity"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        rs.getString("image_base64")
                );
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
