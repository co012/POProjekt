package simulator;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FollowAnimalController {
    @FXML private BarChart<Integer,Integer> followAnimalGenotypeBarChart;
    @FXML private Button previousButton;
    @FXML private Label displayedAnimalFromFieldLabel;
    @FXML private Button nextButton;
    @FXML private Button followButton;
    @FXML private Label numberOfChildrenLabel;
    @FXML private Label numberOfDescendantsLabel;
    @FXML private Label dayOfDeathLabel;
}
