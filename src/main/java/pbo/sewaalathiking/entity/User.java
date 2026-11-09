/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.entity;

/**
 *
 * @author ASUS
 */
public class User {
    private int id;
    private String role; // 'ADMIN' atau 'CUSTOMER'
    private String name;
    private String phone;
    private String email;
    private String password;

    public User() {}

    public User(int id, String role, String name, String phone, String email, String password) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public String getRole() { 
        return role; 
    }
    public void setRole(String role) { 
        this.role = role; 
    }
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public String getPhone() { 
        return phone; 
    }
    public void setPhone(String phone) { 
        this.phone = phone; 
    }
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

}
