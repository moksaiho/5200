package game;

import game.dal.*;
import game.model.*;

import java.sql.*;

public class Driver {
    public static void main(String[] args) {

        try (
            Connection schemalessCxn = ConnectionManager.getSchemalessConnection();
            Statement s1 = schemalessCxn.createStatement()
        ) {
            s1.execute("DROP SCHEMA IF EXISTS CS5200Project");
            s1.execute("CREATE SCHEMA CS5200Project");
            System.out.println("✅ Schema CS5200Project dropped and created.");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        try (
            Connection cxn = ConnectionManager.getConnection();
            Statement stmt = cxn.createStatement()
        ) {
            stmt.execute("USE CS5200Project");
            System.out.println("✅ Switched to schema CS5200Project.");

            createTables(stmt);
            System.out.println("✅ All tables created.");

            runTests(cxn);
            System.out.println("✅ All DAO methods tested successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables(Statement stmt) throws SQLException {
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

    public static void runTests(Connection cxn) throws SQLException {
        Race race = RaceDao.create(cxn, "Miqo'te");
        Clan clan = ClanDao.create(cxn, "Seeker of the Sun", race);
        Player player = PlayerDao.create(cxn, "Y'shtola Rhul", "yshtola@scions.org");
        game.model.Character character = CharacterDao.create(cxn, player, clan, "Y'shtola", "Rhul");
        Job job = JobDao.create(cxn, "CNJ", "Conjurer");
        CharacterJobDao.create(cxn, character, job, 30, 5000);
        Item item = ItemDao.create(cxn, "Ether", 10, 99, 120);
        ConsumableDao.create(cxn, "Mega-Potion", 20, 99, 500, "Restores a large amount of HP");
        GearDao.create(cxn, "White Robe", 30, 1, 2500, Gear.GearType.armor, Gear.SlotType.body, 30, null);
    }
}
