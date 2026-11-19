/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pbo.sewaalathiking.entity;

/**
 *
 * @author ASUS
 */
public class RentalDetail {
    private int id;
    private int transactionId;
    private int equipmentId;
    private int quantity;
    private double subtotalPrice;

    public RentalDetail() {}

    public RentalDetail(int id, int transactionId, int equipmentId, int quantity, double subtotalPrice) {
        this.id = id;
        this.transactionId = transactionId;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.subtotalPrice = subtotalPrice;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotalPrice() { return subtotalPrice; }
    public void setSubtotalPrice(double subtotalPrice) { this.subtotalPrice = subtotalPrice; }
}
