package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Toolbar separator: the "|" divider (slate-400) of the design, drawn as a 1px line. */
public class AeroToolBarSeparatorUI extends BasicToolBarSeparatorUI {
    private static final AeroToolBarSeparatorUI INSTANCE = new AeroToolBarSeparatorUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void paint(Graphics g, JComponent c) {
        JToolBar.Separator s = (JToolBar.Separator) c; boolean horiz = s.getOrientation() == SwingConstants.VERTICAL;
        Dimension d = c.getSize();
        if (horiz) { int x = d.width / 2; g.setColor(AeroPalette.TEXT_FAINT); g.fillRect(x, 4, 1, d.height - 8); g.setColor(AeroPaint.alpha(Color.WHITE, .8)); g.fillRect(x + 1, 4, 1, d.height - 8); }
        else { int y = d.height / 2; g.setColor(AeroPalette.TEXT_FAINT); g.fillRect(4, y, d.width - 8, 1); g.setColor(AeroPaint.alpha(Color.WHITE, .8)); g.fillRect(4, y + 1, d.width - 8, 1); }
    }
    @Override public Dimension getPreferredSize(JComponent c) { JToolBar.Separator s = (JToolBar.Separator) c; return s.getOrientation() == SwingConstants.VERTICAL ? new Dimension(9, 20) : new Dimension(20, 9); }
}
