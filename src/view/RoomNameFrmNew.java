package view;

import controller.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Vào phòng bằng ID - Thiết kế lại hiện đại
 */
public class RoomNameFrmNew extends javax.swing.JFrame {

    // --- VARIABLES DECLARATION (GIỮ NGUYÊN TÊN ĐỂ KHÔNG LỖI LOGIC) ---
    private javax.swing.JLabel findingRoomLabel;
    private javax.swing.JButton goToRoomButton;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel passwordNotificationLabl;
    private javax.swing.JTextField passwordValue; // Giữ là JTextField hoặc JPasswordField đều được
    private javax.swing.JLabel roomIdLabel;
    private javax.swing.JTextField roomIdValue;
    private javax.swing.JLabel frameTitle;

    public RoomNameFrmNew() {
        // Thay thế initComponents() bằng hàm khởi tạo giao diện mới
        initModernComponents();
        
        this.setTitle("Vào Phòng Theo ID");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(450, 600);
        // Ẩn label trạng thái ban đầu
        findingRoomLabel.setVisible(false);
    }

    private void initModernComponents() {
        // 1. Nền
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Container (Hộp chứa nội dung)
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0, 0, 0, 180)); // Đen mờ
        container.setBorder(new EmptyBorder(30, 40, 30, 40));
        container.setPreferredSize(new Dimension(400, 420));

        // --- Tiêu đề ---
        frameTitle = new JLabel("VÀO PHÒNG NGAY");
        frameTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        frameTitle.setForeground(new Color(255, 193, 7)); // Màu vàng
        frameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(frameTitle);
        
        container.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Mã phòng ---
        roomIdLabel = createLabel("Nhập Mã Phòng (ID):");
        container.add(roomIdLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        
        roomIdValue = createTextField();
        container.add(roomIdValue);
        
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Mật khẩu ---
        passwordLabel = createLabel("Mật Khẩu Phòng (Nếu có):");
        container.add(passwordLabel);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Dùng JTextField để khớp với code cũ, nhưng giao diện vẫn đẹp
        passwordValue = createTextField(); 
        container.add(passwordValue);
        
        container.add(Box.createRigidArea(new Dimension(0, 5)));

        // Ghi chú nhỏ
        passwordNotificationLabl = new JLabel("Để trống nếu phòng không có mật khẩu");
        passwordNotificationLabl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        passwordNotificationLabl.setForeground(new Color(150, 150, 150));
        passwordNotificationLabl.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(passwordNotificationLabl);

        container.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Nút vào phòng ---
        goToRoomButton = new JButton("VÀO PHÒNG");
        styleButton(goToRoomButton);
        goToRoomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        goToRoomButton.setMaximumSize(new Dimension(300, 45));
        
        // Sự kiện click
        goToRoomButton.addActionListener(this::goToRoomButtonActionPerformed);
        
        // Sự kiện phím Enter (Tiện lợi hơn)
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    goToRoomButtonActionPerformed(null);
                }
            }
        };
        roomIdValue.addKeyListener(enterKeyAdapter);
        passwordValue.addKeyListener(enterKeyAdapter);

        container.add(goToRoomButton);
        
        container.add(Box.createRigidArea(new Dimension(0, 15)));

        // --- Label Trạng thái ---
        findingRoomLabel = new JLabel("Đang tìm kiếm phòng...");
        findingRoomLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        findingRoomLabel.setForeground(new Color(46, 204, 113)); // Xanh lá
        findingRoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(findingRoomLabel);

        mainPanel.add(container);
    }

    // --- HELPER METHODS ---
    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        field.setMaximumSize(new Dimension(320, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(52, 152, 219)); // Xanh dương
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- LOGIC GIỮ NGUYÊN TỪ CODE CŨ ---

    private void goToRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        String roomName = roomIdValue.getText();
        if (roomName.isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập mã phòng");
            return;
        }
        try {
            String password = " ";
            if (!passwordValue.getText().isEmpty()) {
                password = passwordValue.getText();
            }
            findingRoomLabel.setVisible(true); // Hiển thị thông báo đang tìm
            Client.socketHandle.write("go-to-room," + roomName + "," + password);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }   

    // Class hình nền (Sử dụng lại)
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