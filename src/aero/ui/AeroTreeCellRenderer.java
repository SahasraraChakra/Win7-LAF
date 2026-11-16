package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.plaf.UIResource;

/** Tree node: light-blue rounded selection with border, bold blue text; hover tint. */
public class AeroTreeCellRenderer extends DefaultTreeCellRenderer implements UIResource {
    private boolean sel, hot;
    public AeroTreeCellRenderer() {
        setOpaque(false); setBackgroundSelectionColor(new Color(0, 0, 0, 0)); setBackgroundNonSelectionColor(new Color(0, 0, 0, 0)); setBorderSelectionColor(new Color(0, 0, 0, 0));
        setTextSelectionColor(AeroPalette.TREE_SEL_FG); setTextNonSelectionColor(AeroPalette.TEXT); setIconTextGap(4);
    }
    @Override public Component getTreeCellRendererComponent(JTree t, Object v, boolean s, boolean exp, boolean leaf, int row, boolean focus) {
        super.getTreeCellRendererComponent(t, v, s, exp, leaf, row, false);
        sel = s; Object r = t.getClientProperty("Aero.rolloverRow"); hot = r instanceof Integer && (Integer) r == row && !s;
        setOpaque(false); setFont(s ? AeroFonts.bold(t.getFont().getSize2D()) : t.getFont()); setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
        setForeground(!t.isEnabled() ? AeroPalette.TEXT_FAINT : s ? AeroPalette.TREE_SEL_FG : AeroPalette.TEXT);
        return this;
    }
    @Override public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize(); Font f = getFont(); if (!sel && f != null) { FontMetrics a = getFontMetrics(f), b = getFontMetrics(AeroFonts.bold(f.getSize2D())); d.width += Math.max(0, b.stringWidth(getText()) - a.stringWidth(getText())); }
        d.height = Math.max(d.height, 18); return d;
    }
    @Override public void paint(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
        if (sel) { g.setColor(AeroPalette.TREE_SEL_BG); g.fill(AeroPaint.rr(0, 0, w, h, 2)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.TREE_SEL_BORDER); }
        else if (hot) { g.setColor(AeroPalette.TREE_HOT); g.fill(AeroPaint.rr(0, 0, w, h, 2)); }
        g.dispose(); super.paint(g0);
    }
}
