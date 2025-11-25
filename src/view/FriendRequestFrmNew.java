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
 * Giao diện Lời mời kết bạn (Popup)
 */
public class FriendRequestFrmNew extends javax.swing.JFrame {
    private final int id;
    private final Timer timer;
    
    // Các biến giao diện
    private JLabel autoCloseLabel;
    private JButton acceptButton;
    private JButton declineButton;

    public FriendRequestFrmNew(int id, String nickname) {
        this.id = id;
        
        // Cài đặt cơ bản
        setTitle("Lời mời kết bạn");
        setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        setSize(400, 300); // Kích thước nhỏ gọn
        setResizable(false);
        setUndecorated(true); // Bỏ thanh tiêu đề Windows để làm giao diện Custom đẹp hơn
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Khởi tạo giao diện
        initModernComponents(nickname);

        // Logic Timer đếm ngược (Giữ nguyên logic cũ)
        timer = new Timer(1000, new ActionListener() {
            int count = 10;

            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count >= 0) {
                    autoCloseLabel.setText("Tự động từ chối sau: " + count + "s");
                } else {
                    ((Timer) (e.getSource())).stop();
                    disposeFrame();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    private void initModernComponents(String nickname) {
        // 1. Nền chính (Có viền sáng để nổi bật)
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new LineBorder(new Color(255, 255, 255, 100), 2));
        setContentPane(mainPanel);

        // 2. Nội dung chính (Giữa)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(0, 0, 0, 180)); // Đen trong suốt
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Icon minh họa
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon("assets/icon/friendship.png")); // Đảm bảo bạn có icon này hoặc icon tương tự
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tiêu đề
        JLabel titleLabel = new JLabel("YÊU CẦU KẾT BẠN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(255, 193, 7)); // Màu vàng
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nội dung tin nhắn
        JLabel msgLabel = new JLabel("<html><center><font color='white'>Người chơi <b>" + nickname + "</b> (ID: " + id + ")<br>muốn kết bạn với bạn.</font></center></html>");
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(msgLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // 3. Khu vực nút bấm
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setMaximumSize(new Dimension(300, 40));

        acceptButton = new JButton("Đồng ý");
        styleButton(acceptButton, new Color(46, 204, 113)); // Xanh lá
        acceptButton.addActionListener(evt -> acceptButtonActionPerformed(evt));

        declineButton = new JButton("Từ chối");
        styleButton(declineButton, new Color(231, 76, 60)); // Đỏ
        declineButton.addActionListener(evt -> declineButtonActionPerformed(evt));

        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);
        
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 4. Footer (Timer)
        autoCloseLabel = new JLabel("Tự động từ chối sau: 10s");
        autoCloseLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        autoCloseLabel.setForeground(new Color(180, 180, 180));
        autoCloseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(autoCloseLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    // Hàm style nút bấm
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    public void disposeFrame() {
        this.dispose();
    }

    // --- LOGIC XỬ LÝ SỰ KIỆN ---

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        try {
            timer.stop();
            Client.socketHandle.write("make-friend-confirm," + id);
            this.dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra: " + ex.getMessage());
        }
    }                                            

    private void declineButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        timer.stop();
        this.dispose();
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