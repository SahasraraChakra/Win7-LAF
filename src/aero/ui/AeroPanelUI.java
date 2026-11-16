package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * Panels. "JPanel.style" client property gives the ready-made Aero surfaces:
 * ribbon (brand ribbon gradient), toolbar (secondary toolbar gradient), statusbar, glass (titlebar glass), group (white card),
 * tabbody, header (table header gradient), navy (desktop gradient).
 */
public class AeroPanelUI extends BasicPanelUI {
    private static final AeroPanelUI INSTANCE = new AeroPanelUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
    @Override public void update(Graphics g, JComponent c) {
        Object st = c.getClientProperty("JPanel.style");
        if (st == null || !c.isOpaque()) { super.update(g, c); return; }
        Graphics2D g2 = AeroPaint.g2(g); int w = c.getWidth(), h = c.getHeight();
        switch (st.toString()) {
            case "ribbon": g2.setPaint(AeroPaint.vgrad(0, h, new Gloss(new float[]{0, .45f, .5f, 1}, AeroPalette.RB0, AeroPalette.RB1, AeroPalette.RB2, AeroPalette.RB3))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.RB_BORDER); g2.fillRect(0, h - 1, w, 1); g2.setColor(Color.WHITE); g2.fillRect(0, 0, w, 1); break;
            case "toolbar": g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of3(AeroPalette.T2_0, .5f, AeroPalette.T2_1, AeroPalette.T2_2))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.T2_BORDER); g2.fillRect(0, h - 1, w, 1); g2.setColor(Color.WHITE); g2.fillRect(0, 0, w, 1); break;
            case "statusbar": g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of4(AeroPalette.ST0, AeroPalette.ST1, AeroPalette.ST2, AeroPalette.ST3))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.ST_BORDER); g2.fillRect(0, 0, w, 1); g2.setColor(Color.WHITE); g2.fillRect(0, 1, w, 1); break;
            case "glass": g2.setColor(Color.WHITE); g2.fillRect(0, 0, w, h); g2.setPaint(AeroPaint.vgrad(0, h, new Gloss(new float[]{0, .42f, .43f, 1}, AeroPalette.TB0, AeroPalette.TB1, AeroPalette.TB2, AeroPalette.TB3))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.TB_BORDER); g2.fillRect(0, h - 1, w, 1); break;
            case "header": g2.setPaint(AeroPaint.vgrad(0, h, Gloss.of4(AeroPalette.TH0, AeroPalette.TH1, AeroPalette.TH2, AeroPalette.TH3))); g2.fillRect(0, 0, w, h); g2.setColor(AeroPalette.TH_BOTTOM); g2.fillRect(0, h - 1, w, 1); break;
            case "group": g2.setColor(Color.WHITE); g2.fill(AeroPaint.rr(0, 0, w, h, 3)); AeroPaint.border(g2, 0, 0, w, h, 3, AeroPalette.GROUP_BORDER); break;
            case "tabbody": g2.setColor(AeroPalette.TAB_BODY); g2.fillRect(0, 0, w, h); break;
            case "navy": AeroDesktopPaneUI.paintDesktop(g2, w, h); break;
            default: super.update(g, c);
        }
        g2.dispose(); paint(g, c);
    }
}
