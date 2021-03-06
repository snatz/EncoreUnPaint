package client.controller;

import client.controller.drawingComponents.LineComponentController;
import client.controller.drawingComponents.OvalComponentController;
import client.controller.drawingComponents.RectangleComponentController;
import client.model.action.CreateLineAction;
import client.model.action.CreateOvalAction;
import client.model.action.CreateRectangleAction;
import client.model.drawing.DrawingListener;
import client.model.drawingComponents.*;
import client.view.DrawingContainerView;
import client.view.drawingComponents.DrawingComponentView;
import client.view.drawingComponents.LineComponentView;
import client.view.drawingComponents.OvalComponentView;
import client.view.drawingComponents.RectangleComponentView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class DrawingContainerController implements DrawingListener {
    private DrawingContainerView drawingContainerView;
    private Point startingPoint = null, endingPoint = null;
    private boolean invertWidth = false, invertHeight = false;

    DrawingContainerController(DrawingContainerView drawingContainerView) {
        this.drawingContainerView = drawingContainerView;

        drawingContainerView.addMouseListener(new DrawingContainerListener());
        drawingContainerView.getDrawing().addDrawingListener(this);

        updateView();
    }

    private void updateView() {
        if (drawingContainerView.getDrawingComponentViewList().size() != drawingContainerView.getDrawing().getDrawingComponents().size()) {
            refreshViews();
        }

        drawingContainerView.update();
    }

    private void refreshViews() {
        drawingContainerView.getDrawingComponentViewList().clear();
        for (DrawingComponent component : drawingContainerView.getDrawing().getDrawingComponents()) {
            DrawingComponentView drawingComponentView;

            if (component instanceof LineComponent) {
                drawingComponentView = new LineComponentView((LineComponent) component);
                new LineComponentController((LineComponentView) drawingComponentView);
            } else if (component instanceof OvalComponent) {
                drawingComponentView = new OvalComponentView((OvalComponent) component);
                new OvalComponentController((OvalComponentView) drawingComponentView);
            } else if (component instanceof RectangleComponent) {
                drawingComponentView = new RectangleComponentView((RectangleComponent) component);
                new RectangleComponentController((RectangleComponentView) drawingComponentView);
            } else {
                return;
            }

            drawingContainerView.add(drawingComponentView);
            drawingContainerView.addView(drawingComponentView);
            drawingComponentView.getDrawingComponent().addDrawingComponentListener(new DrawingComponentListener() {
                @Override
                public void onMoved() {

                }

                @Override
                public void onRemoved() {
                    updateView();
                }

                @Override
                public void onColorChanged() {
                    updateView();
                }

                @Override
                public void onSelected() { }

                @Override
                public void onUnselected(){}
            });
        }
    }

    @Override
    public void onDrawingComponentAdded(DrawingComponent drawingComponent) {
        updateView();
    }

    @Override
    public void onDrawingComponentRemoved(DrawingComponent drawingComponent) {
        updateView();
    }

    private class DrawingContainerListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent me) {
            if (drawingContainerView.getDrawing().getCurrentComponentSelected() != null) {
                drawingContainerView.getDrawing().getCurrentComponentSelected().fireUnselected();
                drawingContainerView.getDrawing().setCurrentComponentSelected(null);
            }

            String currentStatus = drawingContainerView.getCurrentStatus();

            if (currentStatus.equals("none"))
                return;

            switch (currentStatus) {
                case "line":
                case "rectangle":
                case "oval":
                    // when the user click, we memorize the starting coordonates
                    startingPoint = me.getPoint();
                    break;
                case "fill":
                    break;
                case "selection":
                    break;
            }

        }

        @Override
        public void mouseReleased(MouseEvent me){
            endingPoint = me.getPoint();
            switch (drawingContainerView.getCurrentStatus()) {
                case "selection": {
                    break;
                }

                case "fill": {
                    break;
                }

                case "line": {
                    if(!(startingPoint.equals(endingPoint))){
                        checkLinesCoordonates();
                        CreateLineAction createLineAction = new CreateLineAction(
                                drawingContainerView.getDrawing(),
                                startingPoint,
                                endingPoint,
                                invertWidth,
                                invertHeight,
                                drawingContainerView.getCurrentColorBackground()
                        );
                        drawingContainerView.getDrawing().getActionStack().push(createLineAction);
                    }
                    break;
                }

                case "rectangle": {
                    if(!(startingPoint.equals(endingPoint))){
                        CreateRectangleAction createRectangleAction = new CreateRectangleAction(
                                drawingContainerView.getDrawing(),
                                checkRectangleAndOvalCoordonates(),
                                new Dimension(Math.abs(endingPoint.x - startingPoint.x), Math.abs(endingPoint.y - startingPoint.y)),
                                drawingContainerView.getCurrentColorBackground(),
                                drawingContainerView.getCurrentColorForeground()
                        );
                        drawingContainerView.getDrawing().getActionStack().push(createRectangleAction);
                    }
                    break;
                }

                case "oval": {
                    if(!(startingPoint.equals(endingPoint))){
                        CreateOvalAction createOvalAction = new CreateOvalAction(
                                drawingContainerView.getDrawing(),
                                checkRectangleAndOvalCoordonates(),
                                new Dimension(Math.abs(endingPoint.x - startingPoint.x), Math.abs(endingPoint.y - startingPoint.y)),
                                drawingContainerView.getCurrentColorBackground(),
                                drawingContainerView.getCurrentColorForeground()
                        );
                        drawingContainerView.getDrawing().getActionStack().push(createOvalAction);
                    }
                    break;
                }
            }
        }

        private void checkLinesCoordonates() {
            int width = endingPoint.x - startingPoint.x;
            int height = endingPoint.y - startingPoint.y;

            invertHeight = false;

            invertWidth = width < 0;

            if (height < 0) {
                invertHeight = true;
            }
        }

        private Point checkRectangleAndOvalCoordonates() {
            int width = endingPoint.x - startingPoint.x;
            int height = endingPoint.y - startingPoint.y;

            if (width < 0 && height < 0) {
                return endingPoint;
            } else if (width < 0) {
                return new Point(endingPoint.x,startingPoint.y);
            } else if (height < 0) {
                return new Point(startingPoint.x,endingPoint.y);
            } else {
                return startingPoint;
            }
        }
    }
}
