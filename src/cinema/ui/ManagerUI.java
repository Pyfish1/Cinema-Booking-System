/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cinema.ui;
import cinema.models.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ivan
 */
public class ManagerUI extends javax.swing.JFrame {
	
	private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ManagerUI.class.getName());

	/**
	 * Creates new form ManagerUI
	 */
	public ManagerUI() {
		initComponents();
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		loadUserTable();
		loadMovieTable();
		loadShowtimeTable();
		
		this.addWindowListener(new WindowAdapter() {
			@Override 
			public void windowClosing(WindowEvent e) {
				int confirm = javax.swing.JOptionPane.showConfirmDialog(
					null, 
					"Are you sure you want to log out?", 
					"Logout", 
					javax.swing.JOptionPane.YES_NO_OPTION
				);
				
				if (confirm == JOptionPane.YES_OPTION) {
					dispose();
					
					LoginUI login = new LoginUI();
					login.setLocationRelativeTo(null);
					login.setVisible(true);
				}
			}
		});
		
		userTable.getModel().addTableModelListener(e -> {
			int row = e.getFirstRow();
			int column = e.getColumn();
			
			if (e.getType() == TableModelEvent.UPDATE) {
				updateUserFromTable(row);
			}
		});
			
		movieTable.getModel().addTableModelListener(e -> {
			int row = e.getFirstRow();
			int column = e.getColumn();
			
			if (e.getType() == TableModelEvent.UPDATE) {
				updateMovieFromTable(row);
			}
		});
		
                showtimeTable.getModel().addTableModelListener(e -> {
                            int row = e.getFirstRow();
    
                            if (e.getType() == TableModelEvent.UPDATE) {
                                updateShowtimeFromTable(row);
                        }
    
});
		
		
	}
	
	public void loadUserTable() {
		Object[][] userData = User.get2DArray();
		String[] userHeaders = {"ID", "Name", "Email", "Password", "Role"};
 		DefaultTableModel userModel = new DefaultTableModel(userData, userHeaders) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0; 
			}
		};
		userTable.setModel(userModel);
	}
	
	public void loadMovieTable() {
		Object[][] movieData = Movie.get2DArray();
		String[] movieHeaders = {"ID", "Title", "Genre", "Duration ( Minutes )", "Rating", "Status", "Poster Path"};
		DefaultTableModel movieModel = new DefaultTableModel(movieData, movieHeaders) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column != 0;
			}
		};
		movieTable.setModel(movieModel);
	}
	
	public void loadShowtimeTable() {
		Object[][] showtimeData = Showtime.get2DArray();
		String[] showtimeHeaders = {"ShowtimeID", "Movie", "Hall", "Date & Time", "Capacity", "Price"};
		DefaultTableModel showtimeModel = new DefaultTableModel(showtimeData, showtimeHeaders) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 2 || column == 3 || column == 5;
			}
		};
		showtimeTable.setModel(showtimeModel);
                showtimeTable.getModel().addTableModelListener(e -> {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        updateShowtimeFromTable(row);
        }
    });
	}
	
	private void updateUserFromTable(int row) {
		DefaultTableModel model = (DefaultTableModel) userTable.getModel();
		
		String ID = model.getValueAt(row, 0).toString();
		String NAME = model.getValueAt(row, 1).toString();
		String EMAIL = model.getValueAt(row, 2).toString();
		String PASSWORD = model.getValueAt(row, 3).toString();
		String ROLESTR = model.getValueAt(row, 4).toString(); // TODO : Add validation here.

		User.Role ROLE = User.Role.valueOf(ROLESTR.toUpperCase());
		
		User updatedUser = User.create(ID, NAME, EMAIL, PASSWORD, ROLE);	
		User.update(ID, updatedUser);
		
		System.out.println("User " + ID + " updated in text file.");
	}
	
	private void updateMovieFromTable(int row) {
		DefaultTableModel model = (DefaultTableModel) movieTable.getModel();
		
		String ID = model.getValueAt(row, 0).toString();
		String TITLE = model.getValueAt(row, 1).toString();
		String GENRE = model.getValueAt(row, 2).toString();
		int DURATION = Integer.parseInt(model.getValueAt(row, 3).toString()); // autistic code my god - Ivan
		double RATING = Double.parseDouble(model.getValueAt(row, 4).toString()); // TODO : DEFINITELY ADD VALIDATION HERE. FUTURE IVAN PLS FIX
		Movie.Status STATUS = Movie.Status.valueOf(model.getValueAt(row, 5).toString());
		String POSTERPATH = model.getValueAt(row, 6).toString();
		
		Movie updatedMovie = new Movie(ID, TITLE, GENRE, DURATION, RATING, STATUS, POSTERPATH);
		Movie.update(ID, updatedMovie); // Takes in ID and Movie object, replaces the corresponding ID the object.
		
		System.out.println("Movie " + ID + " updated in text file.");
		
	}
	
        private void updateShowtimeFromTable(int row){
            try{
                DefaultTableModel model = (DefaultTableModel) showtimeTable.getModel();
                String ID = model.getValueAt(row, 0).toString();

                Showtime existing = Showtime.getAll().stream()
                        .filter(s -> s.getShowtimeID().equals(ID))
                        .findFirst().orElse(null);

                if (existing == null) return;

                int HALL = Integer.parseInt(model.getValueAt(row, 2).toString());
                String DATETIME = model.getValueAt(row, 3).toString();
                double PRICE = Double.parseDouble(model.getValueAt(row, 5).toString()); // Now at Index 5

                String MOVIEID = existing.getMovieID();
                String HEXSEATS = Showtime.encodeSeats(existing.getSeats());
                Showtime updated = new Showtime(ID, MOVIEID, HALL, DATETIME, HEXSEATS, PRICE);
                Showtime.update(ID, updated);

                System.out.println("Showtime " + ID + " updated with Price: " + PRICE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(this, "Update failed: Make sure Hall is an integer and Price is a decimal");
            loadShowtimeTable();
            }
        }
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        deleteSelectedUserButton = new javax.swing.JButton();
        addNewUserButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        movieTable = new javax.swing.JTable();
        addNewMovieButton = new javax.swing.JButton();
        deleteSelectedMoiveButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showtimeTable = new javax.swing.JTable();
        deleteSelectedShowtimeButton1 = new javax.swing.JButton();
        addNewShowtimeButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(userTable);

        deleteSelectedUserButton.setText("Delete Selected");
        deleteSelectedUserButton.addActionListener(this::deleteSelectedUserButtonActionPerformed);

        addNewUserButton.setText("Add New");
        addNewUserButton.addActionListener(this::addNewUserButtonActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(509, 509, 509)
                .addComponent(deleteSelectedUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteSelectedUserButton)
                    .addComponent(addNewUserButton))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Users", jPanel1);

        movieTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(movieTable);

        addNewMovieButton.setText("Add New");
        addNewMovieButton.addActionListener(this::addNewMovieButtonActionPerformed);

        deleteSelectedMoiveButton.setText("Delete Selected");
        deleteSelectedMoiveButton.addActionListener(this::deleteSelectedMoiveButtonActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(512, 512, 512)
                .addComponent(deleteSelectedMoiveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewMovieButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteSelectedMoiveButton)
                    .addComponent(addNewMovieButton))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Movies", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 748, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );

        tabbedPane.addTab("Bookings", jPanel3);

        showtimeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(showtimeTable);

        deleteSelectedShowtimeButton1.setText("Delete Selected");
        deleteSelectedShowtimeButton1.addActionListener(this::deleteSelectedShowtimeButton1ActionPerformed);

        addNewShowtimeButton1.setText("Add New");
        addNewShowtimeButton1.addActionListener(this::addNewShowtimeButton1ActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteSelectedShowtimeButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewShowtimeButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteSelectedShowtimeButton1)
                    .addComponent(addNewShowtimeButton1))
                .addContainerGap(78, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Showtimes", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tabbedPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void deleteMovie() {
		int selectedRow = movieTable.getSelectedRow();
		if (selectedRow != -1) {
			String ID = movieTable.getValueAt(selectedRow, 0).toString();
			int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
			
			if (confirm == JOptionPane.YES_OPTION) {
				Movie.delete(ID);
				loadMovieTable(); // Reload list.
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a movie to delete");
		}
	}
	
	private void deleteUser() {
		int selectedRow = userTable.getSelectedRow();
		if (selectedRow != -1) {
			String ID = userTable.getValueAt(selectedRow, 0).toString();
			int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
			
			if (confirm == JOptionPane.YES_OPTION) {
				User.delete(ID); // Delete from file.
				loadUserTable(); // Reload list.
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select a user to delete");
		}
	}
	
        private void deleteShowtime() {
                int selectedRow = showtimeTable.getSelectedRow();
                if (selectedRow != -1) {
                    String ID = showtimeTable.getValueAt(selectedRow,0).toString();
                    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        Showtime.delete(ID);
                        loadShowtimeTable();
                    }
                } else {
                        JOptionPane.showMessageDialog(this, "Please select a showtime to delete");
                }
        }
	
	
    private void deleteSelectedMoiveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedMoiveButtonActionPerformed
		deleteMovie();
    }//GEN-LAST:event_deleteSelectedMoiveButtonActionPerformed

    private void addNewMovieButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewMovieButtonActionPerformed
        AddMovieUI ui = new AddMovieUI(this);
		ui.setVisible(true);
		ui.setLocationRelativeTo(null);
    }//GEN-LAST:event_addNewMovieButtonActionPerformed

    private void deleteSelectedUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedUserButtonActionPerformed
        deleteUser();
    }//GEN-LAST:event_deleteSelectedUserButtonActionPerformed

    private void addNewUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewUserButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addNewUserButtonActionPerformed

    private void addNewShowtimeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewShowtimeButton1ActionPerformed
        // TODO add your handling code here:
        AddShowtimeUI ui = new AddShowtimeUI(this);
            ui.setVisible(true);
            ui.setLocationRelativeTo(null);
    }//GEN-LAST:event_addNewShowtimeButton1ActionPerformed

    private void deleteSelectedShowtimeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedShowtimeButton1ActionPerformed
        // TODO add your handling code here:
        deleteShowtime();
    }//GEN-LAST:event_deleteSelectedShowtimeButton1ActionPerformed

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
		java.awt.EventQueue.invokeLater(() -> new ManagerUI().setVisible(true));
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewMovieButton;
    private javax.swing.JButton addNewShowtimeButton1;
    private javax.swing.JButton addNewUserButton;
    private javax.swing.JButton deleteSelectedMoiveButton;
    private javax.swing.JButton deleteSelectedShowtimeButton1;
    private javax.swing.JButton deleteSelectedUserButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable movieTable;
    private javax.swing.JTable showtimeTable;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
