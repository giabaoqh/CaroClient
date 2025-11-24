package view;

import controller.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Giao diện Tìm phòng nhanh (Redesigned)
 */
public class FindRoomFrmNew extends javax.swing.JFrame {
    private Timer timer;
    private boolean found;

    // Các thành phần giao diện mới
    private JLabel lblTimer;
    private JProgressBar progressBar;
    private JLabel lblStatus;
    private JLabel lblLoadingIcon;
    private JButton btnCancel;

    public FindRoomFrmNew() {
        // Cài đặt JFrame
        this.setTitle("Đang tìm đối thủ...");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 320);
        this.setLocationRelativeTo(null);

        // Khởi tạo giao diện
        initModernComponents();

        // Logic cũ
        found = false;
        startFind();
        sendFindRequest();
    }

    private void initModernComponents() {
        // 1. Nền chính
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);

        // 2. Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(0, 0, 0, 180)); // Đen trong suốt
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 80), 1),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        // --- Header ---
        JLabel lblTitle = new JLabel("ĐANG TÌM ĐỐI THỦ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(255, 204, 0)); // Màu vàng
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblTitle);
        cardPanel.add(Box.createVerticalStrut(20));

        // --- Đồng hồ đếm ngược ---
        lblTimer = new JLabel("00:20");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 42)); // Font to như đồng hồ điện tử
        lblTimer.setForeground(Color.WHITE);
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblTimer);
        cardPanel.add(Box.createVerticalStrut(20));

        // --- Progress Bar ---
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 10));
        progressBar.setForeground(new Color(46, 204, 113)); // Màu xanh lá
        progressBar.setBackground(new Color(50, 50, 50));
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(progressBar);
        cardPanel.add(Box.createVerticalStrut(15));

        // --- Icon Loading & Status Text ---
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.setOpaque(false);
        
        // Load gif loading nếu có
        ImageIcon loadingIcon = new ImageIcon("assets/icon/loading1.gif");
        // Kiểm tra xem ảnh có load được không, nếu không thì dùng ảnh tĩnh hoặc null
        if (loadingIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
             lblLoadingIcon = new JLabel(""); // Không hiện nếu lỗi ảnh
        } else {
             lblLoadingIcon = new JLabel(loadingIcon);
        }
        
        lblStatus = new JLabel("Hệ thống đang ghép cặp...");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblStatus.setForeground(new Color(200, 200, 200));

        statusPanel.add(lblLoadingIcon);
        statusPanel.add(lblStatus);
        statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(statusPanel);
        
        cardPanel.add(Box.createVerticalStrut(20));

        // --- Nút Hủy ---
        btnCancel = createStyledButton("Hủy tìm kiếm", new Color(192, 57, 43));
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCancel.addActionListener(this::loadingButtonActionPerformed);
        cardPanel.add(btnCancel);

        mainPanel.add(cardPanel);
    }

    // --- LOGIC XỬ LÝ (Giữ nguyên logic quan trọng) ---

    public void stopAllThread() {
        if (timer != null) timer.stop();
    }

    public void startFind() {
        foundLabelState(false);
        
        timer = new Timer(1000, new ActionListener() {
            int count = 20;

            @Override
            public void actionPerformed(ActionEvent e) {
                count--;
                if (count >= 0) {
                    String timeStr = (count >= 10) ? "00:" + count : "00:0" + count;
                    lblTimer.setText(timeStr);
                    // Cập nhật progress bar
                    progressBar.setValue(Math.round((float) count / 20 * 100));
                } else {
                    ((Timer) (e.getSource())).stop();
                    try {
                        Client.socketHandle.write("cancel-room,");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                    }
                    
                    int res = JOptionPane.showConfirmDialog(rootPane, 
                            "Không tìm thấy đối thủ, bạn muốn thử lại không?", 
                            "Hết thời gian", JOptionPane.YES_NO_OPTION);
                    
                    if (res == JOptionPane.YES_OPTION) {
                        startFind();
                        sendFindRequest();
                    } else {
                        Client.closeView(Client.View.FIND_ROOM);
                        Client.openView(Client.View.HOMEPAGE);
                    }
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public void sendFindRequest() {
        try {
            Client.socketHandle.write("quick-room,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    public void showFoundRoom() {
        found = true;
        timer.stop();
        foundLabelState(true);
        btnCancel.setEnabled(false); // Không cho hủy khi đã tìm thấy
        btnCancel.setText("Đang vào phòng...");
        btnCancel.setBackground(new Color(100, 100, 100));
    }

    // Hàm cập nhật trạng thái text/icon
    private void foundLabelState(boolean isFound) {
        if (isFound) {
            lblStatus.setText("Đã tìm thấy đối thủ! Đang kết nối...");
            lblStatus.setForeground(new Color(46, 204, 113)); // Xanh lá
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblLoadingIcon.setVisible(true); 
        } else {
            lblStatus.setText("Đang tìm kiếm người chơi khác...");
            lblStatus.setForeground(Color.WHITE);
            lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            lblLoadingIcon.setVisible(true);
        }
    }

    private void loadingButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (found) return;
        try {
            Client.socketHandle.write("cancel-room,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
        timer.stop();
        Client.closeView(Client.View.FIND_ROOM);
        Client.openView(Client.View.HOMEPAGE);
    } 

    // --- Helper UI Methods ---

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 40)); // Kích thước tối đa để không bị giãn
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                 if (btn.isEnabled()) btn.setBackground(bg);
            }
        });
        return btn;
    }

    // Class hình nền (Dùng chung)
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
            } catch (IOException e) { }
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