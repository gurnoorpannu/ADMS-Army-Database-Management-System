
Army Project DBMS Application
Overview
The Army Project Database Management System (ADMS) is a comprehensive Android application designed to efficiently manage military data. This application provides a user-friendly interface for tracking soldiers, weapons, battalions, and war information in a centralized database system.

Features
1. Soldier Management
View detailed soldier profiles including personal information, physical attributes, and service records
Track soldier postings and visited locations
Monitor soldier status and rank information
Search soldiers by ID
2. Weapon Inventory
Comprehensive weapon database with detailed specifications
Track weapon quantities, caliber, range, and manufacturer information
Search weapons by ID
View weapon images and details
3. Battalion Management
View battalion information including capacity and personnel
Track battalion details and assignments
Monitor battalion status and deployment
4. War Records
Track past and ongoing wars
View war details including location and dates
Historical record of military engagements
5. Database Exploration
User-friendly interface for exploring the military database
Real-time statistics on soldiers, weapons, and battalions
Recent activity tracking
Search functionality for quick data access
Technical Details
Architecture
Built with Jetpack Compose for modern UI development
MVVM architecture pattern for clean separation of concerns
Firebase Firestore integration for real-time data storage and retrieval
Navigation component for seamless screen transitions
UI Features
Dark theme optimized for military operations
Responsive design for various screen sizes
Interactive cards and statistics displays
Modern material design components
Getting Started
Prerequisites
Android Studio Arctic Fox or newer
Minimum SDK: API 21 (Android 5.0 Lollipop)
Gradle version 7.0+
Firebase account for database access
Installation
Clone the repository
Open the project in Android Studio
Sync Gradle files
Connect to Firebase (ensure you have the google-services.json file)
Build and run the application
Firebase Setup
This application uses Firebase Firestore for data storage. To set up your own Firebase instance:

Create a Firebase project at Firebase Console
Add an Android app to your Firebase project
Download the google-services.json file and place it in the app directory
Enable Firestore in your Firebase project
Set up the necessary collections: soldier, weapon, battalion, war
Project Structure
Key Components
MainActivity: Entry point and navigation controller
HomeScreen: Landing page with app overview
DatabaseScreenUI: Central hub for database exploration
SoldierScreenUI: Detailed soldier information display
WeaponDetailScreenUI: Weapon specifications and details
BattalionScreenUI: Battalion information and management
WarScreenUI: War records and details
Data Models
Soldier
Weapon
Battalion
War
Location
Posting
Medal

Contributors
Gurnoor Sngh Pannu
Prabhkanwal Singh

Acknowledgments
Firebase for database services
Jetpack Compose for modern UI toolkit
Material Design for UI guidelines
