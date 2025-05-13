// package com.example.Skytec.Controllers;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;


// import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
// import com.google.api.client.http.javanet.NetHttpTransport;
// import com.google.api.client.json.jackson2.JacksonFactory;
// import com.google.api.services.drive.Drive;
// import com.google.api.services.drive.DriveScopes;
// import com.google.api.services.sheets.v4.Sheets;
// import com.google.api.services.sheets.v4.SheetsScopes;
// import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
// import com.google.api.services.sheets.v4.model.GridProperties;
// import com.google.api.services.sheets.v4.model.Request;
// import com.google.api.services.sheets.v4.model.SheetProperties;
// import com.google.api.services.sheets.v4.model.Spreadsheet;
// import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;

// import lombok.extern.slf4j.Slf4j;

// import org.springframework.http.*;

// import java.io.*;
// import java.util.Collections;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// @RestController
// @RequestMapping("/pdf")
// @Slf4j
// public class PdfController {

//     @GetMapping("/generate")
//     public ResponseEntity<byte[]> generatePdf(@RequestParam String folderName,
//                                               @RequestParam("SheetUrl") String sheetUrl) {
//         log.info("Received request to generate PDF for folder: {}, URL: {}", folderName, sheetUrl);
//         String fileId = null;

//         try {
//             fileId = extractGoogleFileId(sheetUrl);

//             // Step 1: Hide gridlines
//             updateGridlines(fileId, true);

//             // Step 2: Export to PDF
//             ByteArrayOutputStream pdfOutputStream = exportGoogleSheetToPdf(fileId);
//             log.debug("Exported Google Sheet to PDF");

//             // Step 3: Send PDF
//             HttpHeaders headers = new HttpHeaders();
//             headers.setContentType(MediaType.APPLICATION_PDF);
//             headers.setContentDisposition(ContentDisposition.attachment()
//                     .filename(folderName + "_Sheet.pdf")
//                     .build());

//             return new ResponseEntity<>(pdfOutputStream.toByteArray(), headers, HttpStatus.OK);

//         } catch (Exception e) {
//             log.error("Error generating PDF: {}", e.getMessage(), e);
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(("Error: " + e.getMessage()).getBytes());

//         } finally {
//             // Step 4: Re-enable gridlines
//             if (fileId != null) {
//                 try {
//                     updateGridlines(fileId, false);
//                     log.info("Re-enabled gridlines for Google Sheet: {}", fileId);
//                 } catch (Exception ex) {
//                     log.error("Failed to re-enable gridlines: {}", ex.getMessage(), ex);
//                 }
//             }
//         }
//     }

//     private String extractGoogleFileId(String url) {
//         Pattern pattern = Pattern.compile("/d/([a-zA-Z0-9-_]+)");
//         Matcher matcher = pattern.matcher(url);
//         if (matcher.find()) {
//             return matcher.group(1);
//         }
//         throw new IllegalArgumentException("Invalid Google Drive or Sheet URL");
//     }

//     private ByteArrayOutputStream exportGoogleSheetToPdf(String fileId) throws Exception {
//         InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("credentials1.json");
//         if (credentialsStream == null) {
//             throw new RuntimeException("Service account file not found in classpath");
//         }

//         GoogleCredential credential = GoogleCredential
//                 .fromStream(credentialsStream)
//                 .createScoped(Collections.singleton(DriveScopes.DRIVE));

//         Drive driveService = new Drive.Builder(
//                 new NetHttpTransport(),
//                 JacksonFactory.getDefaultInstance(),
//                 credential
//         ).setApplicationName("Sheet PDF Exporter").build();

//         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//         driveService.files()
//                 .export(fileId, "application/pdf")
//                 .executeMediaAndDownloadTo(outputStream);

//         return outputStream;
//     }

//     private void updateGridlines(String fileId, boolean hide) throws Exception {
//         InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("credentials1.json");
//         if (credentialsStream == null) {
//             throw new RuntimeException("Service account file not found in classpath");
//         }

//         GoogleCredential credential = GoogleCredential
//                 .fromStream(credentialsStream)
//                 .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

//         Sheets sheetsService = new Sheets.Builder(
//                 new NetHttpTransport(),
//                 JacksonFactory.getDefaultInstance(),
//                 credential
//         ).setApplicationName("Sheet PDF Exporter").build();

//         Spreadsheet spreadsheet = sheetsService.spreadsheets().get(fileId).execute();
//         Integer sheetId = spreadsheet.getSheets().get(0).getProperties().getSheetId();

//         Request request = new Request().setUpdateSheetProperties(
//                 new UpdateSheetPropertiesRequest()
//                         .setProperties(new SheetProperties()
//                                 .setSheetId(sheetId)
//                                 .setGridProperties(new GridProperties().setHideGridlines(hide)))
//                         .setFields("gridProperties.hideGridlines")
//         );

//         BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
//                 .setRequests(Collections.singletonList(request));

//         sheetsService.spreadsheets().batchUpdate(fileId, batchRequest).execute();
//     }
// }



package com.example.Skytec.Controllers;

import com.example.Skytec.Service.PdfExportService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
@Slf4j
public class PdfController {

    @Autowired
    private PdfExportService pdfExportService;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String folderName,
                                              @RequestParam("SheetUrl") String sheetUrl) {
        log.info("Received request to generate PDF for folder: {}, URL: {}", folderName, sheetUrl);

        try {
            byte[] pdfBytes = pdfExportService.generatePdfFromSheet(sheetUrl, folderName);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.attachment()
                    .filename(folderName + "_Sheet.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
}
