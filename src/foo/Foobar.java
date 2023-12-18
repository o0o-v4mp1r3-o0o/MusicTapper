package foo;

import java.net.URL;

public class Foobar {
    public URL initSounds() {
        return Foobar.class.getResource("/Music/osuSound.wav");
    }
}
