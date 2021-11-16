package kibret_CSCI201_Assignment1;

import java.io.FileNotFoundException;
import java.util.Collections;

public class TimefallShelter implements Comparable<TimefallShelter> {
	/**
	 * Here: all the needed class members and their getters and setters
	 */
	private int chiralFrequency = -1;
	private Boolean timefall;
	private String guid;
	private String name;
	private String phone;
	private String address;

	public void setChiralFrequency(int chiralFrequency) {
		this.chiralFrequency = chiralFrequency;
	}

	public int getChiralFrequency() {
		return chiralFrequency;
	}

	public void setTimefall(boolean timefall) {
		this.timefall = timefall;
	}

	public Boolean getTimefall() {
		return timefall;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	/**
	 * overriding comparator for sorting
	 */

	@Override
	public int compareTo(TimefallShelter compShelter) {
		/* For Ascending order */
		return getChiralFrequency() - compShelter.getChiralFrequency();
	}

	/**
	 * String representation of a shelter
	 */
	@Override
	public String toString() {
		String output = "";
		output += "Shelter Information: \n";
		output += "- Chiral frequency: " + getChiralFrequency() + "\n";
		if (getTimefall()) {
			output += "- Timefall: " + "Current" + "\n";
		} else {
			output += "- Timefall: " + "None" + "\n";
		}
		output += "- GUID: " + getGuid() + "\n";
		output += "- Name: " + getName() + "\n";
		output += "- Phone: " + getPhone() + "\n";
		output += "- Address: " + getAddress() + "\n";
		return output;
	}
}
