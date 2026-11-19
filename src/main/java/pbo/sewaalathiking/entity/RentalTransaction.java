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
public class RentalTransaction {
    private int id;
    private int userId;
    private LocalDateTime startDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private double totalCost;
    private double penaltyFee;
    private String status;

    public RentalTransaction() {}

    public RentalTransaction(int id, int userId, LocalDateTime startDate, LocalDateTime expectedReturnDate,
                             LocalDateTime actualReturnDate, double totalCost, double penaltyFee, String status) {
        this.id = id;
        this.userId = userId;
        this.startDate = startDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.totalCost = totalCost;
        this.penaltyFee = penaltyFee;
        this.status = status;
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

    public LocalDateTime getStartDate() { 
        return startDate; 
    }
    public void setStartDate(LocalDateTime startDate) { 
        this.startDate = startDate; 
    }

    public LocalDateTime getExpectedReturnDate() { 
        return expectedReturnDate; 
    }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { 
        this.expectedReturnDate = expectedReturnDate; 
    }

    public LocalDateTime getActualReturnDate() { 
        return actualReturnDate; 
    }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { 
        this.actualReturnDate = actualReturnDate; 
    }

    public double getTotalCost() { 
        return totalCost; 
    }
    public void setTotalCost(double totalCost) { 
        this.totalCost = totalCost; 
    }

    public double getPenaltyFee() { 
        return penaltyFee; 
    }
    public void setPenaltyFee(double penaltyFee) { 
        this.penaltyFee = penaltyFee; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
