package view;

import controller.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RegisterFrmNew extends javax.swing.JFrame {

    // --- VARIABLES DECLARATION ---
    private javax.swing.JComboBox<ImageIcon> avatarComboBox;
    private javax.swing.JPasswordField jPasswordValue;
    private javax.swing.JTextField nicknameValue;
    private javax.swing.JTextField usernameValue;
    private javax.swing.JButton registerButton;
    private javax.swing.JLabel loginLabel;

    public RegisterFrmNew() {
        // 1. Khởi tạo giao diện
        initModernComponents();
        
        // 2. Cài đặt cửa sổ
        this.setTitle("Đăng Ký Tài Khoản - Caro Game");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Kích thước rộng rãi hơn
        this.setSize(550, 680); 
        this.setLocationRelativeTo(null); 
        
        // 3. Load Avatar
        loadAvatars();
    }

    private void loadAvatars() {
        avatarComboBox.removeAllItems();
        for (int i = 0; i <= 5; i++) {
            try {
                // Resize icon avatar to 50x50 for better list view
                ImageIcon originalIcon = new ImageIcon("assets/avatar/" + i + ".jpg");
                Image img = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                avatarComboBox.addItem(new ImageIcon(img));
            } catch (Exception e) {
                // Ignore if image error
            }
        }
    }

    private void initModernComponents() {
        // Nền chính
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout()); // Căn giữa mọi thứ
        setContentPane(mainPanel);

        // Container bo tròn (Rounded Panel)
        RoundedPanel container = new RoundedPanel(30, new Color(0, 0, 0, 160));
        container.setLayout(new GridBagLayout());
        container.setPreferredSize(new Dimension(420, 580));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20); // Margin: Trên, Trái, Dưới, Phải
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        // --- 1. TIÊU ĐỀ ---
        JLabel titleLabel = new JLabel("ĐĂNG KÝ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 193, 7)); // Màu vàng
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20); // Cách xa bên trên
        container.add(titleLabel, gbc);

        // --- 2. TÀI KHOẢN ---
        gbc.insets = new Insets(5, 20, 2, 20); // Reset margin
        gbc.gridy++;
        container.add(createLabel("Tài khoản:"), gbc);
        
        gbc.gridy++;
        usernameValue = createTextField();
        container.add(usernameValue, gbc);

        // --- 3. MẬT KHẨU ---
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 2, 20); // Cách xa ô trên một chút
        container.add(createLabel("Mật khẩu:"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(2, 20, 2, 20);
        jPasswordValue = createPasswordField();
        container.add(jPasswordValue, gbc);

        // --- 4. NICKNAME ---
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 2, 20);
        container.add(createLabel("Tên hiển thị (Nickname):"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(2, 20, 2, 20);
        nicknameValue = createTextField();
        container.add(nicknameValue, gbc);

        // --- 5. AVATAR ---
        gbc.gridy++;
        gbc.insets = new Insets(10, 20, 2, 20);
        container.add(createLabel("Chọn Avatar:"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(2, 20, 2, 20);
        avatarComboBox = new JComboBox<>();
        avatarComboBox.setPreferredSize(new Dimension(300, 60));
        avatarComboBox.setBackground(Color.WHITE);
        container.add(avatarComboBox, gbc);

        // --- 6. NÚT ĐĂNG KÝ ---
        gbc.gridy++;
        gbc.insets = new Insets(25, 20, 10, 20);
        registerButton = new JButton("TẠO TÀI KHOẢN");
        styleButton(registerButton);
        registerButton.addActionListener(this::registerButtonActionPerformed);
        container.add(registerButton, gbc);

        // --- 7. LINK ĐĂNG NHẬP ---
        gbc.gridy++;
        gbc.insets = new Insets(5, 20, 20, 20);
        loginLabel = new JLabel("Đã có tài khoản? Đăng nhập ngay");
        loginLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        loginLabel.setForeground(new Color(100, 181, 246));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                loginLabelMouseClicked(evt);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLabel.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                loginLabel.setForeground(new Color(100, 181, 246));
            }
        });
        container.add(loginLabel, gbc);

        // Thêm container vào nền chính
        mainPanel.add(container);
    }

    // --- HELPER METHODS ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(220, 220, 220));
        return lbl;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 50)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        // Màu nền ô nhập liệu hơi tối
        field.setBackground(new Color(255, 255, 255, 240)); 
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 50)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 240));
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(46, 204, 113)); // Xanh lá
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- LOGIC GIỮ NGUYÊN ---
    private void loginLabelMouseClicked(java.awt.event.MouseEvent evt) {                                        
        Client.closeView(Client.View.REGISTER);
        Client.openView(Client.View.LOGIN);
    }                                       

    private void registerButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try {
            String username = usernameValue.getText();
            if (username.isEmpty()) throw new Exception("Vui lòng nhập tên tài khoản");
            String password = String.copyValueOf(jPasswordValue.getPassword());
            if (password.isEmpty()) throw new Exception("Vui lòng nhập mật khẩu");
            String nickName = nicknameValue.getText();
            int avatar = avatarComboBox.getSelectedIndex();
            if (avatar == -1) throw new Exception("Vui lòng chọn avatar");
            if (nickName.isEmpty()) throw new Exception("Vui lòng nhập nickname");
            
            Client.closeAllViews();
            Client.openView(Client.View.GAME_NOTICE, "Đăng kí tài khoản", "Đang chờ phản hồi");
            Client.socketHandle.write("register," + username + "," + password + "," + nickName + "," + avatar);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }   

    // --- CLASSES ĐỂ VẼ GIAO DIỆN ---
    
    // 1. Panel hình nền
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        public BackgroundPanel(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) backgroundImage = ImageIO.read(file);
            } catch (IOException e) {}
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(40, 40, 40), 0, getHeight(), new Color(10, 10, 10)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // 2. Panel bo tròn góc (Tạo hiệu ứng đẹp)
    class RoundedPanel extends JPanel {
        private Color backgroundColor;
        private int cornerRadius = 15;
        
        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false); // Để vẽ trong suốt được
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ nền bo tròn
            if (backgroundColor != null) {
                graphics.setColor(backgroundColor);
            } else {
                graphics.setColor(getBackground());
            }
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); 
            
            // Vẽ viền (tùy chọn)
            graphics.setColor(new Color(255, 255, 255, 50));
            graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); 
        }
    }
}