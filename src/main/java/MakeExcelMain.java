import net.lucidman.excel.repository.MyBatisConnectionFactory;
import net.lucidman.excel.table.service.TableMapper;
import net.lucidman.excel.table.vo.ColumnVO;
import net.lucidman.excel.table.vo.TableVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

public class MakeExcelMain {

    private static final Logger logger = LoggerFactory.getLogger("TableDescription");

    public static void main(String[] args) {


        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.setDialogTitle("파일이 저장될 경로를 선택하세요!!!!!");

        int userSelection = jFileChooser.showSaveDialog(null);
        String saveFileName = null;
        logger.info("selection : " + userSelection);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = jFileChooser.getSelectedFile();
            saveFileName = fileToSave.getAbsolutePath();
            logger.info("folder : " + saveFileName);
        } else {
            return;
        }

        SqlSession sqlSession = MyBatisConnectionFactory.getSqlSession();
        List<TableVO> tables = null;
        try {
            TableMapper tableMapper = sqlSession.getMapper(TableMapper.class);
            tables = tableMapper.selectTables();
            List<ColumnVO> columns;
            for(TableVO tableVO: tables) {
                columns = tableMapper.selectColumns(tableVO.getTableName());
                tableVO.setColumns(columns);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }

        for (TableVO tableVO : tables) {

            logger.info("table : " + tableVO.getTableName());
            try (Workbook wb = new XSSFWorkbook()) {  //or new HSSFWorkbook();

                // 해더부분셀에 스타일을 주기위한 인스턴스 생성
                CellStyle headerStyle = wb.createCellStyle();
                headerStyle.setAlignment(HorizontalAlignment.CENTER); // 스타일인스턴스의 속성
                headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderRight(BorderStyle.THIN); // 테두리 설정
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                Font font = wb.createFont(); // 폰트 조정 인스턴스 생성
                font.setBold(true);//setBoldweight((short) 700);
                font.setFontHeightInPoints((short) 16);
                headerStyle.setFont(font);

                // 가운데 정렬과 얇은 테두리를 위한 스타일 인스턴스 생성
                CellStyle cellStyle0 = wb.createCellStyle();
                cellStyle0.setAlignment(HorizontalAlignment.CENTER);
                cellStyle0.setBorderRight(BorderStyle.THIN);
                cellStyle0.setBorderLeft(BorderStyle.THIN);
                cellStyle0.setBorderTop(BorderStyle.THIN);
                cellStyle0.setBorderBottom(BorderStyle.THIN);
                Font font0 = wb.createFont(); // 폰트 조정 인스턴스 생성
                font0.setFontHeightInPoints((short) 14);
                cellStyle0.setFont(font0);

                // 얇은 테두리를 위한 스타일 인스턴스 생성
                CellStyle cellStyle1 = wb.createCellStyle();
                cellStyle1.setBorderRight(BorderStyle.THIN);
                cellStyle1.setBorderLeft(BorderStyle.THIN);
                cellStyle1.setBorderTop(BorderStyle.THIN);
                cellStyle1.setBorderBottom(BorderStyle.THIN);
                Font font1 = wb.createFont(); // 폰트 조정 인스턴스 생성
                font1.setFontHeightInPoints((short) 14);
                cellStyle1.setFont(font1);

                Sheet sheet = wb.createSheet(tableVO.getTableName());
                Row row = null;
                Cell cell = null;
                // 셀별 널비 정하기 헤더 그리기
                sheet.setColumnWidth((short) 0, (short) 2000);
                sheet.setColumnWidth((short) 1, (short) 5000);
                sheet.setColumnWidth((short) 2, (short) 4000);
                sheet.setColumnWidth((short) 3, (short) 3000);
                sheet.setColumnWidth((short) 4, (short) 3000);
                sheet.setColumnWidth((short) 5, (short) 5000);
                sheet.setColumnWidth((short) 6, (short) 10000);

                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 6));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 6));

                // 시트에 타이틀 행을 하나 생성한다.(i 값이 0이면 첫번째 줄에 해당)
                int k = 0;
                row = sheet.createRow((short) k++);
                cell = row.createCell(0);
                cell.setCellValue("TableName");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(1);
                cell.setCellStyle(headerStyle);

                cell = row.createCell(2);
                cell.setCellValue(tableVO.getTableName());
                cell.setCellStyle(cellStyle1);

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(4);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(5);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(6);
                cell.setCellStyle(cellStyle1);

                row = sheet.createRow((short) k++);
                cell = row.createCell(0);
                cell.setCellValue("Description");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(1);
                cell.setCellStyle(headerStyle);

                cell = row.createCell(2);
                cell.setCellValue(tableVO.getComment());
                cell.setCellStyle(cellStyle1);

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(4);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(5);
                cell.setCellStyle(cellStyle1);
                cell = row.createCell(6);
                cell.setCellStyle(cellStyle1);


                row = sheet.createRow((short) k++);

                cell = row.createCell(0);
                cell.setCellValue("No");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(1);
                cell.setCellValue("FieldName");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(2);
                cell.setCellValue("DataType");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(3);
                cell.setCellValue("Null");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(4);
                cell.setCellValue("Key");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(5);
                cell.setCellValue("Extra");
                cell.setCellStyle(headerStyle);

                cell = row.createCell(6);
                cell.setCellValue("Comment");
                cell.setCellStyle(headerStyle);

                List<ColumnVO> structList = tableVO.getColumns();

                for (int j = 0; j < structList.size(); j++, k++) {
                    // 시트에 하나의 행을 생성한다(i 값이 0이면 첫번째 줄에 해당)
                    row = sheet.createRow((short) k);

                    ColumnVO struct = structList.get(j);

                    // 생성된 row에 컬럼을 생성한다
                    int x = 0;
                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getId());
                    cell.setCellStyle(cellStyle0);

                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getColumnName());
                    cell.setCellStyle(cellStyle1);

                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getDataType());
                    cell.setCellStyle(cellStyle0);

                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getIsNull());
                    cell.setCellStyle(cellStyle0);

                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getKey());
                    cell.setCellStyle(cellStyle0);

                    cell = row.createCell(x++);
                    cell.setCellValue("");
                    cell.setCellStyle(cellStyle0);

                    cell = row.createCell(x++);
                    cell.setCellValue(struct.getComment());
                    cell.setCellStyle(cellStyle1);
                }
//            }


                // Write the output to a file
                try (FileOutputStream fileOut = new FileOutputStream(saveFileName + "/" + tableVO.getTableName()+".xlsx")) {
                    wb.write(fileOut);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "확인", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return;
                }

            } catch (IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(null, ioe.getLocalizedMessage(), "확인", JOptionPane.ERROR_MESSAGE);
            }

        }

        JOptionPane.showMessageDialog(null, saveFileName + " 경로에 엑셀파일이 저장되었습니다.", "확인", JOptionPane.INFORMATION_MESSAGE);
    }
}
