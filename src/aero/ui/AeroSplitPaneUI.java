package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Split pane with the 8px glossy divider, dotted grip and one-touch arrows of the design. */
public class AeroSplitPaneUI extends BasicSplitPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroSplitPaneUI(); }
    @Override public BasicSplitPaneDivider createDefaultDivider() { return new Divider(this); }

    static final class Divider extends BasicSplitPaneDivider {
        Divider(BasicSplitPaneUI ui) { super(ui); setBorder(null); setLayout(new LayoutManager() {
            public void addLayoutComponent(String n, Component c) {} public void removeLayoutComponent(Component c) {}
            public Dimension preferredLayoutSize(Container p) { return new Dimension(8, 8); } public Dimension minimumLayoutSize(Container p) { return new Dimension(1, 1); }
            public void layoutContainer(Container p) {
                boolean hz = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT; int w = p.getWidth(), h = p.getHeight();
                if (leftButton == null || rightButton == null) return;
                if (hz) { int bw = 8, bh = 10, cy = h / 2; leftButton.setBounds((w - bw) / 2, cy - 9 - bh - 2, bw, bh); rightButton.setBounds((w - bw) / 2, cy + 9 + 2, bw, bh); }
                else { int bw = 10, bh = 8, cx = w / 2; leftButton.setBounds(cx - 9 - bw - 2, (h - bh) / 2, bw, bh); rightButton.setBounds(cx + 9 + 2, (h - bh) / 2, bw, bh); }
            } }); }
        @Override protected JButton createLeftOneTouchButton() { return new Arrow(true); }
        @Override protected JButton createRightOneTouchButton() { return new Arrow(false); }
        @Override public void paint(Graphics g0) {
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(); boolean hz = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
            Gloss gl = new Gloss(new float[]{0, .5f, 1}, AeroPalette.SP0, AeroPalette.SP1, AeroPalette.SP2);
            if (hz) {
                g.setPaint(AeroPaint.vgrad(0, h, gl)); g.fillRect(0, 0, w, h); g.setColor(AeroPalette.SP_BORDER); g.fillRect(0, 0, 1, h); g.fillRect(w - 1, 0, 1, h); g.setColor(Color.WHITE); g.fillRect(1, 0, 1, h);
                AeroPaint.dots(g, (w - 3) / 2, h / 2 - 9, 3, 18, AeroPalette.SP_DOTS, 1, 4); AeroPaint.dots(g, (w - 3) / 2 + 1, h / 2 - 9, 1, 18, AeroPalette.SP_DOTS, 1, 4);
                g.setColor(AeroPalette.SP_DOTS); for (int y = h / 2 - 9; y < h / 2 + 9; y += 4) { g.fillRect(w / 2 - 1, y, 3, 1); }
            } else {
                g.setPaint(AeroPaint.hgrad(0, w, gl)); g.fillRect(0, 0, w, h); g.setColor(AeroPalette.SP_BORDER); g.fillRect(0, 0, w, 1); g.fillRect(0, h - 1, w, 1); g.setColor(Color.WHITE); g.fillRect(0, 1, w, 1);
                g.setColor(AeroPalette.SP_DOTS); for (int x = w / 2 - 9; x < w / 2 + 9; x += 4) g.fillRect(x, h / 2 - 1, 1, 3);
            }
            g.dispose(); super.paint(g0);
        }
        private final class Arrow extends JButton {
            final boolean first; Arrow(boolean f) { first = f; setBorder(null); setFocusable(false); setOpaque(false); setContentAreaFilled(false); setRolloverEnabled(true); setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); }
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = AeroPaint.g2(g0); boolean hz = splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT;
                int dir = hz ? (first ? SwingConstants.WEST : SwingConstants.EAST) : (first ? SwingConstants.NORTH : SwingConstants.SOUTH);
                int tw = hz ? 4 : 7, th = hz ? 7 : 4;
                AeroIcons.triangle(dir, tw, th, getModel().isRollover() ? AeroPalette.ARROW_BLUE : AeroPalette.TEXT_2).paintIcon(this, g, (getWidth() - tw) / 2, (getHeight() - th) / 2); g.dispose();
            }
        }
    }
}
