package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class AeroCheckBoxUI extends BasicCheckBoxUI {
    private static final AeroCheckBoxUI INSTANCE = new AeroCheckBoxUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override protected void installDefaults(AbstractButton b) { super.installDefaults(b); b.setOpaque(false); b.setRolloverEnabled(true); }
    @Override public void update(Graphics g, JComponent c) { paint(g, c); }
    @Override protected void paintFocus(Graphics g, Rectangle r, Dimension d) {
        Graphics2D g2 = (Graphics2D) g.create(); g2.setColor(new Color(0x7a8ea6)); g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{1f, 1f}, 0));
        g2.drawRect(r.x - 1, r.y - 1, r.width + 1, r.height + 1); g2.dispose();
    }
    @Override public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = AeroPaint.textG2(g); super.paint(g2, c); g2.dispose();
    }
    @Override public Dimension getPreferredSize(JComponent c) { Dimension d = super.getPreferredSize(c); d.height = Math.max(d.height, 18); return d; }
}
