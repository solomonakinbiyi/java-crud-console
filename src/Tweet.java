import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tweet {
    private static Connection con = null;

    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void menu() {
        System.out.println("Please choose an option below \n");
        System.out.println("1. Create an account with us \n");
        System.out.println("2. Login \n");
        System.out.println("3. Exit \n");

        Scanner myObj = new Scanner(System.in);

        int choice = myObj.nextInt();
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("Wrong choice");
                System.exit(0);
        }
    }

    public static void register() {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Create your account");
        System.out.println("");

        System.out.println("Please enter your first name");
        String fnm = myObj.nextLine();

        System.out.println("Please enter your last name");
        String lnm = myObj.nextLine();

        System.out.println("Please enter your username");
        String unm = myObj.nextLine();

        System.out.println("Please enter your password");
        String psw = myObj.nextLine();

        connect();

        Statement st = null;

        boolean userNameAlreadyExist = false;

        try {
            st = con.createStatement();
            ResultSet resultSet = st.executeQuery("SELECT username FROM userdetails WHERE username = '" + unm + "'");
            while (resultSet.next()) {
                userNameAlreadyExist = true;
            }

            if (userNameAlreadyExist) {
                System.out.println("Yellow! username already taken 😜😜");
                login();
            } else {
                insertData(fnm, lnm, unm, psw);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(String fnm, String lnm, String unm, String psw) {
        Statement statement = null;
        try {
            statement = con.createStatement();
            int executeUpdate = statement.executeUpdate("insert into userdetails(firstname, lastname, username, password) values('" + fnm + "','" + lnm + "','" + unm + "','" + psw + "')");
            if (executeUpdate > 0) {
                System.out.println("Hello " + fnm + " your details have been saved successfully");
                menu();
            } else {
                System.out.println("Please try again latter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void login() {
        Scanner myObj = new Scanner(System.in);

        System.out.println("Login menu");
        System.out.println("");

        System.out.println("Please enter your username");
        String unm = myObj.nextLine();

        System.out.println("Please enter your password");
        String psw = myObj.nextLine();

        String pswFromDb = "";

        connect();

        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT username, password FROM userdetails WHERE username = '" + unm + "'");

            boolean userNameExists = false;

            while (rs.next()) {
                pswFromDb = rs.getString("password");
                userNameExists = true;
            }
            if (userNameExists) {
                if (pswFromDb.equals(psw)) {
                    profile(unm);
                } else {
                    System.out.println("Username and password do not match");
                    menu();
                }
            } else {
                System.out.println("Username does not exist");
                menu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void profile(String unm) {
        System.out.println("Yello! Welcome dear " + unm + " 😁, create your tweet");

    }

    public static void main(String[] args) {
        menu();
    }
}
