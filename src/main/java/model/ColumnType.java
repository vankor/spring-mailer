package model;

/**
 * Created by Andrey on 19.09.2015.
 */
public enum ColumnType {
    NAME("name"), EMAIL("email");

    private String name;

    ColumnType(String key){
        this.name = key;
    }

    public String getName(){
        return this.name;
    }

    public static ColumnType findByName(String abbr){
        for(ColumnType v : values()){
            if(v.getName().equals(abbr)){
                return v;
            }
        }
        return null;
    }
}
