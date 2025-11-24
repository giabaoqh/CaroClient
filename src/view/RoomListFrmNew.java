package view;

import controller.Client;

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
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 * Giao diện Danh sách phòng - Thiết kế lại hiện đại
 */
public class RoomListFrmNew extends javax.swing.JFrame {
    private Vector<String> listRoom;
    private Vector<String> listPassword;
    private boolean isPlayThread;
    private boolean isFiltered;
    private DefaultTableModel defaultTableModel;
    
    // Biến giao diện
    private JTable roomTextArea;
    private JButton backButton;

    public RoomListFrmNew() {
        // Cài đặt cơ bản
        setTitle("Sảnh Chờ - Danh Sách Phòng");
        setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        setSize(450, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        isPlayThread = true;
        isFiltered = false;

        // Khởi tạo giao diện mới
        initModernComponents();
        
        // Khởi chạy Thread update phòng (Logic cũ)
        startUpdateThread();
    }

    private void initModernComponents() {
        // 1. Nền
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // 2. Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel lblTitle = new JLabel("DANH SÁCH PHÒNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(46, 204, 113)); // Màu xanh lá
        lblTitle.setIcon(new ImageIcon("assets/icon/home-run.png")); // Icon nếu có
        headerPanel.add(lblTitle);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 3. Table
        String[] columns = {"Tên phòng", "Trạng thái"};
        defaultTableModel = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if(column == 1) return ImageIcon.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTextArea = new JTable(defaultTableModel);
        styleTable(roomTextArea);

        // Sự kiện click
        roomTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                roomTextAreaMouseClicked(evt);
            }
        });

        // ScrollPane trong suốt
        JScrollPane scrollPane = new JScrollPane(roomTextArea);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
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
        backButton.setBackground(new Color(231, 76, 60)); // Đỏ
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backButton.addActionListener(e -> {
            Client.closeView(Client.View.ROOM_LIST);
            Client.openView(Client.View.HOMEPAGE);
        });

        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setRowHeight(55);
        table.setShowVerticalLines(false);
        table.setBackground(new Color(0, 0, 0, 150));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(46, 204, 113));
        table.setSelectionForeground(Color.WHITE);
        
        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0, 200));
        header.setForeground(new Color(255, 193, 7));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Căn giữa text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(0, 0, 0, 0));
        centerRenderer.setForeground(Color.WHITE);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setMaxWidth(100); // Cột icon nhỏ
    }

    // --- LOGIC CŨ (GIỮ NGUYÊN) ---

    private void startUpdateThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (Client.roomListFrm.isDisplayable() && isPlayThread && !isFiltered) {
                    try {
                        Client.socketHandle.write("view-room-list,");
                        Thread.sleep(500);
                    } catch (IOException | InterruptedException ex) {
                        // ex.printStackTrace(); // Tắt log spam lỗi nếu đóng cửa sổ
                    }
                }
            }
        };
        thread.start();
    }

    public void updateRoomList(Vector<String> listData, Vector<String> listPassword) {
        this.listRoom = listData;
        this.listPassword = listPassword;
        defaultTableModel.setRowCount(0);
        ImageIcon imageIcon;
        for (int i = 0; i < listRoom.size(); i++) {
            if (listPassword.get(i).equals(" "))
                imageIcon = new ImageIcon("assets/icon/swords-1-mini.png");
            else
                imageIcon = new ImageIcon("assets/icon/swords-1-lock-mini.png"); // Icon khóa
            
            defaultTableModel.addRow(new Object[]{
                    listRoom.get(i),
                    imageIcon
            });
        }
    }

    private void roomTextAreaMouseClicked(java.awt.event.MouseEvent evt) {                                          
        if (roomTextArea.getSelectedRow() == -1) {
            return;
        }
        try {
            isPlayThread = false;
            int index = roomTextArea.getSelectedRow();
            int room = Integer.parseInt(listRoom.get(index).split(" ")[1]);
            String password = listPassword.get(index);
            if (password.equals(" ")) {
                Client.socketHandle.write("join-room," + room);
                Client.closeView(Client.View.ROOM_LIST);
            } else {
                Client.closeView(Client.View.ROOM_LIST);
                Client.openView(Client.View.JOIN_ROOM_PASSWORD, room, password);
            }
        } catch (IOException ex) {
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
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}