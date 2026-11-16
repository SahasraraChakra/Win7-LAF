package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicLabelUI;

/** Labels. Optional client property "JLabel.style": title, subtitle, muted, error, success, warning, link, legend, mono, badge. */
public class AeroLabelUI extends BasicLabelUI {
    private static final AeroLabelUI INSTANCE = new AeroLabelUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override protected void installDefaults(JLabel c) { super.installDefaults(c); }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.textG2(g);
        Object st = c.getClientProperty("JLabel.style");
        if (st != null && (c.getFont() instanceof UIResource)) {
            switch (st.toString()) {
                case "title": c.setFont(AeroFonts.bold(13f)); break;
                case "subtitle": c.setFont(AeroFonts.semibold(11f)); break;
                case "legend": c.setFont(AeroFonts.bold(10.5f)); break;
                case "mono": c.setFont(AeroFonts.mono(11f)); break;
                default: break;
            }
        }
        if (st != null && (c.getForeground() instanceof UIResource)) {
            switch (st.toString()) {
                case "muted": c.setForeground(AeroPalette.TEXT_MUTED); break;
                case "error": c.setForeground(new Color(0xb91c1c)); break;
                case "success": c.setForeground(new Color(0x047857)); break;
                case "warning": c.setForeground(new Color(0xb45309)); break;
                case "legend": c.setForeground(AeroPalette.GROUP_TITLE); break;
                case "link": c.setForeground(AeroPalette.c(0x1d4ed8)); break;
                default: break;
            }
        }
        super.paint(g2, c); g2.dispose();
    }
    @Override protected void paintDisabledText(JLabel l, Graphics g, String s, int x, int y) {
        g.setColor(AeroPalette.TEXT_FAINT); javax.swing.plaf.basic.BasicGraphicsUtils.drawStringUnderlineCharAt(g, s, l.getDisplayedMnemonicIndex(), x, y);
    }
}
