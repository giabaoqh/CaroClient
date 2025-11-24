package view;

import controller.Client;
import model.Point;
import model.XOButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Giao diện Game AI (Chơi với máy) - Redesign
 */
public class GameAIFrmNew extends javax.swing.JFrame {

    // --- CÁC BIẾN LOGIC (GIỮ NGUYÊN) ---
    private static final int ROW_COUNT = 20;
    private static final int COL_COUNT = 20;
    public XOButton[][] Buttons = new XOButton[COL_COUNT][ROW_COUNT];
    private static final int winScore = 100000000;
    private int gameNumber = 0;
    private int userWin = 0;
    private int aIWin = 0;
    XOButton preButton;

    // --- CÁC BIẾN GIAO DIỆN MỚI ---
    private JPanel gameBoardPanel; // Panel chứa bàn cờ
    private JTextArea roomMessageTextArea; // Log chat/thông báo
    private JLabel scoreLabel; // Tỉ số
    
    // Player Components
    private JLabel playerAvatarLabel;
    private JLabel playerNicknameValue;
    private JLabel playerWinCountLabel;
    private JLabel playerTurnLabel; // "Bạn"
    private JLabel playerMarkLabel; // Icon X/O
    
    // AI Components
    private JLabel aiAvatarLabel;
    private JLabel aiNicknameValue;
    private JLabel aiWinCountLabel;
    private JLabel aiTurnLabel; // "Máy"
    private JLabel aiMarkLabel; // Icon X/O

    public GameAIFrmNew() {
        // Cài đặt cơ bản
        setTitle("Chơi với máy (AI Mode) - Caro Game Nhóm 5");
        setIconImage(new ImageIcon("assets/image/caroicon.png").getImage());
        this.setSize(1200, 750); // Mở rộng kích thước cho thoải mái
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Khởi tạo giao diện mới
        initModernComponents();

        // --- KHỞI TẠO LOGIC BÀN CỜ (GIỮ NGUYÊN) ---
        // Setup play button logic
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                Point point = new Point(i, j);
                Buttons[i][j] = new XOButton(i, j);
                // Giữ nguyên logic mouse listener cũ
                Buttons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        handleClickButton(point);
                    }
                    @Override public void mousePressed(MouseEvent e) {}
                    @Override public void mouseExited(MouseEvent e) {}
                    @Override public void mouseEntered(MouseEvent e) {}
                    @Override public void mouseClicked(MouseEvent e) {}
                });
                gameBoardPanel.add(Buttons[i][j]);
            }
        }
        
        // Load thông tin ban đầu
        gameNumber++;
        preButton = null;
        updateScore();
        
        // Load Avatar User
        try {
            String path = "assets/avatar/" + Client.user.getAvatar() + ".jpg";
            ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            playerAvatarLabel.setIcon(icon);
        } catch (Exception e) {
            // Fallback icon
        }
    }

    // --- PHẦN GIAO DIỆN (ĐƯỢC VIẾT LẠI HOÀN TOÀN) ---
    private void initModernComponents() {
        // 1. Setup Menu Bar
        setupMenuBar();

        // 2. Main Background
        BackgroundPanel mainPanel = new BackgroundPanel("assets/image/background.jpg");
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // --- LEFT PANEL: PLAYER INFO ---
        JPanel leftPanel = createSidePanel(true);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // --- RIGHT PANEL: AI INFO + LOG ---
        JPanel rightPanel = createSidePanel(false);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // --- CENTER PANEL: GAME BOARD ---
        JPanel centerWrapper = new JPanel(new GridBagLayout()); // Dùng wrapper để căn giữa bàn cờ
        centerWrapper.setOpaque(false);
        
        // Panel bàn cờ thực sự
        gameBoardPanel = new JPanel();
        gameBoardPanel.setLayout(new GridLayout(ROW_COUNT, COL_COUNT));
        gameBoardPanel.setPreferredSize(new Dimension(600, 600)); // Kích thước cố định cho bàn cờ vuông
        gameBoardPanel.setBorder(new LineBorder(Color.BLACK, 2));
        gameBoardPanel.setBackground(Color.WHITE);
        
        centerWrapper.add(gameBoardPanel);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        // --- HEADER: SCORE ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        scoreLabel = new JLabel("Tỉ số: 0 - 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        headerPanel.add(scoreLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuGame = new JMenu("Menu");
        JMenuItem itemNewGame = new JMenuItem("Game mới (F1)");
        itemNewGame.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemNewGame.addActionListener(evt -> newGameMenuItemActionPerformed(evt));
        
        JMenuItem itemExit = new JMenuItem("Thoát (Alt+F4)");
        itemExit.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        itemExit.addActionListener(evt -> exitMenuItemActionPerformed(evt));
        
        menuGame.add(itemNewGame);
        menuGame.addSeparator();
        menuGame.add(itemExit);
        
        JMenu menuHelp = new JMenu("Trợ giúp");
        JMenuItem itemHelp = new JMenuItem("Luật chơi (F2)");
        itemHelp.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itemHelp.addActionListener(evt -> helpMenuItemActionPerformed(evt));
        menuHelp.add(itemHelp);

        menuBar.add(menuGame);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    private JPanel createSidePanel(boolean isPlayer) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(280, 0));
        // Màu nền bán trong suốt: Player (Xanh), AI (Đỏ)
        if (isPlayer) {
            panel.setBackground(new Color(0, 100, 200, 100));
            panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(255,255,255,50)));
        } else {
            panel.setBackground(new Color(200, 50, 50, 100));
            panel.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(255,255,255,50)));
        }

        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Avatar
        JLabel lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(100, 100));
        lblAvatar.setMaximumSize(new Dimension(100, 100));
        lblAvatar.setBorder(new LineBorder(Color.WHITE, 2));
        lblAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (isPlayer) {
            playerAvatarLabel = lblAvatar;
        } else {
            // Load AI Avatar
            try {
                ImageIcon icon = new ImageIcon("assets/image/ai.png"); // Đảm bảo có ảnh này
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblAvatar.setText("AI Bot");
            }
            aiAvatarLabel = lblAvatar;
        }
        panel.add(lblAvatar);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Nickname
        JLabel lblName = new JLabel(isPlayer ? Client.user.getNickname() : "BOSS AI");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblName.setForeground(Color.WHITE);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (isPlayer) playerNicknameValue = lblName; else aiNicknameValue = lblName;
        panel.add(lblName);
        
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // 3. Win Count
        JLabel lblWin = new JLabel(isPlayer ? 
                "Số ván thắng: " + Client.user.getNumberOfWin() : "Level: Siêu Cấp");
        lblWin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblWin.setForeground(Color.LIGHT_GRAY);
        lblWin.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (isPlayer) playerWinCountLabel = lblWin; else aiWinCountLabel = lblWin;
        panel.add(lblWin);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // 4. Current Turn & Mark (X/O)
        JPanel statusPanel = new JPanel(new FlowLayout());
        statusPanel.setOpaque(false);
        
        JLabel lblTurn = new JLabel(isPlayer ? "Bạn" : "Máy");
        lblTurn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTurn.setForeground(Color.WHITE);
        if (isPlayer) playerTurnLabel = lblTurn; else aiTurnLabel = lblTurn;
        
        JLabel lblMark = new JLabel();
        // Load ảnh X/O nhỏ
        try {
            String imgName = isPlayer ? "x3.jpg" : "o3.jpg"; // Dùng ảnh resource cũ
            ImageIcon icon = new ImageIcon("assets/image/" + imgName);
            lblMark.setIcon(icon);
        } catch (Exception e) {
            lblMark.setText(isPlayer ? "[X]" : "[O]");
            lblMark.setForeground(Color.YELLOW);
        }
        if (isPlayer) playerMarkLabel = lblMark; else aiMarkLabel = lblMark;

        statusPanel.add(lblTurn);
        statusPanel.add(lblMark);
        panel.add(statusPanel);

        // 5. Nếu là cột bên phải (AI), thêm khung chat/log
        if (!isPlayer) {
            panel.add(Box.createRigidArea(new Dimension(0, 30)));
            JLabel lblLog = new JLabel("Nhật ký trận đấu:");
            lblLog.setForeground(Color.WHITE);
            lblLog.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblLog);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));

            roomMessageTextArea = new JTextArea();
            roomMessageTextArea.setEditable(false);
            roomMessageTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            roomMessageTextArea.setBackground(new Color(30, 30, 30));
            roomMessageTextArea.setForeground(Color.GREEN);
            
            JScrollPane scroll = new JScrollPane(roomMessageTextArea);
            scroll.setPreferredSize(new Dimension(240, 150));
            scroll.setMaximumSize(new Dimension(240, 200));
            panel.add(scroll);
        }

        return panel;
    }

    // --- CÁC HÀM LOGIC (GIỮ NGUYÊN TỪ CODE CŨ) ---

    private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                                
        newGame();
    }                                               

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                             
        this.dispose();
    }                                            

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {                                             
        JOptionPane.showMessageDialog(rootPane, "Luật chơi: luật quốc tế 5 nước chặn 2 đầu\n"
                + "Hai người chơi luân phiên nhau chơi trước\n"
                + "Người chơi trước đánh X, người chơi sau đánh O\n"
                + "Bạn có 20 giây cho mỗi lượt đánh, quá 20 giây bạn sẽ thua\n"
                + "Khi cầu hòa, nếu đối thủ đồng ý thì ván hiện tại được hủy kết quả\n"
                + "Với mỗi ván chơi bạn có thêm 1 điểm, nếu hòa bạn được thêm 5 điểm,\n"
                + "nếu thắng bạn được thêm 10 điểm\n"
                + "Chúc bạn chơi game vui vẻ");
    }       

    private void updateScore() {
        scoreLabel.setText("Tỉ số: " + userWin + " - " + aIWin);
    }

    private void handleClickButton(Point point) {
        // Logic xử lý khi click chuột
        point.log();
        Buttons[point.x][point.y].setState(true);
        Buttons[point.x][point.y].setEnabled(false);
        if (getScore(getMatrixBoard(), true, false) >= winScore) {
            JOptionPane.showMessageDialog(null, "Bạn đã thắng");
            userWin++;
            updateScore();
            displayUserWin();
            newGame();
            return;
        }

        int nextMoveX = 0, nextMoveY = 0;
        int[] bestMove = calcNextMove(3);

        if (bestMove != null) {
            nextMoveX = bestMove[0];
            nextMoveY = bestMove[1];
        }
        Buttons[nextMoveX][nextMoveY].setState(false);
        Buttons[nextMoveX][nextMoveY].setEnabled(false);
        if (getScore(getMatrixBoard(), false, true) >= winScore) {
            JOptionPane.showMessageDialog(null, "Bạn đã thua");
            aIWin++;
            updateScore();
            displayAIWin();
            newGame();
        }
    }

    private void newGame() {
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                Buttons[i][j].resetState();
            }
        }

        gameNumber++;
        if (gameNumber % 2 == 0) {
            JOptionPane.showMessageDialog(rootPane, "Máy đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
            Buttons[9][9].setState(false);
            Buttons[9][9].setEnabled(false);

            if (getScore(getMatrixBoard(), false, true) >= winScore) {
                JOptionPane.showMessageDialog(null, "Bạn đã thua");
                newGame();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Bạn đi trước", "Ván mới", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayUserWin() {
        String tmp = roomMessageTextArea.getText();
        tmp += "-- Bạn thắng! Tỉ số: " + userWin + "-" + aIWin + " --\n";
        roomMessageTextArea.setText(tmp);
        roomMessageTextArea.setCaretPosition(roomMessageTextArea.getDocument().getLength());
    }

    private void displayAIWin() {
        String tmp = roomMessageTextArea.getText();
        tmp += "-- Máy thắng! Tỉ số: " + userWin + "-" + aIWin + " --\n";
        roomMessageTextArea.setText(tmp);
        roomMessageTextArea.setCaretPosition(roomMessageTextArea.getDocument().getLength());
    }

    // --- PHẦN THUẬT TOÁN AI (GIỮ NGUYÊN 100%) ---
    public int[] calcNextMove(int depth) {
        int[][] board = getMatrixBoard();
        Object[] bestMove = searchWinningMove(board);
        Object[] badMove = searchLoseMove(board);

        int[] move = new int[2];

        if (badMove[1] != null && badMove[2] != null) {
            move[0] = (Integer) (badMove[1]);
            move[1] = (Integer) (badMove[2]);
            return move;
        }

        if (bestMove[1] != null && bestMove[2] != null) {
            move[0] = (Integer) (bestMove[1]);
            move[1] = (Integer) (bestMove[2]);
        } else {
            bestMove = minimaxSearchAB(depth, board, true, -1.0, winScore);
            if (bestMove[1] == null) {
                move = null;
            } else {
                move[0] = (Integer) (bestMove[1]);
                move[1] = (Integer) (bestMove[2]);
            }
        }
        return move;
    }

    public int[][] playNextMove(int[][] board, int[] move, boolean isUserTurn) {
        int i = move[0], j = move[1];
        int[][] newBoard = new int[ROW_COUNT][COL_COUNT];
        for (int h = 0; h < ROW_COUNT; h++) {
            for (int k = 0; k < COL_COUNT; k++) {
                newBoard[h][k] = board[h][k];
            }
        }
        newBoard[i][j] = isUserTurn ? 2 : 1;
        return newBoard;
    }

    private Object[] searchWinningMove(int[][] matrix) {
        java.util.ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        Object[] winningMove = new Object[3];
        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, false);
            if (getScore(dummyBoard, false, false) >= winScore) {
                winningMove[1] = move[0];
                winningMove[2] = move[1];
                return winningMove;
            }
        }
        return winningMove;
    }

    private Object[] searchLoseMove(int[][] matrix) {
        java.util.ArrayList<int[]> allPossibleMoves = generateMoves(matrix);
        Object[] losingMove = new Object[3];
        for (int[] move : allPossibleMoves) {
            int[][] dummyBoard = playNextMove(matrix, move, true);
            if (getScore(dummyBoard, true, false) >= winScore) {
                losingMove[1] = move[0];
                losingMove[2] = move[1];
                return losingMove;
            }
        }
        return losingMove;
    }

    public Object[] minimaxSearchAB(int depth, int[][] board, boolean max, double alpha, double beta) {
        if (depth == 0) return new Object[]{evaluateBoardForWhite(board, !max), null, null};
        java.util.ArrayList<int[]> allPossibleMoves = generateMoves(board);
        if (allPossibleMoves.isEmpty()) return new Object[]{evaluateBoardForWhite(board, !max), null, null};

        Object[] bestMove = new Object[3];
        if (max) {
            bestMove[0] = -1.0;
            for (int[] move : allPossibleMoves) {
                int[][] dummyBoard = playNextMove(board, move, false);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, false, alpha, beta);
                if ((Double) (tempMove[0]) > alpha) alpha = (Double) (tempMove[0]);
                if ((Double) (tempMove[0]) >= beta) return tempMove;
                if ((Double) tempMove[0] > (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        } else {
            bestMove[0] = 100000000.0;
            bestMove[1] = allPossibleMoves.get(0)[0];
            bestMove[2] = allPossibleMoves.get(0)[1];
            for (int[] move : allPossibleMoves) {
                int[][] dummyBoard = playNextMove(board, move, true);
                Object[] tempMove = minimaxSearchAB(depth - 1, dummyBoard, true, alpha, beta);
                if (((Double) tempMove[0]) < beta) beta = (Double) (tempMove[0]);
                if ((Double) (tempMove[0]) <= alpha) return tempMove;
                if ((Double) tempMove[0] < (Double) bestMove[0]) {
                    bestMove = tempMove;
                    bestMove[1] = move[0];
                    bestMove[2] = move[1];
                }
            }
        }
        return bestMove;
    }

    public double evaluateBoardForWhite(int[][] board, boolean userTurn) {
        double blackScore = getScore(board, true, userTurn);
        double whiteScore = getScore(board, false, userTurn);
        if (blackScore == 0) blackScore = 1.0;
        return whiteScore / blackScore;
    }

    public java.util.ArrayList<int[]> generateMoves(int[][] boardMatrix) {
        java.util.ArrayList<int[]> moveList = new java.util.ArrayList<>();
        int boardSize = boardMatrix.length;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (boardMatrix[i][j] > 0) continue;
                if (i > 0) {
                    if (j > 0) { if (boardMatrix[i - 1][j - 1] > 0 || boardMatrix[i][j - 1] > 0) { moveList.add(new int[]{i, j}); continue; } }
                    if (j < boardSize - 1) { if (boardMatrix[i - 1][j + 1] > 0 || boardMatrix[i][j + 1] > 0) { moveList.add(new int[]{i, j}); continue; } }
                    if (boardMatrix[i - 1][j] > 0) { moveList.add(new int[]{i, j}); continue; }
                }
                if (i < boardSize - 1) {
                    if (j > 0) { if (boardMatrix[i + 1][j - 1] > 0 || boardMatrix[i][j - 1] > 0) { moveList.add(new int[]{i, j}); continue; } }
                    if (j < boardSize - 1) { if (boardMatrix[i + 1][j + 1] > 0 || boardMatrix[i][j + 1] > 0) { moveList.add(new int[]{i, j}); continue; } }
                    if (boardMatrix[i + 1][j] > 0) { moveList.add(new int[]{i, j}); continue; }
                }
            }
        }
        return moveList;
    }

    public int getScore(int[][] board, boolean forX, boolean blacksTurn) {
        return evaluateHorizontal(board, forX, blacksTurn)
                + evaluateVertical(board, forX, blacksTurn)
                + evaluateDiagonal(board, forX, blacksTurn);
    }

    public static int evaluateHorizontal(int[][] boardMatrix, boolean forX, boolean playersTurn) {
        int consecutive = 0; int blocks = 2; int score = 0;
        for (int i = 0; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[0].length; j++) {
                if (boardMatrix[i][j] == (forX ? 2 : 1)) { consecutive++; } 
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) { blocks--; score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 1; } 
                    else { blocks = 1; }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 2;
                } else { blocks = 2; }
            }
            if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
            consecutive = 0; blocks = 2;
        }
        return score;
    }

    public static int evaluateVertical(int[][] boardMatrix, boolean forX, boolean playersTurn) {
        int consecutive = 0; int blocks = 2; int score = 0;
        for (int j = 0; j < boardMatrix[0].length; j++) {
            for (int i = 0; i < boardMatrix.length; i++) {
                if (boardMatrix[i][j] == (forX ? 2 : 1)) { consecutive++; } 
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) { blocks--; score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 1; } 
                    else { blocks = 1; }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 2;
                } else { blocks = 2; }
            }
            if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
            consecutive = 0; blocks = 2;
        }
        return score;
    }

    public static int evaluateDiagonal(int[][] boardMatrix, boolean forX, boolean playersTurn) {
        int consecutive = 0; int blocks = 2; int score = 0;
        // Đường chéo /
        for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
            int iStart = Math.max(0, k - boardMatrix.length + 1);
            int iEnd = Math.min(boardMatrix.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;
                if (boardMatrix[i][j] == (forX ? 2 : 1)) { consecutive++; } 
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) { blocks--; score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 1; } 
                    else { blocks = 1; }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 2;
                } else { blocks = 2; }
            }
            if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
            consecutive = 0; blocks = 2;
        }
        // Đường chéo \
        for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;
                if (boardMatrix[i][j] == (forX ? 2 : 1)) { consecutive++; } 
                else if (boardMatrix[i][j] == 0) {
                    if (consecutive > 0) { blocks--; score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 1; } 
                    else { blocks = 1; }
                } else if (consecutive > 0) {
                    score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn); consecutive = 0; blocks = 2;
                } else { blocks = 2; }
            }
            if (consecutive > 0) score += getConsecutiveSetScore(consecutive, blocks, forX == playersTurn);
            consecutive = 0; blocks = 2;
        }
        return score;
    }

    public static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
        final int winGuarantee = 1000000;
        if (blocks == 2 && count <= 5) return 0;
        switch (count) {
            case 5: return winScore;
            case 4: return currentTurn ? winGuarantee : (blocks == 0 ? winGuarantee / 4 : 200);
            case 3: 
                if (blocks == 0) return currentTurn ? 50000 : 200;
                else return currentTurn ? 10 : 5;
            case 2: 
                if (blocks == 0) return currentTurn ? 7 : 5;
                else return 3;
            case 1: return 1;
        }
        return winScore * 2;
    }

    public int[][] getMatrixBoard() {
        int[][] matrix = new int[ROW_COUNT][COL_COUNT];
        for (int i = 0; i < Buttons.length; i++) {
            for (int j = 0; j < Buttons.length; j++) {
                matrix[i][j] = Buttons[i][j].value;
            }
        }
        return matrix;
    }
    
    // Class hình nền
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                File file = new File(imagePath);
                if (file.exists()) backgroundImage = ImageIO.read(file);
            } catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 40, 40), 0, getHeight(), new Color(10, 10, 10));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}