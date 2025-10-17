package com.teleauro.datamanagement.service;

import com.teleauro.datamanagement.model.RawAddress;
import com.teleauro.datamanagement.repository.RawAddressRepository;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class CsvImportService {

    @Autowired
    private RawAddressRepository repository;

    private static final int BATCH_SIZE = 1;

    @PostConstruct
    public void importExcel() {
        List<RawAddress> batch = new ArrayList<>();
        int rowNumber = 0;
        int totalImported = 0;
        int totalSkipped = 0;
        int totalFailed = 0;

        try (
            InputStream inputStream = getClass().getResourceAsStream("/Teleaurora_None_Edit.xlsx");
            Workbook workbook = new XSSFWorkbook(inputStream);
            PrintWriter logWriter = new PrintWriter(new FileWriter("failed_rows.log"))
        ) {
            if (inputStream == null) {
                throw new FileNotFoundException("Excel file not found in classpath.");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowNumber++;

                try {
                    // Validate only critical fields
                    if (isMissing(row, 0) || isMissing(row, 1) || isMissing(row, 2) ||
                        isMissing(row, 4) || isMissing(row, 5)) {
                        logWriter.println("Row " + rowNumber + " skipped: missing critical fields.");
                        totalSkipped++;
                        continue;
                    }

                    RawAddress address = new RawAddress();
                    address.setUprn((long) row.getCell(0).getNumericCellValue());
                    address.setSingleLineAddress(row.getCell(1).getStringCellValue().trim());
                    address.setPostcode(row.getCell(2).getStringCellValue().trim());
                    address.setLocalAuthorityDistrict(getStringCell(row, 3));
                    address.setLongitude(row.getCell(4).getNumericCellValue());
                    address.setLatitude(row.getCell(5).getNumericCellValue());
                    address.setPlanned(getStringCell(row, 6));
                    address.setOccComment(getStringCell(row, 7));

                    batch.add(address);
                } catch (Exception e) {
                    logWriter.println("Row " + rowNumber + " failed: " + e.getMessage());
                    totalFailed++;
                }

                if (batch.size() >= BATCH_SIZE) {
                    saveBatch(batch, logWriter, rowNumber);
                    totalImported += batch.size();
                    System.out.println("Imported batch of " + batch.size() + " rows. Total so far: " + totalImported);
                    batch.clear();
                }
            }

            // Final batch
            if (!batch.isEmpty()) {
                saveBatch(batch, logWriter, rowNumber);
                totalImported += batch.size();
                System.out.println("Imported final batch of " + batch.size() + " rows. Total so far: " + totalImported);
            }

            System.out.println("Excel import completed.");
            System.out.println("Total rows imported: " + totalImported);
            System.out.println("Total rows skipped: " + totalSkipped);
            System.out.println("Total rows failed: " + totalFailed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMissing(Row row, int index) {
        return row.getCell(index) == null || row.getCell(index).getCellType() == CellType.BLANK;
    }

    private String getStringCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return (cell != null && cell.getCellType() != CellType.BLANK) ? cell.getStringCellValue().trim() : "";
    }

    private void saveBatch(List<RawAddress> batch, PrintWriter logWriter, int rowNumber) {
        try {
            repository.saveAll(batch);
        } catch (Exception e) {
            logWriter.println("Batch failed at row " + rowNumber + ": " + e.getMessage());
            for (RawAddress addr : batch) {
                logWriter.println("Failed UPRN: " + addr.getUprn());
            }
        }
    }
}
