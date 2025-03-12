import java.sql.*;

public class JDBCExample {
    public static void main(String[] args) {
        // 1. MySQL 연결 정보 설정
        String url = "jdbc:mysql://Junoui-MacBookAir.local:3306/testdb"; // 데이터베이스 주소
        String user = "root"; // mySQL 사용자
        String password = "";

        // 2. JDBC 연결 & SQL 실행
        try {
            // 2-1. MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2-2. 데이터베이스 연결
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("MySQL 연결 성공");

            // 2-3. SQL 실행 - 데이터 삽입
            String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name)";
            PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, "홍길동");
            pstmt.setString(2, "hong@knu.ac.kr");
            int rowInserted = pstmt.executeUpdate();
            System.out.println("데이터 삽입 완료! (" + rowInserted + " 행 추가)");

            // 2-4. SQL 실행 - 데이터 조회
            String selectSQL = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(selectSQL);
            System.out.println("사용자 목록: ");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("ID: " + id + ",이름 : " + name + ", 이메일: " + email);
            }

            // 2-5. 자원 해제
            rs.close();
            stmt.close();
            pstmt.close();
            conn.close();
            System.out.println("MySQL 연결 종료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}