import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseConnection {
	private Connection con;
	private Statement stmnt;
	
	//create all of the prepared statements 
	private PreparedStatement reserveRoomPstmnt;
	private PreparedStatement cancelReservationPstmnt;
	private PreparedStatement listAvailablePstmnt;
	private PreparedStatement occupancyStatusPstmnt;
	private PreparedStatement loginSchedulePstmnt;
	private PreparedStatement removeEmployeePstmnt;
	private PreparedStatement findEmployeeByNamePstmnt;
	private PreparedStatement getAveragePerRoomPstmnt;
	private PreparedStatement getAverageSalaryPstmnt;
	private PreparedStatement getMaxSalaryPstmnt;
	private PreparedStatement getMinSalaryPstmnt;
	private PreparedStatement getPositionsPstmnt;
	private PreparedStatement getCleaningSchedulePstmnt;
	private PreparedStatement getGuestsWithNoReservationPstmnt;
	private PreparedStatement updateCleaningSchedulePstmnt;
	private PreparedStatement getAllEmployeesPstmnt;
	private PreparedStatement getAllDirtyRoomsPstmnt;
	private PreparedStatement insertNewCleaningEntryPstmnt;


	
	/**
	 * Initalize the prepared statements and creating the connection to the database
	 */
	public DatabaseConnection()
	{
		String host = "jdbc:mysql://localhost:3306/HotelReservationSystem";
		String username = "root";
		String password = "saajan1";
		try{
			con = (Connection) DriverManager.getConnection(host, username, password);
			stmnt = (Statement) con.createStatement();
			
			//Setting up the reserve room prepared statement
			String reserveRoomCommand = "insert into reservation(reservationid, arrivalDate,departureDate, userEmail,roomNumber,numberofkeys) values (0, ?,?,?,?,?)";
			reserveRoomPstmnt = con.prepareStatement(reserveRoomCommand);
			 
			//Setting up the cancel room prepared statement
			String cancelReservationCommand = "Delete from Reservation where UserID = ?;";
			cancelReservationPstmnt = con.prepareStatement (cancelReservationCommand);
			 
			//Setting up the list available rooms prepared statement
			String listAvailableCommand = "Select RoomNumber From Rooms where Available= 1;";
			listAvailablePstmnt = con.prepareStatement(listAvailableCommand);
			 
			//Setting up the occupancy status prepared statement
			String occupancyStatusCommand = "Select RoomNumber, Available from Rooms;";
			occupancyStatusPstmnt = con.prepareStatement (occupancyStatusCommand);
			 
			//Setting up the display employee cleaning schedule prepared statement
			String loginScheduleCommand = "Select RoomNumbers from CleaningSchedule where EmployeeID = ?;";
			loginSchedulePstmnt = con.prepareStatement(loginScheduleCommand);
			
			//setting up the removing the employee prepared statement
			String removeEmployeeCommand = "delete from employees where EmployeeID = ?";
			removeEmployeePstmnt = con.prepareStatement(removeEmployeeCommand);
			
			//setting up the select from employee prepared statement
			String findEmployeeCommand = "SELECT * FROM employees WHERE name = ?;";
			findEmployeeByNamePstmnt = con.prepareStatement(findEmployeeCommand);
			
			//setting up finding average price per type of room prepared statement
			String findAveragePerRoomCommand = "SELECT avg(price) FROM rooms WHERE RoomType = ?;";
			getAveragePerRoomPstmnt = con.prepareStatement(findAveragePerRoomCommand);
			
			//setting up finding the average salary for each position
			String getAverageSalaryCommand = "SELECT avg(Salary) FROM Employees WHERE Position = ?;";
			getAverageSalaryPstmnt = con.prepareStatement(getAverageSalaryCommand);
			
			//setting up finding the max salary for each position
			String getMaxSalaryCommand = "SELECT max(Salary) FROM Employees WHERE Position = ?;";
			getMaxSalaryPstmnt = con.prepareStatement(getMaxSalaryCommand);
			
			//setting up finding the min salary for each position
			String getMinSalaryCommand = "SELECT min(Salary) FROM Employees WHERE Position = ?;";
			getMinSalaryPstmnt = con.prepareStatement(getMinSalaryCommand);
			
			//setting up getting all the positions that are currently being held in the Employee database
			String getPositionsCommand = "Select distinct(position) from Employees";
			getPositionsPstmnt = con.prepareStatement(getPositionsCommand);
			
			//getting all the cleaning schedule information
			String getCleaningScheduleCommand = "Select * from CleaningSchedule;";
			getCleaningSchedulePstmnt = con.prepareStatement(getCleaningScheduleCommand);
			
			//getting all the guests with no reservation currently made
			String getGuestsWithNoReservationsCommand = "Select * from Guests where Guests.UserID not in (Select distinct(UserID) from Reservation);";
			getGuestsWithNoReservationPstmnt = con.prepareStatement(getGuestsWithNoReservationsCommand);
			
			//updating the Cleaning Schedule
			String updateScheduleCommand = "Update CleaningSchedule set cleaned = ? where RoomNumber = ?";
			updateCleaningSchedulePstmnt = con.prepareStatement(updateScheduleCommand);
			
			//get all of the employees
			String getAllEmployeesCommand = "Select * from Employees";
			getAllEmployeesPstmnt = con.prepareStatement(getAllEmployeesCommand);
			
			//get list of all the rooms that are dirty
			String getAllDirtyRoomsCommand = "Select * from Rooms where clean = false";
			getAllDirtyRoomsPstmnt = con.prepareStatement(getAllDirtyRoomsCommand);
			
			//insert new Cleaning data
			String insertNewCleaningCommand = "Insert into CleaningSchedule(EmployeeID, RoomNumber) values (?, ?);";
			insertNewCleaningEntryPstmnt = con.prepareStatement(insertNewCleaningCommand);
			
		}
		catch(Exception e){
			System.out.println("There was an error creating connection to the database. Make sure you set up the username and password correctly: " + e);
		}
		
	}
	
	/**
	 * send a simple, general query to the database
	 * @param query the query that you want to send to the database
	 */
	public void sendQuery(String query)
	{
		try 
		{
			String sqlCommand = query;
			stmnt.execute(sqlCommand);
			System.out.println("Successfully executed the SQL command");
		}
		catch(SQLException err)
		{
			System.out.println("An error has occurred: " + err.getMessage());
		}
		
	}
	
	/**
	 * remove an Employee from the Employee table with this specific EmployeeID
	 * @param employeeID the employeeID that is used to delete the employee from the table
	 */
	public void removeEmployee(int employeeID)
	{
		try
		{
			removeEmployeePstmnt.setInt(1, employeeID);
			removeEmployeePstmnt.executeUpdate();
			System.out.println("Deleted the Employee from the database\n");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to delete an employee: " + e);
		}	
	}
	
	/**
	 * find all the employees that have this name
	 * @param name the name that we are using to search for the employee
	 * @return returning the result set of the names
	 */
	public ResultSet findEmployeeByName(String name)
	{
		try
		{
			findEmployeeByNamePstmnt.setString(1, name);
			ResultSet result = findEmployeeByNamePstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to find an employee by their name: " + e);
		}
		return null;
	}
	
	/**
	 * finds the average of a type of room
	 * @param type the type of room we are looking for (small, medium, or large)
	 * @return returns the average
	 */
	public int getAverageForRoomType(String type)
	{
		type = type.toLowerCase();
		try
		{
			getAveragePerRoomPstmnt.setString(1, type);
			ResultSet result = getAveragePerRoomPstmnt.executeQuery();
			result.next();
			return result.getInt(1);
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to find the average value for each type of room: " + e);
		}
		return 0;
	}

	/**
	 * gets the salary for those employees in that specific position
	 * @param position the position we are looking for
	 * @param type whether we are looking for the average, minimum, or maximum
	 * @return returns the number
	 */
	public int getTypeOfSalary(String position, String type)
	{
		position = position.toLowerCase();
		type = type.toLowerCase();
		try
		{
			ResultSet result = null;
			if(type.contains("average") || type.contains("avg"))
			{
				getAverageSalaryPstmnt.setString(1, position);
				result = getAverageSalaryPstmnt.executeQuery();
			}
			else if(type.contains("max"))
			{
				getMaxSalaryPstmnt.setString(1, position);
				result = getMaxSalaryPstmnt.executeQuery();
			}
			else if(type.contains("min"))
			{
				getMinSalaryPstmnt.setString(1, position);
				result = getMinSalaryPstmnt.executeQuery();
			}
			else
			{
				System.out.println("There was an error with finding the maximum/minimum/average salary for a " + position);
				return 0;
			}
			result.next();
			return result.getInt(1);
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to find the different salaries for " + position + ": " + e);
		}
		return 0;
	}
	
	/**
	 * gets all the positions for the employees that are currently in the database
	 * @return returns an array list of all those positions
	 */
	public ArrayList<String> getPositions()
	{
		ArrayList<String> positions = new ArrayList<>();
		try
		{
			ResultSet result = getPositionsPstmnt.executeQuery();
			while (result.next())
			{
				positions.add(result.getString(1));
			}
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to find all of the positions in the Employee table: " + e);
		}
		return positions;
	}

	/**
	 * returns all of the entries in the Cleaning Schedule table
	 * @return returns the result set of the cleaning schedule table
	 */
	public ResultSet getCleaningSchedule()
	{
		try
		{
			ResultSet results = getCleaningSchedulePstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error retrieving the cleaning schedule");
		}
		return null;
	}
	
	/**
	 * gets all the guests who do not currently have a reservation
	 * @return returns the result set of all the guests
	 */
	public ResultSet getGuestsWithNoReservations()
	{
		try
		{
			ResultSet result = getGuestsWithNoReservationPstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("Error with trying to get guests who do not have any reservations");
		}
		return null;
	}

	/**
	 * update the cleaning schedule cleaned value
	 * @param newValue the new value we are updating it to
	 * @param roomNumber the room number we are updating
	 */
	public void updateCleaningSchedule(boolean newValue, int roomNumber)
	{
		try
		{
			updateCleaningSchedulePstmnt.setBoolean(1, newValue);
			updateCleaningSchedulePstmnt.setInt(2,  roomNumber);
			updateCleaningSchedulePstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error occurred when trying to update the Cleaning Schedule: " + e);
		}
		
	}

	/**
	 * gets all of the employees from the database
	 * @return return the employees in a result set
	 */
	public ResultSet getAllEmployees()
	{
		try
		{
			ResultSet results = getAllEmployeesPstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error in getting all the employees from the database: " + e);
		}
		return null;
	}

	/**
	 * gets all rooms that are dirty
	 * @return return the rooms in a result set
	 */
	public ResultSet getAllDirtyRooms()
	{
		try
		{
			ResultSet results = getAllDirtyRoomsPstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error in getting all the dirty rooms from the database: " + e);
		}
		return null;
	}

	/**
	 * add a new cleaning entry
	 * @param employeeID choose an employee id to add a cleaning entry for
	 * @param roomNumber choose a room number to clean
	 */
	public void insertNewCleaningEntry(int employeeID, int roomNumber)
	{
		try
		{
			insertNewCleaningEntryPstmnt.setInt(1, employeeID);
			insertNewCleaningEntryPstmnt.setInt(2,  roomNumber);
			insertNewCleaningEntryPstmnt.execute();
		}
		catch(Exception e)
		{
			System.out.println("An error occurred while inserting the new cleaning entry: "+ e);
		}
	}

	/**
	 * add a reservation for a room
	 * @param arrivalDate the day you will be arriving
	 * @param departureDate the day you will be departuring
	 * @param guestId the guest who made the reservation
	 * @param roomNumber the room number they are reserving
	 * @param numberOfKeys the number of key cards
	 */
	public void reserveRoom (Date arrivalDate, Date departureDate, String guestEmail, int roomNumber,int numberOfKeys){
		try
		{
			reserveRoomPstmnt.setDate(1, arrivalDate);
			reserveRoomPstmnt.setDate(2,departureDate);
			reserveRoomPstmnt.setString(3, guestEmail);
			reserveRoomPstmnt.setInt(4,roomNumber);
			reserveRoomPstmnt.setInt( 5,numberOfKeys);
			reserveRoomPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to book a room: " + e);
		}
	}
	
}
