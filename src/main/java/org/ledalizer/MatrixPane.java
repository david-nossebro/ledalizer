package org.ledalizer;

import eu.hansolo.enzo.canvasled.Led;
import eu.hansolo.enzo.canvasled.LedBuilder;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 2015-09-30.
 */
public class MatrixPane extends GridPane {

    int matrixId;
    int width, height;
    Led[][] leds;
    List<Led> ledList;

    public MatrixPane(int width, int height, int matrixId) {
        init(width, height, matrixId);
    }

    public MatrixPane(int width, int height) {
        init(width, height);
    }

    private void init(int width, int height, int matrixId) {
        this.matrixId = matrixId;
        init(width, height);
    }

    private void init(int width, int height) {
        this.width = width;
        this.height = height;
        leds = new Led[width][height];
        ledList = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Led led = LedBuilder.create()
                        .ledColor(Color.LIGHTBLUE)
                        .frameVisible(true)
                        .build();

                leds[x][y] = led;
                ledList.add(led);
                this.add(led, x, y);
            }
        }
    }

    public void addAllLedsEventHandler(final EventType<? extends Event> eventType,
                                       final EventHandler<? super Event> eventHandler) {
        ledList.forEach((led) -> led.addEventHandler(eventType, eventHandler));
    }

    public void addLedEventHandler(MatrixPoint point, final EventType<Event> eventType,
                                   final EventHandler<? super Event> eventHandler ) {
        leds[point.getX()][point.getY()].addEventHandler(eventType, eventHandler);
    }

    public void setAllColor(Color color) {
        setAll(null, color);
    }

    public void setAllOff() {
        setAll(false, null);
    }

    public void setAllOn() {
        setAll(true, null);
    }

    public void setAll(Boolean on, Color color) {
        ledList.forEach(led -> {
                setLed(led, on, color);
            }
        );
    }

    public void setUpToColor(int limit, Color color) {
        setUpTo(limit, null, color);
    }

    public void setOnUpTo(int limit) {
        setUpTo(limit, true, null);
    }

    public void setOffUpTo(int limit) {
        setUpTo(limit, false, null);
    }

    public void setUpTo(int limit, Boolean on, Color color) {
        for(int y=0; y<limit; y++) {
            setRow(y, on, color);
        }
    }

    public void setDownToColor(int limit, Color color) {
        setDownTo(limit, null, color);
    }

    public void setOffDownTo(int limit) {
        setDownTo(limit, false, null);
    }

    public void setOnDownTo(int limit) {
        setDownTo(limit, true, null);
    }

    public void setDownTo(int limit, Boolean on, Color color) {
        for(int y=limit; y<height; y++) {
            setRow(y, on, color);
        }
    }

    public void setRowColor(int y, Color color) {
        setRow(y, null, color);
    }

    public void setRowOn(int y) {
        setRow(y, true, null);
    }

    public void setRowOff(int y) {
        setRow(y, false, null);
    }

    public void setRow(int y, Boolean on, Color color) {
        for(int x=0; x<width; x++) {
            setLed(x, y, on, color);
        }
    }

    public void setColumnOn(int x) {
        setColumn(x, true, null);
    }

    public void setColumnOff(int x) {
        setColumn(x, false, null);
    }

    public void setColumnColor(int x, Color color) {
        setColumn(x, null, color);
    }

    public void setColumn(int x, Boolean on, Color color) {
        for(int y=0; y<height; y++) {
            setLed(x, y, on, color);
        }
    }

    public void setRectangleAreaOn(int x1, int y1, int x2, int y2) {
        setRectangleArea(x1, y1, x2, y2, true, null);
    }

    public void setRectangleAreaOff(int x1, int y1, int x2, int y2) {
        setRectangleArea(x1, y1, x2, y2, false, null);
    }

    public void setRectangleAreaColor(int x1, int y1, int x2, int y2, Color color) {
        setRectangleArea(x1, y1, x2, y2, null, color);
    }

    public void setRectangleArea(int x1, int y1, int x2, int y2, Boolean on, Color color) {
        int xLow = getLowest(x1, x2);
        int xHigh = getHighest(x1, x2);
        int yLow = getLowest(y1, y2);
        int yHigh = getHighest(y1, y2);

        for(int x=xLow; x < xHigh; x++) {
            for(int y=yLow; y < yHigh; y++) {
                setLed(x, y, on, color);
            }
        }
    }

    private int getHighest(int i1, int i2) {
        return i1 > i2 ? i1 : i2;
    }

    private int getLowest(int i1, int i2) {
        return i1 < i2 ? i1 : i2;
    }

    public void setLedColor(int x, int y, Color color) {
        setLed(leds[x][y], null, color);
    }

    public void setLedOff(int x, int y) {
        setLed(leds[x][y], false, null);
    }

    public void setLedOn(int x, int y) {
        setLed(leds[x][y], true, null);
    }

    public void setLed(int x, int y, Boolean on, Color color) {
        setLed(leds[x][y], on, color);
    }

    public void setLedsColor(List<MatrixPoint> points, Color color) {
        setLeds(points, null, color);
    }

    public void setLedsOn(List<MatrixPoint> points) {
        setLeds(points, true, null);
    }

    public void setLedsOff(List<MatrixPoint> points) {
        setLeds(points, false, null);
    }

    public void setLeds(List<MatrixPoint> points, Boolean on, Color color) {
        points.forEach(p -> setLed(p.getX(), p.getY(), on, color));
    }

    public void setLedOn(MatrixPoint point) {
        setLed(point.getX(), point.getY(), true, null);
    }



    private void setLed(Led led, Boolean on, Color color) {
        if(on != null) {
            led.setOn(on);
        }
        if(color != null) {
            led.setLedColor(color);
        }
    }
}
