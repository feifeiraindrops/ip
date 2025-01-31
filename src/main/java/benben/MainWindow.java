package benben;
import benben.BenBen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import benben.BenBenException;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private BenBen benben;


    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaCat.png"));
    private Image benbenImage = new Image(this.getClass().getResourceAsStream("/images/DaBenBen.png"));
    private Circle clip = new Circle(250,200,80);

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getDukeDialog("Hello! I'm BenBen.\n" + "What can I do for you?", benbenImage)
        );
    }

    public void setDuke(BenBen b) {
        benben = b;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = benben.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, benbenImage)
        );
        userInput.clear();

        if (input.equals("exit")) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.exit();
                } catch (InterruptedException e) {
                    throw new BenBenException("The execution is interrupted! Please restart the app:(");
                }
            }).start();
        }
    }
}
