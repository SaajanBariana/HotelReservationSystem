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
	private PreparedStatement employeeCleaningSchedulePstmnt;
	private PreparedStatement removeEmployeePstmnt;
	private PreparedStatement findEmployeeByNamePstmnt;
	private PreparedStatement getAveragePerRoomPstmnt;
	private PreparedStatement getAverageSalaryPstmnt;
	private PreparedStatement getMaxSalaryPstmnt;
	private PreparedStatement getMinSalaryPstmnt;
	private PreparedStatement getPositionsPstmnt;
	private PreparedStatement getCleaningSchedulePstmnt;
	private PreparedStatement getGuestsWithNoReservationPstmnt;
	private PreparedStatement updateReservationPstmnt;
	private PreparedStatement updateCleaningSchedulePstmnt;
	private PreparedStatement getAllEmployeesPstmnt;
	private PreparedStatement getAllDirtyRoomsPstmnt;
	private PreparedStatement insertNewCleaningEntryPstmnt;
	private PreparedStatement addEmployeePstmnt;
	private PreparedStatement addGuestPstmnt;
	private PreparedStatement getReservationsPstmnt;
	private PreparedStatement getEmployeesPstmnt;
	private PreparedStatement getGuestsPstmnt;


	
	/**
	 * Initalize the prepared statements and creating the connection to the database
	 */
	public DatabaseConnection()
	{
		String host = "jdbc:mysql://localhost:3306/HotelReservationSystem";
		String username = "root";
		String password = "botdbotd1";
		try{
			con = (Connection) DriverManager.getConnection(host, username, password);
			stmnt = (Statement) con.createStatement();
			
			//Setting up the reserve room prepared statement
			String reserveRoomCommand = "insert into reservation(reservationid, arrivalDate,departureDate, userEmail,roomNumber,numberofkeys) values (0, ?,?,?,?,?)";
			reserveRoomPstmnt = con.prepareStatement(reserveRoomCommand);
			
			//Setting up the add employee prepared statement
			String addEmployeeCommand = "insert into Employees(EmployeeID, Name, Address, PhoneNumber, Email, Salary, Birthday, Position) values (0, ?,?,?,?,?,?,?)";
			addEmployeePstmnt = con.prepareStatement(addEmployeeCommand);
			
			//Setting up the add guest prepared statement
			String addGuestCommand = "insert into Guests(FirstName, LastName, Address, PhoneNumber, Email, Password) values (?,?,?,?,?,?)";
			addGuestPstmnt = con.prepareStatement(addGuestCommand);
			 
			//getting all reservations
			String getReservationsCommand = "Select * from Reservation;";
			getReservationsPstmnt = con.prepareStatement(getReservationsCommand);
			
			//getting all employees
			String getEmployeesCommand = "Select * from Employees;";
			getEmployeesPstmnt = con.prepareStatement(getEmployeesCommand);
			
			//getting all guests
			String getGuestsCommand = "Select * from Guests;";
			getGuestsPstmnt = con.prepareStatement(getGuestsCommand);
			
			//Setting up the cancel room prepared statement
			String cancelReservationCommand = "Delete from Reservation where userEmail = ? and arrivalDate = ?";
			cancelReservationPstmnt = con.prepareStatement (cancelReservationCommand);
			 
			//Setting up the list available rooms prepared statement
			String listAvailableCommand = "Select * From Rooms where Available= 1";
			listAvailablePstmnt = con.prepareStatement(listAvailableCommand);
			 
			//Setting up the occupancy status prepared statement
			String occupancyStatusCommand = "Select RoomNumber, Available from Rooms";
			occupancyStatusPstmnt = con.prepareStatement (occupancyStatusCommand);
			 
			//Setting up the display employee cleaning schedule prepared statement
			String employeeCleaningScheduleCommand = "Select RoomNumbers from CleaningSchedule where EmployeeID = ? and cleaned = '0'";
			employeeCleaningSchedulePstmnt = con.prepareStatement(employeeCleaningScheduleCommand);
			
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
			
			//updating a Reservation
			String updateReservationCommand = "Update Reservation set ArrivalDate = ?, set DepartureDate = ?, set RoomNumber = ?, where rID = ?";
			updateReservationPstmnt = con.prepareStatement(updateReservationCommand);
			
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
	 * Cancel a reservation for a user with the given userE-Mail and arrivalDate
	 * @param userEmail The email of the user who made the reservation
	 * @param arrivalDate The arrival date for the reservation
	 */
	public void cancelReservation (String userEmail,Date arrivalDate)
	{
		try
		{
			cancelReservationPstmnt.setString(1,userEmail);
			cancelReservationPstmnt.setDate (2,arrivalDate);
			cancelReservationPstmnt.executeUpdate();
		}
		catch (Exception e)
		{
			System.out.println("An error has occurred when trying to cancel a reservation: " + e);
		}
	}

	/**
	 * Gets the list of all available rooms
	 * @return A result set if the query successfully executes
	 */
	public ResultSet listAvailableRooms ()
	{
		try
		{
			ResultSet result = listAvailablePstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to list all available rooms: " + e);
		}
		return null;
	}

	/**
	 * Lists all the rooms and their occupancy status
	 * @return A result set of all the rooms and the occupancy status if the query successfully executes
	 */
	public ResultSet occupancyStatus ()
	{
		try
		{
			ResultSet result = occupancyStatusPstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to view the occupancy status of all the rooms: " + e);
		}
		return null;
	}

	/**
	 * Gets the cleaning schedule for a specific employee
	 * @param employeeId The employee whose cleaning schedule we want
	 * @return A result set of the cleaning schedule if the query successfully executes
	 */
	public ResultSet getEmployeeCleaningSchedule (int employeeId)
	{
		try
		{
			employeeCleaningSchedulePstmnt.setInt(1,employeeId);
			ResultSet result = employeeCleaningSchedulePstmnt.executeQuery();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to view the schedule of this employee: " + e);
		}
		return null;

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
	 * returns all of the entries in the Reservation table
	 * @return returns the result set of the reservation table
	 */
	public ResultSet getReservations()
	{
		try
		{
			ResultSet results = getReservationsPstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error retrieving the reservations");
		}
		return null;
	}
	
	/**
	 * returns all of the entries in the Guests table
	 * @return returns the result set of the guests table
	 */
	public ResultSet getGuests()
	{
		try
		{
			ResultSet results = getGuestsPstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error retrieving guests");
		}
		return null;
	}
	
	/**
	 * returns all of the entries in the Guests table
	 * @return returns the result set of the guests table
	 */
	public ResultSet getEmployees()
	{
		try
		{
			ResultSet results = getEmployeesPstmnt.executeQuery();
			return results;
		}
		catch(Exception e)
		{
			System.out.println("Error retrieving employees");
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
	 * @param guestEmail the guest who made the reservation
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
	
	public void addEmployee (String name, String address, String phoneNum, String email, Integer salary, Date birthday, String position){
		try
		{
			addEmployeePstmnt.setString(1, name);
			addEmployeePstmnt.setString(2, address);
			addEmployeePstmnt.setString(3, phoneNum);
			addEmployeePstmnt.setString(4, email);
			addEmployeePstmnt.setInt(5, salary);
			addEmployeePstmnt.setDate(6, birthday);
			addEmployeePstmnt.setString(7, position);
			addEmployeePstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to add an employee: " + e);
		}
	}
	
	public void addGuest (String first, String last, String address, String phoneNum, String email, String password){
		try
		{
			addGuestPstmnt.setString(1, first);
			addGuestPstmnt.setString(2, last);
			addGuestPstmnt.setString(3, address);
			addGuestPstmnt.setString(4, phoneNum);
			addGuestPstmnt.setString(5, email);
			addGuestPstmnt.setString(6, password);
			addGuestPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to add a Guest: " + e);
		}
	}

	public void updateReservation (Date newArrival, Date newDeparture, int roomNum)
	{
		try
		{
			updateReservationPstmnt.setDate(1, newArrival);
			updateReservationPstmnt.setDate(2, newDeparture);
			updateReservationPstmnt.setInt(4, roomNum);
			updateReservationPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error occurred when trying to update the Reservation: " + e);
		}
		
	}
}
