package aero.ui;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.MetalFileChooserUI;

/** File chooser: Swing's full-featured chooser layout, composed entirely of Aero components. */
public class AeroFileChooserUI extends MetalFileChooserUI {
    public AeroFileChooserUI(JFileChooser c) { super(c); }
    public static ComponentUI createUI(JComponent c) { return new AeroFileChooserUI((JFileChooser) c); }
}
