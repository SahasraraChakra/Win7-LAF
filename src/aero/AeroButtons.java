package aero;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Convenience API for the Aero button styles. */
public final class AeroButtons {
    private AeroButtons() {}
    public static final String STYLE = "JButton.style";
    public static final String SEGMENT = "JButton.segmentPosition";

    /** Sets a named style (primary, standard, accent, success, error, danger, warning, info, flat, link, outline...). */
    public static <T extends AbstractButton> T style(T b, String style) { b.putClientProperty(STYLE, style); b.revalidate(); b.repaint(); return b; }
    public static JButton create(String text, String style) { JButton b = new JButton(text); return style(b, style); }
    public static JButton create(String text, Icon icon, String style) { JButton b = new JButton(text, icon); return style(b, style); }
    /** Icon-only button using a Lucide icon from the design set. */
    public static JButton icon(String lucideName, String tooltip, String style) {
        JButton b = new JButton(AeroIcons.lucide(lucideName, 13, AeroPalette.TEXT_DIM)); b.setToolTipText(tooltip); return style(b, style);
    }
    public static JToggleButton toggle(String text, String style) { JToggleButton b = new JToggleButton(text); return style(b, style); }
    public static JButton primary(String t) { return create(t, "primary"); }
    public static JButton success(String t) { return create(t, "success"); }
    public static JButton error(String t) { return create(t, "error"); }
    public static JButton warning(String t) { return create(t, "warning"); }
    public static JButton info(String t) { return create(t, "info"); }
    public static JButton danger(String t) { return create(t, "danger"); }
    public static JButton flat(String t) { return create(t, "flat"); }
    public static JButton link(String t) { return create(t, "link"); }

    /** Segmented toggle group (Day | Week | Month). Returns a panel; the ButtonGroup is in the "Aero.group" property. */
    public static JPanel segmented(int selected, String... labels) {
        JPanel p = new JPanel(new GridLayout(1, labels.length, -1, 0)) { @Override public boolean isOpaque() { return false; } };
        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < labels.length; i++) {
            JToggleButton b = new JToggleButton(labels[i]);
            b.putClientProperty(STYLE, "segment");
            b.putClientProperty(SEGMENT, labels.length == 1 ? "only" : i == 0 ? "first" : i == labels.length - 1 ? "last" : "middle");
            b.setMargin(new Insets(1, 8, 1, 8)); b.setSelected(i == selected); bg.add(b); p.add(b);
        }
        p.putClientProperty("Aero.group", bg);
        return p;
    }

    /** Split button ("Save Document" + arrow) - the arrow opens the popup menu. Returns a panel; main button is the first component. */
    public static JPanel split(String text, String style, JPopupMenu menu) {
        JPanel p = new JPanel(new GridBagLayout()) { @Override public boolean isOpaque() { return false; } };
        JButton main = create(text, style); main.putClientProperty(SEGMENT, "first");
        JButton arrow = create("", style); arrow.setIcon(AeroIcons.triangle(SwingConstants.SOUTH, 7, 4, AeroPalette.TEXT_2)); arrow.putClientProperty(SEGMENT, "last"); arrow.setMargin(new Insets(2, 6, 2, 6));
        arrow.addActionListener(e -> { if (menu != null) menu.show(main, 0, main.getHeight()); });
        GridBagConstraints c = new GridBagConstraints(); c.fill = GridBagConstraints.VERTICAL; c.gridx = 0; p.add(main, c); c.gridx = 1; c.insets = new Insets(0, -1, 0, 0); p.add(arrow, c);
        return p;
    }

    /** Drop-in: a JLabel-sized text button that looks like a hyperlink. */
    public static JButton hyperlink(String text, Runnable r) { JButton b = link(text); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setMargin(new Insets(0, 2, 0, 2)); b.addActionListener(e -> r.run()); return b; }
}
