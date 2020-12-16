package simulator.right_sidebar;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import simulator.custom_controls.NumberField;
import simulator.map.Animal;
import simulator.map.Genotype;

import java.util.LinkedList;
import java.util.Optional;

public class InteractiveSidebarController {
    @FXML private VBox followedAnimalInfoVBox;
    @FXML private VBox controlsVBox;
    @FXML private BarChart<String,Integer> currentViewedAnimalGenotypeBarChart;
    @FXML private Label displayedAnimalFromFieldLabel;
    @FXML private Label numberOfChildrenLabel;
    @FXML private Label numberOfDescendantsLabel;
    @FXML private Label dayOfDeathLabel;
    @FXML private Button pauseButton;
    @FXML private Button selectMostPopularButton;
    @FXML private NumberField nInputField;
    private final XYChart.Series<String,Integer> genotypeSeries;

    private boolean isPaused;
    private final static String PAUSE_BUTTON_TEXT = "Pause";
    private final static String UNPAUSE_BUTTON_TEXT = "Unpause";

    private IPauseEventHandler iPauseEventHandler;
    private ISelectMostPopularEventHandler iSelectMostPopularEventHandler;

    private LinkedList<Animal> viewedAnimals;
    private int currentlyViewedAnimalIndex;
    private FollowedAnimalStatisticsObserver followedAnimalStatisticsObserver;



    public InteractiveSidebarController(){
        isPaused = false;
        iPauseEventHandler = null;
        viewedAnimals = new LinkedList<>();
        currentlyViewedAnimalIndex = 0;
        genotypeSeries = new XYChart.Series<>();
        iSelectMostPopularEventHandler = () -> {};
        Platform.runLater(this::init);
    }

    private void init() {
        for (int i = 0; i < Genotype.GENS_TYPES_NUMBER; i++) {
            genotypeSeries.getData().add(new XYChart.Data<>(String.valueOf(i),i));
        }
        currentViewedAnimalGenotypeBarChart.getData().add(genotypeSeries);
    }

    @FXML
    public void onPauseButtonClicked(){
        if(isPaused)unpause();
        else pause();
    }

    private void pause(){
        pauseButton.setText(UNPAUSE_BUTTON_TEXT);
        isPaused = true;
        controlsVBox.setDisable(false);
        if(followedAnimalStatisticsObserver != null) followedAnimalInfoVBox.setDisable(false);
        selectMostPopularButton.setDisable(false);

        if(iPauseEventHandler == null) return;
        iPauseEventHandler.onPause();


    }

    private void unpause(){
        pauseButton.setText(PAUSE_BUTTON_TEXT);
        isPaused = false;
        controlsVBox.setDisable(true);
        followedAnimalInfoVBox.setDisable(true);
        selectMostPopularButton.setDisable(true);

        viewGenotype(new int[Genotype.GENS_TYPES_NUMBER]);
        displayedAnimalFromFieldLabel.setText("0/0");
        viewedAnimals.clear();

        if(iPauseEventHandler == null) return;
        iPauseEventHandler.onUnPause();

    }

    public void setOnPauseEventHandler(IPauseEventHandler iPauseEventHandler){
        this.iPauseEventHandler = iPauseEventHandler;
    }

    public void viewAnimals(LinkedList<Animal> animalsOnField) {
        viewedAnimals = animalsOnField;
        if(viewedAnimals.size() == 0)return;

        changeCurrentDisplayedAnimal(0);

    }

    private void changeCurrentDisplayedAnimal(int index) {
        currentlyViewedAnimalIndex = index;
        viewGenotype(viewedAnimals.get(index).getGeneFrequency());
        updateDisplayedAnimalFromFieldLabel();
    }

    private void viewGenotype(int[] currentAnimalGeneFrequency){
        if(currentAnimalGeneFrequency.length != Genotype.GENS_TYPES_NUMBER){
            throw new IllegalArgumentException("currentAnimalGeneFrequency has wrong length");
        }

        genotypeSeries.getData().forEach(data -> {
            int i = Integer.parseInt(data.getXValue());
            data.setYValue(currentAnimalGeneFrequency[i]);
        });


    }

    private void updateDisplayedAnimalFromFieldLabel(){
        displayedAnimalFromFieldLabel.setText(currentlyViewedAnimalIndex+1 + "/" + viewedAnimals.size());
    }

    @FXML
    private void onNextButtonClicked(){
        if(viewedAnimals.isEmpty() || controlsVBox.isDisabled())return;
        int nextIndex = currentlyViewedAnimalIndex + 1;
        if(nextIndex >= viewedAnimals.size())return;
        changeCurrentDisplayedAnimal(nextIndex);
    }

    @FXML
    private void onPreviousButtonClicked(){
        if(viewedAnimals.isEmpty() || controlsVBox.isDisabled())return;
        int previousIndex = currentlyViewedAnimalIndex - 1;
        if (previousIndex < 0)return;
        changeCurrentDisplayedAnimal(previousIndex);
    }

    @FXML
    private void onFollowButtonClicked(){
        if(viewedAnimals.isEmpty() || controlsVBox.isDisabled())return;
        if(followedAnimalStatisticsObserver != null) followedAnimalStatisticsObserver.onUnfollowAnimal();

        Animal followedAnimal = viewedAnimals.get(currentlyViewedAnimalIndex);
        followedAnimalStatisticsObserver = new FollowedAnimalStatisticsObserver(followedAnimal);
        followedAnimalInfoVBox.setDisable(false);
        followedAnimal.selectAnimal(Animal.FOLLOWED_ANIMAL_COLOR);

        resetFollowedAnimalInfoVBox();

    }

    private void resetFollowedAnimalInfoVBox() {
        numberOfDescendantsLabel.setText("N/A");
        numberOfChildrenLabel.setText("N/A");
        dayOfDeathLabel.setText("N/A");
    }

    @FXML
    private void onNInput(){
        if(followedAnimalInfoVBox.isDisabled())return;
        int n = nInputField.getInt();
        if(!followedAnimalStatisticsObserver.containsDataForDay(n)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No data for day " + n);
            alert.show();
            return;
        }

        numberOfChildrenLabel.setText(String.valueOf(followedAnimalStatisticsObserver.getNumberOfChildrenToDay(n)));
        numberOfDescendantsLabel.setText(String.valueOf(followedAnimalStatisticsObserver.getNumberOfDescendantsToDay(n)));


    }


    public void updateFollowStatistics(int day) {
        if(followedAnimalStatisticsObserver == null) return;
        followedAnimalStatisticsObserver.collectDataForDay(day);

        Optional<Integer> dayOfDeathOptional = followedAnimalStatisticsObserver.getDayOfDead();
        dayOfDeathOptional.ifPresent( dayOfDeath -> dayOfDeathLabel.setText(String.valueOf(dayOfDeath)));

    }

    public void setOnSelectMostPopularEventHandler(ISelectMostPopularEventHandler iSelectMostPopularEventHandler){
        this.iSelectMostPopularEventHandler=iSelectMostPopularEventHandler;
    }

    @FXML
    private void onSelectMostPopular(){
        if(selectMostPopularButton.isDisabled())return;
        iSelectMostPopularEventHandler.selectMostPopular();
    }
}
