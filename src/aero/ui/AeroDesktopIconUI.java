package aero.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicDesktopIconUI;

public class AeroDesktopIconUI extends BasicDesktopIconUI {
    public static ComponentUI createUI(JComponent c) { return new AeroDesktopIconUI(); }
    @Override protected void installComponents() {
        frame = desktopIcon.getInternalFrame(); iconPane = new AeroInternalTitlePane(frame, true);
        desktopIcon.setLayout(new BorderLayout()); desktopIcon.add(iconPane, BorderLayout.CENTER); desktopIcon.setOpaque(false);
    }
    @Override public Dimension getPreferredSize(JComponent c) { return new Dimension(170, 25); }
    @Override public Dimension getMinimumSize(JComponent c) { return new Dimension(100, 25); }
    @Override public Dimension getMaximumSize(JComponent c) { return new Dimension(300, 25); }
}
