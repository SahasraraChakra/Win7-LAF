package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Menu separator: 4px margins, #d2dfec line + white line (design: my-1 mx-1). */
public class AeroPopupMenuSeparatorUI extends BasicSeparatorUI {
    private static final AeroPopupMenuSeparatorUI INSTANCE = new AeroPopupMenuSeparatorUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void paint(Graphics g, JComponent c) {
        int y = c.getHeight() / 2 - 1; g.setColor(AeroPalette.SEP); g.fillRect(4, y, c.getWidth() - 8, 1); g.setColor(Color.WHITE); g.fillRect(4, y + 1, c.getWidth() - 8, 1);
    }
    @Override public Dimension getPreferredSize(JComponent c) { return new Dimension(0, 10); }
}
