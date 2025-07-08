# Employee Management System

A comprehensive Java-based employee management system with modern UI design, integrated payroll functionality, and real-time data synchronization.

## Features

### 🎨 **Modern User Interface**
- **Splash Screen**: Animated welcome screen with company logo
- **Professional Color Scheme**: Navy and orange theme with gradient backgrounds
- **Responsive Design**: Optimized layout for better user experience
- **Interactive Elements**: Hover effects, rounded buttons, and smooth transitions
- **Consistent Styling**: Unified design across all screens

### 👥 **Employee Profile Management**
- **Complete Employee Records**: Store and manage employee information
- **Integrated Payroll System**: Edit and update payroll information directly from employee profiles
- **CSV Data Storage**: All data is automatically saved to CSV files
- **CRUD Operations**: Create, Read, Update, and Delete employee records
- **Real-time Synchronization**: New employees automatically appear in attendance tracking

### 📊 **Attendance Management**
- **Time Tracking**: Record and monitor employee attendance
- **Dynamic Employee List**: Automatically syncs with employee database
- **Report Generation**: View attendance reports and statistics
- **Data Persistence**: Attendance data is saved for future reference

### 💰 **Payroll Management**
- **Salary Calculations**: Automatic computation of deductions and allowances
- **Rate Management**: Configurable SSS, PhilHealth, Pag-IBIG, and tax rates
- **Allowance Tracking**: Rice subsidy, phone allowance, and clothing allowance
- **Real-time Updates**: Live calculation of net salary

### 🔐 **User Authentication**
- **Secure Login**: Modern login interface with validation
- **Session Management**: Proper user session handling
- **Default Credentials**: Pre-configured admin access

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
│   ├── SplashScreen.java  # Welcome screen with logo
│   ├── User.java          # User authentication and login
│   ├── EmployeeProfile.java # Employee and payroll management
│   ├── Attendance.java    # Attendance tracking
│   └── SalaryComputation.java # Salary calculation logic
├── bin/                   # Compiled Java classes
├── Logo/                  # Application logos and icons
├── employees.csv          # Employee data storage
├── payroll_records.csv    # Payroll data storage
├── attendance_records.csv # Attendance data storage
└── README.md             # This file
```

### Key Components

#### SplashScreen.java
- **Welcome Experience**: Displays company logo with gradient background
- **Smooth Transition**: 3-second duration before proceeding to login
- **Professional Presentation**: Sets the tone for the application

#### Main.java
- **Modern UI Design**: Card-based interface with professional styling
- **Navigation**: Centralized access to all system features
- **User Session Management**: Handles user authentication and logout
- **Consistent Theming**: Navy and orange color scheme throughout

#### User.java
- **Modern Login Interface**: Professional design with validation
- **Secure Authentication**: Proper credential checking
- **Smooth User Experience**: Intuitive login flow

#### EmployeeProfile.java
- **Integrated Payroll**: Payroll functionality built into employee management
- **Data Persistence**: Automatic CSV file management
- **User-Friendly Interface**: Intuitive forms and dialogs
- **Real-time Updates**: Changes immediately reflect in other modules

#### Attendance.java
- **Dynamic Employee Selection**: Dropdown automatically updates with new employees
- **Synchronized Data**: Real-time connection with employee database
- **Comprehensive Tracking**: Complete attendance management system

#### SalaryComputation.java
- **Automatic Calculations**: Handles all payroll computations
- **Flexible Rate System**: Configurable deduction and allowance rates
- **Accurate Results**: Precise salary calculations with proper deductions

## Recent Updates

### Version 3.0 - Enhanced Integration & UI
- ✨ **Splash Screen**: Added animated welcome screen with company branding
- 🎨 **Updated Color Scheme**: Navy and orange theme with gradient backgrounds
- 🔄 **Real-time Synchronization**: Employee additions automatically update attendance module
- 🎯 **Improved Navigation**: Enhanced user flow from splash to main application
- 📊 **Dynamic Data**: Live updates between employee and attendance systems
- 🔧 **Consistent Styling**: Unified design language across all screens

### Version 2.0 - Modern UI Overhaul
- ✨ **Modern Design**: Complete UI overhaul with professional styling
- 🎯 **Improved Navigation**: Card-based layout for better user experience
- 🎨 **Visual Enhancements**: Hover effects, rounded corners, and smooth transitions
- 📱 **Responsive Layout**: Optimized for different screen sizes
- 🔧 **Integrated Payroll**: Payroll management now part of employee profiles
- 🔐 **Enhanced Login**: Modern login screen with professional design

## Data Storage & Synchronization

The system uses CSV files for data persistence with automatic synchronization:
- `employees.csv`: Stores employee personal and work information
- `payroll_records.csv`: Stores payroll rates, allowances, and salary data
- `attendance_records.csv`: Stores daily attendance records

**Key Synchronization Features:**
- New employees added in EmployeeProfile automatically appear in Attendance dropdown
- All data changes are immediately reflected across the system
- Automatic CSV file updates ensure data persistence

## Technical Features

### Color Scheme
- **Primary Colors**: Navy blue (#1e3a8a) and Orange (#f97316)
- **Backgrounds**: Gradient backgrounds for headers and footers
- **Text**: White text on dark backgrounds, navy text on light backgrounds
- **Buttons**: Orange buttons with hover effects

### Data Flow
1. **Splash Screen** → **Login** → **Main Menu**
2. **Employee Management** → **Payroll Integration** → **Attendance Sync**
3. **Real-time Updates** across all modules

## Contributing

This is a demonstration project for educational purposes. All information in this program are sample data for demonstration purposes.

## License

This project is for educational use only.
