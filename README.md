# HotelReservationSystem

Set Up:
Manually create the databases in your MySQL workbench
The SQL commands are as follows:
    
    
    DROP DATABASE IF EXISTS HotelReservationSystem;
    CREATE DATABASE HotelReservationSystem;

    CREATE TABLE `HotelReservationSystem`.`Rooms` (
    `RoomNumber` INT NOT NULL AUTO_INCREMENT,
    `Price` INT NULL,
    `Beds` INT NULL,
    `RoomType` VARCHAR(45) NULL,
    `Available` TINYINT NULL,
    `Clean` TINYINT NULL,
    PRIMARY KEY (`RoomNumber`));

    CREATE TABLE `HotelReservationSystem`.`Guests` (
    `Email` VARCHAR(50) NOT NULL ,
    `firstName` VARCHAR(50) NULL,
    `lastName` VARCHAR(50) NULL,
    `Address` VARCHAR(75) NULL,
    `PhoneNumber` VARCHAR(45) NULL,
    `Password` Varchar(45) NULL,
    PRIMARY KEY (`Email`));

    CREATE TABLE `HotelReservationSystem`.`Employees` (
    `EmployeeID` INT NOT NULL AUTO_INCREMENT,
    `Name` VARCHAR(50) NULL,
    `Address` VARCHAR(75) NULL,
    `PhoneNumber` VARCHAR(45) NULL,
    `Email` VARCHAR(50) NULL,
    `Salary` INT NULL,
    `Birthday` DATETIME NULL,
    `Position` VARCHAR(50) NULL,
    `Password` Varchar(45) NULL,
    PRIMARY KEY (`EmployeeID`));

    CREATE TABLE `HotelReservationSystem`.`Reservation` (
    `ReservationID` INT NOT NULL AUTO_INCREMENT,
    `ArrivalDate` DATETIME NULL,
    `DepartureDate` DATETIME NULL,
    `UserEmail` VARCHAR(50) NULL,
    `RoomNumber` INT NULL,
    PRIMARY KEY (`ReservationID`),
    Foreign key (`UserEmail`) references Guests(`Email`) on delete cascade on update cascade,
    Foreign key (`RoomNumber`) references Rooms(`RoomNumber`) on delete cascade on update cascade);

    CREATE TABLE `HotelReservationSystem`.`CleaningSchedule` (
    `CleaningID` INT NOT NULL auto_increment,
    `EmployeeID` INT NOT NULL,
    `RoomNumber` INT NULL,
    `Cleaned` TINYINT NULL,
    PRIMARY KEY (`CleaningID`),
    foreign key (`EmployeeID`) references Employees(`EmployeeID`),
    foreign key (`RoomNumber`) references Rooms(`RoomNumber`));

Manually create the triggers in your MySQL Workbench as well:

        Delimiter |
        Create trigger createReservation
        after insert on Reservation
        for each row
        begin
        update Rooms set available = false where (roomNumber = new.RoomNumber);
        End
        |
        delimiter ;


        Delimiter |
        Create trigger deleteReservation
        after delete on Reservation
        for each row
        begin
        update Rooms set available = true where (roomNumber = old.RoomNumber);
        End
        |
        delimiter ;

    
Once you have completed adding in the tables and triggers, open the DatabaseConnection.java file. Enter in your correct username and password for your localhost MySQL server.

To run this program, run the StartProject.java file.
