package org.BuyDirect_Android_utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.DataFormatter;

import org.apache.poi.ss.usermodel.*;

public class ExcelUtility {

	//Data Driven Approach - driving the test data from excel file
	
    private static Sheet getExcelSheet(String filePath, String sheetName) throws IOException {
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            return workbook.getSheet(sheetName);
        }
    }

    public static int getRowCount(String filePath, String sheetName) {
        try {
            Sheet sheet = getExcelSheet(filePath, sheetName);
            if (sheet != null) {
                return sheet.getLastRowNum() + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if unable to get row count
    }

    public static int getColumnCount(String filePath, String sheetName) {
        try {
            Sheet sheet = getExcelSheet(filePath, sheetName);
            if (sheet != null) {
                Row firstRow = sheet.getRow(0);
                if (firstRow != null) {
                    return firstRow.getLastCellNum();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if unable to get column count
    }

    public static String getCellData(String filePath, String sheetName, String colName, int rowNum) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    int colIndex = getColIndexByName(sheet, colName); // Get the column index by name
                    if (colIndex != -1) {
                        Cell cell = row.getCell(colIndex);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    return cell.getStringCellValue();
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Format as per your requirement
                                        return dateFormat.format(cell.getDateCellValue());
                                    } else {
                                        // Format numeric values as strings using DataFormatter
                                        DataFormatter formatter = new DataFormatter();
                                        return formatter.formatCellValue(cell);
                                    }
                                case BOOLEAN:
                                    return String.valueOf(cell.getBooleanCellValue());
                                case FORMULA:
                                    // Handle formula cells by evaluating the formula
                                    FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
                                    CellValue cellValue = evaluator.evaluate(cell);
                                    return cellValue.formatAsString();
                                case BLANK:
                                    return " "; // Return a space for blank cells
                                default:
                                    return ""; // Handle other cell types if needed
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // Return empty string if unable to get cell data
    }

    private static int getColIndexByName(Sheet sheet, String colName) {
        Row firstRow = sheet.getRow(0); // Assuming the first row contains headers
        if (firstRow != null) {
            for (Cell cell : firstRow) {
                if (cell.getCellType() == CellType.STRING) {
                    if (cell.getStringCellValue().trim().equalsIgnoreCase(colName)) {
                        return cell.getColumnIndex();
                    }
                }
            }
        }
        return -1;
    }
    
    
    
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    