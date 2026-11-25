/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pbo.sewaalathiking.view;

import pbo.sewaalathiking.entity.Equipment;
import pbo.sewaalathiking.entity.User;
import pbo.sewaalathiking.repository.EquipmentRepository;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/**
 *
 * @author ASUS
 */
public class CustomerCatalogFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerCatalogFrame.class.getName());

    private List<Equipment> availableEquipmentList = new ArrayList<>();
    private List<Equipment> filteredEquipmentList = new ArrayList<>();
    private User currentUser;
    private List<Object[]> cartList;
    private final EquipmentRepository equipmentRepo = new EquipmentRepository();
    private boolean isUpdatingFilter = false;
    
    /**
     * Creates new form CustomerCatalogueFrame
     */
    public CustomerCatalogFrame() {
        initComponents();
        styleTableAndPreview();
        this.setLocationRelativeTo(null);
        setupTable();
        setupSearchAndFilter();
        loadKatalogAlat();
    }
    
    public CustomerCatalogFrame(User user) {
        this(user, new ArrayList<>());
    }
    
    public CustomerCatalogFrame(User user, List<Object[]> existingCart) {
        initComponents();
        styleTableAndPreview();
        this.currentUser = user;
        this.cartList = (existingCart != null) ? existingCart : new ArrayList<>();
        this.setLocationRelativeTo(null);

        setupTable();
        setupSearchAndFilter();
        loadKatalogAlat();
        updateCartButtonLabel();
    }

    private void styleTableAndPreview() {
        tabelAlat.setRowHeight(30);
        tabelAlat.setSelectionBackground(new java.awt.Color(216, 243, 220));
        tabelAlat.setSelectionForeground(new java.awt.Color(27, 67, 50));
        tabelAlat.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblGambarAlat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resetSelectionPreview();
    }

    private void resetSelectionPreview() {
        lblItemTerpilih.setText("Item Terpilih: -");
        lblGambarAlat.setIcon(null);
        lblGambarAlat.setText("Pilih alat untuk lihat gambar");
    }

    private void setupSearchAndFilter() {
        isUpdatingFilter = true;
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{"Semua Kategori", "Tenda", "Tas Gunung", "Alat Tidur", "Alat Masak", "Penerangan", "Lainnya"}
        ));
        isUpdatingFilter = false;

        searchField.setToolTipText("Ketik nama atau kategori alat...");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilter();
            }
        });

        jComboBox1.addActionListener(e -> {
            if (!isUpdatingFilter) {
                applyFilter();
            }
        });
    }

    private void syncCategoryComboBox() {
        isUpdatingFilter = true;
        Object currentSelection = jComboBox1.getSelectedItem();
        javax.swing.DefaultComboBoxModel<String> model = (javax.swing.DefaultComboBoxModel<String>) jComboBox1.getModel();

        for (Equipment eq : availableEquipmentList) {
            String cat = eq.getCategory();
            if (cat != null && !cat.trim().isEmpty()) {
                boolean exists = false;
                for (int i = 0; i < model.getSize(); i++) {
                    if (model.getElementAt(i).equalsIgnoreCase(cat.trim())) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    model.addElement(cat.trim());
                }
            }
        }

        if (currentSelection != null) {
            jComboBox1.setSelectedItem(currentSelection);
        }
        isUpdatingFilter = false;
    }
    
    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nama Alat", "Kategori", "Harga/Hari", "Stok Tersedia"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelAlat.setModel(model);
    }
    
    private void loadKatalogAlat() {
        availableEquipmentList = equipmentRepo.getAvailableEquipment();
        syncCategoryComboBox();
        applyFilter();
    }

    private void applyFilter() {
        DefaultTableModel model = (DefaultTableModel) tabelAlat.getModel();
        model.setRowCount(0);
        filteredEquipmentList.clear();

        String keyword = searchField.getText().trim().toLowerCase();
        String selectedCategory = (jComboBox1.getSelectedItem() != null)
                ? jComboBox1.getSelectedItem().toString()
                : "Semua Kategori";

        for (Equipment eq : availableEquipmentList) {
            boolean matchesCategory = "Semua Kategori".equalsIgnoreCase(selectedCategory)
                    || (eq.getCategory() != null && eq.getCategory().equalsIgnoreCase(selectedCategory));

            boolean matchesKeyword = keyword.isEmpty()
                    || (eq.getName() != null && eq.getName().toLowerCase().contains(keyword))
                    || (eq.getCategory() != null && eq.getCategory().toLowerCase().contains(keyword))
                    || String.valueOf(eq.getId()).contains(keyword);

            if (matchesCategory && matchesKeyword) {
                filteredEquipmentList.add(eq);
                model.addRow(new Object[]{
                    eq.getId(),
                    eq.getName(),
                    eq.getCategory(),
                    eq.getPricePerDay(),
                    eq.getAvailableStock()
                });
            }
        }

        resetSelectionPreview();
    }
    
    private void updateCartButtonLabel() {
        btnLihatKeranjang.setText("Lihat Keranjang (" + cartList.size() + ")");
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        panel = new javax.swing.JPanel();
        lblGambarAlat = new javax.swing.JLabel();
        btnLihatKeranjang = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        btnTambahKeranjang = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        fieldQty = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelAlat = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblItemTerpilih = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(45, 106, 79));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Katalog Sewa Alat Hiking");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cari Alat:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Filter Kategori:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(15, 15, 15))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)));

        lblGambarAlat.setText("jLabel5");

        btnLihatKeranjang.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLihatKeranjang.setText("Lihat Keranjang (0)");
        btnLihatKeranjang.addActionListener(this::btnLihatKeranjangActionPerformed);

        btnKembali.setBackground(new java.awt.Color(45, 106, 79));
        btnKembali.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnKembali.setForeground(new java.awt.Color(255, 255, 255));
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(this::btnKembaliActionPerformed);

        btnTambahKeranjang.setBackground(new java.awt.Color(45, 106, 79));
        btnTambahKeranjang.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnTambahKeranjang.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahKeranjang.setText("Tambah Keranjang");
        btnTambahKeranjang.addActionListener(this::btnTambahKeranjangActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Jumlah Sewa:");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblGambarAlat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnKembali))
                    .addGroup(panelLayout.createSequentialGroup()
                        .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fieldQty, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(btnLihatKeranjang)
                            .addComponent(btnTambahKeranjang))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblGambarAlat, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fieldQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnTambahKeranjang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLihatKeranjang)
                .addGap(66, 66, 66)
                .addComponent(btnKembali)
                .addGap(14, 14, 14))
        );

        jScrollPane2.addPropertyChangeListener(this::jScrollPane2PropertyChange);

        tabelAlat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nama Alat", "Kategori", "Harga/Hari", "Stok Tersedia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelAlat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelAlatMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelAlat);

        jPanel2.setBackground(new java.awt.Color(45, 106, 79));

        lblItemTerpilih.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblItemTerpilih.setForeground(new java.awt.Color(255, 255, 255));
        lblItemTerpilih.setText("Item Terpilih:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemTerpilih)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItemTerpilih)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKeranjangActionPerformed
        int selectedRow = tabelAlat.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih alat dari tabel katalog terlebih dahulu!");
            return;
        }

        try {
            int idAlat = Integer.parseInt(tabelAlat.getValueAt(selectedRow, 0).toString());
            String namaAlat = tabelAlat.getValueAt(selectedRow, 1).toString();
            double hargaPerHari = Double.parseDouble(tabelAlat.getValueAt(selectedRow, 3).toString());
            int stokTersedia = Integer.parseInt(tabelAlat.getValueAt(selectedRow, 4).toString());
            int qty = Integer.parseInt(fieldQty.getText().trim());

            if (qty <= 0 || qty > stokTersedia) {
                JOptionPane.showMessageDialog(this, "Jumlah sewa tidak valid atau melebihi stok tersedia (" + stokTersedia + ")!");
                return;
            }
            for (Object[] item : cartList) {
                int existingId = Integer.parseInt(item[0].toString());
                if (existingId == idAlat) {
                    int newQty = Integer.parseInt(item[3].toString()) + qty;
                    if (newQty > stokTersedia) {
                        JOptionPane.showMessageDialog(this, "Total kuantitas di keranjang melebihi stok tersedia!");
                        return;
                    }
                    item[3] = newQty;
                    item[4] = hargaPerHari * newQty;
                    fieldQty.setText("");
                    JOptionPane.showMessageDialog(this, "Jumlah \"" + namaAlat + "\" di keranjang diperbarui menjadi " + newQty + ".");
                    return;
                }
            }

            cartList.add(new Object[]{idAlat, namaAlat, hargaPerHari, qty, hargaPerHari * qty});
            updateCartButtonLabel();
            fieldQty.setText("");
            JOptionPane.showMessageDialog(this, "\"" + namaAlat + "\" ditambahkan ke keranjang!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah sewa dengan angka yang valid!");
        }
    }//GEN-LAST:event_btnTambahKeranjangActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        new CustomerMainFrame(currentUser).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnLihatKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatKeranjangActionPerformed
        new CustomerCartFrame(currentUser, cartList).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLihatKeranjangActionPerformed

    private void tabelAlatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelAlatMouseClicked
        int row = tabelAlat.getSelectedRow();
        if (row != -1 && filteredEquipmentList != null && row < filteredEquipmentList.size()) {
            Equipment selectedEq = filteredEquipmentList.get(row);
            lblItemTerpilih.setText("Item Terpilih: " + selectedEq.getName());

            String path = selectedEq.getImagePath();

            if (path != null && !path.isEmpty()) {
                try {
                    File file = new File(path);
                    if (file.exists()) {
                        BufferedImage img = ImageIO.read(file);
                        if (img != null) {
                            int w = lblGambarAlat.getWidth() > 0 ? lblGambarAlat.getWidth() : 160;
                            int h = lblGambarAlat.getHeight() > 0 ? lblGambarAlat.getHeight() : 160;
                            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                            lblGambarAlat.setIcon(new ImageIcon(scaled));
                            lblGambarAlat.setText("");
                            return;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Gagal memuat gambar katalog: " + e.getMessage());
                }
            }

            lblGambarAlat.setIcon(null);
            lblGambarAlat.setText("Tidak ada gambar");
        }
    }//GEN-LAST:event_tabelAlatMouseClicked

    private void jScrollPane2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jScrollPane2PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2PropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CustomerCatalogFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnLihatKeranjang;
    private javax.swing.JButton btnTambahKeranjang;
    private javax.swing.JTextField fieldQty;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblGambarAlat;
    private javax.swing.JLabel lblItemTerpilih;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable tabelAlat;
    // End of variables declaration//GEN-END:variables
}
