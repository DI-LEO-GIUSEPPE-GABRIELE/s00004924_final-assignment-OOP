package ui;

import exception.LibraryException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import service.MediaService;
import util.InputValidator;
import util.LoggerManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option, try again.");
                }
            } catch (LibraryException e) {
                System.out.println("Error: " + e.getMessage());
                LOGGER.warning("Error while performing operation: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred, please try again.");
                LOGGER.severe("Error: " + e.getMessage());
            }
        }

        System.out.println("Thank you for using the Library Management System!");
        scanner.close();
    }

    private void printWelcomeMessage() {
        System.out.println("*****************************************");
        System.out.println("WELCOME TO THE LIBRARY MANAGEMENT SYSTEM");
        System.out.println("*****************************************");
    }

    private void printMainMenu() {
        System.out.println("MAIN MENU");
        System.out.println("1. Add new media");
        System.out.println("2. View all media");
        System.out.println("3. Search media");
        System.out.println("0. Exit");
    }

    private void addNewMedia() throws LibraryException {
        System.out.println("ADD NEW MEDIA");
        System.out.println("1. Book");
        System.out.println("2. Magazine");
        System.out.println("0. Go back");

        int choice = readIntInput("Select a media type: ");

        switch (choice) {
            case 1:
                addNewBook();
                break;
            case 2:
                addNewMagazine();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option, try again.");
        }
    }

    private void addNewBook() throws LibraryException {
        System.out.println("ADD NEW BOOK");

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
        System.out.println("ADD NEW MAGAZINE");

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
        System.out.println("VIEW ALL MEDIA");

        List<Media> allMedia = mediaService.findAllMedia();

        if (allMedia.isEmpty()) {
            System.out.println("No media available in the system.");
            return;
        }

        System.out.println("LIST OF ALL MEDIA:/n");
        for (Media media : allMedia) {
            System.out.println("- " + media.getDetails());
        }
    }

    private void searchMedia() throws LibraryException {
        System.out.println("SEARCH MEDIA");
        System.out.println("1. Search by title");
        System.out.println("2. Search book by author");
        System.out.println("3. Search by publication year");
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
        for (Media media : results) {
            System.out.println("- " + media.getDetails());
        }
    }

    private void searchByAuthor() throws LibraryException {
        String author = readStringInput("Enter author: ");

        List<Book> results = mediaService.findBooksByAuthor(author);

        if (results.isEmpty()) {
            System.out.println("\nNo books found with author '" + author + "'.");
            return;
        }

        System.out.println("\nSEARCH RESULTS:");
        for (Book book : results) {
            System.out.println("- " + book.getDetails());
        }
    }

    private void searchByYear() throws LibraryException {
        int year = readIntInput("Enter publication year: ");

        List<Media> results = mediaService.findMediaByPublicationYear(year);

        if (results.isEmpty()) {
            System.out.println("\nNo media found for publication year " + year + ".");
            return;
        }

        System.out.println("\nSEARCH RESULTS:");
        for (Media media : results) {
            System.out.println("- " + media.getDetails());
        }
    }
}