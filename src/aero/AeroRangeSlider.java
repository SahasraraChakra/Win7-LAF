package aero;

import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

/** Dual-thumb range slider (design: "Dual-Thumb Range Slider (Price Filter)"). */
public class AeroRangeSlider extends JComponent {
    private int min, max, lo, hi, drag = -1; private final EventListenerList listeners = new EventListenerList();
    public AeroRangeSlider(int min, int max, int lo, int hi) {
        this.min = min; this.max = max; this.lo = lo; this.hi = hi; setOpaque(false); setFocusable(true);
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { requestFocusInWindow(); int v = at(e.getX()); drag = Math.abs(v - AeroRangeSlider.this.lo) <= Math.abs(v - AeroRangeSlider.this.hi) ? 0 : 1; if (AeroRangeSlider.this.lo == AeroRangeSlider.this.hi) drag = v < AeroRangeSlider.this.lo ? 0 : 1; mouseDragged(e); }
            @Override public void mouseDragged(MouseEvent e) { if (drag < 0) return; int v = at(e.getX()); if (drag == 0) setLow(Math.min(v, AeroRangeSlider.this.hi)); else setHigh(Math.max(v, AeroRangeSlider.this.lo)); }
            @Override public void mouseReleased(MouseEvent e) { drag = -1; }
        };
        addMouseListener(ma); addMouseMotionListener(ma);
        addFocusListener(new FocusAdapter() { public void focusGained(FocusEvent e) { repaint(); } public void focusLost(FocusEvent e) { repaint(); } });
    }
    private int at(int x) { double f = (x - 5.0) / Math.max(1, getWidth() - 10); return Math.max(min, Math.min(max, (int) Math.round(min + f * (max - min)))); }
    public int getLow() { return lo; } public int getHigh() { return hi; }
    public void setLow(int v) { v = Math.max(min, Math.min(v, hi)); if (v != lo) { lo = v; fire(); repaint(); } }
    public void setHigh(int v) { v = Math.min(max, Math.max(v, lo)); if (v != hi) { hi = v; fire(); repaint(); } }
    public void addChangeListener(ChangeListener l) { listeners.add(ChangeListener.class, l); }
    private void fire() { ChangeEvent e = new ChangeEvent(this); for (ChangeListener l : listeners.getListeners(ChangeListener.class)) l.stateChanged(e); }
    @Override public Dimension getPreferredSize() { return new Dimension(200, 18); }
    private int px(int v) { return 5 + (int) Math.round((getWidth() - 10) * (v - min) / (double) Math.max(1, max - min)); }
    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(), cy = h / 2;
        g.setColor(AeroPalette.TRACK); g.fill(AeroPaint.rr(2, cy - 2, w - 4, 4, 2)); AeroPaint.border(g, 2, cy - 2, w - 4, 4, 2, AeroPalette.TRACK_BORDER);
        g.setPaint(new GradientPaint(px(lo), 0, AeroPalette.c(0x3b82f6), px(hi), 0, AeroPalette.c(0x38bdf8))); g.fill(AeroPaint.rr(px(lo), cy - 2, Math.max(1, px(hi) - px(lo)), 4, 2));
        for (int v : new int[]{lo, hi}) {
            int x = px(v) - 5; g.setPaint(AeroPaint.vgrad(cy - 8, 16, new Gloss(new float[]{0, .48f, .52f, 1}, AeroPalette.THUMB0, AeroPalette.THUMB1, AeroPalette.THUMB2, AeroPalette.THUMB3)));
            g.fill(AeroPaint.rr(x, cy - 8, 10, 16, 2)); AeroPaint.border(g, x, cy - 8, 10, 16, 2, AeroPalette.THUMB_BORDER);
            if (hasFocus()) { g.setColor(AeroPalette.FOCUS_GLOW); g.draw(AeroPaint.rr(x - .5f, cy - 8.5f, 11, 17, 3)); }
        }
        g.dispose();
    }
}
