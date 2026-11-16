package aero.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/** Internal frame: 1px rounded border, non-opaque (transparent corners), 4px invisible edge-resize band. */
public class AeroInternalFrameUI extends BasicInternalFrameUI {
    private EdgeResizer resizer; private ComponentListener cl;
    public AeroInternalFrameUI(JInternalFrame f) { super(f); }
    public static ComponentUI createUI(JComponent c) { return new AeroInternalFrameUI((JInternalFrame) c); }
    @Override protected JComponent createNorthPane(JInternalFrame w) { return new AeroInternalTitlePane(w, false); }
    @Override protected void installDefaults() { super.installDefaults(); frame.setOpaque(false); frame.setBackground(aero.AeroPalette.FRAME_BG); }
    @Override public void installUI(JComponent c) {
        super.installUI(c);
        JLayeredPane lp = frame.getLayeredPane(); resizer = new EdgeResizer(frame); lp.add(resizer, JLayeredPane.DRAG_LAYER);
        cl = new ComponentAdapter() { @Override public void componentResized(ComponentEvent e) { resizer.setBounds(0, 0, lp.getWidth(), lp.getHeight()); } };
        lp.addComponentListener(cl); resizer.setBounds(0, 0, lp.getWidth(), lp.getHeight());
    }
    @Override public void uninstallUI(JComponent c) {
        if (resizer != null) { frame.getLayeredPane().remove(resizer); frame.getLayeredPane().removeComponentListener(cl); }
        super.uninstallUI(c);
    }
    @Override public void update(Graphics g, JComponent c) { paint(g, c); }

    static final class EdgeResizer extends JComponent {
        static final int B = 4, C = 10, N = 1, S = 2, W = 4, E = 8;
        final JInternalFrame f; int dir; Point start; Rectangle startB;
        EdgeResizer(JInternalFrame f) {
            this.f = f; setOpaque(false);
            MouseAdapter ma = new MouseAdapter() {
                @Override public void mouseMoved(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(cursor(hit(e.getX(), e.getY())))); }
                @Override public void mousePressed(MouseEvent e) {
                    dir = hit(e.getX(), e.getY()); if (dir == 0 || f.getParent() == null) return;
                    startB = f.getBounds(); start = SwingUtilities.convertPoint(EdgeResizer.this, e.getPoint(), f.getParent());
                    DesktopManager dm = dm(); if (dm != null) dm.beginResizingFrame(f, dir);
                }
                @Override public void mouseDragged(MouseEvent e) {
                    if (dir == 0 || f.getParent() == null) return;
                    Point p = SwingUtilities.convertPoint(EdgeResizer.this, e.getPoint(), f.getParent()); int dx = p.x - start.x, dy = p.y - start.y;
                    Dimension min = new Dimension(Math.max(120, f.getMinimumSize().width), Math.max(60, f.getMinimumSize().height)); Rectangle b = new Rectangle(startB);
                    if ((dir & E) != 0) b.width = Math.max(min.width, startB.width + dx);
                    if ((dir & S) != 0) b.height = Math.max(min.height, startB.height + dy);
                    if ((dir & W) != 0) { b.width = Math.max(min.width, startB.width - dx); b.x = startB.x + startB.width - b.width; }
                    if ((dir & N) != 0) { b.height = Math.max(min.height, startB.height - dy); b.y = startB.y + startB.height - b.height; }
                    DesktopManager dm = dm(); if (dm != null) dm.resizeFrame(f, b.x, b.y, b.width, b.height); else f.setBounds(b);
                }
                @Override public void mouseReleased(MouseEvent e) { if (dir != 0) { DesktopManager dm = dm(); if (dm != null) dm.endResizingFrame(f); } dir = 0; }
            };
            addMouseListener(ma); addMouseMotionListener(ma);
        }
        DesktopManager dm() { return f.getDesktopPane() != null ? f.getDesktopPane().getDesktopManager() : null; }
        int hit(int x, int y) {
            int d = 0, w = getWidth(), h = getHeight();
            if (x < B) d |= W; if (x >= w - B) d |= E; if (y < B) d |= N; if (y >= h - B) d |= S;
            if (x < C && y < B || x < B && y < C) d = W | N; if (x >= w - C && y < B || x >= w - B && y < C) d = E | N;
            if (x < C && y >= h - B || x < B && y >= h - C) d = W | S; if (x >= w - C && y >= h - B || x >= w - B && y >= h - C) d = E | S;
            return d;
        }
        static int cursor(int d) {
            switch (d) { case N: return Cursor.N_RESIZE_CURSOR; case S: return Cursor.S_RESIZE_CURSOR; case W: return Cursor.W_RESIZE_CURSOR; case E: return Cursor.E_RESIZE_CURSOR;
                case N | W: return Cursor.NW_RESIZE_CURSOR; case N | E: return Cursor.NE_RESIZE_CURSOR; case S | W: return Cursor.SW_RESIZE_CURSOR; case S | E: return Cursor.SE_RESIZE_CURSOR; default: return Cursor.DEFAULT_CURSOR; }
        }
        @Override public boolean contains(int x, int y) { return f.isResizable() && !f.isMaximum() && !f.isIcon() && super.contains(x, y) && hit(x, y) != 0; }
    }
}
