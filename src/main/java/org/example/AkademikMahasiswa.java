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

    // 2. VARIABEL GLOBAL
    ArrayList<Mahasiswa> dataMahasiswa = new ArrayList<>();
    String namaFile = "data_mahasiswa_kelas.txt";

    // PALET WARNA
    Color colPrimary = new Color(164, 0, 0);
    Color colSecondary = new Color(211, 47, 47);
    Color colAccent = new Color(255, 193, 7);
    Color colEdit = new Color(255, 140, 0);
    Color colTableAlt = new Color(255, 240, 240);

    // FONT
    Font fontTitle = new Font("Segoe UI", Font.BOLD, 22);
    Font fontBold = new Font("Segoe UI", Font.BOLD, 14);
    Font fontRegular = new Font("Segoe UI", Font.PLAIN, 14);

    // KOMPONEN GUI
    JTabbedPane tabMenu;
    DefaultTableModel modelTabel;
    JTable tabelData;
    JLabel lblTotal, lblInfoLain;
    JTextField txtNim, txtNama, txtJurusan, txtSemester, txtKelas;
    JTextArea areaLaporan;

    // 3. CONSTRUCTOR
    public AkademikMahasiswa() {
        super("Sistem Informasi Akademik 2025 - UMM");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bacaFile();

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // HEADER
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        headerPanel.setOpaque(false);

        JLabel lblIcon = new JLabel("🎓");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setForeground(Color.WHITE);

        JLabel lblTitle = new JLabel("<html>SISTEM AKADEMIK MAHASISWA<br><font size=3>Data Kelas & Jurusan</font></html>");
        lblTitle.setFont(fontTitle);
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblIcon);
        headerPanel.add(lblTitle);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // TAB MENU
        tabMenu = new JTabbedPane();
        tabMenu.setFont(fontBold);
        tabMenu.setBackground(Color.WHITE);

        tabMenu.addTab("🏠 Dashboard", createDashboardPanel());
        tabMenu.addTab("✍️ Input Data", createInputPanel());
        tabMenu.addTab("📂 Data Mahasiswa", createTabelPanel());
        tabMenu.addTab("📊 Laporan", createLaporanPanel());

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(0, 20, 0, 20));
        contentWrapper.add(tabMenu);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        // FOOTER
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        JLabel lblFooter = new JLabel("Universitas Muhammadiyah Malang - Informatika 2025");
        lblFooter.setFont(fontBold);
        lblFooter.setForeground(Color.DARK_GRAY);
        footerPanel.add(lblFooter);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        refreshAll();
        setVisible(true);
    }

    // 4. PANEL CREATION

    private JPanel createDashboardPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        lblTotal = createInfoCard("👥 TOTAL MAHASISWA", "0", colPrimary);
        lblInfoLain = createInfoCard("📅 TAHUN AJARAN", "2025/2026", colAccent);
        lblInfoLain.setForeground(Color.DARK_GRAY);

        p.add(lblTotal, gbc);
        p.add(lblInfoLain, gbc);
        return p;
    }

    private JPanel createInputPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNim = new JTextField(20);
        styleField(txtNim);
        txtNama = new JTextField(20);
        styleField(txtNama);
        txtJurusan = new JTextField(20);
        styleField(txtJurusan);
        txtSemester = new JTextField(20);
        styleField(txtSemester);
        txtKelas = new JTextField(20);
        styleField(txtKelas);

        addFormRow(p, gbc, 0, "🏷️ Nomor Induk (NIM)", txtNim);
        addFormRow(p, gbc, 1, "👤 Nama Lengkap", txtNama);
        addFormRow(p, gbc, 2, "🎓 Jurusan", txtJurusan);
        addFormRow(p, gbc, 3, "🏫 Semester", txtSemester);
        addFormRow(p, gbc, 4, "🏛️ Kelas (Misal: A/B)", txtKelas);

        JButton btnSimpan = new JButton("💾 SIMPAN DATA");
        styleButton(btnSimpan, colPrimary);

        // --- PERUBAHAN DI SINI ---
        // Menambahkan '(ActionEvent e)' secara eksplisit agar library java.awt.event terpakai
        btnSimpan.addActionListener((ActionEvent e) -> simpanData());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        p.add(btnSimpan, gbc);
        return p;
    }

    private JPanel createTabelPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] cols = {"NIM", "NAMA MAHASISWA", "JURUSAN", "SEMESTER", "KELAS"};
        modelTabel = new DefaultTableModel(cols, 0);

        tabelData = new JTable(modelTabel) {
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? Color.WHITE : colTableAlt);
                return c;
            }
        };
        tabelData.setRowHeight(35);
        tabelData.setFont(fontRegular);
        tabelData.setShowVerticalLines(false);

        tabelData.getTableHeader().setDefaultRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel l = (JLabel) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            l.setBackground(colPrimary);
            l.setForeground(Color.WHITE);
            l.setFont(fontBold);
            l.setHorizontalAlignment(JLabel.CENTER);
            l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
            return l;
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);

        JButton btnRefresh = new JButton("🔄 Refresh");
        styleButton(btnRefresh, colSecondary);
        JButton btnEdit = new JButton("✏️ Edit Data");
        styleButton(btnEdit, colEdit);
        JButton btnHapus = new JButton("🗑️ Hapus");
        styleButton(btnHapus, new Color(100, 0, 0));

        // --- PERUBAHAN DI SINI JUGA ---
        // Menggunakan '(ActionEvent e)' supaya import tidak abu-abu
        btnRefresh.addActionListener((ActionEvent e) -> refreshAll());
        btnEdit.addActionListener((ActionEvent e) -> editData());
        btnHapus.addActionListener((ActionEvent e) -> hapusData());

        btnPanel.add(btnRefresh);
        btnPanel.add(btnEdit);
        btnPanel.add(btnHapus);

        p.add(new JScrollPane(tabelData), BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);
        return p;
    }

}