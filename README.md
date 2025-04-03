CS5200: Milestone 3
Team: [Your Team Name]
Date: [Submission Date]

1. Overview
Our project models a game database with the following entities:

Player – represents a user account

Race – e.g. “Human,” “Elf,” etc.

Clan – belongs to a specific Race

Character – each belongs to a Player and a Clan

Job – “class” or “profession,” with a many-to-many relationship to Character

Item (base table)

Gear – extends Item for equippable objects

Consumable – extends Item for single-use objects

EquippedItem – join for which gear is worn by a character

Currency and CharacterCurrency – tracks in-game money for each character

StatisticType – e.g. HP, MP

StatisticBonus – join between Item and StatisticType (items that increase stats)

CharacterStatistics – join between Character and StatisticType

2. DAO Classes Implemented
We have one DAO class per non-enumeration table. Each DAO class implements:

create – inserts a record

read by PK – fetches a single record by primary key

read by non-PK – e.g., by name, email domain, or foreign key references

update – modifies one field

delete – removes the record

CRUD Methods Summary
RaceDao

create, getRaceByID (PK), getRacesByName (non-PK), updateRaceName, delete

PlayerDao

create, getPlayerByID, getPlayersByEmailDomain (non-PK), updateEmail, delete

ClanDao

create, getClanByID, getClansByRace (non-PK), updateClanName, delete

CharacterDao

create, getCharacterByID, [optionally getCharactersByName], update methods if needed, delete

JobDao

create, getJobByID, getJobsByName (non-PK), updateJobName, delete

CharacterJobDao (join)

create, getByCharacterAndJob (PK), getJobsForCharacter (non-PK), updateExperiencePoints, delete

CurrencyDao

create, getCurrencyByID, [optionally get by name], [optionally update], delete

CharacterCurrencyDao (join)

create, getCharacterCurrencyByID, [optionally get for a character], updateAmounts, delete

ItemDao

create, getItemByID, getItemsByLevel (non-PK), updateItemPrice, delete

GearDao

create, getGearByID, getGearBySlot (non-PK), updateRequiredLevel, delete

ConsumableDao

create, getConsumableByID, getConsumablesByDescription (non-PK), updateDescription, delete

EquippedItemDao

create, getByCharacterAndSlot (PK), getByCharacter (non-PK), updateEquippedGear, delete

GearJobDao

create, getByItemAndJob (PK), getByGear / getByJob (non-PK), updateJob, delete

StatisticTypeDao

create, getStatisticTypeByName (PK), [getStatisticTypeByDescription], updateDescription, delete

StatisticBonusDao

create, getStatisticBonus (PK), getBonusesByItem (non-PK), updateFlatValue, delete

CharacterStatisticsDao

create, getByCharacterAndStat (PK), getByCharacter (non-PK), updateStatValue, delete

3. Driver Class
Driver.java automates:

resetSchema() – drops & recreates the CS5200Project schema.

createTables() – runs all CREATE TABLE statements.

Creates sample data in each table using create(...).

Demonstrates reading data by PK and non-PK.

Demonstrates updating attributes.

Deletes records in an order respecting foreign keys.

Upon execution, it prints out the intermediate steps and confirms each operation works as expected.

4. How to Compile and Run
Update ConnectionManager – Make sure USER, PASSWORD, and DB info are correct.

Compile all .java files (model, DAO, Driver.java) plus the MySQL connector JAR in your classpath.

Run the Driver class with the MySQL connector JAR. The console output will display the creation, reading, updating, and deletion sequences.

5. Security / SQL Injection Prevention
We consistently use PreparedStatement with ? placeholders, ensuring user input is always parameterized. This approach avoids string concatenation in SQL statements, significantly reducing injection risk.

6. Conclusion
This submission meets the PM3 requirements:

One DAO per non-enumeration table, covering all CRUD methods.

A Driver class that resets the schema, recreates tables, and demonstrates the DAOs with sample data.

The code uses prepared statements for security, matching best practices taught in class.
