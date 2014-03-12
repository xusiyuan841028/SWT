/*
 * Created on May 7, 2013 4:08:59 AM
 * 
 * By Phoenix Xu
 * Copyright Phoenix Xu, 2006-2013, All rights reserved.
 */

package excel;

import static utils.FileUtils.EXCEL_REPORT_PATH_NO_EXT;
import static utils.FileUtils.TEMP_EXCEL_PATH;
import gui.ExcelDownloader;
import gui.QueryProfileTab.ExcelFormat;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import utils.FileUtils;

/**
 * @author Phoenix Xu
 */
public class ExcelMerger {

    private static final Logger   logger          = LoggerFactory.getLogger(ExcelMerger.class);

    private static final int      DATA_ROWNUM_MIN = 1;

    private static final int      DATA_ROWNUM_MAX = 12;

    private static final int      DATA_COLNUM     = 2;

    private static final String[] Headers         = new String[13];

    static {
        Headers[0] = ExcelDownloader.getResourceString("AccountField");
        String[] months = ExcelDownloader.getResourceString("Months").split(ExcelDownloader.VALUE_SEPARATOR);
        System.arraycopy(months, 0, Headers, 1, months.length);
    }

    private File                  sourceFolder;

    private String                destination;

    private ExcelFormat           format;

    public ExcelMerger(String sourceFolder, String destination, ExcelFormat format) {
        this.sourceFolder = new File(sourceFolder);
        this.destination = format.getFileNameWithExt(destination);
        this.format = format;
    }

    public File getDestinationExcel() {
        return new File(this.destination);
    }

    public void mergeExcel() {
        Assert.isTrue(this.sourceFolder.isDirectory());

        // List all Excel files.
        File[] excelFiles = this.sourceFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(ExcelMerger.this.format.getSuffix());
            }
        });

        Workbook workbook = null;
        if (this.format == ExcelFormat.Excel2003) {
            workbook = new HSSFWorkbook();
        } else if (this.format == ExcelFormat.Excel2007) {
            workbook = new XSSFWorkbook();
        }

        String currentYear = null;
        Sheet sheet = null;
        // First
        int rownum = 1;
        for (File file : excelFiles) {
            logger.debug(file.getName());
            String[] fileNameSegments = file.getName().split(FileUtils.FILE_NAME_INTERNAL_SEPARATOR);
            String year = fileNameSegments[0];
            String account = fileNameSegments[1].substring(0, fileNameSegments[1].length() - 4);
            // Start handling the next year, so create a new sheet.
            if (currentYear == null || !currentYear.equals(year)) {
                if (sheet != null) {
                    // Adjust the width of every columns in the current sheet.
                    this.autoAdjustWidth(sheet);
                }
                // Create a new sheet.
                currentYear = year;
                sheet = workbook.createSheet(currentYear);
                this.createHeader(sheet);
                rownum = 1;
            }
            String[] data = this.getData(account, file);
            this.writeRecord(sheet, rownum, data);
            rownum++;
        }
        if (sheet != null) {
            // Adjust the width of every columns in the last sheet.
            this.autoAdjustWidth(sheet);
        }
        this.writeExcel(workbook);
    }

    private void writeExcel(Workbook workbook) {
        // Write the output to a file
        try (FileOutputStream fileOS = new FileOutputStream(this.destination)) {
            workbook.write(fileOS);
        } catch (IOException e) {
            logger.error("Can't write the Excel file for total report", e);
        }
    }

    private void createHeader(Sheet sheet) {
        // Freeze just one row as header
        sheet.createFreezePane(1, 1, 1, 1);

        // Setup just one row as header
        Row row = sheet.createRow(0);
        for (int i = 0; i < Headers.length; i++) {
            row.createCell(i).setCellValue(Headers[i]);
        }
    }

    private void autoAdjustWidth(Sheet sheet) {
        // Adjust the width of all columns
        for (int i = 0; i < Headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void writeRecord(Sheet sheet, int rownum, String[] data) {
        Row row = sheet.createRow(rownum);
        for (int i = 0; i < data.length; i++) {
            row.createCell(i).setCellValue(data[i]);
        }
    }

    private String[] getData(String account, File file) {
        String[] data = new String[13];
        Arrays.fill(data, "0");
        data[0] = account;

        Sheet sheet = null;
        try (FileInputStream fileIS = new FileInputStream(file)) {
            sheet = new HSSFWorkbook(fileIS).getSheetAt(0);
        } catch (IOException e) {
            logger.error("Can't read the Excel file " + file.getName(), e);
            return data;
        }
        for (int i = DATA_ROWNUM_MAX; i >= DATA_ROWNUM_MIN; i--) {
            Row row = sheet.getRow(i);
            if (row != null) {
                data[DATA_ROWNUM_MAX + 1 - i] = row.getCell(DATA_COLNUM).getStringCellValue();
            }
        }
        return data;
    }

    public static void main(String[] args) {
        ExcelMerger merger = new ExcelMerger(TEMP_EXCEL_PATH, EXCEL_REPORT_PATH_NO_EXT, ExcelFormat.Excel2003);
        merger.mergeExcel();
    }
}
