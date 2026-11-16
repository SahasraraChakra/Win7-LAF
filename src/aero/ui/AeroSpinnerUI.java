package aero.ui;

import aero.*;
import aero.AeroButtonStyle.State;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicSpinnerUI;

/** Spinners: input field + two stacked 14px glossy arrow buttons (design "JSpinner"). */
public class AeroSpinnerUI extends BasicSpinnerUI {
    public static ComponentUI createUI(JComponent c) { return new AeroSpinnerUI(); }
    @Override protected void installDefaults() { super.installDefaults(); spinner.setOpaque(false); configure(spinner.getEditor()); }
    @Override protected Component createNextButton() { JButton b = new Arrow(true); b.setName("Spinner.nextButton"); installNextButtonListeners(b); return b; }
    @Override protected Component createPreviousButton() { JButton b = new Arrow(false); b.setName("Spinner.previousButton"); installPreviousButtonListeners(b); return b; }
    @Override protected void replaceEditor(JComponent oldEditor, JComponent newEditor) { super.replaceEditor(oldEditor, newEditor); configure(newEditor); }
    private void configure(JComponent ed) {
        if (ed instanceof JSpinner.DefaultEditor) {
            JFormattedTextField f = ((JSpinner.DefaultEditor) ed).getTextField(); f.setBorder(new EmptyBorder(0, 5, 0, 5)); f.setOpaque(false);
            if (f.getClientProperty("Aero.spFocus") == null) { f.putClientProperty("Aero.spFocus", true);
                f.addFocusListener(new FocusAdapter() {
                    @Override public void focusGained(FocusEvent e) { spinner.putClientProperty("Aero.focusWithin", true); spinner.repaint(); }
                    @Override public void focusLost(FocusEvent e) { spinner.putClientProperty("Aero.focusWithin", null); spinner.repaint(); } }); }
        }
        if (ed != null) ed.setOpaque(false);
    }
    @Override public void paint(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); g.setColor(c.isEnabled() ? Color.WHITE : new Color(0xf3f4f6)); g.fill(AeroPaint.rr(0, 0, c.getWidth(), c.getHeight(), 2)); g.dispose();
    }
    @Override public Dimension getPreferredSize(JComponent c) { Dimension d = super.getPreferredSize(c); if (d == null) d = c.getLayout().preferredLayoutSize(c); d = new Dimension(d); d.height = Math.max(d.height, AeroPaint.scaleH(23)); return d; }

    private static final class Arrow extends JButton implements UIResource {
        final boolean up;
        Arrow(boolean up) { this.up = up; setRolloverEnabled(true); setFocusable(false); setBorder(null); setOpaque(false); setContentAreaFilled(false); }
        @Override public Dimension getPreferredSize() { return new Dimension(14, 11); }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(); ButtonModel m = getModel();
            State st = !isEnabled() ? State.DISABLED : (m.isPressed() && m.isArmed()) ? State.PRESSED : m.isRollover() ? State.HOVER : State.NORMAL;
            AeroButtonStyle.Spec sp = AeroButtonStyle.STANDARD.spec(st);
            g.setPaint(AeroPaint.vgrad(0, h, sp.gloss)); g.fillRect(0, 0, w, h);
            if (sp.inset != null && up) { g.setColor(sp.inset); g.fillRect(1, 1, w - 2, 1); }
            g.setColor(sp.border); g.drawRect(0, up ? 0 : -1, w - 1, up ? h - 1 : h);
            AeroIcons.triangle(up ? SwingConstants.NORTH : SwingConstants.SOUTH, 5, 3, isEnabled() ? AeroPalette.TEXT_DIM : AeroPalette.TEXT_FAINT).paintIcon(this, g, (w - 5) / 2, (h - 3) / 2 + (up ? 0 : 0));
            g.dispose();
        }
    }
}
