package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;
import util.Coordinates;
import util.Grid;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Main class for the Tip Over graphical user interface
 * @author Jack Schneider
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer< TipOverModel, Object > {

    private TipOverModel model;
    private String filename;
    private Label interactionMessage;
    private GridPane optionPane;
    private Button upBtn;
    private Button rightBtn;
    private Button downBtn;
    private Button leftBtn;
    private Button loadBtn;
    private Button reloadBtn;
    private Button hintBtn;
    private GridPane tablePane;
    private Label[][] tableLabelList;

    /**
     * Initializes the GUI
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        interactionMessage = new Label();
        if(getParameters().getRaw().size() == 1){
            filename = getParameters().getRaw().get(0);
            try {
                model = new TipOverModel(filename);
                interactionMessage.setText("New file loaded.");
            } catch (FileNotFoundException e) {
                interactionMessage.setText("File not found!");
            }
        } else {
            model = new TipOverModel();
        }
        model.addObserver(this);
    }

    /**
     * Starts the GUI
     * @param stage the main stage of the GUI
     */
    @Override
    public void start( Stage stage ) {
        stage.setTitle( "Tip Over" );

        BorderPane mainPane = new BorderPane();
        mainPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        interactionMessage = new Label();
        interactionMessage.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        interactionMessage.setText("New file loaded.");

        tablePane = new GridPane();
        tablePane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ColumnConstraints tableColumnLimit = new ColumnConstraints();
        tableColumnLimit.setPercentWidth(Math.round(100.0/((TipOverConfig) model.getCurrentConfig()).getTableWidth()));
        RowConstraints tableRowLimit = new RowConstraints();
        tableRowLimit.setPercentHeight(Math.round(100.0/((TipOverConfig) model.getCurrentConfig()).getTableHeight()));
        tablePane.getColumnConstraints().addAll(tableColumnLimit);
        tablePane.getRowConstraints().addAll(tableRowLimit);
        tableLabelList = new Label[((TipOverConfig) model.getCurrentConfig()).getTableWidth()]
                [((TipOverConfig) model.getCurrentConfig()).getTableHeight()];
        for (int y = 0;y < ((TipOverConfig) model.getCurrentConfig()).getTableHeight();y++) {
            for (int x = 0;x < ((TipOverConfig) model.getCurrentConfig()).getTableWidth();x++) {
                Label space = new Label();
                space.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                space.setText(((TipOverConfig) model.getCurrentConfig()).getTable().get(y,x).toString());
                tablePane.add(space, x, y);
                tableLabelList[x][y] = space;
            }
        }

        optionPane = new GridPane();
        optionPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        ColumnConstraints optionColumnLimit = new ColumnConstraints();
        optionColumnLimit.setPercentWidth(100);
        RowConstraints optionRowLimit = new RowConstraints();
        optionRowLimit.setPercentHeight(100.0/7);
        optionPane.getColumnConstraints().addAll(optionColumnLimit);
        optionPane.getRowConstraints().addAll(optionRowLimit);

        upBtn = new Button();
        upBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        upBtn.setText("North");
        optionPane.add(upBtn,0,0);
        rightBtn = new Button();
        rightBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        rightBtn.setText("East");
        optionPane.add(rightBtn,0,1);
        downBtn = new Button();
        downBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        downBtn.setText("South");
        optionPane.add(downBtn,0,3);
        leftBtn = new Button();
        leftBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        leftBtn.setText("West");
        optionPane.add(leftBtn,0,2);
        loadBtn = new Button();
        loadBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        loadBtn.setText("Load");
        optionPane.add(loadBtn,0,4);
        reloadBtn = new Button();
        reloadBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        reloadBtn.setText("Reload");
        optionPane.add(reloadBtn,0,5);
        hintBtn = new Button();
        hintBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hintBtn.setText("Hint");
        optionPane.add(hintBtn,0,6);

        upBtn.setOnAction(event-> model.move("N"));
        rightBtn.setOnAction(event-> model.move("E"));
        downBtn.setOnAction(event-> model.move("S"));
        leftBtn.setOnAction(event-> model.move("W"));
        loadBtn.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("load");
            File file = chooser.showOpenDialog(new Stage());
            filename = file.getAbsolutePath();
            TipOverConfig newFileConfig = new TipOverConfig(new Grid(new Object(),0,0),
                    0,0,new Coordinates(0,0), new Coordinates(0,0));
            try {
                newFileConfig = new TipOverConfig(filename);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (((TipOverConfig) this.model.getCurrentConfig()).getTableHeight() > newFileConfig.getTableHeight() ||
                    ((TipOverConfig) this.model.getCurrentConfig()).getTableWidth() > newFileConfig.getTableWidth()) {
                for (int y = 0; y < ((TipOverConfig) this.model.getCurrentConfig()).getTableHeight();y++) {
                    for (int x =0;x < ((TipOverConfig) this.model.getCurrentConfig()).getTableWidth();x++) {
                        if (y+1 > newFileConfig.getTableHeight() || x+1 > newFileConfig.getTableWidth()) {
                            tableLabelList[x][y].setText("");
                        } else {
                            tableLabelList[x][y].setText(newFileConfig.getTable().get(y,x).toString());
                        }
                    }
                }
            }
            if (((TipOverConfig) this.model.getCurrentConfig()).getTableHeight() < newFileConfig.getTableHeight() ||
                    ((TipOverConfig) this.model.getCurrentConfig()).getTableWidth() < newFileConfig.getTableWidth()) {
                Label[][] newTableLabelList = new Label[newFileConfig.getTableWidth()][newFileConfig.getTableHeight()];
                for (int y = 0; y < newFileConfig.getTableHeight();y++) {
                    for (int x =0;x < newFileConfig.getTableWidth();x++) {
                        Label space = new Label();
                        space.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                        space.setText(newFileConfig.getTable().get(y,x).toString());
                        if (y+1 > ((TipOverConfig) this.model.getCurrentConfig()).getTableHeight() ||
                                x+1 > ((TipOverConfig) this.model.getCurrentConfig()).getTableWidth()) {
                            newTableLabelList[x][y] = space;
                            tablePane.add(space, x, y);
                        } else {
                            newTableLabelList[x][y] = tableLabelList[x][y];
                            newTableLabelList[x][y].setText(newFileConfig.getTable().get(y,x).toString());
                        }
                    }
                }
                tableLabelList = newTableLabelList;
            }
            this.model.load(filename);
        });
        reloadBtn.setOnAction(event-> {
            try {
                model.reload();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        hintBtn.setOnAction(event-> model.hint());

        mainPane.setTop(interactionMessage);
        mainPane.setLeft(tablePane);
        mainPane.setBottom(optionPane);

        Scene scene = new Scene( mainPane, 400, 300 );
        stage.setScene( scene );
        stage.show();
    }

    /**
     * Updates the GUI
     * @param tipOverModel the tip over model
     * @param o optional parameter
     */
    @Override
    public void update( TipOverModel tipOverModel, Object o ) {
        // Checks if player has won and further updates message text
        if (this.model.getCurrentConfig().isSolution()){
            interactionMessage.setText("YOU WON!");
        } else if (this.model.getTipMovePerformed()) {
            interactionMessage.setText("A tower has been tipped over.");
            this.model.setTipMovePerformed(false);
        } else if (this.model.getReloadState()) {
            interactionMessage.setText("File loaded.");
            this.model.setReloadState(false);
        } else if (this.model.getNonMoveState()) {
            interactionMessage.setText("No crate or tower there.");
            this.model.setNonMoveState(false);
        } else {
            interactionMessage.setText("");
        }

        // Updates board
        for (int y = 0;y < ((TipOverConfig) this.model.getCurrentConfig()).getTableHeight();y++) {
            for (int x = 0;x < ((TipOverConfig) this.model.getCurrentConfig()).getTableWidth();x++) {
                tableLabelList[x][y].setText(((TipOverConfig) this.model.getCurrentConfig()).getTable().get(y,x).toString());
            }
        }
    }

    /**
     * The main function for running the Tip Over GUI
     * @param args filename argument
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }
}
