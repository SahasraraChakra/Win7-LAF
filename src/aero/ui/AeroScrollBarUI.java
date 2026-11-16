package aero.ui;

import aero.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

/** Compact 7px scrollbars (design: ::-webkit-scrollbar 7px) - pale-blue glossy thumb, no arrow buttons. */
public class AeroScrollBarUI extends BasicScrollBarUI {
    public static ComponentUI createUI(JComponent c) { return new AeroScrollBarUI(); }
    @Override protected JButton createDecreaseButton(int o) { return zero(); }
    @Override protected JButton createIncreaseButton(int o) { return zero(); }
    private static JButton zero() { JButton b = new JButton() { @Override public Dimension getPreferredSize() { return new Dimension(0, 0); } }; b.setFocusable(false); b.setBorder(null); b.setOpaque(false); return b; }
    @Override protected void installDefaults() { super.installDefaults(); scrollbar.setOpaque(false); }
    @Override public Dimension getPreferredSize(JComponent c) { int w = UIManager.getInt("ScrollBar.width"); if (w <= 0) w = 7; return scrollbar.getOrientation() == Adjustable.VERTICAL ? new Dimension(w, 48) : new Dimension(48, w); }
    @Override protected Dimension getMinimumThumbSize() { return new Dimension(18, 18); }
    @Override protected void paintTrack(Graphics g0, JComponent c, Rectangle r) {
        Graphics2D g = AeroPaint.g2(g0); g.setColor(AeroPalette.SB_TRACK); g.fillRect(r.x, r.y, r.width, r.height);
        g.setColor(AeroPalette.SB_TRACK_BORDER); g.drawRect(r.x, r.y, r.width - 1, r.height - 1); g.dispose();
    }
    @Override protected void paintThumb(Graphics g0, JComponent c, Rectangle r) {
        if (r.isEmpty() || !scrollbar.isEnabled()) return;
        Graphics2D g = AeroPaint.g2(g0); boolean hot = isThumbRollover() || isDragging;
        Color a = hot ? AeroPalette.SB_H0 : AeroPalette.SB0, b = hot ? AeroPalette.SB_H1 : AeroPalette.SB1, bd = hot ? AeroPalette.SB_H_BORDER : AeroPalette.SB_BORDER;
        g.translate(r.x, r.y);
        g.setPaint(AeroPaint.vgrad(0, r.height, AeroPaint.Gloss.of2(a, b))); g.fill(AeroPaint.rr(0, 0, r.width, r.height, 2));
        AeroPaint.border(g, 0, 0, r.width, r.height, 2, bd); g.dispose();
    }
}
