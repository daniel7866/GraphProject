import Graph.*;

import javax.swing.*;
import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GraphPanel());
        frame.setVisible(true);
    }
}
