package foo;

import java.net.URL;

public class Foobar {
    public URL initSounds() {
        return Foobar.class.getResource("/Music/hitNormal.wav");
    }
    public URL initDSounds() {
        return Foobar.class.getResource("/Music/osuSound.wav");
    }
}
