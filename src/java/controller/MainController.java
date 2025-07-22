package controller;

import dao.ProductDAO;
import dao.UserDAO;
import dao.CartDAO;
import dto.ProductDTO;
import dto.UserDTO;
import dto.CartItemDTO;
import utils.EmailUtils;
import utils.HashUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
@MultipartConfig
public class MainController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CartDAO cartDAO = new CartDAO(); // Quản lý giỏ hàng trong DB (nếu đăng nhập)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // Hàm tự viết để đọc InputStream (thay thế readAllBytes() của Java 9+)
    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String url = "index.jsp";

        try {
            if (action == null) {
                url = "index.jsp";
            } else {
                switch (action) {
                    case "login": {
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");
                        UserDTO user = userDAO.checkLogin(username, password);
                        if (user != null) {
                            session.setAttribute("USER", user);
                            url = "index.jsp";
                        } else {
                            request.setAttribute("LOGIN_ERROR", "Username hoặc mật khẩu không hợp lệ.");
                            request.setAttribute("SHOW_LOGIN_DROPDOWN", true);
                            url = "index.jsp";
                        }
                        break;
                    }
                    case "logout": {
                        session.invalidate();
                        url = "index.jsp";
                        break;
                    }
                    case "register": {
                        String username = request.getParameter("username").trim();
                        String password = request.getParameter("password").trim();
                        String fullName = request.getParameter("fullName").trim();
                        String email = request.getParameter("email").trim();
                        String phone = request.getParameter("phone").trim();
                        String address = request.getParameter("address").trim();                        
                        if (userDAO.usernameExists(username)) {
                            request.setAttribute("REGISTER_MSG", "Username already exists.");
                            url = "register.jsp";
                            break;
                        }
                        if (userDAO.emailExists(email)) {
                            request.setAttribute("REGISTER_MSG", "Email already used.");
                            url = "register.jsp";
                            break;
                        }
                        
                        String verifyCode = userDAO.registerUser(username, password, fullName, email, phone, address);

                        if (verifyCode != null) {
                            
                            String loginUrl = "http://localhost:8080/Final_assignment/index.jsp";

                            
                            boolean sent = EmailUtils.sendWelcomeEmail(email, fullName, username, loginUrl);
                            if (sent) {
                                request.setAttribute("REGISTER_MSG", "Register success! A welcome email has been sent to your inbox.");
                            } else {
                                request.setAttribute("REGISTER_MSG", "Register success, but failed to send welcome email.");
                            }
                        } else {
                            request.setAttribute("REGISTER_MSG", "Register failed.");
                        }
                        url = "register.jsp";
                        break;
                    }
                    case "verify": {
                        String code = request.getParameter("code");
                        boolean verified = userDAO.verifyEmail(code);
                        if (verified) {
                            request.setAttribute("VERIFY_MSG", "Your email has been verified. You can now login.");
                        } else {
                            request.setAttribute("VERIFY_MSG", "Invalid or expired verification link.");
                        }
                        url = "verify.jsp";
                        break;
                    }
                    case "viewProducts": {
                        String brand = request.getParameter("brand");
                        String search = request.getParameter("search");
                        String category = request.getParameter("category");
                        int pageIndex = 1;
                        int pageSize = 8;
                        if (request.getParameter("page") != null) {
                            pageIndex = Integer.parseInt(request.getParameter("page"));
                        }
                        int totalProducts = productDAO.countProducts(brand, search, category);
                        int totalPages = (totalProducts + pageSize - 1) / pageSize;
                        List<ProductDTO> list = productDAO.getProducts(brand, search, category, pageIndex, pageSize);

                        request.setAttribute("PRODUCT_LIST", list);
                        request.setAttribute("pageIndex", pageIndex);
                        request.setAttribute("totalPages", totalPages);
                        url = "products.jsp";
                        break;
                    }
                    case "productDetail": {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        ProductDTO product = productDAO.getProductById(productId);
                        request.setAttribute("PRODUCT", product);
                        url = "productDetail.jsp";
                        break;
                    }
                    case "viewFlashSale": {
                        url = "products.jsp";
                        break;
                    }
                    case "viewBlog": {
                        url = "blog.jsp";
                        break;
                    }
                    case "adminListProductsForUpdate": {
                        try {
                            UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                            if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("Admin")) {
                                request.setAttribute("ADMIN_ERROR", "Bạn không có quyền truy cập chức năng này.");
                                url = "index.jsp";
                                break;
                            }
                            List<ProductDTO> productList = productDAO.getAllProducts();
                            System.out.println("Số lượng sản phẩm lấy được: " + productList.size());
                            request.setAttribute("PRODUCT_LIST", productList);
                            url = "adminUpdateSelect.jsp";
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            request.setAttribute("ADMIN_ERROR", "Có lỗi xảy ra: " + ex.getMessage());
                            url = "adminUpdateSelect.jsp";
                        }
                        break;
                    }
                    // --- Admin Actions ---
                    case "loadProductForUpdate": {
                        try {
                            int productId = Integer.parseInt(request.getParameter("productId"));
                            ProductDTO product = productDAO.getProductById(productId);
                            if (product != null) {
                                request.setAttribute("PRODUCT", product);
                                url = "adminUpdateForm.jsp";
                            } else {
                                request.setAttribute("ADMIN_ERROR", "Sản phẩm không tồn tại.");
                                url = "adminUpdateSelect.jsp";
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            request.setAttribute("ADMIN_ERROR", "Có lỗi xảy ra: " + ex.getMessage());
                            url = "adminUpdateSelect.jsp";
                        }
                        break;
                    }
                    case "adminUpdateProduct": {
                        try {
                            UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                            if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("Admin")) {
                                request.setAttribute("ADMIN_ERROR", "Bạn không có quyền truy cập chức năng này.");
                                url = "index.jsp";
                                break;
                            }
                            int productId = Integer.parseInt(request.getParameter("productId"));
                            ProductDTO existingProduct = productDAO.getProductById(productId);
                            if (existingProduct == null) {
                                request.setAttribute("ADMIN_ERROR", "Sản phẩm không tồn tại.");
                                url = "adminUpdateForm.jsp";
                                break;
                            }
                            String productName = request.getParameter("productName") != null ? request.getParameter("productName").trim() : "";
                            String brand = request.getParameter("brand") != null ? request.getParameter("brand").trim() : "";
                            String category = request.getParameter("category") != null ? request.getParameter("category").trim() : "";
                            String priceStr = request.getParameter("price") != null ? request.getParameter("price").trim() : "";
                            String size = request.getParameter("size") != null ? request.getParameter("size").trim() : "";
                            String quantityStr = request.getParameter("quantity") != null ? request.getParameter("quantity").trim() : "";
                            String description = request.getParameter("description") != null ? request.getParameter("description").trim() : "";

                            if (productName.isEmpty()) {
                                productName = existingProduct.getProductName();
                            }
                            if (brand.isEmpty()) {
                                brand = existingProduct.getBrand();
                            }
                            if (category.isEmpty()) {
                                category = existingProduct.getCategory();
                            }
                            if (priceStr.isEmpty()) {
                                priceStr = existingProduct.getPrice();
                            }
                            if (size.isEmpty()) {
                                size = existingProduct.getSize();
                            }
                            int quantity = 0;
                            try {
                                quantity = quantityStr.isEmpty() ? existingProduct.getQuantity() : Integer.parseInt(quantityStr);
                            } catch (Exception ex) {
                                quantity = existingProduct.getQuantity();
                            }
                            if (description.isEmpty()) {
                                description = existingProduct.getDescription();
                            }
                            Part imageFile = request.getPart("imageFile");
                            String base64Image = existingProduct.getImageBase64();
                            if (imageFile != null && imageFile.getSize() > 0) {
                                InputStream is = imageFile.getInputStream();
                                byte[] bytes = readAllBytes(is);
                                base64Image = Base64.getEncoder().encodeToString(bytes);
                                is.close();
                            }

                            ProductDTO updatedProduct = new ProductDTO();
                            updatedProduct.setProductId(productId);
                            updatedProduct.setProductName(productName);
                            updatedProduct.setBrand(brand);
                            updatedProduct.setCategory(category);
                            updatedProduct.setPrice(priceStr);
                            updatedProduct.setSize(size);
                            updatedProduct.setQuantity(quantity);
                            updatedProduct.setDescription(description);
                            updatedProduct.setImageBase64(base64Image);

                            boolean result = productDAO.updateProduct(updatedProduct);
                            if (result) {
                                request.setAttribute("ADMIN_MSG", "Sản phẩm đã được cập nhật thành công!");
                            } else {
                                request.setAttribute("ADMIN_ERROR", "Cập nhật sản phẩm thất bại.");
                            }
                            url = "adminUpdateForm.jsp";
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            request.setAttribute("ADMIN_ERROR", "Có lỗi xảy ra: " + ex.getMessage());
                            url = "adminUpdateForm.jsp";
                        }
                        break;
                    }
                    case "adminAddProduct": {
                        try {
                            UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                            if (currentUser == null || !currentUser.getRole().equalsIgnoreCase("Admin")) {
                                request.setAttribute("ADMIN_ERROR", "Bạn không có quyền truy cập chức năng này.");
                                url = "index.jsp";
                                break;
                            }
                            String productName = request.getParameter("productName");
                            String brand = request.getParameter("brand");
                            String category = request.getParameter("category");
                            String priceStr = request.getParameter("price");
                            String size = request.getParameter("size");
                            String quantityStr = request.getParameter("quantity");
                            String description = request.getParameter("description");

                            if (productName == null || productName.trim().isEmpty()
                                    || brand == null || brand.trim().isEmpty()
                                    || category == null || category.trim().isEmpty()
                                    || priceStr == null || priceStr.trim().isEmpty()
                                    || quantityStr == null || quantityStr.trim().isEmpty()) {
                                request.setAttribute("ADMIN_ERROR", "Các trường Tên sản phẩm, Hãng, Loại, Giá và Số lượng không được để trống.");
                                url = "adminAddProduct.jsp";
                                break;
                            }
                            if (productDAO.productNameExists(productName.trim())) {
                                request.setAttribute("ADMIN_ERROR", "Sản phẩm đã tồn tại. Vui lòng kiểm tra lại tên sản phẩm.");
                                url = "adminAddProduct.jsp";
                                break;
                            }
                            String price = priceStr.trim();
                            int quantity = Integer.parseInt(quantityStr.trim());
                            String base64Image = null;
                            Part imageFile = request.getPart("imageFile");
                            if (imageFile != null && imageFile.getSize() > 0) {
                                InputStream is = imageFile.getInputStream();
                                byte[] bytes = readAllBytes(is);
                                base64Image = Base64.getEncoder().encodeToString(bytes);
                                is.close();
                            }
                            ProductDTO p = new ProductDTO();
                            p.setProductName(productName.trim());
                            p.setBrand(brand.trim());
                            p.setCategory(category.trim());
                            p.setPrice(price);
                            p.setSize(size != null ? size.trim() : "");
                            p.setQuantity(quantity);
                            p.setDescription(description != null ? description.trim() : "");
                            p.setImageBase64(base64Image);

                            boolean added = productDAO.addProduct(p);
                            if (added) {
                                request.setAttribute("ADMIN_MSG", "Sản phẩm được thêm thành công!");
                            } else {
                                request.setAttribute("ADMIN_ERROR", "Thêm sản phẩm thất bại. Vui lòng thử lại.");
                            }
                            url = "adminAddProduct.jsp";
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            request.setAttribute("ADMIN_ERROR", "Có lỗi xảy ra: " + ex.getMessage());
                            url = "adminAddProduct.jsp";
                        }
                        break;
                    }
                    // --- Các action mới cho Giỏ hàng ---
                    case "addToCart": {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        int quantity = 1;
                        if (request.getParameter("quantity") != null) {
                            try {
                                quantity = Integer.parseInt(request.getParameter("quantity"));
                            } catch (NumberFormatException e) {
                                quantity = 1;
                            }
                        }
                        ProductDTO product = productDAO.getProductById(productId);
                        if (product == null) {
                            request.setAttribute("CART_MSG", "Sản phẩm không tồn tại.");
                            url = "productDetail.jsp";
                            break;
                        }
                        // Nếu người dùng đã đăng nhập, thêm vào DB qua CartDAO; nếu chưa thì lưu vào session
                        UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                        if (currentUser != null) {
                            boolean result = cartDAO.addToCart(currentUser.getUserId(), productId, quantity);
                            if (result) {
                                request.setAttribute("CART_MSG", "Sản phẩm đã được thêm vào giỏ hàng.");
                            } else {
                                request.setAttribute("CART_MSG", "Thêm vào giỏ hàng thất bại.");
                            }
                        } else {
                            List<CartItemDTO> sessionCart = (List<CartItemDTO>) session.getAttribute("SESSION_CART");
                            if (sessionCart == null) {
                                sessionCart = new ArrayList<>();
                            }
                            boolean found = false;
                            for (CartItemDTO item : sessionCart) {
                                if (item.getProductId() == productId) {
                                    item.setQuantity(item.getQuantity() + quantity);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                CartItemDTO newItem = new CartItemDTO();
                                newItem.setProductId(productId);
                                newItem.setQuantity(quantity);
                                newItem.setProductName(product.getProductName());
                                newItem.setBrand(product.getBrand());                          
                                newItem.setPrice(product.getPrice());
                                newItem.setImageBase64(product.getImageBase64());
                                sessionCart.add(newItem);
                            }
                            session.setAttribute("SESSION_CART", sessionCart);
                            request.setAttribute("CART_MSG", "Sản phẩm đã được thêm vào giỏ hàng (Session).");
                        }
                        url = "MainController?action=viewCart";
                        break;
                    }
                    case "deleteFromCart": {
                        int productId = Integer.parseInt(request.getParameter("productId"));
                        UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                        if (currentUser != null) {
                            boolean result = cartDAO.removeFromCart(currentUser.getUserId(), productId);
                            if (result) {
                                request.setAttribute("CART_MSG", "Sản phẩm đã được xóa khỏi giỏ hàng.");
                            } else {
                                request.setAttribute("CART_MSG", "Xóa sản phẩm thất bại.");
                            }
                        } else {
                            List<CartItemDTO> sessionCart = (List<CartItemDTO>) session.getAttribute("SESSION_CART");
                            if (sessionCart != null) {
                                for (int i = 0; i < sessionCart.size(); i++) {
                                    if (sessionCart.get(i).getProductId() == productId) {
                                        sessionCart.remove(i);
                                        break;
                                    }
                                }
                                session.setAttribute("SESSION_CART", sessionCart);
                                request.setAttribute("CART_MSG", "Sản phẩm đã được xóa khỏi giỏ hàng (Session).");
                            }
                        }
                        url = "MainController?action=viewCart";
                        break;
                    }
                    case "viewCart": {
                        UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                        if (currentUser != null) {
                            
                            List<CartItemDTO> cartItems = cartDAO.getCartItemsByUser(currentUser.getUserId());
                            System.out.println("Cart in DB, size = " + (cartItems != null ? cartItems.size() : 0));
                            request.setAttribute("CART_ITEMS", cartItems);
                        } else {
                            
                            List<CartItemDTO> sessionCart = (List<CartItemDTO>) session.getAttribute("SESSION_CART");
                            System.out.println("Cart in Session, size = " + (sessionCart != null ? sessionCart.size() : 0));
                            request.setAttribute("CART_ITEMS", sessionCart);
                        }
                        url = "cart.jsp";
                        break;
                    }
                    case "checkout": {
                        UserDTO currentUser = (UserDTO) session.getAttribute("USER");
                        if (currentUser == null) {
                            request.setAttribute("CHECKOUT_MSG", "Bạn cần đăng nhập để thanh toán.");
                            request.setAttribute("CHECKOUT_ERROR", true);
                            url = "cart.jsp";
                        } else {
                            
                            request.setAttribute("CHECKOUT_MSG", "Thanh toán thành công, cảm ơn bạn đã mua hàng.");
                            
                            url = "cart.jsp";
                        }
                        break;
                    }
                    default:
                        url = "index.jsp";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
    }
}
