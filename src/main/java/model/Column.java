package model;

/**
 * Created by Andrey on 19.09.2015.
 */
public class Column {

    private ColumnType type;
    private Integer number;

    public ColumnType getType() {
        return type;
    }

    public void setType(ColumnType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (type != column.type) return false;
        return !(number != null ? !number.equals(column.number) : column.number != null);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    public Integer getNumber() {
        return number;

    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
