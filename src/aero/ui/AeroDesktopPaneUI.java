package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/** MDI workspace: navy radial gradient (circle at 50% 25%, #244b70 to #0d1e2e) with inner shadow. */
public class AeroDesktopPaneUI extends BasicDesktopPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroDesktopPaneUI(); }
    public static void paintDesktop(Graphics2D g, int w, int h) {
        float cx = w * .5f, cy = h * .25f; float r = (float) Math.hypot(Math.max(cx, w - cx), Math.max(cy, h - cy));
        g.setPaint(new RadialGradientPaint(cx, cy, Math.max(1, r), new float[]{0f, .9f, 1f}, new Color[]{AeroPalette.DESK0, AeroPalette.DESK1, AeroPalette.DESK1})); g.fillRect(0, 0, w, h);
        for (int i = 0; i < 6; i++) { g.setColor(new Color(0, 0, 0, 90 - i * 15)); g.fillRect(0, i, w, 1); }
        g.setColor(AeroPalette.DESK_BORDER); g.drawRect(0, 0, w - 1, h - 1);
    }
    @Override public void update(Graphics g, JComponent c) {
        if (c.isOpaque() && c.getBackground() instanceof UIResource) { Graphics2D g2 = AeroPaint.g2(g); paintDesktop(g2, c.getWidth(), c.getHeight()); g2.dispose(); paint(g, c); } else super.update(g, c);
    }
}
