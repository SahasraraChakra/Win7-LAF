package aero.ui;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class AeroScrollPaneUI extends BasicScrollPaneUI {
    public static ComponentUI createUI(JComponent c) { return new AeroScrollPaneUI(); }
}
