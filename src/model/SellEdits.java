package model;

import java.sql.Date;
import java.sql.Time;

public class SellEdits {
    private int useId;
    private String saleId;
    private Date date;
    private Time time;
    private double priviesAmount;
    private double newAmount;
    private double priviesDiscount;
    private double newDiscount;
    private int priviesQuantity;
    private int newQuantity;


    public SellEdits() {
    }

    public SellEdits(int useId, String sale_id, Date date, Time time, double priviesAmount,
                     double newAmount, double priviesDiscount, double newDiscount,
                     int priviesQuantity, int newQuantity) {
        this.useId = useId;
        this.saleId = sale_id;
        this.date = date;
        this.time = time;
        this.priviesAmount = priviesAmount;
        this.newAmount = newAmount;
        this.priviesDiscount = priviesDiscount;
        this.newDiscount = newDiscount;
        this.priviesQuantity = priviesQuantity;
        this.newQuantity = newQuantity;
    }


    public int getUseId() {
        return useId;
    }

    public void setUseId(int useId) {
        this.useId = useId;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public double getPriviesAmount() {
        return priviesAmount;
    }

    public void setPriviesAmount(double priviesAmount) {
        this.priviesAmount = priviesAmount;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }

    public double getPriviesDiscount() {
        return priviesDiscount;
    }

    public void setPriviesDiscount(double priviesDiscount) {
        this.priviesDiscount = priviesDiscount;
    }

    public double getNewDiscount() {
        return newDiscount;
    }

    public void setNewDiscount(double newDiscount) {
        this.newDiscount = newDiscount;
    }

    public int getPriviesQuantity() {
        return priviesQuantity;
    }

    public void setPriviesQuantity(int priviesQuantity) {
        this.priviesQuantity = priviesQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
