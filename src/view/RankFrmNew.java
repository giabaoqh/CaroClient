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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Giao diện Bảng Xếp Hạng - Thiết kế lại hiện đại
 */
public class RankFrmNew extends javax.swing.JFrame {
    private DefaultTableModel tableModel;
    private List<User> listUserStatics;
    private List<String> rankSrc;
    
    // Biến giao diện (Giữ tên biến cũ là rankTextArea dù nó là JTable để tránh nhầm lẫn)
    private JTable rankTextArea; 

    public RankFrmNew() {
        // Cài đặt cơ bản
        this.setTitle("Bảng Xếp Hạng Cao Thủ");
        this.setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(450, 600);
        this.setLocationRelativeTo(null);
        
        // Khởi tạo dữ liệu rank
        initRankData();
        
        // Khởi tạo giao diện mới
        initModernComponents();

        // Gửi yêu cầu lấy dữ liệu
        try {
            Client.socketHandle.write("get-rank-charts,");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void initRankData() {
        rankSrc = new ArrayList<>();
        rankSrc.add("rank-gold");
        rankSrc.add("rank-sliver");
        rankSrc.add("bronze-rank");
        for (int i = 0; i < 10; i++) { // Tăng giới hạn lên đề phòng lỗi
            rankSrc.add("nomal-rank");
        }
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
        
        JLabel lblTitle = new JLabel("BẢNG XẾP HẠNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(255, 215, 0)); // Màu vàng kim loại
        lblTitle.setIcon(new ImageIcon("assets/icon/rank-gold.png")); // Thêm icon cúp nếu có
        headerPanel.add(lblTitle);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 3. Bảng xếp hạng
        // Định nghĩa cột chuẩn xác hơn
        String[] columns = {"Top", "Người chơi", "Danh hiệu"};
        
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return ImageIcon.class; // Cột 3 là ảnh
                return Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho sửa
            }
        };

        rankTextArea = new JTable(tableModel);
        styleTable(rankTextArea); // Làm đẹp bảng

        // Sự kiện click (Logic cũ)
        rankTextArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                rankTextAreaMouseClicked(evt);
            }
        });

        // ScrollPane trong suốt
        JScrollPane scrollPane = new JScrollPane(rankTextArea);
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 25, 20, 25),
                new LineBorder(new Color(255, 255, 255, 50), 1)
        ));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer (Hướng dẫn nhỏ)
        JLabel footerLbl = new JLabel("Click vào người chơi để xem thông tin chi tiết");
        footerLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerLbl.setForeground(new Color(200, 200, 200));
        footerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        footerLbl.setBorder(new EmptyBorder(5, 0, 10, 0));
        mainPanel.add(footerLbl, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setRowHeight(60); // Cao hơn để chứa icon rank
        table.setShowVerticalLines(false);
        table.setBackground(new Color(0, 0, 0, 150)); // Đen trong suốt
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(255, 215, 0, 100)); // Màu vàng nhạt khi chọn
        table.setSelectionForeground(Color.WHITE);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 0, 0, 200));
        header.setForeground(new Color(255, 193, 7)); // Vàng
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Căn giữa nội dung
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(0, 0, 0, 0)); // Trong suốt
        centerRenderer.setForeground(Color.WHITE);
        
        // Riêng cột TOP (Cột 0) cho màu nổi
        DefaultTableCellRenderer rankRenderer = new DefaultTableCellRenderer();
        rankRenderer.setHorizontalAlignment(JLabel.CENTER);
        rankRenderer.setBackground(new Color(0, 0, 0, 0));
        rankRenderer.setForeground(new Color(46, 204, 113)); // Xanh lá cho số thứ tự
        rankRenderer.setFont(new Font("Segoe UI", Font.BOLD, 18));

        table.getColumnModel().getColumn(0).setCellRenderer(rankRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        table.getColumnModel().getColumn(0).setMaxWidth(60); // Cột Top nhỏ thôi
    }

    // --- LOGIC CŨ (GIỮ NGUYÊN) ---

    public void setDataToTable(List<User> users) {
        this.listUserStatics = users;
        tableModel.setRowCount(0);
        int i = 0;
        for (User user : listUserStatics) {
            // Lấy icon tương ứng rank
            String iconName = (i < rankSrc.size()) ? rankSrc.get(i) : "nomal-rank";
            
            tableModel.addRow(new Object[]{
                    i + 1, // Cột Top
                    user.getNickname(), // Cột Tên
                    new ImageIcon("assets/icon/" + iconName + ".png") // Cột Icon
            });
            i++;
        }
    }

    private void rankTextAreaMouseClicked(java.awt.event.MouseEvent evt) {                                          
        if (rankTextArea.getSelectedRow() == -1)
            return;
        if (listUserStatics.get(rankTextArea.getSelectedRow()).getID() == Client.user.getID()) {
            JOptionPane.showMessageDialog(rootPane, "Thứ hạng của bạn là " + (rankTextArea.getSelectedRow() + 1));
            return;
        }
        Client.openView(Client.View.COMPETITOR_INFO, listUserStatics.get(rankTextArea.getSelectedRow()));
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
                // Gradient nền tối mặc định
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 
                                                     0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}