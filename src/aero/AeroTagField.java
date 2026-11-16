package aero;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/** Tag / chip input ("Category Tag Filter"): Enter or comma adds a chip, Backspace on empty removes the last, the x removes one. */
public class AeroTagField extends JPanel {
    private final List<String> tags = new ArrayList<>(); private final JTextField input = new JTextField(8);
    public AeroTagField() {
        super(new FlowLayout(FlowLayout.LEFT, 3, 1)); setOpaque(false); setBorder(new AeroBorders.InputBorder(1, 2, 1, 2));
        input.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 3)); input.setOpaque(false); input.putClientProperty("JTextField.placeholderText", "Type...");
        input.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyChar() == ',') { String t = input.getText().trim(); if (!t.isEmpty()) { addTag(t); input.setText(""); } e.consume(); }
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && input.getText().isEmpty() && !tags.isEmpty()) removeTag(tags.size() - 1);
            } });
        input.addFocusListener(new FocusAdapter() { public void focusGained(FocusEvent e) { putClientProperty("Aero.focusWithin", true); repaint(); } public void focusLost(FocusEvent e) { putClientProperty("Aero.focusWithin", null); repaint(); } });
        rebuild();
    }
    public void addTag(String t) { tags.add(t); rebuild(); firePropertyChange("tags", null, tags); }
    public void removeTag(int i) { tags.remove(i); rebuild(); input.requestFocusInWindow(); firePropertyChange("tags", null, tags); }
    public List<String> getTags() { return new ArrayList<>(tags); }
    private void rebuild() {
        removeAll();
        for (int i = 0; i < tags.size(); i++) { final int idx = i; add(new Chip(tags.get(i), () -> removeTag(idx))); }
        add(input); revalidate(); repaint();
    }
    @Override protected void paintComponent(Graphics g0) { Graphics2D g = AeroPaint.g2(g0); g.setColor(Color.WHITE); g.fill(AeroPaint.rr(0, 0, getWidth(), getHeight(), 2)); g.dispose(); }
    @Override public Dimension getPreferredSize() { Dimension d = super.getPreferredSize(); d.height = Math.max(d.height, 23); return d; }

    private static final class Chip extends JComponent {
        final String text; final Runnable onRemove; boolean overX;
        Chip(String t, Runnable r) { text = t; onRemove = r; setOpaque(false);
            MouseAdapter m = new MouseAdapter() {
                @Override public void mouseMoved(MouseEvent e) { boolean o = e.getX() > getWidth() - 14; if (o != overX) { overX = o; repaint(); } }
                @Override public void mouseExited(MouseEvent e) { overX = false; repaint(); }
                @Override public void mouseClicked(MouseEvent e) { if (e.getX() > getWidth() - 14) onRemove.run(); } };
            addMouseListener(m); addMouseMotionListener(m); }
        @Override public Dimension getPreferredSize() { FontMetrics fm = getFontMetrics(AeroFonts.semibold(10f)); return new Dimension(fm.stringWidth(text) + 24, 17); }
        @Override protected void paintComponent(Graphics g0) {
            Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight();
            g.setPaint(AeroPaint.vgrad(0, h, AeroPaint.Gloss.of2(AeroPalette.c(0xeff6ff), AeroPalette.c(0xdbeafe)))); g.fill(AeroPaint.rr(0, 0, w, h, 2)); AeroPaint.border(g, 0, 0, w, h, 2, AeroPalette.c(0xbfdbfe));
            g.setFont(AeroFonts.semibold(10f)); FontMetrics fm = g.getFontMetrics(); g.setColor(AeroPalette.c(0x1e3a8a)); g.drawString(text, 6, (h - fm.getHeight()) / 2f + fm.getAscent());
            g.setColor(overX ? new Color(0xdc2626) : AeroPalette.c(0x3b82f6)); g.setStroke(new BasicStroke(1.3f)); int cx = w - 8, cy = h / 2; g.drawLine(cx - 2, cy - 2, cx + 2, cy + 2); g.drawLine(cx + 2, cy - 2, cx - 2, cy + 2); g.dispose();
        }
    }
}
