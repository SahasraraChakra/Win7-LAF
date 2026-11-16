package aero.ui;

import aero.*;
import aero.AeroButtonStyle.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import javax.swing.*;

/** Caption buttons. Window size: minimise/maximise 28x19, close 44x21 (design). "mini" = internal-frame size (16x14 / 20x14). */
public class AeroCaptionButton extends JButton {
    public enum Kind { MIN, MAX, RESTORE, CLOSE }
    private Kind kind; private final boolean mini;
    public AeroCaptionButton(Kind k, boolean mini, String tip) {
        kind = k; this.mini = mini; setToolTipText(tip); setFocusable(false); setBorder(null); setOpaque(false); setContentAreaFilled(false); setRolloverEnabled(true);
        putClientProperty("JButton.noMinHeight", "true");
        getAccessibleContext().setAccessibleName(tip);
    }
    public void setKind(Kind k) { kind = k; repaint(); }
    @Override public Dimension getPreferredSize() {
        if (mini) return kind == Kind.CLOSE ? new Dimension(20, 14) : new Dimension(16, 14);
        return kind == Kind.CLOSE ? new Dimension(44, 21) : new Dimension(28, 19);
    }
    @Override public Dimension getMinimumSize() { return getPreferredSize(); }
    @Override public Dimension getMaximumSize() { return getPreferredSize(); }
    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(); ButtonModel m = getModel();
        State st = (m.isPressed() && m.isArmed()) ? State.PRESSED : m.isRollover() ? State.HOVER : State.NORMAL;
        boolean close = kind == Kind.CLOSE;
        AeroButtonStyle.Spec sp = (close ? AeroButtonStyle.CLOSE : AeroButtonStyle.CAPTION).spec(st);
        if (!close && mini && st == State.NORMAL) { g.setColor(AeroPaint.alpha(Color.WHITE, .7)); g.fill(AeroPaint.rr(0, 0, w, h, 2)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.TEXT_FAINT); }
        else if (sp.gloss != null) { g.setPaint(AeroPaint.vgrad(0, h, sp.gloss)); g.fill(AeroPaint.rr(0, 0, w, h, 2)); if (sp.inset != null) AeroPaint.insetTop(g, 0, 0, w, sp.inset, 2); AeroPaint.border(g, 0, 0, w, h, 2, sp.border); }
        Color fg = close ? Color.WHITE : AeroPalette.TEXT;
        if (mini) {
            g.setColor(fg); g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); float cx = w / 2f, cy = h / 2f;
            switch (kind) {
                case MIN: g.drawLine(Math.round(cx - 3), Math.round(cy + 3), Math.round(cx + 3), Math.round(cy + 3)); break;
                case MAX: g.draw(new java.awt.geom.Rectangle2D.Float(cx - 3, cy - 3, 6, 6)); break;
                case RESTORE: g.draw(new java.awt.geom.Rectangle2D.Float(cx - 3, cy - 1, 5, 5)); g.drawLine(Math.round(cx - 1), Math.round(cy - 3), Math.round(cx + 3), Math.round(cy - 3)); g.drawLine(Math.round(cx + 3), Math.round(cy - 3), Math.round(cx + 3), Math.round(cy + 2)); break;
                default: g.drawLine(Math.round(cx - 2.5f), Math.round(cy - 2.5f), Math.round(cx + 2.5f), Math.round(cy + 2.5f)); g.drawLine(Math.round(cx + 2.5f), Math.round(cy - 2.5f), Math.round(cx - 2.5f), Math.round(cy + 2.5f));
            }
        } else {
            Icon ic = AeroIcons.captionGlyph(kind == Kind.MIN ? "min" : kind == Kind.MAX ? "max" : kind == Kind.RESTORE ? "restore" : "close", fg);
            if (close) { Icon sh = AeroIcons.lucide("x", 13, new Color(0, 0, 0, 80), 2.5f); sh.paintIcon(this, g, (w - 13) / 2, (h - 13) / 2 + 1); }
            ic.paintIcon(this, g, (w - ic.getIconWidth()) / 2, (h - ic.getIconHeight()) / 2);
        }
        g.dispose();
    }
}
