package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    private static byte tipToByte(TipTranzactie tip) {
        return (byte) (tip == TipTranzactie.CREDIT ? 0 : 1);
    }

    private static String statusToString(byte s) {
        return switch (s) {
            case 0 -> "PENDING";
            case 1 -> "PROCESSED";
            case 2 -> "REJECTED";
            default -> "UNKNOWN";
        };
    }

    private static byte statusFromString(String s) {
        return switch (s) {
            case "PENDING" -> 0;
            case "PROCESSED" -> 1;
            case "REJECTED" -> 2;
            default -> 0;
        };
    }

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

        Scanner sc = new Scanner(System.in);

        // citeste N
        int N = Integer.parseInt(sc.nextLine().trim());

        // creeaza folderul output daca nu exista
        new File("output").mkdirs();

        // scriere initiala in fișier
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {

            for (int i = 0; i < N; i++) {
                String line = sc.nextLine().trim();
                while (line.isEmpty()) line = sc.nextLine().trim();

                String[] p = line.split("\\s+");

                int id = Integer.parseInt(p[0]);
                double suma = Double.parseDouble(p[1]);
                String data = p[2];
                TipTranzactie tip = TipTranzactie.valueOf(p[3]);

                // id (4 bytes LE)
                dos.write(ByteBuffer.allocate(4)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putInt(id)
                        .array());

                // suma (8 bytes LE)
                dos.write(ByteBuffer.allocate(8)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putDouble(suma)
                        .array());

                // data (10 bytes ASCII padded)
                byte[] dataBytes = data.getBytes();
                dos.write(dataBytes);
                for (int k = dataBytes.length; k < 10; k++) dos.write(' ');

                // tip (1 byte)
                dos.write(tipToByte(tip));

                // status inițial = 0 (PENDING)
                dos.write(0);

                // padding 8 bytes
                for (int k = 0; k < 8; k++) dos.write(0);
            }
        }

        // proceseaza comenzile
        while (sc.hasNextLine()) {
            String cmd = sc.nextLine().trim();
            if (cmd.isEmpty()) continue;

            String[] parts = cmd.split("\\s+");

            if (parts[0].equals("READ")) {
                int idx = Integer.parseInt(parts[1]);
                printRecord(idx);
            }

            else if (parts[0].equals("UPDATE")) {
                int idx = Integer.parseInt(parts[1]);
                String statusStr = parts[2];
                byte status = statusFromString(statusStr);

                try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
                    raf.seek(idx * RECORD_SIZE + 23);
                    raf.write(status);
                }

                System.out.println("Updated [" + idx + "]: " + statusStr);
            }

            else if (parts[0].equals("PRINT_ALL")) {
                for (int i = 0; i < N; i++) {
                    printRecord(i);
                }
            }
        }
    }

    private static void printRecord(int idx) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "r")) {
            raf.seek(idx * RECORD_SIZE);

            byte[] buffer = new byte[RECORD_SIZE];
            raf.readFully(buffer);

            ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);

            int id = bb.getInt(0);
            double suma = bb.getDouble(4);

            String data = new String(buffer, 12, 10).trim();

            byte tipByte = buffer[22];
            TipTranzactie tip = (tipByte == 0 ? TipTranzactie.CREDIT : TipTranzactie.DEBIT);

            byte statusByte = buffer[23];
            String status = statusToString(statusByte);

            System.out.printf(
                    Locale.US,
                    "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                    idx, id, data, tip, suma, status
            );
        }
    }
}
