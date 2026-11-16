package aero.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicListUI;

public class AeroListUI extends BasicListUI {
    public static ComponentUI createUI(JComponent c) { return new AeroListUI(); }
    private MouseAdapter hover;
    @Override public void paint(Graphics g, JComponent c) { Graphics2D tg = aero.AeroPaint.textG2(g); super.paint(tg, c); tg.dispose(); }
    @Override protected void installListeners() {
        super.installListeners();
        hover = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { set(e.getPoint()); }
            @Override public void mouseExited(MouseEvent e) { list.putClientProperty("Aero.rolloverRow", null); list.repaint(); }
            void set(Point p) {
                int i = list.locationToIndex(p); if (i >= 0) { Rectangle r = list.getCellBounds(i, i); if (r == null || !r.contains(p)) i = -1; }
                Object old = list.getClientProperty("Aero.rolloverRow");
                if (!(old instanceof Integer) || (Integer) old != i) { list.putClientProperty("Aero.rolloverRow", i < 0 ? null : i); list.repaint(); }
            }
        };
        list.addMouseMotionListener(hover); list.addMouseListener(hover);
    }
    @Override protected void uninstallListeners() { list.removeMouseMotionListener(hover); list.removeMouseListener(hover); super.uninstallListeners(); }
}
