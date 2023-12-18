import java.io.Serializable;
import java.util.ArrayList;

public class Songs implements Serializable {
    public String songName;
    public ArrayList<Long> savedTaps = new ArrayList<>();
}
