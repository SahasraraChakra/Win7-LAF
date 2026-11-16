package aero;

import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

/** Borders used by the Aero look and feel. */
public final class AeroBorders {
    private AeroBorders() {}

    /** Button border: insets come from the button margin plus the 1px outline (painted by the UI). */
    public static class ButtonBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) {
            Insets m = c instanceof AbstractButton && ((AbstractButton) c).getMargin() != null ? ((AbstractButton) c).getMargin() : new Insets(2, 10, 2, 10);
            i.set(m.top + 1, m.left + 1, m.bottom + 1, m.right + 1); return i;
        }
    }

    /** Rounded 1px input border with focus glow, validation outline and read-only variants. */
    public static class InputBorder extends AbstractBorder implements UIResource {
        private final Insets margin;
        public InputBorder(int t, int l, int b, int r) { margin = new Insets(t, l, b, r); }
        @Override public Insets getBorderInsets(Component c, Insets i) {
            Insets m = margin;
            if (c instanceof JTextComponent && ((JTextComponent) c).getMargin() != null) { Insets cm = ((JTextComponent) c).getMargin(); if (!(((JTextComponent) c).getMargin() instanceof UIResource)) m = cm; }
            int lw = c instanceof JComponent ? AeroTextAccessories.leadingWidth((JComponent) c) : 0, tw = c instanceof JComponent ? AeroTextAccessories.trailingWidth((JComponent) c) : 0;
            i.set(m.top + 1, m.left + 1 + lw, m.bottom + 1, m.right + 1 + tw); return i;
        }
        @Override public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
            Graphics2D g = AeroPaint.g2(g0);
            boolean focus = c.hasFocus() || (c instanceof JComponent && Boolean.TRUE.equals(((JComponent) c).getClientProperty("Aero.focusWithin")));
            Color col = inputBorderColor(c, focus);
            if (c.isEnabled() && focus) {
                Color glow = glowColor(c);
                g.setColor(AeroPaint.alpha(glow, .20)); g.setStroke(new BasicStroke(2f));
                g.draw(AeroPaint.rr(x + 1.5f, y + 1.5f, w - 3, h - 3, 1.5f));
                g.setColor(AeroPaint.alpha(glow, .12)); g.draw(AeroPaint.rr(x + 2.5f, y + 2.5f, w - 5, h - 5, 1));
            }
            AeroPaint.border(g, x, y, w, h, 2, col); g.dispose();
        }
    }

    static Color glowColor(Component c) {
        String o = c instanceof JComponent ? String.valueOf(((JComponent) c).getClientProperty("JComponent.outline")) : "";
        if ("error".equals(o)) return AeroPalette.ERR_FIELD_BORDER; if ("warning".equals(o)) return AeroPalette.WARN_FIELD_BORDER; if ("success".equals(o)) return AeroPalette.OK_FIELD_BORDER;
        return AeroPalette.FOCUS;
    }
    static Color inputBorderColor(Component c, boolean focus) {
        String o = c instanceof JComponent ? String.valueOf(((JComponent) c).getClientProperty("JComponent.outline")) : "";
        String st = c instanceof JComponent ? String.valueOf(((JComponent) c).getClientProperty("JTextField.style")) : "";
        if (!c.isEnabled()) return new Color(0xd5dbe3);
        if ("error".equals(o)) return AeroPalette.ERR_FIELD_BORDER; if ("warning".equals(o)) return AeroPalette.WARN_FIELD_BORDER; if ("success".equals(o)) return AeroPalette.OK_FIELD_BORDER;
        if (focus) return AeroPalette.FOCUS;
        if ("info".equals(st)) return AeroPalette.INFO_FIELD_BORDER;
        if ("highlight".equals(st) || "warning".equals(st)) return AeroPalette.WARN_FIELD_BORDER;
        return AeroPalette.INPUT_BORDER;
    }

    /** Plain rounded line border. */
    public static class RoundLine extends AbstractBorder implements UIResource {
        private final Color color; private final float radius; private final Insets ins;
        public RoundLine(Color c, float r, int t, int l, int b, int rr) { color = c; radius = r; ins = new Insets(t, l, b, rr); }
        public RoundLine(Color c, float r, int pad) { this(c, r, pad, pad, pad, pad); }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(ins.top, ins.left, ins.bottom, ins.right); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) { Graphics2D g2 = AeroPaint.g2(g); AeroPaint.border(g2, x, y, w, h, radius, color); g2.dispose(); }
    }

    /** Menu popup: gradient, 1px #769ec2 outline, inner white ring, 4px padding. */
    public static class PopupBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(5, 5, 5, 5); return i; }
        @Override public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
            Graphics2D g = AeroPaint.g2(g0);
            AeroPaint.border(g, x, y, w, h, 3, AeroPalette.POP_BORDER);
            g.setColor(AeroPaint.alpha(Color.WHITE, .8)); g.draw(AeroPaint.rr(x + 1.5f, y + 1.5f, w - 3, h - 3, 2)); g.dispose();
        }
    }

    public static class TooltipBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(3, 7, 3, 7); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) { Graphics2D g2 = AeroPaint.g2(g); AeroPaint.border(g2, x, y, w, h, 2, AeroPalette.TIP_BORDER); g2.dispose(); }
    }

    /** Fieldset-style titled border (rounded 3px #b8c9d9, bold legend sitting on the line). */
    public static class GroupBorder extends TitledBorder implements UIResource {
        public GroupBorder(String title) { super(new RoundLine(AeroPalette.GROUP_BORDER, 3, 1), title); setTitleFont(AeroFonts.bold(10.5f)); setTitleColor(AeroPalette.GROUP_TITLE); }
        @Override public Insets getBorderInsets(Component c, Insets i) {
            Insets r = super.getBorderInsets(c, i); r.left += 4; r.right += 4; r.bottom += 3; return r;
        }
        @Override public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
            Graphics2D g = AeroPaint.g2(g0);
            Font f = getTitleFont() != null ? getTitleFont() : AeroFonts.bold(10.5f);
            FontMetrics fm = g.getFontMetrics(f);
            String t = getTitle();
            int top = t == null || t.isEmpty() ? 0 : fm.getHeight() / 2;
            // body
            g.setColor(Color.WHITE); g.fill(AeroPaint.rr(x, y + top, w, h - top, 3));
            AeroPaint.border(g, x, y + top, w, h - top, 3, AeroPalette.GROUP_BORDER);
            if (t != null && !t.isEmpty()) {
                int tw = fm.stringWidth(t) + 12;
                g.setColor(c.getBackground() != null && c.isOpaque() ? c.getBackground() : Color.WHITE);
                g.fillRect(x + 8, y + top, tw, 1 + 1);
                g.setColor(Color.WHITE); g.fillRect(x + 8, y + top - 1, tw, 3);
                g.setFont(f); g.setColor(AeroPalette.GROUP_TITLE); g.drawString(t, x + 14, y + fm.getAscent() - 1);
            }
            g.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return getBorderInsets(c, new Insets(0, 0, 0, 0)); }
    }
    /** Convenience: a titled group border. */
    public static Border group(String title) { return new GroupBorder(title); }
    public static Border inputBorder() { return new InputBorder(3, 5, 3, 5); }

    /** Scroll pane border - 1px line. */
    public static class LineBorder1 extends AbstractBorder implements UIResource {
        private final Color color; public LineBorder1(Color c) { color = c; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(1, 1, 1, 1); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) { g.setColor(color); g.drawRect(x, y, w - 1, h - 1); }
    }

    /** Single-side line border (menubar, toolbars, statusbar). */
    public static class SideLine extends AbstractBorder implements UIResource {
        private final Color color; private final boolean top; private final int pad;
        public SideLine(Color c, boolean top, int padH) { color = c; this.top = top; pad = padH; }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(top ? 1 : 0, pad, top ? 0 : 1, pad); return i; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) { g.setColor(color); g.fillRect(x, top ? y : y + h - 1, w, 1); }
    }

    /** Window frame border (1px translucent blue; painted by the root pane UI). */
    public static class FrameBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(1, 1, 1, 1); return i; }
    }

    /** Internal frame border: a single 1px #234c6e rounded outline (no margin, no bevel). Edge resizing is handled by the UI. */
    public static class InternalFrameBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(1, 1, 1, 1); return i; }
        @Override public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
            Graphics2D g = AeroPaint.g2(g0);
            boolean max = c instanceof JInternalFrame && ((JInternalFrame) c).isMaximum();
            AeroPaint.border(g, x, y, w, h, max ? 0 : 3, AeroPalette.FRAME_BORDER);
            g.dispose();
        }
    }
}
