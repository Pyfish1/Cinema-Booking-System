package cinema.services;

import cinema.models.Seat;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class SeatManager {      //logic for bitmasking
	private static final int ROWS = 5;
	private static final int COLS = 10;
	private static final int TOTAL_SEATS = ROWS * COLS;
	
	public static String[][] decode(String hex, int rows, int cols) {
		String binary = new BigInteger(hex, 16).toString(2);
		binary = String.format("%" + TOTAL_SEATS + "s", binary).replace(' ', '0');
		String[][] grid = new String[rows][cols];
		char[] bits = binary.toCharArray();
		
		for (int i = 0; i < TOTAL_SEATS; i++) {
			int r = i / cols;
			int c = i % cols;
			grid[r][c] = String.valueOf(bits[i]);
		}
		return grid;
	}
	
	public static String updateSeats(String hex, List<Seat> newlyBooked) {  // convert hex to binary 
		String binary = new BigInteger(hex, 16).toString(2);
		binary = String.format("%" + TOTAL_SEATS + "s", binary).replace(' ', '0');
		char[] bits = binary.toCharArray();
		
		for (Seat s : newlyBooked) {
			int index = (s.row() * COLS) + s.col();
			if (index >= 0 && index < TOTAL_SEATS) {
				bits[index] = '1';
			}
		}
		
		String newBinary = new String(bits);
		return new BigInteger(newBinary, 2).toString(16).toUpperCase();
	}
	
	public static String getEmptyHallHex() {
		return "0000000000000";
	}
	
}
