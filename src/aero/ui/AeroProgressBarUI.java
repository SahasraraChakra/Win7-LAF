package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * Progress bars. Style via client property "JProgressBar.style":
 * (default) glossy Win7 green | blue | warning | error | segmented (green/amber/red bands) | mini (status-bar blue 8px).
 * Indeterminate bars use the animated blue diagonal stripes of the design.
 */
public class AeroProgressBarUI extends BasicProgressBarUI {
    public static ComponentUI createUI(JComponent c) { return new AeroProgressBarUI(); }
    @Override protected void installDefaults() { super.installDefaults(); progressBar.setOpaque(false); progressBar.setBorderPainted(false); }
    @Override public Dimension getPreferredSize(JComponent c) {
        boolean hz = progressBar.getOrientation() == SwingConstants.HORIZONTAL; int th = thickness();
        Dimension d = hz ? new Dimension(146, th) : new Dimension(th, 146);
        if (progressBar.isStringPainted() && hz) d.height = Math.max(d.height, 14);
        return d;
    }
    private String style() { Object o = progressBar.getClientProperty("JProgressBar.style"); return o == null ? "" : o.toString(); }
    private int thickness() { String s = style(); if (s.equals("mini")) return 8; if (s.equals("segmented")) return 8; return progressBar.isIndeterminate() ? 10 : 12; }

    private Gloss fillGloss(String s, float frac) {
        switch (s) {
            case "blue": case "mini": return Gloss.of2(new Color(0x60a5fa), new Color(0x2563eb));
            case "warning": return Gloss.of4(new Color(0xfde68a), new Color(0xfbbf24), new Color(0xf59e0b), new Color(0xfbbf24));
            case "error": return Gloss.of4(new Color(0xfca5a5), new Color(0xf87171), new Color(0xdc2626), new Color(0xef4444));
            default: return new Gloss(new float[]{0f, .45f, .5f, .8f, 1f}, AeroPalette.PG0, AeroPalette.PG1, AeroPalette.PG2, AeroPalette.PG3, AeroPalette.PG4);
        }
    }
    private Rectangle box() {
        boolean hz = progressBar.getOrientation() == SwingConstants.HORIZONTAL; Insets in = progressBar.getInsets(); int th = thickness();
        int w = progressBar.getWidth() - in.left - in.right, h = progressBar.getHeight() - in.top - in.bottom;
        return hz ? new Rectangle(in.left, in.top + (h - th) / 2, w, th) : new Rectangle(in.left + (w - th) / 2, in.top, th, h);
    }

    @Override protected void paintDeterminate(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); Rectangle b = box(); boolean hz = progressBar.getOrientation() == SwingConstants.HORIZONTAL;
        String st = style(); boolean mini = st.equals("mini"), seg = st.equals("segmented");
        g.translate(b.x, b.y);
        int w = hz ? b.width : b.height, h = hz ? b.height : b.width;
        if (!hz) { g.translate(0, b.height); g.rotate(-Math.PI / 2); }
        g.setColor(AeroPalette.PG_TRACK); g.fill(AeroPaint.rr(0, 0, w, h, mini ? 1 : 2));
        if (!mini && !seg) { g.setColor(new Color(0, 0, 0, 30)); g.fillRect(1, 1, w - 2, 1); }
        double frac = progressBar.getPercentComplete(); int pad = seg ? 0 : 1 + (mini ? 0 : 0);
        int fw = (int) Math.round((w - 2 * pad - 2) * frac);
        if (fw > 0) {
            Shape clip = g.getClip(); g.clip(AeroPaint.rr(pad + 1, pad + 1, w - 2 * pad - 2, h - 2 * pad - 2, 1));
            if (seg) {
                int full = w - 2; double[] bounds = {0, .5, .75, 1}; Color[] cols = {new Color(0x10b981), new Color(0xfbbf24), new Color(0xf43f5e)}; Color[] bds = {null, new Color(0xf59e0b), new Color(0xe11d48)};
                for (int i = 0; i < 3; i++) { int x0 = 1 + (int) (full * bounds[i]), x1 = 1 + (int) Math.min(fw, full * bounds[i + 1]); if (x1 <= x0) continue; g.setColor(cols[i]); g.fillRect(x0, 1, x1 - x0, h - 2); if (bds[i] != null) { g.setColor(bds[i]); g.fillRect(x0, 1, 1, h - 2); } }
            } else {
                g.setPaint(AeroPaint.vgrad(pad + 1, h - 2 * pad - 2, fillGloss(st, 1f))); g.fillRect(pad + 1, pad + 1, fw, h - 2 * pad - 2);
                g.setColor(AeroPaint.alpha(Color.WHITE, mini ? .6 : .8)); g.fillRect(pad + 1, pad + 1, fw, 1);
                if (st.isEmpty()) { g.setColor(AeroPalette.PG_BORDER); g.fillRect(pad + fw, pad + 1, 1, h - 2 * pad - 2); }
            }
            g.setClip(clip);
        }
        Color bc = seg ? new Color(0xfda4af) : mini ? new Color(0x94a3b8) : AeroPalette.PG_TRACK_BORDER;
        AeroPaint.border(g, 0, 0, w, h, mini ? 1 : 2, bc);
        g.dispose();
        if (progressBar.isStringPainted() && hz) paintStr(g0, b);
    }

    @Override protected void paintIndeterminate(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); Rectangle b = box(); boolean hz = progressBar.getOrientation() == SwingConstants.HORIZONTAL;
        g.translate(b.x, b.y); int w = hz ? b.width : b.height, h = hz ? b.height : b.width;
        if (!hz) { g.translate(0, b.height); g.rotate(-Math.PI / 2); }
        g.setColor(AeroPalette.PG_TRACK); g.fill(AeroPaint.rr(0, 0, w, h, 2));
        Shape clip = g.getClip(); g.clip(AeroPaint.rr(1, 1, w - 2, h - 2, 1));
        float off = getFrameCount() > 0 ? (getAnimationIndex() % getFrameCount()) / (float) getFrameCount() * 32f : 0f;
        int ih = h - 2;
        for (float x0 = -ih - 32 + off; x0 < w + ih; x0 += 16) {
            Path2D.Float p = new Path2D.Float(); p.moveTo(x0, 1 + ih); p.lineTo(x0 + 8, 1 + ih); p.lineTo(x0 + 8 + ih, 1); p.lineTo(x0 + ih, 1); p.closePath();
            g.setColor(AeroPalette.PG_STRIPE_A); g.fill(new Rectangle2D.Float(x0 - 8, 1, 8 + ih + 8, ih)); // base under stripe pair
            g.setColor(AeroPalette.PG_STRIPE_B); g.fill(p);
        }
        g.setClip(clip); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.PG_INDET_BORDER); g.dispose();
        if (progressBar.isStringPainted() && hz) paintStr(g0, b);
    }
    private void paintStr(Graphics g0, Rectangle b) {
        Graphics2D g = AeroPaint.g2(g0); String s = progressBar.getString(); if (s == null || s.isEmpty()) { g.dispose(); return; }
        Font f = AeroFonts.bold(Math.min(10f, Math.max(8f, b.height - 3f))); g.setFont(f); FontMetrics fm = g.getFontMetrics();
        int x = b.x + (b.width - fm.stringWidth(s)) / 2, y = b.y + (b.height - fm.getHeight()) / 2 + fm.getAscent();
        g.setColor(AeroPaint.alpha(Color.WHITE, .7)); g.drawString(s, x, y + 1); g.setColor(AeroPalette.TEXT); g.drawString(s, x, y); g.dispose();
    }
}
