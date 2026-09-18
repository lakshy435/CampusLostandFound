# Project Statement: Campus Lost & Found Management System

## 1. Problem Statement
Educational institutions frequently experience lost and mislaid personal items across sprawling campus buildings, lecture halls, libraries, and recreational facilities. Existing approaches relying on fragmented manual logbooks or physical collection centers lead to several operational bottlenecks:
* Difficulty for students and faculty in searching for reported items across multiple departments.
* High rate of uncollected items due to poor communication channels between finders and owners.
* Inefficient ownership verification processes, increasing the risk of fraudulent claims or misallocated property.
* Inability of campus authorities to track resolution rates, item categories, or localized lost item hotspots.

The Campus Lost & Found Management System addresses these issues by delivering a centralized, console-based Java software solution that automates item recording, categorizing, match detection, ownership claiming, and resolution workflows.

---

## 2. Scope of the Project
The application handles the complete lifecycle of lost and found property within an academic institution.

### Included in Scope:
* Multi-Role User Authentication: Account creation and secure login for Students, Staff, and Administrators with encrypted password storage (SHA-256).
* Property Reporting: Input mechanisms for registering lost property and found items with detailed metadata (item type, location, date, description).
* Data Processing & Searching: Filtering, multi-attribute sorting (Date, Name, Category, Location), and match calculation between lost and found entries.
* Claim Lifecycle Management: Verification pipeline allowing users to submit ownership proof for found items and administrators to approve, reject, or mark cases as returned/closed.
* Reporting & Auditing: Administrative statistics generation and complete file-based logging of operational events.
* Persistent Storage: Serialization of system data to binary files to retain data integrity across app restarts.

### Out of Scope:
* Web or mobile frontend interfaces (system operates strictly as a command-line application).
* Physical item transportation tracking or automated lock-box hardware integrations.
* Direct online financial transactions or reward processing.

---

## 3. Target Users
1. Students: Primary users who report lost belongings, browse found items, run search queries, check potential matches, and submit claims for their missing property.
2. Faculty & Staff: Users who report items found in classrooms or offices, monitor claim statuses, and assist in returning items to owners.
3. Campus Administrators: Authorized managers responsible for verifying claim proof, approving or rejecting submissions, removing invalid item logs, closing resolved cases, and viewing system statistics.

---

## 4. High-Level Features
* User Management: Secure user registration, role assignment (STUDENT, STAFF, ADMIN), SHA-256 hashed password authentication, and session handling.
* Lost & Found Registration: Interactive forms for reporting lost or found items with unique identifier generation (L10001, F0001).
* Algorithmic Search & Sort Engine: In-memory searching and multi-attribute sorting supported by TreeMap and ArrayList data structures.
* Automated Match Engine: Priority-queue based matching logic comparing location, category, name, and dates between lost and found databases.
* Verification & Claim Processing: User ownership proof submission with administrative claim approval/rejection interfaces.
* Data Persistence & Event Logging: Automatic disk storage via Java serialization (users.dat, items.dat, claims.dat) and system event logging (application.log).