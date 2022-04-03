Title: Appointment Scheduling System
Purpose: An app that maintains business meeting schedules for a company with locations in multiple countries.
Author: James Carney
E-mail: jamiecello@gmail.com
Software Version: 1.0
Release Date: 11/23/2021
IDE: IntelliJ 2021.2.3 Ultimate Edition
JDK: JDK-11.0.13
JavaFX: OpenJFX 11.0.2
MySQL Connector Driver: mysql-connector-java-8.0.25
Additional Report: Number of customers by country. Useful to see  which countries are generating the most customers/business.
                    This was accomplished with an INNER JOIN on Division_ID between customers and first_level_divisions,
                    along with a COUNT operation and GROUP BY COUNTRY_ID.

Directions: The program allows monitoring of customers and appointments in a database. The customers button will take you to a list of current customers, as well as buttons for adding, editing, and deleting customers. The appointments button will take you to a similar section for appointments,
	    with the same functionality. Additionally, you can filter appointments by week, month, or all on the appointments screen. Finally, the report button will take you to a screen with some useful information about the customers and appointments in the database. Login attempts are logged in a .txt file
	    in the program's root directory, and Java Docs are available in /Javadoc. 