package com.example.Skytec.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PdfExportService {

    public byte[] generatePdfFromSheet(String sheetUrl, String folderName) throws Exception {
        String fileId = extractGoogleFileId(sheetUrl);

        try {
            updateGridlines(fileId, true);
            ByteArrayOutputStream pdfOutputStream = exportGoogleSheetToPdf(fileId);
            return pdfOutputStream.toByteArray();
        } finally {
            try {
                updateGridlines(fileId, false);
                log.info("Re-enabled gridlines for Google Sheet: {}", fileId);
            } catch (Exception ex) {
                log.error("Failed to re-enable gridlines: {}", ex.getMessage(), ex);
            }
        }
    }

    private String extractGoogleFileId(String url) {
        Pattern pattern = Pattern.compile("/d/([a-zA-Z0-9-_]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid Google Drive or Sheet URL");
    }

    private ByteArrayOutputStream exportGoogleSheetToPdf(String fileId) throws Exception {
        InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("credentials1.json");
        if (credentialsStream == null) {
            throw new RuntimeException("Service account file not found in classpath");
        }

        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        Drive driveService = new Drive.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Sheet PDF Exporter").build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files().export(fileId, "application/pdf")
                .executeMediaAndDownloadTo(outputStream);

        return outputStream;
    }

    private void updateGridlines(String fileId, boolean hide) throws Exception {
        InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("credentials1.json");
        if (credentialsStream == null) {
            throw new RuntimeException("Service account file not found in classpath");
        }

        GoogleCredential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        Sheets sheetsService = new Sheets.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("Sheet PDF Exporter").build();

        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(fileId).execute();
        Integer sheetId = spreadsheet.getSheets().get(0).getProperties().getSheetId();

        Request request = new Request().setUpdateSheetProperties(
                new UpdateSheetPropertiesRequest()
                        .setProperties(new SheetProperties()
                                .setSheetId(sheetId)
                                .setGridProperties(new GridProperties().setHideGridlines(hide)))
                        .setFields("gridProperties.hideGridlines")
        );

        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(request));

        sheetsService.spreadsheets().batchUpdate(fileId, batchRequest).execute();
    }
}
