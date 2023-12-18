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
    int c = 0;
    public void playClip(String pathName) {
        try
        {
            if(c==0) {
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
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void playClipreal(){
        for(int i = 0; i < realclip.size(); i++){
            if(realclip.get(i).getFramePosition() == 0){
                realclip.get(i).start();
                System.out.println("got to"+" "+ i);
                break;
            }else{
                realclip.get(i).setFramePosition(0);
            }
        }
    }

}
