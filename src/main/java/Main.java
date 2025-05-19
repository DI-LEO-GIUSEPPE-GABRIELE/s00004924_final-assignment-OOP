package main.java;

import ioc.IoCContainer;
import ioc.IoCException;
import repository.MediaRepository;
import service.MediaService;
import ui.UserInterfaceUI;
import util.LoggerManager;
import java.util.Scanner;
import java.util.logging.Logger;

// Main class of the application
public class Main {
    private static final Logger LOGGER = LoggerManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting the Library Management System");

        try {
            // Starting the IoC container
            IoCContainer container = IoCContainer.getInstance();

            // Registration of classes with IoC
            MediaRepository mediaRepository = MediaRepository.getInstance();
            container.register(MediaRepository.class, mediaRepository);

            MediaService mediaService = MediaService.getInstance();
            container.register(MediaService.class, mediaService);

            Scanner scanner = new Scanner(System.in);
            container.register(Scanner.class, scanner);

            UserInterfaceUI ui = container.getInstance(UserInterfaceUI.class);
            ui.start();
        } catch (IoCException e) {
            LOGGER.severe("Error with dependency injection: " + e.getMessage());
            System.out.println("Error with dependency injection.");
        } catch (Exception e) {
            LOGGER.severe("Error while running the application: " + e.getMessage());
            System.out.println("An error occurred. See logs for more details.");
        }

        LOGGER.info("Library Management System closed");
    }
}