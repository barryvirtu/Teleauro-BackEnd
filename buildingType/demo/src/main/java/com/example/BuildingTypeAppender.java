package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BuildingTypeAppender {
    static final String OS_API_KEY = "2YyaDjUfLM47ZJukzauryJA4qBr3Bemv"; // Replace with your OS key
    static final String LOG_FILE = "descriptions.log";

    static int findResumeRow(Sheet sheet, int baseCol) {
    int lastRow = sheet.getLastRowNum();
    for (int i = 1; i <= lastRow; i++) { // skip header at row 0
        Row row = sheet.getRow(i);
        if (row == null) continue;
        Cell cell = row.getCell(baseCol); // "Building Type" column
        if (cell == null || cell.toString().trim().isEmpty()) {
            return i; // first unprocessed row
        }
    }
    return lastRow + 1; // everything done
}


public static void main(String[] args) throws Exception {
    File file = new File("Teleaurora_BuildingTypes4027.xlsx");
    Workbook workbook;
    Sheet sheet;

    if (file.exists()) {
        // Resume on already processed file
        FileInputStream fis = new FileInputStream(file);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheetAt(0);
        fis.close();
    } else {
        // Start fresh from original
        FileInputStream fis = new FileInputStream("Teleaurora_BuildingTypes.xlsx");
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheetAt(0);
        fis.close();

        // Add headers only once
        Row header = sheet.getRow(0);
        int baseCol = header.getLastCellNum();
        header.createCell(baseCol).setCellValue("Building Type");
        header.createCell(baseCol + 1).setCellValue("Building Use");
        header.createCell(baseCol + 2).setCellValue("Connectivity");
        header.createCell(baseCol + 3).setCellValue("Is Main?");
        header.createCell(baseCol + 4).setCellValue("Area (m²)");
    }

    // Determine baseCol dynamically
    Row header = sheet.getRow(0);
    int baseCol = header.getLastCellNum() - 5; // new columns start 5 from the end

    // Find where to resume
    int startRow = findResumeRow(sheet, baseCol);
    int maxRows = sheet.getLastRowNum();

    try (PrintWriter logWriter = new PrintWriter(new FileWriter(LOG_FILE, true))) {
        for (int i = startRow; i <= maxRows; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String address = getCellValue(row.getCell(1)); // Single_Line_Address
            String postcode = getCellValue(row.getCell(2)); // Postcode
            String fullAddress = address + ", " + postcode + ", United Kingdom";

            String lat = getCellValue(row.getCell(5)); // Latitude
            String lon = getCellValue(row.getCell(4)); // Longitude

            BuildingInfo info = (!lat.isEmpty() && !lon.isEmpty())
                    ? getBuildingInfoFromOS(lat, lon, logWriter)
                    : new BuildingInfo("Unknown");

            if ("Unknown".equals(info.type)) {
                info.type = getBuildingTypeFromAddress(fullAddress, logWriter);
            }

            // Write values to row
            row.createCell(baseCol).setCellValue(info.type);
            row.createCell(baseCol + 1).setCellValue(info.buildingUse);
            row.createCell(baseCol + 2).setCellValue(info.connectivity);
            row.createCell(baseCol + 3).setCellValue(info.isMain);
            row.createCell(baseCol + 4).setCellValue(info.area);

            // Save progress after each row
            try (FileOutputStream fos = new FileOutputStream("Teleaurora_BuildingTypes4027.xlsx")) {
                workbook.write(fos);
                fos.flush();
            }

            // Sleep depending on API
            if ("Unknown".equals(info.type)) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(300);
            }
        }
    }

    workbook.close();
    System.out.println("✅ File updated with building types (resumable progress).");
}

    static String getCellValue(Cell cell) {
        return (cell == null) ? "" : cell.toString().trim();
    }

    static void logAndPrint(PrintWriter logWriter, String message) {
        if (logWriter != null) {
            logWriter.println(message);
            logWriter.flush();
        }
        System.out.println(message);
    }

    // --- Data holder ---
    static class BuildingInfo {
        String type = "Unknown";
        String buildingUse = "";
        String connectivity = "";
        String isMain = "";
        String area = "";

        BuildingInfo(String type) {
            this.type = type;
        }
    }

    // --- OS NGD API ---
    static BuildingInfo getBuildingInfoFromOS(String latStr, String lonStr, PrintWriter logWriter) {
        BuildingInfo info = new BuildingInfo("Unknown");
        try {
            double lat = Double.parseDouble(latStr);
            double lon = Double.parseDouble(lonStr);

            double delta = 0.0008;
            double minLat = lat - delta;
            double maxLat = lat + delta;
            double minLon = lon - delta;
            double maxLon = lon + delta;

            String urlStr = String.format(
                    "https://api.os.uk/features/ngd/ofa/v1/collections/bld-fts-building-1/items?" +
                            "bbox=%f,%f,%f,%f&key=%s",
                    minLon, minLat, maxLon, maxLat, OS_API_KEY
            );

            logAndPrint(logWriter, "Requesting OS API: " + urlStr);

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestProperty("User-Agent", "JavaApp");
            conn.setRequestMethod("GET");

            int status = conn.getResponseCode();
            if (status != 200) {
                logAndPrint(logWriter, "Failed OS API request (" + status + ")");
                return info;
            }

            String json = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject response = new JSONObject(json);
            JSONArray features = response.optJSONArray("features");
            if (features != null && features.length() > 0) {
                double bestDist = Double.MAX_VALUE;
                JSONObject bestProps = null;

                for (int i = 0; i < features.length(); i++) {
                    JSONObject feat = features.getJSONObject(i);
                    JSONObject props = feat.optJSONObject("properties");
                    JSONObject geom = feat.optJSONObject("geometry");

                    if (props != null && geom != null && "Polygon".equals(geom.optString("type"))) {
                        JSONArray coords = geom.optJSONArray("coordinates");
                        if (coords != null && coords.length() > 0) {
                            JSONArray ring = coords.getJSONArray(0);
                            if (ring.length() > 0) {
                                JSONArray firstPoint = ring.getJSONArray(0);
                                double bLon = firstPoint.getDouble(0);
                                double bLat = firstPoint.getDouble(1);
                                double dist = Math.pow(lat - bLat, 2) + Math.pow(lon - bLon, 2);
                                if (dist < bestDist) {
                                    bestDist = dist;
                                    bestProps = props;
                                }
                            }
                        }
                    }
                }

                if (bestProps != null) {
                    fillBuildingInfo(bestProps, info);
                }
            }

        } catch (Exception e) {
            logAndPrint(logWriter, "OS API error (" + latStr + "," + lonStr + "): " + e.getMessage());
        }
        return info;
    }

    static void fillBuildingInfo(JSONObject props, BuildingInfo info) {
        info.buildingUse = props.optString("buildinguse", "");
        info.connectivity = props.optString("connectivity", "");
        info.isMain = props.has("ismainbuilding") ? String.valueOf(props.optBoolean("ismainbuilding")) : "";
        info.area = props.has("geometry_area_m2") ? String.valueOf(props.optDouble("geometry_area_m2")) : "";

        String typeCandidate = info.buildingUse;
        if (typeCandidate.isEmpty() || "Unknown".equalsIgnoreCase(typeCandidate)) {
            typeCandidate = props.optString("oslandusetiera", "Unknown");
        }
        info.type = typeCandidate;
    }

    // --- Nominatim fallback ---
    static String getBuildingTypeFromAddress(String address, PrintWriter logWriter) {
        try {
            String urlStr = String.format(
                    "https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1",
                    URLEncoder.encode(address, "UTF-8")
            );
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestProperty("User-Agent", "JavaApp");
            conn.setRequestMethod("GET");

            String json = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONArray results = new JSONArray(json);
            if (results.length() == 0) return "Unknown";

            JSONObject result = results.getJSONObject(0);
            if (result.has("type")) return result.getString("type");
            if (result.has("class")) return result.getString("class");

            JSONObject extras = result.optJSONObject("extratags");
            if (extras != null && extras.has("building")) return extras.getString("building");

        } catch (Exception e) {
            logAndPrint(logWriter, "Nominatim error for address: " + address + " → " + e.getMessage());
        }
        return "Unknown";
    }
}
cd 