package net.lucidman.excel.table.vo;

import lombok.Data;

@Data
public class ColumnVO {

    private int id;
    private String columnName;
    private String dataType;
    private String isNull;
    private String key;
    private String defaultValue;
    private String comment;

}
