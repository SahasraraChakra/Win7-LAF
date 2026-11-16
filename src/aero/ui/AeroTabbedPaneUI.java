package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Tabs (TOP/BOTTOM/LEFT/RIGHT): gradient tabs, bold white active tab, strip background (design .aero-tab). */
public class AeroTabbedPaneUI extends BasicTabbedPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroTabbedPaneUI(); }
    @Override protected void installDefaults() {
        super.installDefaults();
        tabInsets = new Insets(4, 10, 4, 10); selectedTabPadInsets = new Insets(0, 0, 0, 0); tabAreaInsets = new Insets(4, 8, 0, 8); contentBorderInsets = new Insets(1, 1, 1, 1);
        tabPane.setOpaque(false); tabPane.setFont(AeroFonts.medium(11f));
    }
    @Override protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) { return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 8; }
    @Override protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) { return Math.max(super.calculateTabHeight(tabPlacement, tabIndex, fontHeight), AeroPaint.scaleH(24)); }
    @Override protected Insets getTabInsets(int tabPlacement, int tabIndex) { return tabInsets; }
    @Override protected Insets getSelectedTabPadInsets(int tabPlacement) { return new Insets(0, 0, 0, 0); }
    @Override protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) { return 0; }
    @Override protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) { return 0; }

    @Override public void paint(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); int p = tabPane.getTabPlacement(); Insets in = tabPane.getInsets();
        int w = tabPane.getWidth(), h = tabPane.getHeight();
        if (tabPane.getTabCount() > 0) {
            g.setColor(AeroPalette.STRIP_BG);
            switch (p) {
                case LEFT: g.fillRect(in.left, in.top, calculateTabAreaWidth(p, runCount, maxTabWidth), h - in.top - in.bottom); break;
                case RIGHT: { int tw = calculateTabAreaWidth(p, runCount, maxTabWidth); g.fillRect(w - in.right - tw, in.top, tw, h - in.top - in.bottom); break; }
                case BOTTOM: { int th = calculateTabAreaHeight(p, runCount, maxTabHeight); g.fillRect(in.left, h - in.bottom - th, w - in.left - in.right, th); break; }
                default: g.fillRect(in.left, in.top, w - in.left - in.right, calculateTabAreaHeight(p, runCount, maxTabHeight));
            }
        }
        g.dispose();
        Graphics2D tg = AeroPaint.textG2(g0); super.paint(tg, c); tg.dispose();
    }

    @Override protected void paintContentBorder(Graphics g0, int p, int sel) {
        Graphics2D g = AeroPaint.g2(g0); int width = tabPane.getWidth(), height = tabPane.getHeight(); Insets in = tabPane.getInsets();
        int x = in.left, y = in.top, w = width - in.right - in.left, h = height - in.top - in.bottom;
        switch (p) {
            case LEFT: x += calculateTabAreaWidth(p, runCount, maxTabWidth); w -= (x - in.left); break;
            case RIGHT: w -= calculateTabAreaWidth(p, runCount, maxTabWidth); break;
            case BOTTOM: h -= calculateTabAreaHeight(p, runCount, maxTabHeight); break;
            default: y += calculateTabAreaHeight(p, runCount, maxTabHeight); h -= (y - in.top);
        }
        if (tabPane.getTabCount() == 0) { g.dispose(); return; }
        g.setColor(tabPane.isOpaque() || tabPane.getBackground() instanceof UIResource == false ? bodyColor() : AeroPalette.TAB_BODY);
        if (!(tabPane.getBackground() instanceof UIResource) || tabPane.isOpaque()) g.fillRect(x, y, w, h); else { g.setColor(AeroPalette.TAB_BODY); g.fillRect(x, y, w, h); }
        g.setColor(AeroPalette.STRIP_BORDER); g.drawRect(x, y, w - 1, h - 1);
        g.dispose();
    }
    private Color bodyColor() { return tabPane.getBackground() instanceof UIResource ? AeroPalette.TAB_BODY : tabPane.getBackground(); }

    @Override protected void paintTabArea(Graphics g, int p, int sel) { super.paintTabArea(g, p, sel); }

    private interface Canon { void run(Graphics2D g, int w, int h); }
    private void oriented(Graphics g0, int p, int x, int y, int w, int h, Canon c) {
        Graphics2D g = AeroPaint.g2(g0);
        switch (p) {
            case BOTTOM: g.translate(x, y + h); g.scale(1, -1); c.run(g, w, h); break;
            case LEFT: g.translate(x, y + h); g.rotate(-Math.PI / 2); c.run(g, h, w); break;
            case RIGHT: g.translate(x + w, y); g.rotate(Math.PI / 2); c.run(g, h, w); break;
            default: g.translate(x, y); c.run(g, w, h);
        }
        g.dispose();
    }
    private static Shape topShape(float w, float h, float r) {
        Path2D.Float s = new Path2D.Float(); s.moveTo(0, h); s.lineTo(0, r); s.quadTo(0, 0, r, 0); s.lineTo(w - r, 0); s.quadTo(w, 0, w, r); s.lineTo(w, h); s.closePath(); return s;
    }

    @Override protected void paintTabBackground(Graphics g, int p, int idx, int x, int y, int w, int h, boolean sel) {
        boolean en = tabPane.isEnabledAt(idx), hot = getRolloverTab() == idx && !sel && en;
        oriented(g, p, x, y, w, h, (g2, cw, ch) -> {
            if (sel) {
                g2.setPaint(AeroPaint.vgrad(0, ch, new Gloss(new float[]{0, .4f, 1}, AeroPalette.ACT0, AeroPalette.ACT1, AeroPalette.ACT2))); g2.fill(topShape(cw, ch + 1, 3));
                g2.setColor(AeroPaint.alpha(Color.WHITE, .9));
            } else {
                g2.setPaint(AeroPaint.vgrad(0, ch, Gloss.of2(AeroPalette.TAB0, AeroPalette.TAB1))); g2.fill(topShape(cw, ch, 3));
                g2.setColor(AeroPaint.alpha(Color.WHITE, .8)); g2.fillRect(1, 1, cw - 2, 1);
                if (hot) { g2.setColor(AeroPalette.TAB_HOT); g2.fill(topShape(cw, ch, 3)); }
            }
        });
    }
    @Override protected void paintTabBorder(Graphics g, int p, int idx, int x, int y, int w, int h, boolean sel) {
        oriented(g, p, x, y, w, h, (g2, cw, ch) -> {
            g2.setColor(sel ? AeroPalette.ACT_BORDER : AeroPalette.TAB_BORDER); g2.setStroke(new BasicStroke(1f));
            Path2D.Float s = new Path2D.Float(); float r = 2.5f; s.moveTo(.5f, ch + (sel ? 1 : 0)); s.lineTo(.5f, r + .5f); s.quadTo(.5f, .5f, r + .5f, .5f); s.lineTo(cw - r - .5f, .5f); s.quadTo(cw - .5f, .5f, cw - .5f, r + .5f); s.lineTo(cw - .5f, ch + (sel ? 1 : 0));
            g2.draw(s);
            if (!sel) { g2.setColor(AeroPalette.STRIP_BORDER); g2.fillRect(0, ch - 1, cw, 1); }
        });
    }
    @Override protected void paintFocusIndicator(Graphics g, int p, Rectangle[] rects, int idx, Rectangle iconRect, Rectangle textRect, boolean sel) {
        if (!tabPane.hasFocus() || !sel) return;
        Graphics2D g2 = (Graphics2D) g.create(); g2.setColor(new Color(0x7a8ea6)); g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{1f, 1f}, 0));
        g2.drawRect(textRect.x - 2, textRect.y, textRect.width + 3, textRect.height); g2.dispose();
    }
    @Override protected void paintText(Graphics g0, int p, Font font, FontMetrics metrics, int idx, String title, Rectangle textRect, boolean sel) {
        Graphics2D g = AeroPaint.g2(g0); boolean en = tabPane.isEnabledAt(idx);
        Font f = sel ? AeroFonts.bold(font.getSize2D()) : font; g.setFont(f); FontMetrics fm = g.getFontMetrics();
        g.setColor(!en ? AeroPalette.TEXT_FAINT : sel ? AeroPalette.ACT_FG : AeroPalette.TAB_FG);
        int cx = textRect.x + (textRect.width - fm.stringWidth(title)) / 2;
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, tabPane.getDisplayedMnemonicIndexAt(idx), cx, textRect.y + fm.getAscent());
        g.dispose();
    }
}
