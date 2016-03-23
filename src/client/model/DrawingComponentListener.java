package client.model;

public interface DrawingComponentListener {
    void onMoved();
    void onRemoved();
    void onColorChanged();
}