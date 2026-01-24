## Description
BugHuntr application offers a solution for identifying, adding bugs and vulnerabilities for various types of applications. One can join programs and report vulnerabilities for the same. On getting the vulnerabilities approved, rewards can be earned in form of GEMS. 

## Modules 
Admin,Programs,Researchers,Resources,Help,Worklist,Reports,Guidance Page,FAQs,History,Leaderboard,My Queries

## Roles & Access
1. Admin has access to:
    - Whole Admin Panel On the Home page 
    - Functionalities of admin Panel
        - Assign roles to User Registered(Bounty admininstrator and Admin)
        - Manage History,Resources,Blog,FAQ's,Help
        - Manage navigation Panel (Menu)
        - Answer Queries
        - View Researcher Details,User Details,User Permisions
        - Add New User which adds a user as visitor role in the application 
        - Manage Dropdowns that are being used in forms across the application
        - ETL Data to add a user as guest 
        - Researcher Details
    - View Programs
    - View Researchers
        - Admin can view the profile of all researchers that are registered in the application.
    - View Help
    - View Guidance Page
    - View My Queries
    - View History
    - View Worklist
    - View Reports
    - View Leaderboard


2. Bounty administrator has access to:
    - Create and Edit Programs
        - Private Program(can be accessed by invited Researcher)
        - Protected Program(Researcher sends joining request and can join after approval)
        - Public Program(can be accessed All Researchers)
    - View,Approve,Reject and Send Back Vulnerabilities reported by Researcher
        - View Programs
    - View Researchers
    - View Help
    - View Guidance Page
    - View My Queries
    - View History
    - View Worklist
    - View Reports
    - View Leaderboard

3. Researcher has access to following functionalities
    - Programs
        - Researcher can join Public programs.
        - Can send request to join Protected programs.
        - Can join Private programs only if invited.
        - Can view only the vulnerabilities that they have reported.
    - View Researchers
        - Can view Self profile only.
    - View Help
    - View Guidance Page
    - View My Queries
    - View History
    - View Worklist
    - View Reports
    - View Leaderboard

    
4. Visitor has access to following functionalities
    - Can view Public programs
    - View Researchers
        - Can register him/herself as researcher by creating researcher profile.
    - View Help
    - View Guidance Page
    - View My Queries
    - View History
    - View Worklist
    - View Reports
    - View Leaderboard

    All other users are considered as Guest login and will not be able to view any content. They can only login to the application.
    

## Credentials 
1. Admin 
    (username : 123456 password : Pass@123),
    (username : 234567 password : Pass@123)
2. Bounty Admin 
    (username : 456789, password : Pass@123),
    (username : 890123, password : Pass@123)
3. Researcher 
    (username : 345678, password : Pass@123),
    (username : 789012, password : Pass@123)
    (username : 901234, password : Pass@123)
4. Visitor 
    (username: 567890, password: Pass@123)
    (username : 678901, password : Pass@123)   

**Excluding above usernames, all other usernames are treated as guest login.


## Prerequisites
- **Java installed**: Ensure Java is installed on your system.
- **PostgreSQL installed**: Ensure PostgreSQL is installed on your system.
- **Maven installed**: Ensure Maven is installed on your system.
- **NodeJS installed**: Ensure NodeJS is installed on your system.



## Application Setup

Follow these steps to install the necessary dependencies and set up the application:

#### On Linux

1. **Install Frontend Dependecies and Push build to Backend**

    ```bash
    
    cd Angular
    npm i
    npx ng build
    ```

2. **Install Backend Dependencies (to generate JAR file)**

    ```bash
    mvn clean install
    ```

3. **Run Server (runs application on 8080)**

    ```bash
    mvn spring-boot:run
    ```
4. **Test Application Start**
    Open http://localhost:8080 






### Database
Your DB Is h2  DB 
    
**After Starting Backend access :**
http://localhost:8080/h2-console/

**Enter your jdbc url**
jdbc:h2:file:./bughuntrdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE

**username**
sa

**password**



#### Refrain from Changing application.properties file credentials
        


#### Vulnerabilities Added: 

1.  Broken Access Control
2.  CSRF
3.  SQL Injection
4.  IDOR
5.  Sensitive information Disclosure
6.  Path Traversal
7.  OS Command Injection
8.  URL redirect
9.  Insecure Deserialization
10. Improper Input validation
11. Vulnerable Dependency
12. Header Manipulation 
