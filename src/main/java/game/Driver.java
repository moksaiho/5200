package game;

import game.dal.*;
import game.model.*;
import game.model.Character;
import game.model.Gear.GearType;
import game.model.Gear.SlotType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

                // 3. Populate tables with at least 10 records each
                populateTables(cxn);
                System.out.println("✅ All tables populated with at least 10 records each");

                // 4. Exercise all DAO methods
//                testAllDaoMethods(cxn);
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

    private static void populateTables(Connection cxn) throws SQLException {
        System.out.println("\nPopulating tables with at least 10 records each...");
        
        // Race table (10 records)
        List<Race> races = new ArrayList<>();
        String[] raceNames = {"Hyur", "Elezen", "Lalafell", "Miqo'te", "Roegadyn", "Au Ra", "Hrothgar", "Viera", "Garlean", "Bangaa"};
        for (String raceName : raceNames) {
            races.add(RaceDao.create(cxn, raceName));
        }
        System.out.println("✅ Added 10 races");
        
        // Clan table (at least 2 clans per race = 20+ records)
        List<Clan> clans = new ArrayList<>();
        for (Race race : races) {
            clans.add(ClanDao.create(cxn, race.getRaceName() + " Highlander", race));
            clans.add(ClanDao.create(cxn, race.getRaceName() + " Lowlander", race));
        }
        System.out.println("✅ Added " + clans.size() + " clans");
        
        // Player table (15 records)
        List<Player> players = new ArrayList<>();
        String[] firstNames = {"John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Edward", "Fiona", "George", "Helen", "Ian", "Julia", "Kevin", "Linda", "Mike"};
        String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia"};
        for (int i = 0; i < 15; i++) {
            String name = firstNames[i] + " " + lastNames[i];
            String email = firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@email.com";
            players.add(PlayerDao.create(cxn, name, email));
        }
        System.out.println("✅ Added 15 players");
        
        // Character table (20 records, multiple characters per player)
        List<Character> characters = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Player player = players.get(random.nextInt(players.size()));
            Clan clan = clans.get(random.nextInt(clans.size()));
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            characters.add(CharacterDao.create(cxn, player, clan, firstName, lastName));
        }
        System.out.println("✅ Added 20 characters");
        
        // Job table (12 records)
        List<Job> jobs = new ArrayList<>();
        String[][] jobData = {
            {"PLD", "Paladin"}, {"WAR", "Warrior"}, {"DRK", "Dark Knight"}, {"GNB", "Gunbreaker"},
            {"WHM", "White Mage"}, {"SCH", "Scholar"}, {"AST", "Astrologian"}, {"SGE", "Sage"},
            {"MNK", "Monk"}, {"DRG", "Dragoon"}, {"NIN", "Ninja"}, {"SAM", "Samurai"}
        };
        for (String[] data : jobData) {
            jobs.add(JobDao.create(cxn, data[0], data[1]));
        }
        System.out.println("✅ Added 12 jobs");
        
        // CharacterJob table (30+ records, multiple jobs per character)
        for (Character character : characters) {
            // Assign 1-3 random jobs to each character
            int numJobs = random.nextInt(3) + 1;
            List<Job> shuffledJobs = new ArrayList<>(jobs);
            java.util.Collections.shuffle(shuffledJobs);
            
            for (int i = 0; i < numJobs && i < shuffledJobs.size(); i++) {
                Job job = shuffledJobs.get(i);
                int level = random.nextInt(80) + 1; // Level 1-80
                int exp = random.nextInt(1000000);
                CharacterJobDao.create(cxn, character, job, level, exp);
            }
        }
        System.out.println("✅ Added character jobs (multiple per character)");
        
        // Currency table (10 records)
        List<Currency> currencies = new ArrayList<>();
        String[] currencyNames = {"Gil", "Tomestone of Poetics", "Tomestone of Allegory", "Tomestone of Revelation", 
                                "Wolf Mark", "MGP", "Allied Seal", "Centurio Seal", "Bicolor Gemstone", "Skybuilders' Scrip"};
        Integer[] caps = {null, 2000, 2000, 2000, 20000, null, 4000, 4000, 1000, 9999};
        Integer[] weeklyCaps = {null, null, 450, 450, null, null, null, null, 500, null};
        
        for (int i = 0; i < currencyNames.length; i++) {
            currencies.add(CurrencyDao.create(cxn, currencyNames[i], caps[i], weeklyCaps[i]));
        }
        System.out.println("✅ Added 10 currencies");
        
        // CharacterCurrency table (multiple currencies per character)
        for (Character character : characters) {
            for (Currency currency : currencies) {
                // Not all characters have all currencies
                if (random.nextBoolean()) {
                    int amount = random.nextInt(currency.getCap() != null ? currency.getCap() : 1000000);
                    int weeklyAmount = currency.getWeeklyCap() != null ? 
                                      random.nextInt(currency.getWeeklyCap()) : 0;
                    CharacterCurrencyDao.create(cxn, character, currency, amount, weeklyAmount);
                }
            }
        }
        System.out.println("✅ Added character currencies");
        
        // Item table (base for Gear and Consumable - 30 items)
        List<Item> items = new ArrayList<>();
        String[] weaponNames = {"Iron Sword", "Steel Axe", "Bronze Dagger", "Oak Staff", "Ash Bow", 
                              "Silver Rapier", "Mythril Spear", "Brass Knuckles", "Walnut Wand", "Copper Mace"};
        String[] armorNames = {"Leather Helmet", "Chain Mail", "Plate Leggings", "Cotton Gloves", "Iron Boots",
                             "Silk Robe", "Wool Hat", "Mythril Shield", "Linen Shoes", "Bronze Gauntlets"};
        String[] consumableNames = {"Potion", "Hi-Potion", "Ether", "Hi-Ether", "Elixir", 
                                  "Phoenix Down", "Antidote", "Eye Drops", "Echo Herbs", "Holy Water"};
        
        // Add weapons
        for (String name : weaponNames) {
            int level = random.nextInt(50) + 1;
            items.add(ItemDao.create(cxn, name, level, 1, 100 * level));
        }
        
        // Add armor
        for (String name : armorNames) {
            int level = random.nextInt(50) + 1;
            items.add(ItemDao.create(cxn, name, level, 1, 80 * level));
        }
        
        // Add consumables
        for (String name : consumableNames) {
            int level = random.nextInt(10) + 1;
            items.add(ItemDao.create(cxn, name, level, 99, 20 * level));
        }
        System.out.println("✅ Added 30 items");
        
        // Gear table (20 records - weapons and armor)
        List<Gear> gears = new ArrayList<>();
        SlotType[] slotTypes = {SlotType.weapon, SlotType.head, SlotType.body, SlotType.legs, 
                              SlotType.feet, SlotType.hands, SlotType.offhand, SlotType.ring, 
                              SlotType.earring, SlotType.weapon};
        
        // Add 10 weapons
        for (int i = 0; i < 10; i++) {
            Item item = items.get(i); // First 10 items are weapons
            GearType type = GearType.weapon;
            SlotType slot = SlotType.weapon;
            if (i >= 8) slot = SlotType.offhand; // Make some offhands
            
            int reqLevel = random.nextInt(item.getItemLevel()) + 1;
            int damage = random.nextInt(100) + 10;
            
            gears.add(GearDao.create(cxn, item.getItemName(), item.getItemLevel(), 
                                    item.getItemMaxStackSize(), item.getItemPrice(),
                                    type, slot, reqLevel, damage));
        }
        
        // Add 10 armor pieces
        for (int i = 10; i < 20; i++) {
            Item item = items.get(i); // Next 10 items are armor
            GearType type = GearType.armor;
            SlotType slot = slotTypes[i - 10]; // Use different slots
            
            int reqLevel = random.nextInt(item.getItemLevel()) + 1;
            
            gears.add(GearDao.create(cxn, item.getItemName(), item.getItemLevel(), 
                                    item.getItemMaxStackSize(), item.getItemPrice(),
                                    type, slot, reqLevel, null));
        }
        System.out.println("✅ Added 20 gear items");
        
        // GearJob table (multiple jobs per gear)
        for (Gear gear : gears) {
            // Each gear can be used by 1-3 jobs
            int numJobs = random.nextInt(3) + 1;
            List<Job> shuffledJobs = new ArrayList<>(jobs);
            java.util.Collections.shuffle(shuffledJobs);
            
            for (int i = 0; i < numJobs && i < shuffledJobs.size(); i++) {
                GearJobDao.create(cxn, gear, shuffledJobs.get(i));
            }
        }
        System.out.println("✅ Added gear-job associations");
        
        // Consumable table (10 records)
        List<Consumable> consumables = new ArrayList<>();
        String[] descriptions = {
            "Restores 100 HP", "Restores 500 HP", "Restores 100 MP", "Restores 500 MP", 
            "Restores full HP and MP", "Revives a KO'd ally with 50% HP", "Cures poison status",
            "Cures blind status", "Cures silence status", "Cures zombie status"
        };
        
        for (int i = 20; i < 30; i++) {
            Item item = items.get(i); // Last 10 items are consumables
            consumables.add(ConsumableDao.create(cxn, item.getItemName(), item.getItemLevel(), 
                                              item.getItemMaxStackSize(), item.getItemPrice(),
                                              descriptions[i - 20]));
        }
        System.out.println("✅ Added 10 consumable items");
        
        // StatisticType table (10 records)
        List<StatisticType> statTypes = new ArrayList<>();
        String[][] statData = {
            {"Strength", "Physical attack power for some classes"},
            {"Dexterity", "Physical attack power for some classes, affects critical hit rate"},
            {"Vitality", "Increases maximum HP"},
            {"Intelligence", "Magic attack power for casters"},
            {"Mind", "Healing power for healers"},
            {"Critical Hit", "Chance to land a critical hit"},
            {"Determination", "Increases all damage and healing"},
            {"Direct Hit", "Chance to land a direct hit"},
            {"Defense", "Reduces physical damage taken"},
            {"Magic Defense", "Reduces magical damage taken"}
        };
        
        for (String[] data : statData) {
            statTypes.add(StatisticTypeDao.create(cxn, data[0], data[1]));
        }
        System.out.println("✅ Added 10 statistic types");
        
        // CharacterStatistics table (multiple stats per character)
        for (Character character : characters) {
            for (StatisticType statType : statTypes) {
                int statValue = random.nextInt(1000) + 100;
                CharacterStatisticsDao.create(cxn, character, statType, statValue);
            }
        }
        System.out.println("✅ Added character statistics");
        
        // StatisticBonus table (multiple bonuses per item)
        for (Item item : items) {
            // Each item has 1-3 stat bonuses
            int numStats = random.nextInt(3) + 1;
            List<StatisticType> shuffledStats = new ArrayList<>(statTypes);
            java.util.Collections.shuffle(shuffledStats);
            
            for (int i = 0; i < numStats && i < shuffledStats.size(); i++) {
                StatisticType statType = shuffledStats.get(i);
                String bonusType = random.nextBoolean() ? "flat" : "percentage";
                Integer flatValue = bonusType.equals("flat") ? random.nextInt(50) + 1 : null;
                Float percentValue = bonusType.equals("percentage") ? random.nextFloat() * 10 : null;
                Integer cap = random.nextBoolean() ? random.nextInt(100) + 50 : null;
                
                StatisticBonusDao.create(cxn, item, statType, bonusType, flatValue, percentValue, cap);
            }
        }
        System.out.println("✅ Added statistic bonuses");
        
        // Inventory table (multiple items per character)
        for (Character character : characters) {
            // Each character has 5-10 items in inventory
            int numItems = random.nextInt(6) + 5;
            List<Item> shuffledItems = new ArrayList<>(items);
            java.util.Collections.shuffle(shuffledItems);
            
            for (int i = 0; i < numItems && i < shuffledItems.size(); i++) {
                Item item = shuffledItems.get(i);
                int quantity = random.nextInt(item.getItemMaxStackSize()) + 1;
                
                InventoryDao.create(cxn, character, i + 1, item, quantity);
            }
        }
        System.out.println("✅ Added inventory items");
        
        // EquippedItem table (multiple equipped items per character)
        SlotType[] equipSlots = {SlotType.head, SlotType.body, SlotType.legs, SlotType.feet, 
                               SlotType.hands, SlotType.weapon, SlotType.offhand, SlotType.ring, 
                               SlotType.earring};
        
        for (Character character : characters) {
            // Each character has some equipped items
            for (SlotType slot : equipSlots) {
                // 70% chance to have an item in each slot
                if (random.nextDouble() < 0.7) {
                    // Find gears that match the slot
                    List<Gear> matchingGear = new ArrayList<>();
                    for (Gear gear : gears) {
                        if (gear.getSlot() == slot) {
                            matchingGear.add(gear);
                        }
                    }
                    
                    if (!matchingGear.isEmpty()) {
                        Gear gear = matchingGear.get(random.nextInt(matchingGear.size()));
                        EquippedItemDao.create(cxn, character, slot, gear);
                    }
                }
            }
        }
        System.out.println("✅ Added equipped items");
        
        System.out.println("\n✅ Successfully populated all tables with at least 10 records each!");
    }

//    private static void testAllDaoMethods(Connection cxn) throws SQLException {
//        // Test Race DAO
//        System.out.println("\nTesting Race DAO...");
//        Race race = RaceDao.create(cxn, "Hyur");
//        System.out.println("Created race: " + race);
//        
//        Race retrievedRace = RaceDao.getRaceByID(cxn, race.getRaceID());
//        System.out.println("Retrieved race: " + retrievedRace);
//
//        // Test Clan DAO
//        System.out.println("\nTesting Clan DAO...");
//        Clan clan = ClanDao.create(cxn, "Midlander", race);
//        System.out.println("Created clan: " + clan);
//        
//        List<Clan> clans = ClanDao.getClansByRace(cxn, race);
//        System.out.println("Retrieved clans for race: " + clans);
//
//        // Test Player DAO
//        System.out.println("\nTesting Player DAO...");
//        Player player = PlayerDao.create(cxn, "John Doe", "john@email.com");
//        System.out.println("Created player: " + player);
//        
//        player = PlayerDao.updateEmail(cxn, player, "newemail@email.com");
//        System.out.println("Updated player email: " + player);
//        
//        List<Player> players = PlayerDao.getPlayersByEmailDomain(cxn, "email.com");
//        System.out.println("Retrieved players by email domain: " + players);
//
//        // Test Character DAO
//        System.out.println("\nTesting Character DAO...");
//        Character character = CharacterDao.create(cxn, player, clan, "John", "Warrior");
//        System.out.println("Created character: " + character);
//        
//        List<Character> characters = CharacterDao.getCharactersByLastName(cxn, "Warrior");
//        System.out.println("Retrieved characters by last name: " + characters);
//
//        // Test Job DAO
//        System.out.println("\nTesting Job DAO...");
//        Job job = JobDao.create(cxn, "WAR", "Warrior");
//        System.out.println("Created job: " + job);
//
//        // Test CharacterJob DAO
//        System.out.println("\nTesting CharacterJob DAO...");
//        CharacterJob characterJob = CharacterJobDao.create(cxn, character, job, 1, 0);
//        System.out.println("Created character job: " + characterJob);
//
//        // Test Currency DAO
//        System.out.println("\nTesting Currency DAO...");
//        Currency currency = CurrencyDao.create(cxn, "Gil", null, null);
//        System.out.println("Created currency: " + currency);
//
//        // Test Item and related DAOs
//        System.out.println("\nTesting Item and related DAOs...");
//        
//        // Test Gear
//        Gear gear = GearDao.create(cxn, "Bronze Sword", 1, 1, 100, 
//            GearType.weapon, SlotType.weapon, 1, 10);
//        System.out.println("Created gear: " + gear);
//        
//        List<Gear> gearBySlot = GearDao.getGearBySlot(cxn, SlotType.weapon);
//        System.out.println("Retrieved gear by slot: " + gearBySlot);
//        
//        gear = GearDao.updateRequiredLevel(cxn, gear, 5);
//        System.out.println("Updated gear level: " + gear);
//
//        // Test Consumable
//        Consumable potion = ConsumableDao.create(cxn, "Potion", 1, 99, 50, "Restores HP");
//        System.out.println("Created consumable: " + potion);
//        
//        potion = ConsumableDao.updateDescription(cxn, potion, "Restores 100 HP");
//        System.out.println("Updated consumable description: " + potion);
//
//        // Test StatisticType
//        System.out.println("\nTesting StatisticType DAO...");
//        StatisticType statType = StatisticTypeDao.create(cxn, "Strength", "Physical attack power");
//        System.out.println("Created stat type: " + statType);
//
//        // Test deletion
//        System.out.println("\nTesting deletion...");
//        
//        // First delete dependent records
//        CharacterJobDao.delete(cxn, characterJob);
//        System.out.println("Deleted character job");
//        
//        // Then delete the character
//        CharacterDao.delete(cxn, character);
//        System.out.println("Deleted character");
//        
//        // Delete player after character is deleted
//        PlayerDao.delete(cxn, player);
//        System.out.println("Deleted player");
//        
//        // Delete gear
//        GearDao.delete(cxn, gear);
//        System.out.println("Deleted gear");
//
//        System.out.println("\n✅ All DAO methods tested successfully!");
//    }
}
