package model.tableRows.itemWindow;

import javafx.scene.control.Button;

import java.sql.Date;
import java.sql.Time;

public class Item {
    private int id;
    private String name;
    private String userName;
    private Date date;
    private Time time;
    private Button deleteBtn;


    public Item() {}

    public Item(int id, String name, String userName, Date date, Time time, Button deleteBtn) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.deleteBtn = deleteBtn;
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

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public void setDeleteBtn(Button deleteBtn) {
        this.deleteBtn = deleteBtn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
