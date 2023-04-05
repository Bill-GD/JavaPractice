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

            int maxRecordCount = 500;
            for (int i = 0; i < maxRecordCount; i++) {
                if (i + 33 >= 127)
                    break;
                if (i + 33 == 39 || i + 33 == 92) {
                    continue;
                }
                String name = Character.toString((char) (i + 33));
                String major = (i % 3 == 0) ? "Biology"
                        : (i % 7 == 0) ? "Chemistry"
                        : (i % 5 == 0) ? "Engineer"
                        : (i % 2 == 0) ? "Tour" : "CNTT";

                String insertStatement = "insert into SinhVien values (" + (i + 1) + ", '" + name + "', '" + major
                        + "')";
                // use executeUpdate for all update statements
                statement.executeUpdate(insertStatement);
                System.out.println("Inserted (" + (i + 1) + ", '" + name + "', '" + major + "')");
            }

            int rowDeleted = statement.executeUpdate("delete from SinhVien where MaSV=4");
            System.out.println("Deleted " + rowDeleted + " record(s)");

            String queryString = "select * from sinhvien where Khoa='CNTT';";
            System.out.println("Query: " + queryString);
            // use executeQuery for queries
            ResultSet resultSet = statement.executeQuery(queryString);

            int rowCount = 0;
            while (resultSet.next()) {
                int studentID = resultSet.getInt("MaSV");
                String studentName = resultSet.getString("TenSV");
                String major = resultSet.getString("Khoa");

                System.out.println(studentID + ", " + studentName + ", " + major);
                rowCount++;
            }
            System.out.println("Row count: " + rowCount);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
