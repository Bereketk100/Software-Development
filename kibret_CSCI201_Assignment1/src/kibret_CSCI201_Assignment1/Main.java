package kibret_CSCI201_Assignment1;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class Main {

	private Set<Integer> supportedFrequencies = new HashSet<>();
	private WristCuff wrist = new WristCuff();

	/**
	 * Uses GSON to read the file inputed by the user
	 */
	@SuppressWarnings("static-access")
	private void readFile() throws IOException {

		TimefallShelter[] tempObjects = null;

		boolean t = true;
		while (t) {
			System.out.print("Please provide timefall shelter data source: ");

			Scanner scan = new Scanner(System.in);
			wrist.setDatasource(scan.nextLine());

			Gson gson = new Gson();
			try (Reader reader = new FileReader(wrist.getDatasource())) {

				try {
					tempObjects = gson.fromJson(reader, TimefallShelter[].class);

				} catch (JsonSyntaxException e) {
					System.out.println("Json File is not formatted correctly.\n");
					readFile();

				}

				for (int index = 0; index < tempObjects.length; index++)

				{

					wrist.getShelters().add(tempObjects[index]);

					if (wrist.getShelters().get(index).getChiralFrequency() == -1) {
						System.out.print("error: Missing Chiral Frequency data parameters.\n\n");
						readFile();

					} else if (wrist.getShelters().get(index).getTimefall() == null) {
						System.out.print("error: Missing Time Fall data parameters.\n\n");
						readFile();
					} else if (wrist.getShelters().get(index).getGuid() == null
							|| wrist.getShelters().get(index).getGuid().length() == 0) {
						System.out.print("error: Missing Guid parameters.\n\n");
						readFile();

					} else if (wrist.getShelters().get(index).getName() == null
							|| wrist.getShelters().get(index).getName().length() == 0) {
						System.out.print("error: Missing Name parameters.\n\n");
						readFile();
					} else if (wrist.getShelters().get(index).getPhone() == null
							|| wrist.getShelters().get(index).getPhone().length() == 0) {
						System.out.print("error: Missing Phone parameters.\n\n");
						readFile();
					} else if (wrist.getShelters().get(index).getAddress() == null
							|| wrist.getShelters().get(index).getAddress().length() == 0) {
						System.out.print("error: Missing Address parameters.\n\n");
						readFile();
					} else if (wrist.getShelters().get(index).getName() == null
							|| wrist.getShelters().get(index).getName().length() == 0) {
						System.out.print("error: Missing Name parameters.\n\n");
						readFile();
					}

					else {
						t = false;
					}

					String name = wrist.getShelters().get(index).getName().replaceAll("\\s", "");
					String guid = wrist.getShelters().get(index).getGuid().replaceAll("\\s", "");
					String phone = wrist.getShelters().get(index).getPhone().replaceAll("\\s", "");
					String address = wrist.getShelters().get(index).getAddress().replaceAll("\\s", "");

					if (name.length() == 0) {
						System.out.print("error: Missing Name parameters.\n\n");
						readFile();
					}

					if (address.length() == 0) {
						System.out.print("error: Missing Address parameters.\n\n");
						readFile();
					}

					if (guid.length() != 36 || guid.charAt(8) != '-' || guid.charAt(13) != '-' || guid.charAt(18) != '-'
							|| guid.charAt(23) != '-') {
						System.out.println("Invalid GUID in Json file.\n");
						readFile();

					}

					String guid1 = wrist.getShelters().get(index).getGuid().substring(0, 8);
					String guid2 = wrist.getShelters().get(index).getGuid().substring(9, 13);
					String guid3 = wrist.getShelters().get(index).getGuid().substring(14, 18);
					String guid4 = wrist.getShelters().get(index).getGuid().substring(19, 23);
					String guid5 = wrist.getShelters().get(index).getGuid().substring(24, 36);

					boolean isHex1 = guid1.matches("^[0-9a-fA-F]+$");
					boolean isHex2 = guid2.matches("^[0-9a-fA-F]+$");
					boolean isHex3 = guid3.matches("^[0-9a-fA-F]+$");
					boolean isHex4 = guid4.matches("^[0-9a-fA-F]+$");
					boolean isHex5 = guid5.matches("^[0-9a-fA-F]+$");

					if (!isHex1 || !isHex2 || !isHex3 || !isHex4 || !isHex5) {
						System.out.println("Invalid Hex GUID in Json file.\n");
						readFile();

					}

					if (phone.length() < 15 || phone.length() > 17) {
						System.out.println("Invalid phone number in Json file.\n");
						readFile();
					}

					if (phone.length() == 15) {

						if (phone.charAt(0) != '+' || phone.charAt(2) != '(' || phone.charAt(6) != ')'
								|| phone.charAt(10) != '-') {
							System.out.println("Invalid phone number in Json file.\n");
							readFile();

						}

					}

					else if (phone.length() == 17) {

						if (phone.charAt(0) != '+' || phone.charAt(4) != '(' || phone.charAt(8) != ')'
								|| phone.charAt(12) != '-') {

							System.out.println("Invalid phone number in Json file.\n");
							readFile();

						}
						char a = phone.charAt(1);
						char b = phone.charAt(2);
						char z = phone.charAt(3);

						if (!Character.isDigit(a) || !Character.isDigit(b) || !Character.isDigit(z)) {
							System.out.println("Invalid phone number in Json file.\n");
							readFile();
						}

					} else if (phone.length() == 16) {

						if (phone.charAt(0) != '+' || phone.charAt(3) != '(' || phone.charAt(7) != ')'
								|| phone.charAt(11) != '-') {
							System.out.println("Invalid phone number in Json file.\n");
							readFile();

						}
						char c = phone.charAt(1);
						char d = phone.charAt(2);

						if (!Character.isDigit(c) || !Character.isDigit(d)) {
							System.out.println("Invalid phone number in Json file.\n");
							readFile();
						}

					}

					if (phone.length() == 15) {

						for (int i = 0; i < phone.length(); i++) {

							if (i == 1 || (i >= 3 && i <= 5) || (i >= 7 && i <= 9) || (i >= 11 && i <= 15)) {
								char c = phone.charAt(i);
								if (!Character.isDigit(c)) {
									System.out.println("Invalid phone number in Json file.\n");
									readFile();

								}
							}
						}

					}

					if (phone.length() == 16) {

						for (int i = 0; i < phone.length(); i++) {

							if (i == 1 || (i >= 4 && i <= 6) || (i >= 8 && i <= 9) || (i >= 12 && i <= 16)) {
								char c = phone.charAt(i);
								if (!Character.isDigit(c)) {
									System.out.println("Invalid phone number in Json file.\n");
									readFile();

								}
							}
						}

					}

					if (phone.length() == 17) {

						for (int i = 0; i < phone.length(); i++) {

							if (i == 1 || (i >= 5 && i <= 7) || (i >= 9 && i <= 11) || (i >= 13 && i <= 17)) {
								char c = phone.charAt(i);
								if (!Character.isDigit(c)) {
									System.out.println("Invalid phone number in Json file.\n");
									readFile();

								}
							}
						}

					}

				}

			} catch (FileNotFoundException e) {
				System.out.println("The file " + wrist.getDatasource() + " could not be found.\n");
			}

		}
		System.out.println("=== Data accepted ===\n");

	}

	/**
	 * Gets the supported chiral frequencies from the user
	 */
	private void setSupportedFrequencies() {
		boolean loop = true;
		int valid = 0;

		while (loop) {
			System.out.print("Please provide supported Chiral frequencies: ");
			Scanner scanner = new Scanner(System.in);
			String line = scanner.nextLine();
			line = line.replaceAll("\\s", "");

			if (!line.isEmpty()) {

				String[] temp = line.split(",");
				for (String eachNum : temp) {

					try {
						supportedFrequencies.add(Integer.parseInt(eachNum));
						valid = 0;
					} catch (NumberFormatException e) {
						System.out.print("Invalid Chiral frequencies exists.\n\n");
						valid = 1;
						loop = true;
						break;
					}

					if (Integer.parseInt(eachNum) < 0) {
						System.out.print("Negetive Chiral frequencies are not allowed. \n\n");
						valid = 1;
					}

				}
			}
			if (valid == 0)
				loop = false;
		}

	}

	/**
	 * choice 1
	 */
	private void choiceOne() {
		wrist.listAllShelters(supportedFrequencies);

	}

	/**
	 * choice 2
	 */
	private void choiceTwo() {
		Scanner scan = new Scanner(System.in);
		int frequency1;
		boolean foundFreq = false;
		while (foundFreq == false) {
			System.out.print("\nWhat Chiral frequency are you looking for? ");
			frequency1 = scan.nextInt();
			foundFreq = wrist.byChiralfrequency(frequency1);
		}
	}

	/**
	 * choice 3
	 */
	private void choiceThree() {
		Scanner scan = new Scanner(System.in);
		String name;
		boolean foundName = false;

		while (foundName == false) {
			System.out.print("\nWhat shelter’s name are you looking for? ");
			name = scan.nextLine();
			foundName = wrist.byName(name.toLowerCase());
		}

	}

	/**
	 * choice 4
	 */
	private void choiceFour() throws IOException {
		System.out.println("");
		wrist.sortShelters();

	}

	/**
	 * choice 4
	 */
	private void choiceFive() throws IOException {
		System.out.println("");
		System.out.println("=== Commencing timefall shelter search ===");
		wrist.findShelter(supportedFrequencies);
	}

	/**
	 * Prints the option menu
	 */
	private void displayOptions() {
		System.out.println("\n\t1) List all available shelters within the min and max of supported Chiral frequencies\n"
				+ "\t2) Search for a shelter by Chiral frequency\n" + "\t3) Search for a shelter by name\n"
				+ "\t4) Sort shelters by Chiral frequency\n"
				+ "\t5) Jump to a shelter with the lowest supported Chiral frequency");
	}

	public static void main(String[] args) throws IOException {
		Main solution = new Main();

		System.out.println("Welcome to Bridges Link.\n");

		solution.readFile();
		solution.setSupportedFrequencies();

		String choice = "";

		do {
			solution.displayOptions();
			System.out.print("Choose an option: ");
			Scanner scan = new Scanner(System.in);
			choice = scan.nextLine();

			if (choice.equals("1")) {

				solution.choiceOne();

			} else if (choice.equals("2")) {
				solution.choiceTwo();
			}

			else if (choice.equals("3")) {
				solution.choiceThree();

			} else if (choice.equals("4")) {
				solution.choiceFour();

			} else if (choice.equals("5")) {
				solution.choiceFive();

			}

		} while (!choice.equals("5"));

	}
}
