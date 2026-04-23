import { GoogleSpreadsheet } from 'google-spreadsheet';
import { JWT } from 'google-auth-library';
import process from 'process';

class SheetsService {
    constructor() {
        this.doc = null;
        this.sheetId = process.env.GOOGLE_SHEET_ID;
        this.clientEmail = process.env.GOOGLE_SERVICE_ACCOUNT_EMAIL;
        this.privateKey = process.env.GOOGLE_PRIVATE_KEY?.replace(/\\n/g, '\n');
    }

    async init() {
        if (!this.sheetId || !this.clientEmail || !this.privateKey) {
            console.error("Missing Google Sheets credentials in .env");
            return; // Or throw
        }

        const formattedPrivateKey = this.privateKey;

        const serviceAccountAuth = new JWT({
            email: this.clientEmail,
            key: formattedPrivateKey,
            scopes: [
                'https://www.googleapis.com/auth/spreadsheets',
            ],
        });

        this.doc = new GoogleSpreadsheet(this.sheetId, serviceAccountAuth);

        await this.doc.loadInfo();
        console.log(`Loaded doc: ${this.doc.title}`);
    }

    async updateCell(unit, part, value) {
        if (!this.doc) await this.init();

        const sheet = this.doc.sheetsByIndex[0];
        // Mapping based on "Rows = Parts, Columns = Units"
        // Row 0: Project Info?
        // Row 1: Headers (Parts, Unit 1, Unit 2...)
        // Row 2: RAM
        // ...

        const PART_ROW_MAP = {
            "RAM": 2,
            "Motherboard": 3,
            "SSD": 4,
            "Heatsink": 5,
            "WIFI": 6,
            "Unit": 7,
            "Battery": 8,
            "Speaker": 9
        };

        const rowIndex = PART_ROW_MAP[part];
        const colIndex = parseInt(unit); // Unit 1 = Col 1 (B), Unit 0 is Label?

        if (rowIndex === undefined || isNaN(colIndex)) {
            console.error(`Invalid Unit/Part mapping: ${unit}/${part}`);
            return;
        }

        // Load specific cell
        // We load a small range around it or just the cell?
        // loadCells accepts 'A1:A1' or generic grid range.
        try {
            await sheet.loadCells({
                startRowIndex: rowIndex, endRowIndex: rowIndex + 1,
                startColumnIndex: colIndex, endColumnIndex: colIndex + 1
            });
            const cell = sheet.getCell(rowIndex, colIndex);
            cell.value = value;
            await sheet.saveUpdatedCells();
            console.log(`Updated Sheet: [${rowIndex}, ${colIndex}] = ${value}`);
        } catch (e) {
            console.error("Error updating sheet cell:", e);
        }
    }

    // Changing strategy: We need a specialized loader that maps the sheet to memory first.
    async loadMatrix() {
        const sheet = this.doc.sheetsByIndex[0];
        // Load reasonable range. 
        // Max units ~200. Max parts ~8.
        // Columns: A (PartName) + 200 units = 201 cols.
        // Rows: 8 parts + 1 header = 9 rows.
        // Range: A1:HT9 (very wide)

        await sheet.loadHeaderRow(); // Assumes row 1 is simple? 
        // Actual format from CsvRepository: 
        // Block Header: [Project...], 
        // Then "Parts,Unit 1,..."
        // The user description says "Header: Project Name... Table: Columns Unit 1..."
        // If it's a fresh sheet, we just need the table.
        // We accept that the sheet contains just ONE table for now as per "Each Project = 1 Google Sheet".

    }
}

export default new SheetsService();
