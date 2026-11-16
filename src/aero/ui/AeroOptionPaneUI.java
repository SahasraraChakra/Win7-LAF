package aero.ui;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;

public class AeroOptionPaneUI extends BasicOptionPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroOptionPaneUI(); }
    @Override protected void installDefaults() { super.installDefaults(); optionPane.setOpaque(true); }
}
