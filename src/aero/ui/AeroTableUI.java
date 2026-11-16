package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTableUI;

/** Tables: striped rows, row hover, glossy blue selection with 1px outline (design JTable). */
public class AeroTableUI extends BasicTableUI {
    public static ComponentUI createUI(JComponent c) { return new AeroTableUI(); }
    private MouseAdapter hover;

    @Override protected void installDefaults() {
        super.installDefaults();
        if (table.getBackground() instanceof UIResource) table.setBackground(new Color(0, 0, 0, 0));
        if (table.getSelectionBackground() instanceof UIResource) table.setSelectionBackground(new Color(0, 0, 0, 0));
        table.setSelectionForeground(AeroPalette.SEL_FG); table.setGridColor(AeroPalette.GRID); table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1)); table.setRowHeight(AeroPaint.scaleH(22)); table.setFillsViewportHeight(true);
    }
    @Override protected void installListeners() {
        super.installListeners();
        hover = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { set(table.rowAtPoint(e.getPoint())); }
            @Override public void mouseExited(MouseEvent e) { set(-1); }
            void set(int r) { Object o = table.getClientProperty("Aero.rolloverRow"); int old = o instanceof Integer ? (Integer) o : -1; if (old != r) { table.putClientProperty("Aero.rolloverRow", r < 0 ? null : r); table.repaint(); } }
        };
        table.addMouseMotionListener(hover); table.addMouseListener(hover);
    }
    @Override protected void uninstallListeners() { table.removeMouseMotionListener(hover); table.removeMouseListener(hover); super.uninstallListeners(); }

    @Override public void paint(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); Rectangle clip = g.getClipBounds(); int w = table.getWidth();
        boolean userBg = !(table.getBackground() instanceof UIResource) && table.getBackground().getAlpha() > 0;
        g.setColor(userBg ? table.getBackground() : Color.WHITE); g.fillRect(clip.x, clip.y, clip.width, clip.height);
        int rows = table.getRowCount();
        if (rows > 0) {
            int rMin = Math.max(0, table.rowAtPoint(new Point(0, clip.y))), rMax = table.rowAtPoint(new Point(0, clip.y + clip.height - 1));
            if (rMax < 0) rMax = rows - 1; if (rMin < 0) rMin = 0;
            Object ho = table.getClientProperty("Aero.rolloverRow"); int hot = ho instanceof Integer ? (Integer) ho : -1;
            boolean rowSel = table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed();
            for (int r = rMin; r <= rMax; r++) {
                Rectangle rr = table.getCellRect(r, 0, true); rr.width = w;
                boolean sel = table.isRowSelected(r) && rowSel;
                if (sel) {
                    g.setPaint(AeroPaint.vgrad(rr.y, rr.height, Gloss.of2(AeroPalette.SEL0, AeroPalette.SEL1))); g.fillRect(rr.x, rr.y, rr.width, rr.height);
                    g.setColor(AeroPalette.SEL_OUTLINE);
                    if (r == 0 || !table.isRowSelected(r - 1)) g.fillRect(rr.x, rr.y, rr.width, 1);
                    if (r == rows - 1 || !table.isRowSelected(r + 1)) g.fillRect(rr.x, rr.y + rr.height - 1, rr.width, 1);
                    g.fillRect(rr.x, rr.y, 1, rr.height); g.fillRect(rr.x + rr.width - 1, rr.y, 1, rr.height);
                } else if (r == hot) { g.setColor(AeroPalette.ROW_HOT); g.fillRect(rr.x, rr.y, rr.width, rr.height); }
                else if ((r & 1) == 1 && !userBg) { g.setColor(AeroPalette.ROW_ALT); g.fillRect(rr.x, rr.y, rr.width, rr.height); }
            }
            if (!rowSel) { // cell / column selection
                for (int r = rMin; r <= rMax; r++) for (int col = 0; col < table.getColumnCount(); col++) if (table.isCellSelected(r, col)) {
                    Rectangle cr = table.getCellRect(r, col, true); g.setPaint(AeroPaint.vgrad(cr.y, cr.height, Gloss.of2(AeroPalette.SEL0, AeroPalette.SEL1))); g.fillRect(cr.x, cr.y, cr.width, cr.height);
                }
            }
        }
        g.dispose();
        Graphics2D tg = AeroPaint.textG2(g0); super.paint(tg, c); tg.dispose();
    }
}
