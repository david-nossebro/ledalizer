package org.ledalizer;

import ddf.minim.*;
import ddf.minim.analysis.BeatDetect;
import eu.hansolo.enzo.canvasled.Led;
import eu.hansolo.enzo.canvasled.LedBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by david on 2015-09-29.
 */
public class Main extends Application {

    public static final int MATRIX_HEIGHT = 30,
                            MATRIX_WIDTH = 5,
                            MATRIX_COUNT = 3;

    MatrixPane[] matrixPanes = new MatrixPane[MATRIX_COUNT];

    class MinimInput {
        String sketchPath( String fileName ) {
            return "";
        }
        InputStream createInput(String fileName) {
            return new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
        };
    }

    @Override
    public void start(Stage stage) throws Exception {
        FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 10, 5);

            for(int matrixId=0; matrixId < MATRIX_COUNT; matrixId++) {
                MatrixPane matrixPane = new MatrixPane(MATRIX_WIDTH, MATRIX_HEIGHT);
                matrixPane.addAllLedsEventHandler(MouseDragEvent.MOUSE_DRAG_ENTERED, toggleLedEventHandler);
                matrixPane.addAllLedsEventHandler(MouseEvent.MOUSE_PRESSED, toggleLedEventHandler);
                matrixPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                 @Override
                                                 public void handle(MouseEvent event) {
                                                     if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                                                         ((MatrixPane) event.getSource()).setAllOn();
                                                     }
                                                 }
                                             }
                );
                matrixPanes[matrixId] = matrixPane;
                flowPane.getChildren().add(matrixPane);
            }

        Scene scene = new Scene(flowPane);

        scene.addEventFilter(MouseEvent.DRAG_DETECTED , new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scene.startFullDrag();
            }
        });

        stage.setTitle("Ledalizer");
        stage.setScene(scene);
        stage.show();

        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                Minim minim = new Minim(new MinimInput());

                AudioInput input = minim.getLineIn(Minim.STEREO, 512);
                BeatDetect beatDetect = new BeatDetect(512, 44100.0f);
                beatDetect.detectMode(BeatDetect.SOUND_ENERGY);
                beatDetect.setSensitivity(50);
                //BeatListener bl = new BeatListener(beatDetect, input);

                /*
                FlashPanelTask hatFlash = new FlashPanelTask(matrixPanes[0], 300);
                FlashPanelTask snareFlash = new FlashPanelTask(matrixPanes[1], 300);
                FlashPanelTask kickFlash = new FlashPanelTask(matrixPanes[2], 300);
                */

                /*
                Thread hatFlashThread = new Thread(hatFlash);
                Thread snareFlashThread = new Thread(snareFlash);
                Thread kickFlashThread = new Thread(kickFlash);
                */

                int i = 0;

                while (true) {

                    beatDetect.detect(input.mix);

                    if(beatDetect.isOnset()) {
                        matrixPanes[0].setAllOn();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        matrixPanes[0].setAllOff();
                    }
                }
            }
        });

        th.start();
    }

    class FlashPanelTask implements Runnable {

        private MatrixPane matrixPane;
        private int time;

        public FlashPanelTask(MatrixPane matrixPane, int time) {
            this.matrixPane = matrixPane;
            this.time = time;
        }

        @Override
        public void run() {
            matrixPane.setAllOn();
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            matrixPane.setAllOff();
        }
    }

    class BeatListener implements AudioListener
    {
        private BeatDetect beat;
        private AudioInput source;

        BeatListener(BeatDetect beat, AudioInput source)
        {
            this.source = source;
            this.source.addListener(this);
            this.beat = beat;
        }

        public void samples(float[] samps) {
            beat.detect(source.mix);
        }

        public void samples(float[] sampsL, float[] sampsR) {
            beat.detect(source.mix);
        }
    }


    EventHandler toggleLedEventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            Object o = event.getSource();
            if(o instanceof Led) {
                Led l = (Led) o;
                l.setOn(!l.isOn());
            }
        }
    };

    EventHandler enableLedEventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            Object o = event.getSource();
            if(o instanceof Led) {
                ((Led) o).setOn(true);
            }
        }
    };
}
