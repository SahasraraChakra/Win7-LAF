package aero.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicMenuUI;

public class AeroMenuUI extends BasicMenuUI {
    public static ComponentUI createUI(JComponent c) { return new AeroMenuUI(); }
    @Override protected void installDefaults() { super.installDefaults(); menuItem.setOpaque(false); menuItem.setRolloverEnabled(true); }
    @Override public void update(Graphics g, JComponent c) { paint(g, c); }
    @Override protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int gap) { AeroMenuPainter.paint(g, (JMenuItem) c); }
    @Override protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int gap) { return AeroMenuPainter.size((JMenuItem) c); }
}
