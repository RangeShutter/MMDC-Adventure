# Employee Management System

A comprehensive Java-based employee management system with modern UI design and integrated payroll functionality.

## Features

### 🎨 **Modern User Interface**
- **Enhanced Main Screen**: Modern card-based layout with hover effects
- **Professional Color Scheme**: Blue and green theme with clean typography
- **Responsive Design**: Optimized layout for better user experience
- **Interactive Elements**: Hover effects, rounded buttons, and smooth transitions

### 👥 **Employee Profile Management**
- **Complete Employee Records**: Store and manage employee information
- **Integrated Payroll System**: Edit and update payroll information directly from employee profiles
- **CSV Data Storage**: All data is automatically saved to CSV files
- **CRUD Operations**: Create, Read, Update, and Delete employee records

### 📊 **Attendance Management**
- **Time Tracking**: Record and monitor employee attendance
- **Report Generation**: View attendance reports and statistics
- **Data Persistence**: Attendance data is saved for future reference

### 💰 **Payroll Management**
- **Salary Calculations**: Automatic computation of deductions and allowances
- **Rate Management**: Configurable SSS, PhilHealth, Pag-IBIG, and tax rates
- **Allowance Tracking**: Rice subsidy, phone allowance, and clothing allowance
- **Real-time Updates**: Live calculation of net salary

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE or text editor

### Installation & Running

1. **Clone or download** the project files
2. **Compile** the Java source files:
   ```bash
   javac -d bin src/*.java
   ```
3. **Run** the application:
   ```bash
   java -cp bin Main
   ```

### Default Login Credentials
- **Username**: admin
- **Password**: admin123

## System Architecture

### File Structure
```
├── src/                    # Source code
│   ├── Main.java          # Main application entry point
│   ├── User.java          # User authentication and login
│   ├── EmployeeProfile.java # Employee and payroll management
│   ├── Attendance.java    # Attendance tracking
│   └── SalaryComputation.java # Salary calculation logic
├── bin/                   # Compiled Java classes
├── employees.csv          # Employee data storage
├── payroll_records.csv    # Payroll data storage
└── README.md             # This file
```

### Key Components

#### Main.java
- **Modern UI Design**: Card-based interface with professional styling
- **Navigation**: Centralized access to all system features
- **User Session Management**: Handles user authentication and logout

#### EmployeeProfile.java
- **Integrated Payroll**: Payroll functionality built into employee management
- **Data Persistence**: Automatic CSV file management
- **User-Friendly Interface**: Intuitive forms and dialogs

#### Payroll Integration
- **Seamless Integration**: Payroll editing available from employee details
- **Real-time Calculations**: Live updates of salary components
- **Data Consistency**: Automatic synchronization between employee and payroll data

## Recent Updates

### Version 2.0 - Enhanced UI
- ✨ **Modern Design**: Complete UI overhaul with professional styling
- 🎯 **Improved Navigation**: Card-based layout for better user experience
- 🎨 **Visual Enhancements**: Hover effects, rounded corners, and smooth transitions
- 📱 **Responsive Layout**: Optimized for different screen sizes
- 🔧 **Integrated Payroll**: Payroll management now part of employee profiles
- 🔐 **Enhanced Login**: Modern login screen with professional design and improved UX

## Data Storage

The system uses CSV files for data persistence:
- `employees.csv`: Stores employee personal and work information
- `payroll_records.csv`: Stores payroll rates, allowances, and salary data

All data is automatically saved and loaded when the application starts.

## Contributing

This is a demonstration project for educational purposes. All information in this program are sample data for demonstration purposes.

## License

This project is for educational use only.
