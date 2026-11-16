package aero;

import aero.AeroPaint.Gloss;
import java.awt.Color;
import java.util.*;
import java.util.function.Function;

/**
 * Named button styles. Select one with {@code button.putClientProperty("JButton.style", "primary")}
 * or {@link AeroButtons#style(javax.swing.AbstractButton, String)}.
 * Built-in names: standard, primary, accent, success, error, danger, warning, info, flat, link, outline, caption, close, segment
 * (aliases: default/ribbon -> primary, print -> accent, cancel/abort -> error, hold -> danger, commit/add -> success,
 * toolbar/ghost -> flat, secondary/neutral -> standard). Custom styles: {@link #register(String, Function)}.
 */
public final class AeroButtonStyle {
    public enum State { NORMAL, HOVER, PRESSED, SELECTED, DISABLED }

    /** Everything needed to paint one state of a button. */
    public static final class Spec {
        public Gloss gloss; public Color border, fg, textShadow, inset; public float radius = 3f; public int weight = 500; public boolean underline;
        public Spec gloss(Gloss g) { gloss = g; return this; } public Spec border(Color c) { border = c; return this; }
        public Spec fg(Color c) { fg = c; return this; } public Spec shadow(Color c) { textShadow = c; return this; }
        public Spec inset(Color c) { inset = c; return this; } public Spec radius(float r) { radius = r; return this; }
        public Spec weight(int w) { weight = w; return this; } public Spec underline(boolean u) { underline = u; return this; }
    }

    public final String name;
    private final Function<State, Spec> fn;
    private AeroButtonStyle(String n, Function<State, Spec> f) { name = n; fn = f; }
    public Spec spec(State s) { return fn.apply(s); }

    private static final Map<String, AeroButtonStyle> REG = new LinkedHashMap<>();
    public static AeroButtonStyle register(String name, Function<State, Spec> fn) { AeroButtonStyle s = new AeroButtonStyle(name, fn); REG.put(name.toLowerCase(Locale.ROOT), s); return s; }
    public static void alias(String alias, String target) { REG.put(alias.toLowerCase(Locale.ROOT), REG.get(target)); }
    public static AeroButtonStyle get(String name) { AeroButtonStyle s = name == null ? null : REG.get(name.toLowerCase(Locale.ROOT)); return s != null ? s : STANDARD; }
    public static Set<String> names() { return Collections.unmodifiableSet(REG.keySet()); }

    private static Spec disabled(float r, int weight) {
        return new Spec().gloss(Gloss.of2(AeroPalette.DIS0, AeroPalette.DIS1)).border(AeroPalette.DIS_BORDER).fg(AeroPalette.DISABLED_FG).radius(3).weight(weight);
    }
    private static Spec flatDisabled() { return new Spec().fg(new Color(0xa3acb8)); }

    public static final AeroButtonStyle STANDARD = register("standard", s -> {
        switch (s) {
            case HOVER: return new Spec().gloss(Gloss.of4(AeroPalette.BTN_H0, AeroPalette.BTN_H1, AeroPalette.BTN_H2, AeroPalette.BTN_H3)).border(AeroPalette.BTN_H_BORDER).fg(AeroPalette.TEXT).inset(AeroPaint.alpha(Color.WHITE, .9));
            case PRESSED: return new Spec().gloss(Gloss.of4(AeroPalette.BTN_P0, AeroPalette.BTN_P1, AeroPalette.BTN_P2, AeroPalette.BTN_P3)).border(AeroPalette.BTN_P_BORDER).fg(AeroPalette.TEXT).inset(AeroPaint.alpha(Color.BLACK, .12));
            case SELECTED: return new Spec().gloss(Gloss.of4(AeroPalette.BTN_S0, AeroPalette.BTN_S1, AeroPalette.BTN_S2, AeroPalette.BTN_S3)).border(AeroPalette.BTN_S_BORDER).fg(AeroPalette.BTN_S_FG).inset(AeroPaint.alpha(new Color(0, 30, 60), .25)).weight(700);
            case DISABLED: return disabled(3, 500);
            default: return new Spec().gloss(Gloss.of4(AeroPalette.BTN0, AeroPalette.BTN1, AeroPalette.BTN2, AeroPalette.BTN3)).border(AeroPalette.BTN_BORDER).fg(AeroPalette.TEXT).inset(AeroPaint.alpha(Color.WHITE, .8));
        }
    });

    public static final AeroButtonStyle PRIMARY = register("primary", s -> {
        Spec p = new Spec().fg(Color.WHITE).shadow(new Color(0, 0, 0, 89)).weight(700).inset(AeroPaint.alpha(Color.WHITE, .5));
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.PR_H0, AeroPalette.PR_H1, AeroPalette.PR_H2, AeroPalette.PR_H3)).border(AeroPalette.PR_H_BORDER);
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.darker(AeroPalette.PR1, .92f), AeroPalette.darker(AeroPalette.PR2, .95f), AeroPalette.darker(AeroPalette.PR2, .82f), AeroPalette.darker(AeroPalette.PR3, .9f))).border(AeroPalette.darker(AeroPalette.PR_BORDER, .85f)).inset(AeroPaint.alpha(Color.BLACK, .2));
            case DISABLED: return disabled(3, 700);
            default: return p.gloss(Gloss.of4(AeroPalette.PR0, AeroPalette.PR1, AeroPalette.PR2, AeroPalette.PR3)).border(AeroPalette.PR_BORDER);
        }
    });

    /** The glossy blue "Print Invoice" button (radius 3, text-shadow .4). */
    public static final AeroButtonStyle ACCENT = register("accent", s -> {
        Spec p = new Spec().fg(Color.WHITE).shadow(new Color(0, 0, 0, 102)).weight(700).inset(AeroPaint.alpha(Color.WHITE, .45));
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.AC_H0, AeroPalette.AC_H1, AeroPalette.AC_H2, AeroPalette.AC_H3)).border(AeroPalette.AC_H_BORDER).inset(AeroPaint.alpha(Color.WHITE, .6));
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.darker(AeroPalette.AC1, .92f), AeroPalette.darker(AeroPalette.AC2, .95f), AeroPalette.darker(AeroPalette.AC2, .82f), AeroPalette.darker(AeroPalette.AC3, .9f))).border(AeroPalette.darker(AeroPalette.AC_BORDER, .85f)).inset(AeroPaint.alpha(Color.BLACK, .2));
            case DISABLED: return disabled(3, 700);
            default: return p.gloss(Gloss.of4(AeroPalette.AC0, AeroPalette.AC1, AeroPalette.AC2, AeroPalette.AC3)).border(AeroPalette.AC_BORDER);
        }
    });

    /** Glossy green "Add / Commit". */
    public static final AeroButtonStyle SUCCESS = register("success", s -> {
        Spec p = new Spec().fg(AeroPalette.SUCCESS_FG).weight(700).inset(Color.WHITE);
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.SUCCESS_H0, AeroPalette.SUCCESS_H1, AeroPalette.SUCCESS_H2, AeroPalette.SUCCESS_H1)).border(AeroPalette.SUCCESS_H_BORDER);
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.SUCCESS1, AeroPalette.SUCCESS2, AeroPalette.darker(AeroPalette.SUCCESS2, .88f), AeroPalette.SUCCESS2)).border(AeroPalette.darker(AeroPalette.SUCCESS_BORDER, .85f)).inset(AeroPaint.alpha(Color.BLACK, .12));
            case DISABLED: return disabled(3, 700);
            default: return p.gloss(Gloss.of4(AeroPalette.SUCCESS0, AeroPalette.SUCCESS1, AeroPalette.SUCCESS2, AeroPalette.SUCCESS1)).border(AeroPalette.SUCCESS_BORDER);
        }
    });

    /** Red "Cancel / Abort". */
    public static final AeroButtonStyle ERROR = register("error", s -> {
        Spec p = new Spec().fg(AeroPalette.ERR_FG).weight(700).inset(Color.WHITE);
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.ERR_H0, AeroPalette.ERR_H1, AeroPalette.ERR_H2, AeroPalette.ERR_H1)).border(AeroPalette.ERR_H_BORDER);
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.ERR1, AeroPalette.ERR2, AeroPalette.darker(AeroPalette.ERR2, .9f), AeroPalette.ERR2)).border(AeroPalette.darker(AeroPalette.ERR_BORDER, .8f)).inset(AeroPaint.alpha(Color.BLACK, .12));
            case DISABLED: return disabled(3, 700);
            default: return p.gloss(Gloss.of4(AeroPalette.ERR0, AeroPalette.ERR1, AeroPalette.ERR2, AeroPalette.ERR1)).border(AeroPalette.ERR_BORDER);
        }
    });

    /** Soft red "Hold" button. */
    public static final AeroButtonStyle DANGER = register("danger", s -> {
        Spec p = new Spec().fg(AeroPalette.HOLD_FG).weight(600).inset(Color.WHITE);
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(Color.WHITE, new Color(0xfff1f2), new Color(0xfecaca), new Color(0xfee2e2))).border(new Color(0xef4444));
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.HOLD1, AeroPalette.HOLD2, AeroPalette.darker(AeroPalette.HOLD2, .92f), AeroPalette.HOLD3)).border(new Color(0xdc2626)).inset(AeroPaint.alpha(Color.BLACK, .12));
            case DISABLED: return disabled(3, 600);
            default: return p.gloss(Gloss.of4(AeroPalette.HOLD0, AeroPalette.HOLD1, AeroPalette.HOLD2, AeroPalette.HOLD3)).border(AeroPalette.HOLD_BORDER);
        }
    });

    /** Amber - same gradient as the "Tokens Available" chip. */
    public static final AeroButtonStyle WARNING = register("warning", s -> {
        Spec p = new Spec().fg(AeroPalette.WARN_FG).weight(600).inset(Color.WHITE);
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.WARN_H0, AeroPalette.WARN_H1, AeroPalette.WARN_H2, AeroPalette.WARN3)).border(AeroPalette.WARN_H_BORDER);
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.WARN1, AeroPalette.WARN2, AeroPalette.darker(AeroPalette.WARN2, .92f), AeroPalette.WARN3)).border(AeroPalette.darker(AeroPalette.WARN_H_BORDER, .85f)).inset(AeroPaint.alpha(Color.BLACK, .12));
            case DISABLED: return disabled(3, 600);
            default: return p.gloss(Gloss.of4(AeroPalette.WARN0, AeroPalette.WARN1, AeroPalette.WARN2, AeroPalette.WARN3)).border(AeroPalette.WARN_BORDER);
        }
    });

    /** Sky info button. */
    public static final AeroButtonStyle INFO = register("info", s -> {
        Spec p = new Spec().fg(AeroPalette.INFO_FG).weight(600).inset(Color.WHITE);
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(Color.WHITE, AeroPalette.INFO1, AeroPalette.INFO2, AeroPalette.INFO1)).border(new Color(0x38bdf8));
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.INFO1, AeroPalette.INFO2, new Color(0x93d5f9), AeroPalette.INFO2)).border(new Color(0x0284c7)).inset(AeroPaint.alpha(Color.BLACK, .12));
            case DISABLED: return disabled(3, 600);
            default: return p.gloss(Gloss.of4(AeroPalette.INFO0, AeroPalette.INFO1, AeroPalette.INFO2, AeroPalette.INFO3)).border(AeroPalette.INFO_BORDER);
        }
    });

    /** Transparent until hovered (toolbar / icon buttons). */
    public static final AeroButtonStyle FLAT = register("flat", s -> {
        switch (s) {
            case HOVER: return STANDARD.spec(State.HOVER);
            case PRESSED: return STANDARD.spec(State.PRESSED);
            case SELECTED: return STANDARD.spec(State.SELECTED);
            case DISABLED: return flatDisabled();
            default: return new Spec().fg(AeroPalette.TEXT);
        }
    });

    public static final AeroButtonStyle LINK = register("link", s -> {
        Color c = AeroPalette.c(0x1d4ed8);
        switch (s) {
            case HOVER: return new Spec().fg(AeroPalette.c(0x0c2d48)).underline(true);
            case PRESSED: case SELECTED: return new Spec().fg(AeroPalette.c(0x172554)).underline(true).weight(600);
            case DISABLED: return flatDisabled();
            default: return new Spec().fg(c).underline(false);
        }
    });

    public static final AeroButtonStyle OUTLINE = register("outline", s -> {
        switch (s) {
            case HOVER: return PRIMARY.spec(State.HOVER);
            case PRESSED: case SELECTED: return PRIMARY.spec(State.PRESSED);
            case DISABLED: return new Spec().border(AeroPalette.DIS_BORDER).fg(AeroPalette.DISABLED_FG);
            default: return new Spec().border(AeroPalette.PR_BORDER).fg(AeroPalette.PR2).weight(600);
        }
    });

    /** Window caption button (minimise / maximise): transparent, glossy on hover. */
    public static final AeroButtonStyle CAPTION = register("caption", s -> {
        switch (s) {
            case HOVER: return new Spec().gloss(Gloss.of2(AeroPalette.CAP_H0, AeroPalette.CAP_H1)).border(AeroPalette.CAP_H_BORDER).fg(AeroPalette.TEXT);
            case PRESSED: case SELECTED: return new Spec().gloss(Gloss.of4(AeroPalette.BTN_P0, AeroPalette.BTN_P1, AeroPalette.BTN_P2, AeroPalette.BTN_P3)).border(AeroPalette.BTN_P_BORDER).fg(AeroPalette.TEXT);
            default: return new Spec().fg(AeroPalette.TEXT);
        }
    });

    /** Red window-close button. */
    public static final AeroButtonStyle CLOSE = register("close", s -> {
        Spec p = new Spec().fg(Color.WHITE).inset(AeroPaint.alpha(Color.WHITE, .4));
        switch (s) {
            case HOVER: return p.gloss(Gloss.of4(AeroPalette.CLOSE_H0, AeroPalette.CLOSE_H1, AeroPalette.CLOSE_H2, AeroPalette.CLOSE_H3)).border(AeroPalette.CLOSE_H_BORDER);
            case PRESSED: case SELECTED: return p.gloss(Gloss.of4(AeroPalette.darker(AeroPalette.CLOSE0, .85f), AeroPalette.darker(AeroPalette.CLOSE1, .85f), AeroPalette.darker(AeroPalette.CLOSE2, .85f), AeroPalette.darker(AeroPalette.CLOSE3, .85f))).border(AeroPalette.darker(AeroPalette.CLOSE_BORDER, .85f));
            default: return p.gloss(Gloss.of4(AeroPalette.CLOSE0, AeroPalette.CLOSE1, AeroPalette.CLOSE2, AeroPalette.CLOSE3)).border(AeroPalette.CLOSE_BORDER);
        }
    });

    /** Segmented-control look: standard, selected segment is the "pressed" blue. */
    public static final AeroButtonStyle SEGMENT = register("segment", s -> STANDARD.spec(s).weight(s == State.SELECTED ? 700 : 500));

    static {
        alias("default", "primary"); alias("ribbon", "primary"); alias("print", "accent"); alias("cancel", "error"); alias("abort", "error");
        alias("hold", "danger"); alias("commit", "success"); alias("add", "success"); alias("toolbar", "flat"); alias("ghost", "flat");
        alias("secondary", "standard"); alias("neutral", "standard"); alias("danger-soft", "danger"); alias("amber", "warning"); alias("sky", "info");
    }
}
