package aero.ui;

import aero.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

/** Painting + accessory behaviour shared by every text component UI. */
final class AeroTextSupport {
    private AeroTextSupport() {}

    static boolean boxed(JTextComponent c) { return c instanceof JTextField || c instanceof JFormattedTextField; }

    static void install(JTextComponent c) {
        if (boxed(c)) c.setOpaque(false);
        c.setCaretColor(AeroPalette.TEXT_STRONG);
        MouseAdapter ma = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                JTextComponent t = (JTextComponent) e.getComponent();
                boolean over = hit(t, e.getPoint()) != 0;
                t.setCursor(over ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            }
            @Override public void mousePressed(MouseEvent e) {
                JTextComponent t = (JTextComponent) e.getComponent(); int h = hit(t, e.getPoint());
                if (h == 0) return; e.consume();
                if (h == 1 && t instanceof JPasswordField) {
                    JPasswordField pf = (JPasswordField) t; Object saved = pf.getClientProperty("Aero.echo");
                    if (saved == null) { pf.putClientProperty("Aero.echo", pf.getEchoChar()); pf.setEchoChar((char) 0); } else { pf.setEchoChar((Character) saved); pf.putClientProperty("Aero.echo", null); }
                    pf.repaint();
                } else if (h == 2) { t.setText(""); t.requestFocusInWindow(); }
            }
        };
        c.putClientProperty("Aero.textMouse", ma);
        c.addMouseListener(ma); c.addMouseMotionListener(ma);
        FocusListener fl = new FocusAdapter() { @Override public void focusGained(FocusEvent e) { ((JComponent) e.getComponent()).repaint(); } @Override public void focusLost(FocusEvent e) { ((JComponent) e.getComponent()).repaint(); } };
        c.putClientProperty("Aero.textFocus", fl); c.addFocusListener(fl);
    }
    static void uninstall(JTextComponent c) {
        Object o = c.getClientProperty("Aero.textMouse"); if (o instanceof MouseAdapter) { c.removeMouseListener((MouseAdapter) o); c.removeMouseMotionListener((MouseAdapter) o); }
        o = c.getClientProperty("Aero.textFocus"); if (o instanceof FocusListener) c.removeFocusListener((FocusListener) o);
    }

    /** 0 none, 1 reveal, 2 clear. */
    static int hit(JTextComponent t, Point p) {
        int x = t.getWidth() - 5 - 1;
        if (AeroTextAccessories.reveal(t)) { x -= AeroTextAccessories.BTN; if (p.x >= x && p.x <= x + AeroTextAccessories.BTN) return 1; }
        if (AeroTextAccessories.clear(t) && !t.getText().isEmpty()) { x -= AeroTextAccessories.BTN; if (p.x >= x && p.x <= x + AeroTextAccessories.BTN) return 2; }
        return 0;
    }

    static void paintBackground(Graphics g0, JTextComponent c) {
        Graphics2D g = AeroPaint.g2(g0); int w = c.getWidth(), h = c.getHeight();
        String st = String.valueOf(c.getClientProperty("JTextField.style"));
        Color bg = c.getBackground();
        boolean uiBg = bg instanceof javax.swing.plaf.UIResource;
        Shape s = AeroPaint.rr(0, 0, w, h, 2);
        if (!c.isEnabled()) { g.setColor(uiBg ? new Color(0xf3f4f6) : bg); g.fill(s); }
        else if ("info".equals(st)) { g.setPaint(AeroPaint.vgrad(0, h, AeroPaint.Gloss.of2(AeroPalette.INFO_BG0, AeroPalette.INFO_BG1))); g.fill(s); }
        else if ("highlight".equals(st) || "warning".equals(st)) { g.setColor(AeroPalette.WARN_BG); g.fill(s); }
        else if (!c.isEditable() && uiBg) { g.setColor(AeroPalette.READONLY_BG); g.fill(s); }
        else { g.setColor(bg); g.fill(s); }
        g.dispose();
    }

    static void paintAccessories(Graphics g0, JTextComponent c) {
        Graphics2D g = AeroPaint.g2(g0); Insets in = c.getInsets(); int h = c.getHeight(), w = c.getWidth();
        // placeholder
        Object ph = c.getClientProperty("JTextField.placeholderText");
        if (ph != null && c.getDocument().getLength() == 0) {
            g.setFont(c.getFont()); g.setColor(AeroPalette.TEXT_FAINT); FontMetrics fm = g.getFontMetrics();
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            if (c instanceof JTextField) g.drawString(ph.toString(), in.left, y); else g.drawString(ph.toString(), in.left, in.top + fm.getAscent());
        }
        Icon li = AeroTextAccessories.leadingIcon(c);
        if (li != null) li.paintIcon(c, g, 5, (h - li.getIconHeight()) / 2);
        int x = w - 5 - 1;
        if (AeroTextAccessories.reveal(c)) {
            x -= AeroTextAccessories.BTN; boolean shown = c instanceof JPasswordField && ((JPasswordField) c).getEchoChar() == 0;
            Icon ic = AeroIcons.lucide(shown ? "eye-off" : "eye", 13, AeroPalette.TEXT_MUTED, 1.9f);
            ic.paintIcon(c, g, x + 1, (h - 13) / 2);
        }
        if (AeroTextAccessories.clear(c) && !c.getText().isEmpty()) {
            x -= AeroTextAccessories.BTN; AeroIcons.lucide("x-circle", 13, AeroPalette.TEXT_FAINT, 1.9f).paintIcon(c, g, x + 1, (h - 13) / 2);
        }
        Icon ti = AeroTextAccessories.trailingIcon(c);
        if (ti != null) ti.paintIcon(c, g, x - ti.getIconWidth() - 1, (h - ti.getIconHeight()) / 2);
        g.dispose();
    }
}
