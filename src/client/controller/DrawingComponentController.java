package client.controller;

import client.model.DrawingComponentListener;
import client.view.DrawingComponentView;

abstract public class DrawingComponentController implements DrawingComponentListener {
    private DrawingComponentView drawingComponentView;

    public DrawingComponentController(DrawingComponentView drawingComponentView) {
        this.drawingComponentView = drawingComponentView;
        drawingComponentView.getDrawingComponent().addDrawingComponentListener(this);
    }

    public void updateView() {
        drawingComponentView.update();
    }

    @Override
    public void onMoved() {
        drawingComponentView.updateLocation();
    }
}
