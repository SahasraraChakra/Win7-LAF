package aero;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import javax.swing.plaf.FontUIResource;

/** Inter (bundled, as used by the design) with Segoe UI / SansSerif fallbacks. */
public final class AeroFonts {
    private AeroFonts() {}
    private static Font regular, medium, semibold, bold;
    private static boolean loaded;
    public static float BASE = 11f;

    private static Font load(String file) {
        try (InputStream in = AeroFonts.class.getResourceAsStream("/aero/res/" + file)) {
            if (in == null) return null;
            Font f = Font.createFont(Font.TRUETYPE_FONT, in);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
            return f;
        } catch (Exception e) { return null; }
    }
    private static synchronized void init() {
        if (loaded) return;
        loaded = true;
        regular = load("Inter-Regular.otf"); medium = load("Inter-Medium.otf"); semibold = load("Inter-SemiBold.otf"); bold = load("Inter-Bold.otf");
        if (regular == null) { regular = new Font("Segoe UI", Font.PLAIN, 12); medium = regular; semibold = new Font("Segoe UI", Font.BOLD, 12); bold = semibold; }
        if (medium == null) medium = regular;
        if (semibold == null) semibold = bold != null ? bold : regular;
        if (bold == null) bold = semibold;
    }
    public static Font plain(float size) { init(); return regular.deriveFont(size); }
    public static Font medium(float size) { init(); return medium.deriveFont(size); }
    public static Font semibold(float size) { init(); return semibold.deriveFont(size); }
    public static Font bold(float size) { init(); return bold.deriveFont(size); }
    public static Font mono(float size) { return new Font(Font.MONOSPACED, Font.PLAIN, Math.round(size)); }
    public static Font monoBold(float size) { return new Font(Font.MONOSPACED, Font.BOLD, Math.round(size)); }
    public static FontUIResource ui(Font f) { return new FontUIResource(f); }
    /** Returns the weight-matched variant of a font (keeps the size). */
    public static Font weight(Font base, int weightClass) {
        float s = base.getSize2D();
        return weightClass >= 700 ? bold(s) : weightClass >= 600 ? semibold(s) : weightClass >= 500 ? medium(s) : plain(s);
    }
}
