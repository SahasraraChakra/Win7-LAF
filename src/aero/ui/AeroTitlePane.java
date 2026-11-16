package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.beans.*;
import java.util.List;
import javax.swing.*;

/**
 * The custom Aero window title bar - dimensions, gradients, text halo and caption buttons are copied from the design CSS:
 * 30px high, 8px side padding, 16px "SK" badge, bold app name + semibold subtitle with white glow,
 * minimise/maximise 28x19, close 44x21 red.
 */
public class AeroTitlePane extends JComponent {
    public static final int HEIGHT = 30;
    private final JRootPane root; private Window window;
    private final AeroCaptionButton min = new AeroCaptionButton(AeroCaptionButton.Kind.MIN, false, "Minimize"),
            max = new AeroCaptionButton(AeroCaptionButton.Kind.MAX, false, "Maximize"), close = new AeroCaptionButton(AeroCaptionButton.Kind.CLOSE, false, "Close");
    private boolean active = true; private Point dragOff; private final boolean frame;
    private final WindowAdapter wl; private final PropertyChangeListener pcl = e -> repaint();

    public AeroTitlePane(JRootPane root) {
        this.root = root; setLayout(null); setOpaque(false);
        int style = root.getWindowDecorationStyle(); frame = style == JRootPane.FRAME;
        add(close); if (frame) { add(max); add(min); }
        close.addActionListener(e -> { Window w = window(); if (w != null) w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING)); });
        min.addActionListener(e -> { if (window instanceof Frame) ((Frame) window).setExtendedState(((Frame) window).getExtendedState() | Frame.ICONIFIED); });
        max.addActionListener(e -> toggleMax());
        wl = new WindowAdapter() {
            @Override public void windowActivated(WindowEvent e) { active = true; repaint(); }
            @Override public void windowDeactivated(WindowEvent e) { active = false; repaint(); }
            @Override public void windowStateChanged(WindowEvent e) { syncMax(); }
        };
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                Window w = window(); if (w == null) return;
                if (e.isPopupTrigger()) { showSystemMenu(e); return; }
                if (SwingUtilities.isLeftMouseButton(e) && !isMaxed()) dragOff = e.getLocationOnScreen().getLocation() == null ? null : new Point(e.getXOnScreen() - w.getX(), e.getYOnScreen() - w.getY());
            }
            @Override public void mouseReleased(MouseEvent e) { dragOff = null; if (e.isPopupTrigger()) showSystemMenu(e); }
            @Override public void mouseDragged(MouseEvent e) { Window w = window(); if (w != null && dragOff != null) w.setLocation(e.getXOnScreen() - dragOff.x, e.getYOnScreen() - dragOff.y); }
            @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && frame && window instanceof Frame && ((Frame) window).isResizable()) toggleMax(); }
        };
        addMouseListener(ma); addMouseMotionListener(ma);
    }

    Window window() { if (window == null) window = SwingUtilities.getWindowAncestor(root); return window; }
    boolean isMaxed() { return window instanceof Frame && (((Frame) window).getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH; }

    @Override public void addNotify() {
        super.addNotify(); window = SwingUtilities.getWindowAncestor(root);
        if (window != null) {
            window.addWindowListener(wl); window.addWindowStateListener(wl); window.addPropertyChangeListener(pcl); active = window.isActive(); syncMax();
            if (window instanceof Frame && ((Frame) window).getMaximizedBounds() == null) ((Frame) window).setMaximizedBounds(usableBounds(window));
            boolean resizable = window instanceof Frame ? ((Frame) window).isResizable() : window instanceof Dialog && ((Dialog) window).isResizable();
            max.setEnabled(resizable); max.setVisible(frame);
        }
    }
    @Override public void removeNotify() { if (window != null) { window.removeWindowListener(wl); window.removeWindowStateListener(wl); window.removePropertyChangeListener(pcl); } super.removeNotify(); }

    static Rectangle usableBounds(Window w) {
        GraphicsConfiguration gc = w.getGraphicsConfiguration() != null ? w.getGraphicsConfiguration() : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Rectangle r = gc.getBounds(); Insets in = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        return new Rectangle(in.left, in.top, r.width - in.left - in.right, r.height - in.top - in.bottom);
    }
    private void toggleMax() {
        if (!(window instanceof Frame)) return; Frame f = (Frame) window; if (!f.isResizable()) return;
        if (isMaxed()) f.setExtendedState(Frame.NORMAL); else { f.setMaximizedBounds(usableBounds(f)); f.setExtendedState(Frame.MAXIMIZED_BOTH); }
    }
    private void syncMax() { max.setKind(isMaxed() ? AeroCaptionButton.Kind.RESTORE : AeroCaptionButton.Kind.MAX); max.setToolTipText(isMaxed() ? "Restore Down" : "Maximize"); repaint(); }
    private void showSystemMenu(MouseEvent e) {
        if (!frame) return; JPopupMenu m = new JPopupMenu();
        JMenuItem r = new JMenuItem("Restore"), mi = new JMenuItem("Minimize"), mx = new JMenuItem("Maximize"), c = new JMenuItem("Close"); c.setAccelerator(KeyStroke.getKeyStroke("alt F4")); c.putClientProperty("JMenuItem.emphasis", true);
        r.setEnabled(isMaxed()); mx.setEnabled(!isMaxed() && max.isEnabled()); r.addActionListener(a -> toggleMax()); mx.addActionListener(a -> toggleMax()); mi.addActionListener(a -> min.doClick()); c.addActionListener(a -> close.doClick());
        m.add(r); m.add(mi); m.add(mx); m.addSeparator(); m.add(c); m.show(this, e.getX(), e.getY());
    }

    @Override public Dimension getPreferredSize() { return new Dimension(100, HEIGHT); }
    @Override public Dimension getMinimumSize() { return new Dimension(80, HEIGHT); }

    // header content box is HEIGHT-1 (1px bottom border), items centered inside it
    @Override public void doLayout() {
        int w = getWidth(), ch = HEIGHT - 1; int x = w - 8;
        Dimension cd = close.getPreferredSize(); x -= cd.width; close.setBounds(x, (ch - cd.height) / 2, cd.width, cd.height);
        if (frame) {
            Dimension d = max.getPreferredSize(); x -= 6 + d.width; max.setBounds(x, (ch - d.height) / 2, d.width, d.height);
            x -= 4 + d.width; min.setBounds(x, (ch - d.height) / 2, d.width, d.height);
        }
    }
    private int buttonsLeft() { return frame ? min.getX() : close.getX(); }

    String title() { Window w = window(); if (w == null) { Object o = root.getClientProperty("Aero.title"); return o == null ? "" : o.toString(); } if (w instanceof Frame) return ((Frame) w).getTitle(); if (w instanceof Dialog) return ((Dialog) w).getTitle(); return ""; }

    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
        g.setColor(Color.WHITE); g.fillRect(0, 0, w, h);
        Gloss gl = new Gloss(new float[]{0f, .42f, .43f, 1f}, AeroPalette.TB0, AeroPalette.TB1, AeroPalette.TB2, AeroPalette.TB3);
        if (!active) { Color wh = new Color(255, 255, 255, 140); gl = new Gloss(gl.f, mix(gl.c[0], wh), mix(gl.c[1], wh), mix(gl.c[2], wh), mix(gl.c[3], wh)); }
        g.setPaint(AeroPaint.vgrad(0, h, gl)); g.fillRect(0, 0, w, h);
        g.setColor(AeroPalette.TB_INNER); g.fillRect(0, 1, w, 1);
        g.setColor(AeroPalette.TB_BORDER); g.fillRect(0, h - 1, w, 1);
        int ch = h - 1, x = 8;
        // icon (16x16): frame icon image or the "SK" badge
        Image img = null; Window win = window(); if (win != null) { List<Image> imgs = win.getIconImages(); if (imgs != null && !imgs.isEmpty()) img = imgs.get(0); }
        int iy = Math.round((ch - 16) / 2f);
        if (img != null) { g.drawImage(img, x, iy, 16, 16, null); }
        else paintBadge(g, x, iy);
        x += 16 + 6;
        String t = title(); if (t == null) t = "";
        int avail = buttonsLeft() - 8 - x;
        Color c1 = active ? AeroPalette.TEXT_STRONG : new Color(0x64748b), c2 = active ? AeroPalette.TEXT_2 : new Color(0x7b8798);
        int sep = t.indexOf(" - "); String a = sep > 0 ? t.substring(0, sep) : t, b = sep > 0 ? t.substring(sep + 1) : "";
        Font f1 = AeroFonts.bold(11f), f2 = AeroFonts.semibold(11f);
        FontMetrics m1 = g.getFontMetrics(f1), m2 = g.getFontMetrics(f2);
        int base = Math.round((ch - m1.getHeight()) / 2f + m1.getAscent());
        String ta = ellipsize(a, m1, avail);
        if (active) AeroPaint.glowText(g, ta, x, base, f1, c1, 3, .55f); else { g.setFont(f1); g.setColor(c1); g.drawString(ta, x, base); }
        if (!b.isEmpty() && ta.equals(a)) {
            int bx = x + m1.stringWidth(ta) + 6, rem = avail - (bx - x);
            if (rem > 12) { String tb = ellipsize(b, m2, rem); if (active) AeroPaint.glowText(g, tb, bx, base, f2, c2, 2, .5f); else { g.setFont(f2); g.setColor(c2); g.drawString(tb, bx, base); } }
        }
        g.dispose();
    }
    private static Color mix(Color a, Color over) { float k = over.getAlpha() / 255f; return new Color(Math.round(a.getRed() * (1 - k) + over.getRed() * k), Math.round(a.getGreen() * (1 - k) + over.getGreen() * k), Math.round(a.getBlue() * (1 - k) + over.getBlue() * k), a.getAlpha()); }
    private static String ellipsize(String s, FontMetrics fm, int w) {
        if (w <= 0) return ""; if (fm.stringWidth(s) <= w) return s;
        int n = s.length(); while (n > 0 && fm.stringWidth(s.substring(0, n) + "\u2026") > w) n--; return n <= 0 ? "" : s.substring(0, n) + "\u2026";
    }
    /** 16x16 rounded badge: gradient blue-400 to blue-700, 1px blue-900 border, "SK" 9px white. */
    static void paintBadge(Graphics2D g, int x, int y) {
        g.setColor(new Color(0, 0, 0, 40)); g.fill(AeroPaint.rr(x, y + 1, 16, 16, 4));
        g.setPaint(new GradientPaint(0, y, new Color(0x60a5fa), 0, y + 16, new Color(0x1d4ed8))); g.fill(AeroPaint.rr(x, y, 16, 16, 4));
        AeroPaint.border(g, x, y, 16, 16, 4, new Color(0x1e3a8a));
        String t = String.valueOf(UIManager.get("Aero.titleBadgeText")); if (t == null || t.equals("null")) t = "SK";
        Font f = AeroFonts.bold(9f); g.setFont(f); FontMetrics fm = g.getFontMetrics(); g.setColor(Color.WHITE);
        g.drawString(t, x + (16 - fm.stringWidth(t)) / 2f, y + (16 - fm.getHeight()) / 2f + fm.getAscent());
    }
}
