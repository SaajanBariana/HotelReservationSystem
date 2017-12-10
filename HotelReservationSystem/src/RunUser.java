import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class RunUser {
	public DatabaseConnection connection;
	public boolean quit = false;
	
	public RunUser()
	{
		connection = new DatabaseConnection("guest");
	}
	
	/**
	 * the main method of the Guest side
	 */
	public void runProgram()
	{
		
		boolean choseValidNumber = false;
		while (!choseValidNumber && !quit)
		{
			int input = readInput("");
			switch(input)
			{
				//Chose to log in
				case 1: logIn();
					choseValidNumber = true;
					break;
				//Chose to create an account
				case 2: createAccount();
					choseValidNumber = true;
					break;
				//Chose to quit the program
				case 3: 
					quit = true;
					System.out.println("Thank you for visiting! :)");
					break;
				//Chose a number that is not one of the above choices
				default: System.out.println("Please try again with a valid number choice");
					break;
			}
		}
		
		choseValidNumber = false; //reset the boolean to be false
		
		while(!choseValidNumber && !quit)
		{
			int input = readInput(UserInformation.name);
			switch(input)
			{
				//Chose to create a reservation
				case 1: reserveRoom();
					break;
				//Chose to cancel a reservation
				case 2: cancelReservation();
					break;
				//Chose to display all the available rooms
				case 3: listAvailableRooms();
					break;
				//Chose to change a reservation
				case 4: updateReservation();
					break;
				//Chose to display the different pricing for different types of rooms
				case 5: getAverageCostPerRoomType();
					break;
				//Chose to show all the active reservations the guest currently has
				case 6: getGuestReservations();
					break;
				//Chose to quit the program
				case 7:
					choseValidNumber = true;
					break;
				//Chose a number that was not a valid choice
				default: System.out.println("Please try again with a valid number choice");
					break;
			}
		}
			
	}
	
	/**
	 * displays the choices to the user
	 * @param name takes the name of the user
	 * @return returns the selection that the user chose
	 */
	public int readInput(String name)
	{
		Scanner in = new Scanner(System.in);
		boolean worked = false;
		while (!worked && !quit)
		{
			if(name.equals(""))
			{
				System.out.println("Welcome to the Guest Side of the Hotel Reservation System!\n"
						+ "Which operation would you like to perform?\n"
						+ "(1) Log In\n"
						+ "(2) Create an account\n"
						+ "(3) Quit program");
			}
			else
			{
				System.out.println("Hello " + name +"!\n"
						+ "What would you like to do?\n"
						+ "(1) Create a reservation\n"
						+ "(2) Cancel a reservation\n"
						+ "(3) Display a list of all available rooms\n"
						+ "(4) Change a reservation\n"
						+ "(5) Display pricing for different types of rooms\n"
						+ "(6) View all of your active reservations\n"
						+ "(7) Log Out\n");
			}
			try
			{
				int result = in.nextInt();
				worked = true;
				return result;
			}
			catch (Exception e)
			{
				System.out.println("There was an error with your input: " + e + "\nPlease try again.");
			}
		}
		return -1;
	}

	/**
	 * Logs the user in and saved their email into the UserInformation class
	 */
	public void logIn()
	{
		boolean loggedIn = false;
		ResultSet result = null;
		while(!loggedIn && !quit)
		{
			Scanner input = new Scanner(System.in);
			boolean foundUsername = false;
			boolean correctPassword = false;
			int count = 0;
			String passInDB = "";
			String ID = null;
			while(!foundUsername && !quit)
			{
				try
				{
					System.out.println("What is your email? ");
					String username = input.nextLine();
					if(username.toLowerCase().equals("q"))
					{
						quit = true;
						System.out.println("\nThank you for visiting! :)");
						break;
					}
					result = connection.sendQuery("select * from guests where email = '" + username + "';");
					if (!result.next())
					{
						System.out.println("This user does not exist. Please make sure you have the correct username or create a new account. Please (q) to quit.");
					}
					else
					{
						passInDB = result.getString(6);
						ID = username;
						foundUsername = true;
					}
				}
				catch (Exception e)
				{
					System.out.println("There was an error: " + e);
					return;
				}
			}
			while(!correctPassword && !passInDB.equals(""))
			{
				try
				{
					System.out.println("What is your password?");
					String password = input.nextLine();
					if (password.equals(passInDB))
					{
						correctPassword = true;
						loggedIn = true;
						UserInformation.ID = ID;
						UserInformation.name = result.getString(2) + " " + result.getString(3);
					}
					else
					{
						if(count == 2)
						{
							System.out.print("This is your last chance");
						}
						if(count == 3)
						{
							System.out.println("Incorrect password. Logging out. Good Bye");
							return;
						}
						else
						{
							System.out.println("Incorrect password. Please try again.");
							count++;
						}
					}
				}
				catch (Exception e)
				{
					System.out.println("There was an error: " + e);
				}
			}	
		}
	}
	
	/**
	 * Creates a new Guest account
	 */
	public void createAccount()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter first name:");
		String first = in.nextLine();
		System.out.println("Enter last name:");
		String last = in.nextLine();
		System.out.println("Enter address:");
		String address = in.nextLine();
		System.out.println("Enter phone number:");
		String phone = in.nextLine();
		System.out.println("Enter email:");
		String email = in.nextLine();
		System.out.println("Enter password:");
		String password = in.nextLine();
		System.out.println("Confirm password: ");
		String confirmation = in.nextLine();
		while(!confirmation.equals(password))
		{
			System.out.println("Passwords did not match\nEnter password:");
			password = in.nextLine();
			System.out.println("Confirm password: ");
			confirmation = in.nextLine();
		}
		try
		{
			connection.addGuest(first, last, address, phone, email, password);
			UserInformation.ID = email;
			UserInformation.name = first+" "+last;

		}
		catch(Exception e)
		{
			
			System.out.println("This account may already exist");
		}
	}

	/**
	 * Gets all the guests that are currently in the database. Used for testing and showing that the user was added
	 * @return returns a lost of all the guests
	 */
	public ArrayList<String> getGuests()
	{
		try 
		{
			ArrayList<String> guestEmail = new ArrayList<>();
			ResultSet results = connection.getGuests();
			while(results.next())
			{
				System.out.println("{Email: " + results.getString(1) + ", First name: " + results.getString(2) + ", Last Name: " + results.getString(3) +"}");
				guestEmail.add(results.getString(1));
			}
			System.out.println();
			return guestEmail;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to display the guests: " + e);
		}
		return null;
	}
	
	
	/**
	 * Reserves a room for the given guest
	 */
	public void reserveRoom ()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter the arrival date (yyyy-mm-dd):");
		String arrivalDate = in.nextLine();
		System.out.println("Enter the departure date:(yyyy-mm-dd)");
		String departureDate = in.nextLine();
		Date aD = Date.valueOf(arrivalDate);
		Date dD = Date.valueOf(departureDate);
		ResultSet result = connection.listAvailableRoomsBetweenDates(aD,dD);
		System.out.println("A list of the available rooms and per night cost: ");
		ArrayList<Integer> list = new ArrayList<>();
		try {
			System.out.println("Room Number"+ "\t"+ "Price");
			while (result.next()) {
				System.out.println(result.getInt(1) +"\t\t$"+result.getInt(2));
				list.add(result.getInt(1));
			}
		}
		catch (Exception exception)
		{
			System.out.println("an error occurred when trying to print the list of available rooms: "+ exception);
		}
		
		String guestEmail = UserInformation.ID;
		boolean looping = true;
		String roomNumber = "";
		while (looping)
		{
			System.out.println("Enter the room number you would like:");
			roomNumber = in.nextLine();
			if(!list.contains(roomNumber))
			{
				looping = false;
			}
		}
		System.out.println("How many keys do you need?");
		String numberOfKeys = in.nextLine();
		try
		{
			connection.reserveRoom(Date.valueOf(arrivalDate),Date.valueOf(departureDate),guestEmail,roomNumber,numberOfKeys);
			System.out.println("Total cost for this stay will be: $" + getReservationCost(roomNumber, arrivalDate, departureDate) + "\n");
			getGuestReservations();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to book a room. Please make sure that the reservation date is valid");
		}
		
	}
	
	/**
	 * Returns the cost of a reservation
	 */
	public Integer getReservationCost(String roomNumber, String arrivalDate, String departureDate){
		int cost = 0;
		int room = Integer.parseInt(roomNumber);
		LocalDate start = LocalDate.parse(arrivalDate);
		LocalDate end = LocalDate.parse(departureDate);
		Period num = Period.between(start, end);
		String query = "SELECT Price from Rooms where RoomNumber = " + room;
	    ResultSet resultSet = connection.sendQuery(query);
	    try 
	    {
	    	while (resultSet.next()) 
	    	{
	    		cost = resultSet.getInt(1);
	    	}
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    cost = cost * num.getDays();
		return cost;
		
	}
	/**
	 * displays a list of all available rooms
	 */
	public void listAvailableRooms ()
	{
		System.out.println("Available Rooms: ");
		ResultSet resultSet = connection.listAvailableRooms();
		try
		{
			System.out.println("#\t\tPrice\t\tBeds\t\tType\t\tClean");
			while(resultSet.next()){
				if( resultSet.getInt(6) == 1)
				{
					System.out.println(resultSet.getInt(1)+"\t\t$"+resultSet.getInt(2)+"\t\t"
							+ resultSet.getInt(3)+"\t\t"+ resultSet.getString(4)+"\t\t"+ resultSet.getInt(5));
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to view the available rooms: " + e);
		}

	}

	/**
	 * gets the average cost per type of room
	 */
	public void getAverageCostPerRoomType()
	{
		String size = "";
		try
		{
			Scanner in = new Scanner(System.in);
			System.out.println("What type of room do you want to find the average of (Small, Medium, or Large)?");
			size = in.nextLine();
			System.out.println("The average for a " + size + " sized room is: $" + connection.getAverageForRoomType(size) + "\n");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred while trying to display the average for a " + size + " room: " + e);
		}
	}

	/**
	 * Cancel the reservation for a given email and date
	 */
	public void cancelReservation()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter the arrival date (yyyy-mm-dd):");
		String date = in.nextLine();
		try
		{
			connection.cancelReservation(UserInformation.ID,Date.valueOf(date));
			System.out.println("Successfully cancelled the reservation.\n");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to cancel the reservation for "+UserInformation.ID+" on "+ date+": " + e);
		}
	}

	/**
	 * View a list of current reservations
	 */
	public ArrayList<Integer> getGuestReservations()
	{
		ArrayList<Integer> reservationNumbers = new ArrayList<>();
		try 
		{
			String email = UserInformation.ID;
			ResultSet results = connection.getGuestReservations(email);
			if (!results.next()) System.out.println("You dont have any active reservations");
			results.beforeFirst();
			while(results.next())
			{
				reservationNumbers.add(results.getInt(1));
				System.out.println("{Reservation ID: " + results.getInt(1) + ", ArrivalDate: " + results.getString(2) + ", DepartureDate: " + results.getString(3) + ", RoomNumber: " + results.getInt(5)+"}");
			}
			
			System.out.println();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying display reservations for "+UserInformation.ID+ e);
		}
		return reservationNumbers;
	}
	
	/**
	 * Updates a guests reservation
	 */
	public void updateReservation()
	{
		try
		{
			String email = UserInformation.ID;
			ResultSet results = connection.getGuestReservations(email);
			if (!results.next()) 
			{
				System.out.println("You dont have any active reservations");
			}
			else
			{
				Scanner in = new Scanner(System.in);
			    ArrayList<Integer> reservations = getGuestReservations();
			    if(reservations.size() > 0)
			    {
					System.out.println("Please enter a Reservation ID that you would like to be updated");
					int resNum = in.nextInt();
					if(!reservations.contains(resNum))
					{
						System.out.println("This is not a valid reservation number");
					}
					else
					{
						in.nextLine();
						System.out.println("Enter new arrival date (yyyy-mm-dd): ");
						String newArrival = in.nextLine();
						System.out.println("Enter new departure date (yyyy-mm-dd): ");
						String newDeparture = in.nextLine();
						System.out.println("Enter a new room number: ");
						int roomNum = in.nextInt();
						connection.updateReservation(Date.valueOf(newArrival), Date.valueOf(newDeparture), roomNum, resNum);
						System.out.println();
						getGuestReservations();
						System.out.println();
					}
			    }
			}
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to update a reservation: " + e);
		}
	}
}