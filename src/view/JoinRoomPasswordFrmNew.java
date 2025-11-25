package view;

import controller.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Nhập mật khẩu phòng - Hiện đại
 */
public class JoinRoomPasswordFrmNew extends javax.swing.JFrame {
    private int room;
    private String password;

    // Các biến giao diện (Giữ nguyên tên để không lỗi logic)
    private JButton exitButton;
    private JLabel frameLabel;
    private JButton goToRoomButton;
    private JTextField passwordInput;
    private JLabel passwordLabel;

    public JoinRoomPasswordFrmNew(int room, String password) {
        this.room = room;
        this.password = password;
        
        // Setup cơ bản
        this.setTitle("Xác thực phòng riêng tư");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);

        // Khởi tạo giao diện mới
        initModernComponents();
        
        // Logic cũ (Gán sự kiện)
        setupEvents();
    }

    private void initModernComponents() {
        // 1. Nền
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Hộp chứa nội dung (Glassmorphism)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0, 0, 0, 180)); // Đen mờ
        contentPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        contentPanel.setPreferredSize(new Dimension(320, 220));

        // --- Tiêu đề ---
        frameLabel = new JLabel("PHÒNG KÍN");
        frameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        frameLabel.setForeground(new Color(255, 193, 7)); // Màu vàng
        frameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(frameLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel subLabel = new JLabel("Phòng #" + room);
        subLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subLabel.setForeground(Color.LIGHT_GRAY);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Ô nhập mật khẩu ---
        passwordLabel = new JLabel("Nhập mật khẩu:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(passwordLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        passwordInput = new JPasswordField(); // Dùng PasswordField để che mật khẩu
        passwordInput.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passwordInput.setHorizontalAlignment(JTextField.CENTER);
        passwordInput.setMaximumSize(new Dimension(250, 35));
        contentPanel.add(passwordInput);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(250, 40));

        exitButton = new JButton("Hủy");
        styleButton(exitButton, new Color(192, 57, 43)); // Đỏ

        goToRoomButton = new JButton("Vào ngay");
        styleButton(goToRoomButton, new Color(46, 204, 113)); // Xanh lá

        buttonPanel.add(exitButton);
        buttonPanel.add(goToRoomButton);
        
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel);
    }
    
    private void setupEvents() {
        goToRoomButton.addActionListener(this::goToRoomButtonActionPerformed);
        exitButton.addActionListener(this::exitButtonActionPerformed);
        
        // Cho phép ấn Enter để vào phòng ngay
        passwordInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == 10) { // Enter key
                     goToRoomButtonActionPerformed(null);
                }
            }
        });
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- LOGIC GIỮ NGUYÊN ---

    private void goToRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try {
            String inputPass = passwordInput.getText();
            if (!inputPass.equals(this.password))
                throw new Exception("Mật khẩu không chính xác");
            Client.socketHandle.write("join-room," + this.room);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }                                              

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        Client.closeView(Client.View.JOIN_ROOM_PASSWORD);
        Client.openView(Client.View.HOMEPAGE);
    }                                          

    // Class hình nền
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
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
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}