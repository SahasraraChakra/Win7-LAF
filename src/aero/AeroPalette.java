package aero;

import java.awt.Color;

/**
 * Every colour used by the Aero look and feel. Values are lifted verbatim from the design CSS
 * (win7_aero_flatlaf_ui_showcase). Blue-family colours go through {@link #c(int)} so the whole LAF can be
 * re-tinted at runtime (Sky Blue, Sea Teal, Graphite, Twilight, Solar Amber or a custom hex).
 */
public final class AeroPalette {
    private AeroPalette() {}

    /** Aero tint presets (menu "Aero Tint Palettes" in the design). */
    public enum Tint {
        SKY_BLUE("Sky Blue", 0, 1f, 1f), SEA_TEAL("Sea Teal Gloss", -34, 1.05f, 1f), GRAPHITE("Graphite Slate", 0, 0.10f, 0.97f),
        TWILIGHT("Twilight Aero", 37, 1.1f, 1f), SOLAR_AMBER("Solar Amber Glow", -170, 1.9f, 1f);
        public final String label; final float shift, sat, bri;
        Tint(String l, float s, float sa, float b) { label = l; shift = s; sat = sa; bri = b; }
        @Override public String toString() { return label; }
    }

    private static float shift = 0, satScale = 1, briScale = 1;
    private static Tint tint = Tint.SKY_BLUE;
    public static Tint getTint() { return tint; }
    public static int version = 0;

    public static void setTint(Tint t) { tint = t; shift = t.shift; satScale = t.sat; briScale = t.bri; reload(); }
    /** Custom tint from any colour (hue is rotated relative to the default sky blue, 208 degrees). */
    public static void setCustomTint(Color base) {
        float[] h = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        tint = null; shift = h[0] * 360f - 208f; satScale = Math.max(0.1f, Math.min(2.2f, h[1] * 1.4f)); briScale = 1f; reload();
    }

    /** Tinted (blue-family) colour. */
    public static Color c(int rgb) { return c(rgb, 255); }
    public static Color c(int rgb, int a) {
        Color k = new Color(rgb);
        float[] h = Color.RGBtoHSB(k.getRed(), k.getGreen(), k.getBlue(), null);
        float hue = h[0] * 360f;
        if (h[1] > 0.012f && hue >= 180f && hue <= 245f && (shift != 0 || satScale != 1f || briScale != 1f)) {
            hue = (hue + shift + 360f) % 360f;
            float s = Math.min(1f, h[1] * satScale), b = Math.min(1f, h[2] * briScale);
            int r = Color.HSBtoRGB(hue / 360f, s, b);
            return new Color((r >> 16) & 255, (r >> 8) & 255, r & 255, a);
        }
        return new Color(k.getRed(), k.getGreen(), k.getBlue(), a);
    }
    /** Untinted colour with alpha. */
    public static Color n(int rgb) { return new Color(rgb); }
    public static Color n(int r, int g, int b, double a) { return new Color(r, g, b, (int) Math.round(a * 255)); }

    // ---- neutrals / text (Tailwind slate) ----
    public static final Color TEXT = new Color(0x1e293b), TEXT_STRONG = new Color(0x0f172a), TEXT_2 = new Color(0x334155),
            TEXT_DIM = new Color(0x475569), TEXT_MUTED = new Color(0x64748b), TEXT_FAINT = new Color(0x94a3b8),
            WHITE = Color.WHITE, DISABLED_FG = new Color(0x838383);
    public static final Color INPUT_BORDER = new Color(0xcbd5e1), READONLY_BG = new Color(0xf1f5f9), GRID = new Color(0xe2e8f0);

    // ---- window / titlebar ----
    public static Color TB0, TB1, TB2, TB3, TB_BORDER, WIN_BORDER, TB_INNER;
    public static Color WIN_BG = Color.WHITE;
    // ---- menubar / menu / popup ----
    public static Color MB0, MB1, MB_BORDER, MB_HOT_BG, MB_HOT_BORDER, MB_OPEN_BG, MB_OPEN_BORDER, MB_OPEN_FG;
    public static Color POP0, POP1, POP_BORDER, MI_HOT0, MI_HOT1, MI_HOT_BORDER, MI_HOT_FG, SEP, SEP_LIGHT, ARROW_BLUE, CHECK_BLUE, MENU_HEADER;
    // ---- toolbars ----
    public static Color RB0, RB1, RB2, RB3, RB_BORDER, T2_0, T2_1, T2_2, T2_BORDER, ST0, ST1, ST2, ST3, ST_BORDER;
    // ---- buttons ----
    public static Color BTN0, BTN1, BTN2, BTN3, BTN_BORDER, BTN_H0, BTN_H1, BTN_H2, BTN_H3, BTN_H_BORDER,
            BTN_P0, BTN_P1, BTN_P2, BTN_P3, BTN_P_BORDER, BTN_S0, BTN_S1, BTN_S2, BTN_S3, BTN_S_BORDER, BTN_S_FG,
            DEF_GLOW;
    public static Color PR0, PR1, PR2, PR3, PR_BORDER, PR_H0, PR_H1, PR_H2, PR_H3, PR_H_BORDER;
    public static Color AC0, AC1, AC2, AC3, AC_BORDER, AC_H0, AC_H1, AC_H2, AC_H3, AC_H_BORDER;
    public static Color CAP_H0, CAP_H1, CAP_H_BORDER, CLOSE0, CLOSE1, CLOSE2, CLOSE3, CLOSE_BORDER, CLOSE_H0, CLOSE_H1, CLOSE_H2, CLOSE_H3, CLOSE_H_BORDER;
    // semantic (not tinted)
    public static final Color SUCCESS0 = new Color(0xd1fae5), SUCCESS1 = new Color(0xa7f3d0), SUCCESS2 = new Color(0x6ee7b7), SUCCESS_BORDER = new Color(0x1f6e35), SUCCESS_FG = new Color(0x064e3b),
            SUCCESS_H0 = new Color(0xecfdf5), SUCCESS_H1 = new Color(0xbbf7d0), SUCCESS_H2 = new Color(0x86efac), SUCCESS_H_BORDER = new Color(0x166534);
    public static final Color ERR0 = new Color(0xfee2e2), ERR1 = new Color(0xfca5a5), ERR2 = new Color(0xf87171), ERR_BORDER = new Color(0xef4444), ERR_FG = new Color(0x4c0519),
            ERR_H0 = new Color(0xfff1f2), ERR_H1 = new Color(0xfecaca), ERR_H2 = new Color(0xfb8c8c), ERR_H_BORDER = new Color(0xdc2626);
    public static final Color HOLD0 = Color.WHITE, HOLD1 = new Color(0xfee2e2), HOLD2 = new Color(0xfca5a5), HOLD3 = new Color(0xfecaca), HOLD_BORDER = new Color(0xf87171), HOLD_FG = new Color(0x881337);
    public static final Color WARN0 = new Color(0xfffdf2), WARN1 = new Color(0xfef3c7), WARN2 = new Color(0xfde68a), WARN3 = new Color(0xfef08a), WARN_BORDER = new Color(0xfcd34d), WARN_FG = new Color(0x78350f),
            WARN_H0 = new Color(0xfffef7), WARN_H1 = new Color(0xfef9c3), WARN_H2 = new Color(0xfde047), WARN_H_BORDER = new Color(0xf59e0b);
    public static final Color INFO0 = new Color(0xf0f9ff), INFO1 = new Color(0xe0f2fe), INFO2 = new Color(0xbae6fd), INFO3 = new Color(0xe0f2fe), INFO_BORDER = new Color(0x7dd3fc), INFO_FG = new Color(0x0c4a6e);
    public static final Color DIS0 = new Color(0xf4f4f4), DIS1 = new Color(0xe9e9e9), DIS_BORDER = new Color(0xadb2b5);
    // ---- inputs ----
    public static Color FOCUS, FOCUS_GLOW, INFO_BG0, INFO_BG1, INFO_FIELD_BORDER, INFO_FIELD_FG;
    public static final Color WARN_BG = new Color(0xfffbeb), WARN_FIELD_BORDER = new Color(0xf59e0b), ERR_FIELD_BORDER = new Color(0xef4444), OK_FIELD_BORDER = new Color(0x22c55e);
    public static Color SELECTION_BG, SELECTION_FG;
    // ---- tables / lists / trees ----
    public static Color TH0, TH1, TH2, TH3, TH_BOTTOM, TH_RIGHT, ROW_ALT, ROW_HOT, SEL0, SEL1, SEL_OUTLINE, SEL_FG;
    public static Color LIST_SEL, TREE_SEL_BG, TREE_SEL_BORDER, TREE_SEL_FG, TREE_HOT, LIST_HOT;
    // ---- tabs ----
    public static Color TAB0, TAB1, TAB_BORDER, TAB_FG, ACT0, ACT1, ACT2, ACT_BORDER, ACT_FG, STRIP_BG, STRIP_BORDER, TAB_BODY, TAB_HOT;
    // ---- frames / desktop ----
    public static Color IF0, IF1, IF2, IF3, IF_BORDER, FRAME_BORDER, FRAME_BG, FRAME_TITLE_FG, DESK0, DESK1, DESK_BORDER;
    // ---- misc controls ----
    public static Color TRACK, TRACK_BORDER, THUMB0, THUMB1, THUMB2, THUMB3, THUMB_BORDER;
    public static Color PG0, PG1, PG2, PG3, PG4, PG_BORDER, PG_TRACK, PG_TRACK_BORDER, PG_STRIPE_A, PG_STRIPE_B, PG_INDET_BORDER;
    public static Color SB_TRACK, SB_TRACK_BORDER, SB0, SB1, SB_BORDER, SB_H0, SB_H1, SB_H_BORDER;
    public static Color SP0, SP1, SP2, SP_BORDER, SP_DOTS;
    public static Color TIP_BG, TIP_BORDER, GROUP_BORDER, GROUP_TITLE, CTRL_BORDER, PANEL_BG, TAB_PANEL_BG, CHECK_FILL, CHECK_BORDER;

    static { reload(); }

    /** Re-evaluates every tinted colour. */
    public static void reload() {
        version++;
        int a;
        TB0 = c(0xebf5ff, 224); TB1 = c(0xc3daf2, 184); TB2 = c(0xa2c3e6, 166); TB3 = c(0xbcd6f0, 209);
        TB_BORDER = c(0x789bbe, 191); WIN_BORDER = c(0x789bbe, 217); TB_INNER = n(255, 255, 255, .95);
        MB0 = c(0xf3f7fc); MB1 = c(0xe4edf5); MB_BORDER = c(0xb5c7d8); MB_HOT_BG = c(0xcde4f7); MB_HOT_BORDER = c(0x8bbce0);
        MB_OPEN_BG = c(0xb9dbf7); MB_OPEN_BORDER = c(0x528fc2); MB_OPEN_FG = c(0x172554);
        POP0 = Color.WHITE; POP1 = c(0xf4f7fb); POP_BORDER = c(0x769ec2); MI_HOT0 = c(0xeaf6fd); MI_HOT1 = c(0xcde9fa);
        MI_HOT_BORDER = c(0x8fc2e9); MI_HOT_FG = c(0x0c2d48); SEP = c(0xd2dfec); SEP_LIGHT = Color.WHITE;
        ARROW_BLUE = c(0x1e40af); CHECK_BLUE = c(0x2563eb); MENU_HEADER = new Color(0x94a3b8);
        RB0 = c(0xf3f7fb); RB1 = c(0xe1edf7); RB2 = c(0xcee0f2); RB3 = c(0xe0effa); RB_BORDER = c(0x9ab6d1);
        T2_0 = c(0xfdfdfd); T2_1 = c(0xedf2f7); T2_2 = c(0xe1e9f1); T2_BORDER = c(0xb2c5d8);
        ST0 = new Color(0xf2f2f2); ST1 = new Color(0xebebeb); ST2 = new Color(0xdfdfdf); ST3 = new Color(0xe8e8e8); ST_BORDER = new Color(0xbfbfbf);
        BTN0 = new Color(0xfbfbfb); BTN1 = new Color(0xececec); BTN2 = new Color(0xdedede); BTN3 = new Color(0xd5d5d5); BTN_BORDER = c(0x8e9cad);
        BTN_H0 = c(0xeaf6fd); BTN_H1 = c(0xd8edfa); BTN_H2 = c(0xbde4f9); BTN_H3 = c(0xc8e9fa); BTN_H_BORDER = c(0x3c7fb1);
        BTN_P0 = c(0xbde4f9); BTN_P1 = c(0x9dd3f5); BTN_P2 = c(0x81c5f0); BTN_P3 = c(0x90cdf2); BTN_P_BORDER = c(0x266493);
        BTN_S0 = c(0xcde3f7); BTN_S1 = c(0xb3d5f3); BTN_S2 = c(0x98c6ed); BTN_S3 = c(0xb2d5f2); BTN_S_BORDER = c(0x3275a8); BTN_S_FG = c(0x092642);
        DEF_GLOW = c(0x37a0e6);
        PR0 = c(0x62abed); PR1 = c(0x3590dc); PR2 = c(0x1d77c4); PR3 = c(0x298cd9); PR_BORDER = c(0x165b99);
        PR_H0 = c(0x74bbf3); PR_H1 = c(0x46a1e8); PR_H2 = c(0x2686d1); PR_H3 = c(0x3ea0e7); PR_H_BORDER = c(0x1a67ad);
        AC0 = c(0x59a5e1); AC1 = c(0x308cd6); AC2 = c(0x1b73be); AC3 = c(0x2086d8); AC_BORDER = c(0x155894);
        AC_H0 = c(0x70baf2); AC_H1 = c(0x46a1e8); AC_H2 = c(0x2888d4); AC_H3 = c(0x3c9fe8); AC_H_BORDER = c(0x1a67ad);
        CAP_H0 = n(255, 255, 255, .85); CAP_H1 = n(215, 236, 252, .85); CAP_H_BORDER = c(0x7da2ce);
        CLOSE0 = new Color(0xf48a85); CLOSE1 = new Color(0xe8554e); CLOSE2 = new Color(0xd62d25); CLOSE3 = new Color(0xe24e46); CLOSE_BORDER = new Color(0x9c1c16);
        CLOSE_H0 = new Color(0xf7a19d); CLOSE_H1 = new Color(0xee6862); CLOSE_H2 = new Color(0xe8372e); CLOSE_H3 = new Color(0xef5c54); CLOSE_H_BORDER = new Color(0xb5221b);
        FOCUS = new Color(0x0284c7); FOCUS_GLOW = new Color(2, 132, 199, 89);
        INFO_BG0 = c(0xeff6ff); INFO_BG1 = c(0xdbeafe); INFO_FIELD_BORDER = c(0x93c5fd); INFO_FIELD_FG = c(0x1e3a8a);
        SELECTION_BG = c(0xb3d5f3); SELECTION_FG = c(0x092642);
        TH0 = c(0xf7f9fa); TH1 = c(0xe9edf1); TH2 = c(0xdbe2e8); TH3 = c(0xd2dbe2); TH_BOTTOM = c(0xa4b4c4); TH_RIGHT = c(0xbccad6);
        ROW_ALT = c(0xf8fbfe); ROW_HOT = c(0xeaf4fe); SEL0 = c(0xdbeefa); SEL1 = c(0xc1e2f9); SEL_OUTLINE = c(0x7da2ce); SEL_FG = c(0x172554);
        LIST_SEL = c(0x2563eb); TREE_SEL_BG = c(0xcce4f7); TREE_SEL_BORDER = c(0x7db0d8); TREE_SEL_FG = c(0x1e3a8a); TREE_HOT = c(0xf0f9ff); LIST_HOT = new Color(0xf1f5f9);
        TAB0 = c(0xe4ecf3); TAB1 = c(0xccd8e5); TAB_BORDER = c(0x9fb3c8); TAB_FG = c(0x3b4d5e);
        ACT0 = Color.WHITE; ACT1 = c(0xf4f8fc); ACT2 = c(0xe8f0f8); ACT_BORDER = c(0x7b96b0); ACT_FG = c(0x0c2136);
        STRIP_BG = c(0xdbe6f2); STRIP_BORDER = c(0xa6bcd1); TAB_BODY = c(0xf4f7fb); TAB_HOT = n(255, 255, 255, .6);
        IF0 = c(0xebf5ff, 242); IF1 = c(0xc3daf2, 224); IF2 = c(0xa2c3e6, 209); IF3 = c(0xbcd6f0, 235); IF_BORDER = c(0x789bbe, 191);
        FRAME_BORDER = c(0x234c6e); FRAME_BG = new Color(0xf8fafc); FRAME_TITLE_FG = c(0x102436);
        DESK0 = c(0x244b70); DESK1 = c(0x0d1e2e); DESK_BORDER = c(0x2b4d6b);
        TRACK = c(0xcbd7e6); TRACK_BORDER = c(0x8e9cad);
        THUMB0 = Color.WHITE; THUMB1 = c(0xc8ddf2); THUMB2 = c(0x93c0e8); THUMB3 = c(0xb5d5f2); THUMB_BORDER = c(0x3c77a8);
        PG0 = new Color(0xc3ef86); PG1 = new Color(0x8ad728); PG2 = new Color(0x65be09); PG3 = new Color(0x79d115); PG4 = new Color(0x92dd24); PG_BORDER = new Color(0x5aa108);
        PG_TRACK = new Color(0xe2e8f0); PG_TRACK_BORDER = c(0x7a9ab5); PG_STRIPE_A = c(0x4ba0ec); PG_STRIPE_B = c(0x297ecc); PG_INDET_BORDER = new Color(0x60a5fa);
        SB_TRACK = new Color(0xf1f5f9); SB_TRACK_BORDER = new Color(0xe2e8f0); SB0 = c(0xdfecf7); SB1 = c(0xbddcf4); SB_BORDER = c(0x7ea5c7);
        SB_H0 = c(0xc3e2fa); SB_H1 = c(0x8ac0ec); SB_H_BORDER = c(0x4b89bd);
        SP0 = c(0xedf3fa); SP1 = c(0xd4e2f0); SP2 = c(0xc0d3e6); SP_BORDER = c(0x9fb3c8); SP_DOTS = c(0x637c95);
        TIP_BG = new Color(0xffffe1); TIP_BORDER = new Color(0x595959);
        GROUP_BORDER = c(0xb8c9d9); GROUP_TITLE = c(0x1f4060); CTRL_BORDER = c(0xb8c9d9); PANEL_BG = c(0xf4f7fb); TAB_PANEL_BG = c(0xf4f7fb);
        CHECK_FILL = new Color(0x2563eb); CHECK_BORDER = new Color(0x94a3b8);
    }

    public static Color mix(Color a, Color b, float t) {
        return new Color(Math.round(a.getRed() + (b.getRed() - a.getRed()) * t), Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
                Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t), Math.round(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t));
    }
    public static Color darker(Color c, float f) { return new Color(Math.round(c.getRed() * f), Math.round(c.getGreen() * f), Math.round(c.getBlue() * f), c.getAlpha()); }
    public static Color alpha(Color c, double a) { return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) Math.round(255 * a)); }
}
