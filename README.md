# Campus Lost & Found Management System

## Overview
The Campus Lost & Found Management System is a modular Java console-based application designed to streamline the reporting, tracking, matching, and claiming of lost items across a university campus. Built to replace manual, error-prone lost-and-found registries, the system enforces secure role-based operations (Student, Staff, and Admin), precise item filtering/sorting, algorithmic matching of lost and found records, and local data persistence.

---

## Problem Statement
On large university campuses, lost belongings (wallets, identification cards, electronic gadgets, books, personal items) are frequently turned in at disparate department desks or mislaid entirely. The absence of a centralized, real-time repository creates multiple issues:
* Students and staff lack a single channel to check for missing items.
* Manual physical logbooks lead to lost entries, unverified claims, and redundant tracking efforts.
* Campus administrators struggle to verify legitimate ownership, leading to uncollected items and lack of accountability.

---

## Objectives
1. Centralize Item Tracking: Provide a single platform to register both lost and found belongings.
2. Automate Match Detection: Implement matching mechanisms to connect lost item reports with found item records.
3. Secure Claim Management: Ensure claims are processed with submitted ownership proof and reviewed by administrators before items are marked as returned.
4. Role-Based Access Control: Differentiate privileges between general campus users (Students and Staff) and system administrators.
5. Ensure Data Integrity & Persistence: Maintain continuous application state across restarts using local file serialization, input validation, and security hashing.

---

## Key Features
* Authentication & Authorization:
  * User registration and authentication (Student/Staff roles).
  * Built-in default Administrator account.
  * Security hashing using SHA-256 for all user passwords.
* Item Management:
  * Report lost items with attributes (Name, Category, Location, Date Lost, Description).
  * Report found items with attributes (Name, Category, Location, Date Found, Description).
  * Unique ID generation (L10001 for Lost, F0001 for Found).
  * Admin capability to view all items and remove invalid entries.
* Search & Sorting Algorithms:
  * Exact and partial search across item properties.
  * Sorting capabilities by Date, Item Name, Category, and Location.
* Match Engine:
  * Algorithmic evaluation comparing active lost item parameters against found item records to suggest potential matches.
* Claim & Resolution Workflow:
  * Submit claims against found items by attaching proof of ownership.
  * View claim status (PENDING, APPROVED, REJECTED).
  * Administrator review interface to approve/reject pending claims and mark items as RETURNED/CLOSED.
* System Persistence & Auditing:
  * Automatic local object serialization (.dat binary storage).
  * Application logging into application.log using standard Java logging frameworks.

---

## System Architecture & Data Flow

+-----------------------------------------------------------------------+
|                             USER INTERFACE                            |
|                            (Main / Console)                           |
+-----------------------------------------------------------------------+
                                   |
                                   v
+-----------------------------------------------------------------------+
|                            SERVICE LAYER                              |
|   +------------------+  +------------------+  +------------------+    |
|   |   UserService    |  |   ItemService    |  |   ClaimService   |    |
|   +------------------+  +------------------+  +------------------+    |
|   |         ReportService        |         DataStore             |    |
|   +------------------------------+-------------------------------+    |
+-----------------------------------------------------------------------+
            |                              |                   |
            v                              v                   v
+-----------------------+     +------------------+   +------------------+
|      DSA LAYER        |     |   UTIL LAYER     |   |   MODEL LAYER    |
|  * ItemSearch         |     |  * Validate      |   |  * User / Role   |
|  * ItemSorter         |     |  * Security      |   |  * Item / Status |
|  * MatchEngine        |     |  * FileManager   |   |  * Claim / Status|
+-----------------------+     |  * AppLogger     |   +------------------+
                              +------------------+
                                       |
                                       v
                              +------------------+
                              | LOCAL STORAGE    |
                              |  * users.dat     |
                              |  * items.dat     |
                              |  * claims.dat    |
                              |  * app.log       |
                              +------------------+

### High-Level Workflow:
1. User Auth: User registers/logins -> Passwords hashed via SHA-256 in Security.java -> Validated against users.dat.
2. Item Registration: User logs a Lost or Found item -> Input validated by Validate.java -> Persisted in items.dat via DataStore.java / FileManager.java.
3. Matching & Search: User triggers match/search -> Executed by MatchEngine.java / ItemSearch.java using Java Collections structures.
4. Claim Resolution: Claimant submits ownership details -> Admin reviews pending claims in ClaimService.java -> Item status transitions to RETURNED/CLOSED.

---

## Project Structure

CampusLostandFound/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── campus/
│                   └── lostfound/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── User.java
│                       │   ├── Item.java
│                       │   ├── LostItem.java
│                       │   ├── FoundItem.java
│                       │   ├── Claim.java
│                       │   ├── Role.java
│                       │   ├── ItemStatus.java
│                       │   └── ClaimStatus.java
│                       ├── service/
│                       │   ├── DataStore.java
│                       │   ├── UserService.java
│                       │   ├── ItemService.java
│                       │   ├── ClaimService.java
│                       │   └── ReportService.java
│                       ├── dsa/
│                       │   ├── ItemSearch.java
│                       │   ├── ItemSorter.java
│                       │   └── MatchEngine.java
│                       └── util/
│                           ├── Validate.java
│                           ├── Security.java
│                           ├── FileManager.java
│                           └── AppLogger.java
├── data/
│   ├── users.dat
│   ├── items.dat
│   ├── claims.dat
│   └── application.log
├── docs/
├── .vscode/
│   └── settings.json
├── README.md
└── statement.md

---

## Data Structures & OOP Concepts Applied

### 1. Data Structures & Algorithms (DSA)
* ArrayList: Used across services (ItemService, ClaimService, UserService) to hold dynamic collections of users, lost/found items, and claims in memory during execution.
* HashMap: Utilized in index construction and rapid key-value lookup operations (e.g., mapping User IDs to User objects or Item IDs to Item records within DataStore.java).
* TreeMap: Applied in ItemSorter.java and ReportService.java to maintain natural order sorting by key (such as sorting items chronologically by date string or organizing category counts).
* PriorityQueue: Leveraged inside MatchEngine.java to rank and prioritize potential item matches based on calculated string similarity and attribute matching scores.

### 2. Object-Oriented Programming (OOP)
* Encapsulation: All model properties (User, Item, Claim) use private fields with standard public getters, setters, and constructors, ensuring strict control over state mutations.
* Inheritance: LostItem and FoundItem extend the abstract base class Item, inheriting base properties (ID, name, category, location, status) while introducing specific fields like dateLost or dateFound.
* Abstraction: Interfaces and modular separation in service layers (UserService, ItemService, ClaimService) decouple operational logic from lower-level I/O tasks in FileManager.
* Polymorphism: Methods accept base references (such as handling Item types) while dynamically processing derived LostItem or FoundItem instances at runtime.

---

## Storage & Security Mechanisms

### Local File Persistence
* Object Serialization: Data structures are preserved on disk inside the data/ folder as serialized binary files (users.dat, items.dat, claims.dat).
* Logging: Runtime activity, system events, and operational exceptions are tracked through standard Java Logging (java.util.logging) directed to data/application.log.

### Security Implementation
* Password Hashing: Passwords are never stored in plaintext. Security.java applies the SHA-256 message digest algorithm to convert inputs into fixed-length hex strings before saving or validating against stored user credentials.
* Role-Based Access Control (RBAC): Users are assigned roles via the Role enum (STUDENT, STAFF, ADMIN). Administrative actions (claim approvals, item deletions, platform analytics) are restricted to ADMIN accounts.

---

## Prerequisites & Installation

### Requirements
* Java Development Kit (JDK): Version 11 or higher installed and configured in system path.
* Integrated Development Environment (IDE): Visual Studio Code (with Java Extension Pack) or any standard Java IDE / Terminal interface.
* Git: Installed for version control tracking.

### Compilation and Execution

1. Clone the repository:
   git clone https://github.com/lakshy435/CampusLostandFound.git
   cd CampusLostandFound

2. Compile the source code:
   Navigate to the root directory and compile all Java source files into an output destination:
   javac -d bin src/main/java/com/campus/lostfound/*.java src/main/java/com/campus/lostfound/*/*.java

3. Run the Application:
   Execute the Main entry point class:
   java -cp bin com.campus.lostfound.Main

---

## Credentials & Execution Flow

### Default Admin Account
Upon initial execution, if users.dat does not contain an administrative identity, the application initializes the system admin account:
* User ID: admin
* Password: admin123
* Role: ADMIN

### Standard User Flow
1. Launch application and select "2. Register".
2. Register as a Student or Staff member (e.g., User ID: 25bai10855, Name: Lakshya Singh).
3. Select "1. Login" using registered credentials.
4. Access Dashboard features:
   * Select 1 to report a lost item (generates ID like L0001).
   * Select 2 to report a found item (generates ID like F0001).
   * Select 4 to sort listed items by Name, Category, Location, or Date.
   * Select 6 to submit a claim against a found item using proof of ownership details.
   * Select 7 to review claim status.

---

## Verification & Workflow Demonstration

The system workflow has been validated through execution testing in VS Code:
1. User Registration: Successfully created student profile (25bai10855).
2. User Authentication: Validated SHA-256 login verification against stored credentials.
3. Lost Item Logging: Created lost record for a black wallet logged at location library under date 2026-01-23 (Assigned ID: L0001).
4. Found Item Logging: Created found record for a black wallet logged at location library under date 2026-01-23 (Assigned ID: F0001).
5. Sorting: Verified sorted console outputs organized by item name and date.
6. Claim Processing: Submitted ownership claim against item F0001 with proof description ("wallet has my student id inside"). Verified pending claim generation (C0001).

---

## College Assessment Alignment Matrix
(Aligned with VITyarthi Project Criteria)

| Evaluation Metric | Project Implementation |
| :--- | :--- |
| Functional Modules (>= 3) | User Auth Module, Item Management Module, Search & Sorting Module, Claim Resolution Module, Reporting Module. |
| Non-Functional Requirements (>= 4) | Security (SHA-256), Persistence (.dat serialization), Robustness (Validation in Validate.java), Maintainability (Modular Package Structure). |
| Data Structures Applied | ArrayList, HashMap, TreeMap, PriorityQueue. |
| OOP Paradigm Usage | Inheritance (LostItem/FoundItem), Encapsulation, Abstraction, Polymorphism. |
| Storage & Logging | Binary Object Files (users.dat, items.dat, claims.dat), System Logging (application.log). |
| Version Control | Managed via Git repository structure with modular package layout. |

---

## Future Enhancements
* Development of a Graphical User Interface (GUI) using JavaFX or Swing.
* Integration of a relational database system (such as MySQL or PostgreSQL via JDBC) to replace binary file storage.
* Implementation of automated email notifications using JavaMail API for claim status updates.
* Advanced fuzzy matching logic utilizing Levenshtein Distance for broader match accuracy on user-entered descriptions.