package client.view.drawingComponents;

import client.model.drawingComponents.DrawingComponent;
import client.model.drawingComponents.LineComponent;
import client.model.drawingComponents.OvalComponent;
import client.model.drawingComponents.RectangleComponent;

import javax.swing.*;
import java.awt.*;

abstract public class DrawingComponentView extends JPanel {
    private DrawingComponent drawingComponent;
    private Point anchorPoint;

    public DrawingComponentView(DrawingComponent drawingComponent) {
        this.drawingComponent = drawingComponent;
        setLayout(null);
    }

    public void update() {
        updateSize();
    }

    private void updateSize() {
        Point position = drawingComponent.getPosition();
        Dimension size = drawingComponent.getSize();

        setBounds(position.x, position.y, size.width, size.height);
    }

    public void updateLocation() {
        if (drawingComponent.getPosition() == null)
            return;

        if (drawingComponent instanceof LineComponent) {
            setLocation( (((LineComponent) drawingComponent).getFirstPoint().x),
                        (((LineComponent) drawingComponent).getFirstPoint().y));
        } else if (drawingComponent instanceof RectangleComponent) {
            setLocation(((RectangleComponent) drawingComponent).getBoundingBox().getLocation());
        } else if (drawingComponent instanceof OvalComponent) {
            setLocation(((OvalComponent) drawingComponent).getBoundingBox().getLocation());
        }
    }

    public DrawingComponent getDrawingComponent() {
        return drawingComponent;
    }


    public Point getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(Point anchorPoint) {
        this.anchorPoint = anchorPoint;
    }
}
