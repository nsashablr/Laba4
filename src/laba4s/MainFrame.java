package laba4s;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainFrame extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JFileChooser fileChooser = null;

    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showAxisGridMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem turnLeftMenuItem;

    private GraphicDisplay display = new GraphicDisplay();

    private boolean fileLoaded = false;

    public MainFrame() {
        super("Построение графиков функций на основе подготовленных файлов");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        setExtendedState(MAXIMIZED_BOTH);
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };
        fileMenu.add(openGraphicsAction);
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);
        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        Action showAxisGridAction = new AbstractAction("Показывать координатную сетку") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.setShowAxisGrid(showAxisGridMenuItem.isSelected());
            }
        };
        showAxisGridMenuItem = new JCheckBoxMenuItem(showAxisGridAction);
        graphicsMenu.add(showAxisGridMenuItem);
        showAxisGridMenuItem.setSelected(true);
        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(false);
        Action turnLeftAction = new AbstractAction("Повернуть оси координат на 90 градусов") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                display.setTurn(turnLeftMenuItem.isSelected());
            }
        };
        turnLeftMenuItem = new JCheckBoxMenuItem(turnLeftAction);
        graphicsMenu.add(turnLeftMenuItem);
        turnLeftMenuItem.setSelected(false);
        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void openGraphics(File selectedFile) {
        try {
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader reader = new BufferedReader(fileReader);
            int fileSize = Integer.parseInt(reader.readLine());
            Double[][] graphicsData = new Double[fileSize][];
            for (int i = 0; i < fileSize; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");
                graphicsData[i] = new Double[] {Double.parseDouble(parts[0]), Double.parseDouble(parts[1])};
            }
            if (fileSize > 0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Указанный файл не найден", "Ошибка загрузки данных",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainFrame.this,
                    "Ошибка чтения координат точек из файла",
                    "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
        }
    }

    private class GraphicsMenuListener implements MenuListener {  //отвечает за состояния элементов меню график
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showAxisGridMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            turnLeftMenuItem.setEnabled(fileLoaded);
        }

        @Override
        public void menuDeselected(MenuEvent menuEvent) {

        }

        @Override
        public void menuCanceled(MenuEvent menuEvent) {

        }
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
