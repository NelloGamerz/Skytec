package com.example.Skytec.Service;

import com.google.api.services.sheets.v4.model.GridRange;

public class A1NotationConverter {
    public static GridRange convert(String range, int sheetId) {
        String[] parts = range.split(":");
        CellRef start = parseCellRef(parts[0]);
        CellRef end = parseCellRef(parts[1]);

        return new GridRange()
                .setSheetId(sheetId)
                .setStartRowIndex(start.row - 1)
                .setEndRowIndex(end.row)
                .setStartColumnIndex(start.col - 1)
                .setEndColumnIndex(end.col);
    }

    private static CellRef parseCellRef(String ref) {
        String letters = ref.replaceAll("[^A-Z]", "");
        String numbers = ref.replaceAll("[^0-9]", "");
        int col = 0;
        for (char c : letters.toCharArray()) {
            col = col * 26 + (c - 'A' + 1);
        }
        return new CellRef(Integer.parseInt(numbers), col);
    }

    private static class CellRef {
        int row;
        int col;

        CellRef(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}

