package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils {

    
    public static boolean sendWelcomeEmail(String toEmail, String fullName, String username, String loginUrl) {
        try {
            
            final String fromEmail = "minh7n3@gmail.com"; // Thay bằng email thật của bạn
            final String password = "zgfjpmgkdloxemdu";      // App Password của Gmail

            // Cấu hình thuộc tính SMTP cho Gmail
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Tạo session với Authenticator để xác thực
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            // Tạo tiêu đề và nội dung email dạng HTML
            String subject = "Welcome to Our Website!";
            String htmlContent = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "    <meta charset='UTF-8'/>"
                    + "    <style>"
                    + "        .btn {"
                    + "            display: inline-block;"
                    + "            padding: 10px 20px;"
                    + "            background-color: #4CAF50;"
                    + "            color: #fff;"
                    + "            text-decoration: none;"
                    + "            border-radius: 4px;"
                    + "        }"
                    + "        .btn:hover {"
                    + "            background-color: #45a049;"
                    + "        }"
                    + "    </style>"
                    + "</head>"
                    + "<body>"
                    + "    <h2>Welcome to Our Website!</h2>"
                    + "    <p>Hello, <strong>" + fullName + "</strong>!</p>"
                    + "    <p>Thank you for registering with our website. Your account has been successfully created.</p>"
                    + "    <p>Your login information:</p>"
                    + "    <ul>"
                    + "        <li>Username: <strong>" + username + "</strong></li>"
                    + "    </ul>"
                    + "    <p>You can now login to your account and start exploring our services.</p>"
                    + "    <p>"
                    + "        <a class='btn' href='" + loginUrl + "'>Login to Your Account</a>"
                    + "    </p>"
                    + "    <br/>"
                    + "    <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>"
                    + "    <p>Best regards,<br/>The Team</p>"
                    + "</body>"
                    + "</html>";

            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            
            Transport.send(message);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public static boolean sendVerificationEmail(String toEmail, String subject, String content) {
        
        return false;
    }
}
