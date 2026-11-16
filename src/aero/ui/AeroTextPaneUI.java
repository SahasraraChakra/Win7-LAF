package aero.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.JTextComponent;

public class AeroTextPaneUI extends BasicTextPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroTextPaneUI(); }
    @Override protected void installDefaults() { super.installDefaults(); AeroTextSupport.install(getComponent()); }
    @Override protected void uninstallDefaults() { AeroTextSupport.uninstall(getComponent()); super.uninstallDefaults(); }
    @Override protected void paintSafely(Graphics g) {
        JTextComponent c = getComponent();
        if (AeroTextSupport.boxed(c)) AeroTextSupport.paintBackground(g, c);
        Graphics2D tg = aero.AeroPaint.textG2(g); super.paintSafely(tg); tg.dispose();
        AeroTextSupport.paintAccessories(g, c);
    }
}
