package org.tuc;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class TestDataCollector {
	/**
	 * Holds the headings of the table
	 */
	private List<String> headings;

	/**
	 * Holds the measurements as a list for each row
	 */
	private List<List> rows;

	/**
	 * Constructor
	 * 
	 * @param headings Headings
	 */
	public TestDataCollector(List headings) {
		this.headings = headings;
		rows = new ArrayList();
	}

	/**
	 * Adds a row with measurements
	 * 
	 * @param row row with measurements
	 */
	public void addRow(List row) {
		rows.add(row);
	}

	public void toScreen() {
		// print headings
		this.print(System.out, " | ");
		System.out.println("Data collected printed to screen");
	}

	/**
	 * Writes the table to a file
	 * 
	 * @param fileName
	 */
	public void toFile(String fileName) {
		try {
			PrintStream printStream = new PrintStream(fileName);
			this.print(printStream, ";");
			printStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Data collected saved to " + fileName);
	}

	private void print(PrintStream printstream, String separator) {
		// Determine maximum width for each column
		int[] maxWidths = new int[headings.size()];
		for (int i = 0; i < headings.size(); i++) {
			maxWidths[i] = headings.get(i).toString().length();
		}
		for (List<Object> row : rows) {
			for (int i = 0; i < row.size(); i++) {
				int len = row.get(i).toString().length();
				if (len > maxWidths[i]) {
					maxWidths[i] = len;
				}
			}
		}

		// Print headings
		for (int i = 0; i < headings.size(); i++) {
			printstream.printf("%-" + (maxWidths[i]) + "s", headings.get(i));
			if (i < headings.size() - 1) {
				printstream.print(separator);
			}
		}
		printstream.println(); // new line

		// Print rows
		for (List<Object> row : rows) {
			for (int i = 0; i < row.size(); i++) {
				Object cell = row.get(i);
				if (cell instanceof Integer) {
					printstream.printf("%-" + (maxWidths[i]) + "d", (Integer) cell);
				} else if (cell instanceof Float) {
					printstream.printf("%-" + (maxWidths[i]) + ".2f", (Float) cell);
				} else {
					printstream.printf("%-" + (maxWidths[i]) + "s", cell);
				}
				if (i < row.size() - 1) {
					printstream.print(separator);
				}
			}
			printstream.println(); // new line
		}
	}

}
