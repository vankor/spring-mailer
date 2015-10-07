package dto;

import model.ColumnType;

/**
 * Created by Andrey on 19.09.2015.
 */
public class ColumnDTO {

    private String type;
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
