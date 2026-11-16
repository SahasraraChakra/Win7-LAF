package aero.ui;

import aero.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.*;

public class AeroTreeUI extends BasicTreeUI {
    public static ComponentUI createUI(JComponent c) { return new AeroTreeUI(); }
    private MouseAdapter hover;
    @Override protected void installDefaults() {
        super.installDefaults();
        tree.setRowHeight(AeroPaint.scaleH(20)); tree.putClientProperty("JTree.lineStyle", "None");
        if (tree.getCellRenderer() instanceof UIResource || tree.getCellRenderer() instanceof DefaultTreeCellRenderer && tree.getCellRenderer().getClass() == DefaultTreeCellRenderer.class) tree.setCellRenderer(new AeroTreeCellRenderer());
    }
    @Override protected void installListeners() {
        super.installListeners();
        hover = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { set(tree.getRowForLocation(e.getX(), e.getY())); }
            @Override public void mouseExited(MouseEvent e) { set(-1); }
            void set(int r) { Object o = tree.getClientProperty("Aero.rolloverRow"); int old = o instanceof Integer ? (Integer) o : -1; if (old != r) { tree.putClientProperty("Aero.rolloverRow", r < 0 ? null : r); tree.repaint(); } }
        };
        tree.addMouseMotionListener(hover); tree.addMouseListener(hover);
    }
    @Override protected void uninstallListeners() { tree.removeMouseMotionListener(hover); tree.removeMouseListener(hover); super.uninstallListeners(); }
    @Override public void paint(Graphics g, JComponent c) { Graphics2D tg = AeroPaint.textG2(g); super.paint(tg, c); tg.dispose(); }
    @Override protected void paintHorizontalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) { }
    @Override protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) { }
}
