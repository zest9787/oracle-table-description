package net.lucidman.excel.table.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TableVO {

    private int id;
    private String tableName;
    private String comment;
    private List<ColumnVO> columns;

}
