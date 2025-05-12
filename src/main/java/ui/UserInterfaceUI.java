package ui;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
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

// Class for the User Interface in console
public class UserInterfaceUI {
    private static final Logger LOGGER = LoggerManager.getLogger(UserInterfaceUI.class.getName());
    private final Scanner scanner;
    private final MediaService mediaService;

    public UserInterfaceUI() {
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
        System.out.println("1. Add new media");
        System.out.println("2. View all media");
        System.out.println("3. Search media");
        System.out.println("4. Manage collections");
        System.out.println("0. Exit");
    }

    private void addNewMedia() throws LibraryException {
        System.out.println("\nADD NEW MEDIA");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("3. Collection");
        System.out.println("0. Go back");

        int choice = readIntInput("Select an option: ");

        switch (choice) {
            case 1:
                addNewBook();
                break;
            case 2:
                addNewMagazine();
                break;
            case 3:
                addNewCollection();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option, try again.");
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

        List<Media> allMedia = mediaService.findAllMedia();

        if (allMedia.isEmpty()) {
            System.out.println("No media available in the system.");
            return;
        }

        System.out.println("\nLIST OF ALL MEDIA:");
        for (Media media : allMedia) {
            // Show all media without collections
            if (!(media instanceof MediaCollection)) {
                System.out.println("- " + media.getDetails());
            }
        }
    }

    private void searchMedia() throws LibraryException {
        System.out.println("\nSEARCH MEDIA");
        System.out.println("1. Search by title");
        System.out.println("2. Search book by author");
        System.out.println("3. Search by publication year");
        System.out.println("4. Search by ID");
        System.out.println("0. Go back");

        int choice = readIntInput("Select an option: ");

        switch (choice) {
            case 1:
                searchByTitle();
                break;
            case 2:
                searchByAuthor();
                break;
            case 3:
                searchByYear();
                break;
            case 4:
                searchById();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option, try again.");
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

    private void searchByAuthor() throws LibraryException {
        String author = readStringInput("Enter author: ");

        List<Book> results = mediaService.findBooksByAuthor(author);

        if (results.isEmpty()) {
            System.out.println("\nNo books found with author '" + author + "'.");
            return;
        }

        System.out.println("\nSEARCH RESULTS:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getDetails());
        }

        handleSearchResults(new ArrayList<>(results));
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
        System.out.println("1. Add to collection");
        System.out.println("2. Delete media");
        System.out.println("0. Go back");

        int choice = readIntInput("Select an option: ");

        switch (choice) {
            case 1:
                addToCollection(results);
                break;
            case 2:
                deleteMedia(results);
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

        System.out.println("\nAVAILABLE COLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int collectionIndex = readIntInput("Select a collection (0 to cancel): ") - 1;

        if (collectionIndex < 0 || collectionIndex >= collections.size()) {
            System.out.println("Action cancelled.");
            return;
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
        int index = readIntInput("") - 1;

        if (index < 0 || index >= mediaList.size()) {
            System.out.println("Action cancelled.");
            return;
        }

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
        String answer = scanner.nextLine().trim().toLowerCase();

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

            System.out.println("\nAVAILABLE MEDIA:");
            for (int i = 0; i < availableMedia.size(); i++) {
                System.out.println((i + 1) + ". " + availableMedia.get(i).getDetails());
            }

            addToCollection(availableMedia, collection);
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
                            System.out.println("Invalid selection: " + (index + 1) + ", enter a number between 1 and"
                                    + mediaList.size());
                            allSelectionsValid = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format: '" + sel.trim()
                                + "'. Please enter valid numbers separated by commas.");
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
        System.out.println("0. Go back");

        int choice = readIntInput("Select an option: ");

        switch (choice) {
            case 1:
                viewAllCollections();
                break;
            case 2:
                viewCollectionContent();
                break;
            case 3:
                removeFromCollection();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option, try again.");
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

        System.out.println("\nAVAILABLE COLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int index = readIntInput("Select a collection (0 to cancel): ") - 1;

        if (index < 0 || index >= collections.size()) {
            System.out.println("Action cancelled.");
            return;
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

        System.out.println("\nAVAILABLE COLLECTIONS:");
        for (int i = 0; i < collections.size(); i++) {
            System.out.println((i + 1) + ". " + collections.get(i).getDetails());
        }

        int collectionIndex = readIntInput("Select a collection (0 to cancel): ") - 1;

        if (collectionIndex < 0 || collectionIndex >= collections.size()) {
            System.out.println("Action cancelled.");
            return;
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
        int mediaIndex = readIntInput("") - 1;

        if (mediaIndex < 0 || mediaIndex >= mediaItems.size()) {
            System.out.println("Action cancelled.");
            return;
        }

        Media mediaToRemove = mediaItems.get(mediaIndex);

        try {
            mediaService.removeMediaFromCollection(selectedCollection.getId(), mediaToRemove.getId());
            System.out.println("Media removed successfully from the collection.");
        } catch (Exception e) {
            System.out.println("Error removing media: " + e.getMessage());
        }
    }
}