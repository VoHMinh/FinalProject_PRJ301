package dao;

import dto.UserDTO;
import utils.DBUtils;
import utils.HashUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class UserDAO {

    public UserDTO checkLogin(String username, String passwordPlain) {
        String sql = "SELECT * FROM tblUsers WHERE username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (HashUtils.checkPassword(passwordPlain, storedHash)) {
                        return new UserDTO(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            storedHash,
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getString("role"),
                            rs.getBoolean("is_verified")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String registerUser(String username, String passwordPlain, String fullName,
                               String email, String phone, String address) {
        String verifyCode = UUID.randomUUID().toString();
        String hashedPass = HashUtils.hashPassword(passwordPlain);
        String sql = "INSERT INTO tblUsers(username, password, full_name, email, phone, address, role, is_verified) VALUES(?,?,?,?,?,?,'User',0)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashedPass);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setString(6, address);
            int row = ps.executeUpdate();
            if (row > 0) {
                return verifyCode; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean usernameExists(String username) {
        String sql = "SELECT username FROM tblUsers WHERE username = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean emailExists(String email) {
        String sql = "SELECT email FROM tblUsers WHERE email = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean verifyEmail(String code) {
        String sql = "UPDATE tblUsers SET is_verified = 1 WHERE verify_code = ? AND is_verified = 0";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
