import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import mediator.LoginPackage;
import mediator.RmiClient;
import model.Event;
import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LoadTest {
    public static void main(String[] args) throws Exception {
        RmiClient client = new RmiClient();
        while (true) {

            ArrayList<UUID> attendeeIDs = new ArrayList<>(Arrays.asList(UUID.fromString("5d2d3ece-b5ff-4cda-a487-cbad1336826a"),
                    UUID.fromString("96543a8a-371b-4f5f-91d4-b0ac60a8aa97")));
            Event event1 = new Event(
                    UUID.randomUUID(),
                    UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"),
                    "Team Meeting",
                    "Discuss project progress and next steps.",
                    LocalDateTime.of(2024, 6, 1, 9, 0),
                    LocalDateTime.of(2024, 6, 1, 10, 0),
                    "Conference Room A",
                    attendeeIDs
            );
            User user = new User(
                    UUID.randomUUID(),
                    "john.doe@example.com",
                    "password123",
                    "John",
                    "Doe",
                    "Male",
                    "123-456-7890",
                    LocalDateTime.now(),
                    LocalDate.of(1990, 1, 1),
                    imageToByteArray("")
            );

            try {
                long startTime = System.nanoTime();

                LoginPackage loginPackage = client.loginUser(new LoginPackage("user1@example.com", "password1"));
                client.createEvent(event1);
                client.createUser(user);

                long endTime = System.nanoTime();
                long latency = endTime - startTime;

                System.out.println("Creating event: " + event1.toString());
                System.out.println("Latency: " + latency + " nanoseconds");

            } catch (Exception e) {
                e.printStackTrace();
            }

            Thread.sleep(500);
        }
    }

    public static byte[] imageToByteArray(String path) throws IOException {
        // Read the image from the file at the given path
        System.out.println(path);
        BufferedImage bufferedImage = ImageIO.read(new File(path));

        // Check if the image was successfully read
        if (bufferedImage == null) {
            throw new IOException("Failed to read image from path: " + path);
        }

        // Write the image to a ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);

        // Return the resulting byte array
        return baos.toByteArray();
    }
}
