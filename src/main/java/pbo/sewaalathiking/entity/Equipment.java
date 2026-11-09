/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.entity;

/**
 *
 * @author ASUS
 */
public class Equipment {
    private int id;
    private String name;
    private String category;
    private double pricePerDay;
    private int totalStock;
    private int availableStock;

    public Equipment() {}

    public Equipment(int id, String name, String category, double pricePerDay, int totalStock, int availableStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.totalStock = totalStock;
        this.availableStock = availableStock;
    }
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }
    public String getCategory() { 
        return category; 
    }
    public void setCategory(String category) { 
        this.category = category; 
    }
    public double getPricePerDay() { 
        return pricePerDay; 
    }
    public void setPricePerDay(double pricePerDay) { 
        this.pricePerDay = pricePerDay; 
    }
    public int getTotalStock() { 
        return totalStock; 
    }
    public void setTotalStock(int totalStock) { 
        this.totalStock = totalStock; 
    }
    public int getAvailableStock() { 
        return availableStock; 
    }
    public void setAvailableStock(int availableStock) { 
        this.availableStock = availableStock; 
    }

}
