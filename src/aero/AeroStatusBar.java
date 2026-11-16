package aero;

import java.awt.*;
import javax.swing.*;

/** Status bar (design footer): glossy grey strip, 23px high, left items and right items separated by "|". */
public class AeroStatusBar extends JPanel {
    private final JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0)), right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    public AeroStatusBar() {
        super(new BorderLayout()); putClientProperty("JPanel.style", "statusbar"); setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        for (JPanel p : new JPanel[]{left, right}) p.setOpaque(false);
        add(left, BorderLayout.WEST); add(right, BorderLayout.EAST);
    }
    @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); d.height = AeroPaint.scaleH(23); return d; }
    public static JLabel text(String s) { JLabel l = new JLabel(s); l.setFont(AeroFonts.plain(10f)); l.setForeground(AeroPalette.TEXT_DIM); return l; }
    public static JLabel separator() { JLabel l = new JLabel("|"); l.setFont(AeroFonts.plain(10f)); l.setForeground(AeroPalette.TEXT_FAINT); return l; }
    /** Small status dot with a glow, e.g. green "Ready". */
    public static JLabel dot(Color c, String text) {
        JLabel l = new JLabel(text) { };
        l.setIcon(new Icon() {
            public int getIconWidth() { return 8; } public int getIconHeight() { return 8; }
            public void paintIcon(Component cc, Graphics g0, int x, int y) { Graphics2D g = AeroPaint.g2(g0); g.setColor(AeroPaint.alpha(c, .35)); g.fill(new java.awt.geom.Ellipse2D.Float(x - 2, y - 2, 12, 12)); g.setColor(c); g.fill(new java.awt.geom.Ellipse2D.Float(x, y, 8, 8)); g.dispose(); } });
        l.setIconTextGap(6); l.setFont(AeroFonts.bold(10f)); l.setForeground(AeroPalette.TEXT); return l;
    }
    public AeroStatusBar addLeft(Component c) { left.add(c); return this; }
    public AeroStatusBar addRight(Component c) { right.add(c); return this; }
}
