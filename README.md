# Project Core - Library Management System

This project implements a complete library management system that allows the management of books, magazines, and collections. It is possible to create, search, add or remove media from collections, view the availability status of media, and filter results. All data is saved to file to ensure persistence.

## Features

- Collection Management: Ability to create custom collections and add/remove media
- Display Filters: Option to display only books, only magazines, or exclude collections
- Availability Status: Each media has an availability indicator that can be modified
- Input Validation: Advanced validation system to ensure data integrity
- Date Management: Standardized format for publication dates
- Advanced Search: Ability to combine multiple search criteria

## Flows

- Complete management of books, magazines, and collections
- Search by id, title, author, and publication year
- Add or remove media from collections
- Indication of media availability status
- User input validation
- Standardized date management
- Data persistence on file
- Interactive console user interface

## Design Patterns

- Factory Pattern: Implemented in the MediaFactory class for creating media objects
- Composite Pattern: Implemented through the Media interface and the MediaCollection, Book, and Magazine classes
- Iterator Pattern: Implemented through the MediaIterator interface and the MediaCollectionIterator class
- Singleton Pattern: Implemented in the MediaRepository, MediaService, and FileStorageManager classes
- Exception Shielding: Implemented through the creation and nesting of custom exceptions

## Core Technologies

- Collections Framework: Used to manage media collections
- Generics: Used in the Repository interface and its implementations
- Java I/O: Used for data persistence on file
- Logging: Implemented through the LoggerManager class
- Date API: Used for standardized date management
- Exception Handling: Advanced exception handling for a better user experience

## Project Structure

- model: Contains data model classes
- repository: Contains classes for data persistence
- service: Contains services for app logics
- factory: Contains factories for object creation
- iterator: Contains iterators for collections
- exception: Contains custom exceptions
- ui: Contains the ui for the user experience on console
- util: Contains utility classes
- validation: Contains classes for input validation
- test: Contains test classes

## Run

To run the application, run in console:

`java -cp target/classes main.java.Main`

To run the tests, run in console:

`java -cp target/classes test.MediaServiceTest` or `mvn test`

## Requirements

- Java 8 or higher
- Maven (for compilation and package management)

## Compilation

To compile the project files, in the console:

`mvn clean compile`
