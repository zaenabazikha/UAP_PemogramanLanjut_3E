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

        txtNim = new JTextField(20); styleField(txtNim);
        txtNama = new JTextField(20); styleField(txtNama);
        txtJurusan = new JTextField(20); styleField(txtJurusan);
        txtSemester = new JTextField(20); styleField(txtSemester);
        txtKelas = new JTextField(20); styleField(txtKelas);

        addFormRow(p, gbc, 0, "🏷️ Nomor Induk (NIM)", txtNim);
        addFormRow(p, gbc, 1, "👤 Nama Lengkap", txtNama);
        addFormRow(p, gbc, 2, "🎓 Jurusan", txtJurusan);
        addFormRow(p, gbc, 3, "🏫 Semester", txtSemester);
        addFormRow(p, gbc, 4, "🏛️ Kelas (Misal: A/B)", txtKelas);

        JButton btnSimpan = new JButton("💾 SIMPAN DATA");
        styleButton(btnSimpan, colPrimary);

        btnSimpan.addActionListener((ActionEvent e) -> simpanData());

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
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

    private JPanel createLaporanPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(15, 15, 15, 15));
        p.setBackground(Color.WHITE);

        areaLaporan = new JTextArea();
        areaLaporan.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaLaporan.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        areaLaporan.setEditable(false);

        JButton btnCetak = new JButton("🖨️ CETAK LAPORAN");
        styleButton(btnCetak, new Color(39, 174, 96));

        // --- PERUBAHAN DI SINI ---
        btnCetak.addActionListener((ActionEvent e) -> generateLaporan());

        p.add(new JScrollPane(areaLaporan), BorderLayout.CENTER);
        p.add(btnCetak, BorderLayout.SOUTH);
        return p;
    }

    // 5. HELPER METHODS
    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color atas = new Color(164, 0, 0);
            Color bawah = new Color(250, 250, 250);
            g2d.setPaint(new GradientPaint(0, 0, atas, 0, getHeight(), bawah));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JLabel createInfoCard(String title, String val, Color color) {
        JLabel l = new JLabel("<html><center><p style='font-size:12px'>"+title+"</p><h1 style='font-size:36px'>"+val+"</h1></center></html>", JLabel.CENTER);
        l.setOpaque(true);
        l.setBackground(color);
        l.setForeground(Color.WHITE);
        l.setPreferredSize(new Dimension(250, 120));
        return l;
    }

    private void styleField(JTextField txt) {
        txt.setFont(fontRegular);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(fontBold);
        btn.setFocusPainted(false);
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String lbl, JTextField txt) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel l = new JLabel(lbl);
        l.setFont(fontBold);
        p.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(txt, gbc);
    }

    // 6. LOGIKA UTAMA (CRUD)
    void simpanData() {
        if(txtNim.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM tidak boleh kosong!");
            return;
        }
        dataMahasiswa.add(new Mahasiswa(
                txtNim.getText(), txtNama.getText(), txtJurusan.getText(),
                txtSemester.getText(), txtKelas.getText()
        ));
        simpanFile();
        refreshAll();
        JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!");
        txtNim.setText(""); txtNama.setText(""); txtKelas.setText("");
    }

    void hapusData() {
        int row = tabelData.getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel dulu!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            dataMahasiswa.remove(row);
            simpanFile();
            refreshAll();
        }
    }

    void editData() {
        int row = tabelData.getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang mau diedit dulu!");
            return;
        }
        Mahasiswa m = dataMahasiswa.get(row);
        JTextField tNama = new JTextField(m.nama);
        JTextField tJurusan = new JTextField(m.jurusan);
        JTextField tSem = new JTextField(m.semester);
        JTextField tKelas = new JTextField(m.kelas);

        Object[] message = {
                "Edit Nama:", tNama, "Edit Jurusan:", tJurusan,
                "Edit Semester:", tSem, "Edit Kelas:", tKelas
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Edit Data Mahasiswa (NIM: " + m.nim + ")", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            m.nama = tNama.getText();
            m.jurusan = tJurusan.getText();
            m.semester = tSem.getText();
            m.kelas = tKelas.getText();
            simpanFile();
            refreshAll();
            JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate!");
        }
    }

    void refreshAll() {
        modelTabel.setRowCount(0);
        for(Mahasiswa m : dataMahasiswa) {
            modelTabel.addRow(new Object[]{m.nim, m.nama, m.jurusan, m.semester, m.kelas});
        }
        lblTotal.setText("<html><center><p style='font-size:12px'>👥 TOTAL MAHASISWA</p><h1 style='font-size:36px'>"+dataMahasiswa.size()+"</h1></center></html>");
    }

    void generateLaporan() {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("      LAPORAN DATA MAHASISWA 2025        \n");
        sb.append("=========================================\n\n");
        int no = 1;
        for(Mahasiswa m : dataMahasiswa) {
            sb.append(no++).append(". NIM   : ").append(m.nim).append("\n");
            sb.append("   Nama  : ").append(m.nama).append("\n");
            sb.append("   Jur   : ").append(m.jurusan).append("\n");
            sb.append("   Kelas : ").append(m.kelas).append(" (Smt ").append(m.semester).append(")\n");
            sb.append("-----------------------------------------\n");
        }
        areaLaporan.setText(sb.toString());
    }

    void simpanFile() {
        try(PrintWriter pw = new PrintWriter(new FileWriter(namaFile))) {
            for(Mahasiswa m : dataMahasiswa) pw.println(m.toCSV());
        } catch(Exception e) { e.printStackTrace(); }
    }

    void bacaFile() {
        File f = new File(namaFile);
        if(!f.exists()) return;
        try(BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            while((l=br.readLine())!=null) {
                String[] d = l.split(",");
                if(d.length==5) dataMahasiswa.add(new Mahasiswa(d[0], d[1], d[2], d[3], d[4]));
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch(Exception e){}
        SwingUtilities.invokeLater(() -> new AkademikMahasiswa());
    }
}