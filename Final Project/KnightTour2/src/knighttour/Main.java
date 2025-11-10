package knighttour;

import javax.swing.SwingUtilities;

/**
 * Lớp Main khởi chạy ứng dụng GUI.
 */
public class Main {

    public static void main(String[] args) {
        // Khởi chạy GUI trên Event Dispatch Thread (EDT)
        // Đây là cách làm đúng chuẩn cho Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tạo và hiển thị JFrame
                new KnightTourGUI().setVisible(true);
            }
        });
    }
}