package core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBConnection {
    private String userName;
    private String password;
    private String dbms;
    private String serverName;
    private int portNumber;
    private String databaseName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbms() {
        return dbms;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public  java.sql.Connection getConnection() throws SQLException, IOException {


        java.sql.Connection conn;
        String str = "jdbc:" + this.dbms + "://" +
                this.serverName+":"+this.portNumber+"/"+this.databaseName;
        System.out.println(str);
        conn = DriverManager.getConnection(str,
                this.userName, this.password);
        return conn;
    }
}

