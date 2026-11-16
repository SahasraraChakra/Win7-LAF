package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.util.List;

public class AeroTableHeaderUI extends BasicTableHeaderUI {
    public static ComponentUI createUI(JComponent c) { return new AeroTableHeaderUI(); }
    @Override protected void installDefaults() {
        super.installDefaults();
        if (header.getDefaultRenderer() instanceof UIResource || header.getDefaultRenderer() == null) header.setDefaultRenderer(new HeaderRenderer());
        header.setOpaque(false);
    }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.g2(g); int w = c.getWidth(), h = c.getHeight();
        g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of4(AeroPalette.TH0, AeroPalette.TH1, AeroPalette.TH2, AeroPalette.TH3))); g2.fillRect(0, 0, w, h); g2.dispose();
        super.paint(g, c);
    }

    /** Aero ListView header cell: glossy gradient, uppercase bold label, 1px separators, sort arrow. */
    public static class HeaderRenderer extends DefaultTableCellRenderer implements UIResource {
        private int sort; // 1 asc, -1 desc
        public HeaderRenderer() { setOpaque(false); setHorizontalAlignment(LEADING); }
        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
            String s = v == null ? "" : v.toString();
            if (Boolean.TRUE.equals(UIManager.get("Aero.TableHeader.uppercase"))) s = s.toUpperCase();
            super.getTableCellRendererComponent(t, s, false, false, row, col);
            setFont(AeroFonts.bold(11f)); setForeground(AeroPalette.TEXT); setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            sort = 0;
            if (t != null && t.getRowSorter() != null) {
                List<? extends RowSorter.SortKey> keys = t.getRowSorter().getSortKeys(); int mc = t.convertColumnIndexToModel(col);
                if (!keys.isEmpty() && keys.get(0).getColumn() == mc) { SortOrder o = keys.get(0).getSortOrder(); sort = o == SortOrder.ASCENDING ? 1 : o == SortOrder.DESCENDING ? -1 : 0; }
            }
            if (sort != 0) setIcon(null);
            return this;
        }
        @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); d.height = Math.max(d.height, AeroPaint.scaleH(23)); if (sort != 0) d.width += 12; return d; }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
            g.setPaint(AeroPaint.vgrad(0, h, Gloss.of4(AeroPalette.TH0, AeroPalette.TH1, AeroPalette.TH2, AeroPalette.TH3))); g.fillRect(0, 0, w, h);
            g.setColor(AeroPalette.TH_BOTTOM); g.fillRect(0, h - 1, w, 1); g.setColor(AeroPalette.TH_RIGHT); g.fillRect(w - 1, 0, 1, h - 1);
            g.setFont(getFont()); FontMetrics fm = g.getFontMetrics(); String s = getText(); Insets in = getInsets();
            int tw = fm.stringWidth(s) + (s.length() > 1 ? (int) (s.length() * 0.33f) : 0);
            int x = getHorizontalAlignment() == RIGHT ? w - in.right - tw - (sort != 0 ? 12 : 0) : getHorizontalAlignment() == CENTER ? (w - tw) / 2 : in.left;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g.setColor(AeroPaint.alpha(Color.WHITE, .8)); drawSpaced(g, s, x, y + 1, .33f);
            g.setColor(getForeground()); drawSpaced(g, s, x, y, .33f);
            if (sort != 0) AeroIcons.triangle(sort > 0 ? SwingConstants.NORTH : SwingConstants.SOUTH, 7, 4, AeroPalette.TEXT_MUTED).paintIcon(this, g, w - in.right - 6, (h - 4) / 2);
            g.dispose();
        }
        private static void drawSpaced(Graphics2D g, String s, int x, int y, float spacing) {
            // letter-spacing: .03em
            float cx = x; FontMetrics fm = g.getFontMetrics();
            for (int i = 0; i < s.length(); i++) { String ch = s.substring(i, i + 1); g.drawString(ch, cx, y); cx += fm.stringWidth(ch) + spacing; }
        }
    }
}
