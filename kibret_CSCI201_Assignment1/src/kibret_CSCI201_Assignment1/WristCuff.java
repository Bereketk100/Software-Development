package kibret_CSCI201_Assignment1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class WristCuff {

	private static ArrayList<TimefallShelter> shelters = new ArrayList<TimefallShelter>();
	String dataSource;


	public static void setShelters(ArrayList<TimefallShelter> shelters) {
		WristCuff.shelters = shelters;
	}

	public static ArrayList<TimefallShelter> getShelters() {
		return shelters;
	}

	public void setDatasource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDatasource() {
		return dataSource;
	}

	/**
	 * List all available shelters within the min and max of supported Chiral
	 * frequencies
	 */
	public void listAllShelters(Set<Integer> chiralFrequencies) {
		int max = -1;
		int min = -1;
		for (Integer num : chiralFrequencies) {
			max = Math.max(num, max);
			min = Math.min(num, min);

		}

		ArrayList<TimefallShelter> rangedShelters = new ArrayList<TimefallShelter>();

		for (TimefallShelter shelter : shelters) {
			if (shelter.getChiralFrequency() <= max && shelter.getChiralFrequency() >= min
					&& shelter.getTimefall() == false) {

				rangedShelters.add(shelter);

			}
		}

		System.out.println("\n" + rangedShelters.size() + " results\n");

		for (int index = 0; index < rangedShelters.size(); index++) {
			System.out.println(rangedShelters.get(index));
		}

	}

	public boolean byChiralfrequency(int freq) {
		for (TimefallShelter shelter : shelters) {
			if (shelter.getChiralFrequency() == freq) {
				System.out.println("");
				System.out.println(shelter);
				return true;
			}

		}
		System.out.println("\nThat Chiral frequency does not exist.");
		return false;

	}

	public boolean byName(String name) {

		for (TimefallShelter shelter : shelters) {
			if (shelter.getName().toLowerCase().equals(name)) {
				System.out.println("\nFound!\n");
				System.out.println(shelter);
				return true;
			}

		}
		System.out.println("\nNo such shelter...");
		return false;

	}

	/**
	 * Find an available shelter with the lowest supported Chiral frequency
	 * 
	 * @throws IOException
	 */
	public void findShelter(Set<Integer> chiralFrequencies) throws IOException {

		ArrayList<Integer> chiralFreq = new ArrayList<Integer>();
		for (Integer frequency : chiralFrequencies) {
			chiralFreq.add(frequency);
		}

		Collections.sort(chiralFreq);

		boolean found = false;
		for (Integer frequency : chiralFreq) {
			if (found) {
				break;
			}

			for (int index = 0; index < shelters.size(); index++) {
				if (shelters.get(index).getChiralFrequency() == frequency) {
					if (shelters.get(index).getTimefall()) {
						System.out
								.println("=== Chiral frequency " + frequency + " unstable, Chiral jump unavaible ===");// PRINT
																														// OUT
																														// NOT
																														// SAFE
						System.out.println(
								"=== Removing target shelter from the list of shelters and saving updated list to disk. ===\n");
						shelters.remove(index);
						index--;

						save();

					} else {
						found = true;
						System.out.println("=== Matching timefall shelter found! ===");
						System.out.println(shelters.get(index));
						System.out.println("=== Commencing Chiral jump, see you in safety. ===\n");
						break;
					}
				}
			}
		}
		if (!found) {
			System.out.println("=== No shelter available. You are DOOMED. ===");
		}

	}

	/**
	 * Sort shelters by Chiral frequency
	 * 
	 * @throws IOException
	 */
	public void sortShelters() throws IOException {
		Collections.sort(shelters);
		save();
		System.out.println("Shelters successfully sorted by Chiral frequency.");
	}

	/**
	 * Saves the updated list of shelters to disk
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		Gson gson = new Gson();
		FileWriter fileWriter = new FileWriter(dataSource);
		gson.toJson(shelters, fileWriter);
		fileWriter.close();

	}

}
