# App Read Me

## Introduction
This document provides instructions to set up and run the source code of the application. Follow the steps outlined below to ensure that all dependencies are correctly configured and the application is connected to the necessary database.

## Prerequisites
Before running the application, ensure you have the following installed:
- Java Development Kit (JDK) 17 (Liberica recommended)
- PostgreSQL
- IntelliJ IDEA
- DataGrip (for managing PostgreSQL)

## Steps to Run the Source Code

### 1. Add Required Libraries in IntelliJ IDEA

#### From SEP2/jars Folder
1. Open IntelliJ IDEA and load the project.
2. Navigate to `File > Project Structure > Modules > Dependencies`.
3. Click on the `+` icon and select `JARs or directories`.
4. Navigate to the `SEP2/Code/SEP2/jars` folder and add the libraries within to your project and classpath.


#### From Maven
1. Open IntelliJ IDEA and load the project.
2. Navigate to `File > Project Structure > Modules > Dependencies`.
3. Click on the `+` icon and select `From Maven`.
4. Add the following dependencies:
    ```xml
    org.slf4j:slf4j-simple:1.7.30
    dnsjava:dnsjava:3.4.2
    com.dlsc.phonenumberfx:phonenumberfx:1.12.1
    ```

### 2. Setting Up PostgreSQL Database

Using DataGrip, create the database and execute the SQL script to set up the schema and tables.

1. Open DataGrip and establish a connection to your PostgreSQL database server.
2. Create a new database by executing SQL commands or using DataGrip's graphical interface.
3. Once the database is created, execute the provided `.sql` script from `SEP2/Code/SEP2/database` to create the schema, tables, and test data.

### 3. Configure Database Connection

1. Open DataGrip and connect to your PostgreSQL database.
2. In the source code, navigate to the `DatabaseSingleton` class.
3. Update the database connection properties with your PostgreSQL URL, username and password:
    ```java
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2";
    private static final String USER = "your-username";
    private static final String PASSWORD = "your-password";
    ```
   Be sure to set the  `[currentSchema]` to sep2 in the URL.

### 4. Run the Application

Once all dependencies are added and the database is set up:

1. Open IntelliJ IDEA and load the project.
2. Build the project to ensure all dependencies are correctly added.
3. Run the main class to start the application.

## Troubleshooting

- Ensure all required library files are added to the build path.
- Verify that the PostgreSQL database is running and accessible.
- Check the database connection configuration for accuracy.
- Review any error messages for specific details that can guide further troubleshooting.

## Conclusion

By following these steps, you should be able to successfully set up and run the source code of the application. If you encounter any issues, refer to the troubleshooting section or consult the project documentation for further assistance.
