package view;

import controller.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Phòng Chờ - Thiết kế lại hiện đại
 */
public class WaitingRoomFrmNew extends javax.swing.JFrame {
    private boolean isOpenning;

    // --- VARIABLES DECLARATION (GIỮ NGUYÊN TÊN) ---
    private javax.swing.JButton loadingButtonImage; // Nút thoát
    private javax.swing.JLabel loadingIcon;
    private javax.swing.JLabel pendingMessageLabel;
    private javax.swing.JLabel roomNameLabel;
    private javax.swing.JLabel roomPasswordLabel;

    public WaitingRoomFrmNew() {
        // Init Components mới
        initModernComponents();

        this.setTitle("Phòng Chờ - Caro Game");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(450, 300);
        isOpenning = false;
        
        // Setup Icon Loading (Logic cũ)
        try {
            loadingIcon.setIcon(new ImageIcon("assets/icon/loading2.gif"));
        } catch (Exception e) {
            loadingIcon.setText("Loading...");
        }
        
        // Ẩn mật khẩu mặc định
        roomPasswordLabel.setVisible(false);
    }

    private void initModernComponents() {
        // 1. Nền
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Container (Glassmorphism)
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(0, 0, 0, 180)); // Đen mờ
        container.setBorder(new EmptyBorder(30, 40, 30, 40));
        container.setPreferredSize(new Dimension(400, 350));

        // --- Tiêu đề Phòng ---
        roomNameLabel = new JLabel("Phòng ???");
        roomNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        roomNameLabel.setForeground(new Color(255, 193, 7)); // Vàng
        roomNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(roomNameLabel);

        container.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- Mật khẩu (Nếu có) ---
        roomPasswordLabel = new JLabel("Mật khẩu: ???");
        roomPasswordLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        roomPasswordLabel.setForeground(new Color(46, 204, 113)); // Xanh lá
        roomPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(roomPasswordLabel);

        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Loading Icon ---
        loadingIcon = new JLabel();
        loadingIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(loadingIcon);

        container.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Thông báo trạng thái ---
        pendingMessageLabel = new JLabel("Đang chờ người chơi khác...");
        pendingMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pendingMessageLabel.setForeground(Color.WHITE);
        pendingMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(pendingMessageLabel);

        container.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Nút Thoát (loadingButtonImage) ---
        // Dù tên biến là Image nhưng ta style nó thành nút bấm đẹp
        loadingButtonImage = new JButton("Rời phòng");
        loadingButtonImage.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loadingButtonImage.setBackground(new Color(231, 76, 60)); // Đỏ
        loadingButtonImage.setForeground(Color.WHITE);
        loadingButtonImage.setFocusPainted(false);
        loadingButtonImage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadingButtonImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingButtonImage.setMaximumSize(new Dimension(200, 40));
        
        // Sự kiện click
        loadingButtonImage.addActionListener(this::loadingButtonImageActionPerformed);

        container.add(loadingButtonImage);

        mainPanel.add(container);
    }

    // --- LOGIC CŨ (GIỮ NGUYÊN) ---

    public void setRoomName(String roomName) {
        roomNameLabel.setText("Phòng " + roomName);
    }

    public void setRoomPassword(String password) {
        roomPasswordLabel.setText("Mật khẩu: " + password);
        roomPasswordLabel.setVisible(true);
    }

    public void showFoundCompetitor() {
        isOpenning = true;
        pendingMessageLabel.setText("Tìm thấy đối thủ! Đang vào game...");
        pendingMessageLabel.setForeground(new Color(46, 204, 113)); // Xanh lá
        pendingMessageLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loadingButtonImage.setVisible(false); // Ẩn nút thoát
    }

    private void loadingButtonImageActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        if (isOpenning) return;
        try {
            Client.closeView(Client.View.WAITING_ROOM);
            Client.openView(Client.View.HOMEPAGE);
            Client.socketHandle.write("cancel-room,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
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