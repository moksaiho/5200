# 5200
# CS5200 PM3 JDBC Project

## Overview

This project implements a game-style database in MySQL with corresponding Java model classes and DAO (Data Access Object) classes. The database stores:

- **Players** (individual game accounts)
- **Races** (like Human, Elf, etc.)
- **Clans** (groups associated with a certain Race)
- **Characters** (belong to a Player and a Clan)
- **Jobs** (e.g., Warrior, Mage), linked to Characters via a join table
- **Currencies** (in-game monetary types), also linked to Characters
- **Items** (base table)
  - **Gear** (specific equipment items, referencing Item)
  - **Consumable** (potions, etc., referencing Item)

We’ve created Java DAO classes for these tables, each supporting create/read/update/delete operations using JDBC with prepared statements.

## DAO Methods Covered

Each DAO class has:

- **Create** – e.g., `RaceDao.create()`, `PlayerDao.create()`, etc.
- **Read by primary key** – e.g., `RaceDao.getRaceByID()`, `PlayerDao.getPlayerByID()`
- **Read by non-PK attribute** – e.g., `RaceDao.getRacesByName()`, `PlayerDao.getPlayersByEmailDomain()`, etc.
- **Update** – e.g., `RaceDao.updateRaceName()`, `PlayerDao.updateEmail()`
- **Delete** – e.g., `RaceDao.delete()`, `PlayerDao.delete()`

Each DAO method uses **prepared statements** to avoid SQL injection risks.

## How to Compile and Run

1. **MySQL Setup**  
   - Ensure you have a local or remote MySQL server running.
   - In `ConnectionManager.java`, update `USER`, `PASSWORD`, `HOSTNAME`, `PORT`, and `SCHEMA` if needed.

2. **Compile**  
   - Make sure all `.java` files (model classes, DAO classes, `ConnectionManager.java`, and `Driver.java`) plus the MySQL Connector/J jar are in your classpath. Example command (Linux/macOS):
     ```bash
     javac -cp .:mysql-connector-java-X.Y.Z.jar game/model/*.java game/dal/*.java game/Driver.java
     ```
   - On Windows, separate classpath entries with a semicolon (`;`).

3. **Run**  
   - After successful compilation, run the `Driver` class:
     ```bash
     java -cp .:mysql-connector-java-X.Y.Z.jar game.Driver
     ```
   - The program will:
     1. Drop any existing schema named `CS5200Project`.
     2. Create a fresh `CS5200Project` schema.
     3. Create all the necessary tables.
     4. Invoke DAO methods to insert (CREATE), select (READ), update, and delete data in the new schema.

## Notes

- **Schema Reset**: Running the driver will destroy any existing `CS5200Project` schema.  
- **Foreign Keys**: The sample code uses `ON DELETE CASCADE` in many places, so child records are removed automatically when the parent record is deleted (or we do it in an explicit order).  
- **Extend Further**: If you create additional DAOs (e.g., `StatisticTypeDao`, `InventoryDao`, etc.), you can add matching `CREATE TABLE` statements in the `createTables()` method and call them in `Driver` to exercise all CRUD functions. 
- **References**: For PM3, you only need to demonstrate that each table’s DAO class can do the four required operations. The provided `Driver` code covers that with simple test data. You can refine or expand it as needed for future milestones.
