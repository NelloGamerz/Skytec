package com.example.Skytec.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
// import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleService {

    private static final String APPLICATION_NAME = "Template Manager";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // private Credential getCredentials() throws IOException, GeneralSecurityException {
    //     InputStream in = new ClassPathResource("credentials.json").getInputStream();
    //     ClassPathResource resource = new ClassPathResource("credentials.json");
    //     System.out.println("Exists: " + resource.exists());
    //     System.out.println("Filename: " + resource.getFilename());

    //     GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    //     GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
    //             GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets,
    //             List.of(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS))
    //             .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
    //             .setAccessType("offline")
    //             .build();

    //     return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    // }

    private Credential getCredentials() throws IOException, GeneralSecurityException {
        // Load the credentials.json file
        InputStream in = new ClassPathResource("credentials.json").getInputStream();
        ClassPathResource resource = new ClassPathResource("credentials.json");
        System.out.println("Exists: " + resource.exists()); // Check if file exists
        System.out.println("Filename: " + resource.getFilename()); // Check the file name
    
        // Load the client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    
        // If clientSecrets is null or invalid, throw an exception
        if (clientSecrets == null || clientSecrets.getDetails() == null) {
            throw new IOException("Invalid credentials file.");
        }
    
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets,
                List.of(DriveScopes.DRIVE, SheetsScopes.SPREADSHEETS))
                .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline")
                .build();
    
        // Authorize and return the credential
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
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
}
