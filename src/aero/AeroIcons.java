package aero;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.plaf.UIResource;

/** Vector icons: the Lucide icons used in the design (parsed from SVG path data), arrows, status badges, tree/file icons. */
public final class AeroIcons {
    private AeroIcons() {}

    private static Map<String, String> DATA;
    private static final Map<String, Path2D.Double> CACHE = new HashMap<>();

    private static synchronized Path2D.Double path(String name) {
        Path2D.Double p = CACHE.get(name);
        if (p != null) return p;
        if (DATA == null) {
            DATA = new HashMap<>();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(AeroIcons.class.getResourceAsStream("/aero/res/lucide.txt"), "UTF-8"))) {
                String l; while ((l = r.readLine()) != null) { int i = l.indexOf('='); if (i > 0) DATA.put(l.substring(0, i), l.substring(i + 1)); }
            } catch (Exception e) { throw new IllegalStateException(e); }
        }
        String d = DATA.get(name);
        if (d == null) throw new IllegalArgumentException("Unknown icon: " + name);
        p = SvgPath.parse(d); CACHE.put(name, p); return p;
    }
    public static boolean has(String name) { path("check"); return DATA.containsKey(name); }

    /** A Lucide icon (24x24 viewBox scaled to size; stroke width 2 like the design). */
    public static LucideIcon lucide(String name, int size, Color color) { return new LucideIcon(name, size, color, 2f); }
    public static LucideIcon lucide(String name, int size, Color color, float strokeWidth) { return new LucideIcon(name, size, color, strokeWidth); }

    public static final class LucideIcon implements Icon, UIResource {
        private final String name; private final int size; private Color color; private final float sw; private Color fill; private Color disabled;
        LucideIcon(String n, int s, Color c, float sw) { name = n; size = s; color = c; this.sw = sw; }
        public LucideIcon filled(Color f) { fill = f; return this; }
        public LucideIcon color(Color c) { color = c; return this; }
        public LucideIcon withDisabled(Color c) { disabled = c; return this; }
        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
        @Override public void paintIcon(java.awt.Component c, Graphics g0, int x, int y) {
            Graphics2D g = AeroPaint.g2(g0); g.translate(x, y); double sc = size / 24.0; g.scale(sc, sc);
            boolean en = c == null || c.isEnabled();
            if (fill != null) { g.setColor(en ? fill : AeroPalette.mix(fill, Color.WHITE, .6f)); g.fill(path(name)); }
            g.setColor(en ? color : (disabled != null ? disabled : AeroPalette.mix(color, Color.WHITE, .55f)));
            g.setStroke(new BasicStroke(sw, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(path(name)); g.dispose();
        }
    }

    // ------------------------------------------------------------------ triangles & chevrons
    public static Icon triangle(int dir, int w, int h, Color c) { return new Tri(dir, w, h, c); }
    private static final class Tri implements Icon, UIResource {
        final int dir, w, h; final Color c;
        Tri(int d, int w, int h, Color c) { dir = d; this.w = w; this.h = h; this.c = c; }
        public int getIconWidth() { return w; } public int getIconHeight() { return h; }
        public void paintIcon(Component comp, Graphics g0, int x, int y) {
            Graphics2D g = AeroPaint.g2(g0); Path2D.Float p = new Path2D.Float();
            switch (dir) {
                case SwingConstants.NORTH: p.moveTo(x, y + h); p.lineTo(x + w, y + h); p.lineTo(x + w / 2f, y); break;
                case SwingConstants.EAST: p.moveTo(x, y); p.lineTo(x, y + h); p.lineTo(x + w, y + h / 2f); break;
                case SwingConstants.WEST: p.moveTo(x + w, y); p.lineTo(x + w, y + h); p.lineTo(x, y + h / 2f); break;
                default: p.moveTo(x, y); p.lineTo(x + w, y); p.lineTo(x + w / 2f, y + h);
            }
            p.closePath();
            g.setColor(comp != null && !comp.isEnabled() ? AeroPalette.mix(c, Color.WHITE, .5f) : c); g.fill(p); g.dispose();
        }
    }

    // ------------------------------------------------------------------ check / radio glyphs for menus
    public static void paintCheckBox(Graphics2D g, int x, int y, int size, boolean selected, boolean indeterminate, boolean enabled, boolean hot, boolean pressed) {
        Color fill = selected || indeterminate ? AeroPalette.CHECK_FILL : Color.WHITE;
        Color bd = selected || indeterminate ? AeroPalette.darker(AeroPalette.CHECK_FILL, .85f) : (hot ? AeroPalette.BTN_H_BORDER : AeroPalette.CHECK_BORDER);
        if (!enabled) { fill = selected ? new Color(0xb4c0d0) : new Color(0xf1f5f9); bd = new Color(0xcbd5e1); }
        else if (pressed) fill = selected ? AeroPalette.darker(fill, .9f) : AeroPalette.BTN_P0;
        else if (hot && !selected) fill = AeroPalette.BTN_H0;
        g.setColor(fill); g.fill(AeroPaint.rr(x, y, size, size, 2));
        AeroPaint.border(g, x, y, size, size, 2, bd);
        if (selected) {
            g.setColor(Color.WHITE); g.setStroke(new BasicStroke(size / 7f + .5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D.Float p = new Path2D.Float(); p.moveTo(x + size * .24f, y + size * .53f); p.lineTo(x + size * .43f, y + size * .72f); p.lineTo(x + size * .77f, y + size * .30f); g.draw(p);
        } else if (indeterminate) {
            g.setColor(Color.WHITE); g.fill(new Rectangle2D.Float(x + size * .25f, y + size * .43f, size * .5f, size * .14f));
        }
    }
    public static void paintRadio(Graphics2D g, int x, int y, int size, boolean selected, boolean enabled, boolean hot, boolean pressed) {
        Color fill = selected ? AeroPalette.CHECK_FILL : Color.WHITE;
        Color bd = selected ? AeroPalette.darker(AeroPalette.CHECK_FILL, .85f) : (hot ? AeroPalette.BTN_H_BORDER : AeroPalette.CHECK_BORDER);
        if (!enabled) { fill = selected ? new Color(0xb4c0d0) : new Color(0xf1f5f9); bd = new Color(0xcbd5e1); }
        else if (pressed) fill = selected ? AeroPalette.darker(fill, .9f) : AeroPalette.BTN_P0;
        else if (hot && !selected) fill = AeroPalette.BTN_H0;
        g.setColor(fill); g.fill(new Ellipse2D.Float(x, y, size, size));
        g.setColor(bd); g.setStroke(new BasicStroke(1f)); g.draw(new Ellipse2D.Float(x + .5f, y + .5f, size - 1, size - 1));
        if (selected) { g.setColor(Color.WHITE); float d = size * .38f; g.fill(new Ellipse2D.Float(x + (size - d) / 2, y + (size - d) / 2, d, d)); }
    }
    public static Icon checkBoxIcon() { return new StateIcon(false); }
    public static Icon radioIcon() { return new StateIcon(true); }
    private static final class StateIcon implements Icon, UIResource {
        final boolean radio; StateIcon(boolean r) { radio = r; }
        public int getIconWidth() { return 14; } public int getIconHeight() { return 14; }
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            Graphics2D g = AeroPaint.g2(g0); javax.swing.AbstractButton b = (javax.swing.AbstractButton) c; javax.swing.ButtonModel m = b.getModel();
            boolean hot = m.isRollover() || m.isArmed();
            if (radio) paintRadio(g, x, y, 14, m.isSelected(), c.isEnabled(), hot, m.isPressed() && m.isArmed());
            else paintCheckBox(g, x, y, 14, m.isSelected(), Boolean.TRUE.equals(b.getClientProperty("JCheckBox.indeterminate")), c.isEnabled(), hot, m.isPressed() && m.isArmed());
            if (c.hasFocus() && b.isFocusPainted()) {
                g.setColor(AeroPalette.FOCUS_GLOW); g.setStroke(new BasicStroke(1.4f));
                if (radio) g.draw(new Ellipse2D.Float(x - .7f, y - .7f, 15.4f, 15.4f)); else g.draw(AeroPaint.rr(x - .7f, y - .7f, 15.4f, 15.4f, 3));
            }
            g.dispose();
        }
    }

    // ------------------------------------------------------------------ tree / file icons
    public static Icon folder(boolean open) { return lucide(open ? "folder-open" : "folder", 14, new Color(0xd97706), 1.7f).filled(new Color(0xfde68a)); }
    public static Icon document() { return lucide("file", 14, AeroPalette.c(0x2563eb), 1.7f).filled(Color.WHITE); }
    public static Icon javaFile() { return lucide("file", 14, AeroPalette.c(0x1d4ed8), 1.7f).filled(AeroPalette.c(0xdbeafe)); }
    public static Icon expandIcon(boolean expanded) { return expanded ? new Tri(SwingConstants.SOUTH, 7, 5, new Color(0x64748b)) : new Tri(SwingConstants.EAST, 5, 7, new Color(0x94a3b8)); }

    // ------------------------------------------------------------------ status badges for JOptionPane
    public static Icon status(int messageType, int size) { return new Badge(messageType, size); }
    private static final class Badge implements Icon, UIResource {
        final int type, size; Badge(int t, int s) { type = t; size = s; }
        public int getIconWidth() { return size; } public int getIconHeight() { return size; }
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            Graphics2D g = AeroPaint.g2(g0); g.translate(x, y);
            Color a, b, d; String glyph;
            switch (type) {
                case javax.swing.JOptionPane.ERROR_MESSAGE: a = new Color(0xf87171); b = new Color(0xdc2626); d = new Color(0x991b1b); glyph = "x"; break;
                case javax.swing.JOptionPane.WARNING_MESSAGE: a = new Color(0xfcd34d); b = new Color(0xf59e0b); d = new Color(0xb45309); glyph = "!"; break;
                case javax.swing.JOptionPane.QUESTION_MESSAGE: a = AeroPalette.c(0x7dd3fc); b = AeroPalette.c(0x2563eb); d = AeroPalette.c(0x1e3a8a); glyph = "?"; break;
                default: a = AeroPalette.c(0x7dd3fc); b = AeroPalette.c(0x2563eb); d = AeroPalette.c(0x1e3a8a); glyph = "i";
            }
            Shape s = type == javax.swing.JOptionPane.WARNING_MESSAGE ? tri(size) : new Ellipse2D.Float(1, 1, size - 2, size - 2);
            g.setPaint(new LinearGradientPaint(0, 0, 0, size, new float[]{0, .5f, .51f, 1}, new Color[]{a, b, AeroPalette.darker(b, .93f), b}));
            g.fill(s); g.setColor(d); g.setStroke(new BasicStroke(1.2f)); g.draw(s);
            g.setColor(new Color(255, 255, 255, 70)); g.fill(new Ellipse2D.Float(size * .18f, size * .06f, size * .64f, size * .36f));
            g.setColor(Color.WHITE);
            if (glyph.equals("x")) { g.setStroke(new BasicStroke(size / 7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); float m = size * .33f; g.draw(new Line2D.Float(m, m, size - m, size - m)); g.draw(new Line2D.Float(size - m, m, m, size - m)); }
            else { Font f = AeroFonts.bold(size * .62f); g.setFont(f); FontMetrics fm = g.getFontMetrics(); g.drawString(glyph, (size - fm.stringWidth(glyph)) / 2f, size * (type == javax.swing.JOptionPane.WARNING_MESSAGE ? .84f : .5f) + (fm.getAscent() - fm.getDescent()) / 2f - (type == javax.swing.JOptionPane.WARNING_MESSAGE ? size * .05f : 0)); }
            g.dispose();
        }
        static Shape tri(int s) { Path2D.Float p = new Path2D.Float(); p.moveTo(s / 2f, 1.5f); p.lineTo(s - 1.5f, s - 2.5f); p.quadTo(s - 1f, s - 1f, s - 3f, s - 1.5f); p.lineTo(3f, s - 1.5f); p.quadTo(1f, s - 1f, 1.5f, s - 2.5f); p.closePath(); return p; }
    }

    /** A window-caption glyph exactly as in the design: lucide minus (11px, 2.2), square (10px, 2), x (13px, 2.5). */
    public static Icon captionGlyph(String kind, Color c) {
        switch (kind) {
            case "min": return lucide("minus", 11, c, 2.2f);
            case "max": return lucide("square", 10, c, 2f);
            case "close": return lucide("x", 13, c, 2.5f);
            default: return new RestoreIcon(c);
        }
    }
    private static final class RestoreIcon implements Icon, UIResource {
        final Color c; RestoreIcon(Color c) { this.c = c; }
        public int getIconWidth() { return 10; } public int getIconHeight() { return 10; }
        public void paintIcon(Component comp, Graphics g0, int x, int y) {
            Graphics2D g = AeroPaint.g2(g0); g.setColor(c); g.setStroke(new BasicStroke(1.5f));
            g.draw(new Rectangle2D.Float(x + .75f, y + 2.75f, 6f, 6f)); g.draw(new Rectangle2D.Float(x + 2.75f, y + .75f, 6f, 6f)); g.dispose();
        }
    }
}
