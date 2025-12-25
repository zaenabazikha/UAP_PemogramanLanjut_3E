package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class AkademikMahasiswa extends JFrame {

    // 1. DATA MODEL
    static class Mahasiswa {
        String nim, nama, jurusan, semester, kelas;

        public Mahasiswa(String nim, String nama, String jurusan, String semester, String kelas) {
            this.nim = nim;
            this.nama = nama;
            this.jurusan = jurusan;
            this.semester = semester;
            this.kelas = kelas;
        }

        public String toCSV() {
            return nim + "," + nama + "," + jurusan + "," + semester + "," + kelas;
        }
    }
