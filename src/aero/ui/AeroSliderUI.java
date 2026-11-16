package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicSliderUI;

/** Slider: 4px track, 9x15 glossy thumb, major/minor ticks with mono labels (design "Rich Sliders Matrix"). */
public class AeroSliderUI extends BasicSliderUI {
    public AeroSliderUI(JSlider s) { super(s); }
    public static ComponentUI createUI(JComponent c) { return new AeroSliderUI((JSlider) c); }
    @Override protected void installDefaults(JSlider s) { super.installDefaults(s); s.setOpaque(false); s.setFocusable(true); }
    @Override protected Dimension getThumbSize() { return slider.getOrientation() == JSlider.HORIZONTAL ? new Dimension(9, 15) : new Dimension(15, 9); }
    @Override public void paintFocus(Graphics g) { }
    @Override public void paint(Graphics g, JComponent c) { Graphics2D tg = AeroPaint.textG2(g); super.paint(tg, c); tg.dispose(); }

    @Override public void paintTrack(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); Rectangle t = trackRect; boolean h = slider.getOrientation() == JSlider.HORIZONTAL;
        float th = 4; Rectangle2D.Float r = h ? new Rectangle2D.Float(t.x, t.y + (t.height - th) / 2f, t.width, th) : new Rectangle2D.Float(t.x + (t.width - th) / 2f, t.y, th, t.height);
        Object tc = slider.getClientProperty("JSlider.trackColor");
        g.setColor(tc instanceof Color ? (Color) tc : AeroPalette.TRACK); g.fill(AeroPaint.rr(r.x, r.y, r.width, r.height, 2));
        if (!(tc instanceof Color)) { g.setColor(new Color(0, 0, 0, 38)); if (h) g.fillRect((int) r.x + 1, (int) r.y + 1, (int) r.width - 2, 1); }
        AeroPaint.border(g, r.x, r.y, r.width, r.height, 2, tc instanceof Color ? AeroPalette.darker((Color) tc, .82f) : AeroPalette.TRACK_BORDER);
        if (Boolean.TRUE.equals(slider.getClientProperty("JSlider.trackFill")) && slider.isEnabled()) {
            g.setPaint(new GradientPaint(r.x, 0, AeroPalette.c(0x3b82f6), r.x + r.width, 0, AeroPalette.c(0x38bdf8)));
            if (h) g.fill(AeroPaint.rr(r.x + 1, r.y + 1, thumbRect.x + thumbRect.width / 2f - r.x - 1, r.height - 2, 1));
            else g.fill(AeroPaint.rr(r.x + 1, thumbRect.y + thumbRect.height / 2f, r.width - 2, r.y + r.height - thumbRect.y - thumbRect.height / 2f - 1, 1));
        }
        g.dispose();
    }
    @Override public void paintThumb(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); Rectangle r = thumbRect; boolean en = slider.isEnabled();
        g.setColor(new Color(0, 0, 0, 38)); g.fill(AeroPaint.rr(r.x, r.y + 1, r.width, r.height, 2));
        Gloss gl = new Gloss(new float[]{0, .48f, .52f, 1}, AeroPalette.THUMB0, AeroPalette.THUMB1, AeroPalette.THUMB2, AeroPalette.THUMB3);
        if (!en) gl = Gloss.of4(Color.WHITE, new Color(0xeeeeee), new Color(0xdddddd), new Color(0xe6e6e6));
        if (isDragging()) gl = new Gloss(gl.f, AeroPalette.THUMB1, AeroPalette.THUMB2, AeroPalette.darker(AeroPalette.THUMB2, .92f), AeroPalette.THUMB3);
        g.setPaint(AeroPaint.vgrad(r.y, r.height, gl)); g.fill(AeroPaint.rr(r.x, r.y, r.width, r.height, 2));
        g.setColor(AeroPaint.alpha(Color.WHITE, .9)); g.fillRect(r.x + 1, r.y + 1, r.width - 2, 1);
        AeroPaint.border(g, r.x, r.y, r.width, r.height, 2, en ? AeroPalette.THUMB_BORDER : new Color(0xadb2b5));
        if (slider.hasFocus() && en) { g.setColor(AeroPalette.FOCUS_GLOW); g.setStroke(new BasicStroke(1.5f)); g.draw(AeroPaint.rr(r.x - .75f, r.y - .75f, r.width + 1.5f, r.height + 1.5f, 3)); }
        g.dispose();
    }
    @Override protected void paintMajorTickForHorizSlider(Graphics g, Rectangle r, int x) { g.setColor(AeroPalette.TEXT_FAINT); g.drawLine(x, 0, x, 6); }
    @Override protected void paintMinorTickForHorizSlider(Graphics g, Rectangle r, int x) { g.setColor(new Color(0xcbd5e1)); g.drawLine(x, 0, x, 3); }
    @Override protected void paintMajorTickForVertSlider(Graphics g, Rectangle r, int y) { g.setColor(AeroPalette.TEXT_FAINT); g.drawLine(0, y, 6, y); }
    @Override protected void paintMinorTickForVertSlider(Graphics g, Rectangle r, int y) { g.setColor(new Color(0xcbd5e1)); g.drawLine(0, y, 3, y); }
    @Override protected void paintHorizontalLabel(Graphics g, int value, Component label) { style(label, value); super.paintHorizontalLabel(g, value, label); }
    @Override protected void paintVerticalLabel(Graphics g, int value, Component label) { style(label, value); super.paintVerticalLabel(g, value, label); }
    private void style(Component label, int value) {
        if (label instanceof JLabel) { JLabel l = (JLabel) label; boolean cur = value == slider.getValue();
            l.setFont(cur ? AeroFonts.monoBold(9f) : AeroFonts.mono(9f)); l.setForeground(!slider.isEnabled() ? AeroPalette.TEXT_FAINT : cur ? AeroPalette.c(0x1e40af) : AeroPalette.TEXT_FAINT); }
    }
}
