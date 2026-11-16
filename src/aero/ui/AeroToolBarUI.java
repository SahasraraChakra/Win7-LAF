package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicToolBarUI;

/** Glossy toolbars. "JToolBar.style": "ribbon" (36px brand ribbon gradient) or default "secondary" (30px). */
public class AeroToolBarUI extends BasicToolBarUI {
    public static ComponentUI createUI(JComponent c) { return new AeroToolBarUI(); }
    @Override protected void installDefaults() { super.installDefaults(); toolBar.setOpaque(true); toolBar.setFloatable(false); toolBar.setBorder(new TBorder()); }
    // keep Aero button borders (Basic would swap them for rollover/non-rollover borders)
    @Override protected void installRolloverBorders(JComponent c) { }
    @Override protected void installNonRolloverBorders(JComponent c) { }
    @Override protected void installNormalBorders(JComponent c) { }
    @Override protected void setBorderToRollover(Component c) { }
    @Override protected void setBorderToNonRollover(Component c) { }
    @Override protected void setBorderToNormal(Component c) { }
    @Override public void update(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.g2(g); int w = c.getWidth(), h = c.getHeight(); boolean ribbon = "ribbon".equals(String.valueOf(c.getClientProperty("JToolBar.style")));
        if (toolBar.getOrientation() == SwingConstants.HORIZONTAL) {
            if (ribbon) { g2.setPaint(AeroPaint.vgrad(0, h, new Gloss(new float[]{0, .45f, .5f, 1}, AeroPalette.RB0, AeroPalette.RB1, AeroPalette.RB2, AeroPalette.RB3))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.RB_BORDER); g2.fillRect(0, h - 1, w, 1); }
            else { g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of3(AeroPalette.T2_0, .5f, AeroPalette.T2_1, AeroPalette.T2_2))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.T2_BORDER); g2.fillRect(0, h - 1, w, 1); }
            g2.setColor(Color.WHITE); g2.fillRect(0, 0, w, 1);
        } else { g2.setPaint(AeroPaint.hgrad(0, w, Gloss.of3(AeroPalette.T2_0, .5f, AeroPalette.T2_1, AeroPalette.T2_2))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.T2_BORDER); g2.fillRect(w - 1, 0, 1, h); }
        g2.dispose(); paint(g, c);
    }
    static final class TBorder extends AbstractBorder implements UIResource {
        @Override public Insets getBorderInsets(Component c, Insets i) { boolean ribbon = c instanceof JComponent && "ribbon".equals(String.valueOf(((JComponent) c).getClientProperty("JToolBar.style"))); if (ribbon) i.set(6, 8, 6, 8); else i.set(3, 8, 4, 8); return i; }
    }
}
