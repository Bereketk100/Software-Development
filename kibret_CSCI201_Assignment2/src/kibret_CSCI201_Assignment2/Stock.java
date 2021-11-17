package kibret_CSCI201_Assignment2;


public class Stock {
    /**
	 * Here: all the needed class members and their getters and setters
	 */
	private String name;
	private String ticker;
	private String startDate;
	private Integer stockBrokers;
	private String description;
	private String exchangeCode;
	
	
    public Stock() {

    }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getTicker() {
		return ticker;
	}


	public void setTicker(String ticker) {
		this.ticker = ticker;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDtae(String startDtae) {
		this.startDate = startDtae;
	}


	public Integer getStockBroker() {
		return stockBrokers;
	}


	public void setStockBroker(int stockBroker) {
		this.stockBrokers = stockBroker;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getExchangeCode() {
		return exchangeCode;
	}


	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	@Override
	public String toString() {
		String output = "";
		
		output += "Stock Information: \n";
		output += "- Name: " + getName() + "\n";
		output += "- ticker: " + getTicker() + "\n";
		output += "- startDtae: " + getStartDate() + "\n";
		output += "- stockBroker: " + getStockBroker() + "\n";
		output += "- description: " + getDescription() + "\n";
		output += "- exchangeCode: " + getExchangeCode() + "\n\n\n\n";
		return output;
	}


}

