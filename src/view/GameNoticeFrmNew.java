package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Thông báo (Loading/Notice) - Đã làm đẹp
 */
public class GameNoticeFrmNew extends javax.swing.JFrame {

    // Giữ nguyên Constructor để không lỗi code bên Client
    public GameNoticeFrmNew(String title, String message) {
        // Thay vì gọi initComponents() cũ, ta gọi hàm dựng giao diện mới
        initModernComponents(title, message);
        
        // Các thiết lập cơ bản giữ nguyên
        this.setTitle("Caro Game");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 300); // Chỉnh kích thước cho cân đối
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
    }

    // Hàm dựng giao diện mới (Thay thế initComponents)
    private void initModernComponents(String title, String message) {
        // 1. Tạo nền (Background)
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout()); // Dùng GridBag để căn giữa nội dung
        setContentPane(mainPanel);

        // 2. Tạo hộp chứa nội dung (Màu đen trong suốt)
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setBackground(new Color(0, 0, 0, 180)); // Đen mờ (Alpha 180)
        contentBox.setBorder(new EmptyBorder(20, 30, 20, 30)); // Padding xung quanh
        contentBox.setPreferredSize(new Dimension(320, 220));

        // --- Tiêu đề (Title) ---
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(255, 193, 7)); // Màu vàng
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(lblTitle);

        contentBox.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách

        // --- Icon Loading ---
        JLabel lblIcon = new JLabel();
        try {
            // Load icon loading (Giữ nguyên path cũ của bạn)
            lblIcon.setIcon(new ImageIcon("assets/icon/loading2.gif"));
        } catch (Exception e) {
            lblIcon.setText("Loading...");
            lblIcon.setForeground(Color.WHITE);
        }
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(lblIcon);

        contentBox.add(Box.createRigidArea(new Dimension(0, 20))); // Khoảng cách

        // --- Nội dung tin nhắn (Message) ---
        JLabel lblMessage = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        lblMessage.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblMessage.setForeground(Color.WHITE); // Chữ trắng
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentBox.add(lblMessage);

        // Thêm hộp nội dung vào màn hình chính
        mainPanel.add(contentBox);
    }

    // Class xử lý hình nền (Dùng chung với các form khác)
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
                }
            } catch (IOException e) {
                // Không in lỗi để tránh spam console nếu chưa có ảnh
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Nếu không có ảnh, dùng màu nền tối gradient
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(50, 50, 50), 
                                                     0, getHeight(), new Color(0, 0, 0));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}