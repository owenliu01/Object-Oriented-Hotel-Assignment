import java.util.ArrayList;
import java.util.concurrent.locks.*;
/**
* Hotel that keeps track of all reservation and available days. Makes sure days are valid and no double booking.
* Allows only one thread to access each method once.
* 
* @author Owen Liu
* Student Number: 100321041
* Date:July 23,2019
* CPSC 1181-001
* Lab 9
*/
public class Hotel{
	private ArrayList<Integer> begin = new ArrayList<Integer>(31);//Customers first day booked
	private ArrayList<Integer> end = new ArrayList<Integer>(31);//Customers last day booked
	private ArrayList<String> names = new ArrayList<String>(31);//Customers name
	private Lock counterLock;
	
	public Hotel(){
		counterLock = new ReentrantLock(); //Create lock for threads
	}
	
	/**
	 * Makes a reservation in the calendar of August. Does not allow double booking and verifies days.
	 * Checks whether the day is booked or not.
	 * 
	 * @param name name of customer
	 * @param firstDay first day they want to stay at the hotel
	 * @param lastDay last day they will stay at the hotel
	 * @return returns true if reservation was successful, or false if unsuccessful
	 */
	public boolean makeReservation(String name, int firstDay, int lastDay) {
		counterLock.lock();
		
		boolean notBooked = true;
		
		//Make sure days are valid
		if(firstDay <= 0 || lastDay > 31 || lastDay < firstDay) {
			notBooked = false;
		}
		
		//Check if person has booked before
		for(int i = 0; i < names.size(); i++) {
			if(name.equalsIgnoreCase(names.get(i))) {
				notBooked = false;
			}
			
		}
		
		//Check if dates overlap
		for(int i = 0; i < begin.size(); i++) {
			if(firstDay >= begin.get(i) && firstDay <= end.get(i) || lastDay <= end.get(i) && lastDay >= end.get(i) || firstDay < begin.get(i) && lastDay > end.get(i)) {
				notBooked = false;
				break;
			}
		}
		
		if(notBooked == true) {
			names.add(name);
			begin.add(firstDay);
			end.add(lastDay);
			counterLock.unlock();
			return true;
		}else {
			counterLock.unlock();
			return false;
		}
	}
	
	/**
	 * cancels the users reservation based on their name.
	 * 
	 * @param name name of customer
	 * @return returns true if the cancellation was successful, false if there is no reservation to cancel.
	 */
	public boolean cancelReservation(String name) {
		counterLock.lock();
		
		int customerIndex = -1; //default check index
		
		//Find customers position in booking
		for(int i = 0; i < names.size(); i++) {
			if(name.equalsIgnoreCase(names.get(i))) {
				customerIndex = i;
			}
		}
		
		if(customerIndex == -1) {
			counterLock.unlock();
			return false;
		}else {
			names.remove(customerIndex);
			begin.remove(customerIndex);
			end.remove(customerIndex);
			counterLock.unlock();
			return true;
		}
	}
	
	/**
	 * Displays entire calendar of August, and shows which days a booked, and which are available.
	 * 
	 * @return String of entire calendar booking days.
	 */
	public String reservationInformation() {
		int dayCount = 1;
		String output = "";
		for(int i = 0; i < 31; i ++) {
			for(int j = 0; j < names.size(); j++) {
				if(begin.get(j) == dayCount) {
					dayCount += (end.get(j) - begin.get(j)) + 1;
					i += (end.get(j) - begin.get(j)) + 1;
					output += "Name: " + names.get(j) + " Date Booked for month of August: " + begin.get(j) + "-" + end.get(j) + "\n";
				}
			}
			output += "August " + dayCount + ": Available!" + "\n";
			dayCount += 1;
		}
		return output;
	}
	
	
}//end of class
