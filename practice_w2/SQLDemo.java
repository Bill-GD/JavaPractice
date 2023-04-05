package practice_w2;

import java.sql.*; // Using 'Connection', 'Statement' and 'ResultSet' classes in java.sql package

public class SQLDemo { // Save as "JdbcSelectTest.java"
    public static void main(String[] args) {
        try (
                // Step 1: Construct a database 'Connection' object
                Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/classicmodels?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "root", "AMinecraftPlayer!"); // For MySQL only
                // The format is:
                // "jdbc:mysql://hostname:port/databaseName", "username", "password"

                // Step 2: Construct a 'Statement' object inside the Connection created
                // Statement objects are used to send queries to SQL server
                Statement statement = connection.createStatement();) {

            // Step 3: Write a SQL query string. Execute the SQL query via the 'Statement'.
            String strSelect = "select pa.customerNumber, pr.total, pa.paid from (select o. customerNumber, sum(od.quantityOrdered * od.priceEach) total from orderdetails od, orders o where o.orderNumber = od.orderNumber group by o.customerNumber) as pr, (select customerNumber, sum(amount) paid from payments group by customerNumber) as pa where pr.customerNumber = pa.customerNumber;";
            System.out.println("The SQL statement is: " + strSelect + "\n"); // Echo For debugging

            // The query result is returned in a 'ResultSet' object called 'resultSet'.
            ResultSet resultSet = statement.executeQuery(strSelect);

            // Step 4: Process the 'ResultSet' by scrolling the cursor forward via next().
            // For each row, retrieve the contents of the cells with getXxx(columnName).
            // Row-cursor initially positioned before the first row of the 'ResultSet'.
            // resultSet.next() inside the whole-loop repeatedly moves the cursor to the
            // next row.
            // It returns false if no more rows.
            System.out.println("The records selected are:");
            int rowCount = 0;
            while (resultSet.next()) { // Repeatedly process each row
                // retrieve a cell in the row
                // datatype var-name = <ResultSet>.<get<datatype>(<column-name>);
                // <column-name> is the column in the query result
                int customerNumber = resultSet.getInt("customerNumber");
                double total = resultSet.getDouble("total");
                double paid = resultSet.getDouble("paid");
                System.out.println(customerNumber + ", " + total + ", " + paid);
                ++rowCount;
            }
            System.out.println("Total number of records = " + rowCount);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Step 5: Close connection and statement - Done automatically by try-with-resources
        // (JDK 7)
    }
}
