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
 * Giao diện Tạo phòng có mật khẩu (Design by Admin)
 */
public class CreateRoomPasswordFrmNew extends javax.swing.JFrame {

    private JTextField txtPassword;
    private JButton btnCreate;
    private JButton btnBack;

    public CreateRoomPasswordFrmNew() {
        // Cài đặt cơ bản cho JFrame
        this.setTitle("Tạo phòng riêng - Caro Game");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 250); // Kích thước nhỏ gọn
        this.setLocationRelativeTo(null);

        // Khởi tạo giao diện mới
        initModernComponents();
        
        // Thêm sự kiện nhấn Enter để tạo nhanh
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnCreate.doClick();
                }
            }
        });
    }

    private void initModernComponents() {
        // 1. Nền chính
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Card Panel (Khung chứa nội dung)
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(new Color(0, 0, 0, 180)); // Đen trong suốt
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setPreferredSize(new Dimension(320, 180));

        // --- Header ---
        JLabel lblTitle = new JLabel("CÀI ĐẶT MẬT KHẨU", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(255, 204, 0)); // Màu vàng
        cardPanel.add(lblTitle, BorderLayout.NORTH);

        // --- Body (Ô nhập liệu) ---
        JPanel bodyPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bodyPanel.setOpaque(false);
        bodyPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblInstruct = new JLabel("Nhập mật khẩu cho phòng:");
        lblInstruct.setForeground(Color.WHITE);
        lblInstruct.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        txtPassword = new JTextField();
        txtPassword.setFont(new Font("Segoe UI", Font.BOLD, 16));
        txtPassword.setHorizontalAlignment(JTextField.CENTER);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100)),
            new EmptyBorder(5, 5, 5, 5)
        ));

        bodyPanel.add(lblInstruct);
        bodyPanel.add(txtPassword);
        cardPanel.add(bodyPanel, BorderLayout.CENTER);

        // --- Footer (Nút bấm) ---
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        // Nút Quay lại
        btnBack = createStyledButton("Hủy bỏ", new Color(100, 100, 100));
        btnBack.addActionListener(e -> {
            Client.closeView(Client.View.CREATE_ROOM_PASSWORD);
            Client.openView(Client.View.HOMEPAGE);
        });

        // Nút Tạo phòng
        btnCreate = createStyledButton("Tạo phòng", new Color(46, 204, 113));
        btnCreate.addActionListener(this::handleCreateRoom);

        buttonPanel.add(btnBack);
        buttonPanel.add(btnCreate);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(cardPanel);
    }

    // --- LOGIC XỬ LÝ ---
    
    private void handleCreateRoom(ActionEvent e) {
        try {
            String password = txtPassword.getText();
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu bạn muốn đặt cho phòng!");
                return;
            }
            Client.socketHandle.write("create-room," + password);
            Client.closeView(Client.View.CREATE_ROOM_PASSWORD);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // Hàm tạo nút bấm đẹp
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    // Class hình nền (Dùng chung với các form khác)
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File f = new File(imagePath);
                if (f.exists()) {
                    backgroundImage = ImageIO.read(f);
                } else {
                    java.net.URL imgURL = getClass().getResource("/" + imagePath);
                    if (imgURL != null) backgroundImage = ImageIO.read(imgURL);
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
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(50, 50, 50), 
                                                     0, getHeight(), new Color(20, 20, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}