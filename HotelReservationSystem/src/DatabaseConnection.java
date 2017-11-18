import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseConnection {
	private Connection con;
	private Statement stmnt;
	
	//create all of the prepared statements 
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

	/**
	 * Initalize the prepared statements and creating the connection to the database
	 */
	public DatabaseConnection()
	{
		String host = "jdbc:mysql://localhost:3306/HotelReservationSystem";
		String username = "root";
		String password = "Enter Password";
		try{
			con = (Connection) DriverManager.getConnection(host, username, password);
			stmnt = (Statement) con.createStatement();
			
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
			System.out.println("Deleted the Employee from the database\n\n\n\n\n\n");
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






}
