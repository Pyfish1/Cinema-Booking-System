package cinema.models;

/**
 *
 * @author Ivan
 */
public record Seat(int row, int col) {
	@Override
	public String toString() {
		return row + ":" + col;
	}
	
	public String getLabel() {
		char row = (char) ('A' + this.row);
		int col = this.col + 1;
		return row + String.valueOf(col);
	}
}
