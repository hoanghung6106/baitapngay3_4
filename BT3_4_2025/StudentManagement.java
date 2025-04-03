package BT3_4_2025;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.*;
import java.util.Scanner;
import java.net.URL;

class StudentManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/universityms";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Thêm sinh viên");
                System.out.println("2. Hiển thị danh sách sinh viên");
                System.out.println("3. Đăng nhập");
                System.out.println("4. Thoát");
                System.out.print("Chọn chức năng: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        addStudent(conn, scanner);
                        break;
                    case 2:
                        listStudents(conn);
                        break;
                    case 3:
                        login(scanner);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Nhập tên: ");
        String name = scanner.nextLine();
        System.out.print("Nhập tuổi: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập GPA: ");
        double gpa = scanner.nextDouble();
        scanner.nextLine();
        String email = generateEmail(name);
        String sql = "INSERT INTO Student (name, age, email, gpa) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setDouble(4, gpa);
            stmt.executeUpdate();
        }
        System.out.println("Thêm sinh viên thành công. Email: " + email);
    }

    private static void listStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Student";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " - " + rs.getString("name") + " - " + rs.getString("email"));
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Nhập email: ");
        String email = scanner.nextLine();
        System.out.println("Đăng nhập thành công với email " + email);
    }

    private static String generateEmail(String name) {
        return name.toLowerCase().replace(" ", "") + "@vku.udn.vn";
    }
    private static void addStudent(Scanner scanner) throws IOException {
        System.out.print("Nhập tên: ");
        String name = scanner.nextLine();
        System.out.print("Nhập tuổi: ");
        int age = scanner.nextInt();
        System.out.print("Nhập GPA: ");
        double gpa = scanner.nextDouble();
        scanner.nextLine();

        String jsonInput = String.format("{\"name\":\"%s\", \"age\":%d, \"gpa\":%.2f}", name, age, gpa);

        sendPostRequest("http://localhost/student_api/add_student.php", jsonInput);
    }

    private static void sendPostRequest(String urlStr, String jsonInput) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }
}
