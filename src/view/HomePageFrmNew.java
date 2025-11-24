package view;

import controller.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Trang chủ (Home) - Phiên bản nâng cấp giao diện
 */
public class HomePageFrmNew extends javax.swing.JFrame {

    // --- CONSTRUCTOR (GIỮ NGUYÊN LOGIC) ---
    public HomePageFrmNew() {
        // Thay vì gọi initComponents() cũ, ta gọi hàm dựng giao diện mới
        initModernComponents();
        
        // Các thiết lập logic cũ
        this.setTitle("Caro Game - Trang Chủ");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        // Load dữ liệu User (Logic cũ)
        jLabel4.setText(Client.user.getNickname());
        numberOfWinValue.setText(Integer.toString(Client.user.getNumberOfWin()));
        numberOfGameValue.setText(Integer.toString(Client.user.getNumberOfGame()));
        
        // Xử lý ảnh Avatar
        try {
            jLabel8.setIcon(new ImageIcon("assets/avatar/" + Client.user.getAvatar() + ".jpg"));
        } catch (Exception e) {
            jLabel8.setIcon(new ImageIcon("assets/avatar/1.jpg")); // Fallback avatar
        }
        
        sendMessageButton.setIcon(new ImageIcon("assets/image/send2.png"));
        messageTextArea.setEditable(false);
        
        if (Client.user.getNumberOfGame() == 0) {
            winRatioValue.setText("-");
        } else {
            winRatioValue.setText(String.format("%.2f", (float) Client.user.getNumberOfWin() / Client.user.getNumberOfGame() * 100) + "%");
        }
        drawValue.setText("" + Client.user.getNumberOfDraw());
        markValue.setText("" + (Client.user.getNumberOfGame() + Client.user.getNumberOfWin() * 10));
        rankValue.setText("" + Client.user.getRank());
    }

    // --- HÀM DỰNG GIAO DIỆN MỚI (THAY THẾ INITCOMPONENTS) ---
    private void initModernComponents() {
        // 1. Khởi tạo các biến (Bắt buộc để Logic cũ hoạt động)
        initVariables();

        // 2. Setup Panel chính (Background)
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(mainPanel);

        // --- PHẦN 1: HEADER (THÔNG TIN USER) ---
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        headerPanel.setBackground(new Color(0, 0, 0, 150)); // Nền đen mờ

        // Avatar (Bên trái)
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        avatarPanel.setPreferredSize(new Dimension(90, 90));
        avatarPanel.setBorder(new LineBorder(Color.WHITE, 2));
        jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
        avatarPanel.add(jLabel8, BorderLayout.CENTER);
        headerPanel.add(avatarPanel, BorderLayout.WEST);

        // Stats (Thông tin chi tiết - Ở giữa)
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        statsPanel.setOpaque(false);
        
        // Helper thêm stats
        addStatItem(statsPanel, "Nickname", jLabel4);
        addStatItem(statsPanel, "Thứ hạng", rankValue);
        addStatItem(statsPanel, "Điểm số", markValue);
        addStatItem(statsPanel, "Tỉ lệ thắng", winRatioValue);
        addStatItem(statsPanel, "Số trận", numberOfGameValue);
        addStatItem(statsPanel, "Thắng", numberOfWinValue);
        addStatItem(statsPanel, "Hòa", drawValue);
        
        // Panel chứa Stats có nền mờ
        JPanel glassStats = new JPanel(new BorderLayout());
        glassStats.setBackground(new Color(0, 0, 0, 120));
        glassStats.setBorder(new EmptyBorder(5, 10, 5, 10));
        glassStats.add(statsPanel);
        
        headerPanel.add(glassStats, BorderLayout.CENTER);
        
        // Tiêu đề Game (Bên trên cùng)
        frameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        frameLabel.setForeground(new Color(255, 193, 7)); // Màu vàng
        frameLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(frameLabel, BorderLayout.NORTH);
        topContainer.add(headerPanel, BorderLayout.CENTER);
        
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // --- PHẦN 2: CENTER (CHAT BOX) ---
        JPanel chatPanel = new JPanel(new BorderLayout(5, 5));
        chatPanel.setOpaque(false);
        chatPanel.setBorder(createTitledBorder("Kênh Chat Thế Giới"));

        messageTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageTextArea.setBackground(new Color(255, 255, 255, 200)); // Trắng mờ
        jScrollPane1 = new JScrollPane(messageTextArea);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setOpaque(false);
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setOpaque(false);
        messageInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageInput.setPreferredSize(new Dimension(0, 35));
        
        sendMessageButton.setText("Gửi");
        sendMessageButton.setBackground(new Color(46, 204, 113));
        sendMessageButton.setForeground(Color.WHITE);
        
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendMessageButton, BorderLayout.EAST);
        
        chatPanel.add(jScrollPane1, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);
        
        mainPanel.add(chatPanel, BorderLayout.CENTER);

        // --- PHẦN 3: BOTTOM (BUTTONS GRID) ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 10, 10)); // 2 dòng 4 cột
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 15, 10));

        // Styling buttons
        styleButton(quickGameButton, new Color(52, 152, 219)); // Xanh dương
        styleButton(goRoomButton, new Color(155, 89, 182)); // Tím
        styleButton(createRoomButton, new Color(46, 204, 113)); // Xanh lá
        styleButton(findRoomButton, new Color(230, 126, 34)); // Cam
        styleButton(playWithBotButton, new Color(22, 160, 133)); // Xanh biển đậm
        styleButton(friendListButton, new Color(241, 196, 15)); // Vàng
        styleButton(scoreBoardButton, new Color(52, 73, 94)); // Xanh đen
        styleButton(scoreBotButton, new Color(127, 140, 141)); // Xám (Đăng xuất)
        styleButton(exitGameButton, new Color(192, 57, 43)); // Đỏ

        // Add buttons theo thứ tự hợp lý
        buttonPanel.add(quickGameButton);
        buttonPanel.add(goRoomButton);
        buttonPanel.add(createRoomButton);
        buttonPanel.add(findRoomButton);
        
        buttonPanel.add(playWithBotButton);
        buttonPanel.add(friendListButton);
        buttonPanel.add(scoreBoardButton);
        buttonPanel.add(scoreBotButton); // Nút đăng xuất
        // Nút Thoát game để riêng hoặc thêm vào Panel khác nếu muốn, ở đây tôi add vào cuối
        
        // Panel chứa nút và nút thoát
        JPanel bottomContainer = new JPanel(new BorderLayout(0, 10));
        bottomContainer.setOpaque(false);
        bottomContainer.add(buttonPanel, BorderLayout.CENTER);
        
        // Nút thoát to ở dưới cùng
        exitGameButton.setPreferredSize(new Dimension(0, 35));
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.setOpaque(false);
        exitPanel.add(exitGameButton);
        bottomContainer.add(exitPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomContainer, BorderLayout.SOUTH);

        // Setup Events (Kết nối lại các sự kiện cũ)
        setupEvents();
        
        // Set size cửa sổ
        this.setSize(750, 650);
    }

    // Khởi tạo biến (Để tránh NullPointerException với logic cũ)
    private void initVariables() {
        frameLabel = new JLabel("Game Caro");
        jLabel1 = new JLabel(); // Nickname label title
        numberOfWinLabel = new JLabel();
        jLabel4 = new JLabel(); // Nickname value
        numberOfWinValue = new JLabel();
        jLabel8 = new JLabel(); // Avatar
        numberOfGameValue = new JLabel();
        numberOfGameLabel = new JLabel();
        markLabel = new JLabel();
        markValue = new JLabel();
        rankLabel = new JLabel();
        rankValue = new JLabel();
        winRatioLabel = new JLabel();
        winRatioValue = new JLabel();
        drawLabel = new JLabel();
        drawValue = new JLabel();
        
        messageTextArea = new JTextArea();
        messageInput = new JTextField();
        
        createRoomButton = new JButton("Tạo Phòng");
        scoreBoardButton = new JButton("BXH");
        findRoomButton = new JButton("Tìm phòng");
        scoreBotButton = new JButton("Đăng xuất");
        exitGameButton = new JButton("Thoát");
        quickGameButton = new JButton("Chơi nhanh");
        playWithBotButton = new JButton("Chơi với Bot");
        friendListButton = new JButton("Bạn bè");
        goRoomButton = new JButton("Vào phòng");
        sendMessageButton = new JButton();
    }

    private void setupEvents() {
        createRoomButton.addActionListener(this::createRoomButtonActionPerformed);
        scoreBoardButton.addActionListener(this::scoreBoardButtonActionPerformed);
        findRoomButton.addActionListener(this::findRoomButtonActionPerformed);
        scoreBotButton.addActionListener(this::scoreBotButtonActionPerformed);
        exitGameButton.addActionListener(this::exitGameButtonActionPerformed);
        quickGameButton.addActionListener(this::quickGameButtonActionPerformed);
        playWithBotButton.addActionListener(this::playWithBotButtonActionPerformed);
        friendListButton.addActionListener(this::friendListButtonActionPerformed);
        goRoomButton.addActionListener(this::goRoomButtonActionPerformed);
        sendMessageButton.addActionListener(this::sendMessageButtonActionPerformed);
        
        messageInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageInputKeyPressed(evt);
            }
        });
    }

    // --- CÁC HÀM HELPER STYLE ---
    
    private void addStatItem(JPanel panel, String title, JLabel valueLabel) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(200, 200, 200));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(Color.WHITE);
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        panel.add(p);
    }
    
    private void styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                new LineBorder(new Color(255, 255, 255, 100)), title);
        border.setTitleColor(Color.WHITE);
        border.setTitleFont(new Font("Segoe UI", Font.ITALIC, 12));
        return border;
    }

    // --- LOGIC GIỮ NGUYÊN 100% ---

    private void jLabel1AncestorMoved(javax.swing.event.AncestorEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void createRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        int res = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn đặt mật khẩu cho phòng không?", "Tạo phòng", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            Client.closeView(Client.View.HOMEPAGE);
            Client.openView(Client.View.CREATE_ROOM_PASSWORD);
        } else if (res == JOptionPane.NO_OPTION) {
            try {
                Client.socketHandle.write("create-room,");
                Client.closeView(Client.View.HOMEPAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
        }
    }                                                

    private void findRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try {
            Client.closeView(Client.View.HOMEPAGE);
            Client.openView(Client.View.ROOM_LIST);
            Client.socketHandle.write("view-room-list,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }                                              

    private void scoreBoardButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        Client.openView(Client.View.RANK);
    }                                                

    private void scoreBotButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try {
            Client.socketHandle.write("offline," + Client.user.getID());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
        Client.closeView(Client.View.HOMEPAGE);
        Client.openView(Client.View.LOGIN);
    }                                              

    private void exitGameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }                                              

    private void friendListButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        Client.closeView(Client.View.HOMEPAGE);
        Client.openView(Client.View.FRIEND_LIST);
    }                                                

    private void quickGameButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                
        Client.closeView(Client.View.HOMEPAGE);
        Client.openView(Client.View.FIND_ROOM);
    }                                               

    private void playWithBotButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        Client.openView(Client.View.GAME_AI);
    }                                                 

    private void goRoomButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        Client.openView(Client.View.ROOM_NAME_FRM);
    }                                            

    private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        sendMessage();
    }                                                 

    private void messageInputKeyPressed(java.awt.event.KeyEvent evt) {                                        
        if (evt.getKeyCode() == 10) {
            sendMessage();
        }
    }                                       

    private void sendMessage() {
        try {
            if (messageInput.getText().isEmpty()) {
                throw new Exception("Vui lòng nhập nội dung tin nhắn");
            }
            String temp = messageTextArea.getText();
            temp += "Tôi: " + messageInput.getText() + "\n";
            messageTextArea.setText(temp);
            Client.socketHandle.write("chat-server," + messageInput.getText());
            messageInput.setText("");
            messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    public void addMessage(String message) {
        String temp = messageTextArea.getText();
        temp += message + "\n";
        messageTextArea.setText(temp);
        messageTextArea.setCaretPosition(messageTextArea.getDocument().getLength());
    }

    // --- VARIABLES DECLARATION (GIỮ NGUYÊN TÊN) ---
    private javax.swing.JButton createRoomButton;
    private javax.swing.JLabel drawLabel;
    private javax.swing.JLabel drawValue;
    private javax.swing.JButton exitGameButton;
    private javax.swing.JButton findRoomButton;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JButton friendListButton;
    private javax.swing.JButton goRoomButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    // private javax.swing.JLayeredPane jLayeredPane1; // Không dùng nhưng có thể để lại nếu code cũ refer
    // private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel markLabel;
    private javax.swing.JLabel markValue;
    private javax.swing.JTextField messageInput;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JLabel numberOfGameLabel;
    private javax.swing.JLabel numberOfGameValue;
    private javax.swing.JLabel numberOfWinLabel;
    private javax.swing.JLabel numberOfWinValue;
    private javax.swing.JButton playWithBotButton;
    private javax.swing.JButton quickGameButton;
    private javax.swing.JLabel rankLabel;
    private javax.swing.JLabel rankValue;
    private javax.swing.JButton scoreBoardButton;
    private javax.swing.JButton scoreBotButton;
    private javax.swing.JButton sendMessageButton;
    private javax.swing.JLabel winRatioLabel;
    private javax.swing.JLabel winRatioValue;
    
    // Class hình nền (Sử dụng lại để đồng bộ)
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