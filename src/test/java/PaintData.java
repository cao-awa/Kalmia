import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.collection.timed.TimedList;

import java.util.Random;

public class PaintData {
    private static final Random RANDOM = new Random();
    private static final int WEIGHT = 500;

    public static void main(String[] args) {
        TimedList<String> strings = new TimedList<>(300);
        strings.add("awa");
        TimeUtil.coma(100);
        strings.add("awa1");
        TimeUtil.coma(100);
        strings.add("awa2");
        TimeUtil.coma(100);
        strings.add("awa3");
        TimeUtil.coma(100);
        strings.add("awa4");
        TimeUtil.coma(100);
        strings.add("awa5");
        System.out.println(strings);

        TimeUtil.coma(100);
        System.out.println(strings);
        TimeUtil.coma(200);
        System.out.println(strings);

    }
}
