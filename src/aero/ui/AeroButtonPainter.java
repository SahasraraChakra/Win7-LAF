package aero.ui;

import aero.*;
import aero.AeroButtonStyle.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/** Shared painter for JButton / JToggleButton (all styles, hover fade, segments). */
final class AeroButtonPainter {
    private AeroButtonPainter() {}

    static AeroButtonStyle styleOf(AbstractButton b) {
        Object o = b.getClientProperty(AeroButtons.STYLE);
        if (o instanceof AeroButtonStyle) return (AeroButtonStyle) o;
        if (o != null) return AeroButtonStyle.get(o.toString());
        return AeroButtonStyle.STANDARD;
    }

    static State state(AbstractButton b) {
        ButtonModel m = b.getModel();
        if (!b.isEnabled()) return State.DISABLED;
        if (m.isPressed() && m.isArmed()) return State.PRESSED;
        if (m.isSelected() && b instanceof JToggleButton) return State.SELECTED;
        if (m.isRollover()) return State.HOVER;
        return State.NORMAL;
    }

    // ---- hover fade (transition: all .12s ease) ----
    static final class Fader implements ActionListener {
        final AbstractButton b; final Timer t = new Timer(15, this); float v; boolean on;
        Fader(AbstractButton b) { this.b = b; }
        void set(boolean hot) { on = hot; if (!t.isRunning()) t.start(); }
        public void actionPerformed(ActionEvent e) {
            float step = 15f / 120f; v = on ? Math.min(1, v + step) : Math.max(0, v - step);
            b.repaint(); if (v == 0 || v == 1) t.stop();
        }
    }
    static void installFader(AbstractButton b) {
        Fader f = new Fader(b); b.putClientProperty("Aero.fader", f);
        b.getModel().addChangeListener(e -> { Object o = b.getClientProperty("Aero.fader"); if (o instanceof Fader) { boolean hot = b.getModel().isRollover() && b.isEnabled(); if (hot != ((Fader) o).on) ((Fader) o).set(hot); } });
    }
    static float fade(AbstractButton b) { Object o = b.getClientProperty("Aero.fader"); return o instanceof Fader ? ((Fader) o).v : (b.getModel().isRollover() ? 1f : 0f); }

    static Font font(AbstractButton b, Spec spec) {
        Font f = b.getFont();
        if (f == null || f instanceof UIResource) return AeroFonts.weight(f != null ? f : AeroFonts.medium(11f), spec.weight);
        return f;
    }

    static Gloss mix(Gloss a, Gloss b, float t) {
        if (a == null || b == null) return t > .5f ? b : a;
        if (a.c.length != b.c.length) return t > .5f ? b : a;
        Color[] c = new Color[a.c.length];
        for (int i = 0; i < c.length; i++) c[i] = AeroPalette.mix(a.c[i], b.c[i], t);
        return new Gloss(a.f, c);
    }
    static Color mix(Color a, Color b, float t) { if (a == null) return b == null ? null : AeroPaint.alpha(b, t * b.getAlpha() / 255.0); if (b == null) return AeroPaint.alpha(a, (1 - t) * a.getAlpha() / 255.0); return AeroPalette.mix(a, b, t); }

    /** Shape for a segment (only outer corners rounded). */
    static Shape shape(float x, float y, float w, float h, float r, String seg) {
        float tl = r, tr = r, br = r, bl = r;
        if ("first".equals(seg)) { tr = br = 0; } else if ("middle".equals(seg)) { tl = tr = br = bl = 0; } else if ("last".equals(seg)) { tl = bl = 0; }
        Path2D.Float p = new Path2D.Float();
        p.moveTo(x + tl, y); p.lineTo(x + w - tr, y); if (tr > 0) p.quadTo(x + w, y, x + w, y + tr); else p.lineTo(x + w, y);
        p.lineTo(x + w, y + h - br); if (br > 0) p.quadTo(x + w, y + h, x + w - br, y + h); else p.lineTo(x + w, y + h);
        p.lineTo(x + bl, y + h); if (bl > 0) p.quadTo(x, y + h, x, y + h - bl); else p.lineTo(x, y + h);
        p.lineTo(x, y + tl); if (tl > 0) p.quadTo(x, y, x + tl, y); else p.lineTo(x, y);
        p.closePath(); return p;
    }

    static void paintBorder(Graphics2D g, int w, int h, float r, Color c, String seg) {
        g.setColor(c); g.setStroke(new BasicStroke(1f));
        if (seg == null || "only".equals(seg)) { AeroPaint.border(g, 0, 0, w, h, r, c); return; }
        Shape s = shape(.5f, .5f, w - 1, h - 1, Math.max(0, r - .5f), seg);
        if ("first".equals(seg)) g.draw(s);
        else { // no left edge for middle/last
            Shape clip = g.getClip(); g.clipRect(1, 0, w, h); g.draw(s); g.setClip(clip);
        }
    }

    static void paint(Graphics g0, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g = AeroPaint.g2(g0);
        AeroButtonStyle style = styleOf(b); State st = state(b);
        float t = fade(b); String seg = String.valueOf(b.getClientProperty(AeroButtons.SEGMENT));
        if ("null".equals(seg)) seg = null;
        Spec spec;
        Spec normal = style.spec(State.NORMAL), hot = style.spec(State.HOVER);
        boolean flatLike = normal.gloss == null && normal.border == null;
        if (st == State.NORMAL || st == State.HOVER) {
            if (st == State.HOVER && t == 0) t = 1;
            spec = hot; // paint states below
        } else spec = style.spec(st);
        int w = b.getWidth(), h = b.getHeight();
        float alpha = 1f;
        Spec draw;
        if (st == State.NORMAL || st == State.HOVER) {
            if (flatLike) { draw = hot; alpha = t; }
            else { draw = new Spec(); draw.gloss = mix(normal.gloss, hot.gloss, t); draw.border = mix(normal.border, hot.border, t); draw.fg = mix(normal.fg, hot.fg, t); draw.inset = mix(normal.inset, hot.inset, t); draw.radius = normal.radius; draw.weight = normal.weight; draw.textShadow = normal.textShadow; draw.underline = hot.underline && t > .5f; }
            if (flatLike) { draw = new Spec(); draw.gloss = hot.gloss; draw.border = hot.border; draw.inset = hot.inset; draw.radius = hot.radius; draw.fg = mix(normal.fg, hot.fg, t); draw.weight = normal.weight; draw.underline = hot.underline && t > .5f; }
        } else draw = spec;
        boolean fill = b.isContentAreaFilled() && !"true".equals(String.valueOf(b.getClientProperty("JButton.borderless")));
        if (fill && (draw.gloss != null || draw.border != null)) {
            Composite old = g.getComposite();
            if (alpha < 1f) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            if (draw.gloss != null) { g.setPaint(AeroPaint.vgrad(0, h, draw.gloss)); g.fill(seg == null || "only".equals(seg) ? AeroPaint.rr(0, 0, w, h, draw.radius) : shape(0, 0, w, h, draw.radius, seg)); }
            if (draw.inset != null) AeroPaint.insetTop(g, 0, 0, w, draw.inset, draw.radius);
            if (draw.border != null) paintBorder(g, w, h, draw.radius, draw.border, seg);
            if (b instanceof JButton && ((JButton) b).isDefaultButton() && st != State.DISABLED) { g.setColor(AeroPaint.alpha(AeroPalette.DEF_GLOW, .85)); g.draw(AeroPaint.rr(1.5f, 1.5f, w - 3, h - 3, Math.max(0, draw.radius - 1))); }
            g.setComposite(old);
        }
        if (b.isFocusPainted() && b.hasFocus() && st != State.DISABLED && fill) {
            g.setColor(AeroPaint.alpha(AeroPalette.DEF_GLOW, .9)); g.setStroke(new BasicStroke(1f)); g.draw(AeroPaint.rr(1.5f, 1.5f, w - 3, h - 3, Math.max(0, draw.radius - 1)));
        }
        // content
        Font f = font(b, draw); g.setFont(f); FontMetrics fm = g.getFontMetrics(f);
        Insets in = b.getInsets(); Rectangle view = new Rectangle(in.left, in.top, w - in.left - in.right, h - in.top - in.bottom);
        Rectangle iconR = new Rectangle(), textR = new Rectangle();
        Icon icon = st == State.DISABLED && b.getDisabledIcon() != null ? b.getDisabledIcon() : (b.getModel().isPressed() && b.getPressedIcon() != null ? b.getPressedIcon() : (b.getModel().isRollover() && b.getRolloverIcon() != null ? b.getRolloverIcon() : (b.getModel().isSelected() && b.getSelectedIcon() != null ? b.getSelectedIcon() : b.getIcon())));
        String text = b.getText();
        String clipped = SwingUtilities.layoutCompoundLabel(c, fm, text, icon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), view, iconR, textR, text == null ? 0 : b.getIconTextGap());
        int dy = st == State.PRESSED ? 0 : 0;
        if (icon != null) icon.paintIcon(c, g, iconR.x, iconR.y + dy);
        if (clipped != null && !clipped.isEmpty()) {
            Color fg = st == State.DISABLED ? draw.fg : (b.getForeground() != null && !(b.getForeground() instanceof UIResource) ? b.getForeground() : draw.fg);
            int ty = textR.y + fm.getAscent();
            if (draw.textShadow != null && st != State.DISABLED) { g.setColor(draw.textShadow); BasicGraphicsUtils.drawStringUnderlineCharAt(g, clipped, b.getDisplayedMnemonicIndex(), textR.x, ty - 1); }
            g.setColor(fg);
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, clipped, b.getDisplayedMnemonicIndex(), textR.x, ty);
            if (draw.underline) g.fillRect(textR.x, ty + 1, fm.stringWidth(clipped), 1);
        }
        g.dispose();
    }

    static Dimension preferred(JComponent c, int minH) {
        AbstractButton b = (AbstractButton) c;
        Spec sp = styleOf(b).spec(State.NORMAL);
        Font f = font(b, sp); FontMetrics fm = AeroPaint.fm(f);
        Icon icon = b.getIcon(); String text = b.getText();
        Insets in = b.getInsets();
        int tw = text == null || text.isEmpty() ? 0 : (int) Math.ceil(fm.stringWidth(text)), th = text == null || text.isEmpty() ? 0 : fm.getHeight();
        int iw = icon == null ? 0 : icon.getIconWidth(), ih = icon == null ? 0 : icon.getIconHeight();
        int gap = icon != null && tw > 0 ? b.getIconTextGap() : 0;
        int cw, ch;
        int htp = b.getHorizontalTextPosition();
        if (htp == SwingConstants.CENTER && b.getVerticalTextPosition() != SwingConstants.CENTER) { cw = Math.max(tw, iw); ch = th + ih + gap; }
        else { cw = tw + iw + gap; ch = Math.max(th, ih); }
        int w = cw + in.left + in.right, h = ch + in.top + in.bottom;
        if (!"true".equals(String.valueOf(b.getClientProperty("JButton.noMinHeight")))) h = Math.max(h, minH);
        return new Dimension(w, h);
    }
}
