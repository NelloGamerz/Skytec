package com.example.Skytec.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.script.model.Operation;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleService {

        private static final String APPLICATION_NAME = "Template Manager";
        private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        private static final String SERVICE_ACCOUNT_FILE_PATH = "credentials1.json";
        @Value("${google.script.webapp.url}")
        private String webApp;


        private Credential getCredentials() throws IOException, GeneralSecurityException {
                GoogleCredential credential = GoogleCredential.fromStream(
                                new ClassPathResource(SERVICE_ACCOUNT_FILE_PATH).getInputStream())
                                .createScoped(List.of(
                                                DriveScopes.DRIVE,
                                                SheetsScopes.SPREADSHEETS,
                                                "https://www.googleapis.com/auth/script.projects",
                                                "https://www.googleapis.com/auth/script.scriptapp",
                                                "https://www.googleapis.com/auth/script.external_request"));

                return credential;
        }

        public String createGoogleSheet(String templateName) throws Exception {
                Credential credential = getCredentials();

                Sheets sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                                credential)
                                .setApplicationName(APPLICATION_NAME)
                                .build();

                Spreadsheet spreadsheet = new Spreadsheet()
                                .setProperties(new SpreadsheetProperties().setTitle(templateName));

                Spreadsheet result = sheetsService.spreadsheets().create(spreadsheet).execute();

                // Add headers
                List<List<Object>> values = List.of(List.of("Column 1", "Column 2", "Column 3"));
                ValueRange body = new ValueRange().setValues(values);
                sheetsService.spreadsheets().values()
                                .update(result.getSpreadsheetId(), "A1", body)
                                .setValueInputOption("RAW")
                                .execute();

                return result.getSpreadsheetId();
        }

        public String getSheetUrl(String sheetId) {
                return "https://docs.google.com/spreadsheets/d/" + sheetId;
        }


        public String copySheetFromTemplate(String templateSheetId, String newTitle, String folderId,
                        Map<String, String> metadata) throws Exception {
                Credential credential = getCredentials();

                // Step 1: Initialize Drive service
                Drive driveService = new Drive.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, setHttpTimeout(credential))
                                .setApplicationName(APPLICATION_NAME)
                                .build();

                // Step 2: Copy the template sheet
                com.google.api.services.drive.model.File copiedFileMetadata = new com.google.api.services.drive.model.File()
                                .setName(newTitle)
                                .setParents(List.of(folderId));

                com.google.api.services.drive.model.File copiedFile = driveService.files()
                                .copy(templateSheetId, copiedFileMetadata)
                                .execute();

                String newSheetId = copiedFile.getId();
                System.out.println("✅ Sheet copied successfully: " + newSheetId);

                // Step 3: Call Google Apps Script Web App to install trigger
                try {
                        RestTemplate restTemplate = new RestTemplate();
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        Map<String, String> payload = Map.of("sheetId", newSheetId);

                        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

                        ResponseEntity<String> response = restTemplate.postForEntity(webApp, request, String.class);
                        System.out.println("📡 Apps Script response: " + response.getBody());
                } catch (Exception e) {
                        System.err.println("❌ Failed to call Apps Script: " + e.getMessage());
                }

                // Step 4: Initialize Sheets service
                Sheets sheetsService = new Sheets.Builder(
                                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, setHttpTimeout(credential))
                                .setApplicationName(APPLICATION_NAME)
                                .build();

                // Step 5: Fill metadata
                String fileLabel = metadata.get("fileLabel");
                String activityName = metadata.get("activityName");

                LocalDate today = LocalDate.now();
                String formattedDate = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int year = today.getYear();
                int month = today.getMonthValue();

                String suffix = activityName.replaceAll("\\D+", "");
                String invoiceNumber = (month >= 4)
                                ? String.format("SS/%d-%d/%s", year, year + 1, suffix)
                                : String.format("SS/%d-%d/%s", year - 1, year, suffix);

                List<ValueRange> values = List.of(
                                new ValueRange().setRange("I3:K3").setValues(List.of(List.of(fileLabel))),
                                new ValueRange().setRange("I11:K11").setValues(List.of(List.of(invoiceNumber))),
                                new ValueRange().setRange("I14:K14").setValues(List.of(List.of(formattedDate))));

                BatchUpdateValuesRequest valueRequest = new BatchUpdateValuesRequest()
                                .setValueInputOption("RAW")
                                .setData(values);

                sheetsService.spreadsheets().values()
                                .batchUpdate(newSheetId, valueRequest)
                                .execute();

                // Step 6: Format cells
                Spreadsheet spreadsheet = sheetsService.spreadsheets().get(newSheetId).execute();
                int sheetId = spreadsheet.getSheets().get(0).getProperties().getSheetId();

                List<String> ranges = List.of("I3:K3", "I12:K12", "I14:K14");
                List<Request> styleRequests = new ArrayList<>();

                for (String range : ranges) {
                        GridRange gridRange = A1NotationConverter.convert(range, sheetId);
                        CellFormat cellFormat = new CellFormat().setHorizontalAlignment("CENTER");

                        styleRequests.add(new Request().setRepeatCell(
                                        new RepeatCellRequest()
                                                        .setRange(gridRange)
                                                        .setCell(new CellData().setUserEnteredFormat(cellFormat))
                                                        .setFields("userEnteredFormat(horizontalAlignment)")));
                }

                BatchUpdateSpreadsheetRequest formatRequest = new BatchUpdateSpreadsheetRequest()
                                .setRequests(styleRequests);

                sheetsService.spreadsheets().batchUpdate(newSheetId, formatRequest).execute();

                System.out.println("✅ Metadata updated and formatting applied.");

                return newSheetId;
        }

        private static HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
                return request -> {
                        requestInitializer.initialize(request);
                        request.setConnectTimeout(3 * 60000); // 3 minutes
                        request.setReadTimeout(3 * 60000); // 3 minutes
                };
        }

}
