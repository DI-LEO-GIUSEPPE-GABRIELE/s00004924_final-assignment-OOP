# Project Core - Library Management System

This project implements a library management system, allowing for the management of books, magazines and collections. You can create, search, add or remove media from collections, and the data is persisted on file.

## Characteristics

- Books, magazines and collections management
- Search by id, title, author and publication year
- Add or remove media from collections
- Data persistence on file
- UI user interface on console

## Design Patterns

- Factory Pattern: Implemented in the MediaFactory class for the creation of media objects
- Composite Pattern: Implemented through the Media interface and the MediaCollection, Book e Magazine classes
- Iterator Pattern: Implemented through the MediaIterator interface and the the MediaCollectionIterator class
- Singleton Pattern: Implemented in the MediaRepository, MediaService` and FileStorageManager classes
- Exception Shielding: Implemented through the creation and nesting of custom exceptions

## Core Technologies

- Collections Framework: Used to manage media collections
- Generics: Used in the Repository interface and its implementations
- Java I/O: Used for data persistence on file
- Logging: Implemented through the LoggerManager class

## Project structure

- model: Contains the classes of model data
- repository: Contains the classes for data persistence
- service: Contains the services for app logics
- factory: Contains the factories for object creation
- iterator: Contains the iterators for the collections
- exception: Contains the custom exceptions
- ui: Contains the ui for the user experience on console
- util: Contains the classes for utilities
- test: Contains the classes for tests

## Run

To run the application, in console:

`java -cp target/classes main.java.Main`

To run the tests, in console:

`java -cp target/classes test.MediaServiceTest`

## Requirements

- Java 8 or higher
- Maven (for building and packets management)

## Compiling

To compile project's files, in console:

`mvn clean compile`
