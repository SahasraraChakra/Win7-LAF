package aero.demo;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

/** Headless renderer: paints the showcase (and open menu popups) into a PNG. */
public class Shot {
    static void layout(Component c) { if (c instanceof Container) { ((Container) c).doLayout(); for (Component k : ((Container) c).getComponents()) layout(k); } }
    static void popup(JPopupMenu p, Graphics2D g, int x, int y) {
        p.setSize(p.getPreferredSize()); layout(p); Graphics2D g2 = (Graphics2D) g.create(); g2.translate(x, y); p.paint(g2); g2.dispose();
    }
    static double SC = 1.0;
    public static void main(String[] a) throws Exception {
        System.setProperty("java.awt.headless", "true");
        aero.AeroLookAndFeel.install();
        int w = 1280, h = 840; String out = a[0]; boolean menu = a.length > 1 && a[1].equals("menu"); if (a.length > 2) SC = Double.parseDouble(a[2]);
        SwingUtilities.invokeAndWait(() -> {
            try {
                JRootPane root = new JRootPane(); root.setWindowDecorationStyle(JRootPane.FRAME); root.putClientProperty("Aero.title", Showcase.TITLE);
                Showcase.populate(root); root.setSize(w, h); layout(root); layout(root);
                BufferedImage img = new BufferedImage((int) ((w + 120) * SC), (int) ((h + 100) * SC), BufferedImage.TYPE_INT_ARGB); Graphics2D g = img.createGraphics();
                g.setPaint(new RadialGradientPaint(w / 2f, 200, 900, new float[]{0, 1}, new Color[]{new Color(0x16243b), new Color(0x0b1220)})); g.fillRect(0, 0, img.getWidth(), img.getHeight());
                g.scale(1, 1); g.translate(60 * SC, 50 * SC); g.scale(SC, SC); root.paint(g);
                if (menu) {
                    JMenuBar mb = root.getJMenuBar(); JMenu laf = mb.getMenu(5); Point lp = SwingUtilities.convertPoint(laf, 0, laf.getHeight(), root);
                    JPopupMenu pm = laf.getPopupMenu(); popup(pm, g, lp.x, lp.y);
                    JMenu tints = (JMenu) laf.getMenuComponent(4); Rectangle tb = tints.getBounds(); popup(tints.getPopupMenu(), g, lp.x + pm.getWidth() - 3, lp.y + tb.y - 4);
                }
                g.dispose(); ImageIO.write(img, "png", new java.io.File(out));
            } catch (Exception e) { e.printStackTrace(); }
        });
        System.exit(0);
    }
}
