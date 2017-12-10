import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.jws.soap.SOAPBinding.Use;

public class RunEmployee {

	DatabaseConnection connection;
	public boolean quit = false;
	
	public RunEmployee()
	{
		connection = new DatabaseConnection("employee");
	}
	
	/**
	 * the main method of the Employee side
	 */
	public void runProgram()
	{
		boolean choseValidNumber = false;
		while (!choseValidNumber && !quit)
		{
			int input = readInput("");
			switch(input)
			{	
				case 1:		//Chose to log in 
					logIn();
					choseValidNumber = true;
					break;
				
				case 2:		//Chose to quit the program 
					quit = true;
					System.out.println("Thank you for visiting! :)");
					return;
				
				default: 	//Chose a number that is not one of the above choices
					System.out.println("Please try again with a valid number choice");
					break;
			}
		}
		
		choseValidNumber = false; //reset the boolean to be false
		
		while(!choseValidNumber && !quit)
		{
			int input = readInput(UserInformation.name);
			if(UserInformation.type.equals("cleaning staff"))
			{
				switch(input)
				{
					case 1:		//Chose to display the rooms that this employee is responsible for 
						getCleaningSchedulesForEmployee();
						break;
					
					case 2: 	//Chose to update one of their rooms 
						updateCleaningScheduleForEmployee();
						break;					
					
					case 3:		//Chose to log out
						choseValidNumber = true;
						break;
					
					default: 	//Chose a number that was not a valid choice
						System.out.println("Please try again with a valid number choice");
						break;
				}
			}
			else if (UserInformation.type.equals("receptionist"))
			{
				switch(input)
				{
					case 1:	reserveRoom();
						break;
					
					case 2: 	//Chose to cancel a guests reservation
						cancelReservation();	
						break;
					
					case 3: 	//Chose to update a guests reservation
						updateReservation();
						break;
					
					case 4: 	//Chose to display a guests reservations
						getGuestReservations();
						break;
					case 5:		//Chose to display a list of all the rooms along with their availability 
						listAllRooms();  																									
						break;
					
					case 6: 	//Chose to update the availability of a room
						updateRoomAvailability();									
						break;
					
					case 7:		//Chose to quit the program
						choseValidNumber = true;
						break;
					
					default:	//Chose a number that was not a valid choice 
						System.out.println("Please try again with a valid number choice");
						break;
				}
			}
			else if (UserInformation.type.equals("manager"))
			{

				switch(input)
				{
					case 1:		//Chose to create a new employee 
						addEmployee();
						break;
					
					case 2:		//Chose to remove an employee
						removeEmployee();
						break;
					
					case 3: 	//Chose to create a guest account
						createGuestAccount();
						break;
					
					case 4: 	//Chose to remove a guest account
						deleteGuestAccount();
						break;
					
					case 5: 	//Chose to display the salaries of a type of employee
						getSalaryPerPosition();
						break;
					
					case 6: 	//Chose to display the cleaning schedule information
						getCleaningSchedules();
						break;
					
					case 7: 	//Chose to display all the guests who do not have a reservation
						getGuestsWithNoReservation();
						break;
					
					case 8:		//Chose to display all the employees who have a higher pay than other people in their position
						getEmployeesWithHigherAveragePay();
						break;
					
					case 9:		//Chose to display the cleaning staff employees who have the least number of rooms to clean
						getCleanersWithLeastRooms();
						break;
					
					case 10:	//Chose to display all the rooms what are not cleaned and are not listed to be cleaned
						getDirtyUnassignedRooms();
						break;
					
					case 11: 	//Chose to assign an employee to clean a room
						assignEmployeeToCleanRoom();
						break;
					case 12:	//Chose to log out
						choseValidNumber = true;
						break;
					
					default:	//Chose a number that was not a valid choice 
						System.out.println("Please try again with a valid number choice");
						break;
				}
			}
		}
		
	}

	/**
	 * displays the choices to the employee
	 * @param name takes the name of the employee
	 * @return returns the selection that the employee chose
	 */
	public int readInput(String name)
	{
		Scanner in = new Scanner(System.in);
		boolean worked = false;
		while (!worked && !quit)
		{
			if(name.equals(""))
			{
				System.out.println("Welcome to the Employee Side of the Hotel Reservation System!\n"
						+ "Which operation would you like to perform?\n"
						+ "(1) Log In\n"
						+ "(2) Quit Program");
			}
			else
			{
				System.out.print("Hello " + name +"!\n"
						+ "What would you like to do?\n");
				
				if (UserInformation.type.equals("cleaning staff"))
				{
					System.out.println(
						  "(1) Display all the rooms you have to clean\n"
						+ "(2) Change status of one of your rooms\n"
						+ "(3) Log Out");
				}
				else if (UserInformation.type.equals("receptionist"))
				{
					System.out.println(
							  "(1) Create a reservation\n"
							+ "(2) Delete a reservation\n"
							+ "(3) Update a reservation\n"
							+ "(4) Display all reservations for a specific guest\n"
							+ "(5) Display a list of all the rooms along with their availability\n"
							+ "(6) Update the availability of a room\n"
							+ "(7) Log Out");
				}
				else if (UserInformation.type.equals("manager"))
				{
					System.out.println(
							  "(1) Create a new employee\n"
							+ "(2) Remove an employee\n"
							+ "(3) Create a new guest account\n"
							+ "(4) Remove a guest account\n"
							+ "(5) Display salaries (average, minumum, or maximum) for each type of employee\n"
							+ "(6) Display all cleaning schedule information\n"
							+ "(7) List of all the guests who do not currently have a reservation\n"
							+ "(8) Display all employees who have a higher pay than the average pay for their position\n"
							+ "(9) Display all the employees from the cleaning staff that have the least number of rooms to clean\n"
							+ "(10) Display a list of all rooms that are not clean and are not currently listed to be cleaned\n"
							+ "(11) Assign an employee to clean a room\n"
							+ "(12) Log Out");
				}
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
				return -1;
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
			while(!foundUsername)
			{
				try
				{
					System.out.println("What is your email? ");
					String username = input.nextLine();
					if(username.toLowerCase().equals("q"))
					{
						quit = true;
						System.out.println("\nThank you for visiting! :) ");
						break;
					}
					result = connection.sendQuery("select * from employees where email = '" + username + "';");
					if (!result.next())
					{
						System.out.println("This user does not exist. Please make sure you have the correct username. Please (q) to quit.");
					}
					else
					{
						passInDB = result.getString(8);
						ID = "" + result.getInt(1);
						foundUsername = true;
					}
				}
				catch (Exception e)
				{
					System.out.println("There was an error: " + e);
					foundUsername = true;
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
						UserInformation.type = result.getString(10);
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
	 *
	 */
	public void assignEmployeeToCleanRoom()
	{
		try
		{
			Scanner in = new Scanner(System.in);
			System.out.println("Please provide the employee ID of the employee who will be cleaning the room: ");
			int eId = in.nextInt();
			System.out.println("Please provide the room number of the room that is to be cleaned: ");
			int rNum = in.nextInt();
			connection.assignEmployeeToCleanRoom(eId,rNum);
		}
		catch (Exception e)
		{
			System.out.println("Failed to assign the employee to clean the room: " + e);
		}
	}
	
	/**
	 * updates a Cleaning Schedule tuple based on the room number
	 */
	public void updateCleaningScheduleForEmployee()
	{
		try
		{
			Scanner in = new Scanner(System.in);
			ArrayList<Integer> rooms = getCleaningSchedulesForEmployee();
			if(!rooms.isEmpty())
			{
				System.out.print("Please enter a Room Number you would like to be updated: ");
				int room = in.nextInt();
				while (!rooms.contains(room))
				{
					System.out.println("Invalid Entry. Please enter a valid Room Number you would like to be updated");
					room = in.nextInt();
				}
				System.out.println("Set Cleaned to true or false for Room Number " + room + "?");
				boolean cleaned = in.nextBoolean();
				connection.updateCleaningSchedule(cleaned, room);
				getCleaningSchedulesForEmployee();
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to display the changes made in Cleaning Schedule: " + e);
		}
	}
	/**
	 * Prints the employees who have higher than average pay
	 */
	public void getEmployeesWithHigherAveragePay()
	{
		System.out.println("Here are the employees with higher than average pay: ");
		ResultSet resultSet = connection.getEmployeesWithHigherThanAveragePay();
		try
		{
			System.out.println("#\t\tName\t\tSalary\t\tAverage Salary\t\tPosition");
			while(resultSet.next()){
				System.out.println(resultSet.getInt(1)+ "\t\t"+resultSet.getString(2) + "\t\t$" +
						resultSet.getInt(4)+ "\t\t$"+resultSet.getInt(5)+ "\t\t"+resultSet.getString(6));
			}
		}
		catch (Exception e)
		{
			System.out.println("error when trying to find the employees with higher than average pay: "+e);
		}
	}
	/**
	 * Prints the room numbers of the rooms that are dirty but not assigned for cleaning
	 */
	public void getDirtyUnassignedRooms ()
	{
		System.out.println("here are the room numbers of the rooms that are dirty but not assigned for cleaning: ");
		ResultSet resultSet = connection.getDirtyUnassignedRooms();
		try
		{
			System.out.println("#");
			while (resultSet.next())
			{
				System.out.println(resultSet.getInt(1));
			}
		}
		catch (Exception e)
		{
			System.out.println("an error occurred when trying to get the room numbers of the rooms that are dirty but not assigned for cleaning: "+ e);
		}
	}
	/**
	 * Prints the cleaners who have the least amount of rooms to clean
	 */
	public void getCleanersWithLeastRooms ()
	{
		System.out.println("here are the cleaners with the least amount of rooms to clean: ");
		ResultSet resultSet = connection.getCleanerWithLeastRooms();
		try
		{
			System.out.println("#\t\troomsLeft");
			while (resultSet.next())
			{
				System.out.println(resultSet.getInt(1)+ "\t\t"+resultSet.getInt(2));
			}
		}
		catch (Exception e)
		{
			System.out.println("error when trying to find the cleaners with the least amount of rooms to clean: "+ e);
		}
	}
	/**
	 * Deletes a guest account based on the email provided
	 */
	public void deleteGuestAccount()
	{
		try
		{
			Scanner in = new Scanner (System.in);
			System.out.println ("What is the email of the guest whose account you want to delete?");
			String email = in.nextLine();
			connection.deleteGuestAccount(email);
			System.out.println("Successfully deleted guest account.");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to delete the guest account" + e);
		}
	}
	
	/**
	 * gets all the cleaning schedules
	 * @return returns a list of all the rooms in these schedules
	 */
	public ArrayList<Integer> getCleaningSchedules()
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
	 * gets all the cleaning schedules
	 * @return returns a list of all the rooms in these schedules
	 */
	public ArrayList<Integer> getCleaningSchedulesForEmployee()
	{
		try 
		{
			ArrayList<Integer> roomNumbers = new ArrayList<>();
			ResultSet results = connection.sendQuery("select * from cleaningschedule where employeeID = "  + UserInformation.ID + " and Cleaned = false;");
			while(results.next())
			{
				System.out.println("{CleaningID: " + results.getInt(1) + ", EmployeeID: " + results.getInt(2) + ", Room Number: " + results.getInt(3) + ", Cleaned: " + results.getBoolean(4)+"}");
				roomNumbers.add(results.getInt(3));
			}
			if(roomNumbers.isEmpty())
			{
				System.out.println("There are no rooms for you to currently clean.");
			}
			return roomNumbers;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to display the cleaning schedule: " + e);
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

		try {
			System.out.println("Room Number"+ "\t"+ "Price");
			while (result.next()) {
				System.out.println(result.getInt(1) +"\t\t$"+result.getInt(2));
			}
		}
		catch (Exception exception)
		{
			System.out.println("an error occurred when trying to print the list of available rooms: "+ exception);
		}
		
		
		System.out.println("Enter the guest's email:");
		String guestEmail = in.nextLine();
		System.out.println("Enter the room number:");
		String roomNumber = in.nextLine();
		System.out.println("How many keys will the guest receive?");
		String numberOfKeys = in.nextLine();
		try
		{
			connection.reserveRoom(Date.valueOf(arrivalDate),Date.valueOf(departureDate),guestEmail,roomNumber,numberOfKeys);
			System.out.println("Created the reservation!");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to book a room. May be using an email that does not exist: " + e);
		}
		System.out.println("Total cost for this stay will be: $" + getReservationCost(roomNumber, arrivalDate, departureDate) + "\n");

	}
	
	public void listAllRooms()
	{
		System.out.println("All rooms and their current status:");
		ResultSet resultSet = connection.listRooms();
		try
		{
			System.out.println("#\t\tPrice\t\tBeds\t\tType\t\tAvailable?\tClean?");
			while(resultSet.next())
			{
				System.out.print(resultSet.getInt(1)+"\t\t"+resultSet.getInt(2)+"\t\t"
						+ resultSet.getInt(3)+"\t\t"+ resultSet.getString(4)+"\t\t");
				if(resultSet.getInt(5) == 1)
				{
					System.out.print("Yes\t\t");
				}
				else
				{
					System.out.print("No\t\t");
				}
				if(resultSet.getInt(6) == 1)
				{
					System.out.println("Yes");
				}
				else
				{
					System.out.println("No");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to view the available rooms: " + e);
		}

	}

	/**
	 * Adds a Employee account
	 */
	public void addEmployee ()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter first and last name:");
		String name = in.nextLine();
		System.out.println("Enter home address:");
		String address = in.nextLine();
		System.out.println("Enter phone number:");
		String phone = in.nextLine();
		System.out.println("Enter email:");
		String email = in.nextLine();
		System.out.println("Enter birthday (yyyy-mm-dd):");
		String birthday = in.nextLine();
		String position = "";
		while (true)
		{
			System.out.println("Enter position (cleaning staff, receptionist, or manager):");
			position = in.nextLine().toLowerCase();
			if(position.equals("cleaning staff") || position.equals("receptionist") || position.equals("manager"))
			{
				break;
			}
			else
			{
				System.out.println("Please select a position from the ones listed");
			}
		}
		System.out.print("Enter salary: \n$");
		Integer salary = in.nextInt();
		in = new Scanner(System.in);
		System.out.println("Enter this employee's password: ");
		String password = in.next();
		
		try
		{
			connection.addEmployee(name, address, phone, email, salary, Date.valueOf(birthday), position, password);
			getEmployees();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to create a employee account " + e);
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
	    catch (SQLException e) {
			e.printStackTrace();
		}
	    cost = cost * num.getDays();
		return cost;
		
	}
	
	/**
	 * gets all the employees
	 * @return returns a list of all the employees
	 */
	public ArrayList<String> getEmployees()
	{
		try 
		{
			ArrayList<String> employeeId = new ArrayList<>();
			ResultSet results = connection.getEmployees();
			while(results.next())
			{
				System.out.println("{Employee ID: " + results.getInt(1) + ", Name: " + results.getString(2) + " " + results.getString(3) + ", Address: " + results.getString(4) + ", Phone Number: " + results.getString(5) + ", Email: "+ results.getString(6) + 
						", Salary: " + results.getInt(7) + ", Birthday: " + results.getDate(9) + ", Position: " + results.getString(10)+ "}");
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
	 * removes an employee from the table based on their name or their ID
	 */
	public void removeEmployee()
	{
		//create database connection class with all the host name, the username, and password
		
		//create methods in this class that help you perform some query
		Scanner in = new Scanner(System.in);
		System.out.println("Enter an an Employee's name or EmployeeID: ");
		getEmployees();
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
	 * Creates a new Guest account
	 */
	public void createGuestAccount()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter first name:");
		String first = in.nextLine();
		System.out.println("Enter last name:");
		String last = in.nextLine();
		System.out.println("Enter home address:");
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
			getGuests();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to create a guest account. This email may already be in use.");
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
	 * gets the salary for each position of employees
	 */
	public void getSalaryPerPosition()
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
						System.out.println("The " + type + " salary for a " + position + " is $" + result + "\n" );
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
	 * gets all the current users who do not have a reservation set up
	 */
	public void getGuestsWithNoReservation()
	{
		try
		{
			ResultSet results = connection.getGuestsWithNoReservations();
			while(results.next())
			{
				System.out.println("{Email: " + results.getString(1) + ", Name: " + results.getString(2) + " " + results.getString(3) + ", Address: " 
						+ results.getString(4) + ", Phone Number: " + results.getString(5) + "}" );
			}
		}
		catch(Exception e)
		{
			System.out.println("Error occurred with displaying the information for guests who do not currently have a reservation: " + e);
		}
	}
	
	/**
	 * gets all the current users who do not have a reservation set up
	 */
	public void cancelReservation()
	{
		Scanner in = new Scanner (System.in);
		System.out.println("Enter guest email to cancel reservation:");
		String email = in.nextLine();
		System.out.println("Enter guest arrival date (YYYY-MM-DD) for reservation to cancel:");
		String date = in.nextLine();
		try
		{
			connection.cancelReservation(email, Date.valueOf(date));
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to cancel a reservation: " + e);
		}

	}
	
	/**
	 * updates the availability of a room
	 */
	public void updateRoomAvailability()
	{
		try
		{
			listAllRooms();
			Scanner in = new Scanner(System.in);
			ArrayList<Integer> rooms = getRooms();
			System.out.print("Please enter a Room Number you would like to be updated: ");
			int room = in.nextInt();
			while (!rooms.contains(room))
			{
				System.out.println("Invalid Entry. Please enter a valid Room Number you would like to be updated");
				room = in.nextInt();
			}
			System.out.println("Set the Availability attibute to true or false for Room Number " + room + "?");
			boolean available = in.nextBoolean();
			connection.updateRoomAvailability(available, room);
			System.out.println();
			listAllRooms();
			System.out.println();
			
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to display the changes made in Cleaning Schedule: " + e);
		}
		
	}
	
	/**
	 * updates a reservation
	 */
	public void updateReservation()
	{
		try
		{
			String email = UserInformation.ID;
			ResultSet results = connection.getReservations();
			while(results.next())
			{
				System.out.println("{Reservation Number: " + results.getInt(1) + ", ArrivalDate: " + results.getString(2) + ", DepartureDate: " + results.getString(3) + ", User Email: " + results.getString(4)+ ", RoomNumber: " + results.getInt(5)+"}");
			}
			System.out.println();
			Scanner in = new Scanner(System.in);
			System.out.println("Please enter a Reservation Number you would like to be updated");
			int resNum = in.nextInt();
			in = new Scanner(System.in);
			System.out.println("Enter an arrival date (yyyy-mm-dd): ");
			String newArrival = in.nextLine();
			System.out.println("Enter a departure date (yyyy-mm-dd): ");
			String newDeparture = in.nextLine();
			System.out.println("Enter a room number: ");
			int roomNum = in.nextInt();
			connection.updateReservation(Date.valueOf(newArrival), Date.valueOf(newDeparture), roomNum, resNum);
			System.out.println();
			System.out.println();
				
		}
		catch(Exception e)
		{
			System.out.println("Error in trying to update a reservation: " + e);
		}
	}
	
	/**
	 * gets all the current guests reservations
	 */
	public void getGuestReservations()
	{
		try 
		{
			Scanner in = new Scanner(System.in);
			System.out.println("Enter guests email to view their reservations: ");
			String email = in.nextLine();
			ResultSet results = connection.getGuestReservations(email);
			if (!results.next()) System.out.println("That guest does not have any active reservations");
			results.beforeFirst();
			while(results.next())
			{
				System.out.println("{Reservation ID: " + results.getInt(1) + ", Arrival Date: " + results.getString(2) + ", Departure Date: " + results.getString(3) + ", Room Number: " + results.getInt(5)+"}");
			}
			System.out.println();
		}
			catch(Exception e)
			{
				System.out.println("An error has occurred when trying display guests reservations" + e);
			}
	}
	
	/**
	 * gets all the the rooms
	 */
	public ArrayList<Integer> getRooms()
	{
		try 
		{
			ArrayList<Integer> rooms = new ArrayList<>();
			ResultSet results = connection.listRooms();
			if (!results.next()) System.out.println("There are no rooms");
			while(results.next())
			{
				rooms.add(results.getInt(1));
			}
			System.out.println();
			return rooms;
		}
			catch(Exception e)
			{
				System.out.println("An error has occurred when trying display the rooms" + e);
			}
		return null;
	}
}
