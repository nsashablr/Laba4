package laba4s;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;

public class GraphicDisplay extends JPanel {

    private Double[][] graphicsData;

    private boolean showAxis = true;
    private boolean showAxisGrid = true;
    private boolean showMarkers = false;
    private boolean turnAxis = false;
    private boolean isTurned = false;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke axisGridStroke;
    private BasicStroke markerStroke;
    private Font axisFont; 
    private final Double littleSteps = 10.0;
    private final int bigSteps = 10;

    public GraphicDisplay() {
        setBackground(Color.WHITE);
        // перо для графика
        graphicsStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                //BasicStroke.JOIN_ROUND, 10.0f, new float[] {3, 2}, 0.0f);
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {24, 16}, 0.0f);
        // перо для осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        // перо для координатной сетки
        axisGridStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        // перо для контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        // шрифт для подписей осей координат
        axisFont = new Font("Serif", Font.PLAIN, 16);
    }

    public void setShowAxis(boolean showAxis) {  //методы для параметров отображения графика
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    public void showGraphics(Double[][] graphicsData) { //метод показа нового графика
        isTurned = false;
        this.graphicsData = graphicsData;
        repaint();
    }

    public void setTurn(boolean turnAxis) { //методы
        this.turnAxis = turnAxis;
        repaint();
    }

    public void setShowAxisGrid(boolean showAxisGrid) {
        this.showAxisGrid = showAxisGrid;
        repaint();
    }

    protected Point2D.Double xyToPoint(double x, double y) {  //преобразование координат
        calculateMaxMin();
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected void paintGraphics(Graphics2D canvas) {   //рисуем график
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    protected void paintAxis(Graphics2D canvas) {   //рисуем оси
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        calculateMaxMin();
        if (isTurned) {
            if (minY <= 0.0 && maxY >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
                GeneralPath arrow = new GeneralPath();
                Point2D.Double lineEnd = xyToPoint(minX, 0);
                arrow.moveTo(lineEnd.getX(), lineEnd.getY());
                arrow.lineTo(arrow.getCurrentPoint().getX() + 20, arrow.getCurrentPoint().getY() - 5);
                arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
                arrow.closePath();
                canvas.draw(arrow);
                canvas.fill(arrow);
                Rectangle2D bounds = axisFont.getStringBounds("y", context);
                Point2D.Double labelPos = xyToPoint(maxX, 0);
                canvas.drawString("y", (float) (bounds.getWidth() + 10), (float) (labelPos.getY() + bounds.getY()));
            }
            if (minX <= 0.0 && maxX >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
                GeneralPath arrow = new GeneralPath();
                Point2D.Double lineEnd = xyToPoint(0, maxY);
                arrow.moveTo(lineEnd.getX(), lineEnd.getY());
                arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
                arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
                arrow.closePath();
                canvas.draw(arrow);
                canvas.fill(arrow);
                Rectangle2D bounds = axisFont.getStringBounds("x", context);
                Point2D.Double labelPos = xyToPoint(0, maxY);
                canvas.drawString("x", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));
            }
        }
        else {
            if (minX <= 0.0 && maxX >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
                GeneralPath arrow = new GeneralPath();
                Point2D.Double lineEnd = xyToPoint(0, maxY);
                arrow.moveTo(lineEnd.getX(), lineEnd.getY());
                arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
                arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
                arrow.closePath();
                canvas.draw(arrow);
                canvas.fill(arrow);
                Rectangle2D bounds = axisFont.getStringBounds("y", context);
                Point2D.Double labelPos = xyToPoint(0, maxY);
                canvas.drawString("y", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));
            }
            if (minY <= 0.0 && maxY >= 0.0) {
                canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
                GeneralPath arrow = new GeneralPath();
                Point2D.Double lineEnd = xyToPoint(maxX, 0);
                arrow.moveTo(lineEnd.getX(), lineEnd.getY());
                arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
                arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
                arrow.closePath();
                canvas.draw(arrow);
                canvas.fill(arrow);
                Rectangle2D bounds = axisFont.getStringBounds("x", context);
                Point2D.Double labelPos = xyToPoint(maxX, 0);
                canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
            }
        }
    }

    protected void paintAxisGrid(Graphics2D canvas) {   //рисуем сетку
        canvas.setStroke(axisGridStroke);
        canvas.setColor(Color.GRAY);
        canvas.setPaint(Color.GRAY);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        calculateMaxMin();
        Double doubleBigStep = Math.max(maxX - minX, maxY - minY);
        int intBigStep = Integer.parseInt(doubleBigStep.toString().split("\\.")[0]) / bigSteps;
        Double doubleLittleStep = (intBigStep / littleSteps);
        Double fromDouble = Math.abs(minY) / intBigStep;
        int fromInt = - Integer.parseInt(fromDouble.toString().split("\\.")[0]) * intBigStep;
        Double toDouble = Math.abs(maxY) / intBigStep;
        int toInt = Integer.parseInt(toDouble.toString().split("\\.")[0]) * intBigStep;
        for (int i = fromInt; i <= toInt; i += intBigStep) {
            if (i == 0) {
                continue;
            }
            Point2D.Double from = xyToPoint(minX, i);
            Point2D.Double to = xyToPoint(maxX, i);
            Line2D.Double line = new Line2D.Double(from, to);
            canvas.draw(line);
            String numberString = Integer.toString(i);
            Rectangle2D bounds = axisFont.getStringBounds(numberString, context);
            Point2D.Double labelPos = xyToPoint(0, i);
            canvas.drawString(numberString, (float) (labelPos.getX() - bounds.getX()), (float) labelPos.getY() - 10);
        }
        int count = 0;
        for (Double i = fromInt + doubleLittleStep; i < toInt; i += doubleLittleStep) {
            Point2D.Double from = xyToPoint(0, i);
            Point2D.Double to = xyToPoint(0, i);
            count++;
            if (count == 5) {
                from.setLocation(from.getX() - 10, from.getY());
                to.setLocation(to.getX() + 10, to.getY());
                count = 0;
            }
            else {
                from.setLocation(from.getX() - 5, from.getY());
                to.setLocation(to.getX() + 5, to.getY());
            }
            Line2D.Double line = new Line2D.Double(from, to);
            canvas.draw(line);
        }
        if (isTurned) {
            fromDouble = Math.abs(minX) / intBigStep;
            fromInt = -  Integer.parseInt(fromDouble.toString().split("\\.")[0]) * intBigStep;
            toDouble = Math.abs(maxX) / intBigStep;
            toInt = Integer.parseInt(toDouble.toString().split("\\.")[0]) * intBigStep;
            for (int i = fromInt; i <= toInt; i += intBigStep) {
                Point2D.Double from = xyToPoint(i, minY);
                Point2D.Double to = xyToPoint(i,maxY);
                Line2D.Double line = new Line2D.Double(from, to);
                canvas.draw(line);
                String numberString = Integer.toString(-i);
                Rectangle2D bounds = axisFont.getStringBounds(numberString, context);
                Point2D.Double labelPos = xyToPoint(i, 0);
                canvas.drawString(numberString, (float) labelPos.getX() + 10, (float) (labelPos.getY() + bounds.getY()));
            }
        } else {
            fromDouble = Math.abs(minX) / intBigStep;
            fromInt = -  Integer.parseInt(fromDouble.toString().split("\\.")[0]) * intBigStep;
            toDouble = Math.abs(maxX) / intBigStep;
            toInt = Integer.parseInt(toDouble.toString().split("\\.")[0]) * intBigStep;
            for (int i = fromInt; i <= toInt; i += intBigStep) {
                Point2D.Double from = xyToPoint(i, minY);
                Point2D.Double to = xyToPoint(i,maxY);
                Line2D.Double line = new Line2D.Double(from, to);
                canvas.draw(line);
                String numberString = Integer.toString(i);
                Rectangle2D bounds = axisFont.getStringBounds(numberString, context);
                Point2D.Double labelPos = xyToPoint(i, 0);
                canvas.drawString(numberString, (float) labelPos.getX() + 10, (float) (labelPos.getY() + bounds.getY()));
            }
        }
        count = 0;
        for (Double i = fromInt + doubleLittleStep; i < toInt; i += doubleLittleStep) {
            Point2D.Double from = xyToPoint(i, 0);
            Point2D.Double to = xyToPoint(i, 0);
            count++;
            if (count == 5) {
                from.setLocation(from.getX(), from.getY() - 10);
                to.setLocation(to.getX(), to.getY() + 10);
                count = 0;
            }
            else {
                from.setLocation(from.getX(), from.getY() - 5);
                to.setLocation(to.getX(), to.getY() + 5);
            }
            Line2D.Double line = new Line2D.Double(from, to);
            canvas.draw(line);
        }
    }

    protected void turnAxis(Graphics2D canvas) {  //поворачиваем график
        if (!isTurned) {
            isTurned = true;
            for (Double[] pair : graphicsData) {
                Double x = -pair[1];
                Double y = pair[0];
                pair[0] = x;
                pair[1] = y;
            }
        }
        else
        {
            isTurned = false;
            for (Double[] pair : graphicsData) {
                Double x = pair[1];
                Double y = - pair[0];
                pair[0] = x;
                pair[1] = y;
            }
        }
    }

    protected void paintMarkers(Graphics2D canvas) {   //рисуем маркеры
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);
        for (Double[] point : graphicsData) {
            GeneralPath arrow = new GeneralPath();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            arrow.moveTo(center.getX(), center.getY());
            // left
            arrow.lineTo(arrow.getCurrentPoint().getX() - 5, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 2);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 3);
            // right
            arrow.lineTo(arrow.getCurrentPoint().getX() + 10, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 2);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 3);
            // up
            arrow.lineTo(arrow.getCurrentPoint().getX() - 5, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 2, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 3, arrow.getCurrentPoint().getY());
            // down
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 2, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 3, arrow.getCurrentPoint().getY());
            arrow.closePath();
            //
            Double number = point[1];
            if (isTurned) number = point[0];
            int divNumber = Integer.parseInt(number.toString().split("\\.")[0]);
            boolean met = true;
            while (divNumber != 0) {
                    int numeral = divNumber % 10;
                    divNumber /= 10;
                    if (numeral % 2 != 1) {
                        met = false;
                        break;
                    }
            }
            if (met) {
                canvas.setColor(Color.BLUE);
                canvas.setPaint(Color.BLUE);
                canvas.draw(arrow);
                canvas.fill(arrow);
                canvas.setColor(Color.BLUE);
                canvas.setPaint(Color.BLUE);
                continue;
            }
            canvas.draw(arrow);
            canvas.fill(arrow);
        }
    }

    public void paintComponent(Graphics g) {   //реализация перерисовки компонента
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        calculateMaxMin();
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
        if (turnAxis) turnAxis(canvas);
        if (showAxis) paintAxis(canvas);
        if (showAxisGrid) paintAxisGrid(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    public void calculateMaxMin() {
        minX = graphicsData[0][0];
        maxX = minX;
        maxY = graphicsData[0][1];
        minY = maxY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
            if (graphicsData[i][0] < minX) {
                minX = graphicsData[i][0];
            }
            if (graphicsData[i][0] > maxX) {
                maxX = graphicsData[i][0];
            }
        }
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
    }
}
