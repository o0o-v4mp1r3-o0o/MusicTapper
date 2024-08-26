import foo.Foobar;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class soundManager {
    public ArrayList<Clip> realclip = new ArrayList<>();
    public ArrayList<Clip> doubleClip = new ArrayList<>();
    public void playClip() {
        try
        {
                for (int i = 0; i < 15; i++) {
                    URL yourFile = new Foobar().initSounds();
                    AudioInputStream stream;
                    AudioFormat format;
                    DataLine.Info info;
                    Clip clip;
                    stream = AudioSystem.getAudioInputStream(yourFile);
                    format = stream.getFormat();
                    info = new DataLine.Info(Clip.class, format);
                    clip = (Clip) AudioSystem.getLine(info);
                    clip.open(stream);
                    realclip.add(clip);
            }
            for (int i = 0; i < 15; i++) {
                URL yourFile = new Foobar().initDSounds();
                AudioInputStream stream;
                AudioFormat format;
                DataLine.Info info;
                Clip clip;
                stream = AudioSystem.getAudioInputStream(yourFile);
                format = stream.getFormat();
                info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                FloatControl gainControl =
                        (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(6f);
                doubleClip.add(clip);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void playClipreal(long start, long stop){
        if(stop-start > 25) {
            for (int i = 0; i < realclip.size(); i++) {
                if (realclip.get(i).getFramePosition() == 0) {
                    realclip.get(i).start();
                    System.out.println("got to" + " " + i);
                    break;
                } else {
                    realclip.get(i).setFramePosition(0);
                }
            }
        }else{
            for (int i = 0; i < doubleClip.size(); i++) {
                if (doubleClip.get(i).getFramePosition() == 0) {
                    doubleClip.get(i).start();
                    System.out.println("got to" + " " + i);
                    break;
                } else {
                    doubleClip.get(i).setFramePosition(0);
                }
            }
        }
        System.out.println(start + " start"); //update pls
        System.out.println(stop + "stop");
        System.out.println(start-stop + " start minus stop");
    }

}
