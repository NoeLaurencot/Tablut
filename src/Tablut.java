import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.TablutController;
import javafx.scene.image.Image;

import view.TablutView;
import view.TablutRootPane;

import javafx.application.Application;
import javafx.stage.Stage;

public class Tablut extends Application {

    public static void main(String[] args) {
        // Debug
        //Logger.setLevel(Logger.LOGGER_TRACE);
        //Logger.setVerbosity(Logger.VERBOSE_HIGH);

        if (args.length > 0) {
            System.err.println("Error: found args when expecting none");
            System.err.print("Usage: java Tablut");
            System.exit(1);
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Model model = new Model();

        StageFactory.registerModelAndView("tablut", "model.TablutStageModel", "view.TablutStageView");
        TablutRootPane rootPane = new TablutRootPane();
        TablutView tablutView = new TablutView(model, stage, rootPane);
        TablutController control = new TablutController(model, tablutView);
        control.setFirstStageName("tablut");

        stage.setTitle("Tablut");
        stage.getIcons().add(new Image(getClass().getResource("/images/king.png").toExternalForm()));
        stage.show();
    }
}
