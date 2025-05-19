package ui;

import annotation.Inject;
import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Media;
import model.media.MediaCollection;
import service.MediaService;
import util.InputValidator;
import util.LoggerManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import pattern.strategy.SortingStrategy;
import pattern.strategy.DateSortStrategy;
import pattern.template.ExportProcessor;
import java.io.File;
import java.util.stream.Collectors;
import util.ConcurrentMediaProcessor;

// Class for the User Interface in console
public class UserInterfaceUI {
    private static final Logger LOGGER = LoggerManager.getLogger(UserInterfaceUI.class.getName());

    @Inject
    // Annotation: Dependency injection for Scanner
    private Scanner scanner;

    @Inject
    // Annotation: Dependency injection for MediaService
    private MediaService mediaService;

    public UserInterfaceUI() {
        // The constructor is empty because the dependencies are injected through the
        // IoCContainer
        // Fallback to default implementation if the IoCContainer is not initialized
        this.scanner = new Scanner(System.in);
        this.mediaService = MediaService.getInstance();
    }

    public void start() {
        boolean running = true;

        printWelcomeMessage();

        while (running) {
            printMainMenu();
            int choice = readIntInput("Select an option: ");

            try {
                switch (choice) {
                    case 1:
                        addNewMedia();
                        break;
                    case 2:
                        viewAllMedia();
                        break;
                    case 3:
                        searchMedia();
                        break;
                    case 4:
                        manageCollections();
                        break;
                    case 5:
                        exportMedia();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option, try again.");
                }
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
                LOGGER.warning("Error while performing action: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred, please try again.");
                LOGGER.severe("Error: " + e.getMessage());
            }
        }

        System.out.println("Thank you for using the Library Management System!");
        scanner.close();
    }

    private void printWelcomeMessage() {
        System.out.println("\n*****************************************");
        System.out.println("WELCOME TO THE LIBRARY MANAGEMENT SYSTEM");
        System.out.println("*****************************************");
    }

    private void printMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Add new media or collection");
        System.out.println("2. View all media");
        System.out.println("3. Search media or collection");
        System.out.println("4. Manage collections");
        System.out.println("5. Export media");
        System.out.println("0. Exit");
    }

    private void addNewMedia() throws LibraryException {
        System.out.println("\nADD NEW MEDIA OR COLLECTION");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. Collection");
        System.out.println("0. Go back");

        boolean validChoice = false;
        while (!validChoice) {
            int choice = readIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    addNewBook();
                    validChoice = true;
                    break;
                case 2:
                    addNewMagazine();
                    validChoice = true;
                    break;
                case 3:
                    addNewCollection();
                    validChoice = true;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option, please enter a number between 0 and 3.");
            }
        }
    }

    private void addNewBook() throws LibraryException {
        System.out.println("\nADD NEW BOOK");

        String title = readStringInput("Title: ");
        String author = readStringInput("Author: ");
        LocalDate publicationDate = readDateInput("Publication date (dd/MM/yyyy): ");
        String publisher = readStringInput("Publisher: ");
        int pages = readIntInput("Number of pages: ");

        Media book = MediaFactory.createBook(title, author, publicationDate, publisher, pages);

        mediaService.saveMedia(book);
        System.out.println("Book added successfully: " + book.getDetails());
        LOGGER.info("Book added successfully: " + book.getId());
    }

    private void addNewMagazine() throws LibraryException {
        System.out.println("\nADD NEW MAGAZINE");

        String title = readStringInput("Title: ");
        LocalDate publicationDate = readDateInput("Publication date (dd/MM/yyyy): ");
        String publisher = readStringInput("Publisher: ");
        int issue = readIntInput("Number: ");

        Media magazine = MediaFactory.createMagazine(title, publicationDate, publisher, issue);

        mediaService.saveMedia(magazine);
        System.out.println("Magazine added successfully: " + magazine.getDetails());
        LOGGER.info("Magazine added successfully: " + magazine.getId());
    }

    private String readStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = InputValidator.sanitizeString(scanner.nextLine());
            if (!InputValidator.isValidString(input)) {
                System.out.println("Invalid input, try again.");
            }
        } while (!InputValidator.isValidString(input));
        return input;
    }

    private int readIntInput(String prompt) {
        int input;
        while (true) {
            try {
                System.out.print(prompt);
                input = Integer.parseInt(scanner.nextLine().trim());
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter an integer.");
            }
        }
    }

    private LocalDate readDateInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!InputValidator.isValidDate(input)) {
                System.out.println("Invalid date. Use the format dd/MM/yyyy.");
            }
        } while (!InputValidator.isValidDate(input));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(input, formatter);
    }

    private void viewAllMedia() throws LibraryException {
        System.out.println("\nVIEW ALL MEDIA");
        System.out.println("1. View without sorting");
        System.out.println("2. Sort by publication date (desc)");
        System.out.println("0. Go back");

        int sortChoice = readIntInput("Select an option: ");
        if (sortChoice == 0)
            return;

        List<Media> allMedia = mediaService.findAllMedia();
        List<Media> filteredMedia = new ArrayList<>();

        for (Media media : allMedia) {
            // Filter media to exclude collections
            if (!(media instanceof MediaCollection)) {
                filteredMedia.add(media);
            }
        }

        if (filteredMedia.isEmpty()) {
            System.out.println("No media available in the system.");
            return;
        }

        // Apply sorting if requested
        if (sortChoice == 2) {
            SortingStrategy sortStrategy = new DateSortStrategy();
            sortStrategy.sort(filteredMedia);
            System.out.println("\nALL MEDIA (sorted by " + sortStrategy.getSortName() + "):");
        } else {
            System.out.println("\nALL MEDIA:");
        }

        for (Media media : filteredMedia) {
            System.out.println("- " + media.getDetails());
        }
    }

    private void searchMedia() throws LibraryException {
        System.out.println("\nSEARCH MEDIA OR COLLECTION");
        System.out.println("1. Search by title");
        System.out.println("2. Search by publication year");
        System.out.println("3. Search by ID");
        System.out.println("0. Go back");

        boolean validChoice = false;
        while (!validChoice) {
            int choice = readIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    searchByTitle();
                    validChoice = true;
                    break;
                case 2:
                    searchByYear();
                    validChoice = true;
                    break;
                case 3:
                    searchById();
                    validChoice = true;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option, please enter a number between 0 and 4.");
            }
        }
    }

    private void searchByTitle() throws LibraryException {
        String title = readStringInput("Enter title: ");

        List<Media> results = mediaService.findMediaByTitle(title);

        if (results.isEmpty()) {
            System.out.println("\nNo media found with title '" + title + "'.");
            return;
        }

        System.out.println("\nSEARCH RESULTS:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getDetails());
        }

        handleSearchResults(results);
    }

    private void searchByYear() throws LibraryException {
        int year = readIntInput("Enter publication year: ");

        List<Media> results = mediaService.findMediaByPublicationYear(year);

        if (results.isEmpty()) {
            System.out.println("\nNo media found for publication year " + year + ".");
            return;
        }

        System.out.println("\nSEARCH RESULTS:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getDetails());
        }

        handleSearchResults(results);
    }

    private void searchById() throws LibraryException {
        String id = readStringInput("Enter media ID: ");

        try {
            Media media = mediaService.findMediaById(id);
            System.out.println("\nSEARCH RESULT:");
            System.out.println("1. " + media.getDetails());

            List<Media> results = new ArrayList<>();
            results.add(media);
            handleSearchResults(results);
        } catch (MediaNotFoundException e) {
            System.out.println("\nNo media found with ID '" + id + "'.");
        }
    }

    private void handleSearchResults(List<Media> results) throws LibraryException {
        System.out.println("\nOPTIONS:");

        boolean onlyCollections = true;
        for (Media media : results) {
            if (!(media instanceof MediaCollection)) {
                onlyCollections = false;
                break;
            }
        }

        if (!onlyCollections) {
            System.out.println("1. Add to collection");
            System.out.println("2. Delete media or collection");
        } else {
            System.out.println("1. Delete collection");
        }
        System.out.println("0. Go back");

        int choice = readIntInput("Select an option: ");

        switch (choice) {
            case 1:
                if (!onlyCollections) {
                    addToCollection(results);
                } else {
                    deleteMedia(results);
                }
                break;
            case 2:
                if (!onlyCollections) {
                    deleteMedia(results);
                } else {
                    System.out.println("Invalid option, try again.");
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option, try again.");
        }
    }

    private void addToCollection(List<Media> mediaList) throws LibraryException {
        if (mediaList.isEmpty()) {
            System.out.println("No media to add.");
            return;
        }

        List<Media> collections = getAllCollections();

        if (collections.isEmpty()) {
            System.out.println("No collections available. Please create a collection first.");
            return;
        }

        System.out.println("\nCOLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int collectionIndex = -1;
        boolean validInput = false;

        while (!validInput) {
            collectionIndex = readIntInput("Select a collection (0 to cancel): ") - 1;

            if (collectionIndex == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (collectionIndex < 0 || collectionIndex >= collections.size()) {
                System.out.println(
                        "Invalid selection, enter a number between 1 and " + collections.size() + " or 0 to cancel.");
            } else {
                validInput = true;
            }
        }

        MediaCollection selectedCollection = (MediaCollection) collections.get(collectionIndex);
        addToCollection(mediaList, selectedCollection);
    }

    private void deleteMedia(List<Media> mediaList) throws LibraryException {
        if (mediaList.isEmpty()) {
            System.out.println("No media to delete.");
            return;
        }

        System.out.println("\nSelect media to delete (1-" + mediaList.size() + ", 0 to cancel): ");

        int index;
        boolean validInput = false;

        while (!validInput) {
            index = readIntInput("") - 1;

            if (index == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (index < 0 || index >= mediaList.size()) {
                System.out.println("Invalid selection, enter a number between 1 and " + mediaList.size()
                        + " or 0 to cancel.");
            } else {
                validInput = true;

                Media mediaToDelete = mediaList.get(index);
                System.out.println("Are you sure you want to delete: " + mediaToDelete.getTitle() + "? (y/n)");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.equals("y")) {
                    try {
                        mediaService.deleteMedia(mediaToDelete.getId());
                        System.out.println("Media deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Error deleting media: " + e.getMessage());
                    }
                } else {
                    System.out.println("Deletion cancelled.");
                }
            }
        }
    }

    private List<Media> getAllCollections() throws LibraryException {
        List<Media> allMedia = mediaService.findAllMedia();
        List<Media> collections = new ArrayList<>();

        for (Media media : allMedia) {
            if (media instanceof MediaCollection) {
                collections.add(media);
            }
        }

        return collections;
    }

    private void addNewCollection() throws LibraryException {
        System.out.println("\nADD NEW COLLECTION");

        String title = readStringInput("Collection title: ");

        MediaCollection collection = MediaFactory.createMediaCollection(title);
        mediaService.saveMedia(collection);

        System.out.println("Collection created successfully: " + collection.getDetails());
        LOGGER.info("Collection added successfully: " + collection.getId());

        System.out.println("Do you want to add media to this collection now? (y/n)");
        String answer = "";
        boolean validAnswer = false;

        while (!validAnswer) {
            answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("y") || answer.equals("n")) {
                validAnswer = true;
            } else {
                System.out.println("Invalid input, please enter 'y' for yes or 'n' for not.");
            }
        }

        if (answer.equals("y")) {
            List<Media> allMedia = mediaService.findAllMedia();
            List<Media> availableMedia = new ArrayList<>();

            // Filter available media
            for (Media media : allMedia) {
                if (!media.getId().equals(collection.getId())) {
                    availableMedia.add(media);
                }
            }

            if (availableMedia.isEmpty()) {
                System.out.println("No media available to add to the collection.");
                return;
            }

            System.out.println("\nMEDIA:");
            for (int i = 0; i < availableMedia.size(); i++) {
                System.out.println((i + 1) + ". " + availableMedia.get(i).getDetails());
            }

            addToCollection(availableMedia, collection);
        } else {
            System.out.println("Collection created without any media.");
        }
    }

    private void addToCollection(List<Media> mediaList, MediaCollection collection) throws LibraryException {
        System.out.println("\nSelect the media to add");
        System.out.println("\nAvailable options:");
        System.out.println("- Enter comma-separated numbers (e.g. 1, 3, 4) to add specific media");
        System.out.println("- Enter 'A' to add all media");
        System.out.println("- Enter '0' to cancel");
        System.out.println("\nAvailable range (1-" + mediaList.size() + "): ");

        boolean validInput = false;
        while (!validInput) {
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                System.out.println("Action canceled.");
                return;
            }

            if (input.equalsIgnoreCase("A")) {
                validInput = true;
                // Add all media
                for (Media media : mediaList) {
                    try {
                        mediaService.addMediaToCollection(collection.getId(), media.getId());
                        System.out.println("Added: " + media.getTitle());
                    } catch (Exception e) {
                        System.out.println("Error adding " + media.getTitle() + ": " + e.getMessage());
                    }
                }
            } else {
                // Add only selectd media
                String[] selections = input.split(",");
                boolean allSelectionsValid = true;

                // Verify each selection
                for (String sel : selections) {
                    try {
                        int index = Integer.parseInt(sel.trim()) - 1;
                        if (index < 0 || index >= mediaList.size()) {
                            System.out.println("Invalid selection: " + (index + 1) + ", enter a number between 1 and "
                                    + mediaList.size());
                            allSelectionsValid = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format: '" + sel.trim()
                                + "', please enter valid numbers separated by commas.");
                        allSelectionsValid = false;
                        break;
                    }
                }

                // Add all valid selections
                if (allSelectionsValid && selections.length > 0) {
                    validInput = true;
                    for (String sel : selections) {
                        try {
                            int index = Integer.parseInt(sel.trim()) - 1;
                            Media media = mediaList.get(index);
                            mediaService.addMediaToCollection(collection.getId(), media.getId());
                            System.out.println("Added: " + media.getTitle());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                } else if (selections.length == 0) {
                    System.out.println("No selection made, try again.");
                }
            }

            if (!validInput) {
                System.out.println("\nPlease enter a valid choice (range between 1-" + mediaList.size()
                        + " separated by commas, 'A' for all, '0' for cancel): ");
            }
        }

        System.out.println("Action completed.");
    }

    private void manageCollections() throws LibraryException {
        System.out.println("\nMANAGE COLLECTIONS");
        System.out.println("1. View all collections");
        System.out.println("2. View collection content");
        System.out.println("3. Remove media from collection");
        System.out.println("4. Add media to collection");
        System.out.println("0. Go back");

        boolean validChoice = false;
        while (!validChoice) {
            int choice = readIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    viewAllCollections();
                    validChoice = true;
                    break;
                case 2:
                    viewCollectionContent();
                    validChoice = true;
                    break;
                case 3:
                    removeFromCollection();
                    validChoice = true;
                    break;
                case 4:
                    addToCollection();
                    validChoice = true;
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option, please enter a number between 0 and 4.");
            }
        }
    }

    private void viewAllCollections() throws LibraryException {
        List<Media> collections = getAllCollections();

        if (collections.isEmpty()) {
            System.out.println("No collections available.");
            return;
        }

        System.out.println("\nALL COLLECTIONS:");
        for (Media collection : collections) {
            System.out.println("- " + collection.getDetails());
        }
    }

    private void viewCollectionContent() throws LibraryException {
        List<Media> collections = getAllCollections();

        if (collections.isEmpty()) {
            System.out.println("No collections available.");
            return;
        }

        System.out.println("\nCOLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int index = -1;
        boolean validInput = false;

        while (!validInput) {
            index = readIntInput("Select a collection (0 to cancel): ") - 1;

            if (index == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (index < 0 || index >= collections.size()) {
                System.out.println(
                        "Invalid selection, enter a number between 1 and " + collections.size() + " or 0 to cancel.");
            } else {
                validInput = true;
            }
        }

        MediaCollection selectedCollection = (MediaCollection) collections.get(index);
        List<Media> mediaItems = selectedCollection.getMediaItems();

        if (mediaItems.isEmpty()) {
            System.out.println("\nThe collection is empty.");
            return;
        }

        System.out.println("\nCOLLECTION CONTENT:");
        for (int i = 0; i < mediaItems.size(); i++) {
            System.out.println((i + 1) + ". " + mediaItems.get(i).getDetails());
        }
    }

    private void removeFromCollection() throws LibraryException {
        List<Media> collections = getAllCollections();

        if (collections.isEmpty()) {
            System.out.println("No collections available.");
            return;
        }

        System.out.println("\nCOLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int collectionIndex = -1;
        boolean validInput = false;

        while (!validInput) {
            collectionIndex = readIntInput("Select a collection (0 to cancel): ") - 1;

            if (collectionIndex == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (collectionIndex < 0 || collectionIndex >= collections.size()) {
                System.out.println(
                        "Invalid selection, enter a number between 1 and " + collections.size() + " or 0 to cancel.");
            } else {
                validInput = true;
            }
        }

        MediaCollection selectedCollection = (MediaCollection) collections.get(collectionIndex);
        List<Media> mediaItems = selectedCollection.getMediaItems();

        if (mediaItems.isEmpty()) {
            System.out.println("\nThe collection is empty.");
            return;
        }

        System.out.println("\nCOLLECTION CONTENT:");
        for (int i = 0; i < mediaItems.size(); i++) {
            System.out.println((i + 1) + ". " + mediaItems.get(i).getDetails());
        }

        System.out.println("\nSelect media to remove (1-" + mediaItems.size() + ", 0 to cancel): ");

        int mediaIndex = -1;
        boolean removeMediaValidInput = false;

        while (!removeMediaValidInput) {
            mediaIndex = readIntInput("") - 1;

            if (mediaIndex == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (mediaIndex < 0 || mediaIndex >= mediaItems.size()) {
                System.out.println(
                        "Invalid selection, enter a number between 1 and " + mediaItems.size() + " or 0 to cancel.");
            } else {
                removeMediaValidInput = true;
            }
        }

        Media mediaToRemove = mediaItems.get(mediaIndex);

        try {
            mediaService.removeMediaFromCollection(selectedCollection.getId(), mediaToRemove.getId());
            System.out.println("Media removed successfully from the collection.");
        } catch (Exception e) {
            System.out.println("Error removing media: " + e.getMessage());
        }
    }

    private void addToCollection() throws LibraryException {
        List<Media> collections = getAllCollections();

        if (collections.isEmpty()) {
            System.out.println("No collections available. Please create a collection first.");
            return;
        }

        System.out.println("\nCOLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int collectionIndex = -1;
        boolean validInput = false;

        while (!validInput) {
            collectionIndex = readIntInput("Select a collection (0 to cancel): ") - 1;

            if (collectionIndex == -1) {
                System.out.println("Action cancelled.");
                return;
            } else if (collectionIndex < 0 || collectionIndex >= collections.size()) {
                System.out.println(
                        "Invalid selection, enter a number between 1 and " + collections.size() + " or 0 to cancel.");
            } else {
                validInput = true;
            }
        }

        MediaCollection selectedCollection = (MediaCollection) collections.get(collectionIndex);

        // Get all media that are not collections
        List<Media> allMedia = mediaService.findAllMedia();
        List<Media> availableMedia = new ArrayList<>();

        for (Media media : allMedia) {
            if (!(media instanceof MediaCollection) && !selectedCollection.containsMedia(media.getId())) {
                availableMedia.add(media);
            }
        }

        if (availableMedia.isEmpty()) {
            System.out.println("No media available to add to the collection.");
            return;
        }

        System.out.println("\nAVAILABLE MEDIA:");
        for (int i = 0; i < availableMedia.size(); i++) {
            System.out.println((i + 1) + ". " + availableMedia.get(i).getDetails());
        }

        addToCollection(availableMedia, selectedCollection);
    }

    private void exportMedia() throws LibraryException {
        System.out.println("\nEXPORT MEDIA");
        System.out.println("1. Export Books");
        System.out.println("2. Export Magazines");
        System.out.println("3. Export Collections");
        System.out.println("0. Go back");

        boolean validMediaTypeChoice = false;
        int mediaTypeChoice = 0;

        while (!validMediaTypeChoice) {
            mediaTypeChoice = readIntInput("Select media type to export: ");

            if (mediaTypeChoice >= 0 && mediaTypeChoice <= 3) {
                validMediaTypeChoice = true;
            } else {
                System.out.println("Invalid option, please enter a number between 0 and 3.");
            }
        }

        if (mediaTypeChoice == 0)
            return;

        String mediaType;
        switch (mediaTypeChoice) {
            case 1:
                mediaType = "Book";
                break;
            case 2:
                mediaType = "Magazine";
                break;
            case 3:
                mediaType = "Collection";
                break;
            default:
                System.out.println("Invalid option, operation cancelled.");
                return;
        }

        System.out.println("\nEXPORT FORMAT");
        System.out.println("1. Export to JSON");
        System.out.println("2. Export to Word");
        System.out.println("0. Go back");

        boolean validFormatChoice = false;
        int formatChoice = 0;

        while (!validFormatChoice) {
            formatChoice = readIntInput("Select export format: ");

            if (formatChoice >= 0 && formatChoice <= 2) {
                validFormatChoice = true;
            } else {
                System.out.println("Invalid option, please enter a number between 0 and 2.");
            }
        }

        if (formatChoice == 0)
            return;

        String format;
        switch (formatChoice) {
            case 1:
                format = "JSON";
                break;
            case 2:
                format = "WORD";
                break;
            default:
                System.out.println("Invalid option, operation cancelled.");
                return;
        }

        // Export in download directory
        String userHome = System.getProperty("user.home");
        String exportPath = userHome + "/Downloads";
        System.out.println("Exporting to: " + exportPath);

        // Create directory if it doesn't exist
        File exportDir = new File(exportPath);
        if (!exportDir.exists()) {
            if (exportDir.mkdir()) {
                System.out.println("Created export directory: " + exportDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create export directory. Using current directory.");
                exportPath = ".";
            }
        }

        try {
            // Get all media
            List<Media> allMedia = mediaService.findAllMedia();

            // Filter media by media type using ConcurrentMediaProcessor for parallelism
            System.out.println("Filtering media concurrently...");
            int threadCount = Runtime.getRuntime().availableProcessors();
            ConcurrentMediaProcessor concurrentProcessor = new ConcurrentMediaProcessor(threadCount);

            List<Media> filteredMedia = concurrentProcessor.processConcurrently(allMedia, media -> {
                String className = media.getClass().getSimpleName();
                return className.equals(mediaType) ? media : null;
            }).stream().filter(media -> media != null).collect(Collectors.toList());

            if (filteredMedia.isEmpty()) {
                System.out.println("No " + mediaType + "s found to export.");
                // Close the executor service
                concurrentProcessor.shutdown(5);
                return;
            }

            // Create the processor and export all media to a single file
            ExportProcessor processor = new ExportProcessor(format, exportPath);
            boolean success = processor.processMediaList(filteredMedia, mediaType);

            // Close the executor service
            concurrentProcessor.shutdown(5);

            if (success) {
                System.out.println("Export completed successfully.");
                LOGGER.info("Export completed successfully.");
            } else {
                System.out.println("Error during export.");
            }

        } catch (Exception e) {
            System.out.println("Error during export: " + e.getMessage());
            LOGGER.severe("Error during export: " + e.getMessage());
        }
    }
}