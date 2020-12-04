package shapescontroller;

public class FileReturn {
    private Object[][] shapes;
    private Object[] steps;

    public FileReturn(Object [][]shapes, Object[]steps) {
        this.shapes = shapes;
        this.steps = steps;

    }

    public Object[][] getShapes() {
        return shapes;
    }



    public Object[] getSteps() {
        return steps;
    }
}




