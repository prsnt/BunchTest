# EmployeeRecords

## Overview

EmployeeRecords is an Android application developed as part of a technical task for Bunch Welding Company. The app allows users to view employee records stored in an SQLite database, display them using a RecyclerView, and export these records to an Excel file via email.

## Features

- **SQLite Database**: Stores employee records with fields like EmpID and EmpName.
- **RecyclerView Screen**: Displays employee records using a RecyclerView.
- **Export to Excel**: Allows users to export employee records to an Excel file.
- **Email Integration**: Sends the exported Excel file to the user's Gmail address.

## Task Details

- **Database Setup**: Create a SQLite table with fields `EmpID` and `EmpName` and populate it with 15-20 records.
- **RecyclerView Screen**: Create a screen with a RecyclerView to display the employee records.
- **Email Button**: Add an 'Email' button at the bottom left corner of the screen.
- **Export to Excel**: On clicking the 'Email' button, export the employee records to an Excel file with formatted layout (borders around the data) and send it to a specified Gmail address (for demonstration, a hard-coded email address may be used).

## Implementation Details

- **SQLite Database**: Utilizes SQLite to store employee records locally within the app.
- **RecyclerView**: Implements a RecyclerView to efficiently display the list of employee records on the screen.
- **Excel Export**: Implements functionality to export employee records to an Excel file with formatted layout (borders around the data).
- **Email Integration**: Uses Gmail integration to send the exported Excel file to a specified email address.

## Installation

To run the EmployeeRecords project locally, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/prsnt/BunchTest.git
   ```

2. Open the project in Android Studio.

3. Build and run the application on an Android device or emulator.

## Dependencies

No specific dependencies mentioned.

## Usage

1. Launch the EmployeeRecords app on your Android device or emulator.
2. View the list of employee records displayed using the RecyclerView.
3. Click the 'Email' button to export the records to an Excel file and send it to the specified Gmail address.
