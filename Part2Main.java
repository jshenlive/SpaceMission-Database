import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class Part2Main {

    static String[] TABLE_NAMES = {"agencies","astronaut","projects","spacecraft","satellite","manufacturers","country","films","builtBy","flewBy","funding","nationality","spacecraftUsed"};

    static String adminPass = "";

    static ArrayList<ArrayList<String[]>> table_seeds = new ArrayList<>();

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {

      introMessage();

      Properties prop = new Properties();
      String fileName = "auth.cfg";
      try {
          FileInputStream configFile = new FileInputStream(fileName);
          prop.load(configFile);
          configFile.close();
      } catch (FileNotFoundException ex) {
          System.out.println("Could not find config file.");
          System.exit(1);
      } catch (IOException ex) {
          System.out.println("Error reading config file.");
          System.exit(1);
      }
      String username = (prop.getProperty("username"));
      String password = (prop.getProperty("password"));
      adminPass = (prop.getProperty("admin_pass"));

      if (username == null || password == null){
          System.out.println("Username or password not provided.");
          System.exit(1);
      }

      String connectionUrl =
              "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
              + "database=cs3380;"
              + "user=" + username + ";"
              + "password="+ password +";"
              + "encrypt=false;"
              + "trustServerCertificate=false;"
              + "loginTimeout=30;";



      try (Connection connection = DriverManager.getConnection(connectionUrl)){
        System.out.println("Connection established succesfully.");

        runConsole(connection);
      }
            
        catch (SQLException e) {
          System.out.println("Error connecting Database, please check server and login info.");  
          // e.printStackTrace();
            
        }
      
      

        // connectSQL();
    }

    private static void introMessage(){

      String intro = 
      "\n"+
      "                           ⋆｡°✩\n" +
      "\n"+
      "⋆｡˚ ☁︎ ˚｡⋆｡˚☽˚｡⋆ WELCOME TO THE SPACE MISSIONS DATABASE⋆｡˚ ☁︎ ˚｡⋆｡˚☽˚｡⋆\n"+
      "\n"+
      "                        ⋆⁺₊⋆ ☾⋆⁺₊⋆\n"+
      "               * ･ ｡ﾟ☆━ ੧༼ •́ ヮ •̀ ༽୨\n"+
      "\n"+
      "\n"+
      "Created by Andrea Nguyen, Jesse Shen and Roselle Deinla\n"+
      "For Comp 3380, Fall 2022"+
      "\n"+
      "\n"+
      "Attempting to connect to MSSQL database: jdbc:sqlserver://uranium.cs.umanitoba.ca:1433"
      ;
       System.out.println(intro);
       
      }

    private static void runConsole(Connection connection){
      
      Scanner console = new Scanner(System.in);

      boolean exit = false;

      while(!exit){
        System.out.println("Please enter new command to continue: (Type h for help.) ");
        System.out.print("db > ");
        String line = console.nextLine();

        String[] parts = line.split("\\s+");
        // String arg = "";

        //first arg after command
        // if (line.indexOf(" ")>0)
        //   arg = line.substring(line.indexOf(" ")).trim();

        if (parts[0].equals("h")){
          printHelp();
        } else if (parts[0].equals("q")){
          exit = true;
        } else if (parts[0].equals("init")){
          initiateDB(connection);
        } else if (parts[0].equals("queries")){
          printQueryList();

        }else if (parts[0].equals("query")){
          System.out.println("Please enter admin password for custom select queries: ");
          System.out.print("db > ");
          String pass = console.nextLine();
          if(pass.equals(adminPass)){
            System.out.println("Logged in Success");
            System.out.println("CAUTION: This option may be dangerous if used for delete/update/insert. USE ONLY SELECT QUERIES");
            System.out.println("Would you like to continue? y/n");
            System.out.print("db > ");
            String ans = console.nextLine();
            if(ans.equals("y")){
              System.out.println("Enter your select query:");
              System.out.print("db > ");
              String arg = console.nextLine();
              querySelect(connection, arg);
            }
          }

        
        } else if (parts[0].equals("missions")){
          queryMissions(connection);
        } else if (parts[0].equals("agencies")){
          queryAgencies(connection);
        } else if (parts[0].equals("spacecrafts")){
          querySpacecrafts(connection);
        } else if (parts[0].equals("astronauts")){
          queryAstronauts(connection);
        } else if (parts[0].equals("flew")){
          if (parts.length == 2){
          queryFlew(connection, parts[1]);
          }else{
            System.out.println("wrong number of arguments");
            System.out.println("E.g. flew 4, for data on personID 4");
            
          }
        }else if (parts[0].equals("built")){
          System.out.println("Enter manufacturer Name:");
          System.out.println("E.g. SpaceX");
          System.out.print("db > ");
          String arg1 = console.nextLine();
          queryBuilt(connection,arg1);

        
        }else if (parts[0].equals("1")){
          queryOne(connection);
        } else if (parts[0].equals("2")){
          queryTwo(connection);
        } else if (parts[0].equals("3")){
          if(parts.length == 3){
            queryThree(connection, parts[1], parts[2]);
          }else{
            System.out.println("Invalid amount of arguments! E.g. 3 Neil Armstrong or 3 n a");
          }
        } else if (parts[0].equals("4")){
          queryFour(connection);
        } else if (parts[0].equals("5")){
          queryFive(connection);
        } else if (parts[0].equals("6")){
          querySix(connection);
        } else if (parts[0].equals("7")){
          System.out.println("Enter country name: E.g. United States");
          System.out.print("db > ");
          String arg1 = console.nextLine();
          System.out.println("Enter start year: ie. 2015");
          System.out.print("db > ");
          String arg2 = console.nextLine();
          System.out.println("Enter end year: ie. 2017");
          System.out.print("db > ");
          String arg3 = console.nextLine();
          querySeven(connection, arg1, arg2, arg3);

        } else if (parts[0].equals("8")){
          queryEight(connection);
        } else if (parts[0].equals("9")){
          queryNine(connection);
        } else if (parts[0].equals("10")){
          queryTen(connection);
        } else if (parts[0].equals("11")){
          if (parts.length ==2){
            queryEleven(connection, parts[1]);
          }else{
            System.out.println("Invalid number of arugments, please try again.");
            System.out.println("Example: 11 55 (for search projectID 55)");
          }
          
        } else if (parts[0].equals("12")){
          if (parts.length ==2){
            queryTwelve(connection, parts[1]);
          }else{
            System.out.println("Invalid number of arugments, please try again.");
            System.out.println("Example: 12 55 (for search projectID 55)");
          }
        } else if (parts[0].equals("13")){
          System.out.println("Enter spacecraft name: ie. Apollo 16");
          System.out.print("db > ");
          String arg1 = console.nextLine();

          queryThirteen(connection, arg1);

        } else if (parts[0].equals("films")){
          queryFilms(connection);
        
        }else{
          System.out.println(parts[0] + " is not a valid command");
          System.out.println (" Please try again or type h for help / queries for queries list \n");
        }
        
      }

      System.out.println("Exiting.....");
      System.out.println("Thank you for using our Space Missions Database. GoodBye.");
      console.close();
    };

    private static void printHelp() {
      System.out.println("--------------------------------------");
      System.out.println("SPACE MISSIONS DATABASE HELP MENU");
      System.out.println("Commands:");
      System.out.println("h - Get help");
      System.out.println("init - Initiate database");
      System.out.println("queries - Show available queries lists");

      System.out.println("q - Exit the program");

      System.out.println("---- end help ----- ");

    }

    private static void printQueryList(){
      System.out.println("--------------------------------------");
      System.out.println("Available Queries List");
      System.out.println("Commands:");
      System.out.println("--------------------------------------");
      System.out.println("query - Manually enter select queries (Admin ONLY)");
      System.out.println("missions - Show all space missions in database");
      System.out.println("agencies - Show all agencies in database");
      System.out.println("spacecrafts - Show all spacecrafts in database");
      System.out.println("astronauts - Show all astronauts in database");
      System.out.println("films - List of project info and astronauts that were featured in a film");
      System.out.println("flew <personID> - Show all spacecraft flew by astronaut personID");
      System.out.println("built - Show all spacecraft built by given manufacturer name");
      System.out.println("1 - Top 5 most expensive projects by country");
      System.out.println("2 - Top 5 least expensive projects");
      System.out.println("3 <firstname> <lastname> - Search astronaut with first and last name");
      System.out.println("4 - Top launch month with the highest number of successful space missions");
      System.out.println("5 - Based on average funding amount, report the number of failed/succeeded projects that are below or above the average funding");
      System.out.println("6 - Return all manufacturers founded in the 21st century that have built more than 5 spacecrafts");
      System.out.println("7 - All the missions between two given years from given country");
      System.out.println("8 - List of agencies that have fulfilled at least one activity");
      System.out.println("9 - Top 3 astronauts in each country having the most fly time (window functions)");
      System.out.println("10 - Return all satellite names from the launch vehicle which launched the most satellites");
      System.out.println("11 <projectID> - search spacecraft, funding, by projectID ");
      System.out.println("12 <projectID> - search project by projectID");
      System.out.println("13 - Display project, flightYear and Astronaut based on spacecraft Name");


      System.out.println("------------- end list -------------- ");

    }

    public static void initiateDB(Connection connection){
      //drop current tables
      resetDB(connection, TABLE_NAMES);

      //create tables
      createTables(connection, TABLE_NAMES);
      // readSchema(connection);

      //add seed files
      addSeeds(connection);
    }

    private static void resetDB(Connection connection, String[] tableNames){
      try(Statement statement = connection.createStatement()){
        connection.setAutoCommit(false);
        for (int i = tableNames.length-1; i>=0; i--){
          String dropTable = "drop table if exists " + tableNames[i]  + ";";
          System.out.println("Executing: "+dropTable);
          statement.execute(dropTable);
          System.out.println("Drop -"+ tableNames[i]+"- successfully!");
        }

        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println("ALL Tables Dropped Successfully");
        System.out.println("--------------------------------------------");
        statement.close();
      }catch(SQLException e){
        System.out.println("Error Dropping Tables");
        e.printStackTrace();
      }
    }

    private static void createTables(Connection connection, String[] tableNames){
      try(Statement statement = connection.createStatement()){
        connection.setAutoCommit(false);
        for (int i = 0; i< tableNames.length; i++){

          ArrayList<String[]> tableEntries = new ArrayList<>();

          String tableName = tableNames[i];
          loadFile(tableName+".csv",tableEntries);
  
          String[] header = tableEntries.get(0);
          tableEntries.remove(0);
          table_seeds.add(tableEntries);

          String createTable = "create table "+tableName+" ( ";
          for (int j = 0; j<header.length-1;j++){
            createTable += header[j]+", ";
          }
  
          createTable += header[header.length-1] +");";

          System.out.println("Executing: "+createTable);

          statement.execute(createTable);

          System.out.println("Create -"+ tableNames[i]+"- successfully!");
        
        }
        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println("ALL Tables Created Successfully");
        System.out.println("--------------------------------------------");
      }catch(SQLException e){
        System.out.println("Error Creating Tables");
        e.printStackTrace();
      }      

    }

    private static void addSeeds(Connection connection){

      loadAgencies(connection,table_seeds.get(0));
      loadAstronaut(connection,table_seeds.get(1));
      loadProjects(connection,table_seeds.get(2));
      loadSpacecraft(connection,table_seeds.get(3));
      loadSatellite(connection,table_seeds.get(4));
      loadManufacturers(connection,table_seeds.get(5));
      loadCountry(connection,table_seeds.get(6));
      loadFilm(connection,table_seeds.get(7));
      loadBuiltBy(connection,table_seeds.get(8));
      loadFlewBy(connection,table_seeds.get(9));
      loadFunding(connection,table_seeds.get(10));
      loadNationality(connection,table_seeds.get(11));
      loadSpacecraftUsed(connection,table_seeds.get(12));
      

      System.out.println("--------------------------------------------");
      System.out.println("ALL Seeds Added Successfully");
      System.out.println("--------------------------------------------");
    }
    private static void loadAgencies(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Agencies");
      System.out.println("--------------------------------------------");
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into agencies (agencyName, agencyAcr,countryName, altName,yearFound,evActivity,spaceRD,spaceStation,humanFlight,multipleSat,extraProbe) values(?,?,?,?,?,?,?,?,?,?,?)";
            String agencyName = row[0];
            String agencyAcr = row[1];
            String countryName = row[2];
            String altName = row[3];
            String yearFound = row[4];
            String evActivity = row[5];
            String spaceRD = row[6];
            String spaceStation= row[7];
            String humanFlight= row[8];
            String multipleSat = row[9];
            String extraProbe = row[10];
      
            PreparedStatement state = connection.prepareStatement(insertSql);

            state.setString(1, agencyName);
            state.setString(2, agencyAcr);
            state.setString(3, countryName);
            state.setString(4, altName);
            state.setInt(5, Integer.parseInt(yearFound));
            state.setBoolean(6, Boolean.valueOf(evActivity));
            state.setBoolean(7, Boolean.valueOf(spaceRD));
            state.setBoolean(8, Boolean.valueOf(spaceStation));
            state.setBoolean(9, Boolean.valueOf(humanFlight));
            state.setBoolean(10, Boolean.valueOf(multipleSat));
            state.setBoolean(11, Boolean.valueOf(extraProbe));
       

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println("Agencies Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding Agencies Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }
    }
    private static void loadAstronaut(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Astronauts");
      System.out.println("--------------------------------------------");
      String name = "astronaut";
      String cName = "Astronaut";
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (firstname, lastname, numFlight, totalFlightTime) values(?,?,?,?)";
            // String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
            String var4 = row[3];
            String var5 = row[4];

      
            PreparedStatement state = connection.prepareStatement(insertSql);

            // state.setInt(1, Integer.parseInt(var1));
            state.setString(1, var2);
            state.setString(2, var3);
            state.setInt(3, Integer.parseInt(var4));
            state.setInt(4, Integer.parseInt(var5));
       

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+" Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }
    private static void loadBuiltBy(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for builtBy");
      System.out.println("--------------------------------------------");
      String name = "builtBy";
      String cName = "BuiltBy";
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (manufacturerName, spacecraftName) values(?,?)";
            // String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
      
            PreparedStatement state = connection.prepareStatement(insertSql);

            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var2);
            state.setString(2, var3);

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
            state.close();

          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": Total Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }
    private static void loadCountry(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Country");
      System.out.println("--------------------------------------------");
      String name = "country";
      String cName = "Country";
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (countryName, continent) values(?,?)";
            String var1 = row[0];
            String var2 = row[1];
            // String var3 = row[2];
      
            PreparedStatement state = connection.prepareStatement(insertSql);

            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var1);
            state.setString(2, var2);

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
            state.close();

          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": Total Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }


    }
    private static void loadFilm(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Films");
      System.out.println("--------------------------------------------");
      String name = "films";
      String cName = "Films";
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (filmName, releasedYear, filmLength, spacecraftName) values(?,?,?,?)";
            // String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
            String var4 = row[3];
            String var5 = row[4];
      
            PreparedStatement state = connection.prepareStatement(insertSql);

            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var2);
            state.setInt(2,Integer.parseInt(var3));
            state.setInt(3,Integer.parseInt(var4));
            state.setString(4, var5);

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
            state.close();

          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }
    private static void loadFlewBy(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for FlewBy");
      System.out.println("--------------------------------------------");
      String name = "flewBy";
      String cName = "FlewBy";
      try{
        connection.setAutoCommit(false);

        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (spacecraftName, personID, flightYear) values(?,?,?)";
            // String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
            String var4 = row[3];
            // String var5 = row[4];
      
            PreparedStatement state = connection.prepareStatement(insertSql);

            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var2);
            state.setInt(2,Integer.parseInt(var3));
            state.setInt(3,Integer.parseInt(var4));
            // state.setString(5, var5);

            System.out.println("insert data for: "+ Arrays.toString(row));

            state.executeUpdate();
         
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }

        });


        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }
    private static void loadFunding(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Funding");
      System.out.println("--------------------------------------------");

    String name = "funding";
    String cName = "Funding";
    try{
      connection.setAutoCommit(false);

      table.forEach((row) -> {
        try{
          String insertSql = "insert into "+name+" ( projectID, agencyAcr, cost) values(?,?,?)";
          // String var1 = row[0];
          String var2 = row[1];
          String var3 = row[2];
          String var4 = row[3];
          // String var5 = row[4];
    
          PreparedStatement state = connection.prepareStatement(insertSql);

          // state.setInt(1,Integer.parseInt(var1));
          
          state.setInt(1,Integer.parseInt(var2));
          state.setString(2, var3);
          state.setFloat(3,Float.parseFloat(var4));
          // state.setString(5, var5);

          System.out.println("insert data for: "+ Arrays.toString(row));

          state.executeUpdate();
          state.close();

        }
        catch(Exception e){
          System.out.println("Error adding: "+ Arrays.toString(row));
          e.printStackTrace();

        }

      });


      connection.commit();
      System.out.println("--------------------------------------------");
      System.out.println(cName+": ALL Data Added Successfully");
      System.out.println("--------------------------------------------");
    }
    catch (SQLException e){
      System.out.println("--------------------------------------------");
      System.out.println("Error Adding "+cName+" Data");
      System.out.println("--------------------------------------------");
      e.printStackTrace();
    }
}
    private static void loadManufacturers(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Manufacturers");
      System.out.println("--------------------------------------------");

      String name = "manufacturers";
      String cName = "Manufacturers";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (manufacturerName, yearFound) values(?,?)";
            String var1 = row[0];
            String var2 = row[1];
            // String var3 = row[2];
            // String var4 = row[3];
            // String var5 = row[4];
      
            PreparedStatement state = connection.prepareStatement(insertSql);
  
            // state.setInt(1,Integer.parseInt(var1));
            
            // state.setInt(2,Integer.parseInt(var2));
            state.setString(1, var1);
            state.setString(2, var2);
            // state.setString(5, var5);
  
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            state.executeUpdate();
         
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
  
        });
  
  
        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }
    private static void loadNationality(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Nationality");
      System.out.println("--------------------------------------------");
      
      String name = "nationality";
      String cName = "Nationality";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (personID , countryName) values(?,?)";
            String var1 = row[0];
            String var2 = row[1];

            PreparedStatement state = connection.prepareStatement(insertSql);

            state.setInt(1,Integer.parseInt(var1));
            state.setString(2, var2);

  
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            state.executeUpdate();
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
        });

        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }
    }
    private static void loadProjects(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Projects");
      System.out.println("--------------------------------------------");
      String name = "projects";
      String cName = "Projects";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (launchMonth, flightYear, launchLocation, countryName, missionStatus) values(?,?,?,?,?)";
            // String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
            String var4 = row[3];
            String var5 = row[4];
            String var6 = row[5];
      
            PreparedStatement state = connection.prepareStatement(insertSql);
  
            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var2);
            state.setInt(2,Integer.parseInt(var3));
            state.setString(3, var4);
            state.setString(4, var5);
            state.setString(5, var6);
        
            // state.setString(5, var5);
  
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            state.executeUpdate();
         
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
  
        });
  
  
        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }
    }
    private static void loadSatellite(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Satellites");
      System.out.println("--------------------------------------------");
      
      String name = "satellite";
      String cName = "Satellite";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (spacecraftName, orbit, orbitType, LaunchVehicle) values(?,?,?,?)";
            String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];
            String var4 = row[3];
            // String var5 = row[4];
            // String var6 = row[5];
      
            PreparedStatement state = connection.prepareStatement(insertSql);
  
            // state.setInt(1,Integer.parseInt(var1));
            state.setString(1, var1);
            // state.setInt(3,Integer.parseInt(var3));
            // state.setString(4, var4);
            state.setString(2, var2);
            state.setString(3, var3);
            state.setString(4, var4);
        
            // state.setString(5, var5);
  
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            state.executeUpdate();
         
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
  
        });
  
  
        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }
    }
    private static void loadSpacecraft(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for Spacecrafts");
      System.out.println("--------------------------------------------");

      String name = "spacecraft";
      String cName = "Spacecraft";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (spacecraftName, missionType) values(?,?)";
            String var1 = row[0];
            String var2 = row[1];
 
      
            PreparedStatement state = connection.prepareStatement(insertSql);
  
            state.setString(1, var1);
            state.setString(2, var2);
        
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            state.executeUpdate();
         
            state.close();
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
        });
  
  
        connection.commit();
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }
    }
    private static void loadSpacecraftUsed(Connection connection, ArrayList<String[]> table){
      System.out.println();
      System.out.println("--------------------------------------------");
      System.out.println("Loading seed data for SpacecraftUsed");
      System.out.println("--------------------------------------------");
      String name = "spacecraftUsed";
      String cName = "SpacecraftUsed";
      try{
        connection.setAutoCommit(false);
  
        table.forEach((row) -> {
          try{
            String insertSql = "insert into "+name+" (projectID, spacecraftName, launcherName) values(?,?,?)";
            String var1 = row[0];
            String var2 = row[1];
            String var3 = row[2];

      
            PreparedStatement statement = connection.prepareStatement(insertSql);
  
            statement.setInt(1,Integer.parseInt(var1));
 
            statement.setString(2, var2);
            statement.setString(3, var3);
        
            // statement.setString(5, var5);
  
            System.out.println("insert data for: "+ Arrays.toString(row));
  
            statement.executeUpdate();
         
            statement.close();
  
          }
          catch(Exception e){
            System.out.println("Error adding: "+ Arrays.toString(row));
            e.printStackTrace();
  
          }
  
        });
  
  
        connection.commit();
        
        System.out.println("--------------------------------------------");
        System.out.println(cName+": ALL Data Added Successfully");
        System.out.println("--------------------------------------------");
      }
      catch (SQLException e){
        System.out.println("--------------------------------------------");
        System.out.println("Error Adding "+cName+" Data");
        System.out.println("--------------------------------------------");
        e.printStackTrace();
      }

    }

    public static void loadFile(String filename, ArrayList<String[]> table){
        try {
          File file = new File(filename);
          Scanner myReader = new Scanner(Part2Main.class.getResourceAsStream("/seeds/"+filename));
          String data;
          String[] rowData;

          while (myReader.hasNextLine()) {
              data = myReader.nextLine();
              rowData = data.split("[,]");
              table.add(rowData);
          }
          myReader.close();
      } catch (Exception e) {
          System.out.println("Please check filename input: " + filename + " and try again.");
  //            e.printStackTrace();
      }
    }

    public static void readSchema(Connection connection){
    
      try(Statement statement = connection.createStatement()){
        connection.setAutoCommit(false);
        ArrayList<String[]> test = new ArrayList<>();

        String table = "test";
        loadFile(table+".csv",test);

        String[] header = test.get(0);
        test.remove(0);

        String createSql = "create table test( ";
        for (int i = 0; i<header.length-1;i++){
          createSql += header[i]+", ";
        }

        createSql += header[header.length-1] +");";
        statement.execute(createSql);

        test.forEach((row) -> {
   
          try{
            String insertSql = "insert into "+table+" (id,name,letter) values(?,?,?)";
            String id = row[0];
            String name = row[1];
            String letter = row[2];
            PreparedStatement state = connection.prepareStatement(insertSql);

            state.setInt(1, Integer.parseInt(id));
            state.setString(2,name);
            state.setString(3, letter);
            state.executeUpdate();

          }
          catch(Exception e){
            e.printStackTrace();
          }

        });

        connection.commit();
      }
      catch (SQLException e){
        e.printStackTrace();
      }
    }
    
    private static void querySelect(Connection connection, String arg){
      try {
        String sql = arg;
  
        PreparedStatement statement = connection.prepareStatement(sql);
      
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
          System.out.println("Nothing data found with given query");
        } else{
          ResultSetMetaData rsmd = resultSet.getMetaData();
          int columnsNumber = rsmd.getColumnCount();

          do{
            for (int i = 1; i <= columnsNumber; i++) {
              if (i > 1) System.out.print(" |  ");
              String columnValue = resultSet.getString(i);
              System.out.print(rsmd.getColumnName(i)+ ": "+ columnValue + " ");
            }
          System.out.println("");

          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
  
    
  }

    private static void queryMissions(Connection connection){
        try {
          String sql = "Select * from projects";
    
          PreparedStatement statement = connection.prepareStatement(sql);
        
          ResultSet resultSet = statement.executeQuery();
    
          System.out.println("Showing All Space Missions: ");
          if (!resultSet.next()) {
            System.out.println("Nothing data found with given query");
          } else{
            System.out.println("projectID | launchMonth | flightYear | launchLocation | countryName | missionStatus");
            do{
              // projectID, launchMonth, flightYear, launchLocation, countryName, missionStatus
              System.out.println(resultSet.getInt("projectID") + " | " 
              + resultSet.getString("launchMonth") + " | " 
              + resultSet.getInt("flightYear") + " | "
              + resultSet.getString("launchLocation") + " | "
              + resultSet.getString("countryName") + " | "
              + resultSet.getString("missionStatus"));
            }while (resultSet.next());
          }
          resultSet.close();
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace(System.out);
        }
    
      
    }

    private static void queryAgencies(Connection connection){
      try {
        String sql = "Select * from agencies";
  
        PreparedStatement statement = connection.prepareStatement(sql);
      
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Showing All Agencies: ");
        if (!resultSet.next()) {
          System.out.println("Nothing data found with given query");
        } else{
          System.out.println("agencyName | agencyAcr | countryName | altName | yearFound | evActivity | spaceRD | spaceStation | humanFlight | multipleSat | extraProbe");
          do{
            // projectID, launchMonth, flightYear, launchLocation, countryName, missionStatus
            System.out.println(
              resultSet.getString("agencyName") + " | " 
            + resultSet.getString("agencyAcr") + " | " 
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("altName") + " | "
            + resultSet.getInt("yearFound") + " | "
            + resultSet.getBoolean("evActivity") + " | "
            + resultSet.getBoolean("spaceRD") + " | "
            + resultSet.getBoolean("spaceStation") + " | "
            + resultSet.getBoolean("humanFlight") + " | "
            + resultSet.getBoolean("multipleSat") + " | "
            + resultSet.getBoolean("extraProbe"));
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
  }

    private static void queryAstronauts(Connection connection){
    try {
      String sql = "Select * from astronaut";

      PreparedStatement statement = connection.prepareStatement(sql);
    
      ResultSet resultSet = statement.executeQuery();

      System.out.println("Showing All Astronauts: ");
      if (!resultSet.next()) {
        System.out.println("No data found with given query");
      } else{
        System.out.println("personID | firstname | lastname | numFligth | totalFlightTime");
        do{
          System.out.println(
            resultSet.getInt("personID") + " | " 
          + resultSet.getString("firstname") + " | " 
          + resultSet.getString("lastname") + " | "
          + resultSet.getInt("numFlight") + " | "
          + resultSet.getInt("totalFlightTime"));
        }while (resultSet.next());
      }
      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace(System.out);
    }
}
    private static void queryFlew(Connection connection, String arg1){
      try{

        String sql = "select astronaut.firstname, astronaut.lastname, flewBy.spacecraftName, flewBy.flightYear from astronaut left join flewBy on astronaut.personID = flewBy.personID where flewBy.personID = ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(arg1));
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Result for spacecrafts flew by given astronaut ID: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("Astronaut name | spacecraftName | flightYear ");
          do{
            System.out.println(
              resultSet.getString("firstname") + " " 
            + resultSet.getString("lastname") + " | " 
            + resultSet.getString("spacecraftName") + " | " 
            + resultSet.getInt("flightYear")); 

          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();

      }
      catch(SQLException e){
        e.printStackTrace(System.out);
      }
    }
    private static void queryBuilt(Connection connection, String arg1){
      try{

        String sql = "SELECT manufacturers.manufacturerName, builtBy.spacecraftName FROM builtBy join manufacturers on builtBy.manufacturerName = manufacturers.manufacturerName where manufacturers.manufacturerName like ?;";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1,'%'+ arg1 + '%');
        ResultSet resultSet = statement.executeQuery();

        System.out.println("Result for spacecrafts built by given manufacturer: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("Manufacturer name | spacecraftName ");
          do{
            System.out.println(
              resultSet.getString("manufacturerName") + " | "  
            + resultSet.getString("spacecraftName")); 

          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();

      }
      catch(SQLException e){
        e.printStackTrace(System.out);
      }
    }

    private static void querySpacecrafts(Connection connection){
    try {
      String sql = "Select * from spacecraft";

      PreparedStatement statement = connection.prepareStatement(sql);
    
      ResultSet resultSet = statement.executeQuery();

      System.out.println("Showing All Spacecrafts: ");
      if (!resultSet.next()) {
        System.out.println("No data found with given query");
      } else{
        System.out.println("spacecraftName | missionType");
        do{
          System.out.println(
            resultSet.getString("spacecraftName") + " | " 
          + resultSet.getString("missionType")); 

        }while (resultSet.next());
      }
      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace(System.out);
    }  
}
    
    private static void queryOne(Connection connection){
      try {
      String sql = "SELECT top 5 agencies.countryName, funding.projectID, funding.agencyAcr, funding.cost FROM funding LEFT JOIN agencies ON funding.agencyAcr = agencies.agencyAcr GROUP BY agencies.countryName, funding.projectID, funding.agencyAcr, funding.cost ORDER BY funding.cost DESC;";

      PreparedStatement statement = connection.prepareStatement(sql);
    
      ResultSet resultSet = statement.executeQuery();

      System.out.println("Showing query for Top 5 most expensive projects by country: ");
      if (!resultSet.next()) {
        System.out.println("No data found with given query");
      } else{
        System.out.println("countryName | projectID | agencyAcr | cost (millions)");
        do{
          System.out.println(
            resultSet.getString("countryName") + " | " 
          + resultSet.getInt("projectID") + " | " 
          + resultSet.getString("agencyAcr") + " | "
          + resultSet.getFloat("cost") + " million"); 

        }while (resultSet.next());
      }
      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace(System.out);
    }
    }
    
    private static void queryTwo(Connection connection){
      try {
        String sql = "SELECT top 5 agencies.countryName, funding.projectID, funding.agencyAcr, funding.cost FROM funding LEFT JOIN agencies ON funding.agencyAcr = agencies.agencyAcr GROUP BY agencies.countryName, funding.projectID, funding.agencyAcr, funding.cost HAVING countryName is NOT NULL ORDER BY funding.cost ASC;";
  
        PreparedStatement statement = connection.prepareStatement(sql);
      
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Showing query for Top 5 least expensive projects: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("countryName | projectID | agencyAcr | cost (millions)");
          do{
            System.out.println(
              resultSet.getString("countryName") + " | " 
            + resultSet.getInt("projectID") + " | " 
            + resultSet.getString("agencyAcr") + " | "
            + resultSet.getFloat("cost") + " million"); 
  
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    
    private static void queryThree(Connection connection, String arg1, String arg2){
      try {
        String sql = "SELECT astronaut.firstname, astronaut.lastname,country.countryName, country.continent FROM astronaut JOIN nationality ON astronaut.personID= nationality.personID JOIN country ON nationality.countryName=country.countryName WHERE astronaut.firstname LIKE ? AND astronaut.lastname LIKE ?";
  
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, arg1+"%");
        // System.out.println(arg1 +" "+ arg2);
        // statement.setString(1, arg1);
        // statement.setString(2, arg2);
        statement.setString(2, arg2+"%");
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Search astronaut with first and last name: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("firstname | lastname | countryName | continent");
          do{
            System.out.println(
              resultSet.getString("firstname") + " | " 
            + resultSet.getString("lastname") + " | "
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("continent") + " | "
            ); 
  
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    
    private static void queryFour(Connection connection){
      try {
        String sql = "WITH successMissions AS " +
                "(SELECT projects.projectID, projects.missionStatus " +
                "FROM projects WHERE projects.missionStatus= 'success') " +
                "SELECT TOP 3 projects.launchMonth , COUNT(projects.launchMonth) AS numSuccessfulMissions FROM projects WHERE projects.projectID IN (SELECT projectID FROM successMissions) GROUP BY projects.launchMonth ORDER BY numSuccessfulMissions DESC";
  
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Launch month with highest successful space mission ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("launchMonth | Number of successful missions");
          do{
            System.out.println(
              resultSet.getString("launchMonth") + " | " 
            + resultSet.getInt("numSuccessfulMissions")); 
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    
    private static void queryFive(Connection connection){
      try {
        String sql = "WITH avgCost AS( SELECT COUNT(funding.projectID) AS totalFundedProjects, AVG(funding.cost) AS averageCost FROM funding ), belowAvg AS(SELECT funding.projectID, funding.cost FROM funding WHERE funding.cost < (SELECT averageCost FROM avgCost) ), aboveAvg AS( SELECT funding.projectID, funding.cost FROM funding WHERE funding.cost > (SELECT averageCost from avgCost)), successMissions AS(SELECT projects.projectID,funding.cost FROM projects LEFT JOIN funding ON projects.projectID = funding.projectID WHERE projects.missionStatus = 'success' ), numSuccessBelow AS( SELECT averageCost, totalFundedProjects, COUNT(funding.projectID) as numSuccessBelow FROM funding CROSS JOIN avgCost WHERE funding.projectID IN (SELECT projectID FROM belowAvg) AND funding.projectID IN (SELECT projectID FROM successMissions) GROUP BY averageCost , totalFundedProjects ), numFailedBelow AS(SELECT averageCost, COUNT(funding.projectID) as numFailedBelow FROM funding CROSS JOIN avgCost WHERE funding.projectID IN (SELECT projectID FROM belowAvg) AND funding.projectID NOT IN (SELECT projectID FROM successMissions)  GROUP BY averageCost ), numSuccessAbove AS(SELECT averageCost, totalFundedProjects, COUNT(funding.projectID) as numSuccessAbove FROM funding CROSS JOIN avgCost  WHERE funding.projectID IN (SELECT projectID FROM aboveAvg)   AND funding.projectID IN (SELECT projectID FROM successMissions)  GROUP BY averageCost , totalFundedProjects ), numFailedAbove AS( SELECT averageCost, COUNT(funding.projectID) as numFailedAbove FROM funding CROSS JOIN avgCost  WHERE funding.projectID IN (SELECT projectID FROM aboveAvg)   AND funding.projectID NOT IN (SELECT projectID FROM successMissions)  GROUP BY averageCost ) SELECT numSuccessBelow.averageCost, numSuccessBelow, numFailedBelow, numSuccessAbove, numFailedAbove, numSuccessBelow.totalFundedProjects  FROM numSuccessBelow LEFT JOIN numFailedBelow ON  numSuccessBelow.averageCost = numFailedBelow.averageCost  LEFT JOIN numSuccessAbove ON numSuccessAbove.averageCost = numFailedBelow.averageCost  LEFT JOIN numFailedAbove ON numSuccessAbove.averageCost = numFailedAbove.averageCost";


  
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Based on average funding amount, find number of failed/succeeded projects that are below or above the average funding");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("averageCost | numSuccessBelowAverage | numFailedBelowAverage | numSuccessAboveAverage | numFailedAboveAverage | totalFundedProjects");
          do{
            System.out.println(
              resultSet.getFloat("averageCost") + " | " 
            + resultSet.getInt("numSuccessBelow") + " | " 
            + resultSet.getInt("numFailedBelow") + " | " 
            + resultSet.getInt("numSuccessAbove") + " | " 
            + resultSet.getInt("numFailedAbove") + " | " 
            + resultSet.getInt("totalFundedProjects")); 
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    
    }
    private static void querySix(Connection connection){
      try {
        String sql = "SELECT manufacturers.manufacturerName,COUNT(manufacturers.manufacturerName)AS 	numSpacecrafts FROM builtBy join manufacturers on builtBy.manufacturerName = manufacturers.manufacturerName where yearFound >2000 GROUP BY manufacturers.manufacturerName HAVING(COUNT(manufacturers.manufacturerName) > 5)";
  
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Return all manufacturers founded in the 21st century that have built more than 5 spacecrafts");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("manufacturerName | Number of spacecraftMade");
          do{
            System.out.println(
              resultSet.getString("manufacturerName") + " | " 
            + resultSet.getInt("numSpacecrafts")); 
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    private static void querySeven(Connection connection, String arg1, String arg2, String arg3){
      try {
        String sql = "WITH allMissFromCountry AS(SELECT projects.projectID FROM projects WHERE projects.countryName = ?) SELECT * FROM projects WHERE projects.projectID IN (SELECT projectID FROM allMissFromCountry) AND projects.flightYear BETWEEN ? AND ?";
  
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, arg1);
        statement.setInt(2, Integer.parseInt(arg2));
        statement.setInt(3, Integer.parseInt(arg3));
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("All the missions between two given years from given country: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("projectID | launchMonth | flightYear | launchLocation | countryName | missionStatus");
          do{
            System.out.println(
              resultSet.getInt("projectID") + " | " 
            + resultSet.getString("launchMonth") + " | " 
            + resultSet.getInt("flightYear") + " | " 
            + resultSet.getString("launchLocation") + " | "
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("missionStatus")); 
  
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    private static void queryEight(Connection connection){
      try {
             String sql = "SELECT * from agencies WHERE evActivity = 1 OR spaceRD = 1 OR spaceStation = 1 OR humanFlight = 1 OR multipleSAt = 1 OR extraProbe = 1 ";
              PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();
              System.out.println("List of agencies that have fulfilled at least one activity");
             if (!resultSet.next()) {
               System.out.println("No data found with given query");
             } else{
               System.out.println("agencyName | agencyAcr | countryName | yearFound | evActivity | spaceRD | spaceStation | humanFlight | multipleSat | extraProbe");
               
               do{
                 
                System.out.println(
                   resultSet.getString("agencyName") + " | "
                 + resultSet.getString("agencyAcr")+ " | "
                 +resultSet.getString("countryName")+" | "
                 
                 +resultSet.getInt("yearFound")+" | "
                 +resultSet.getBoolean("evActivity")+" | "
                 +resultSet.getBoolean("spaceRD")+" | "
                 +resultSet.getBoolean("spaceStation")+" | "
                 +resultSet.getBoolean("humanFlight")+" | "
                 +resultSet.getBoolean("multipleSat")+" | "
                 +resultSet.getBoolean("extraProbe"));

               }while (resultSet.next());
             }
             resultSet.close();
             statement.close();
           } catch (SQLException e) {
             e.printStackTrace(System.out);
           }
      }
      
    private static void queryNine(Connection connection){
        try {
               String sql = "with mostFlyTime as( select nationality.countryName, astronaut.firstname, astronaut.lastname, astronaut.totalFlightTime, rank() over (partition by nationality.countryName order by totalFlightTime desc) as Rank from astronaut join nationality on astronaut.personID = nationality.personID ) select * from mostFlyTime as x where x.Rank < 4";
                PreparedStatement statement = connection.prepareStatement(sql);
               ResultSet resultSet = statement.executeQuery();
                System.out.println("Top 3 astronauts in each country having the most fly time");
               if (!resultSet.next()) {
                 System.out.println("No data found with given query");
               } else{
                 System.out.println("countryName | firstname | lastname | totalFlightTime | Rank ");
                 do{
                   System.out.println(
                     resultSet.getString("countryName") + " | "
                   + resultSet.getString("firstname")+ " | "
                   +resultSet.getString("lastname")+" | "
                   +resultSet.getInt("totalFlightTime") + " | "
                   +resultSet.getInt("Rank"));
                  
                 }while (resultSet.next());
               }
               resultSet.close();
               statement.close();
             } catch (SQLException e) {
               e.printStackTrace(System.out);
             }
        }
        
    private static void queryTen(Connection connection){
      try{

        String sql = "WITH numLaunched AS( SELECT launchVehicle, COUNT(launchVehicle) AS " + 
        "numLaunched FROM satellite GROUP BY launchVehicle ), " + "mostLaunched AS( SELECT launchVehicle FROM numLaunched WHERE numLaunched " +
        "=(SELECT MAX(numLaunched) AS mostLaunched FROM numLaunched) ) SELECT " + "satellite.spacecraftName AS satellites FROM satellite WHERE " + 
        "satellite.launchVehicle = (SELECT launchVehicle FROM mostLaunched)" ;
    
          PreparedStatement statement = connection.prepareStatement(sql);
          ResultSet resultSet = statement.executeQuery();
    
          System.out.println("The following satellites have been launched by the launchVehicle that launched the most satellites: ");
          if (!resultSet.next()) {
            System.out.println("No data found with given query");
          } else{
            System.out.println("satellites");
            do{
              System.out.println(
                resultSet.getString("satellites")); 
    
            }while (resultSet.next());
          }
          resultSet.close();
          statement.close();
        }
        catch(SQLException e){
          e.printStackTrace(System.out);
        }
    }
    private static void queryEleven(Connection connection, String arg1){
      try{

        String sql = "SELECT spacecraftUsed.spacecraftName, spacecraftUsed.launcherName, projects.flightYear, projects.countryName, funding.cost FROM projects LEFT JOIN funding ON projects.projectID = funding.projectID LEFT JOIN spacecraftUsed ON spacecraftUsed.projectID = projects.projectID WHERE projects.projectID = ?";
  
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(arg1));
        ResultSet resultSet = statement.executeQuery();
  
        System.out.println("Results for spacecraft, funding, by projectID : ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("spacecraftName | launcherName | flightYear | launchCountry | cost (in millions) ");
          do{
            System.out.println(
              resultSet.getString("spacecraftName") + " | " 
            + resultSet.getString("launcherName") + " | " 
            + resultSet.getString("flightYear") + " | " 
            + resultSet.getString("countryName") + " | " 
            + resultSet.getFloat("cost") + " million"); 
  
          }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
  
      }
      catch(SQLException e){
        e.printStackTrace(System.out);
      }
  
    }
    private static void queryTwelve(Connection connection, String arg1){
      try {
        String sql = "select * from projects where projects.projectID=? ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(arg1));
        ResultSet resultSet = statement.executeQuery();
         System.out.println("search project by projectID: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("projectID | launchMonth | flightYear | launchLocation | countryName | missionStatus");
          do{
            System.out.println(
              resultSet.getInt("projectID") + " | "
            + resultSet.getString("launchMonth") + " | "
            + resultSet.getString("launchLocation") + " | "
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("missionStatus"));
           }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    private static void queryThirteen(Connection connection, String arg1){
      
      try {
        String sql = "select projects.*, spacecraftUsed.spacecraftName, spacecraftUsed.launcherName, astronaut.firstname, astronaut.lastname, flewBy.flightID from projects left join spacecraftUsed on projects.projectID = spacecraftUsed.projectID left join flewBy on  '%'+ flewBy.spacecraftName + '%' like '%'+ spacecraftUsed.spacecraftName + '%' left join astronaut on flewBy.personID = astronaut.personID where spacecraftUsed.spacecraftName = ? ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, arg1);
        ResultSet resultSet = statement.executeQuery();

         System.out.println("Display project, flightYear and Astronaut based on spacecraft Name: ");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("projectID | launchMonth | flightYear | launch location | launch country | missionStatus | spacecraftName | Astronault name | FlightID");
          do{
            System.out.println(
              resultSet.getInt("projectID") + " | "
            + resultSet.getString("launchMonth") + " | "
            + resultSet.getInt("flightYear") + " | "
            + resultSet.getString("launchLocation") + " | "
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("missionStatus") + " | "
            + resultSet.getString("spacecraftName") + " | "
            + resultSet.getString("firstname") + " "
            + resultSet.getString("lastname") + " | "
            + resultSet.getInt("flightID"));
           }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
    private static void queryFilms(Connection connection){
      try {
        String sql = "select projects.*, spacecraftUsed.spacecraftName as sName, spacecraftUsed.launcherName, astronaut.firstname, astronaut.lastname, films.* from films left join spacecraftUsed on films.spacecraftName like spacecraftUsed.spacecraftName left join projects on projects.projectID = spacecraftUsed.projectID left join flewBy on  '%'+ flewBy.spacecraftName + '%' like '%'+ spacecraftUsed.spacecraftName + '%' left join astronaut on flewBy.personID = astronaut.personID where projects.projectID is not null";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        // statement.setString(1, arg1);
        ResultSet resultSet = statement.executeQuery();

         System.out.println("List of project info and astronauts that were featured in a film");
        if (!resultSet.next()) {
          System.out.println("No data found with given query");
        } else{
          System.out.println("Film Name | Released Year | Spacecraft based on | Astronaut Name | projectID | Launch Month | Flight Year | Launch Location | Launch Country | Mission Status | Spacecraft Name | Launcher Name  ");
          do{
            System.out.println(
              resultSet.getString("filmName") + " | "
            + resultSet.getInt("releasedYear") + " | "
            + resultSet.getString("spacecraftName") + " | "
            + resultSet.getString("firstname") + " "
            + resultSet.getString("lastname") + " | "
            + resultSet.getInt("projectID") + " | "
            + resultSet.getString("launchMonth") + " | "
            + resultSet.getInt("flightYear") + " | "
            + resultSet.getString("launchLocation") + " | "
            + resultSet.getString("countryName") + " | "
            + resultSet.getString("missionStatus") + " | "
            + resultSet.getString("sName") + " | "
            + resultSet.getString("launcherName"));
           }while (resultSet.next());
        }
        resultSet.close();
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }
  }

