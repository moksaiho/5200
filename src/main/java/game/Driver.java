package game;

import game.dal.*;
import game.model.*;
import game.model.Character;
import game.model.Gear.GearType;
import game.model.Gear.SlotType;

import java.sql.*;
import java.util.List;

/**
 * Driver class for testing all DAO implementations.
 */
public class Driver {
    public static void main(String[] args) {
        try {
            // 1. Create the PM3 schema (after deleting if exists)
            createSchema();
            System.out.println("✅ Schema created successfully");

            // 2. Create all tables
            try (Connection cxn = ConnectionManager.getConnection()) {
                createTables(cxn);
                System.out.println("✅ All tables created successfully");

                // 3. Exercise all DAO methods
                testAllDaoMethods(cxn);
                System.out.println("✅ All DAO methods tested successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error occurred: " + e.getMessage());
        }
    }

    private static void createSchema() throws SQLException {
        try (Connection conn = ConnectionManager.getSchemalessConnection();
             Statement stmt = conn.createStatement()) {
            // Drop the schema if it exists (this will drop all tables)
            stmt.execute("DROP SCHEMA IF EXISTS PM3");
            // Create a new schema
            stmt.execute("CREATE SCHEMA PM3");
            // Use the new schema
            stmt.execute("USE PM3");
            System.out.println("✅ Schema PM3 dropped and created successfully");
        }
    }

    private static void createTables(Connection cxn) throws SQLException {
        try (Statement stmt = cxn.createStatement()) {
            // Drop all tables if they exist (in reverse order of dependencies)
            stmt.execute("DROP TABLE IF EXISTS EquippedItem");
            stmt.execute("DROP TABLE IF EXISTS Inventory");
            stmt.execute("DROP TABLE IF EXISTS StatisticBonus");
            stmt.execute("DROP TABLE IF EXISTS CharacterStatistics");
            stmt.execute("DROP TABLE IF EXISTS StatisticType");
            stmt.execute("DROP TABLE IF EXISTS Consumable");
            stmt.execute("DROP TABLE IF EXISTS GearJob");
            stmt.execute("DROP TABLE IF EXISTS Gear");
            stmt.execute("DROP TABLE IF EXISTS Item");
            stmt.execute("DROP TABLE IF EXISTS CharacterCurrency");
            stmt.execute("DROP TABLE IF EXISTS Currency");
            stmt.execute("DROP TABLE IF EXISTS CharacterJob");
            stmt.execute("DROP TABLE IF EXISTS Job");
            stmt.execute("DROP TABLE IF EXISTS `Character`");
            stmt.execute("DROP TABLE IF EXISTS Player");
            stmt.execute("DROP TABLE IF EXISTS Clan");
            stmt.execute("DROP TABLE IF EXISTS Race");
            System.out.println("✅ All existing tables dropped successfully");

            // Create tables in order of dependencies
            stmt.execute("""
                CREATE TABLE Race (
                    raceID INT AUTO_INCREMENT PRIMARY KEY, 
                    raceName VARCHAR(50) NOT NULL
                )""");

            stmt.execute("""
                CREATE TABLE Clan (
                    clanID INT AUTO_INCREMENT PRIMARY KEY,
                    clanName VARCHAR(50) NOT NULL,
                    raceID INT NOT NULL,
                    FOREIGN KEY (raceID) REFERENCES Race(raceID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Player (
                    playerID INT AUTO_INCREMENT PRIMARY KEY,
                    fullName VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL
                )""");

            stmt.execute("""
                CREATE TABLE `Character` (
                    characterID INT AUTO_INCREMENT PRIMARY KEY,
                    playerID INT NOT NULL,
                    clanID INT NOT NULL,
                    firstName VARCHAR(50) NOT NULL,
                    lastName VARCHAR(50) NOT NULL,
                    FOREIGN KEY (playerID) REFERENCES Player(playerID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (clanID) REFERENCES Clan(clanID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Job (
                    jobID VARCHAR(10) PRIMARY KEY,
                    jobName VARCHAR(50) NOT NULL
                )""");

            stmt.execute("""
                CREATE TABLE CharacterJob (
                    characterID INT NOT NULL,
                    jobID VARCHAR(10) NOT NULL,
                    level INT NOT NULL,
                    experiencePoints INT NOT NULL,
                    PRIMARY KEY (characterID, jobID),
                    FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (jobID) REFERENCES Job(jobID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Currency (
                    currencyID INT AUTO_INCREMENT PRIMARY KEY,
                    currencyName VARCHAR(50) NOT NULL,
                    cap INT,
                    weeklyCap INT
                )""");

            stmt.execute("""
                CREATE TABLE CharacterCurrency (
                    characterID INT NOT NULL,
                    currencyID INT NOT NULL,
                    amount INT NOT NULL,
                    weeklyAmountAcquired INT NOT NULL,
                    PRIMARY KEY (characterID, currencyID),
                    FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (currencyID) REFERENCES Currency(currencyID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Item (
                    itemID INT AUTO_INCREMENT PRIMARY KEY,
                    itemName VARCHAR(50) NOT NULL,
                    itemLevel INT NOT NULL,
                    itemMaxStackSize INT NOT NULL,
                    itemPrice INT NOT NULL
                )""");

            stmt.execute("""
                CREATE TABLE Gear (
                    itemID INT NOT NULL,
                    gearType ENUM('weapon','armor','accessory') NOT NULL,
                    slot ENUM('head','body','legs','feet','hands','weapon','offhand','ring','earring') NOT NULL,
                    requiredLevel INT NOT NULL,
                    damage INT,
                    PRIMARY KEY (itemID),
                    FOREIGN KEY (itemID) REFERENCES Item(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE GearJob (
                    itemID INT NOT NULL,
                    jobID VARCHAR(10) NOT NULL,
                    PRIMARY KEY (itemID, jobID),
                    FOREIGN KEY (itemID) REFERENCES Gear(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (jobID) REFERENCES Job(jobID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Consumable (
                    itemID INT NOT NULL,
                    description VARCHAR(255),
                    PRIMARY KEY (itemID),
                    FOREIGN KEY (itemID) REFERENCES Item(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE StatisticType (
                    statName VARCHAR(50) PRIMARY KEY,
                    description VARCHAR(255)
                )""");

            stmt.execute("""
                CREATE TABLE CharacterStatistics (
                    characterID INT NOT NULL,
                    statName VARCHAR(50) NOT NULL,
                    statValue INT NOT NULL,
                    PRIMARY KEY (characterID, statName),
                    FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (statName) REFERENCES StatisticType(statName)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE StatisticBonus (
                    itemID INT NOT NULL,
                    statName VARCHAR(50) NOT NULL,
                    bonusType VARCHAR(50),
                    bonusFlatValue INT,
                    bonusPercentageValue FLOAT,
                    bonusCap INT,
                    PRIMARY KEY (itemID, statName),
                    FOREIGN KEY (itemID) REFERENCES Item(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (statName) REFERENCES StatisticType(statName)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE Inventory (
                    characterID INT NOT NULL,
                    slotNumber INT NOT NULL,
                    itemID INT NOT NULL,
                    quantity INT NOT NULL,
                    PRIMARY KEY (characterID, slotNumber),
                    FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (itemID) REFERENCES Item(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");

            stmt.execute("""
                CREATE TABLE EquippedItem (
                    characterID INT NOT NULL,
                    slot ENUM('head','body','legs','feet','hands','weapon','offhand','ring','earring') NOT NULL,
                    itemID INT NOT NULL,
                    PRIMARY KEY (characterID, slot),
                    FOREIGN KEY (characterID) REFERENCES `Character`(characterID)
                        ON DELETE RESTRICT ON UPDATE CASCADE,
                    FOREIGN KEY (itemID) REFERENCES Gear(itemID)
                        ON DELETE RESTRICT ON UPDATE CASCADE
                )""");
        }
    }

    private static void testAllDaoMethods(Connection cxn) throws SQLException {
        // Test Race DAO
        System.out.println("\nTesting Race DAO...");
        Race race = RaceDao.create(cxn, "Hyur");
        System.out.println("Created race: " + race);
        
        Race retrievedRace = RaceDao.getRaceByID(cxn, race.getRaceID());
        System.out.println("Retrieved race: " + retrievedRace);

        // Test Clan DAO
        System.out.println("\nTesting Clan DAO...");
        Clan clan = ClanDao.create(cxn, "Midlander", race);
        System.out.println("Created clan: " + clan);
        
        List<Clan> clans = ClanDao.getClansByRace(cxn, race);
        System.out.println("Retrieved clans for race: " + clans);

        // Test Player DAO
        System.out.println("\nTesting Player DAO...");
        Player player = PlayerDao.create(cxn, "John Doe", "john@email.com");
        System.out.println("Created player: " + player);
        
        player = PlayerDao.updateEmail(cxn, player, "newemail@email.com");
        System.out.println("Updated player email: " + player);
        
        List<Player> players = PlayerDao.getPlayersByEmailDomain(cxn, "email.com");
        System.out.println("Retrieved players by email domain: " + players);

        // Test Character DAO
        System.out.println("\nTesting Character DAO...");
        Character character = CharacterDao.create(cxn, player, clan, "John", "Warrior");
        System.out.println("Created character: " + character);
        
        List<Character> characters = CharacterDao.getCharactersByLastName(cxn, "Warrior");
        System.out.println("Retrieved characters by last name: " + characters);

        // Test Job DAO
        System.out.println("\nTesting Job DAO...");
        Job job = JobDao.create(cxn, "WAR", "Warrior");
        System.out.println("Created job: " + job);

        // Test CharacterJob DAO
        System.out.println("\nTesting CharacterJob DAO...");
        CharacterJob characterJob = CharacterJobDao.create(cxn, character, job, 1, 0);
        System.out.println("Created character job: " + characterJob);

        // Test Currency DAO
        System.out.println("\nTesting Currency DAO...");
        Currency currency = CurrencyDao.create(cxn, "Gil", null, null);
        System.out.println("Created currency: " + currency);

        // Test Item and related DAOs
        System.out.println("\nTesting Item and related DAOs...");
        
        // Test Gear
        Gear gear = GearDao.create(cxn, "Bronze Sword", 1, 1, 100, 
            GearType.weapon, SlotType.weapon, 1, 10);
        System.out.println("Created gear: " + gear);
        
        List<Gear> gearBySlot = GearDao.getGearBySlot(cxn, SlotType.weapon);
        System.out.println("Retrieved gear by slot: " + gearBySlot);
        
        gear = GearDao.updateRequiredLevel(cxn, gear, 5);
        System.out.println("Updated gear level: " + gear);

        // Test Consumable
        Consumable potion = ConsumableDao.create(cxn, "Potion", 1, 99, 50, "Restores HP");
        System.out.println("Created consumable: " + potion);
        
        potion = ConsumableDao.updateDescription(cxn, potion, "Restores 100 HP");
        System.out.println("Updated consumable description: " + potion);

        // Test StatisticType
        System.out.println("\nTesting StatisticType DAO...");
        StatisticType statType = StatisticTypeDao.create(cxn, "Strength", "Physical attack power");
        System.out.println("Created stat type: " + statType);

        // Test deletion
        System.out.println("\nTesting deletion...");
        
        // First delete dependent records
        CharacterJobDao.delete(cxn, characterJob);
        System.out.println("Deleted character job");
        
        // Then delete the character
        CharacterDao.delete(cxn, character);
        System.out.println("Deleted character");
        
        // Delete player after character is deleted
        PlayerDao.delete(cxn, player);
        System.out.println("Deleted player");
        
        // Delete gear
        GearDao.delete(cxn, gear);
        System.out.println("Deleted gear");

        System.out.println("\n✅ All DAO methods tested successfully!");
    }
}
