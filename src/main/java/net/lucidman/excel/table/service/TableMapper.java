package net.lucidman.excel.table.service;

import net.lucidman.excel.table.vo.ColumnVO;
import net.lucidman.excel.table.vo.TableVO;

import java.util.List;

public interface TableMapper {

    public List<TableVO> selectTables();

    public List<ColumnVO> selectColumns(String tableName);
}
