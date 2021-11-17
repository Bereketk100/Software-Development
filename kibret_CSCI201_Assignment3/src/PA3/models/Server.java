package PA3.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static void main(String[] args) {
		sendCompletedServer();
	}

	private static Semaphore semaphore;
	private static ServerSocket serverSockets;

	public static int drivers = 0;
	private static double lat = 0;
	private static double lon = 0;
	public static int maxDrivers;

	private static Map<Integer, DeliveryInformation> timeMap = new HashMap<>();
	public static ArrayBlockingQueue<ServerThread> serverThreads;

	public static class diliveryThread extends Thread {
		
		private static ArrayBlockingQueue<ServerThread> servThr;
		DeliveryInformation driverInfo;
		private Semaphore semThr;
	

		public diliveryThread(Semaphore semThr, ArrayBlockingQueue<ServerThread> servThr, DeliveryInformation driverInfo) {
			diliveryThread.servThr = servThr;
			this.semThr = semThr;
			this.driverInfo = driverInfo;
		}

		public void run() {
			try {
				semThr.acquire();
				for (ServerThread availableThread : servThr) {
					if (availableThread.isAvailable()) {
						availableThread.sendOrder(driverInfo); // in serverThread
						while (!availableThread.isAvailable()) {
							Thread.sleep(70);
						}
					}
				}

			} catch (InterruptedException e) {}
			finally {
				semThr.release();
			}
		}
	}

	private static void readScheduleFile() throws IOException {
		System.out.println("What is the name of the schedule file? ");
		Scanner scanner = new Scanner(System.in);
		String txtFile = scanner.nextLine().toLowerCase();
		String line = "";
		Map<Integer, ArrayList<Event>> easy = new HashMap<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(txtFile));
			while ((line = reader.readLine()) != null) {
				String[] task = line.split(",");

				int time = Integer.parseInt(task[0]);
				String resName = task[1];
				String item = task[2];

				if (easy.containsKey(time)) {
					ArrayList<Event> t = easy.get(time);
					t.add(new Event(time, resName, item));
					easy.replace(time, t);

				} else {
					ArrayList<Event> t = new ArrayList<Event>();
					Event e = new Event(time, resName, item);
					t.add(e);
					easy.put(time, t);

				}
			}

		} catch (FileNotFoundException e) {
			System.out.println();
			System.out.print("That file does not exist. ");
			readScheduleFile();
			return;
		} catch (NumberFormatException e) {
			System.out.println();
			System.out.print("That file is not properly format. ");
			readScheduleFile();
			return;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println();
			System.out.print("That file is not properly format. ");
			readScheduleFile();
			return;
		} catch (NoSuchElementException e) {
			System.out.println();
			System.out.print("That file is not properly format. ");
			readScheduleFile();
			return;
		} catch (NullPointerException e) {
			System.out.println();
			System.out.print("That file is not properly format. ");
			readScheduleFile();
			return;
		}

		Map<Integer, DeliveryInformation> m = new HashMap<>();
		Set<Integer> times = easy.keySet();

		for (Integer t : times) {
			ArrayList<String> restaurants = new ArrayList<String>();
			ArrayList<String> items = new ArrayList<String>();
			ArrayList<Event> events = easy.get(t);
			for (int i = 0; i < easy.get(t).size(); i++) {
				Event e = events.get(i);
				restaurants.add(e.getStartLocation());
				items.add(e.getItemName());
			}

			m.put(t, new DeliveryInformation(restaurants, items, new Location(lat, lon), t));
		}
		timeMap = m;

	}

	public static void longLat() {
		try {
			Scanner input = new Scanner(System.in);
			System.out.println("");
			System.out.println("What is your latitude?");
			lat = input.nextDouble();
			System.out.println("");
			System.out.println("What is your longitude?");
			lon = input.nextDouble();
			System.out.println("");
			System.out.println("How many drives will be in service today?");
			drivers = input.nextInt();

			if (drivers == 0) {
				System.out.println("A minimum of one driver is needed!");
				System.out.println("Goodbye");
				try {
					readScheduleFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			maxDrivers = drivers;

			for (Integer time : timeMap.keySet()) {
				DeliveryInformation d = timeMap.get(time);
				d.setLocation(new Location(lat, lon));
			}
		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Try again! ");
			longLat();
			return;
		} catch (NoSuchElementException e) {
			System.out.println("Invalid input. Try again! ");
			longLat();
			return;

		} catch (NullPointerException e) {
			System.out.println("Invalid input. Try again! ");
			longLat();
			return;

		}

	}

	public void connectDrivers() {
		serverThreads = new ArrayBlockingQueue<ServerThread>(drivers);
		try {
			serverSockets = new ServerSocket(3456);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (drivers != 0) {
			try {
				Socket s = serverSockets.accept();
				String ad = s.getInetAddress().toString();
				drivers -= 1;
				System.out.println();
				System.out.println("Connection from " + ad.substring(1));
				if (drivers != 0) {
					System.out.println("Waiting for " + drivers + " more driver(s)...");
				}

				ServerThread st = new ServerThread(s, this);
				serverThreads.add(st);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Starting Service");
	}

	public static void sendOrder() {

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(maxDrivers);
		
		ArrayList<Integer> listofTimes = new ArrayList<Integer>();
		ArrayList<diliveryThread> dilThr = new ArrayList<diliveryThread>();
		for (Integer time : timeMap.keySet()) {
			listofTimes.add(time);
		}
		Collections.sort(listofTimes); // we must sort
		for (Integer t : listofTimes) {
			dilThr.add(new diliveryThread(semaphore, serverThreads, timeMap.get(t)));
		}
		for (diliveryThread d : dilThr)
			executor.schedule(d, d.driverInfo.getTime(), TimeUnit.SECONDS);
		executor.shutdown();
		while (!executor.isTerminated()) {
			Thread.yield();
		}
	}

	

	public static void sendCompletedServer() {
		Server server = new Server();
		try {
			readScheduleFile();
			longLat();
			semaphore = new Semaphore(maxDrivers);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("Listening on port " + "3456" + ".");
		System.out.println("Waiting for driver(s)...");
		server.connectDrivers();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		sendOrder();
		for (ServerThread s : serverThreads) {
			s.sendDi(null);
		}
		System.out.println("All orders are completed");
	}

}
