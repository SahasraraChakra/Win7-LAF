package aero;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/** Geometry of leading / trailing icons, clear button and password reveal button inside text fields. */
public final class AeroTextAccessories {
    private AeroTextAccessories() {}
    public static final int BTN = 16;
    public static Icon leadingIcon(JComponent c) { Object o = c.getClientProperty("JTextField.leadingIcon"); return o instanceof Icon ? (Icon) o : null; }
    public static Icon trailingIcon(JComponent c) { Object o = c.getClientProperty("JTextField.trailingIcon"); return o instanceof Icon ? (Icon) o : null; }
    public static boolean reveal(JComponent c) {
        if (!(c instanceof JPasswordField)) return false;
        Object o = c.getClientProperty("JPasswordField.showRevealButton");
        return o == null ? Boolean.TRUE.equals(UIManager.get("PasswordField.showRevealButton")) : Boolean.TRUE.equals(o);
    }
    public static boolean clear(JComponent c) { return Boolean.TRUE.equals(c.getClientProperty("JTextField.showClearButton")) && c instanceof JTextField && !(c instanceof JPasswordField); }
    public static int leadingWidth(JComponent c) { Icon i = leadingIcon(c); return i == null ? 0 : i.getIconWidth() + 5; }
    public static int trailingWidth(JComponent c) {
        int w = 0; Icon t = trailingIcon(c); if (t != null) w += t.getIconWidth() + 5;
        if (reveal(c)) w += BTN; if (clear(c)) w += BTN; return w;
    }
}
