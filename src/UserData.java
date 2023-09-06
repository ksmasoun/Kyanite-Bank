/*
author = Karanveer Singh
*/
public class UserData {
    private String name;
    private String username;
    private String email;
    private String password;
    private double balance;

    public UserData(String name, String username, String email, String password, double balance) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}