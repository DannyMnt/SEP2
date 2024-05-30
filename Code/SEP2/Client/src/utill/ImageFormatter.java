package utill;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.User;

import java.io.ByteArrayInputStream;

/**
 * A utility class for formatting images, such as creating an ImageView from a user's profile picture.
 */
public class ImageFormatter {

    /**
     * Creates an ImageView from a user's profile picture.
     *
     * @param user   The user whose profile picture is to be displayed.
     * @param height The desired height of the ImageView.
     * @return An ImageView displaying the user's profile picture.
     */
    public static ImageView getImageView(User user, int height) {
        Image image = new Image(new ByteArrayInputStream(user.getProfilePicture()));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(height);
        imageView.setFitHeight(height);

        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
        return imageView;
    }
}
