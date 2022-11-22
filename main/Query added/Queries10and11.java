///////////////////////CHANGED///////////////////////////////////////
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
  ///////////////////////CHANGED///////////////////////////////////////
  private static void queryEleven(Connection connection, String arg1){
    try{

      String sql = "SELECT builtBy.spacecraftName, funding.cost FROM projects " + 
      "LEFT JOIN funding ON projects.projectID = funding.projectID LEFT JOIN " + "builtBy ON funding.agencyAcr = builtBy.manufacturerName " + 
      "WHERE projects.projectID = ? AND builtBy.spacecraftName IS NOT NULL AND funding.cost IS NOT NULL";

      PreparedStatement statement = connection.prepareStatment(sql);
      statement.setInt(1, Float.parseFloat(arg1));
      ResultSet resultSet = statement.executeQuery();

      System.out.println("The associated Spacecraft and cost with the entered projectID is: ");
      if (!resultSet.next()) {
        System.out.println("No data found with given query");
      } else{
        System.out.println("spacecraftName | cost ");
        do{
          System.out.println(
            resultSet.getString("spacecraftName") + " | " 
          + resultSet.getFloat("cost")); 

        }while (resultSet.next());
      }
      resultSet.close();
      statement.close();

    }
    catch(SQLException e){
      e.printStackTrace(System.out);
    }

  }