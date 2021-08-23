/*
 
 * This Java Program will create a parent class Media and 3 child classes EBook, MovieDVD, MusicCD
 * This Java program will implement Manager class that will stores list of Media objects,
 * load Media objects from files, add new Media object to its Media list,
 * find all media objects for a specific title, and rent Media based on id.
 * This program will also design and implement MediaRentalSystem.
 * 
 *  Vanessa Nankong
 * 07/12/2021
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class CMIS242FPNankongV {

	public static abstract class Media {
		// attributes
		private int id;
		private String title;
		private int year;
		private boolean available;

		// constructor
		public Media(int id, String title, int year, boolean available) {
			this.id = id;
			this.title = title;
			this.year = year;
			this.available = available;
		}

		// get methods
		public int getId() {
			return this.id;
		}

		public String getTitle() {
			return this.title;
		}

		public int getYear() {
			return this.year;
		}

		public boolean isAvailable() {
			return available;
		}

		protected void setTitle(String title) {
			this.title = title;
		}

		protected void setYear(int year) {
			this.year = year;
		}

		protected void setAvailable(boolean available) {
			this.available = available;
		}

		// calculate rental fee; for generic media it is flat fee $3.50
		public double calculateRentalFee() {
			return 3.50;
		}

	}

	public static class EBook extends Media {
		private int numChapters;

		public EBook(int id, String title, int year, int chapters, boolean available) {
			super(id, title, year, available);
			numChapters = chapters;

		}

		// get method
		public int getNumChapters() {
			return numChapters;
		}

		// set method
		public void setNumChapters(int numChapters) {
			this.numChapters = numChapters;
		}

		// override parent's
		@Override
		public double calculateRentalFee() {
			double fee = numChapters * 0.10; // basic fee
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			if (this.getYear() == currYear)
				fee += 1; // add $1.00 fee
			return fee;
		}

		@Override
		public String toString() {
			return "EBook [ id=" + getId() + ", title=" + getTitle() + ", year=" + getYear() + ", chapters="
					+ numChapters + ", available=" + this.isAvailable() + " ]";
		}
	}

	public static class MovieDVD extends Media {
		// local attributes
		private double size; // value in MB

		// constructor
		public MovieDVD(int id, String title, int year, double size, boolean available) {
			super(id, title, year, available);
			this.size = size;
		}

		// get method
		public double getSize() {
			return size;
		}

		// set method
		public void setSize(double size) {
			this.size = size;
		}

		// inherits calculate rental fee method and no different calculation so should
		// not override
		@Override
		public String toString() {
			return "MovieDVD [ id=" + getId() + ", title=" + getTitle() + ", year=" + getYear() + ", size=" + getSize()
					+ "MB, available=" + this.isAvailable() + " ]";
		}
	}

	public static class MusicCD extends Media {
		private int length;

		// constructor
		public MusicCD(int id, String title, int year, int length, boolean isRented) {
			super(id, title, year, isRented);
			this.length = length;
		}

		// get method
		public int getLength() {
			return length;
		}

		// set method
		public void setLength(int length) {
			this.length = length;
		}

		// override parent's
		@Override
		public double calculateRentalFee() {
			double fee = length * 0.02; // basic fee
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			if (this.getYear() == currYear)
				fee += 1; // add $1.00 fee
			return fee;
		}

		@Override
		public String toString() {
			return "MusicCD [ id=" + getId() + ", title=" + getTitle() + ", year=" + getYear() + ", length="
					+ getLength() + "min, available=" + this.isAvailable() + " ]";
		}

	}

	public static class Manager {

		// attributes
		List<Media> medias;

		// default constructor
		public Manager() {

			medias = new ArrayList<Media>();

		}

		// load all media files from directory; assumes file name convention starts with
		// media type EBook or MovieDVD or MusicCD followed by id
		// If directory is not found, it will throw exception
		public void LoadMedias(String directory) {

			// initialize empty medias list
			this.medias = new ArrayList<Media>();

			// Create a File object for directory
			File directoryPath = new File(directory);

			// Get list of all files and directories
			File files[] = directoryPath.listFiles();

			if (files == null) {
				System.out.println("File cannot be opened: Could not load, no such directory.");
				return;
			}

			// declare local variables
			Media media = null;
			String line = null;
			String[] array = null;
			int id;
			String title = null;
			int year;
			boolean available;
			Scanner scan = null;

			// Process each Media file
			for (File file : files) {

				// parse files whose filename starts with "EBook" or "MovieDVD" or "MusicCD"
				if (file.getName().contains("EBook") || file.getName().contains("MovieDVD")
						|| file.getName().contains("MusicCD")) {

					// open and read line (assumes whole object is stored on single line)
					try {
						scan = new Scanner(file);
						line = scan.nextLine(); // assumes the file is not empty
						array = line.split(",");
						id = Integer.parseInt(array[0]);
						title = array[1];
						year = Integer.parseInt(array[2]);
						available = Boolean.parseBoolean(array[4]);

						// if EBook object than call EBook constructor
						if (file.getName().contains("EBook")) {
							int chapters = Integer.parseInt(array[3]);

							media = new EBook(id, title, year, chapters, available);
						}

						// if MovieDVD object than call MovieDVD constructor
						if (file.getName().contains("MovieDVD")) {
							double size = Double.parseDouble(array[3]);

							media = new MovieDVD(id, title, year, size, available);
						}

						// if MusicCD object than call MusicCD constructor
						if (file.getName().contains("MusicCD")) {
							int length = Integer.parseInt(array[3]);

							media = new MusicCD(id, title, year, length, available);
						}

						// add Media object to medias attribute
						this.addMedia(media);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			// display all medias
			displayAllMedias();
		}

		// find media by title
		public void findMedia(String title) {
			boolean found = false;

			for (Media media : medias) {
				if (media.getTitle().equals(title)) {
					System.out.println(media.toString());
					found = true;
				}
			}

			if (!found)
				System.out.println("There is no media with this title: " + title);
		}

		// rent media by id and return rental fee if available
		public void rentMedia(int id) {
			boolean found = false;

			for (Media media : medias) {

				if (media.getId() == id) {

					found = true;

					if (media.isAvailable()) {

						// update rental status on media
						media.setAvailable(false);

						// return rental fee
						String formattedRentalFee = String.format("%.2f", media.calculateRentalFee());
						
						System.out.println(
								"Media was successfully rented out. Rental fee = $" + formattedRentalFee);

					} else {

						System.out.println("Media with id=" + id + " is not available for rent.");
						
					}

					break;

				}
			}

			if (!found) {
				System.out.println("The media object id=" + id + " is not found.");
			}
		}

		// display all stored medias on console
		public void displayAllMedias() {

			// for all Media objects display data
			for (Media media : medias) {
				System.out.println(media.toString());
			}
		}

		// add Media object
		public void addMedia(Media media) {
			this.medias.add(media);
		}

	}

	public static class MediaRentalSystem {

		public static void main(String[] args) {

			// Set selection variable
			int selection = -1;

			// Create Manager object
			Manager manager = new Manager();

			// Show menu until user quits
			while (selection != 9) {

				// Create Scanner object
				Scanner scanner = new Scanner(System.in);

				// Show menu for selection
				showMenu();

				// Display "Enter your selection"
				System.out.print("Enter your selection: ");

				// Get selection
				selection = scanner.nextInt();

				System.out.println();

				switch (selection) {
				case 1:
					System.out.print("Enter path (directory) where to load from: ");

					// get user input
					scanner.nextLine();
					String path = scanner.nextLine();

					System.out.println();

					manager.LoadMedias(path);

					System.out.println("\n------------------");
					break;

				case 2:
					System.out.print("Enter the title: ");

					// get user input
					scanner.nextLine();
					String title = scanner.nextLine();

					System.out.println();

					manager.findMedia(title);

					System.out.println("\n------------------");
					break;

				case 3:
					System.out.print("Enter the id: ");

					int id = scanner.nextInt();

					System.out.println();

					manager.rentMedia(id);

					System.out.println("\n------------------");
					break;

				case 9:
					System.out.println("Thank you for using the program. Goodbye!");
					
					System.exit(0);
					break;
				}

			}
		}

		private static void showMenu() {
			System.out.println("\nWelcome to Media Rental System");
			System.out.println("1: Load Media objects...");
			System.out.println("2: Find Media object...");
			System.out.println("3: Rent Media object...");
			System.out.println("9: Quit");
			System.out.println();
		}
	}

}// end CMIS242FinalProjectNankongV

/*
 * EBook.txt
 * 
 123,Forever Young,2018,20,true
122,My Bag,2011,15,true
120,The Room,2012,25,false


MovieDVD.txt

126,Forever Young,2020,140,false
127,Only Lovers,2013,145,true
121,Hard Day,1964,120,false

MusicCD.txt

124,Beyond Today,2020,114,true
212,Love You,2015,115,false
321,My Life, 2019,105,true



 * 
 * */
