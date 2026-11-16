package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** JSeparator / JToolBar.Separator / JPopupMenu.Separator: 1px light-blue line with a white highlight below. */
public class AeroSeparatorUI extends BasicSeparatorUI {
    private static final AeroSeparatorUI INSTANCE = new AeroSeparatorUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void paint(Graphics g, JComponent c) {
        Dimension s = c.getSize(); JSeparator sep = (JSeparator) c;
        if (sep.getOrientation() == SwingConstants.VERTICAL) { int x = (s.width - 2) / 2; g.setColor(AeroPalette.SEP); g.fillRect(x, 0, 1, s.height); g.setColor(Color.WHITE); g.fillRect(x + 1, 0, 1, s.height); }
        else { int y = (s.height - 2) / 2; g.setColor(AeroPalette.SEP); g.fillRect(0, y, s.width, 1); g.setColor(Color.WHITE); g.fillRect(0, y + 1, s.width, 1); }
    }
    @Override public Dimension getPreferredSize(JComponent c) { return ((JSeparator) c).getOrientation() == SwingConstants.VERTICAL ? new Dimension(2, 0) : new Dimension(0, 2); }
}
