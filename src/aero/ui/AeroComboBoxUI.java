package aero.ui;

import aero.*;
import aero.AeroButtonStyle.State;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/** Combo boxes: non-editable = input with chevron (HTML select look); editable = input + glossy button. Popup uses the Aero menu surface. */
public class AeroComboBoxUI extends BasicComboBoxUI {
    public static ComponentUI createUI(JComponent c) { return new AeroComboBoxUI(); }

    @Override protected void installDefaults() { super.installDefaults(); comboBox.setOpaque(false); }
    @Override protected JButton createArrowButton() { return new Arrow(); }
    @Override protected ComboPopup createPopup() { return new AeroComboPopup(comboBox); }
    @Override protected ListCellRenderer<Object> createRenderer() { return new AeroComboRenderer(); }
    @Override protected ComboBoxEditor createEditor() { return new BasicComboBoxEditor.UIResource() {
        @Override protected JTextField createEditorComponent() { JTextField f = super.createEditorComponent(); f.setBorder(new EmptyBorder(0, 5, 0, 5)); return f; } }; }
    @Override public void configureEditor() {
        super.configureEditor();
        Component ed = comboBox.getEditor().getEditorComponent();
        if (ed instanceof JComponent) { ((JComponent) ed).setBorder(new EmptyBorder(0, 5, 0, 5)); ((JComponent) ed).setOpaque(false); }
        if (ed instanceof JTextField && ((JTextField) ed).getClientProperty("Aero.cbFocus") == null) {
            ((JTextField) ed).putClientProperty("Aero.cbFocus", Boolean.TRUE);
            ed.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { comboBox.putClientProperty("Aero.focusWithin", true); comboBox.repaint(); }
                @Override public void focusLost(FocusEvent e) { comboBox.putClientProperty("Aero.focusWithin", null); comboBox.repaint(); } });
        }
    }
    @Override public void paint(Graphics g0, JComponent c) {
        Graphics2D g = AeroPaint.g2(g0); int w = c.getWidth(), h = c.getHeight();
        g.setColor(comboBox.isEnabled() ? (comboBox.getBackground() instanceof UIResource ? Color.WHITE : comboBox.getBackground()) : new Color(0xf3f4f6));
        g.fill(AeroPaint.rr(0, 0, w, h, 2));
        g.dispose();
        hasFocus = comboBox.hasFocus();
        if (!comboBox.isEditable()) { Rectangle r = rectangleForCurrentValue(); Graphics2D tg = AeroPaint.textG2(g0); paintCurrentValue(tg, r, false); tg.dispose(); }
    }
    @Override public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) { }
    @Override public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer<Object> r = comboBox.getRenderer();
        Component c = r.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
        c.setFont(comboBox.getFont()); c.setForeground(comboBox.isEnabled() ? comboBox.getForeground() : AeroPalette.TEXT_FAINT);
        if (c instanceof JComponent) ((JComponent) c).setOpaque(false);
        boolean shouldValidate = c instanceof JPanel;
        currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
    }
    @Override public Dimension getMinimumSize(JComponent c) { Dimension d = super.getMinimumSize(c); d.height = Math.max(d.height, AeroPaint.scaleH(23)); return d; }
    @Override public Dimension getPreferredSize(JComponent c) { Dimension d = super.getPreferredSize(c); d.height = Math.max(d.height, AeroPaint.scaleH(23)); return d; }

    private final class Arrow extends JButton implements UIResource {
        Arrow() { setRolloverEnabled(true); setFocusable(false); setBorder(null); setOpaque(false); setContentAreaFilled(false); setName("ComboBox.arrowButton"); }
        @Override public Dimension getPreferredSize() { return new Dimension(comboBox != null && comboBox.isEditable() ? 18 : 20, 20); }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
            boolean ed = comboBox.isEditable(), open = comboBox.isPopupVisible(); ButtonModel m = getModel();
            if (ed) {
                State st = !comboBox.isEnabled() ? State.DISABLED : (m.isPressed() || open) ? State.PRESSED : m.isRollover() ? State.HOVER : State.NORMAL;
                AeroButtonStyle.Spec sp = AeroButtonStyle.STANDARD.spec(st);
                g.translate(0, -0); Shape s = AeroButtonPainter.shape(0, 0, w, h, 3, "last");
                g.setPaint(AeroPaint.vgrad(0, h, sp.gloss)); g.fill(s);
                if (sp.inset != null) AeroPaint.insetTop(g, 0, 0, w, sp.inset, 0);
                Shape clip = g.getClip(); g.clipRect(0, 0, w, h); g.setColor(sp.border); g.draw(AeroButtonPainter.shape(-.5f, .5f, w, h - 1, 2.5f, "last")); g.setClip(clip); g.setColor(sp.border); g.fillRect(0, 0, 1, h);
                AeroIcons.triangle(SwingConstants.SOUTH, 7, 4, comboBox.isEnabled() ? AeroPalette.TEXT_DIM : AeroPalette.TEXT_FAINT).paintIcon(this, g, (w - 7) / 2, (h - 4) / 2);
            } else {
                AeroIcons.lucide("chevron-down", 11, comboBox.isEnabled() ? AeroPalette.TEXT_DIM : AeroPalette.TEXT_FAINT, 2.2f).paintIcon(this, g, (w - 11) / 2, (h - 11) / 2);
            }
            g.dispose();
        }
    }

    static class AeroComboRenderer extends DefaultListCellRenderer implements UIResource {
        AeroComboRenderer() { setOpaque(false); setBorder(new EmptyBorder(0, 5, 0, 5)); }
        @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean sel, boolean focus) {
            if (index < 0) { // current value display
                setText(value == null ? " " : value.toString()); setIcon(value instanceof Icon ? (Icon) value : null); if (value instanceof Icon) setText("");
                setFont(list.getFont()); setBorder(new EmptyBorder(0, 5, 0, 5)); setOpaque(false); return this;
            }
            Component c = AeroListRenderers.menuStyle().getListCellRendererComponent(list, value, index, sel, focus);
            return c;
        }
        @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); if (getText() == null || getText().trim().isEmpty()) d.height = 16; return d; }
    }

    static final class AeroListRenderers { private static final AeroListCellRenderer R = new AeroListCellRenderer(); static AeroListCellRenderer menuStyle() { return R; } }

    static class AeroComboPopup extends BasicComboPopup {
        AeroComboPopup(JComboBox<Object> cb) { super(cb); }
        @Override protected JList<Object> createList() { JList<Object> l = super.createList(); l.putClientProperty("Aero.popupList", Boolean.TRUE); l.setCellRenderer(new AeroListCellRenderer()); l.setBackground(AeroPalette.POP0); return l; }
        @Override protected void configurePopup() { super.configurePopup(); setBorder(new CompoundBorder(new AeroBorders.LineBorder1(AeroPalette.POP_BORDER), new EmptyBorder(2, 2, 2, 2))); setOpaque(true); setBackground(AeroPalette.POP0); }
        @Override protected JScrollPane createScroller() { JScrollPane sp = super.createScroller(); sp.setBorder(null); sp.getViewport().setBackground(AeroPalette.POP0); return sp; }
    }
}
