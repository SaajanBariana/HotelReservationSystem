import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class RunUser {
	public DatabaseConnection connection;
	
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
		while (!choseValidNumber)
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
				case 3: System.out.println("Thank you for visiting! :)");
					return;
				//Chose a number that is not one of the above choices
				default: System.out.println("Please try again with a valid number choice");
					break;
			}
		}
		
		choseValidNumber = false; //reset the boolean to be false
		
		while(!choseValidNumber)
		{
			int input = readInput(UserInformation.name);
			switch(input)
			{
				//Chose to create a reservation
				case 1: System.out.println("NEED TO UPDATE AND FIX RESERVATION OF ROOMS. \n"
						+ "DISPLAY ALL THE ROOMS THAT ARE AVAILABLE DURING THAT TIME PERIOD FOR THE USER TO CHOOSE FROM. \n"
						+ "ALSO ADD A TOTAL COST AFTER CALCULATING THE TOTAL NUMBER OF DAYS THE USER IS STAYING. \n"
						+ "DISPLAY ALL THE CURRENT RESERVATIONS FOR THIS GUEST AFTER ADDING THE NEW RESERVATION INTO THE DATABASE");
					reserveRoom();
					break;
				//Chose to cancel a reservation
				case 2: System.out.println("Need to implement cancel reservation. Prepared Statement is already created for this command");
					break;
				//Chose to display all the available rooms
				case 3: listAvailableRooms();
					break;
				//Chose to change a reservation
				case 4: System.out.println("Need to implement changing a reservation. Prepared Statement is already created for this command");
					break;
				//Chose to display the different pricing for different types of rooms
				case 5: getAverageCostPerRoomType();
					break;
				//Chose to show all the active reservations the guest currently has
				case 6: 
					System.out.println("Need to implement viewing all your active reservations. Need to create a Prepared Statement for this command");
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
		
		System.out.println("Thank you for visiting! :)");
			
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
		while (!worked)
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
		while(!loggedIn)
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
					System.out.println("What is your username? ");
					String username = input.nextLine();
					if(username.toLowerCase().equals("q"))
					{
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
						System.out.println("This is the password for debugging purposes. Remove when finished: " + passInDB);
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
						UserInformation.name = result.getString(2);
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
//			try
//			{
//				
//				if (result == null || result.next() == false)
//				{
//					System.out.println("Incorrect username or password. Please make sure you have created an account with this username.");
//				}
//			}
//			catch (Exception e)
//			{
//				System.out.println("An error has occurred: " + e);
//				return;
//			}
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
			getGuests();
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred when trying to create a guest account " + e);
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
		
		/*
		 * TO DO:
		 * SHOW A LIST OF ALL THE ROOMS THAT ARE AVAILABLE DURING THIS TIME PERIOD
		 * GET THE TOTAL COST FOR THIS STAY
		 */
		
		
		
		
		
		
		
		String guestEmail = UserInformation.ID;
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
			//	System.out.println(resultSet.getInt(1));
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
			System.out.println("The average for a " + size + " sized room is: $" + connection.getAverageForRoomType(size) + "\n\n\n\n\n");
		}
		catch(Exception e)
		{
			System.out.println("An error has occurred while trying to display the average for a " + size + " room: " + e);
		}
	}
}
