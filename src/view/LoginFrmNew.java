package view;

import controller.Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Giao diện Đăng nhập hiện đại
 */
public class LoginFrmNew extends javax.swing.JFrame {
    // Các component giao diện mới
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    /**
     * Constructor mặc định
     */
    public LoginFrmNew() {
        commonInit();
    }

    /**
     * Constructor có điền sẵn thông tin (cho chức năng ghi nhớ/re-login)
     */
    public LoginFrmNew(String taiKhoan, String matKhau) {
        commonInit();
        txtUsername.setText(taiKhoan);
        txtPassword.setText(matKhau);
    }

    private void commonInit() {
        this.setTitle("Đăng nhập - Caro Game");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(800, 500); // Kích thước cửa sổ to hơn chút cho thoáng
        this.setLocationRelativeTo(null);

        // Khởi tạo giao diện
        initModernComponents();
        
        // Thêm sự kiện nhấn Enter để đăng nhập
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);
    }

    private void initModernComponents() {
        // 1. Nền chính
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Card Đăng nhập (Khung mờ ở giữa)
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new GridBagLayout());
        loginCard.setBackground(new Color(0, 0, 0, 160)); // Màu đen trong suốt
        loginCard.setBorder(new LineBorder(new Color(255, 255, 255, 50), 1)); // Viền mỏng
        
        // Layout constraints cho nội dung bên trong Card
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 30, 20);
        loginCard.add(lblTitle, gbc);

        // --- Ô nhập Tài khoản ---
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 20, 5, 20);
        JLabel lblUser = new JLabel("Tài khoản:");
        lblUser.setForeground(new Color(200, 200, 200));
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginCard.add(lblUser, gbc);

        gbc.gridy++;
        txtUsername = createStyledTextField();
        loginCard.add(txtUsername, gbc);

        // --- Ô nhập Mật khẩu ---
        gbc.gridy++;
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setForeground(new Color(200, 200, 200));
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginCard.add(lblPass, gbc);

        gbc.gridy++;
        txtPassword = createStyledPasswordField();
        loginCard.add(txtPassword, gbc);

        // --- Các nút bấm ---
        gbc.gridy++;
        gbc.insets = new Insets(25, 20, 10, 20);
        
        btnLogin = createStyledButton("ĐĂNG NHẬP", new Color(46, 204, 113)); // Màu xanh lá
        loginCard.add(btnLogin, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 30, 20);
        btnRegister = createStyledButton("Đăng ký tài khoản mới", new Color(52, 152, 219)); // Màu xanh dương
        // Làm nút đăng ký nhỏ hơn chút hoặc style khác nếu muốn
        btnRegister.setFont(new Font("Segoe UI", Font.ITALIC, 13));
  // 3 DÒNG QUAN TRỌNG ĐỂ KHẮC PHỤC LỖI NỀN TRẮNG:
        btnRegister.setContentAreaFilled(false); // Không vẽ nền (giải quyết lỗi trong ảnh)
        btnRegister.setBorderPainted(false);     // Không vẽ khung viền
        btnRegister.setFocusPainted(false);      // Không vẽ viền khi click
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Hiệu ứng hover cho text link
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegister.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegister.setForeground(new Color(100, 200, 255));
            }
        });
        
        loginCard.add(btnRegister, gbc);

        // Thêm Card vào màn hình chính
        mainPanel.add(loginCard);

        // --- Action Listeners ---
        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> {
            Client.closeView(Client.View.LOGIN);
            Client.openView(Client.View.REGISTER);
        });
    }

    // --- LOGIC ĐĂNG NHẬP (Giữ nguyên logic cũ) ---
    private void performLogin() {
        try {
            String taiKhoan = txtUsername.getText();
            if (taiKhoan.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập tài khoản!");
                return;
            }
            String matKhau = String.copyValueOf(txtPassword.getPassword());
            if (matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mật khẩu!");
                return;
            }
            
            Client.closeAllViews();
            Client.openView(Client.View.GAME_NOTICE, "Đăng nhập", "Đang xác thực thông tin...");
            Client.socketHandle.write("client-verify," + taiKhoan + "," + matKhau);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(rootPane, message);
    }

    public void log(String message) {
        JOptionPane.showMessageDialog(rootPane, "ID của server thread là:" + message);
    }

    // --- CÁC HÀM STYLE GIAO DIỆN ---
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100)), 
            new EmptyBorder(5, 10, 5, 10) // Padding text
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover nút thường
        if (!text.contains("Đăng ký")) { 
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(bg.brighter());
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(bg);
                }
            });
        }
        return btn;
    }

    // --- CLASS HÌNH NỀN (Tái sử dụng) ---
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                // Ưu tiên tìm trong thư mục assets (chạy file jar/IDE)
                File f = new File(imagePath);
                if (f.exists()) {
                     backgroundImage = ImageIO.read(f);
                } else {
                    // Fallback: Tìm trong resources
                    java.net.URL imgURL = getClass().getResource("/" + imagePath);
                    if (imgURL != null) backgroundImage = ImageIO.read(imgURL);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Gradient nền tối mặc định nếu không có ảnh
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 30), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}