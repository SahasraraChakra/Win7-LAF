package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicToolTipUI;

public class AeroToolTipUI extends BasicToolTipUI {
    private static final AeroToolTipUI INSTANCE = new AeroToolTipUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.g2(g); g2.setColor(AeroPalette.TIP_BG); g2.fill(AeroPaint.rr(0, 0, c.getWidth(), c.getHeight(), 2)); g2.dispose();
        Graphics2D tg = AeroPaint.textG2(g); super.paint(tg, c); tg.dispose();
    }
    @Override public Dimension getPreferredSize(JComponent c) { Dimension d = super.getPreferredSize(c); return d; }
}
