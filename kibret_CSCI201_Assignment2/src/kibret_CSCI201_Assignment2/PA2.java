package kibret_CSCI201_Assignment2;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class PA2 {
	private static ArrayList<Stock> listOfStocks = new ArrayList<>();
	private static Map<String, Semaphore> semaphoresPool;
	private static Schedule schedule = new Schedule();
	private static int totalBrokers = 0;
	private static long starttime = 0;

	/**
	 * Read Stock Json File inputed by user using GSON
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void readStockFile() throws FileNotFoundException, IOException {

		Gson gson = new Gson();
		jsonData data = new jsonData();
		System.out.println("What is the name of the file containing the company information?: ");
		Scanner scan = new Scanner(System.in);
		String dataSource = scan.nextLine().toLowerCase();

		
		try {

			Reader reader = new FileReader(dataSource);
			data = gson.fromJson(reader, jsonData.class);
			System.out.println();

		} catch (JsonSyntaxException e) {
			System.out.println(dataSource + " Json File is not formatted correctly. \n");
			readStockFile();

		} catch (FileNotFoundException e) {
			System.out.println(dataSource + " Json File is not found. \n");
			readStockFile();
		} catch (NullPointerException e) {
			System.out.println(dataSource + " has an a empty field \n");
			readStockFile();
		} catch (IllegalArgumentException e) {
			System.out.println(dataSource + " has a field that is not supported \n");
			readStockFile();
		}

		if (data.getData() != null) {

			listOfStocks = new ArrayList<Stock>(Arrays.asList(data.getData()));

			for (int i = 0; i < data.getSize(); i++) {

				if (listOfStocks.get(i).getName() == null) {
					System.out.print("error: Missing Name data parameters.\n\n");
					readStockFile();
					return;
				}

				if (listOfStocks.get(i).getTicker() == null) {
					System.out.print("error: Missing Ticker data parameters.\n\n");
					readStockFile();
					return;
				}

				if (listOfStocks.get(i).getStartDate() == null) {
					System.out.print("error: Missing Start Date data parameters.\n\n");
					readStockFile();
					return;
				}

				if (listOfStocks.get(i).getStockBroker() == null || listOfStocks.get(i).getStockBroker() <= 0) {
					System.out.print("error: Invalid Stock Brokers data parameters.\n\n");
					readStockFile();
					return;
				}

				if (listOfStocks.get(i).getDescription() == null) {
					System.out.print("error: Missing Description data parameters.\n\n");
					readStockFile();
					return;
				}
				
				if (listOfStocks.get(i).getExchangeCode() == null) {
					System.out.print("error: Missing Description data parameters.\n\n");
					readStockFile();
					return;
				}

				String name = listOfStocks.get(i).getName().replaceAll("\\s", "");

				String ticker = listOfStocks.get(i).getTicker().replaceAll("\\s", "");

				String start = listOfStocks.get(i).getStartDate().replaceAll("\\s", "");

				String des = listOfStocks.get(i).getDescription().replaceAll("\\s", "");
				
				String exch = listOfStocks.get(i).getExchangeCode().replaceAll("\\s", "");

				if (name.length() == 0) {
					System.out.print("error: Missing Name parameters.\n\n");
					readStockFile();
				}
				if (ticker.length() == 0) {
					System.out.print("error: Missing Ticker parameters.\n\n");
					readStockFile();
				}
				if (start.length() == 0) {
					System.out.print("error: Missing Start Date parameters.\n\n");
					readStockFile();
				}
				if (des.length() == 0) {
					System.out.print("error: Missing Description parameters.\n\n");
					readStockFile();
				}
				if (exch.length() == 0) {
					System.out.print("error: Exchange Code parameters.\n\n");
					readStockFile();
				}
			}		
		}
	}

	/**
	 * Read Stock Trades CSV File inputed by user
	 * 
	 * @throws IOException
	 */
	private static void readScheduleFile() throws IOException {
		System.out.println("What is the name of the file containing the schedule information? ");
		Scanner scan = new Scanner(System.in);
		String dataSource = scan.nextLine().toLowerCase();
		ArrayList<String> ticks = new ArrayList<>();
		String line = "";

		try {

			BufferedReader reader = new BufferedReader(new FileReader(dataSource));
			while ((line = reader.readLine()) != null) {
				String[] task = line.split(",");
				int initiated = Integer.parseInt(task[0]);
				String ticker = task[1];
				ticks.add(ticker);
				int boughtOrSold = Integer.parseInt(task[2]);

				try {
					schedule.getBlockingQueue().put(new Schedule.Task(initiated, ticker, boughtOrSold));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println(dataSource + " file not found \n");
			readScheduleFile();
			return;
		} catch (NumberFormatException e) {
			System.out.println(dataSource + " file format not accepted \n");
			readScheduleFile();
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(dataSource + " file format not accepted \n");
			readScheduleFile();
			return;
		} 

		ArrayList<String> s = new ArrayList<>();

		for (int i = 0; i < listOfStocks.size(); i++) {
			s.add(listOfStocks.get(i).getTicker());
		}

		for (int i = 0; i < ticks.size(); i++) {

			if (!s.contains(ticks.get(i))) {
				System.out.println(dataSource + " contains Invalid Ticker \n");
				readScheduleFile();
				return;

			}
		}

	}

	/**
	 * Set up Semaphore for Stock Brokers
	 */

	private static void initializeSemaphor() {

		Map<String, Semaphore> tempMap = new HashMap<String, Semaphore>();

		for (Stock s : listOfStocks) {
			int broker = s.getStockBroker();
			totalBrokers += broker;
			String str = s.getTicker();
			tempMap.put(str, new Semaphore(broker));

		}
		semaphoresPool = Collections.synchronizedMap(tempMap);

	}

	private static void executeTrades() throws InterruptedException {
		starttime = System.currentTimeMillis();
		System.out.println("\nStarting execution of program...");
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(totalBrokers);
		
		for (Schedule.Task t : schedule.getBlockingQueue()) {
			int start = t.getInitiatedCsv();

			executor.schedule(new Trade(schedule, t, semaphoresPool.get(t.getTickerCsv())), start, TimeUnit.SECONDS);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			Thread.yield();
		}

		System.out.println("All trades are completed!");
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws InterruptedException {

		PA2 solution = new PA2();

		try {
			solution.readStockFile();
			solution.readScheduleFile();
			solution.initializeSemaphor();
			solution.executeTrades();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}
