package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicMenuBarUI;

public class AeroMenuBarUI extends BasicMenuBarUI {
    public static ComponentUI createUI(JComponent c) { return new AeroMenuBarUI(); }
    @Override public void update(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.g2(g); int w = c.getWidth(), h = c.getHeight();
        g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of2(AeroPalette.MB0, AeroPalette.MB1))); g2.fillRect(0, 0, w, h);
        g2.setColor(AeroPaint.alpha(Color.WHITE, .9)); g2.fillRect(0, 0, w, 1); g2.setColor(AeroPalette.MB_BORDER); g2.fillRect(0, h - 1, w, 1); g2.dispose();
        paint(g, c);
    }
}
