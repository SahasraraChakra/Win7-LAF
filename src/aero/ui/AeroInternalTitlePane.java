package aero.ui;

import aero.*;
import aero.AeroPaint.Gloss;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

/** Internal-frame title bar: 24px frosted gradient, 13px icon, bold 11px title, 16x14 / 20x14 caption buttons. */
public class AeroInternalTitlePane extends JComponent {
    private final JInternalFrame frame; private final boolean iconMode;
    private final AeroCaptionButton min = new AeroCaptionButton(AeroCaptionButton.Kind.MIN, true, "Minimize"), max = new AeroCaptionButton(AeroCaptionButton.Kind.MAX, true, "Maximize"), close = new AeroCaptionButton(AeroCaptionButton.Kind.CLOSE, true, "Close");
    private final PropertyChangeListener pcl;

    public AeroInternalTitlePane(JInternalFrame f, boolean iconMode) {
        frame = f; this.iconMode = iconMode; setLayout(null); setOpaque(false);
        add(min); add(max); add(close);
        close.addActionListener(e -> frame.doDefaultCloseAction());
        min.addActionListener(e -> { try { frame.setIcon(!frame.isIcon()); } catch (PropertyVetoException ex) { } });
        max.addActionListener(e -> { try { if (frame.isIcon()) frame.setIcon(false); else frame.setMaximum(!frame.isMaximum()); } catch (PropertyVetoException ex) { } });
        pcl = e -> { sync(); revalidate(); repaint(); };
        addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) { try { if (frame.isIcon()) frame.setIcon(false); else if (frame.isMaximizable()) frame.setMaximum(!frame.isMaximum()); } catch (PropertyVetoException ex) { } } } });
        sync();
    }
    @Override public void addNotify() { super.addNotify(); frame.addPropertyChangeListener(pcl); sync(); }
    @Override public void removeNotify() { frame.removePropertyChangeListener(pcl); super.removeNotify(); }
    private void sync() {
        close.setVisible(frame.isClosable()); max.setVisible(frame.isMaximizable() || iconMode); min.setVisible(frame.isIconifiable() && !iconMode);
        max.setKind(frame.isMaximum() && !iconMode ? AeroCaptionButton.Kind.RESTORE : AeroCaptionButton.Kind.MAX);
        max.setToolTipText(iconMode ? "Restore" : frame.isMaximum() ? "Restore Down" : "Maximize");
    }
    @Override public Dimension getPreferredSize() { return new Dimension(100, AeroPaint.scaleH(24)); }
    @Override public void doLayout() {
        int x = getWidth() - 6, h = getHeight() - 1;
        for (AeroCaptionButton b : new AeroCaptionButton[]{close, max, min}) { if (!b.isVisible()) continue; Dimension d = b.getPreferredSize(); x -= d.width; b.setBounds(x, (h - d.height) / 2, d.width, d.height); x -= 2; }
    }
    private int btnLeft() { int x = getWidth() - 6; for (AeroCaptionButton b : new AeroCaptionButton[]{close, max, min}) if (b.isVisible()) x = b.getX() - 2; return x; }
    @Override protected void paintComponent(Graphics g0) {
        Graphics2D g = AeroPaint.g2(g0); int w = getWidth(), h = getHeight(); boolean sel = frame.isSelected() || iconMode;
        java.awt.Shape top = new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h + 4, 3, 3);
        Shape clip = g.getClip(); g.clip(new java.awt.geom.Rectangle2D.Float(0, 0, w, h));
        g.setColor(Color.WHITE); g.fill(top);
        Gloss gl = new Gloss(new float[]{0, .42f, .43f, 1}, AeroPalette.IF0, AeroPalette.IF1, AeroPalette.IF2, AeroPalette.IF3);
        if (!sel) { Color wh = new Color(255, 255, 255, 150); Color[] c = new Color[4]; for (int i = 0; i < 4; i++) { Color o = gl.c[i]; float k = .59f; c[i] = new Color(Math.round(o.getRed() * (1 - k) + 255 * k), Math.round(o.getGreen() * (1 - k) + 255 * k), Math.round(o.getBlue() * (1 - k) + 255 * k), o.getAlpha()); } gl = new Gloss(gl.f, c); }
        g.setPaint(AeroPaint.vgrad(0, h, gl)); g.fill(top);
        g.setColor(AeroPaint.alpha(Color.WHITE, .95)); g.fillRect(2, 1, w - 4, 1);
        g.setColor(AeroPalette.IF_BORDER); g.fillRect(0, h - 1, w, 1); g.setClip(clip);
        int x = 8; Icon ic = frame.getFrameIcon();
        if (ic == null) ic = AeroIcons.lucide("layout-grid", 13, AeroPalette.c(0x1e3a8a), 2f);
        ic.paintIcon(this, g, x, (h - 1 - ic.getIconHeight()) / 2); x += ic.getIconWidth() + 6;
        Font f = AeroFonts.bold(11f); g.setFont(f); FontMetrics fm = g.getFontMetrics(); String t = frame.getTitle() == null ? "" : frame.getTitle();
        int avail = btnLeft() - x - 4; if (fm.stringWidth(t) > avail) { int n = t.length(); while (n > 0 && fm.stringWidth(t.substring(0, n) + "\u2026") > avail) n--; t = n > 0 ? t.substring(0, n) + "\u2026" : ""; }
        g.setColor(sel ? AeroPalette.FRAME_TITLE_FG : AeroPalette.TEXT_MUTED); g.drawString(t, x, (h - 1 - fm.getHeight()) / 2f + fm.getAscent());
        g.dispose();
    }
}
