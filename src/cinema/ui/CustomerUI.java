/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package cinema.ui;

import cinema.models.Movie;
import cinema.models.Showtime;
import cinema.models.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showInputDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Ivan
 */
public class CustomerUI extends javax.swing.JFrame {
	
	private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CustomerUI.class.getName());
	
	private JPanel movieGallery;
	private JTextField searchField;
	private User currentUser;

	/**
	 * Creates new form CustomerUI
	 */
	public CustomerUI(User user) {
		this.currentUser = user;
		initComponents();
		setupCustomLayout();
		displayMovies("");
		setLocationRelativeTo(null);
                setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
                
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
	}
	
	private void setupCustomLayout() {
		// Main cointainer ( BorderLayout )
		jPanel1.setLayout(new BorderLayout(10, 10));
		
		// Header Panel + Search field
		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(new JLabel("Search Movies : "));
		searchField = new JTextField(20);
		
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				displayMovies(searchField.getText());
			}
		});
		header.add(searchField);
		jPanel1.add(header, BorderLayout.NORTH);
		
		JPanel galleryWrapper = new JPanel(new BorderLayout());
		movieGallery = new JPanel(new GridLayout(0, 3, 5, 5));
		galleryWrapper.add(movieGallery, BorderLayout.NORTH);
		
		// Scroll Pane
		JScrollPane scrollPane = new JScrollPane(galleryWrapper);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // ?
		jPanel1.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void displayMovies(String query) {
		movieGallery.removeAll();
		List<Movie> allMovies = Movie.getAll();
		
		for (Movie m : allMovies) {
			if (m.getTitle().toLowerCase().contains(query.toLowerCase()) && m.getStatus() == Movie.Status.SHOWING) {
				movieGallery.add(createMovieCard(m));
			}
		}
		
		movieGallery.revalidate();
		movieGallery.repaint();
	}
	
	private JPanel createMovieCard(Movie m) {
		JPanel card = new JPanel(new BorderLayout(0,5));
		card.setPreferredSize(new Dimension(140, 220));
		card.setMaximumSize(new Dimension(140, 220));
		// card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		// card.setBackground(Color.WHITE);
		
		JLabel poster = new JLabel();
		poster.setHorizontalAlignment(JLabel.CENTER);
		String path = m.getPosterPath();
		File imageFile = new File(path);
		
		if (imageFile.exists()) {
			ImageIcon icon = new ImageIcon(path);
			if (icon.getIconWidth() > 0) {
				Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
				poster.setIcon(new ImageIcon(img));
			} else {
				poster.setText("Image Unavailable");
				poster.setPreferredSize(new Dimension(120, 160));
			}
		} else {
			System.err.println("Missing Image : " + imageFile.getAbsolutePath());
			poster.setText("Missing : " + m.getMovieID());
		}
		
		JButton btnBook = new JButton("<html><center><b>" + m.getTitle() + "</b><br>" + m.getGenre() + " | " + m.getRating() + "⭐</center></html>");
		btnBook.setMargin(new Insets(2, 2, 2, 2)); // Tighten the button padding
		btnBook.setFont(new Font("SansSerif", Font.PLAIN, 11));
		
		btnBook.addActionListener(e -> {
			openBookingForMovie(m);
		});
		
		card.add(poster, BorderLayout.CENTER);
		card.add(btnBook, BorderLayout.SOUTH);
		
		return card; // JPanel
	}
	
	private void openBookingForMovie(Movie selectedMovie) {
		List<Showtime> allShowtimes = Showtime.getAll();
		
		List<Showtime> movieShowtimes = new ArrayList<>();
		for (Showtime st : allShowtimes) {
			if (st.getMovieID().equals(selectedMovie.getMovieID())) {
				movieShowtimes.add(st);
			}
		}
		
		if (movieShowtimes.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No showtimes available for this movie.");
			return;
		}
		
		String[] options = new String[movieShowtimes.size()];
        for (int i = 0; i < movieShowtimes.size(); i++) {
            Showtime s = movieShowtimes.get(i);
            options[i] = "Hall " + s.getHallNum() + " | " + s.getDateTime();
        }
		
		String selection = (String) JOptionPane.showInputDialog(
            this,
            "Pick a time for " + selectedMovie.getTitle(),
            "Showtime Selection",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
		
		if (selection != null) {
            int index = -1;
            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selection)) index = i;
            }
            
            Showtime chosenShowtime = movieShowtimes.get(index);
            
            // Pass the showtime and the current user to the next window
            BookingUI bookingWin = new BookingUI(chosenShowtime, currentUser, selectedMovie);
            bookingWin.setVisible(true);
            bookingWin.setLocationRelativeTo(this);
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

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 520, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
