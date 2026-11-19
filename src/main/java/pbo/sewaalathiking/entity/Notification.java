/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.entity;

import java.time.LocalDateTime;

/**
 *
 * @author ASUS
 */
public class Notification {
    private int id;
    private int userId;
    private String message;
    private boolean isRead;
    private LocalDateTime scheduledTime;

    public Notification() {}

    public Notification(int id, int userId, String message, boolean isRead, LocalDateTime scheduledTime) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.scheduledTime = scheduledTime;
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getMessage() { 
        return message; 
    }
    public void setMessage(String message) { 
        this.message = message; 
    }

    public boolean isRead() { 
        return isRead; 
    }
    public void setRead(boolean isRead) { 
        this.isRead = isRead; 
    }

    public LocalDateTime getScheduledTime() { 
        return scheduledTime; 
    }
    public void setScheduledTime(LocalDateTime scheduledTime) { 
        this.scheduledTime = scheduledTime; 
    }
}
