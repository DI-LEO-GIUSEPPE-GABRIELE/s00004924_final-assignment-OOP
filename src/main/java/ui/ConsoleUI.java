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

/**
 * Interfaccia utente a console per il sistema di gestione biblioteca.
 */
public class ConsoleUI {
    private static final Logger LOGGER = LoggerManager.getLogger(ConsoleUI.class.getName());
    private final Scanner scanner;
    private final MediaService mediaService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.mediaService = MediaService.getInstance();
    }

    public void start() {
        boolean running = true;
        
        printWelcomeMessage();
        
        while (running) {
            printMainMenu();
            int choice = readIntInput("Seleziona un'opzione: ");
            
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
                        System.out.println("Opzione non valida. Riprova.");
                }
            } catch (LibraryException e) {
                System.out.println("Errore: " + e.getMessage());
                LOGGER.warning("Errore durante l'esecuzione dell'operazione: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Si è verificato un errore imprevisto. Riprova.");
                LOGGER.severe("Errore imprevisto: " + e.getMessage());
            }
        }
        
        System.out.println("Grazie per aver utilizzato il Sistema di Gestione Biblioteca!");
        scanner.close();
    }

    private void printWelcomeMessage() {
        System.out.println("===================================================");
        System.out.println("  BENVENUTO NEL SISTEMA DI GESTIONE BIBLIOTECA");
        System.out.println("===================================================");
    }

    private void printMainMenu() {
        System.out.println("\nMENU PRINCIPALE");
        System.out.println("1. Aggiungi nuovo media");
        System.out.println("2. Visualizza tutti i media");
        System.out.println("3. Cerca media");
        System.out.println("0. Esci");
    }

    private void addNewMedia() throws LibraryException {
        System.out.println("\nAGGIUNGI NUOVO MEDIA");
        System.out.println("1. Libro");
        System.out.println("2. Rivista");
        System.out.println("0. Indietro");
        
        int choice = readIntInput("Seleziona un tipo di media: ");
        
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
                System.out.println("Opzione non valida. Riprova.");
        }
    }

    private void addNewBook() throws LibraryException {
        System.out.println("\nAGGIUNGI NUOVO LIBRO");
        
        String title = readStringInput("Titolo: ");
        String author = readStringInput("Autore: ");
        LocalDate publicationDate = readDateInput("Data di pubblicazione (dd/MM/yyyy): ");
        String publisher = readStringInput("Editore: ");
        int pages = readIntInput("Numero di pagine: ");
        
        Media book = MediaFactory.createBook(title, author, publicationDate, publisher, pages);
        
        mediaService.saveMedia(book);
        System.out.println("Libro aggiunto con successo: " + book.getDetails());
        LOGGER.info("Nuovo libro aggiunto: " + book.getId());
    }

    private void addNewMagazine() throws LibraryException {
        System.out.println("\nAGGIUNGI NUOVA RIVISTA");
        
        String title = readStringInput("Titolo: ");
        LocalDate publicationDate = readDateInput("Data di pubblicazione (dd/MM/yyyy): ");
        String publisher = readStringInput("Editore: ");
        int issue = readIntInput("Numero: ");
        
        Media magazine = MediaFactory.createMagazine(title, publicationDate, publisher, issue);
        
        mediaService.saveMedia(magazine);
        System.out.println("Rivista aggiunta con successo: " + magazine.getDetails());
        LOGGER.info("Nuova rivista aggiunta: " + magazine.getId());
    }

    private String readStringInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = InputValidator.sanitizeString(scanner.nextLine());
            if (!InputValidator.isValidString(input)) {
                System.out.println("Input non valido. Riprova.");
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
                System.out.println("Input non valido. Inserisci un numero intero.");
            }
        }
    }

    private LocalDate readDateInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!InputValidator.isValidDate(input)) {
                System.out.println("Data non valida. Utilizza il formato dd/MM/yyyy.");
            }
        } while (!InputValidator.isValidDate(input));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(input, formatter);
    }
    
    private void viewAllMedia() throws LibraryException {
        System.out.println("\nVISUALIZZA TUTTI I MEDIA");
        
        List<Media> allMedia = mediaService.findAllMedia();
        
        if (allMedia.isEmpty()) {
            System.out.println("\nNessun media disponibile nel sistema.");
            return;
        }
        
        System.out.println("\nELENCO DI TUTTI I MEDIA:");
        for (Media media : allMedia) {
            System.out.println("- " + media.getDetails());
        }
    }
    
    private void searchMedia() throws LibraryException {
        System.out.println("\nCERCA MEDIA");
        System.out.println("1. Cerca per titolo");
        System.out.println("2. Cerca per autore (solo libri)");
        System.out.println("3. Cerca per anno di pubblicazione");
        System.out.println("0. Indietro");
        
        int choice = readIntInput("Seleziona un'opzione di ricerca: ");
        
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
                System.out.println("Opzione non valida. Riprova.");
        }
    }
    
    private void searchByTitle() throws LibraryException {
        String title = readStringInput("Inserisci il titolo da cercare: ");
        
        List<Media> results = mediaService.findMediaByTitle(title);
        
        if (results.isEmpty()) {
            System.out.println("\nNessun media trovato con il titolo '" + title + "'.");
            return;
        }
        
        System.out.println("\nRISULTATI DELLA RICERCA:");
        for (Media media : results) {
            System.out.println("- " + media.getDetails());
        }
    }
    
    private void searchByAuthor() throws LibraryException {
        String author = readStringInput("Inserisci l'autore da cercare: ");
        
        List<Book> results = mediaService.findBooksByAuthor(author);
        
        if (results.isEmpty()) {
            System.out.println("\nNessun libro trovato con l'autore '" + author + "'.");
            return;
        }
        
        System.out.println("\nRISULTATI DELLA RICERCA:");
        for (Book book : results) {
            System.out.println("- " + book.getDetails());
        }
    }
    
    private void searchByYear() throws LibraryException {
        int year = readIntInput("Inserisci l'anno di pubblicazione da cercare: ");
        
        List<Media> results = mediaService.findMediaByPublicationYear(year);
        
        if (results.isEmpty()) {
            System.out.println("\nNessun media trovato pubblicato nell'anno " + year + ".");
            return;
        }
        
        System.out.println("\nRISULTATI DELLA RICERCA:");
        for (Media media : results) {
            System.out.println("- " + media.getDetails());
        }
    }
}