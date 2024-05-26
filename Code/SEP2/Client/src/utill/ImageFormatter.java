package utill;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.User;

import java.io.ByteArrayInputStream;

public class ImageFormatter {

    public static ImageView getImageView(User user, int height) {
        Image image = new Image(new ByteArrayInputStream(user.getProfilePicture()));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(height);
        imageView.setFitHeight(height);

        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(imageView.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
        imageView.setClip(clip);
        return imageView;
    }
}
