package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;

/** JList renderer: blue rounded selection (white semibold text), hover highlight; menu-style highlight inside combo popups. */
public class AeroListCellRenderer extends DefaultListCellRenderer implements UIResource {
    private boolean sel, hot, popup;
    public AeroListCellRenderer() { setOpaque(false); setBorder(new EmptyBorder(3, 6, 3, 6)); }
    @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, false, false);
        popup = Boolean.TRUE.equals(list.getClientProperty("Aero.popupList"));
        sel = isSelected; Object r = list.getClientProperty("Aero.rolloverRow"); hot = r instanceof Integer && (Integer) r == index && !isSelected;
        setOpaque(false);
        Color fg = list.getForeground();
        if (sel) { fg = popup ? AeroPalette.MI_HOT_FG : Color.WHITE; setFont(AeroFonts.weight(list.getFont(), popup ? 500 : 600)); } else setFont(list.getFont());
        if (!list.isEnabled()) fg = AeroPalette.TEXT_FAINT;
        setForeground(fg);
        return this;
    }
    @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); d.height = Math.max(d.height, 18); return d; }
    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
        int pad = popup ? 0 : 0;
        if (sel) {
            if (popup) { AeroPaint.fillGloss(g, 0, 0, w, h, 2, AeroPaint.Gloss.of2(AeroPalette.MI_HOT0, AeroPalette.MI_HOT1)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.MI_HOT_BORDER); }
            else { g.setColor(AeroPalette.LIST_SEL); g.fill(AeroPaint.rr(0, 1, w, h - 2, 2)); }
        } else if (hot) {
            if (popup) { AeroPaint.fillGloss(g, 0, 0, w, h, 2, AeroPaint.Gloss.of2(AeroPalette.MI_HOT0, AeroPalette.MI_HOT1)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.MI_HOT_BORDER); }
            else { g.setColor(AeroPalette.LIST_HOT); g.fill(AeroPaint.rr(0, 1, w, h - 2, 2)); }
        }
        g.dispose(); super.paintComponent(g0);
    }
}
