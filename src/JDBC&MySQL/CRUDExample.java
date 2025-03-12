import java.sql.*;
import java.util.Scanner;

public class CRUDExample {
    static final String URL = "jdbc:mysql://localhost:3306/testdb";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        //MySQL 연결 및 확인
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("JDBC 드라이버 로드 완료 !");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("MySQL 연결 성공 !");
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL 드라이버를 찾을 수 없음 !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("MySQL 연결 실패!");
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("선택하세요.");
                System.out.println("1. 삽입 | 2. 조회 | 3. 수정 | 4. 삭제 | 5. 종료");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 버퍼 비우기

                switch (choice) {
                    case 1:
                        createUser(scanner);
                        break;
                    case 2:
                        readUsers();
                        break;
                    case 3:
                        updateUser(scanner);
                        break;
                    case 4:
                        deleteUser(scanner);
                        break;
                    case 5:
                        System.out.println("프로그램 종료");
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 선택하세요.");
                }
            }
        }
    }

    private static void createUser(Scanner scanner) {
        System.out.println("이름 입력 : ");
        String name = scanner.nextLine();
        System.out.println("이메일 입력 : ");
        String email = scanner.nextLine();

        String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted + "명의 사용자가 추가되었습니다.");
        } catch (SQLException e) {
            System.out.println("데이터 삽입 실패 !");
            e.printStackTrace();
        }

    }

    private static void readUsers() {
        String selectSQL = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {
            System.out.println("\n 사용자 목록:");
            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("id") + ", 이름: " + rs.getString("name") + ", 이메일: " + rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("데이터 조회 실패!");
            e.printStackTrace();
        }
    }

    private static void updateUser(Scanner scanner) {
        System.out.print("수정할 사용자 ID 입력: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기
        System.out.print("새 이름 입력: ");
        String newName = scanner.nextLine();

        String updateSQL = "UPDATE users SET name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("사용자 정보가 업데이트되었습니다.");
            } else {
                System.out.println("해당 ID의 사용자가 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("데이터 수정 실패!");
            e.printStackTrace();
        }
    }

    private static void deleteUser(Scanner scanner) {
        System.out.print("삭제할 사용자 ID 입력: ");
        int id = scanner.nextInt();

        String deleteSQL = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("사용자 삭제 완료!");
            } else {
                System.out.println("해당 ID의 사용자가 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("데이터 삭제 실패!");
            e.printStackTrace();
        }
    }
}
