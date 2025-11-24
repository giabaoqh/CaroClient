package view;

import controller.Client;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Giao diện Thông tin đối thủ được thiết kế lại
 */
public class CompetitorInfoFrmNew extends javax.swing.JFrame {
    private boolean isFriend;
    private User user;

    // Các biến giao diện
    private JLabel avatarLabel;
    private JLabel nicknameLabel;
    private JLabel isFriendLabel;
    private JButton actionButton; // Nút kết bạn
    
    // Các label chỉ số
    private JLabel lbGameCount, lbWinCount, lbDrawCount;
    private JLabel lbRatio, lbScore, lbRank;

    public CompetitorInfoFrmNew(User user) {
        this.user = user;
        // Cài đặt cơ bản cho cửa sổ
        setTitle("Thông tin người chơi - Caro Game");
        setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 450); // Kích thước form gọn gàng hơn
        setLocationRelativeTo(null);

        // Khởi tạo giao diện hiện đại
        initModernComponents();

        // Đổ dữ liệu vào giao diện
        loadData();

        // Gửi yêu cầu kiểm tra bạn bè
        try {
            Client.socketHandle.write("check-friend," + user.getID());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + ex.getMessage());
        }
    }

    private void initModernComponents() {
        // 1. Nền chính (Background)
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg"); // Đường dẫn ảnh nền nếu có
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Panel chứa thông tin (Card ở giữa)
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(new Color(0, 0, 0, 180)); // Màu đen trong suốt
        cardPanel.setBorder(new LineBorder(new Color(255, 255, 255, 100), 1)); // Viền mỏng
        cardPanel.setPreferredSize(new Dimension(480, 380));

        // --- HEADER: Tiêu đề ---
        JLabel lblHeader = new JLabel("HỒ SƠ ĐỐI THỦ", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(new Color(255, 193, 7)); // Màu vàng gold
        lblHeader.setBorder(new EmptyBorder(15, 0, 10, 0));
        cardPanel.add(lblHeader, BorderLayout.NORTH);

        // --- BODY: Avatar + Thông tin ---
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        bodyPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // Cột Trái: Avatar
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(110, 110));
        avatarLabel.setBorder(new LineBorder(Color.WHITE, 2));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
        bodyPanel.add(avatarLabel, gbc);

        // Cột Phải: Tên + Nút
        JPanel namePanel = new JPanel(new GridLayout(2, 1));
        namePanel.setOpaque(false);
        
        nicknameLabel = new JLabel("Unknown");
        nicknameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nicknameLabel.setForeground(Color.WHITE);
        
        isFriendLabel = new JLabel("Đang tải trạng thái...");
        isFriendLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        isFriendLabel.setForeground(new Color(200, 200, 200));

        namePanel.add(nicknameLabel);
        namePanel.add(isFriendLabel);

        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        bodyPanel.add(namePanel, gbc);

        // Nút hành động (Kết bạn)
        actionButton = new JButton("Kết bạn");
        styleButton(actionButton);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        bodyPanel.add(actionButton, gbc);

        // --- STATS: Bảng chỉ số ---
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10)); // 2 dòng, 3 cột
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

        // Tạo các ô chỉ số
        lbGameCount = createStatItem(statsPanel, "Số ván chơi");
        lbWinCount = createStatItem(statsPanel, "Số ván thắng");
        lbDrawCount = createStatItem(statsPanel, "Số ván hòa");
        lbRatio = createStatItem(statsPanel, "Tỉ lệ thắng");
        lbScore = createStatItem(statsPanel, "Điểm số");
        lbRank = createStatItem(statsPanel, "Thứ hạng");

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        bodyPanel.add(statsPanel, gbc);

        cardPanel.add(bodyPanel, BorderLayout.CENTER);
        mainPanel.add(cardPanel);

        // Sự kiện nút bấm
        actionButton.addActionListener(evt -> handleActionButtonClick());
    }

    // Hàm tạo ô chỉ số đẹp
    private JLabel createStatItem(JPanel parent, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(255, 255, 255, 50))); // Vạch ngăn cách
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(180, 180, 180));
        
        JLabel lblValue = new JLabel("0", SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValue.setForeground(Color.WHITE);
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        parent.add(p);
        
        return lblValue;
    }

    private void loadData() {
        // Load Avatar
        try {
            String path = "assets/avatar/" + user.getAvatar() + ".jpg";
            ImageIcon icon = new ImageIcon(path);
            // Resize ảnh cho vừa khung
            Image img = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            avatarLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            avatarLabel.setText("No Image");
            avatarLabel.setForeground(Color.WHITE);
        }

        nicknameLabel.setText(user.getNickname());
        lbGameCount.setText("" + user.getNumberOfGame());
        lbWinCount.setText("" + user.getNumberOfWin());
        lbDrawCount.setText("" + user.getNumberOfDraw());
        lbRank.setText("" + user.getRank());

        // Tính điểm và tỉ lệ
        int score = (user.getNumberOfWin() * 10 + user.getNumberOfGame());
        lbScore.setText("" + score);

        if (user.getNumberOfGame() == 0) {
            lbRatio.setText("-");
        } else {
            float ratio = (float) user.getNumberOfWin() / user.getNumberOfGame() * 100;
            lbRatio.setText(String.format("%.1f%%", ratio));
        }
    }

    // Hàm logic từ code cũ
    // Thay thế hàm checkFriend cũ bằng hàm này
    public void checkFriend(boolean isFriend) {
        this.isFriend = isFriend;
        if (isFriend) {
            // Đã là bạn: Icon nhỏ, màu xanh lá
            actionButton.setIcon(getResizedIcon("assets/icon/friendship.png", 24, 24));
            actionButton.setText("Bạn bè"); 
            actionButton.setToolTipText("Hai bạn đã là bạn bè");
            actionButton.setEnabled(false); 
            actionButton.setBackground(new Color(46, 204, 113)); // Xanh lá
            
            isFriendLabel.setText("Các bạn đang là bạn bè");
            isFriendLabel.setForeground(new Color(46, 204, 113));
        } else {
            // Chưa là bạn: Icon nhỏ, màu xanh dương
            actionButton.setIcon(getResizedIcon("assets/icon/add-friend.png", 24, 24));
            actionButton.setText("Kết bạn");
            actionButton.setToolTipText("Gửi lời mời kết bạn");
            actionButton.setEnabled(true);
            actionButton.setBackground(new Color(52, 152, 219)); // Xanh dương
            
            isFriendLabel.setText("Kết bạn để chơi cùng nhau!");
            isFriendLabel.setForeground(new Color(200, 200, 200));
        }
    }

    private void handleActionButtonClick() {
        if (isFriend) {
            JOptionPane.showMessageDialog(this, "Bạn và đối thủ đang là bạn bè");
        } else {
            int res = JOptionPane.showConfirmDialog(this, 
                "Bạn có muốn gửi lời mời kết bạn tới " + user.getNickname() + " không?", 
                "Xác nhận kết bạn", JOptionPane.YES_NO_OPTION);
            
            if (res == JOptionPane.YES_OPTION) {
                try {
                    Client.socketHandle.write("make-friend," + user.getID());
                    JOptionPane.showMessageDialog(this, "Đã gửi lời mời kết bạn!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        }
    }
    
    // Style cho nút bấm
    private void styleButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 152, 219));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Class hình nền (dùng lại của AdminNew cho đồng bộ)
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    backgroundImage = ImageIO.read(file);
                } else {
                    // Fallback nếu không có ảnh file
                     // System.out.println("Không thấy ảnh tại: " + imagePath);
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
                // Gradient nền tối mặc định
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
    // Hàm hỗ trợ thu nhỏ icon
    private ImageIcon getResizedIcon(String path, int width, int height) {
        try {
            File f = new File(path);
            if (!f.exists()) return null;
            BufferedImage img = ImageIO.read(f);
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }
}