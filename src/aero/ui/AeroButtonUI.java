package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class AeroButtonUI extends BasicButtonUI {
    public static ComponentUI createUI(JComponent c) { return new AeroButtonUI(); }
    @Override public void installUI(JComponent c) { super.installUI(c); AeroButtonPainter.installFader((AbstractButton) c); }
    @Override protected void installDefaults(AbstractButton b) { super.installDefaults(b); b.setOpaque(false); b.setRolloverEnabled(true); b.setFocusable(true); }
    @Override public void update(Graphics g, JComponent c) { paint(g, c); }
    @Override public void paint(Graphics g, JComponent c) { AeroButtonPainter.paint(g, c); }
    @Override public Dimension getPreferredSize(JComponent c) { return AeroButtonPainter.preferred(c, AeroPaint.scaleH(23)); }
    @Override public Dimension getMinimumSize(JComponent c) { return getPreferredSize(c); }
    @Override public Dimension getMaximumSize(JComponent c) { return getPreferredSize(c); }
}
