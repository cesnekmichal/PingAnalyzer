package pinganalyzer;

import java.awt.EventQueue;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Michal
 */
public class SwingUtil {
    public static void loop(int milis){
        Toolkit tk = Toolkit.getDefaultToolkit();
        EventQueue eq = tk.getSystemEventQueue();
        final SecondaryLoop loop = eq.createSecondaryLoop();
        // Enter the loop to block the current event
        // handler, but leave UI responsive
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                loop.exit();
                t.cancel();
            }
        }, milis);
    }
}
