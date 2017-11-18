import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.mysql.jdbc.UpdatableResultSet;


public class StartProject {
	
	static DatabaseConnection connection = new DatabaseConnection();

	/**
	 * starts the program
	 * @param args
	 */
	public static void main(String args[])
	{
		
		int result = readInput();
		while(result != 5)
		{
			switch(result)
			{
			case 0: removeEmployee();
				break;
			//add other options
			case 1 : System.out.println("Need To Implement this feature");
				break;
			case 2 : System.out.println("Need To Implement this feature");
				break;
			case 3 : System.out.println("Need To Implement this feature");
				break;
			case 4 : System.out.println("Need To Implement this feature");
				break;
			case 5 : System.out.println("Need To Implement this feature");
				break;
			case 6: getAverageCostPerRoomType();
				break;
			case 7: getSalaryPerPosition();
				break;
			case 8: getCleaningSchedules();
				break;
			case 9: getGuestsWithNoReservation();
				break;
			case 10: updateCleaningSchedule();
				break;
			default: System.out.println("Invalid entry. Please choose one of the provided numbers");
				break;
			}
			result = readInput();
		}
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
				+ "(10) Update Cleaning Schedule\n");
		int result = in.nextInt();
		return result;
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
						System.out.println("{ID: " + rs.getInt(1) + ", Name: " + rs.getString(2) + "}");
					}
					System.out.println("Choose the ID of the employee you want to delete:");
					ID = in.nextInt();
					
				}	
				else
				{
					rs.next();
					System.out.println("ID: " + rs.getInt(1) + ", Name: " + rs.getString(2));
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
			System.out.println("The average for a " + size + " sized room is: " + connection.getAverageForRoomType(size) + "\n\n\n\n\n");
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
	 * gets all the current users who do not have a reservation set up
	 */
	public static void getGuestsWithNoReservation()
	{
		try
		{
			ResultSet results = connection.getGuestsWithNoReservations();
			while(results.next())
			{
				System.out.println("{UserID: " + results.getInt(1) + ", Name: " + results.getString(2) + ", Address: " 
						+ results.getString(3) + ", Phone Number: " + results.getString(4) + ", Email: " + results.getString(5) + 
						", Room Number: " + results.getInt(6) + ", Number of Keys: " + results.getInt(7) + "}" );
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
			System.out.println("Please enter a Room Number you would like to be updated");
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
	 * creates a reservation
	 */
	public static void createReservation()
	{
		//have not implemented yet
	}


}
