package view;

import controller.Client;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Giao diện Danh sách bạn bè được thiết kế lại
 */
public class FriendListFrmNew extends javax.swing.JFrame {
    private List<User> listFriend;
    private boolean isClicked;
    private DefaultTableModel defaultTableModel;
    
    // Các biến giao diện
    private JTable friendTable;
    private JButton backButton;

    public FriendListFrmNew() {
        // Cài đặt cơ bản
        setTitle("Danh Sách Bạn Bè - Caro Game");
        setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        setSize(500, 600); // Kích thước dài hơn để hiện nhiều bạn
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        isClicked = false;

        // Khởi tạo giao diện hiện đại
        initModernComponents();

        // Logic cũ giữ nguyên
        requestUpdate();
        startThread();
    }

    // --- PHẦN GIAO DIỆN MỚI ---
    private void initModernComponents() {
        // 1. Nền chính
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // 2. Header (Tiêu đề)
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel lblTitle = new JLabel("DANH SÁCH BẠN BÈ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(255, 255, 255));
        lblTitle.setIcon(new ImageIcon("assets/icon/friendship.png")); // Thêm icon nếu có
        headerPanel.add(lblTitle);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 3. Table (Danh sách)
        // Cấu hình Model cho bảng
        String[] columns = {"ID", "Nickname", "Trạng thái"};
        defaultTableModel = new DefaultTableModel(new Object[][]{}, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa trực tiếp
            }
            
            // Quan trọng: Để hiển thị Icon
            @Override
            public Class<?> getColumnClass(int column) {
                if(column == 2) return ImageIcon.class;
                return String.class;
            }
        };

        friendTable = new JTable(defaultTableModel);
        styleTable(friendTable); // Áp dụng style đẹp

        // Sự kiện click chuột vào bảng
        friendTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                friendTableMouseClicked(evt);
            }
        });

        // ScrollPane chứa bảng (Trong suốt)
        JScrollPane scrollPane = new JScrollPane(friendTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30)); // Màu nền khi bảng rỗng
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 20, 0, 20),
                new LineBorder(new Color(255, 255, 255, 50), 1)
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 4. Footer (Nút Quay lại)
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(15, 0, 20, 0));

        backButton = new JButton("Quay lại trang chủ");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(231, 76, 60)); // Màu đỏ cam
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeButtonActionPerformed(e);
            }
        });

        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    // Hàm trang trí bảng cho đẹp
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(50); // Hàng cao dễ bấm
        table.setShowVerticalLines(false);
        table.setBackground(new Color(0, 0, 0, 150)); // Nền bảng đen trong suốt
        table.setForeground(Color.WHITE); // Chữ trắng
        table.setSelectionBackground(new Color(46, 204, 113)); // Màu xanh khi chọn
        table.setSelectionForeground(Color.WHITE);
        
        // Căn chỉnh cột
        table.getColumnModel().getColumn(0).setMaxWidth(60); // Cột ID nhỏ lại
        table.getColumnModel().getColumn(2).setMaxWidth(100); // Cột Icon nhỏ lại

        // Trang trí Header bảng
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0, 200));
        header.setForeground(new Color(255, 193, 7)); // Màu vàng
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Căn giữa nội dung trong ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(0,0,0,0)); // Trong suốt để ăn theo nền bảng
        centerRenderer.setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        // Cột 2 là Icon thì không cần renderer text
    }

    // --- LOGIC GIỮ NGUYÊN TỪ CODE CŨ ---

    public void stopAllThread() {
        isClicked = true;
    }

    public void startThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (Client.friendListFrm.isDisplayable() && !isClicked) {
                    try {
                        System.out.println("Xem danh sách bạn bè đang chạy!");
                        requestUpdate();
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    public void requestUpdate() {
        try {
            Client.socketHandle.write("view-friend-list,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    public void updateFriendList(List<User> friends) {
        listFriend = friends;
        defaultTableModel.setRowCount(0);
        ImageIcon icon;
        for (User friend : listFriend) {
            if (!friend.isOnline()) {
                icon = new ImageIcon("assets/icon/offline.png");
            } else if (friend.isPlaying()) {
                icon = new ImageIcon("assets/icon/swords-mini.png"); // Đang chơi
            } else {
                icon = new ImageIcon("assets/icon/swords-1-mini.png"); // Sẵn sàng
            }
            defaultTableModel.addRow(new Object[]{
                    "" + friend.getID(),
                    friend.getNickname(),
                    icon
            });
        }
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Client.closeView(Client.View.FRIEND_LIST);
        Client.openView(Client.View.HOMEPAGE);
    }                                           

    private void friendTableMouseClicked(java.awt.event.MouseEvent evt) {                                         
        try {
            if (friendTable.getSelectedRow() == -1) return;
            User friend = listFriend.get(friendTable.getSelectedRow());
            if (!friend.isOnline()) {
                throw new Exception("Người chơi không online");
            }
            if (friend.isPlaying()) {
                throw new Exception("Người chơi đang trong trận đấu");
            }
            isClicked = true;
            int res = JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn thách đấu người bạn này không?", "Xác nhận thách đấu", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                Client.closeAllViews();
                Client.openView(Client.View.GAME_NOTICE, "Thách đấu", "Đang chờ phản hồi từ đối thủ");
                Client.socketHandle.write("duel-request," + friend.getID());
            } else {
                isClicked = false;
                startThread();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }                                        

    // Class hình nền (Đã tối ưu)
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
                // Gradient nền tối mặc định nếu không có ảnh
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}