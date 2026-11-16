package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicGraphicsUtils;

/** Paints and measures every kind of menu item (menu-bar menus, submenus, items, check/radio items, headers). */
final class AeroMenuPainter {
    private AeroMenuPainter() {}
    static final int PAD_L = 7, COL = 12, GAP = 8, PAD_R = 9;

    static boolean top(JMenuItem mi) { return mi instanceof JMenu && ((JMenu) mi).isTopLevelMenu(); }
    static boolean header(JMenuItem mi) { return Boolean.TRUE.equals(mi.getClientProperty("JMenuItem.header")); }

    static Dimension size(JMenuItem mi) {
        Font f = mi.getFont(); FontMetrics fm = AeroPaint.fm(f); String t = mi.getText() == null ? "" : mi.getText();
        if (top(mi)) { return new Dimension(fm.stringWidth(t) + 18, Math.max(fm.getHeight() + 6, AeroPaint.scaleH(22))); }
        if (header(mi)) { Font hf = AeroFonts.bold(9.5f); FontMetrics hm = AeroPaint.fm(hf); return new Dimension(hm.stringWidth(t.toUpperCase()) + 24, 20); }
        int w = PAD_L + Math.max(COL, mi.getIcon() != null ? mi.getIcon().getIconWidth() : 0) + GAP + fm.stringWidth(t) + PAD_R;
        KeyStroke ks = mi.getAccelerator();
        if (ks != null) { FontMetrics am = AeroPaint.fm(AeroFonts.mono(10f)); w += 28 + am.stringWidth(accel(ks)); }
        if (mi instanceof JMenu) w += 18;
        int h = Math.max(fm.getHeight() + 8, AeroPaint.scaleH(22));
        if (mi.getIcon() != null) h = Math.max(h, mi.getIcon().getIconHeight() + 6);
        return new Dimension(Math.max(w, 110), h);
    }

    static String accel(KeyStroke ks) {
        String m = java.awt.event.InputEvent.getModifiersExText(ks.getModifiers()); String k = KeyEvent2.text(ks);
        return m.isEmpty() ? k : m + "+" + k;
    }
    private static final class KeyEvent2 { static String text(KeyStroke ks) { return ks.getKeyCode() == 0 ? String.valueOf(ks.getKeyChar()) : java.awt.event.KeyEvent.getKeyText(ks.getKeyCode()); } }

    static void paint(Graphics g0, JMenuItem mi) {
        Graphics2D g = AeroPaint.g2(g0); int w = mi.getWidth(), h = mi.getHeight(); ButtonModel m = mi.getModel();
        boolean en = mi.isEnabled(), armed = m.isArmed() || (mi instanceof JMenu && m.isSelected()), hot = en && (armed || m.isRollover());
        Font f = mi.getFont(); g.setFont(f); FontMetrics fm = g.getFontMetrics();
        String t = mi.getText() == null ? "" : mi.getText();
        if (top(mi)) {
            boolean open = m.isSelected() || m.isArmed() && ((JMenu) mi).isPopupMenuVisible();
            int x = 1, ww = w - 2;
            if (open && en) {
                g.setColor(new Color(0, 0, 0, 25)); g.fill(AeroPaint.rr(x, 1, ww, h, 2));
                g.setColor(AeroPalette.MB_OPEN_BG); g.fill(AeroPaint.rr(x, 0, ww, h, 2)); g.fillRect(x, h / 2, ww, h - h / 2);
                g.setColor(AeroPalette.MB_OPEN_BORDER); g.drawLine(x, 2, x, h - 1); g.drawLine(x + ww - 1, 2, x + ww - 1, h - 1); g.drawLine(x + 2, 0, x + ww - 3, 0);
                g.drawLine(x, 1, x + 1, 0); g.drawLine(x + ww - 1, 1, x + ww - 2, 0);
                f = AeroFonts.semibold(f.getSize2D());
            } else if (hot) { g.setColor(AeroPalette.MB_HOT_BG); g.fill(AeroPaint.rr(x, 0, ww, h, 2)); AeroPaint.border(g, x, 0, ww, h, 2, AeroPalette.MB_HOT_BORDER); }
            g.setFont(f); fm = g.getFontMetrics();
            g.setColor(!en ? AeroPalette.TEXT_FAINT : open ? AeroPalette.MB_OPEN_FG : AeroPalette.TEXT);
            int tx = (w - fm.stringWidth(t)) / 2, ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, t, mi.getDisplayedMnemonicIndex(), tx, ty); g.dispose(); return;
        }
        if (header(mi)) {
            Font hf = AeroFonts.bold(9.5f); g.setFont(hf); fm = g.getFontMetrics(); g.setColor(AeroPalette.MENU_HEADER);
            String s = t.toUpperCase(); float x = 8; int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            for (int i = 0; i < s.length(); i++) { String c = s.substring(i, i + 1); g.drawString(c, x, y); x += fm.stringWidth(c) + .5f; }
            g.dispose(); return;
        }
        if (hot) { AeroPaint.fillGloss(g, 0, 0, w, h, 2, Gloss.of2(AeroPalette.MI_HOT0, AeroPalette.MI_HOT1)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.MI_HOT_BORDER); }
        Color fg = !en ? AeroPalette.TEXT_FAINT : hot ? AeroPalette.MI_HOT_FG : (mi.getForeground() instanceof UIResource ? AeroPalette.TEXT : mi.getForeground());
        if (Boolean.TRUE.equals(mi.getClientProperty("JMenuItem.destructive")) && en) fg = new Color(0x9f1239);
        // leading column
        int cy = h / 2; int cx = PAD_L;
        if (mi instanceof JCheckBoxMenuItem) AeroIcons.paintCheckBox(g, cx, cy - 6, 12, mi.isSelected(), false, en, false, false);
        else if (mi instanceof JRadioButtonMenuItem) {
            if (Boolean.TRUE.equals(mi.getClientProperty("JRadioButtonMenuItem.checkStyle"))) { if (mi.isSelected()) AeroIcons.lucide("check", 12, AeroPalette.CHECK_BLUE, 3f).paintIcon(mi, g, cx, cy - 6); }
            else AeroIcons.paintRadio(g, cx, cy - 6, 12, mi.isSelected(), en, false, false);
        } else if (mi.getIcon() != null) { Icon ic = !en && mi.getDisabledIcon() != null ? mi.getDisabledIcon() : mi.getIcon(); ic.paintIcon(mi, g, cx, cy - ic.getIconHeight() / 2); }
        int iw = Math.max(COL, mi.getIcon() != null && !(mi instanceof JCheckBoxMenuItem) && !(mi instanceof JRadioButtonMenuItem) ? mi.getIcon().getIconWidth() : COL);
        int tx = PAD_L + iw + GAP, ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        if (Boolean.TRUE.equals(mi.getClientProperty("JMenuItem.emphasis")) || (mi instanceof JMenu && hot)) g.setFont(AeroFonts.semibold(f.getSize2D()));
        g.setColor(fg); BasicGraphicsUtils.drawStringUnderlineCharAt(g, t, mi.getDisplayedMnemonicIndex(), tx, ty);
        int right = w - PAD_R;
        if (mi instanceof JMenu) { AeroIcons.triangle(SwingConstants.EAST, 4, 7, en ? AeroPalette.ARROW_BLUE : AeroPalette.TEXT_FAINT).paintIcon(mi, g, right - 4, cy - 3); right -= 14; }
        KeyStroke ks = mi.getAccelerator();
        if (ks != null) { g.setFont(AeroFonts.mono(10f)); FontMetrics am = g.getFontMetrics(); g.setColor(AeroPalette.TEXT_FAINT); String a = accel(ks); g.drawString(a, right - am.stringWidth(a), (h - am.getHeight()) / 2 + am.getAscent()); }
        g.dispose();
    }
}
