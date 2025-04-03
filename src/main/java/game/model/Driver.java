package game;

import game.dal.*;
import game.model.*;
import game.model.Gear.GearType;
import game.model.Gear.SlotType;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * CS5200 PM3 Driver
 * Demonstrates table creation, schema reset, and exercises DAO CRUD methods.
 */
public class Driver {

    public static void main(String[] args) {
        // 1) Drop and recreate the CS5200Project schema.
        resetSchema();

        // 2) Create all tables in the newly-created schema.
        createTables();

        // 3) Exercise DAO methods with sample data.
        try (Connection connection = ConnectionManager.getConnection()) {
            System.out.println("Connected to the database.");

            /************************************************************
             *  RACE
             ************************************************************/
            Race race = RaceDao.create(connection, "Human");
            System.out.println("Created race: " + race);
            Race retrievedRace = RaceDao.getRaceByID(connection, race.getRaceID());
            System.out.println("Retrieved race by ID: " + retrievedRace);
            RaceDao.updateRaceName(connection, retrievedRace, "Humanoid");
            System.out.println("Updated race name: " + retrievedRace);
            // Deletion deferred to the end

            /************************************************************
             *  STATISTIC TYPE
             ************************************************************/
            StatisticType statTypeHP = StatisticTypeDao.create(connection, "HP", "Health Points");
            System.out.println("Created StatisticType: " + statTypeHP);
            StatisticType statTypeMana = StatisticTypeDao.create(connection, "MP", "Mana Points");
            System.out.println("Created StatisticType: " + statTypeMana);

            StatisticType retrievedStatType = StatisticTypeDao.getStatisticTypeByName(connection, "HP");
            System.out.println("Retrieved statisticType by name (HP): " + retrievedStatType);
            StatisticTypeDao.updateDescription(connection, retrievedStatType, "Hit Points");
            System.out.println("Updated statisticType description: " + retrievedStatType);

            /************************************************************
             *  PLAYER
             ************************************************************/
            Player player = PlayerDao.create(connection, "Bruce Wayne", "bruce@wayneenterprises.com");
            System.out.println("Created player: " + player);
            Player retrievedPlayer = PlayerDao.getPlayerByID(connection, player.getPlayerID());
            System.out.println("Retrieved player by ID: " + retrievedPlayer);

            // Example non-PK method: find players by email domain
            List<Player> domainPlayers = PlayerDao.getPlayersByEmailDomain(connection, "wayneenterprises.com");
            System.out.println("Players at wayneenterprises.com: " + domainPlayers);

            // Update email
            PlayerDao.updateEmail(connection, retrievedPlayer, "bruce@batman.org");
            System.out.println("Updated player's email: " + retrievedPlayer);

            /************************************************************
             *  CLAN
             ************************************************************/
            Clan clan = ClanDao.create(connection, "WayneClan", race);
            System.out.println("Created clan: " + clan);
            Clan retrievedClan = ClanDao.getClanByID(connection, clan.getClanID());
            System.out.println("Retrieved clan by ID: " + retrievedClan);
            ClanDao.updateClanName(connection, retrievedClan, "BatClan");
            System.out.println("Updated clan name: " + retrievedClan);

            /************************************************************
             *  CHARACTER
             ************************************************************/
            Character character = CharacterDao.create(connection, retrievedPlayer, retrievedClan, "Batman", "DarkKnight");
            System.out.println("Created character: " + character);
            Character retrievedCharacter = CharacterDao.getCharacterByID(connection, character.getCharacterID());
            System.out.println("Retrieved character by ID: " + retrievedCharacter);

            /************************************************************
             *  JOB
             ************************************************************/
            Job job = JobDao.create(connection, "BAT", "Dark Knight Job");
            System.out.println("Created job: " + job);
            Job retrievedJob = JobDao.getJobByID(connection, "BAT");
            System.out.println("Retrieved job by ID: " + retrievedJob);
            JobDao.updateJobName(connection, retrievedJob, "BatmanJob");
            System.out.println("Updated job name: " + retrievedJob);

            /************************************************************
             *  CHARACTER JOB
             ************************************************************/
            CharacterJob charJob = CharacterJobDao.create(connection, character, job, 1, 50);
            System.out.println("Created CharacterJob: " + charJob);
            CharacterJob retrievedCharJob = CharacterJobDao.getByCharacterAndJob(connection, character.getCharacterID(), job.getJobID());
            System.out.println("Retrieved CharacterJob by PK: " + retrievedCharJob);
            CharacterJobDao.updateExperiencePoints(connection, retrievedCharJob, 150);
            System.out.println("Updated CharacterJob experience: " + retrievedCharJob);

            /************************************************************
             *  CURRENCY
             ************************************************************/
            Currency currency = CurrencyDao.create(connection, "BatCoin", 9999, 500);
            System.out.println("Created currency: " + currency);
            Currency retrievedCurrency = CurrencyDao.getCurrencyByID(connection, currency.getCurrencyID());
            System.out.println("Retrieved currency by ID: " + retrievedCurrency);

            /************************************************************
             *  CHARACTER CURRENCY
             ************************************************************/
            CharacterCurrency charCurr = CharacterCurrencyDao.create(connection, character, currency, 200, 10);
            System.out.println("Created CharacterCurrency: " + charCurr);
            CharacterCurrency retrievedCharCurr = CharacterCurrencyDao.getCharacterCurrencyByID(
                    connection, character.getCharacterID(), currency.getCurrencyID());
            System.out.println("Retrieved CharacterCurrency: " + retrievedCharCurr);
            CharacterCurrencyDao.updateAmounts(connection, retrievedCharCurr, 250, 20);
            System.out.println("Updated CharacterCurrency: " + retrievedCharCurr);

            /************************************************************
             *  ITEM (base table)
             ************************************************************/
            Item itemBatarang = ItemDao.create(connection, "Batarang", 1, 5, 100);
            System.out.println("Created Item: " + itemBatarang);
            Item retrievedItem = ItemDao.getItemByID(connection, itemBatarang.getItemID());
            System.out.println("Retrieved Item by ID: " + retrievedItem);
            ItemDao.updateItemPrice(connection, retrievedItem, 150);
            System.out.println("Updated item price: " + retrievedItem);

            /************************************************************
             *  GEAR (extends ITEM)
             ************************************************************/
            Gear gearHelmet = GearDao.create(connection, "BatHelmet", 5, 1, 500,
                    GearType.armor, SlotType.head, 5, null);
            System.out.println("Created Gear: " + gearHelmet);
            Gear retrievedGear = GearDao.getGearByID(connection, gearHelmet.getItemID());
            System.out.println("Retrieved Gear by ID: " + retrievedGear);
            GearDao.updateRequiredLevel(connection, retrievedGear, 6);
            System.out.println("Updated gear required level: " + retrievedGear);

            /************************************************************
             *  EQUIPPED ITEM
             ************************************************************/
            EquippedItem equippedItem = EquippedItemDao.create(connection, retrievedCharacter, SlotType.head, gearHelmet);
            System.out.println("Created EquippedItem: " + equippedItem);
            EquippedItem retrievedEqItem = EquippedItemDao.getByCharacterAndSlot(connection, character.getCharacterID(), SlotType.head);
            System.out.println("Retrieved EquippedItem by PK: " + retrievedEqItem);

            /************************************************************
             *  CONSUMABLE (extends ITEM)
             ************************************************************/
            Consumable potion = ConsumableDao.create(connection, "HealthPotion", 1, 2, 30, "Restores 100 HP");
            System.out.println("Created Consumable: " + potion);
            Consumable retrievedPotion = ConsumableDao.getConsumableByID(connection, potion.getItemID());
            System.out.println("Retrieved Consumable by ID: " + retrievedPotion);
            ConsumableDao.updateDescription(connection, retrievedPotion, "Restores 500 HP");
            System.out.println("Updated consumable description: " + retrievedPotion);

            /************************************************************
             *  GEAR JOB (join between GEAR and JOB)
             ************************************************************/
            GearJob gearJob = GearJobDao.create(connection, retrievedGear, retrievedJob);
            System.out.println("Created GearJob: " + gearJob);
            GearJob retrievedGearJob = GearJobDao.getByItemAndJob(connection, retrievedGear.getItemID(), retrievedJob.getJobID());
            System.out.println("Retrieved GearJob by PK: " + retrievedGearJob);
            GearJobDao.updateJob(connection, retrievedGearJob, job);  // no real change, just demonstration
            System.out.println("Updated GearJob job: " + retrievedGearJob);

            /************************************************************
             *  STATISTIC BONUS (join between ITEM and STATISTIC TYPE)
             ************************************************************/
            // e.g. add a +20 HP to gearHelmet
            StatisticBonus sbHelmetHP = StatisticBonusDao.create(
                    connection, retrievedGear, statTypeHP, "Flat", 20, null, null);
            System.out.println("Created StatisticBonus for gear helmet +HP: " + sbHelmetHP);
            StatisticBonus retrievedSB = StatisticBonusDao.getStatisticBonus(
                    connection, retrievedGear.getItemID(), "HP", "Flat");
            System.out.println("Retrieved StatisticBonus by PK: " + retrievedSB);
            StatisticBonusDao.updateFlatValue(connection, retrievedSB, 50);
            System.out.println("Updated StatisticBonus flat value: " + retrievedSB);

            /************************************************************
             *  CHARACTER STATISTICS (join between CHARACTER and STATISTIC TYPE)
             ************************************************************/
            CharacterStatistics charStatsHP = CharacterStatisticsDao.create(
                    connection, retrievedCharacter, statTypeHP, 500);
            System.out.println("Created CharacterStatistics: " + charStatsHP);
            CharacterStatistics retrievedCSHP = CharacterStatisticsDao.getByCharacterAndStat(
                    connection, character.getCharacterID(), "HP");
            System.out.println("Retrieved CharacterStatistics by PK: " + retrievedCSHP);
            CharacterStatisticsDao.updateStatValue(connection, retrievedCSHP, 600);
            System.out.println("Updated CharacterStatistics statValue: " + retrievedCSHP);

            // Demonstrate non-PK search (CharacterStatisticsDao.getByCharacter)
            List<CharacterStatistics> statsForChar = CharacterStatisticsDao.getByCharacter(connection, retrievedCharacter);
            System.out.println("All stats for character: " + statsForChar);

            /************************************************************
             *  CLEAN-UP: DELETE in an order respecting foreign keys
             ************************************************************/

            // 1) Child records in join tables
            CharacterStatisticsDao.delete(connection, retrievedCSHP);
            StatisticBonusDao.delete(connection, retrievedSB);
            GearJobDao.delete(connection, retrievedGearJob);
            EquippedItemDao.delete(connection, retrievedEqItem);
            CharacterJobDao.delete(connection, retrievedCharJob);
            CharacterCurrencyDao.delete(connection, retrievedCharCurr);

            // 2) Gear, Consumable, Item
            GearDao.delete(connection, retrievedGear);
            ConsumableDao.delete(connection, retrievedPotion);
            ItemDao.delete(connection, retrievedItem);

            // 3) Character
            CharacterDao.delete(connection, retrievedCharacter);

            // 4) Clan, Currency, Job
            ClanDao.delete(connection, retrievedClan);
            CurrencyDao.delete(connection, retrievedCurrency);
            JobDao.delete(connection, retrievedJob);

            // 5) Player
            PlayerDao.delete(connection, retrievedPlayer);

            // 6) StatisticType
            StatisticTypeDao.delete(connection, statTypeHP);
            StatisticTypeDao.delete(connection, statTypeMana);

            // 7) Race
            RaceDao.delete(connection, retrievedRace);

            System.out.println("All records deleted successfully. Demo complete.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drops (if exists) and recreates the CS5200Project schema.
     */
    private static void resetSchema() {
        try (
            Connection schemalessCxn = ConnectionManager.getSchemalessConnection();
            Statement stmt = schemalessCxn.createStatement()
        ) {
            stmt.executeUpdate("DROP SCHEMA IF EXISTS CS5200Project;");
            stmt.executeUpdate("CREATE SCHEMA CS5200Project;");
            System.out.println("Schema CS5200Project reset successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates all tables for the classes you’ve shown in your model.
     */
    private static void createTables() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement stmt = connection.createStatement()) {

            // Race
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Race (
                  raceID INT AUTO_INCREMENT,
                  raceName VARCHAR(255) NOT NULL,
                  PRIMARY KEY (raceID)
                )
            """);

            // StatisticType
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.StatisticType (
                  statName VARCHAR(255) NOT NULL,
                  description VARCHAR(255),
                  PRIMARY KEY (statName)
                )
            """);

            // Player
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Player (
                  playerID INT AUTO_INCREMENT,
                  fullName VARCHAR(255) NOT NULL,
                  email VARCHAR(255) NOT NULL,
                  PRIMARY KEY (playerID),
                  UNIQUE(email)
                )
            """);

            // Clan
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Clan (
                  clanID INT AUTO_INCREMENT,
                  clanName VARCHAR(255) NOT NULL,
                  raceID INT NOT NULL,
                  PRIMARY KEY (clanID),
                  FOREIGN KEY (raceID)
                    REFERENCES CS5200Project.Race(raceID)
                    ON DELETE CASCADE
                )
            """);

            // Character
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.`Character` (
                  characterID INT AUTO_INCREMENT,
                  playerID INT NOT NULL,
                  clanID INT NOT NULL,
                  firstName VARCHAR(255) NOT NULL,
                  lastName VARCHAR(255) NOT NULL,
                  PRIMARY KEY (characterID),
                  FOREIGN KEY (playerID)
                    REFERENCES CS5200Project.Player(playerID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (clanID)
                    REFERENCES CS5200Project.Clan(clanID)
                    ON DELETE CASCADE
                )
            """);

            // Job
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Job (
                  jobID VARCHAR(255) NOT NULL,
                  jobName VARCHAR(255) NOT NULL,
                  PRIMARY KEY (jobID)
                )
            """);

            // CharacterJob
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.CharacterJob (
                  characterID INT NOT NULL,
                  jobID VARCHAR(255) NOT NULL,
                  level INT NOT NULL,
                  experiencePoints INT NOT NULL,
                  PRIMARY KEY (characterID, jobID),
                  FOREIGN KEY (characterID)
                    REFERENCES CS5200Project.`Character`(characterID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (jobID)
                    REFERENCES CS5200Project.Job(jobID)
                    ON DELETE CASCADE
                )
            """);

            // Currency
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Currency (
                  currencyID INT AUTO_INCREMENT,
                  currencyName VARCHAR(255) NOT NULL,
                  cap INT,
                  weeklyCap INT,
                  PRIMARY KEY (currencyID)
                )
            """);

            // CharacterCurrency
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.CharacterCurrency (
                  characterID INT NOT NULL,
                  currencyID INT NOT NULL,
                  amount INT NOT NULL,
                  weeklyAmountAcquired INT NOT NULL,
                  PRIMARY KEY (characterID, currencyID),
                  FOREIGN KEY (characterID)
                    REFERENCES CS5200Project.`Character`(characterID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (currencyID)
                    REFERENCES CS5200Project.Currency(currencyID)
                    ON DELETE CASCADE
                )
            """);

            // Item
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Item (
                  itemID INT AUTO_INCREMENT,
                  itemName VARCHAR(255) NOT NULL,
                  itemLevel INT NOT NULL,
                  itemMaxStackSize INT NOT NULL,
                  itemPrice INT NOT NULL,
                  PRIMARY KEY (itemID)
                )
            """);

            // Gear
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Gear (
                  itemID INT NOT NULL,
                  gearType VARCHAR(255) NOT NULL,
                  slot VARCHAR(255) NOT NULL,
                  requiredLevel INT NOT NULL,
                  damage INT,
                  PRIMARY KEY (itemID),
                  FOREIGN KEY (itemID)
                    REFERENCES CS5200Project.Item(itemID)
                    ON DELETE CASCADE
                )
            """);

            // Consumable
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.Consumable (
                  itemID INT NOT NULL,
                  description VARCHAR(255) NOT NULL,
                  PRIMARY KEY (itemID),
                  FOREIGN KEY (itemID)
                    REFERENCES CS5200Project.Item(itemID)
                    ON DELETE CASCADE
                )
            """);

            // EquippedItem
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.EquippedItem (
                  characterID INT NOT NULL,
                  slot VARCHAR(50) NOT NULL,
                  itemID INT NOT NULL,
                  PRIMARY KEY (characterID, slot),
                  FOREIGN KEY (characterID)
                    REFERENCES CS5200Project.`Character`(characterID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (itemID)
                    REFERENCES CS5200Project.Gear(itemID)
                    ON DELETE CASCADE
                )
            """);

            // GearJob
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.GearJob (
                  itemID INT NOT NULL,
                  jobID VARCHAR(255) NOT NULL,
                  PRIMARY KEY (itemID, jobID),
                  FOREIGN KEY (itemID)
                    REFERENCES CS5200Project.Gear(itemID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (jobID)
                    REFERENCES CS5200Project.Job(jobID)
                    ON DELETE CASCADE
                )
            """);

            // StatisticBonus
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.StatisticBonus (
                  itemID INT NOT NULL,
                  statName VARCHAR(255) NOT NULL,
                  bonusType VARCHAR(255) NOT NULL,
                  bonusFlatValue INT,
                  bonusPercentageValue FLOAT,
                  bonusCap INT,
                  PRIMARY KEY (itemID, statName, bonusType),
                  FOREIGN KEY (itemID) REFERENCES CS5200Project.Item(itemID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (statName) REFERENCES CS5200Project.StatisticType(statName)
                    ON DELETE CASCADE
                )
            """);

            // CharacterStatistics
            stmt.executeUpdate("""
                CREATE TABLE CS5200Project.CharacterStatistics (
                  characterID INT NOT NULL,
                  statName VARCHAR(255) NOT NULL,
                  statValue INT NOT NULL,
                  PRIMARY KEY (characterID, statName),
                  FOREIGN KEY (characterID)
                    REFERENCES CS5200Project.`Character`(characterID)
                    ON DELETE CASCADE,
                  FOREIGN KEY (statName)
                    REFERENCES CS5200Project.StatisticType(statName)
                    ON DELETE CASCADE
                )
            """);

            System.out.println("All tables created successfully in CS5200Project schema.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
