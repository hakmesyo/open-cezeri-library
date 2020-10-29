/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import cezeri.call_back_interface.CallBackDataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elcezerilab
 */
public class FactoryDataBase {

    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;


    public static Connection connectDB(String ip, String dbName, String userName, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String timeZone="?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            try {
                connect = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName+timeZone, userName, password);
            } catch (SQLException ex) {
                Logger.getLogger(FactoryDataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FactoryDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }

    public static ResultSet getResultSet(String query) {
        try {
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(FactoryDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }

    public static void getResultSet(String sql, CallBackDataBase callBackDataBase) {
        try {
            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
            // Result set get the result of the SQL query
            resultSet = statement.executeQuery(sql);
            callBackDataBase.executeSQL(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(FactoryDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // You need to close the resultSet
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

}
