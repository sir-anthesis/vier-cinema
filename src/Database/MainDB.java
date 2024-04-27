/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;
import Home.HomeMovies;
import Login.Login;
import static Login.Login.pw;
import static Login.Login.username;
import java.sql.DriverManager;
import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class MainDB {
    
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db_cinema";
    private static final String USER = "root";
    private static final String PASS = "";
    
    private static Connection cn;
    private static Statement st;
    private static ResultSet rs;
    
    public static void cn(){
        try {
            Class.forName(JDBC_DRIVER);
            cn = DriverManager.getConnection(DB_URL, USER, PASS);
            JOptionPane.showMessageDialog(null, "Koneksi Berhasil");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(0);
        }
    }
    
    public static void table() {
        DefaultTableModel tb_User = new DefaultTableModel();
        tb_User.addColumn("UserID");
        tb_User.addColumn("PhoneNumber");
        tb_User.addColumn("Name");
        tb_User.addColumn("Username");
        tb_User.addColumn("Password");
        tb_User.addColumn("Balance");
        
        try {
            st = cn.createStatement();
            rs = MainDB.st.executeQuery("SELECT * FROM user");
            
            int count = 0;
            while (rs.next()) {
                tb_User.addRow(new Object[] {
                    rs.getInt("UserID"),
                    rs.getInt("PhoneNumber"),
                    rs.getString("Name"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getInt("Balance"),
                });
                count++;
                dbUser.jTable1.setModel(tb_User);
              }
            
            if (count==0) {
                dbUser.jTable1.setModel(tb_User);
            }
            st.close();
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public static void tableClick() {
        dbUser.jTextFieldUserID.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 0).toString());
        dbUser.jTextFieldPhoneNumber.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 1).toString());
        dbUser.jTextFieldName.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 2).toString());
        dbUser.jTextFieldUsername.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 3).toString());
        dbUser.jPasswordFieldPw.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 4).toString());
        dbUser.jTextFieldBalance.setText(dbUser.jTable1.getValueAt(dbUser.jTable1.getSelectedRow(), 5).toString());
    }
    
    public static boolean addUser (String name, String username, String pw, int userID, int balance, int noHP) {
        boolean user = false;
        try {
            st = cn.createStatement(); 
            
            String add = "INSERT INTO user VALUES ('" + userID + "','" + noHP + "','"
                    + name + "','" + username + "','"
                    + pw + "','" + balance + "')";
            
            user = st.execute(add);
            JOptionPane.showMessageDialog(null, "Berhasil");
            
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
          }
        return user;
    }
    
    public static boolean deleteUser(int userID) {
        boolean user = false;
        
        try {
            st = cn.createStatement();
            
            String delete = "DELETE FROM user WHERE UserID = " + userID;
            
            if( !st.execute(delete) ){
                 user = true;
               }
            st.close();
        } catch (Exception e) {
          e.printStackTrace();
          }
        return user;
  }
    
    public static boolean updateUser(String name, String username, String pw, int balance, int noHP, int userID) {
        boolean user = false;
        try {
            st = cn.createStatement();

            String update = "SELECT * FROM user WHERE UserID = " + userID;

            rs = st.executeQuery(update);

            int noHPx = 0, balancex = 0;
            String namex = "", usernamex = "", pwx = "";

            while (rs.next()) {
                noHPx = rs.getInt("PhoneNumber");
                namex = rs.getString("Name");
                usernamex = rs.getString("Username");
                pwx = rs.getString("Password");
                balancex = rs.getInt("Balance");
            }

            if (noHP != 0) {
                noHPx = noHP;
            }
            if (!name.equalsIgnoreCase("")) {
                namex = name;
            }
            if (!username.equalsIgnoreCase("")) {
                usernamex = username;
            }
            if (!pw.equalsIgnoreCase("")) {
                pwx = pw;
            }
            if (balance != 0) {
                balancex = balance;
            }

            String queryUpdate = "UPDATE user SET PhoneNumber = '" + noHPx + "', Name = '" + namex + "', Username = '" + usernamex + "', Password = '" + pwx + "', Balance = " + balancex + " WHERE UserID = " + userID;

            if (st.executeUpdate(queryUpdate) > 0) {
                user = true;
            }

            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return user;
    }


    
    public static void searchUser( int userID ) {
    try {
      st = cn.createStatement();

      String query = "SELECT * FROM user WHERE UserID LIKE '%" + userID + "%'";
      rs = st.executeQuery(query);

      int count = 0;
      
      DefaultTableModel tb_User = new DefaultTableModel();
        tb_User.addColumn("UserID");
        tb_User.addColumn("PhoneNumber");
        tb_User.addColumn("Name");
        tb_User.addColumn("Username");
        tb_User.addColumn("Password");
        tb_User.addColumn("Balance");
      
      while( rs.next() ){
        tb_User.addRow(new Object[] {
            rs.getInt("UserID"),
            rs.getInt("PhoneNumber"),
            rs.getString("Name"),
            rs.getString("Username"),
            rs.getString("Password"),
            rs.getInt("Balance"),
        });
        count++;
        dbUser.jTable1.setModel(tb_User);
      }
      
      if( count == 0 ){
        JOptionPane.showMessageDialog(null, "User Not Found");
      }
      st.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    
    public static void login( String username, String pw, String name, int balance, int userID){
      try {
      st = cn.createStatement();

      String query = "SELECT * FROM user WHERE Username LIKE '%" + username + "%'";
      rs = st.executeQuery(query);
      
      while( rs.next() ){
      username = rs.getString("Username");
      pw = rs.getString("Password");
      HomeMovies.userID = rs.getInt("UserID");
      }
      
              if (Login.username.equals(username) && Login.pw.equals(pw)) {
              HomeMovies hm = new HomeMovies();
              hm.setVisible(true);
              hm.pack();
              hm.setLocationRelativeTo(null);
              hm.setDefaultCloseOperation(Login.EXIT_ON_CLOSE);
              
              Login.valid = true;
          }
              else {
              JOptionPane.showMessageDialog(null, "Your Username or Password are Incorrect");
          }
          st.close();
      }
      catch (Exception e){
          JOptionPane.showMessageDialog(null, e.getMessage());
      }
    }
    
    public static String home (int userID, int balance, int phoneNumber, String name) {
        try {
            st = cn.createStatement();
            
            String show = "SELECT * FROM user WHERE UserID = " + userID;

            rs = st.executeQuery(show);
            
            while( rs.next() ){
              HomeMovies.userID = rs.getInt("UserID");
              HomeMovies.phoneNumber = rs.getInt("PhoneNumber");
              HomeMovies.name = rs.getString("Name");
              Home.HomeTopUp.total = rs.getInt("Balance");
              }
            st.close();
        }
        catch (Exception e){
          JOptionPane.showMessageDialog(null, e.getMessage());
      }
        return "salah";
    }
    
    public static boolean order ( int orderID, int userID, int totalPrice, int movieID, int tickets, String seats, String time, String studio) {
        boolean user = false;
        try {
            st = cn.createStatement(); 
            
            String add = "INSERT INTO orders VALUES ('" + orderID + "','" + userID + "','"
                    + movieID + "','" + tickets + "','" + seats + "','"
                    + time + "','" + totalPrice + "','" + studio + "')";
            
            user = st.execute(add);
            JOptionPane.showMessageDialog(null, "Berhasil");
            
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
          }
        return user;
    }
    
    public static void updateBalance (int total, int userID){
        boolean user = false;
        try {
            st = cn.createStatement();
            
            String query = "SELECT * FROM user WHERE UserID = " + userID;
            
            
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                rs.getInt("UserID");
                rs.getInt("Balance");
            }
            userID = HomeMovies.userID;
            total = Home.HomeTopUp.total;
            
            String update = "UPDATE user SET Balance = '" + total + "' WHERE UserID = " + userID;
            if( !st.execute(update) ){
                user = true;
                }
            
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public static boolean topup (int topupID, int userID, String bank, int nominal) {
        boolean user = false;
        try {
            st = cn.createStatement(); 
            
            String add = "INSERT INTO topup VALUES ('" + topupID + "','" + userID + "','" + bank + "','"
                    + nominal + "')";
            
            user = st.execute(add);
            JOptionPane.showMessageDialog(null, "Berhasil");
            
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
          }
        return user;
    }
}



