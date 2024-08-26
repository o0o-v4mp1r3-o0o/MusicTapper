import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;

import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.awt.event.KeyListener;
import java.util.Timer;

public class main implements KeyListener{
    public long startSeconds = System.currentTimeMillis();
    public static ArrayList<Long> timeLine = new ArrayList<>();
    public static HashMap<String,Songs> savedTaps = new HashMap<>();
    public static boolean keyReleaseBoolean[] = new boolean[700];
    public String webURL;
    public Songs song;
    public JFrame j = new JFrame();
    JPanel panel = new JPanel();
    MediaPlayer mediaPlayer;
    WebView web;
    public ColorFadingDemo f = new ColorFadingDemo();
    public static ArrayList<ColorFadingDemo> circleArray = new ArrayList<>();

    Timer timer = new Timer();
    boolean timerOn=false;
    boolean isPlaying = false;
    boolean playingVid = false;
    soundManager soundmanager = new soundManager();
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

            if ((keyReleaseBoolean[e.getKeyCode()] == true || e.getKeyCode() > 700) && !e.isControlDown() || !isPlaying) return;
            keyReleaseBoolean[e.getKeyCode()] = true;
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                //playEvent();
                return;
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                timer.cancel();
                return;
            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
                song.savedTaps = timeLine;
                savedTaps.put(song.songName, song);
                saveKeyPresses();
                return;
            } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_P) {
                for (long i : timeLine) {
                    System.out.println(i);
                }
            } else if (e.isControlDown()) return;
            System.out.println(e.getKeyChar() + " " + ((e.getWhen() - startSeconds)) / 1_000.0d);
            long x = (long) (((e.getWhen() - startSeconds) / 1_000.0d) * 1000d);
            System.out.println(x);

            soundmanager.playClipreal(timeLine.size()>0?timeLine.get(timeLine.size()-1):x,x);
            timeLine.add(x);
            circleArray.get(0).x = (int)(Math.random()*(j.getBounds().width-100));
            circleArray.get(0).y = (int)(Math.random()*(j.getBounds().height-100));
            circleArray.get(0).start();
    }

    @Override
    public void keyReleased(KeyEvent e) {
       if(isPlaying) keyReleaseBoolean[e.getKeyCode()] = false;
    }

    public void saveKeyPresses(){
        try {
            FileOutputStream fileOut = new FileOutputStream("test.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.savedTaps);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/test.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void playEvent(){
        timerOn=true;
        timer = new Timer();
        for(int i = 0; i < timeLine.size(); i++){
            int finalI = i;
            TimerTask task1 = new TimerTask() {
                int x = finalI;
                @Override
                public void run() {
                    System.out.println("hi"+finalI);
                    if(finalI % 2==0) {
                        j.getContentPane().setBackground(new Color(1500));
                    }else{
                        j.getContentPane().setBackground(new Color(600));
                    }
                }
            };
            timer.schedule(task1,timeLine.get(i));
        }
    }

    public void loadSaves(JMenu loadSongsHere){
        try {
            System.out.println(new File(".").getAbsolutePath());
            FileInputStream fis = new FileInputStream("test.ser");
            ObjectInputStream iis = new ObjectInputStream(fis);
            savedTaps = (HashMap<String,Songs>) iis.readObject();
            for(Map.Entry<String,Songs> ss : savedTaps.entrySet()){
                JMenuItem addthis = new JMenuItem(ss.getKey());
                loadSongsHere.add(addthis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                main main = new main();
                JMenuBar menuBar = new JMenuBar();
                JMenu menu = new JMenu("Options");
                JMenu savedSongs = new JMenu("Saved Songs");
                main.loadSaves(savedSongs);
                JButton Start = new JButton("Start");
                Start.setFocusable(false);
                Start.addActionListener((e) -> {
                    if(!main.isPlaying) {
                        main.startSeconds = System.currentTimeMillis();
                        Start.setText("Stop");
                        main.isPlaying=true;
                    }else{
                        Start.setText("Start");
                        main.isPlaying=false;
                    }
                });
                JMenuItem New = new JMenuItem("New");
                New.addActionListener((e) -> {
                    main.song = new Songs();
                    main.song.songName = JOptionPane.showInputDialog("Song Name");
                });
                JMenuItem save = new JMenuItem("Save Current Timeline (CTRL+S)");
                save.addActionListener((e) -> {
                    main.song.savedTaps = timeLine;
                    savedTaps.put(main.song.songName,main.song);
                    JMenuItem addthis = new JMenuItem(main.song.songName);
                    savedSongs.add(addthis);
                    main.saveKeyPresses();
                });
                JMenuItem delete = new JMenuItem("Delete A Song");
                delete.addActionListener((e) -> {
                    Object[] options =
                            savedTaps.entrySet().stream().map(Map.Entry::getKey).toArray();
                    Object selectionObject = JOptionPane.showInputDialog(null, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE,
                            null, options, options[0]);
                    String selectionString = selectionObject.toString();
                    savedTaps.remove(selectionString);
                    for (int i = 0 ; i <  savedSongs.getItemCount(); i++) {
                        JMenuItem item = savedSongs.getItem(i);
                        if (item.getText().equals(selectionString)) {
                            savedSongs.remove(item);
                            break;
                        }
                    }
                    main.saveKeyPresses();
                });
                JMenuItem clearAll = new JMenuItem("Clear All Saved Timelines");
                clearAll.addActionListener((e) -> {
                    savedTaps.clear();
                    main.saveKeyPresses();
                });
                JMenuItem clearCurrent = new JMenuItem("Clear Current Timeline");
                clearCurrent.addActionListener((e) -> {
                    timeLine.clear();
                });

                JSlider slider = new JSlider(-20,6);
                slider.setFocusable(false);
                slider.addChangeListener((e)->{
                    int d = slider.getValue();
                    for(int i = 0; i < main.soundmanager.realclip.size(); i++){
                        FloatControl gainControl =
                                (FloatControl)main.soundmanager.realclip.get(i).getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(d);
                    }
                    for(int i = 0; i < main.soundmanager.doubleClip.size(); i++){
                        FloatControl gainControl =
                                (FloatControl)main.soundmanager.doubleClip.get(i).getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(d);
                    }

                });

                menu.add(New);
                menu.add(save);
                menu.add(delete);
                menu.add(clearAll);
                menu.add(clearCurrent);
                menuBar.add(menu);
                menuBar.add(savedSongs);
                menuBar.add(Start);
                menuBar.add(slider);

                circleArray.add(main.f);
                for(int i = 0; i < 15; i++){
                    circleArray.add(new ColorFadingDemo());
                    circleArray.get(i).setBounds(0,0,55,55);
                    //main.j.add(circleArray.get(i));
                }
                circleArray.add(new ColorFadingDemo());
                //circleArray.get(0).setBounds(0,0,55,55);
               // circleArray.get(1).setBounds(150,150,55,55);
                //main.j.add(circleArray.get(1));
                //circleArray.get(0).setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
                main.j.add(circleArray.get(0));
                //main.panel.setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
                //main.j.add(main.panel);
                main.j.setJMenuBar(menuBar);
                main.j.setFocusTraversalKeysEnabled(true);
                //main.j.setUndecorated(true);

                JFXPanel mainJFXPanel = new JFXPanel();

                mainJFXPanel.setFocusable(false);

                JMenu Video = new JMenu("Video");
                JMenuItem video = new JMenuItem("Show Video");
                video.setFocusable(false);
                video.addActionListener((e) -> {
                    if(main.webURL==null){
                        JOptionPane.showMessageDialog(null,"No Video To Show!");
                    }else {
                        if (!main.playingVid) {
                            video.setText("Hide Video");
                            main.playingVid = true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    circleArray.get(0).setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                                    main.web = new WebView();
                                    main.web.getEngine().load(main.webURL);
                                    VBox box = new VBox(main.web);
                                    Scene scene = new Scene(box, 960, 600);
                                    main.j.getContentPane().add(mainJFXPanel);
                                    mainJFXPanel.setScene(scene);
                                    main.j.repaint();
                                    main.j.validate();
                                }
                            });
                        } else {
                            video.setText("Show Video");
                            main.playingVid = false;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    circleArray.get(0).setBackground(circleArray.get(7).getBackground());
                                    main.web.getEngine().load(null);
                                    main.j.repaint();
                                    main.j.validate();
                                }
                            });
                        }
                    }
                });
                JMenuItem loadVideo = new JMenuItem("Load Youtube Video");
                loadVideo.setFocusable(false);
                loadVideo.addActionListener((e) -> {
                    String webpage = JOptionPane.showInputDialog("Enter Youtube URL here");
                    webpage = webpage.replace("watch?v=","embed/");
                    main.webURL = webpage;
                    video.setText("Hide Video");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(main.web!=null) main.web.getEngine().load(null);
                            circleArray.get(0).setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
                            main.web = new WebView();
                            main.web.getEngine().load(main.webURL);
                            VBox box = new VBox(main.web);
                            Scene scene = new Scene(box, 960, 600);
                            main.j.getContentPane().add(mainJFXPanel);
                            mainJFXPanel.setScene(scene);
                            main.j.repaint();
                            main.j.validate();
                        }
                    });
                    JOptionPane.showMessageDialog(null,"Video Loaded!"+" "+main.webURL);
                });
                Video.add(video);
                Video.add(loadVideo);
                menuBar.add(Video);
                main.j.addKeyListener(main);
                main.j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                main.j.setPreferredSize(new Dimension(960, 600));
                main.j.setLocationRelativeTo(null);
                main.j.pack();
                main.j.setVisible(true);

                main.soundmanager.playClip();
//                String musicFile = "src/MP3s/AfterRain.mp3";     // For example
//
//                Media sound = new Media(new File(musicFile).toURI().toString());
//                main.mediaPlayer = new MediaPlayer(sound);
//                main.mediaPlayer.setAutoPlay(true);
            }
        });

    }
}
