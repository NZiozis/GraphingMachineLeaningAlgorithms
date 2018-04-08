package ui;

import actions.AppActions;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import settings.AppPropertyTypes;
import vilij.propertymanager.PropertyManager;
import vilij.templates.ApplicationTemplate;
import vilij.templates.UITemplate;

import static vilij.settings.PropertyTypes.GUI_RESOURCE_PATH;
import static vilij.settings.PropertyTypes.ICONS_RESOURCE_PATH;

/**
 * This is the application's user interface implementation.
 *
 * @author Ritwik Banerjee
 */
public final class AppUI extends UITemplate{

    /**
     * The application to which this class of actions belongs.
     */
    private ApplicationTemplate applicationTemplate;

    @SuppressWarnings("FieldCanBeLocal")
    private Button                   scrnshotButton; // toolbar button to take a screenshot of the data
    private Button                   editDoneButton; // toolbar button to edit the textArea when inputting new data
    private LineChart<Number,Number> chart;          // the chart where data will be displayed
    private TextArea                 textArea;       // text area for new data input
    private boolean                  hasNewText;
    private Text                     loadedInFileText; // text displayed when
    private ToggleGroup              algorithmTypes;   // this will hold the algoTypes in the form of radio buttons
    private ToggleGroup              algorithms;       // This will hold the algoritms of the currently selected type.

    public AppUI(Stage primaryStage, ApplicationTemplate applicationTemplate){
        super(primaryStage, applicationTemplate);
        this.applicationTemplate = applicationTemplate;
    }

    public ToggleGroup getAlgorithms(){
        return algorithms;
    }

    public ToggleGroup getAlgorithmTypes(){
        return algorithmTypes;
    }

    public LineChart<Number,Number> getChart(){
        return chart;
    }

    public Button getEditDoneButton(){
        return editDoneButton;
    }

    public TextArea getTextArea(){
        return textArea;
    }

    public void setLoadedInFileText(String text){
        this.loadedInFileText.setText(text);
    }

    @Override
    protected void setResourcePaths(ApplicationTemplate applicationTemplate){
        super.setResourcePaths(applicationTemplate);
    }

    @Override
    protected void setToolBar(ApplicationTemplate applicationTemplate){
        super.setToolBar(applicationTemplate);
        PropertyManager manager = applicationTemplate.manager;
        String iconsPath = SEPARATOR + String.join(SEPARATOR, manager.getPropertyValue(GUI_RESOURCE_PATH.name()),
                                                   manager.getPropertyValue(ICONS_RESOURCE_PATH.name()));
        String scrnshoticonPath =
                String.join(SEPARATOR, iconsPath, manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_ICON.name()));
        scrnshotButton =
                setToolbarButton(scrnshoticonPath, manager.getPropertyValue(AppPropertyTypes.SCREENSHOT_TOOLTIP.name()),
                                 true);
        editDoneButton = new Button(manager.getPropertyValue(AppPropertyTypes.EDIT_TEXT.name()));

        toolBar.getItems().addAll(scrnshotButton, editDoneButton);
    }

    @Override
    protected void setToolbarHandlers(ApplicationTemplate applicationTemplate){
        applicationTemplate.setActionComponent(new AppActions(applicationTemplate));
        newButton.setOnAction(e -> applicationTemplate.getActionComponent().handleNewRequest());
        saveButton.setOnAction(e -> applicationTemplate.getActionComponent().handleSaveRequest());
        loadButton.setOnAction(e -> applicationTemplate.getActionComponent().handleLoadRequest());
        exitButton.setOnAction(e -> applicationTemplate.getActionComponent().handleExitRequest());
        scrnshotButton.setOnAction(
                e -> ((AppActions) applicationTemplate.getActionComponent()).handleScreenshotRequest());
        editDoneButton.setOnAction(e -> ((AppActions) applicationTemplate.getActionComponent()).handleEditDone());
    }

    @Override
    public void initialize(){
        layout();
        setWorkspaceActions();
        appPane.getStylesheets()
                .add(applicationTemplate.manager.getPropertyValue(AppPropertyTypes.CSS_RESOURCE_PATH.name()));
    }

    @Override
    public void clear(){
        textArea.clear();
        chart.getData().clear();
    }

    public String getCurrentText(){
        return textArea.getText();
    }

    private void layout(){
        PropertyManager manager = applicationTemplate.manager;
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(manager.getPropertyValue(AppPropertyTypes.CHART_TITLE.name()));

        VBox leftPanel = new VBox(8);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(10));

        VBox.setVgrow(leftPanel, Priority.ALWAYS);
        leftPanel.setMaxSize(windowWidth * 0.29, windowHeight * 0.3);
        leftPanel.setMinSize(windowWidth * 0.29, windowHeight * 0.3);

        Text leftPanelTitle = new Text(manager.getPropertyValue(AppPropertyTypes.LEFT_PANE_TITLE.name()));
        String fontname = manager.getPropertyValue(AppPropertyTypes.LEFT_PANE_TITLEFONT.name());
        Double fontsize = Double.parseDouble(manager.getPropertyValue(AppPropertyTypes.LEFT_PANE_TITLESIZE.name()));
        leftPanelTitle.setFont(Font.font(fontname, fontsize));

        textArea = new TextArea();
        textArea.setDisable(true);

        HBox processButtonsBox = new HBox();
        HBox.setHgrow(processButtonsBox, Priority.ALWAYS);

        loadedInFileText = new Text(manager.getPropertyValue(AppPropertyTypes.NO_DATA_LOADED_IN_PLACEHOLDER.name()));
        loadedInFileText.setWrappingWidth(leftPanel.getMaxWidth());

        ScrollPane algorithmHouse = new ScrollPane();
        GridPane algorithms = new GridPane();
        algorithmHouse.setContent(algorithms);

        //TODO so what you have to do here is find a way to add the radio buttons, not the groups. It might not make
        // sense to do this on initialization, at least the for the second case. There should be a way to toggle back
        // and forth as well as see the relevant ones for each type without bugs. Maybe a foreach is in order?

        leftPanel.getChildren().addAll(leftPanelTitle, textArea, processButtonsBox, loadedInFileText, algorithmHouse);

        StackPane rightPanel = new StackPane(chart);
        rightPanel.setMaxSize(windowWidth * 0.69, windowHeight * 0.69);
        rightPanel.setMinSize(windowWidth * 0.69, windowHeight * 0.69);
        StackPane.setAlignment(rightPanel, Pos.CENTER);

        workspace = new HBox(leftPanel, rightPanel);
        HBox.setHgrow(workspace, Priority.ALWAYS);

        appPane.getChildren().add(workspace);
        VBox.setVgrow(appPane, Priority.ALWAYS);
    }

    private void setWorkspaceActions(){
        setTextAreaActions();
    }

    private void setTextAreaActions(){
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                if (!newValue.equals(oldValue)){
                    if (!newValue.isEmpty()){
                        ((AppActions) applicationTemplate.getActionComponent()).setIsUnsavedProperty(true);
                        if (newValue.charAt(newValue.length() - 1) == '\n') hasNewText = true;
                        newButton.setDisable(false);
                        saveButton.setDisable(false);
                    }
                    else{
                        hasNewText = true;
                        newButton.setDisable(true);
                        saveButton.setDisable(true);
                    }
                }
            }
            catch (IndexOutOfBoundsException e){
                System.err.println(newValue);
            }
        });
    }
}
