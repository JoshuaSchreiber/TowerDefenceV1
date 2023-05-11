package de.Joshua.Utility.WindowsAndPanels;

import de.Joshua.GameObjects.Tower;
import de.Joshua.Utility.Coordinate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;

import static java.awt.event.KeyEvent.VK_F11;


public class GameWindow extends JPanel {
    JMenuBar mainMenuBar;
    public JMenuItem points;
    public static double height;
    public static double width;
    public Timer timer;
    public double tick;
    public Tower mainPlayerTower;
    public Coordinate mainPlayerTowerCoordinate;
    public int backgroundHeight;
    public int backgroundWidth;
    public String movingDirekton = "+";

    private final Image buntBig = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Bunt-Big.png")));
    private final Image buntSmall = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Bunt-Small.png")));
    private final Image stoneBig = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Stone-Big.jpg")));
    private final Image stoneSmall = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("Stone-Small.png")));
    String whichImage;
    private final JFrame mainFrame;

    public GameWindow(JFrame frame, int height, int width) throws IOException {
        this.mainFrame = frame;
        this.height = height;
        this.width = width;
        gameWindowInitialisation();
        gamePanelInitialisation();
    }

    public void gameWindowInitialisation(){
        mainMenuBar = new JMenuBar();

        addF11KeyListener();
        registerWindowListener();

        createMenuBar();
    }

    public void gamePanelInitialisation(){
        initGame();
        mainFrame.add(this);
        backgroundHeight = 128;
        backgroundWidth = 128;
        whichImage = "Stone-Small";
        repaint();
    }
    public void createMenuBar(){
        JMenu settings = new JMenu("Game Menu");
        addPointsItem();

        mainFrame.setJMenuBar(mainMenuBar);
        addSettingsItems(settings);
        mainMenuBar.add(settings);
        mainMenuBar.add(points);
    }

    public void addPointsItem(){
        points = new JMenuItem("Points: " + String.valueOf(tick));
        mainMenuBar.add(points);
    }

    public void addSettingsItems(JMenu settings){
        JMenuItem closeWindowItem = new JMenuItem("Close Game");
        settings.add(closeWindowItem);
        closeWindowItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu fullGameWindow = new JMenu("Full Game Window");
        JMenu infoItem = new JMenu("Important: To get the Menu back, press F11!!!");
        JMenuItem doItItem = new JMenuItem("  X");
        doItItem.addActionListener(e -> {
            mainMenuBar.setVisible(false);
            mainFrame.dispose();
            mainFrame.setUndecorated(true);
            mainFrame.setVisible(true);

        });
        fullGameWindow.add(infoItem);
        infoItem.add(doItItem);
        settings.add(fullGameWindow);

        JMenu background = new JMenu("Background");
        settings.add(background);
        JMenuItem stoneBig = new JMenuItem("Stone-Big");
        background.add(stoneBig);
        JMenuItem stoneSmall = new JMenuItem("Stone-Small");
        background.add(stoneSmall);
        JMenuItem buntBig = new JMenuItem("Bunt-Big");
        background.add(buntBig);
        JMenuItem buntSmall = new JMenuItem("Bunt-Small");
        background.add(buntSmall);
        stoneSmall.addActionListener(event -> {
            backgroundHeight = 128;
            backgroundWidth = 128;
            whichImage = "Stone-Small";
            repaint();
        });
        stoneBig.addActionListener(event -> {
            backgroundHeight = 590;
            backgroundWidth = 590;
            whichImage = "Stone-Big";
            repaint();
        });
        buntBig.addActionListener(event -> {
            backgroundHeight = 417;
            backgroundWidth = 626;
            whichImage = "Bunt-Big";
            repaint();
        });
        buntSmall.addActionListener(event -> {
            backgroundHeight = 166;
            backgroundWidth = 257;
            whichImage = "Bunt-Small";
            repaint();
        });

        JMenuItem startItem = new JMenuItem("Continue Game");
        JMenuItem stopItem = new JMenuItem("Stop Game");
        startItem.setVisible(false);
        settings.add(startItem);
        startItem.addActionListener(e -> {
            continueGame();
            startItem.setVisible(false);
            stopItem.setVisible(true);
        });
        settings.add(stopItem);
        stopItem.addActionListener(e -> {
            stopGame();
            stopItem.setVisible(false);
            startItem.setVisible(true);
        });
    }
    public void addF11KeyListener(){
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == VK_F11) {
                    if (mainMenuBar.isVisible()) {
                        mainMenuBar.setVisible(false);
                        mainFrame.dispose();
                        mainFrame.setUndecorated(true);
                        mainFrame.setVisible(true);
                    } else {
                        mainFrame.dispose();
                        mainFrame.setUndecorated(false);
                        mainFrame.setVisible(true);
                        mainMenuBar.setVisible(true);
                    }
                }
                if(e.getKeyCode() == 32){
                    if(movingDirekton.equals("+")){
                        movingDirekton = "-";
                    }else{
                        movingDirekton = "+";
                    }
                }

            }
        });
    }

    private void registerWindowListener() {
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
                stopGame();
            }
            @Override
            public void windowActivated(WindowEvent e) {
                continueGame();
            }
        });
    }
    private void initGame() {
        createGameGameObjects();
        tick = 0;
        timer = new Timer(5, e -> doOnTick());
        timer.start();
    }

    public void doOnTick() {
        tick++;
        remove(points);
        points.setText("Points: " + String.valueOf(tick));
        double movingSpeed = 0.05;
        mainPlayerTower.setDeltaMovingAngle(movingDirekton, movingSpeed);;
        System.out.println(movingSpeed);
        repaint();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void continueTimer() {
        timer.start();
    }

    public void stopGame() {
        stopTimer();
    }

    public void continueGame() {
        continueTimer();
    }

    public void createGameGameObjects() {
        mainPlayerTowerCoordinate = new Coordinate(GameWindow.width/2-50, GameWindow.height/2-50);
        mainPlayerTower = new Tower(mainPlayerTowerCoordinate, 100, 100);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int x = 0; x < GameWindow.width; x+=this.backgroundWidth) {
            for (int y = 0; y < GameWindow.height; y+=this.backgroundHeight) {
                switch (whichImage) {
                    case "Bunt-Big" -> g2d.drawImage(buntBig, x, y, null);
                    case "Bunt-Small" -> g2d.drawImage(buntSmall, x, y, null);
                    case "Stone-Big" -> g2d.drawImage(stoneBig, x, y, null);
                    case "Stone-Small" -> g2d.drawImage(stoneSmall, x, y, null);
                }
            }
        }
        mainPlayerTower.paintMe(g);
    }

}
