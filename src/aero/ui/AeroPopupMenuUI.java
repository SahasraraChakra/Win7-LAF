package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicPopupMenuUI;

/** Menu popup: white-to-pale gradient, #769ec2 outline, inner white ring. */
public class AeroPopupMenuUI extends BasicPopupMenuUI {
    public static ComponentUI createUI(JComponent c) { return new AeroPopupMenuUI(); }
    @Override public void update(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.g2(g); g2.setPaint(AeroPaint.vgrad(0, c.getHeight(), Gloss.of2(AeroPalette.POP0, AeroPalette.POP1))); g2.fillRect(0, 0, c.getWidth(), c.getHeight()); g2.dispose();
        paint(g, c);
    }
}
