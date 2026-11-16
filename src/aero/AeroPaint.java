package aero;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.JComponent;

/** Shared painting helpers: CSS-style gradients, rounded borders, glows, dots. */
public final class AeroPaint {
    private AeroPaint() {}

    /** A CSS vertical gradient (stop fractions + colours). */
    public static final class Gloss {
        public final float[] f; public final Color[] c;
        public Gloss(float[] f, Color... c) { this.f = f; this.c = c; }
        /** linear-gradient(180deg, a 0%, b 49%, c 50%, d 100%) - the Win7 glossy pattern. */
        public static Gloss of4(Color a, Color b, Color c, Color d) { return new Gloss(new float[]{0f, .49f, .50f, 1f}, a, b, c, d); }
        public static Gloss of2(Color a, Color b) { return new Gloss(new float[]{0f, 1f}, a, b); }
        public static Gloss of3(Color a, float mid, Color b, Color c) { return new Gloss(new float[]{0f, mid, 1f}, a, b, c); }
    }

    private static Graphics2D scratch;
    /** Font metrics measured with the same rendering hints Aero paints with (fractional metrics) so sizes never truncate text. */
    public static synchronized FontMetrics fm(Font f) {
        if (scratch == null) { scratch = g2(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics()); }
        return scratch.getFontMetrics(f);
    }

    /** Graphics for Swing-measured text (text fields, labels, tooltips): AA on, integer metrics so layout and drawing agree. */
    public static Graphics2D textG2(Graphics g) {
        Graphics2D g2 = g2(g);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        return g2;
    }

    public static Graphics2D g2(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g2;
    }

    public static Paint vgrad(float y, float h, Gloss gl) {
        if (h < 1) h = 1;
        return new LinearGradientPaint(0, y, 0, y + h, gl.f, gl.c);
    }
    public static Paint hgrad(float x, float w, Gloss gl) {
        if (w < 1) w = 1;
        return new LinearGradientPaint(x, 0, x + w, 0, gl.f, gl.c);
    }

    public static RoundRectangle2D rr(float x, float y, float w, float h, float r) {
        return new RoundRectangle2D.Float(x, y, w, h, r * 2, r * 2);
    }

    /** Fills a rounded rect with a vertical gradient. */
    public static void fillGloss(Graphics2D g, float x, float y, float w, float h, float r, Gloss gl) {
        g.setPaint(vgrad(y, h, gl));
        g.fill(rr(x, y, w, h, r));
    }

    /** CSS "border: 1px solid c; border-radius: r" drawn inside x,y,w,h. */
    public static void border(Graphics2D g, float x, float y, float w, float h, float r, Color c) {
        g.setColor(c);
        g.setStroke(new BasicStroke(1f));
        float rr = Math.max(0, r * 2 - 1);
        g.draw(new RoundRectangle2D.Float(x + .5f, y + .5f, w - 1, h - 1, rr, rr));
    }

    /** CSS "box-shadow: inset 0 1px 0 c" - a 1px highlight under the top border. */
    public static void insetTop(Graphics2D g, float x, float y, float w, Color c, float r) {
        g.setColor(c);
        float inset = Math.max(1f, r - .5f);
        g.fill(new Rectangle2D.Float(x + 1 + (r > 1 ? 0.5f : 0), y + 1, Math.max(0, w - 2 - (r > 1 ? 1f : 0)), 1));
    }

    /** Full glossy surface: gradient + border + inset highlight. */
    public static void glossRect(Graphics2D g, int x, int y, int w, int h, float r, Gloss gl, Color border, Color insetColor) {
        fillGloss(g, x, y, w, h, r, gl);
        if (insetColor != null) insetTop(g, x, y, w, insetColor, r);
        border(g, x, y, w, h, r, border);
    }

    public static Color alpha(Color c, double a) { return AeroPalette.alpha(c, a); }

    /** Draws text with a white halo ("text-shadow: 0 0 8px #fff, 0 0 3px #fff") using a small blur. */
    public static void glowText(Graphics2D g, String s, int x, int baseline, Font font, Color fg, int radius, float strength) {
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(s) + radius * 4 + 4, h = fm.getHeight() + radius * 4 + 4;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D ig = g2(img.getGraphics());
        ig.setFont(font); ig.setColor(Color.WHITE);
        ig.drawString(s, radius * 2 + 2, radius * 2 + 2 + fm.getAscent());
        ig.dispose();
        int k = radius * 2 + 1; float[] kernel = new float[k * k]; java.util.Arrays.fill(kernel, 1f / (k * k));
        BufferedImage blurred = new ConvolveOp(new Kernel(k, k, kernel), ConvolveOp.EDGE_NO_OP, null).filter(img, null);
        Composite old = g.getComposite();
        for (int i = 0; i < 3; i++) { g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1f, strength))); g.drawImage(blurred, x - radius * 2 - 2, baseline - fm.getAscent() - radius * 2 - 2, null); }
        g.setComposite(old);
        g.setColor(fg);
        g.drawString(s, x, baseline);
    }

    /** Draws a string with a 1px offset text-shadow. */
    public static void shadowText(Graphics2D g, String s, int x, int y, Color fg, Color shadow, int dy) {
        g.setColor(shadow); g.drawString(s, x, y + dy);
        g.setColor(fg); g.drawString(s, x, y);
    }

    public static void centerText(Graphics2D g, String s, Rectangle r, Font f, Color c) {
        g.setFont(f); g.setColor(c);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, r.x + (r.width - fm.stringWidth(s)) / 2f, r.y + (r.height - fm.getHeight()) / 2f + fm.getAscent());
    }

    /** Dotted grip (CSS radial-gradient dots). */
    public static void dots(Graphics2D g, int x, int y, int w, int h, Color c, int stepX, int stepY) {
        g.setColor(c);
        for (int yy = y; yy < y + h; yy += stepY) for (int xx = x; xx < x + w; xx += stepX) g.fillRect(xx, yy, 1, 1);
    }

    public static void aaOn(Graphics2D g) { g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); }

    /** Soft shadow rectangle painted from translucent rings (for tooltips etc). */
    public static void softShadow(Graphics2D g, int x, int y, int w, int h, int blur, int offX, int offY, float alpha, float radius) {
        for (int i = blur; i >= 1; i--) {
            float a = alpha * (1f - (i - 1f) / blur) / blur * 1.6f;
            g.setColor(new Color(0, 0, 0, Math.min(255, Math.round(a * 255))));
            g.fill(rr(x + offX - i, y + offY - i, w + 2 * i, h + 2 * i, radius + i));
        }
    }

    public static boolean isCompact() { return AeroLookAndFeel.compact; }

    public static int scaleH(int px) { return isCompact() ? px - 2 : px; }

    public static boolean isEnabledLook(JComponent c) { return c == null || c.isEnabled(); }
}
