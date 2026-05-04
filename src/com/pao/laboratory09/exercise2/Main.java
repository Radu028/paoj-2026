package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    private static final int STATUS_PENDING = 0;
    private static final int STATUS_PROCESSED = 1;
    private static final int STATUS_REJECTED = 2;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        File outFile = new File(OUTPUT_FILE);
        if (outFile.getParentFile() != null) {
            outFile.getParentFile().mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                String[] parts = scanner.nextLine().trim().split("\\s+");
                int id = Integer.parseInt(parts[0]);
                double suma = Double.parseDouble(parts[1]);
                String data = parts[2];
                TipTranzactie tip = TipTranzactie.valueOf(parts[3]);

                byte[] record = new byte[RECORD_SIZE];

                byte[] idBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array();
                System.arraycopy(idBytes, 0, record, 0, 4);

                byte[] sumaBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array();
                System.arraycopy(sumaBytes, 0, record, 4, 8);

                byte[] dataBytes = data.getBytes("US-ASCII");
                int copyLen = Math.min(dataBytes.length, 10);
                System.arraycopy(dataBytes, 0, record, 12, copyLen);
                for (int k = 12 + copyLen; k < 22; k++) {
                    record[k] = ' ';
                }

                record[22] = (byte) (tip == TipTranzactie.CREDIT ? 0 : 1);
                record[23] = (byte) STATUS_PENDING;

                dos.write(record);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNextLine()) {
                String linie = scanner.nextLine().trim();
                if (linie.isEmpty()) continue;

                if (linie.startsWith("READ ")) {
                    int idx = Integer.parseInt(linie.substring("READ ".length()).trim());
                    System.out.println(formatRecord(raf, idx));
                } else if (linie.startsWith("UPDATE ")) {
                    String[] parts = linie.split("\\s+");
                    int idx = Integer.parseInt(parts[1]);
                    String statusName = parts[2];
                    int statusByte = statusFromName(statusName);
                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte);
                    System.out.println("Updated [" + idx + "]: " + statusName);
                } else if (linie.equals("PRINT_ALL")) {
                    long total = raf.length() / RECORD_SIZE;
                    for (int i = 0; i < total; i++) {
                        System.out.println(formatRecord(raf, i));
                    }
                }
            }
        }
    }

    private static String formatRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);
        byte[] buf = new byte[RECORD_SIZE];
        raf.readFully(buf);
        ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        int id = bb.getInt(0);
        double suma = bb.getDouble(4);
        String data = new String(buf, 12, 10, java.nio.charset.StandardCharsets.US_ASCII).trim();
        int tipByte = buf[22] & 0xFF;
        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        int statusByte = buf[23] & 0xFF;
        String status = statusName(statusByte);
        return String.format("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }

    private static int statusFromName(String name) {
        switch (name) {
            case "PENDING": return STATUS_PENDING;
            case "PROCESSED": return STATUS_PROCESSED;
            case "REJECTED": return STATUS_REJECTED;
            default: throw new IllegalArgumentException("Unknown status: " + name);
        }
    }

    private static String statusName(int b) {
        switch (b) {
            case STATUS_PENDING: return "PENDING";
            case STATUS_PROCESSED: return "PROCESSED";
            case STATUS_REJECTED: return "REJECTED";
            default: return "UNKNOWN";
        }
    }
}
