package model.tableRows.itemWindow;

import javafx.scene.control.Button;

public class Item {
    private int id;
    private String name;
    private Button deleteBtn;

    public Item() {}

    public Item(int id, String name, Button deleteBtn) {
        this.id = id;
        this.name = name;
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
}
