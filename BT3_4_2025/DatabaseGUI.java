package BT3_4_2025;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseGUI extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "universityms";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public DatabaseGUI() {
        setTitle("Tạo Bảng Cơ Sở Dữ Liệu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        JLabel titleLabel = new JLabel("Chọn bảng để tạo:", SwingConstants.CENTER);
        add(titleLabel);

        JButton btnCreateStudent = new JButton("Tạo Bảng Student");
        JButton btnCreateClass = new JButton("Tạo Bảng Class");
        JButton btnCreateLearn = new JButton("Tạo Bảng Learn");

        add(btnCreateStudent);
        add(btnCreateClass);
        add(btnCreateLearn);

        btnCreateStudent.addActionListener(e -> createTable("Student"));
        btnCreateClass.addActionListener(e -> createTable("Class"));
        btnCreateLearn.addActionListener(e -> createTable("Learn"));
    }

    private void createTable(String tableName) {
        try {
            Connection conn = DriverManager.getConnection(URL + DATABASE_NAME, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            String sql = "";
            if (tableName.equals("Student")) {
                sql = "CREATE TABLE IF NOT EXISTS Student ("
                        + "StudentID INT AUTO_INCREMENT PRIMARY KEY, "
                        + "name VARCHAR(100) NOT NULL, "
                        + "age INT NOT NULL, "
                        + "email VARCHAR(100) UNIQUE NOT NULL, "
                        + "gpa FLOAT NOT NULL)";
            } else if (tableName.equals("Class")) {
                sql = "CREATE TABLE IF NOT EXISTS Class ("
                        + "ClassID INT AUTO_INCREMENT PRIMARY KEY, "
                        + "description VARCHAR(255) NOT NULL, "
                        + "numberOfCredits INT NOT NULL)";
            } else if (tableName.equals("Learn")) {
                sql = "CREATE TABLE IF NOT EXISTS Learn ("
                        + "LearnID INT AUTO_INCREMENT PRIMARY KEY, "
                        + "StudentID INT, "
                        + "ClassID INT, "
                        + "FOREIGN KEY (StudentID) REFERENCES Student(StudentID) ON DELETE CASCADE, "
                        + "FOREIGN KEY (ClassID) REFERENCES Class(ClassID) ON DELETE CASCADE)";
            }

            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "✔ Bảng '" + tableName + "' đã được tạo hoặc đã tồn tại!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseGUI gui = new DatabaseGUI();
            gui.setVisible(true);
        });
    }
}
