package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Classe per la validazione degli input utente.
 * Implementa la sanitizzazione degli input come parte della programmazione sicura.
 */
public class InputValidator {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    private static final Pattern ISSN_PATTERN = Pattern.compile("^\\d{4}-\\d{3}[\\dX]$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private InputValidator() {
        // Costruttore privato per evitare l'istanziazione
    }

    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isValidIsbn(String isbn) {
        if (isbn == null) {
            return false;
        }
        return ISBN_PATTERN.matcher(isbn).matches();
    }

    public static boolean isValidIssn(String issn) {
        if (issn == null) {
            return false;
        }
        return ISSN_PATTERN.matcher(issn).matches();
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null) {
            return false;
        }
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static boolean isValidInteger(String input) {
        if (input == null) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        // Rimuovi caratteri potenzialmente pericolosi
        return input.replaceAll("[<>\"'&]", "").trim();
    }
}