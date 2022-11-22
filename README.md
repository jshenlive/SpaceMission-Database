# Welcome to Space Missions Database

Created by Andrea Nguyen, Jesse Shen, and Roselle Deinla
For Comp 3380, Fall 2022

Github Repo: https://github.com/jshenlive/SpaceMission-Database


## Introduction


This project includes datasets from space missions ranging from 1957 - 2022, international astronauts, international agencies, international spacecrafts used in missions, international spacecraft manufacturers and films featured based on space missions. 

Some relational findings include, top astronaults with most flight time per country, most expensive projects, sucesseful/failed missions based on average funding, searching project and spacecraft flew by astronaults, and more...

The program program is created with Java and MS SQL Server was used for the Database.
In order to run properly, make sure that a java complier is installed for the system Command Line Interface(CLI).

### Screenshots
!["screenshot on Queries List"](https://github.com/jshenlive/SpaceMission-Database/blob/main/screenshots/queriesList.jpg)

!["screenshot on Query 11"](https://github.com/jshenlive/SpaceMission-Database/blob/main/screenshots/query11.jpg)

!["screenshot on Query built"](https://github.com/jshenlive/SpaceMission-Database/blob/main/screenshots/built.jpg)

!["screenshot on Query films"](https://github.com/jshenlive/SpaceMission-Database/blob/main/screenshots/films.jpg)

!["screenshot on custom Query"](https://github.com/jshenlive/SpaceMission-Database/blob/main/screenshots/customQuery.jpg)



## Project Setup


### Startup
* Type `make run` in CLI from the main directory folder to start program.
* Type `h` for help, `q` to quit program at any point and `queries` for queries list.

### Initializing Database
1. Update the `auth.cfg` file with your correct server login info.
   - username= `your_username`
   - password= `your_password` 
2. Make sure seed files are in seeds folder
3. Run java program by using command: `make run` in CLI
4. Enter `init` in CLI to reset, create, and add all seed files


## Queries


* Queries List can be found by entering `queries`

* For queries that do not require inputs, users can then type the desired query phrase into the console and hit enter to see the results.

* For queries that require one or more inputs, the user must first specify the query phase from the query menu, followed by the inputs. 
  * For numerical inputs, such as projectID or personID simply enter the query option followed by the ID in one line.
    * E.g. for spacecrafts flew by astronaut with personID = 7:

            db > flew 7

  * For multiple values, type in each input followed by a space. Once all inputs have been typed, hit enter.
    * E.g. To view all projects from Canada that occurred between 2015 and 2020:

            db >  `7`
            db >  `Canada 2015 2020`


## Relational Model


-	agencies(**agencyName**, agencyAcr, countryName) altname,yearFound,evActivity,spaceRD,spaceStation,humanFlight,multipleSat,extraProbe)
-	astronaut(**personID**,firstname,lastname,numFlight,totalFlightTime)
-	builtBy(**builtBy**,manufacturerName,spacecraftName)
-	country(**countryName**, continent)
-	films(**filmID**, filmName,releasedYear, filmLength,spacecraftName)
-	flewBy(**flightID**, spacecraftName, personID, flightYear)
-	funding(**fundID**, projectID, agencyAcr, cost)
-	manufacturers(**manufacturerName**, yearFound)
-	nationality(**personID**, countryName)
-	projects(**projectID**, launchMonth, flightYear, launchLocation, countryName, missionStatus)
-	satellite(**spacecraftName**, orbit,orbitType,launchVehicle)
-	spacecraft(**spacecraftName**, missionType)
-	spacecraftUsed(**spacecraftUsedID**, projectID, spacecraftName, launcherName)


--------------
#### Dataset References
- Dataset were collected through opensource media such as Wikipedia, Kaggle, and DatasetSearch(google). This project was created for learning purposes and we do not warranty accuracy of any data provided.
-------
Nathan Nguyen also contributed in selecting/planning the database, however he was unable to continue working on this project after the planning phase. 
