# Project Core - Library Management System

This project implements a complete library management system that allows the management of books, magazines, and collections. It is possible to create, search, add or remove media from collections, view the availability status of media, and filter results. All data is saved to files to ensure persistence.

## Features

- Collection Management: Ability to create collections and add/remove media
- Media Management: Ability to create, search or delete media as books and magazines
- Availability Status: Each media has an availability indicator that can be modified
- Input Validation: Advanced validation system to ensure data integrity
- Date Management: Standardized format for publication dates
- Advanced Search: Ability to combine multiple search criteria
- Multithreading: Parallel processing for intensive operations
- Stream API & Lambdas: Efficient manipulation of media collections
- Reflection: Dynamic inspection and manipulation of classes at runtime
- IoC (Inversion of Control): Dependency management through container
- Custom Annotations: Custom metadata for advanced functionality
- Mockito: Testing framework to simulate system components

## Flows

- Complete management of books, magazines, and collections
- Search by id, title and publication year
- Addition or removal of media from collections
- Indication of media availability status
- User input validation
- Data persistence on file
- Interactive console user interface

## Design Patterns

- Factory Pattern: Implemented in the MediaFactory class for creating media objects
- Composite Pattern: Implemented through the Media interface and the MediaCollection, Book, and Magazine classes
- Iterator Pattern: Implemented through the MediaIterator interface and the MediaCollectionIterator class
- Singleton Pattern: Implemented in the MediaRepository, MediaService, and FileStorageManager classes
- Exception Shielding: Implemented through the creation and nesting of custom exceptions
- Abstract Factory: Implemented in MediaFactoryProvider to create families of related objects
- Builder Pattern: Implemented in MediaBuilder to build complex media objects
- Strategy Pattern: Implemented in SortingStrategy for interchangeable sorting algorithms
- Observer Pattern: Implemented in MediaChangeSubject and MediaChangeObserver for change notifications
- Chain of Responsibility: Implemented in MediaHandler to process requests in sequence
- Adapter Pattern: Implemented in MediaAdapter for compatibility between different interfaces
- Memento Pattern: Implemented in MediaMemento to save and restore state
- Template Method: Implemented in MediaProcessor to define algorithm skeletons

## Core Technologies

- Collections Framework: Used to manage media collections
- Generics: Used in the Repository interface and its implementations
- Java I/O: Used for data persistence on file
- Logging: Implemented through the LoggerManager class
- Date API: Used for standardized date management
- Exception Handling: Advanced exception handling for a better user experience
- Multithreading: Use of threads for parallel operations and performance improvement
- Stream API: Declarative and functional processing of collections
- Reflection API: Dynamic inspection and manipulation of classes at runtime
- Annotation Processing: Processing of custom annotations
- Dependency Injection: Dependency management through inversion of control
- Unit Testing: Automated testing with JUnit and Mockito

## Project Structure

- model: Contains data model classes
- repository: Contains classes for data persistence
- service: Contains services for application logic
- factory: Contains factories for object creation
- iterator: Contains iterators for collections
- exception: Contains custom exceptions
- ui: Contains the user interface for console experience
- util: Contains utility classes
- validation: Contains classes for input validation
- pattern: Contains design pattern implementations
- advanced: Contains advanced features such as multithreading and reflection
- annotation: Contains custom annotations
- test: Contains test class

## Run

To run the application, execute in the console:

`java -cp target/classes main.java.Main` or `mvn exec:java -Dexec.mainClass="main.java.Main"` to run with Maven

To run the tests, execute in the console:

`mvn test` or `mvn -Dtest=MediaServiceJUnitTest testName` to run a specific test

## Requirements

- Java 8 or higher
- Maven (for compilation and package management)

## Compilation

To compile the project files, in the console:

`mvn clean compile`
