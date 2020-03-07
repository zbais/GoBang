package GoBangPkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


class Chess{
    int x;
    int y;
    int xPosition;
    int yPosition;
    int isPlayer1;
}

public class GoBang {

    public static ArrayList<Chess> chessList = new ArrayList<Chess>();
    public static JFrame frame = new JFrame("五子棋");
    public static JPanel boardPanel = new BoardPanel();
    public static JPanel infoPanel = new InfoPanel();

    public static int Margin = 40;
    public static int isPlayer1 = 1;
    public static boolean existWarning = false;
    public static boolean isOver = false;

    public static void drawBoard() {
        frame.setBounds(300, 100, 800, 600);
        frame.setBackground(new Color(190, 190, 190));
        frame.add(boardPanel);
        frame.add(infoPanel);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static Chess searchPosition(int x, int y) {
        Chess chess = new Chess();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int xPosition = i * 35 + Margin;
                int yPosition = j * 35 + Margin;
                if (Math.abs(xPosition - x) <= 35 / 2 && Math.abs(yPosition - y) <= 35 / 2) {
                    chess.x = j;
                    chess.y = i;
                    chess.xPosition = xPosition;
                    chess.yPosition = yPosition;
                }
            }
        }
        return chess;
    }

    public static boolean containsChess(ArrayList<Chess> chessList, Chess chess) {
        for (Chess c : chessList) {
            if (c.x == chess.x && c.y == chess.y) {
                return true;
            }
        }
        return false;
    }

    public static boolean subWhoWin(ArrayList<Integer> list) {
        if ((list.size() >= 5)) {
            int max = 0, min = 15;
            for (Integer i : list) {
                if (i > max) {
                    max = i;
                }
                if (i < min) {
                    min = i;
                }
            }
            if (max - min == list.size() - 1) {
                return true;
            }
        }
        return false;
    }

    public static int whoWin(ArrayList<Chess> chesslist, Chess chess) {
        ArrayList<Integer> rowArry = new ArrayList<Integer>();
        ArrayList<Integer> colArry = new ArrayList<Integer>();
        ArrayList<Integer> diagArry = new ArrayList<Integer>();
        ArrayList<Integer> backDiagArry = new ArrayList<Integer>();
        for (Chess c : chesslist) {
            int x = c.x;
            int y = c.y;
            int isPlayer1 = c.isPlayer1;
            if (isPlayer1 == chess.isPlayer1) {
                if (x == chess.x && Math.abs(y - chess.y) < 5) {
                    rowArry.add(y);
                }
                if (y == chess.y && Math.abs(x - chess.x) < 5) {
                    colArry.add(x);
                }
                if ((x - y) == (chess.x - chess.y) && (x - chess.x) < 5) {
                    diagArry.add(x);
                }
                if ((x + y) == (chess.x + chess.y) && (x - chess.x) < 5) {
                    backDiagArry.add(x);
                }
            }
        }
        int status = 0;
        if (subWhoWin(rowArry) || subWhoWin(colArry) || subWhoWin(diagArry) || subWhoWin(backDiagArry)) {
            status = isPlayer1 == 1 ? 1 : 2;
        }
        return status;
    }

    public static void onBoard(int x, int y) {
        Chess chess = searchPosition(x, y);
        if (containsChess(chessList, chess)) {
            existWarning = true;
            infoPanel.repaint();
            return;
        } else {
            chess.isPlayer1 = isPlayer1;
            chessList.add(chess);
        }
        infoPanel.repaint();
        boardPanel.repaint();
        int status = whoWin(chessList, chess);
        if (status == 1) {
            JOptionPane.showMessageDialog(null, "黑方胜利！");
            isOver = true;
        } else if (status == 2) {
            JOptionPane.showMessageDialog(null, "白方胜利！");
            isOver = true;
        }
        isPlayer1 = isPlayer1 == 1 ? 0 : 1;
    }

    public static void running() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON1){
                    if (!isOver && e.getX() > Margin && e.getY() > Margin
                            && e.getX() < Margin + 14 * 35 && e.getY() < Margin + 14 * 35) {
                        onBoard(e.getX(), e.getY());
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if(chessList.size()>0){
                        chessList.remove(chessList.size()-1);
                        isPlayer1 = isPlayer1 == 1 ? 0 : 1;
                        isOver = false;
                        boardPanel.repaint();
                        infoPanel.repaint();
                    }
                }

            }

        });

    }

    public static void main(String[] args) {
        drawBoard();
        running();
    }

    static class InfoPanel extends JPanel {
        public InfoPanel() {
            setBounds(600, 0, 200, 600);
        }

        @Override
        public void paint(Graphics g) {
//            super.paint(g);
            Font f = new Font(Font.DIALOG, Font.BOLD, 30);
            g.setFont(f);
            g.drawString("五子棋", 645, 100);
            f = new Font(Font.DIALOG, Font.BOLD, 15);
            g.setFont(f);
            g.drawString("1. 黑先白后交替下", 620, 180);
            g.drawString("2. 纵横斜连五子胜", 620, 200);
            g.drawString("3. 左键落子右悔棋", 620, 220);
            Font f20 = new Font(Font.DIALOG, Font.BOLD, 20);
            g.setFont(f20);
            g.drawString("游戏规则：", 610, 150);
            g.drawString("当前执子：", 610, 280);
            if (isPlayer1 == 1) {
                g.setColor(Color.BLACK);
            } else {
                g.setColor(Color.WHITE);
            }
            g.fillOval(660, 300, 50, 50);
            if (existWarning) {
                g.setColor(Color.RED);
                g.setFont(f);
                g.drawString("此处已有棋子！", 640, 430);
                existWarning = false;
            } else {
                g.setColor(new Color(190, 190, 190));
                g.fillRect(600, 400, 200, 200);
            }
        }
    }

    static class BoardPanel extends JPanel{
        public BoardPanel(){
            setBounds(0, 0, 600, 600);
            setBackground(new Color(211, 211, 211));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int i = 0; i < 15; i++) {
                g.drawLine(Margin, Margin+i*35, Margin+14*35, Margin+i*35);
                g.drawLine(Margin+i*35, Margin, Margin+i*35, Margin+14*35);
            }
            int num = 1;
            for(Chess chess: chessList){
                if(((Chess) chess).isPlayer1==1){
                    g.setColor(Color.BLACK);
                    g.fillOval(chess.xPosition-15,chess.yPosition-15, 30, 30);
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(num),chess.xPosition-5, chess.yPosition+5);
                }else{
                    g.setColor(Color.WHITE);
                    g.fillOval(chess.xPosition-15,chess.yPosition-15, 30, 30);
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(num),chess.xPosition-5, chess.yPosition+5);
                }
                num++;
            }
        }
    }

}