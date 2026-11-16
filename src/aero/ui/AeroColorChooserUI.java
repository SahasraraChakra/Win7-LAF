package aero.ui;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicColorChooserUI;

public class AeroColorChooserUI extends BasicColorChooserUI {
    public static ComponentUI createUI(JComponent c) { return new AeroColorChooserUI(); }
}
