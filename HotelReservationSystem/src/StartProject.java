import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;  
import java.sql.Date;

import com.mysql.jdbc.UpdatableResultSet;


public class StartProject {
	
	static DatabaseConnection connection = new DatabaseConnection("other");

	/**
	 * starts the program
	 * @param args
	 */
	public static void main(String args[])
	{
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter Database username: ");
		String username = input.nextLine();
		System.out.print("Enter Database password: ");
		String password = input.nextLine();
		UserInformation.databaseUsername = username;
		UserInformation.databasePassword = password;
		System.out.println("Welcome to the Max(*) Hotel Reservation System\nPlease choose an option from below:\n(1) Enter as a Guest\n(2) Enter as an Employee");
		int choice = input.nextInt();
		if(choice == 1)
		{
			RunUser runAsGuest = new RunUser();
			runAsGuest.runProgram();
		}
		else
		{
			RunEmployee runAsEmployee = new RunEmployee();
			runAsEmployee.runProgram();
		}
		
//		int result = readInput();
//		while(result != 5)
//		{
//			switch(result)
//			{
//			case 0: removeEmployee();
//				break;
//			//add other options
//			case 1 : createAccount();
//				break;
//			case 2 : System.out.println("Need To Implement this feature");
//				break;
//			case 3 : listAvailableRooms();
//				break;
//			case 4 : reserveRoom();
//				break;
//			case 5 : System.out.println("Need To Implement this feature");
//				break;
//			case 6: getAverageCostPerRoomType();
//				break;
//			case 7: getSalaryPerPosition();
//				break;
//			case 8: getCleaningSchedules();
//				break;
//			case 9: getGuestsWithNoReservation();
//				break;
//			case 10: updateCleaningSchedule();
//				break;
//			case 11: createNewCleaningEntry();
//				break;
//			default: System.out.println("Invalid entry. Please choose one of the provided numbers");
//				break;
//			}
//			result = readInput();
//		}
	}

	/**
	 * displays all the possible choices for the user to select
	 * @return the number that the user selects
	 */
	public static int readInput()
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Welcome to the Hotel Reservation System!\n"
				+ "Which operation would you like to perform?\n"
				+ "(0) Delete Employee\n"
				+ "(1) Create a Account\n"
				+ "(2) Login to Account\n"
				+ "(3) List of all available rooms\n"
				+ "(4) Make Reservation\n"
				+ "(5) Quit program\n"
				+ "(6) Get the Average Cost per Room Type\n"
				+ "(7) Get Salary per Position\n"
				+ "(8) Get the Cleaning Schedule for All Employees\n"
				+ "(9) Get the Guests Who Do Not Currently Have A Reservation\n"
				+ "(10) Update Cleaning Schedule\n"
				+ "(11) Create a New Cleaning Entry");
		int result = in.nextInt();
		return result;
	}


	public static void listAvailableRooms ()
	{
		System.out.println("Available Rooms: ");
		ResultSet resultSet = connection.listAvailableRooms();
		try
		{
			System.out.println("#      Price     Beds     Type     Clean");
			while(resultSet.next()){
			//	System.out.println(resultSet.getInt(1));
				System.out.println(resultSet.getInt(1)+"     "+resultSet.getInt(2)+"     "
						+ resultSet.getInt(3)+"     "+ resultSet.getString(4)+"     "+ resultSet.getInt(5)+"     "+resultSet.getInt(6));
			}
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to view the available rooms: " + e);
		}

	}
	
	/**
	 * Creates a Guest or Employee account
	 */
	public static void createAccount ()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Which account would you like to create?\n" 
		+ "(0) Guest\n"
		+ "(1) Employee");
		Integer result = in.nextInt();
		if(result == 0){
			addGuest();
		}else addEmployee();
		}
	
	/**
	 * Adds a Guest account
	 */
	public static void addGuest ()
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
		try
		{
			connection.addGuest(first, last, address, phone, email, password);
			getGuests();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to create a guest account " + e);
		}
	}

	/**
	 * Adds a Employee account
	 */
	public static void addEmployee ()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter name:");
		String name = in.nextLine();
		System.out.println("Enter address:");
		String address = in.nextLine();
		System.out.println("Enter phone number:");
		String phone = in.nextLine();
		System.out.println("Enter email:");
		String email = in.nextLine();
		System.out.println("Enter birthday (yyyy-mm-dd):");
		String birthday = in.nextLine();
		System.out.println("Enter position:");
		String position = in.nextLine();
		System.out.println("Enter salary:");
		Integer salary = in.nextInt();
		try
		{
			//connection.addEmployee(name, address, phone, email, salary, Date.valueOf(birthday), position);
			getEmployees();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to create a employee account " + e);
		}
	}
	
	/**
	 * Reserves a room for the given guest
	 */
	public static void reserveRoom ()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter the arrival date (yyyy-mm-dd):");
		String arrivalDate = in.nextLine();
		System.out.println("Enter the departure date:(yyyy-mm-dd)");
		String departureDate = in.nextLine();
		System.out.println("Enter the guest email:");
		String guestEmail = in.nextLine();
		System.out.println("Enter the room number:");
		String roomNumber = in.nextLine();
		System.out.println("How many keys will the guest receive?");
		String numberOfKeys = in.nextLine();
		try
		{
			connection.reserveRoom(Date.valueOf(arrivalDate),Date.valueOf(departureDate),guestEmail,roomNumber,numberOfKeys);
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to book a room: " + e);
		}

	}

	/**
	 * removes an employee from the table based on their name or their ID
	 */
	public static void removeEmployee()
	{
		//create database connection class with all the host name, the username, and password
		
		//create methods in this class that help you perform some query
		Scanner in = new Scanner(System.in);
		System.out.print("Enter an an Employee's name or EmployeeID: ");
		try
		{
			int employeeID = in.nextInt();
			connection.removeEmployee(employeeID);
			
		}
		catch(InputMismatchException e)
		{
			String employeeName = in.nextLine();
			ResultSet rs = connection.findEmployeeByName(employeeName);
			int size = 0;
			int ID = 0;
			try 
			{
				if(rs.last())
				{
					size = rs.getRow();
				}
				rs.beforeFirst();
				if (size > 1)
				{
					System.out.println("Multiple Employees came up with that name:");
					while(rs.next())
					{
						System.out.println("{ID: " + rs.getInt(1) + ", Name: " + rs.getString(2) + " " + rs.getString(3) + "}");
					}
					System.out.println("Choose the ID of the employee you want to delete:");
					ID = in.nextInt();
					
				}	
				else
				{
					rs.next();
					System.out.println("ID: " + rs.getInt(1) + ", Name: " + rs.getString(2) + " " + rs.getString(3));
					ID = rs.getInt(1);
				}
				
				connection.removeEmployee(ID);
			}
			catch(Exception err)
			{
				System.out.println("An error occurred while retrieving the names of the employees: " + err);
			}
		}
		
	}

	/**
	 * gets the average cost per type of room
	 */
	public static void getAverageCostPerRoomType()
	{
		String size = "";
		try
		{
			Scanner in = new Scanner(System.in);
			System.out.println("What type of room do you want to find the average of (Small, Medium, or Large)?");
			size = in.nextLine();
			System.out.println("The average for a " + size + " sized room is: $" + connection.getAverageForRoomType(size) + "\n\n\n\n\n");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred while trying to display the average for a " + size + " room: " + e);
		}
	}

	/**
	 * gets the salary for each position of employees
	 */
	public static void getSalaryPerPosition()
	{
		Scanner in = new Scanner(System.in);
		boolean foundPosition = false;
		boolean foundType = false;
		ArrayList<String> positions = connection.getPositions();
		while (!foundType)
		{
			System.out.println("Do you want to find the maximum, minimum, or average salary?");
			String type = in.nextLine().toLowerCase();
			if(type.contains("avg") || type.contains("average") || type.contains("max")|| type.contains("min"))
			{
				foundType = true;
				while (!foundPosition)
				{
					System.out.println("Here is a list of all the positions that are currently stored:");
					for (String position : positions)
					{
						System.out.println(position);
					}
					System.out.println("Please choose a position that you would like to find the " + type + " salary for: ");
					String position = in.nextLine().toLowerCase();
					if(positions.contains(position))
					{
						int result = connection.getTypeOfSalary(position, type);
						foundPosition = true;
						System.out.println("The " + type + " salary for a " + position + " is $" + result + "\n\n\n\n\n" );
					}
					else
					{
						System.out.println("Invalid Entry. Please choose one of the positions listed");
					}
				}
				
			}
			else
			{
				System.out.println("Invalid Entry. Please enter one of the following choices");
			}
		}
		
		
	}
	
	/**
	 * gets all the cleaning schedules
	 * @return returns a list of all the rooms in these schedules
	 */
	public static ArrayList<Integer> getReservations()
	{
		try 
		{
			ArrayList<Integer> reservations = new ArrayList<>();
			ResultSet results = connection.getReservations();
			while(results.next())
			{
				System.out.println("{rID: " + results.getInt(1) + ", ArrivalDate: " + results.getInt(2) + ", DepartureDate: " + results.getInt(3) + ", UserID: " + results.getBoolean(4)+ ", RoomNumber: " + results.getInt(5)+"}");
				reservations.add(results.getInt(1));
			}
			System.out.println();
			return reservations;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to display the Reservation " + e);
		}
		return null;
	}
	
	/**
	 * gets all the cleaning schedules
	 * @return returns a list of all the rooms in these schedules
	 */
	public static ArrayList<Integer> getCleaningSchedules()
	{
		try 
		{
			ArrayList<Integer> roomNumbers = new ArrayList<>();
			ResultSet results = connection.getCleaningSchedule();
			while(results.next())
			{
				System.out.println("{CleaningID: " + results.getInt(1) + ", EmployeeID: " + results.getInt(2) + ", Room Number: " + results.getInt(3) + ", Cleaned: " + results.getBoolean(4)+"}");
				roomNumbers.add(results.getInt(3));
			}
			System.out.println();
			return roomNumbers;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to display the cleaning schedule: " + e);
		}
		return null;
	}
	
	/**
	 * gets all the guests
	 * @return returns a list of all the guests
	 */
	public static ArrayList<String> getGuests()
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
	 * gets all the employees
	 * @return returns a list of all the employees
	 */
	public static ArrayList<String> getEmployees()
	{
		try 
		{
			ArrayList<String> employeeId = new ArrayList<>();
			ResultSet results = connection.getEmployees();
			while(results.next())
			{
				System.out.println("{Employee ID: " + results.getInt(1) + ", Name: " + results.getString(2) + ", Address: " + results.getString(3) + ", Phone Number: " + results.getString(4) + ", Email: "+ results.getString(5) + 
						", Salary: " + results.getInt(6) + ", Birthday: " + results.getDate(7) + ", Position: " + results.getString(8)+ "}");
				employeeId.add(results.getString(1));
			}
			System.out.println();
			return employeeId;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to display the employees: " + e);
		}
		return null;
	}
	
	/**
	 * gets all the current users who do not have a reservation set up
	 */
	public static void getGuestsWithNoReservation()
	{
		try
		{
			ResultSet results = connection.getGuestsWithNoReservations();
			while(results.next())
			{
				System.out.println("{Email: " + results.getInt(1) + ", Name: " + results.getString(2) + " " + results.getString(3) + ", Address: " 
						+ results.getString(4) + ", Phone Number: " + results.getString(5) + "}" );
			}
			System.out.println("\n\n\n\n\n");
		}
		catch(Exception e)
		{
			System.out.println("Error occurred with displaying the information for guests who do not currently have a reservation");
		}
	}

	/**
	 * updates a Cleaning Schedule tuple based on the room number
	 */
	public static void updateCleaningSchedule()
	{
		try
		{
			Scanner in = new Scanner(System.in);
			ArrayList<Integer> rooms = getCleaningSchedules();
			System.out.print("Please enter a Room Number you would like to be updated: ");
			int room = in.nextInt();
			while (!rooms.contains(room))
			{
				System.out.println("Invalid Entry. Please enter a valid Room Number you would like to be updated");
				room = in.nextInt();
			}
			System.out.println("Set the Cleaned attibute to true or false for Room Number " + room + "?");
			boolean cleaned = in.nextBoolean();
			connection.updateCleaningSchedule(cleaned, room);
			System.out.println();
			getCleaningSchedules();
			System.out.println();
			
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to display the changes made in Cleaning Schedule: " + e);
		}
	}
	
	/**
	 * Updates a reservation
	 */
	public static void updateReservation()
	{
		try
		{
			Scanner in = new Scanner(System.in);
			ArrayList<Integer> reservations = getReservations();
			System.out.println("Please enter a Reservation Number you would like to be updated");
			int resNum = in.nextInt();
			while (!reservations.contains(resNum))
			{
				System.out.println("Invalid Entry. Please enter a valid Reservation Number you would like to be updated");
				resNum = in.nextInt();
			}
			System.out.println("Enter an arrival date: ");
			String newArrival = in.nextLine();
			System.out.println("Enter a departure date: ");
			String newDeparture = in.nextLine();
			System.out.println("Enter a room number: ");
			int roomNum = in.nextInt();
			connection.updateReservation(Date.valueOf(newArrival), Date.valueOf(newDeparture), roomNum, resNum);;
			System.out.println();
			getReservations();
			System.out.println();
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to display the changes made in reservations: " + e);
		}
	}

	/**
	 * creates a new entry for the cleaning schedule
	 */
	public static void createNewCleaningEntry()
	{
		try
		{
			Scanner in = new Scanner(System.in);
			
			//create an array list of all the employee IDs
			ArrayList<Integer> employeeIDs = new ArrayList<>();
			
			//create an array list of all the rooms numbers that are currently dirty
			ArrayList<Integer> roomNumbers = new ArrayList<>();
			
			//grab a list of all the Employees
			ResultSet employeeResults = connection.getAllEmployees();
			
			//Display all the Employees
			System.out.println("Here is a list of all the Employees:");
			while(employeeResults.next())
			{
				employeeIDs.add(employeeResults.getInt(1));
				System.out.println("{EmployeeID: " + employeeResults.getInt(1)+ ", Name: " + employeeResults.getString(2) + " " + employeeResults.getString(3) + ", Address: " 
						+ employeeResults.getString(4) + ", Phone Number: " + employeeResults.getString(5)+ ", Email: " + employeeResults.getString(6)
						+ ", Salary: " + employeeResults.getInt(7) + ", Birthday: " + employeeResults.getDate(9) + ", Job Title: " + employeeResults.getString(10) + "}");
			}
			
			//Choose an Employee ID
			System.out.print("Choose an EmployeeID from above: ");
			int employeeID = in.nextInt();
			while (!employeeIDs.contains(employeeID))
			{
				System.out.print("There was an error with your input. Please choose a valid EmployeeID that was listed above: ");
				employeeID = in.nextInt();
			}
			
			//Display a list of all the rooms that are dirty
			System.out.println("Here is a list of all the rooms that are currently dirty");
			ResultSet roomResults = connection.getAllDirtyRooms();
			while(roomResults.next())
			{
				roomNumbers.add(roomResults.getInt(1));
				System.out.println("{Room Number: "+ roomResults.getInt(1) +  ", Price: " + roomResults.getInt(2) + ", Beds: " + roomResults.getInt(3) +  ", Room Type: " + roomResults.getString(4) +  ", Available: " + roomResults.getBoolean(5) + ", Clean: " + roomResults.getBoolean(6) + "}");				
			}
			
			//Choose a room number from that list
			System.out.print("Please choose a Room Number from above: ");
			int roomNumber = in.nextInt();
			while(!roomNumbers.contains(roomNumber))
			{
				System.out.println("There was an error with your input. Please choose a valid Room Number that was listed above: ");
				roomNumber = in.nextInt();
			}
			
			//Take the the employeeID and RoomNumber and insert it into the table.
			connection.insertNewCleaningEntry(employeeID, roomNumber);
			System.out.println("Inserting new Cleaning Data!\n");
		}
		catch(Exception e)
		{
			System.out.println("Error in displaying all the employees and creating a new Cleaning Entry: " + e);
		}
		
		
	}
	
}