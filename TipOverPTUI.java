package puzzles.tipover.ptui;

import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class of the Tip Over plain-text user interface
 * @author Jack Schneider
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object> {

    private TipOverModel model;

    /**
     * Creates a new Tip Over plain-text user interface using a filename
     * @param filename the file name used to create the plain-text user interface
     */
    public TipOverPTUI(String filename){
        try {
            this.model = new TipOverModel(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeView();
    }

    /**
     * Creates a new Tip Over plain-text user interface without use of a file name
     */
    public TipOverPTUI(){
        try {
            this.model = new TipOverModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeView();
    }

    /**
     * A function that continuously allows the player to use commands until they quit
     * @throws FileNotFoundException
     */
    public void run() throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        for(;;) {
            System.out.print("> ");
            String command = in.nextLine();
            String[] params = command.split(" ");
            if (params.length > 0) {
                if (params[0].equals("move")) {
                    if (params[1].equals("north")) {
                        this.model.move("N");
                    } else if (params[1].equals("east")) {
                        this.model.move("E");
                    } else if (params[1].equals("south")) {
                        this.model.move("S");
                    } else if (params[1].equals("west")) {
                        this.model.move("W");
                    } else {
                        System.out.println("Legal directions are");
                        System.out.println("[NORTH, EAST, SOUTH, WEST]");
                    }
                } else if (params[0].equals("load")) {
                    System.out.println("New file loaded.");
                    this.model.load(params[1]);
                } else if (params[0].equals("reload")) {
                    System.out.println("New file loaded.");
                    this.model.reload();
                } else if (params[0].equals("hint")) {
                    this.model.hint();
                } else if (params[0].equals("help")) {
                    this.model.help();
                } else if (params[0].equals("quit")) {
                    this.model.quit();
                } else {
                    System.out.println("Illegal command");
                    this.model.help();
                }
            }
        }
    }

    /**
     * Updates the model
     * @param tipOverModel the model being updated
     * @param o optional data a model can send to an observer (unused)
     */
    @Override
    public void update(TipOverModel tipOverModel, Object o) {
        if (this.model.getCurrentConfig().isSolution()){
            System.out.println("YOU WON!");
        }
        this.model.show();
    }

    /**
     * Initializes the view of the model
     */
    public void initializeView() {
        this.model.addObserver( this );
        update( this.model, null );
    }

    /**
     * The main function for running the Tip Over plain-text user interface
     * @param args arguments that may contain a file name
     */
    public static void main(String[] args ) throws FileNotFoundException{
        TipOverPTUI ptui;
        if(args.length == 1){
            System.out.println("New file loaded.");
            ptui = new TipOverPTUI(args[0]);
        } else {
            ptui = new TipOverPTUI();
        }
        ptui.run();
    }
}
