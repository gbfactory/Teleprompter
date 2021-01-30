import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    Frame() {
        super("Teleprompter");
        setLayout(new BorderLayout());
        add(new Teleprompter(), BorderLayout.CENTER);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("teleprompter.png")));
        setSize(1080, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
