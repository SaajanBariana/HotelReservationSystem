import javax.xml.transform.Result;
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
	private PreparedStatement getGuestReservationsPstmnt;
	private PreparedStatement updateRoomAvailabilityPstmnt;
	private PreparedStatement getRoomsPstmnt;
	private PreparedStatement deleteGuestPstmt;
	private PreparedStatement getEmployeesWithHigherPayPstmnt;
	private PreparedStatement getCleanerWithLeastRoomsPstmnt;
	private PreparedStatement getDirtyUnassignedRoomsPstmnt;
	private PreparedStatement assignEmployeeToCleanRoomPstmnt;
	private PreparedStatement listAvailableRoomsBetweenDatesPstmnt;
	/**
	 * Initialize the prepared statements and creating the connection to the database
	 */
	public DatabaseConnection(String type)
	{
		String host = "jdbc:mysql://localhost:3306/HotelReservationSystem";
		String username = UserInformation.databaseUsername;
		String password = UserInformation.databasePassword;
		try{
			con = (Connection) DriverManager.getConnection(host, username, password);
			stmnt = (Statement) con.createStatement();
			
			//Setting up the reserve room prepared statement
			String reserveRoomCommand = "insert into reservation(reservationid, arrivalDate,departureDate, userEmail,roomNumber,numberofkeys) values (0, ?,?,?,?,?)";
			reserveRoomPstmnt = con.prepareStatement(reserveRoomCommand);
			
			//Setting up the add guest prepared statement
			String addGuestCommand = "insert into Guests(FirstName, LastName, Address, PhoneNumber, Email, Password) values (?,?,?,?,?,?)";
			addGuestPstmnt = con.prepareStatement(addGuestCommand);
			
			//Getting all the guests reservations
			String getGuestReservatiomCommand = " Select * from Reservation where UserEmail = ?";
			getGuestReservationsPstmnt = con.prepareStatement(getGuestReservatiomCommand);
			
			//getting all guests
			String getGuestsCommand = "Select * from Guests;";
			getGuestsPstmnt = con.prepareStatement(getGuestsCommand);
			
			//Setting up the cancel room prepared statement
			String cancelReservationCommand = "Delete from Reservation where userEmail = ? and arrivalDate = ?";
			cancelReservationPstmnt = con.prepareStatement (cancelReservationCommand);
			 
			//Setting up the list available rooms prepared statement
			String listAvailableCommand = "Select * From Rooms where Available = 1";
			listAvailablePstmnt = con.prepareStatement(listAvailableCommand);
			
			//Getting a list of all rooms
			String getRoomsCommand = "Select * from Rooms";
			getRoomsPstmnt = con.prepareStatement(getRoomsCommand);
			 
			//Setting up the occupancy status prepared statement
			String occupancyStatusCommand = "Select RoomNumber, Available from Rooms";
			occupancyStatusPstmnt = con.prepareStatement (occupancyStatusCommand);
							
			//setting up finding average price per type of room prepared statement
			String findAveragePerRoomCommand = "SELECT avg(price) FROM rooms WHERE RoomType = ?;";
			getAveragePerRoomPstmnt = con.prepareStatement(findAveragePerRoomCommand);
			
			//updating a Reservation
			String updateReservationCommand = "Update Reservation set ArrivalDate = ?, DepartureDate = ?, RoomNumber = ? where ReservationID = ?";
			updateReservationPstmnt = con.prepareStatement(updateReservationCommand);
	
			System.out.println("Sets up employee database");
			//Setting up the add employee prepared statement
			String addEmployeeCommand = "insert into Employees(firstname, lastname, Address, PhoneNumber, Email, Salary, Birthday, Position, Password) values (?, ?,?,?,?,?,?,?, ?)";
			addEmployeePstmnt = con.prepareStatement(addEmployeeCommand);
			
			//getting all reservations
			String getReservationsCommand = "Select * from Reservation";
			getReservationsPstmnt = con.prepareStatement(getReservationsCommand);
			
			//getting all employees
			String getEmployeesCommand = "Select * from Employees;";
			getEmployeesPstmnt = con.prepareStatement(getEmployeesCommand);
			
			//Setting up the display employee cleaning schedule prepared statement
			String employeeCleaningScheduleCommand = "Select RoomNumbers from CleaningSchedule where EmployeeID = ? and cleaned = '0'";
			employeeCleaningSchedulePstmnt = con.prepareStatement(employeeCleaningScheduleCommand);
			
			//setting up the removing the employee prepared statement
			String removeEmployeeCommand = "delete from employees where EmployeeID = ?";
			removeEmployeePstmnt = con.prepareStatement(removeEmployeeCommand);
			
			//setting up the select from employee prepared statement
			String findEmployeeCommand = "SELECT * FROM employees WHERE name = ?;";
			findEmployeeByNamePstmnt = con.prepareStatement(findEmployeeCommand);
			
			//setting up finding the average salary for each position
			String getAverageSalaryCommand = "SELECT avg(Salary) FROM Employees WHERE Position = ?;";
			getAverageSalaryPstmnt = con.prepareStatement(getAverageSalaryCommand);

			//Setting up finding the employees who have higher than average pay for their position
			String getEmployeesWithHigherPay = "SELECT employeeId, firstname, salary, avg (salary) as average, position from employees e1 where e1.salary > ( select avg (salary) from employees e2 where e1.position = e2.position )";
			getEmployeesWithHigherPayPstmnt = con.prepareStatement(getEmployeesWithHigherPay);

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
			String getGuestsWithNoReservationsCommand = "Select * from Guests where Guests.Email not in (Select distinct(UserEmail) from Reservation);";
			getGuestsWithNoReservationPstmnt = con.prepareStatement(getGuestsWithNoReservationsCommand);
			
			//updating the Cleaning Schedule
			String updateScheduleCommand = "Update CleaningSchedule set cleaned = ? where RoomNumber = ?";
			updateCleaningSchedulePstmnt = con.prepareStatement(updateScheduleCommand);
			
			//updating the availability of a room
			String updateRoomCommand = "Update Rooms set Available = ? where RoomNumber = ?";
			updateRoomAvailabilityPstmnt = con.prepareStatement(updateRoomCommand);
			
			//get all of the employees
			String getAllEmployeesCommand = "Select * from Employees";
			getAllEmployeesPstmnt = con.prepareStatement(getAllEmployeesCommand);
			
			//get list of all the rooms that are dirty
			String getAllDirtyRoomsCommand = "Select * from Rooms where clean = false";
			getAllDirtyRoomsPstmnt = con.prepareStatement(getAllDirtyRoomsCommand);
			
			//insert new Cleaning data
			String insertNewCleaningCommand = "Insert into CleaningSchedule(EmployeeID, RoomNumber) values (?, ?);";
			insertNewCleaningEntryPstmnt = con.prepareStatement(insertNewCleaningCommand);

			//delete guest account based on email
			String deleteGuestCommand = "Delete from Guests where Email = ?";
			deleteGuestPstmt = con.prepareStatement(deleteGuestCommand);

			//Gets a cleaner with the least amount of rooms to clean
			String getCleanerWithLeastRooms =
			"Select employeeId, count(*) as roomsLeft  From cleaningSchedule Where cleaned = 0 group by employeeId having count(*) = " +
			"(Select min(roomsLeft) "+
			"from (Select employeeId, count(*) as roomsLeft  From cleaningSchedule Where cleaned = 0 group by employeeId))";
			getCleanerWithLeastRoomsPstmnt = con.prepareStatement(getCleanerWithLeastRooms);

			String getDirtyUnassignedRooms = "Select r.roomNumber FROM rooms r where r.roomNumber not in (SELECT roomNumber from cleaningSchedule) and r.clean = 0";
			getDirtyUnassignedRoomsPstmnt = con.prepareStatement(getDirtyUnassignedRooms);

			String assignEmployeeToCleanRoom ="insert into cleaningSchedule values (0, ?,?,0)";
			assignEmployeeToCleanRoomPstmnt = con.prepareStatement(assignEmployeeToCleanRoom);

			String listAvailableRoomsBetweenDates = "Select roomNumber, price from rooms where roomNumber not in (Select distinct roomNumber from reservation where (? >= arrivalDate and ? < departureDate) || (? >= arrivalDate and ? <= departureDate)" +
					" || (? <= arrivalDate and ? >=departureDate)) ";
			listAvailableRoomsBetweenDatesPstmnt = con.prepareStatement(listAvailableRoomsBetweenDates);
		}
		catch(Exception e){
			System.out.println("There was an error creating connection to the database. Make sure you set up the username and password correctly: " + e);
		}
		
	}
	
	/**
	 * send a simple, general query to the database
	 * @param query the query that you want to send to the database
	 */
	public ResultSet sendQuery(String query)
	{
		try 
		{
			String sqlCommand = query;
			ResultSet result = stmnt.executeQuery(sqlCommand);
			return result;
		}
		catch(SQLException err)
		{
			System.out.println("An error has occurred: " + err.getMessage());
		}
		return null;
		
	}
	public ResultSet listAvailableRoomsBetweenDates (Date arrivalDate, Date departureDate)
	{
		try {
			listAvailableRoomsBetweenDatesPstmnt.setDate(1,arrivalDate);
			listAvailableRoomsBetweenDatesPstmnt.setDate(2,arrivalDate);
			listAvailableRoomsBetweenDatesPstmnt.setDate(3,departureDate);
			listAvailableRoomsBetweenDatesPstmnt.setDate(4,departureDate);
			listAvailableRoomsBetweenDatesPstmnt.setDate(5,arrivalDate);
			listAvailableRoomsBetweenDatesPstmnt.setDate(6,departureDate);
			ResultSet resultSet = listAvailableRoomsBetweenDatesPstmnt.executeQuery();
			return resultSet;
		}
		catch (Exception e)
		{
			System.out.println("there was an error when trying to list the available rooms between the dates provided: " + e);
		}
		return  null;
	}
	/**
	 * Assigns an employee to clean a room
	 * @param eID The ID of the employee who will be cleaning the room
	 * @param rNum the room number of the room that needs to be cleaned
	 */
	public void assignEmployeeToCleanRoom(int eID,int rNum)
	{
		try
		{
			assignEmployeeToCleanRoomPstmnt.setInt(1, eID);
			assignEmployeeToCleanRoomPstmnt.setInt(2,rNum);
			assignEmployeeToCleanRoomPstmnt.executeUpdate();
			System.out.println("Employee " + eID+" successfully assigned to clean room "+rNum+".");
		}
		catch (Exception e)
		{
			System.out.println("Error when trying to assign the employee to clean the room: "+e);
		}
	}
	/**
	 * Gets the room numbers of the rooms that are dirty but have not been assigned for cleaning
	 * @return result set containing the room numbers
	 */
	public ResultSet getDirtyUnassignedRooms ()
	{
		try
		{
			ResultSet result = getDirtyUnassignedRoomsPstmnt.executeQuery();
			return result;
		}
		catch (Exception e)
		{
			System.out.println("there was a problem when trying to find the dirty unassigned rooms: "+ e);
		}
		return null;
	}
	/**
	 * gets the employees that have higher pay than the average for their position
	 * @return a result set containing the employee information
	 */
	public ResultSet getEmployeesWithHigherThanAveragePay ()
	{
		try
		{
			ResultSet result = getEmployeesWithHigherPayPstmnt.executeQuery();
			return result;
		}
		catch (Exception e)
		{
			System.out.println("an error has occurred when trying to fetch the data: "+ e);
		}
		return null;
	}

	/**
	 * Gets the employees with the least amount of rooms to clean
	 * @return a result set containing the employee/S information
	 */
	public ResultSet getCleanerWithLeastRooms ()
	{
		try
		{
			ResultSet result = getCleanerWithLeastRoomsPstmnt.executeQuery();
			return result;
		}
		catch (Exception e)
		{
			System.out.println("an error has occurred when trying to find the cleaner with the least amount of rooms to clean: "+ e);
		}
		return null;
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
	 * Delete the guest account for user with the given email
	 * @param guestEmail The email of the guest whose account is to be deleted
	 */
	public void deleteGuestAccount(String guestEmail)
	{
		try
		{
			deleteGuestPstmt.setString(1, guestEmail);
			deleteGuestPstmt.executeUpdate();
		}
		catch (Exception e)
		{
			System.out.println("An error has occurred when trying to delete the account: " + e);
		}
	}


	/**
	 * Gets the list of all rooms
	 * @return A result set if the query successfully executes
	 */
	public ResultSet listRooms()
	{
		try
		{
			ResultSet result = getRoomsPstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to list all rooms: " + e);
		}
		return null;
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
	 * Gets the list of a guests current reservations
	 * @return A result set if the query successfully executes
	 */
	public ResultSet getGuestReservations (String email)
	{
		email = email.toLowerCase();
		try
		{
			getGuestReservationsPstmnt.setString(1, email);
			ResultSet result = getGuestReservationsPstmnt.executeQuery();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to get your reservations: " + e);
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
			System.out.println("Error with trying to get guests who do not have any reservations: " + e);
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
	 * update the cleaning schedule cleaned value
	 * @param newValue the new value we are updating it to
	 * @param roomNumber the room number we are updating
	 */
	public void updateRoomAvailability(boolean newValue, int roomNumber)
	{
		try
		{
			updateRoomAvailabilityPstmnt.setBoolean(1, newValue);
			updateRoomAvailabilityPstmnt.setInt(2,  roomNumber);
			updateRoomAvailabilityPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error occurred when trying to update the availability of room " + roomNumber + ":" + e);
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
	public void reserveRoom (Date arrivalDate, Date departureDate, String guestEmail, String roomNumber, String numberOfKeys){
		try
		{

			reserveRoomPstmnt.setDate(1, arrivalDate);
			reserveRoomPstmnt.setDate(2,departureDate);
			reserveRoomPstmnt.setString(3, guestEmail);
			int num = Integer.parseInt(roomNumber);
			int keys = Integer.parseInt(numberOfKeys.trim());
			reserveRoomPstmnt.setInt(4,num);
			reserveRoomPstmnt.setInt( 5,keys);
			reserveRoomPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to book a room: " + e);
		}
	}
	
	public void addEmployee (String name, String address, String phoneNum, String email, Integer salary, Date birthday, String position, String password){
		try
		{
			String firstname = name.split(" ")[0];
			String secondname = name.split(" ")[1];
			
			addEmployeePstmnt.setString(1, firstname);
			addEmployeePstmnt.setString(2, secondname);
			addEmployeePstmnt.setString(3, address);
			addEmployeePstmnt.setString(4, phoneNum);
			addEmployeePstmnt.setString(5, email);
			addEmployeePstmnt.setInt(6, salary);
			addEmployeePstmnt.setDate(7, birthday);
			addEmployeePstmnt.setString(8, position);
			addEmployeePstmnt.setString(9, password);
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

	public void updateReservation (Date newArrival, Date newDeparture, int roomNum, int resID)
	{
		try
		{
			updateReservationPstmnt.setDate(1, newArrival);
			updateReservationPstmnt.setDate(2, newDeparture);
			updateReservationPstmnt.setInt(3, roomNum);
			updateReservationPstmnt.setInt(4, resID);
			updateReservationPstmnt.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error occurred when trying to update the Reservation: " + e);
		}
		
	}
}