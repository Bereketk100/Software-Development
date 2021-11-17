package PA3.driver;

import PA3.models.DeliveryInformation;
import PA3.models.DriverInformation;
import PA3.models.Location;
import PA3.models.Order;
import PA3.util.DistanceCalc;
import PA3.util.TimeFormatter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;



class DriverShell {
	public static void main(String[] args) {   	
//		YelpAPIParser n = new YelpAPIParser();
//		n.getLocation("McDonalds", new Location();
		
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to SalEats v2.0!");
        Socket s;
        while (true) {
            String ans = null;
            try {
                System.out.print("Enter the server hostname: ");
                String hostname = in.nextLine();
                System.out.print("Enter the server port number: ");
                ans = in.nextLine();
                int port = Integer.parseInt(ans);
             
                s = new Socket(hostname, port);
                System.out.println();
                break;
            } catch (java.net.ConnectException e) {
            	System.out.println("Invalid port. Try again!");
            
            } catch (NumberFormatException nfe) {
                System.out.println("The given input " + ans + " is not a number.\n");
            } catch (UnknownHostException uhe) {
                System.out.println("The given host is unknown.\n");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } 
        }
        in.close();
        try {
        	ObjectOutputStream pw = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            while (true) {
                Integer num = (Integer) ois.readObject();
                if (num == 0) {
                    System.out.println("All drivers have arrived!");
                    System.out.println("Starting service.\n");
                    break;
                } else {
                    System.out.println(num + " more driver is needed before the service can begin.");
                    System.out.println("Waiting...\n");
                }
            }
            while (true) {
            	
                DeliveryInformation info = (DeliveryInformation) ois.readObject();
                if (info == null) {
                    System.out.println("["+TimeFormatter.getTimestamp()+"]" + " All orders completed!");
                    pw.writeObject("finished");
                    break;
                }
               try {
            	   DriverInformation driverInfo = new DriverInformation(info);
            	   deliver(driverInfo);
                     
               } catch (IndexOutOfBoundsException e) {
            	   System.out.println("Location cannot be found due to coordinates of HQ");
            	   e.printStackTrace();
            	   
               } catch(NullPointerException e) {}
                pw.writeObject("done");
                pw.flush();
            }
        } catch (SocketException se) {
            System.out.println(TimeFormatter.getTimestamp() + " Server dropped connection. All orders completed!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
        }
    }

    private static void deliver(DriverInformation info) {
        for (Order order : info.getOrders()) {
            System.out.print("["+TimeFormatter.getTimestamp()+ "]" + " Starting delivery of");
            System.out.println(order.getItemName() + " to" + order.getRestaurantName() + ".");
        }
        try {
            Order currentOrder = info.getNext();
            if (currentOrder != null) {
                String prevName = currentOrder.getRestaurantName();
                Location prevLocation = currentOrder.getLocation();
                while (true) {
                    info.reorder(prevLocation);
                 
                   
                    Thread.sleep((long) (currentOrder.getDistance()*1000));
                    while (currentOrder != null && prevName.equals(currentOrder.getRestaurantName())) {
                        System.out.print("["+TimeFormatter.getTimestamp()+"]" + " Finished delivery of");
                        System.out
                                .println(currentOrder.getItemName() + " to" + currentOrder.getRestaurantName() + ".");
                        currentOrder = info.getNext();
                    }
                    if (currentOrder == null) {
          
                        System.out.println("["+TimeFormatter.getTimestamp()+"]" + " Finished all deliveries, returning back to HQ.");
                     
                        Thread.sleep((long) (DistanceCalc.getDistance(prevLocation, info.getHQLocation())));
                        System.out.println("["+TimeFormatter.getTimestamp()+"]" + " Returned to HQ.");
                        break;
                    }
                    prevLocation = currentOrder.getLocation();
                    prevName = currentOrder.getRestaurantName();
                    System.out.println("["+TimeFormatter.getTimestamp()+"]" + " Continuing delivery to "
                            + currentOrder.getRestaurantName() + ".");
                }
            }
        } catch (InterruptedException ie) {
            System.out.println("Interrupted");
        }
    }
}
