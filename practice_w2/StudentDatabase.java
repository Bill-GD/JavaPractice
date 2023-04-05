package practice_w2;

import java.sql.*;

public class StudentDatabase {
    public static void main(String[] args) {
        try (
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/qlsinhvien",
                        "root",
                        "AMinecraftPlayer!");

                Statement statement = connection.createStatement();) {

            statement.executeUpdate(
                    "create table if not exists SinhVien (MaSV int, TenSV varchar(50), Khoa varchar(50));");
            statement.executeUpdate("delete from SinhVien");

            int maxRecordCount = 10;
            for (int i = 0; i < maxRecordCount; i++) {
                String name = Character.toString((char) (i + 65));
                String insertStatement = "insert into SinhVien values (" + (i + 1) + ", '" + name + "', 'CNTT')";
                // use executeUpdate for all update statements
                statement.executeUpdate(insertStatement);
                System.out.println("Inserted (" + (i + 1) + ", '" + name + "', 'CNTT')");
            }

            int row = statement.executeUpdate("delete from SinhVien where MaSV=4 or MaSV=8");
            System.out.println("Deleted " + row + " record(s)");

            String queryString = "select * from SinhVien";
            System.out.println("Query: " + queryString);
            // use executeQuery for queries
            ResultSet resultSet = statement.executeQuery(queryString);

            while (resultSet.next()) {
                int studentID = resultSet.getInt("MaSV");
                String studentName = resultSet.getString("TenSV");
                String major = resultSet.getString("Khoa");

                System.out.println(studentID + ", " + studentName + ", " + major);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
