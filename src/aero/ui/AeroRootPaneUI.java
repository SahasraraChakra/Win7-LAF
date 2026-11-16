package aero.ui;

import aero.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicRootPaneUI;

/** Root pane that draws the Aero window frame: custom title bar, 1px glass border, rounded window shape, 5px resize band. */
public class AeroRootPaneUI extends BasicRootPaneUI {
    private static final AeroRootPaneUI INSTANCE = new AeroRootPaneUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    private static final String TP = "Aero.titlePane", RS = "Aero.resizer", LM = "Aero.oldLayout", BD = "Aero.oldBorder", HL = "Aero.hierarchy";

    @Override public void installUI(JComponent c) {
        super.installUI(c); JRootPane r = (JRootPane) c; if (r.getWindowDecorationStyle() != JRootPane.NONE) install(r);
    }
    @Override public void uninstallUI(JComponent c) { uninstall((JRootPane) c); super.uninstallUI(c); }
    @Override public void update(Graphics g, JComponent c) {
        if (Boolean.TRUE.equals(c.getClientProperty("Aero.translucent"))) {
            Graphics2D g2 = AeroPaint.g2(g); Window w = SwingUtilities.getWindowAncestor(c);
            boolean max = w instanceof Frame && (((Frame) w).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
            g2.setColor(Color.WHITE); g2.fill(AeroPaint.rr(0, 0, c.getWidth(), c.getHeight(), max ? 0 : 3)); g2.dispose(); paint(g, c);
        } else super.update(g, c);
    }
    @Override public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);
        if ("windowDecorationStyle".equals(e.getPropertyName())) { JRootPane r = (JRootPane) e.getSource(); uninstall(r); if (r.getWindowDecorationStyle() != JRootPane.NONE) install(r); r.revalidate(); r.repaint(); }
    }

    private void install(JRootPane r) {
        if (r.getClientProperty(TP) != null) return;
        r.putClientProperty(BD, r.getBorder()); r.setBorder(new AeroBorders.FrameBorder() {
            @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                Graphics2D g2 = AeroPaint.g2(g); Window win = SwingUtilities.getWindowAncestor(c);
                boolean max = win instanceof Frame && (((Frame) win).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
                AeroPaint.border(g2, x, y, w, h, max ? 0 : 3, AeroPalette.WIN_BORDER); g2.dispose();
            } });
        AeroTitlePane tp = new AeroTitlePane(r); r.putClientProperty(TP, tp); r.getLayeredPane().add(tp, JLayeredPane.FRAME_CONTENT_LAYER);
        Resizer rs = new Resizer(r); r.putClientProperty(RS, rs); r.getLayeredPane().add(rs, JLayeredPane.DRAG_LAYER);
        r.putClientProperty(LM, r.getLayout()); r.setLayout(new RootLayout(tp, rs));
        r.setBackground(Color.WHITE); r.setOpaque(true);
        r.putClientProperty("Aero.translucent", enableTranslucency(r));
        HierarchyListener hl = e -> { if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0 || (e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) shape(r); };
        r.putClientProperty(HL, hl); r.addHierarchyListener(hl);
    }
    /** Makes the (not yet displayable, undecorated) window per-pixel translucent so the rounded corners are truly transparent. */
    private boolean enableTranslucency(JRootPane r) {
        try {
            Window w = SwingUtilities.getWindowAncestor(r);
            if (w == null || w.isDisplayable() || !AeroLookAndFeel.roundedWindows) return false;
            if (!GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) return false;
            w.setBackground(new Color(0, 0, 0, 0)); r.setOpaque(false); return true;
        } catch (Throwable t) { return false; }
    }
    private void uninstall(JRootPane r) {
        Object tp = r.getClientProperty(TP); if (tp == null) return;
        r.getLayeredPane().remove((Component) tp); r.getLayeredPane().remove((Component) r.getClientProperty(RS));
        r.setLayout((LayoutManager) r.getClientProperty(LM)); r.setBorder((Border) r.getClientProperty(BD));
        r.removeHierarchyListener((HierarchyListener) r.getClientProperty(HL));
        r.putClientProperty(TP, null); r.putClientProperty(RS, null); r.putClientProperty(HL, null);
    }
    private void shape(JRootPane r) {
        Window w = SwingUtilities.getWindowAncestor(r); if (w == null || r.getClientProperty("Aero.shaped") != null) return;
        if (w instanceof RootPaneContainer) ((JComponent) ((RootPaneContainer) w).getContentPane()).setOpaque(true);
        w.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { apply(w); } @Override public void componentShown(ComponentEvent e) { apply(w); } });
        r.putClientProperty("Aero.shaped", true);
        w.addWindowStateListener(e -> apply(w));
        apply(w);
    }
    private static void apply(Window w) {
        try {
            if (w instanceof RootPaneContainer && Boolean.TRUE.equals(((RootPaneContainer) w).getRootPane().getClientProperty("Aero.translucent"))) { w.repaint(); return; }
            boolean max = w instanceof Frame && (((Frame) w).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
            if (!AeroLookAndFeel.roundedWindows || max || w.getWidth() < 20) { if (w.getShape() != null) w.setShape(null); return; }
            if (!GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT)) return;
            w.setShape(new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), 6, 6));
        } catch (Throwable t) { /* unsupported */ }
    }

    /** Title pane at the top, menu bar under it, content below (like MetalRootLayout). */
    private static final class RootLayout implements LayoutManager2 {
        final AeroTitlePane tp; final Resizer rs; RootLayout(AeroTitlePane t, Resizer r) { tp = t; rs = r; }
        public void addLayoutComponent(String n, Component c) {} public void addLayoutComponent(Component c, Object o) {} public void removeLayoutComponent(Component c) {}
        public float getLayoutAlignmentX(Container t) { return 0; } public float getLayoutAlignmentY(Container t) { return 0; } public void invalidateLayout(Container t) {}
        public Dimension preferredLayoutSize(Container p) { return size(p, true); } public Dimension minimumLayoutSize(Container p) { return size(p, false); } public Dimension maximumLayoutSize(Container p) { return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE); }
        Dimension size(Container p, boolean pref) {
            JRootPane r = (JRootPane) p; Insets i = p.getInsets();
            Dimension cd = r.getContentPane() == null ? new Dimension(0, 0) : pref ? r.getContentPane().getPreferredSize() : r.getContentPane().getMinimumSize();
            Dimension md = r.getJMenuBar() == null ? new Dimension(0, 0) : pref ? r.getJMenuBar().getPreferredSize() : r.getJMenuBar().getMinimumSize();
            Dimension td = tp.getPreferredSize();
            return new Dimension(Math.max(Math.max(cd.width, md.width), td.width) + i.left + i.right, cd.height + md.height + td.height + i.top + i.bottom);
        }
        public void layoutContainer(Container p) {
            JRootPane r = (JRootPane) p; Insets i = p.getInsets(); int w = p.getWidth() - i.left - i.right, h = p.getHeight() - i.top - i.bottom;
            if (r.getLayeredPane() != null) r.getLayeredPane().setBounds(i.left, i.top, w, h);
            if (r.getGlassPane() != null) r.getGlassPane().setBounds(i.left, i.top, w, h);
            int y = 0; Dimension td = tp.getPreferredSize(); tp.setBounds(0, 0, w, td.height); y += td.height;
            if (r.getJMenuBar() != null && r.getJMenuBar().isVisible()) { Dimension md = r.getJMenuBar().getPreferredSize(); r.getJMenuBar().setBounds(0, y, w, md.height); y += md.height; }
            if (r.getContentPane() != null) r.getContentPane().setBounds(0, y, w, h < y ? 0 : h - y);
            rs.setBounds(-i.left, -i.top, w + i.left + i.right, h + i.top + i.bottom);
        }
    }

    /** Invisible 5px band around the window that shows resize cursors and resizes the window. */
    private static final class Resizer extends JComponent {
        static final int B = 5, C = 12; final JRootPane root; int dir = 0; Rectangle startB; Point startP;
        static final int N = 1, S = 2, W = 4, E = 8;
        Resizer(JRootPane r) {
            root = r; setOpaque(false);
            MouseAdapter ma = new MouseAdapter() {
                @Override public void mouseMoved(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(cursorFor(hit(e.getX(), e.getY())))); }
                @Override public void mousePressed(MouseEvent e) { Window w = SwingUtilities.getWindowAncestor(root); if (w == null) return; dir = hit(e.getX(), e.getY()); startB = w.getBounds(); startP = e.getLocationOnScreen(); }
                @Override public void mouseReleased(MouseEvent e) { dir = 0; }
                @Override public void mouseDragged(MouseEvent e) {
                    Window w = SwingUtilities.getWindowAncestor(root); if (w == null || dir == 0) return;
                    int dx = e.getXOnScreen() - startP.x, dy = e.getYOnScreen() - startP.y; Rectangle b = new Rectangle(startB);
                    Dimension min = new Dimension(Math.max(160, w.getMinimumSize().width), Math.max(90, w.getMinimumSize().height));
                    if ((dir & E) != 0) b.width = Math.max(min.width, startB.width + dx);
                    if ((dir & S) != 0) b.height = Math.max(min.height, startB.height + dy);
                    if ((dir & W) != 0) { b.width = Math.max(min.width, startB.width - dx); b.x = startB.x + startB.width - b.width; }
                    if ((dir & N) != 0) { b.height = Math.max(min.height, startB.height - dy); b.y = startB.y + startB.height - b.height; }
                    w.setBounds(b); w.validate();
                }
            };
            addMouseListener(ma); addMouseMotionListener(ma);
        }
        boolean allowed() { Window w = SwingUtilities.getWindowAncestor(root); if (w instanceof Frame) { Frame f = (Frame) w; return f.isResizable() && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0; } return w instanceof Dialog && ((Dialog) w).isResizable(); }
        int hit(int x, int y) {
            int d = 0, w = getWidth(), h = getHeight();
            if (x < B) d |= W; if (x >= w - B) d |= E; if (y < B) d |= N; if (y >= h - B) d |= S;
            if (x < C && y < B || x < B && y < C) d = W | N; if (x >= w - C && y < B || x >= w - B && y < C) d = E | N;
            if (x < C && y >= h - B || x < B && y >= h - C) d = W | S; if (x >= w - C && y >= h - B || x >= w - B && y >= h - C) d = E | S;
            return d;
        }
        static int cursorFor(int d) {
            switch (d) { case N: return Cursor.N_RESIZE_CURSOR; case S: return Cursor.S_RESIZE_CURSOR; case W: return Cursor.W_RESIZE_CURSOR; case E: return Cursor.E_RESIZE_CURSOR;
                case N | W: return Cursor.NW_RESIZE_CURSOR; case N | E: return Cursor.NE_RESIZE_CURSOR; case S | W: return Cursor.SW_RESIZE_CURSOR; case S | E: return Cursor.SE_RESIZE_CURSOR; default: return Cursor.DEFAULT_CURSOR; }
        }
        @Override public boolean contains(int x, int y) { return allowed() && super.contains(x, y) && hit(x, y) != 0; }
        @Override protected void paintComponent(Graphics g0) {
            if (!Boolean.TRUE.equals(root.getClientProperty("Aero.translucent"))) return;
            Window win = SwingUtilities.getWindowAncestor(root);
            if (win instanceof Frame && (((Frame) win).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) return;
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(); float r = 3f;
            java.awt.geom.Area corners = new java.awt.geom.Area(new java.awt.geom.Rectangle2D.Float(0, 0, w, h)); corners.subtract(new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, r * 2, r * 2)));
            g.setComposite(AlphaComposite.Clear); g.fill(corners); g.dispose();
        }
    }
}
