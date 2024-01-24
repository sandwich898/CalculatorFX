package programm.sandwich.calculater;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.*;

public class Main extends Application {

    private final short columns = 5, rows = 4;
    private final Pane root = new Pane();
    private final Scene scene = new Scene(root, 320, 590);

    private List<Button> buildButtons() {

        List<Button> buttonList = new ArrayList<>();
        Button[] digitButtons = new Button[10];

        for(int i = 0; i < 20; i++)
            buttonList.add(new Button());

        buttonList.set(0, new Button("C"));
        buttonList.get(0).setId("deleteAll");
        buttonList.set(1, new Button("CE"));
        buttonList.get(1).setId("delete");
        buttonList.set(2, new Button("<"));
        buttonList.get(2).setId("undo");
        buttonList.set(7, new Button("+"));
        buttonList.get(7).setId("addition");
        buttonList.set(11, new Button("-"));
        buttonList.get(11).setId("subtraction");
        buttonList.set(15, new Button("*"));
        buttonList.get(15).setId("multiplication");
        buttonList.set(19, new Button("/"));
        buttonList.get(19).setId("divided");
        buttonList.set(3, new Button("="));
        buttonList.get(3).setId("equals");
        buttonList.set(18, new Button("."));
        buttonList.get(18).setId("dot");
        buttonList.set(16, new Button("+/-"));
        buttonList.get(16).setId("negativ");

        for (int i = 0; i < digitButtons.length; i++) {
            digitButtons[i] = new Button(String.valueOf(i));
            digitButtons[i].setId("digit:" + i);

            switch (i) {
                case 1, 2, 3 ->
                    buttonList.set(i + 3, digitButtons[i]);
                case 4, 5, 6 ->
                    buttonList.set(i + 4, digitButtons[i]);
                case 7, 8, 9 ->
                    buttonList.set(i + 5, digitButtons[i]);
                case 0 ->
                    buttonList.set(17, digitButtons[i]);

            }
        }
        return buttonList;
    }

    private void setPostions(double windowWidth, double windowHeight, List<Button> buttons, TextField textField) {
        double buttonWidth = windowWidth / rows;
        double buttonHeight = windowHeight / (columns  + 1);

        textField.setStyle("-fx-font-size: " + ((buttonHeight/10) * 3) + "px;");
        textField.setPrefSize(windowWidth, buttonHeight);

        int i = 0;
        int j = 1;
        for (Button b: buttons) {
            b.setPrefSize(buttonWidth, buttonHeight);

            b.setLayoutX(i * buttonWidth);
            b.setLayoutY(j * buttonHeight);

            i++;
            if(i == rows) {
                j++;
                i = 0;
            }
        }
    }

    private void buttonFunction(List<Button> buttons, TextField output) {
        for (Button b: buttons) {

            int digitNumber;
            if(b.getId().startsWith("digit")) {
                digitNumber = Integer.parseInt(b.getId().substring(b.getId().length() - 1));
                b.setOnMousePressed(event -> {
                    output.setText(output.getText() + digitNumber);
                });
            } else {
                digitNumber = 0;
            }

            switch (b.getId()) {
                case "addition" -> {
                    b.setOnMousePressed(e -> {
                        if(isLastIndexANumber(output.getText()))
                            output.setText(output.getText() + "+");
                    });
                }
                case "subtraction" -> {
                    b.setOnMousePressed(e -> {
                        if(isLastIndexANumber(output.getText()))
                            output.setText(output.getText() + "-");
                    });
                }
                case "multiplication" -> {
                    b.setOnMousePressed(e -> {
                        if(isLastIndexANumber(output.getText()))
                            output.setText(output.getText() + "*");
                    });
                }
                case "divided" -> {
                    b.setOnMousePressed(e -> {
                        if(isLastIndexANumber(output.getText()))
                            output.setText(output.getText() + "/");
                    });
                }
                case "dot" -> {
                    b.setOnMousePressed(e -> {
                        if(canIWriteADot(output.getText()))
                            output.setText(output.getText() + ".");
                    });
                }
                case "equals" -> {
                    b.setOnMousePressed(e -> {
                        output.setText(Calculate.start(output.getText()));
                    });
                }
                case "deleteAll" -> {
                    b.setOnMousePressed(e -> {
                        output.setText("");
                    });
                }
                case "delete" -> {
                    b.setOnMousePressed(e -> {
                        if(!output.getText().isEmpty())
                            output.setText(output.getText().substring(0, output.getText().length() - 1));
                    });
                }
                case "negativ" -> {
                    b.setOnMousePressed(e -> {
                        if(output.getText().isEmpty())
                            output.setText("-");
                        else if(output.getText().equals("-"))
                            output.setText("");

                    });
                }
            }
        }
    }

    private boolean isLastIndexANumber(String s) {
        return !(s.endsWith("+") || s.endsWith("-") || s.endsWith("*") || s.endsWith("/") || s.endsWith("."));
    }

    private boolean canIWriteADot(String s) {
        if(!s.isEmpty() && isLastIndexANumber(s)) {
            for (int i = s.length() - 1; i > 1; i--) {
                if(s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '*' || s.charAt(i) == '/')
                    return true;
                else if(s.charAt(i) == '.')
                    return false;
            }
        } else return false;
        return true;
    }

    @Override
    public void start(Stage stage) {
        List<Button> allButtons = buildButtons();

        TextField textField = new TextField();
        textField.setLayoutY(0);
        textField.setLayoutX(0);
        textField.setAlignment(Pos.CENTER_RIGHT);

        buttonFunction(allButtons, textField);

        ChangeListener<Number> windowChange = (observable, oldValue, newValue) -> {
            setPostions(stage.getScene().getWidth(), stage.getScene().getHeight(), allButtons, textField);
        };

        //Hier ist noch ein Fehler, wenn das Fenster durch den MAXIMAL button vergrößert wird, paßen sich die Buttons nicht mit an.
        //Das gleiche, wenn man die das Fenster an den Rand schiebt und es sich automatisch ganz lang macht!
        stage.widthProperty().addListener(windowChange);
        stage.heightProperty().addListener(windowChange);

        root.getChildren().addAll(allButtons);
        root.getChildren().add(textField);

        stage.setTitle("CalculatorFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}