package aero.ui;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicViewportUI;

public class AeroViewportUI extends BasicViewportUI {
    private static final AeroViewportUI INSTANCE = new AeroViewportUI();
    public static ComponentUI createUI(JComponent c) { return INSTANCE; }
}
