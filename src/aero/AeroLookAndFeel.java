package aero;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 * Windows 7 Aero Glass look and feel for Java Swing.
 * <pre>
 *   AeroLookAndFeel.install();           // LAF + decorated (custom titlebar) frames and dialogs
 *   AeroButtons.style(button, "primary");
 * </pre>
 * Every one of the 43 standard Swing UIClassIDs has an Aero delegate.
 */
public class AeroLookAndFeel extends BasicLookAndFeel {
    public static boolean compact = false;
    public static boolean roundedWindows = true;
    private static final String P = "aero.ui.Aero";

    /** Installs the LAF and turns on Aero-decorated frames/dialogs. Call before creating any window. */
    public static boolean install() { return install(true); }
    public static boolean install(boolean decoratedWindows) {
        try {
            System.setProperty("awt.useSystemAAFontSettings", "on"); System.setProperty("swing.aatext", "true"); System.setProperty("sun.java2d.dpiaware", "true");
            UIManager.setLookAndFeel(new AeroLookAndFeel());
            JFrame.setDefaultLookAndFeelDecorated(decoratedWindows); JDialog.setDefaultLookAndFeelDecorated(decoratedWindows);
            return true;
        } catch (UnsupportedLookAndFeelException e) { e.printStackTrace(); return false; }
    }
    /** Re-tints the whole UI at runtime (Sky Blue, Sea Teal, Graphite, Twilight, Solar Amber). */
    public static void applyTint(AeroPalette.Tint t) { AeroPalette.setTint(t); refresh(); }
    public static void applyCustomTint(Color c) { AeroPalette.setCustomTint(c); refresh(); }
    public static void setCompact(boolean c) { compact = c; refresh(); }
    public static void refresh() {
        try { UIManager.setLookAndFeel(new AeroLookAndFeel()); } catch (Exception e) { e.printStackTrace(); }
        for (Window w : Window.getWindows()) { SwingUtilities.updateComponentTreeUI(w); w.repaint(); }
    }

    @Override public String getName() { return "Win7-LAF"; }
    @Override public String getID() { return "Aero"; }
    @Override public String getDescription() { return "Win7-LAF: Windows 7 Aero Glass look and feel"; }
    @Override public boolean isNativeLookAndFeel() { return false; }
    @Override public boolean isSupportedLookAndFeel() { return true; }
    @Override public boolean getSupportsWindowDecorations() { return true; }

    @Override protected void initClassDefaults(UIDefaults t) {
        super.initClassDefaults(t);
        String[] ids = {"Button", "ToggleButton", "CheckBox", "RadioButton", "CheckBoxMenuItem", "RadioButtonMenuItem", "MenuItem", "Menu", "MenuBar", "PopupMenu",
                "PopupMenuSeparator", "Separator", "ToolBarSeparator", "ToolBar", "TextField", "PasswordField", "FormattedTextField", "TextArea", "TextPane", "EditorPane",
                "ComboBox", "List", "ScrollBar", "ScrollPane", "Slider", "Spinner", "SplitPane", "TabbedPane", "Table", "TableHeader", "Tree", "ProgressBar", "Label", "Panel",
                "Viewport", "ToolTip", "OptionPane", "FileChooser", "ColorChooser", "InternalFrame", "DesktopPane", "DesktopIcon", "RootPane"};
        for (String id : ids) t.put(id + "UI", P + id + "UI");
    }

    @Override protected void initSystemColorDefaults(UIDefaults t) {
        super.initSystemColorDefaults(t);
        Object[] c = {
            "desktop", AeroPalette.DESK1, "activeCaption", AeroPalette.c(0xc3daf2), "activeCaptionText", AeroPalette.TEXT_STRONG, "activeCaptionBorder", AeroPalette.WIN_BORDER,
            "inactiveCaption", AeroPalette.c(0xdbe6f2), "inactiveCaptionText", AeroPalette.TEXT_MUTED, "inactiveCaptionBorder", AeroPalette.c(0xb5c7d8),
            "window", Color.WHITE, "windowBorder", AeroPalette.WIN_BORDER, "windowText", AeroPalette.TEXT, "menu", AeroPalette.POP0, "menuText", AeroPalette.TEXT,
            "text", Color.WHITE, "textText", AeroPalette.TEXT, "textHighlight", AeroPalette.SELECTION_BG, "textHighlightText", AeroPalette.SELECTION_FG, "textInactiveText", AeroPalette.TEXT_FAINT,
            "control", AeroPalette.PANEL_BG, "controlText", AeroPalette.TEXT, "controlHighlight", Color.WHITE, "controlLtHighlight", Color.WHITE, "controlShadow", AeroPalette.BTN_BORDER,
            "controlDkShadow", AeroPalette.c(0x6b7f94), "scrollbar", AeroPalette.SB_TRACK, "info", AeroPalette.TIP_BG, "infoText", AeroPalette.TEXT};
        t.putDefaults(c);
    }

    private static InsetsUIResource ins(int t, int l, int b, int r) { return new InsetsUIResource(t, l, b, r); }
    private static ColorUIResource col(Color c) { return new ColorUIResource(c); }
    private static BorderUIResource.EmptyBorderUIResource eb(int t, int l, int b, int r) { return new BorderUIResource.EmptyBorderUIResource(t, l, b, r); }

    @Override protected void initComponentDefaults(UIDefaults t) {
        super.initComponentDefaults(t);
        FontUIResource base = AeroFonts.ui(AeroFonts.medium(11f)), plain = AeroFonts.ui(AeroFonts.plain(11f)), bold = AeroFonts.ui(AeroFonts.bold(11f)), small = AeroFonts.ui(AeroFonts.plain(10.5f));
        ColorUIResource text = col(AeroPalette.TEXT), white = col(Color.WHITE), panel = col(AeroPalette.PANEL_BG), faint = col(AeroPalette.TEXT_FAINT), sel = col(AeroPalette.SELECTION_BG), selFg = col(AeroPalette.SELECTION_FG);
        Border input = new AeroBorders.InputBorder(3, 5, 3, 5), combo = new AeroBorders.InputBorder(0, 0, 0, 0);
        Object txtFont = AeroFonts.ui(AeroFonts.medium(11f));
        Object[] d = {
            "Aero.TableHeader.uppercase", Boolean.TRUE, "Aero.popupMinWidth", 180, "Aero.titleBadgeText", "SK",
            // generic
            "Panel.background", panel, "Panel.foreground", text, "Panel.font", plain, "Viewport.background", white, "Viewport.foreground", text, "Viewport.font", plain,
            "Label.font", plain, "Label.foreground", text, "Label.disabledForeground", faint, "Label.background", panel,
            // buttons
            "Button.font", base, "Button.margin", ins(2, 10, 2, 10), "Button.border", new AeroBorders.ButtonBorder(), "Button.rollover", Boolean.TRUE, "Button.textIconGap", 4, "Button.textShiftOffset", 0,
            "Button.background", panel, "Button.foreground", text, "Button.disabledText", col(AeroPalette.DISABLED_FG), "Button.defaultButtonFollowsFocus", Boolean.FALSE,
            "ToggleButton.font", base, "ToggleButton.margin", ins(2, 10, 2, 10), "ToggleButton.border", new AeroBorders.ButtonBorder(), "ToggleButton.rollover", Boolean.TRUE, "ToggleButton.textIconGap", 4, "ToggleButton.textShiftOffset", 0,
            "ToggleButton.background", panel, "ToggleButton.foreground", text, "ToggleButton.disabledText", col(AeroPalette.DISABLED_FG),
            "CheckBox.font", base, "CheckBox.icon", AeroIcons.checkBoxIcon(), "CheckBox.rollover", Boolean.TRUE, "CheckBox.textIconGap", 5, "CheckBox.margin", ins(1, 0, 1, 2), "CheckBox.background", panel, "CheckBox.foreground", text, "CheckBox.border", eb(0, 0, 0, 0),
            "RadioButton.font", base, "RadioButton.icon", AeroIcons.radioIcon(), "RadioButton.rollover", Boolean.TRUE, "RadioButton.textIconGap", 5, "RadioButton.margin", ins(1, 0, 1, 2), "RadioButton.background", panel, "RadioButton.foreground", text, "RadioButton.border", eb(0, 0, 0, 0),
            // text
            "TextField.font", txtFont, "TextField.border", input, "TextField.margin", ins(0, 0, 0, 0), "TextField.background", white, "TextField.foreground", text, "TextField.inactiveForeground", faint, "TextField.inactiveBackground", col(new Color(0xf3f4f6)),
            "TextField.selectionBackground", sel, "TextField.selectionForeground", selFg, "TextField.caretForeground", col(AeroPalette.TEXT_STRONG), "TextField.caretBlinkRate", 530,
            "PasswordField.font", txtFont, "PasswordField.border", input, "PasswordField.margin", ins(0, 0, 0, 0), "PasswordField.background", white, "PasswordField.foreground", text, "PasswordField.inactiveForeground", faint, "PasswordField.inactiveBackground", col(new Color(0xf3f4f6)),
            "PasswordField.selectionBackground", sel, "PasswordField.selectionForeground", selFg, "PasswordField.caretForeground", col(AeroPalette.TEXT_STRONG), "PasswordField.echoChar", '\u25CF', "PasswordField.showRevealButton", Boolean.TRUE,
            "FormattedTextField.font", txtFont, "FormattedTextField.border", input, "FormattedTextField.margin", ins(0, 0, 0, 0), "FormattedTextField.background", white, "FormattedTextField.foreground", text, "FormattedTextField.inactiveForeground", faint,
            "FormattedTextField.inactiveBackground", col(new Color(0xf3f4f6)), "FormattedTextField.selectionBackground", sel, "FormattedTextField.selectionForeground", selFg, "FormattedTextField.caretForeground", col(AeroPalette.TEXT_STRONG),
            "TextArea.font", plain, "TextArea.border", eb(3, 5, 3, 5), "TextArea.margin", ins(0, 0, 0, 0), "TextArea.background", white, "TextArea.foreground", text, "TextArea.inactiveForeground", faint, "TextArea.selectionBackground", sel, "TextArea.selectionForeground", selFg, "TextArea.caretForeground", col(AeroPalette.TEXT_STRONG),
            "TextPane.font", plain, "TextPane.border", eb(3, 5, 3, 5), "TextPane.margin", ins(0, 0, 0, 0), "TextPane.background", white, "TextPane.foreground", text, "TextPane.selectionBackground", sel, "TextPane.selectionForeground", selFg, "TextPane.caretForeground", col(AeroPalette.TEXT_STRONG),
            "EditorPane.font", plain, "EditorPane.border", eb(3, 5, 3, 5), "EditorPane.margin", ins(0, 0, 0, 0), "EditorPane.background", white, "EditorPane.foreground", text, "EditorPane.selectionBackground", sel, "EditorPane.selectionForeground", selFg, "EditorPane.caretForeground", col(AeroPalette.TEXT_STRONG),
            // combo / spinner / list
            "ComboBox.font", txtFont, "ComboBox.border", combo, "ComboBox.background", white, "ComboBox.foreground", text, "ComboBox.disabledBackground", col(new Color(0xf3f4f6)), "ComboBox.disabledForeground", faint,
            "ComboBox.selectionBackground", col(AeroPalette.MI_HOT1), "ComboBox.selectionForeground", col(AeroPalette.MI_HOT_FG), "ComboBox.buttonBackground", panel, "ComboBox.timeFactor", 1000L, "ComboBox.squareButton", Boolean.FALSE,
            "Spinner.font", txtFont, "Spinner.border", combo, "Spinner.background", white, "Spinner.foreground", text, "Spinner.arrowButtonInsets", ins(0, 0, 0, 0), "Spinner.editorBorderPainted", Boolean.FALSE,
            "List.font", AeroFonts.ui(AeroFonts.plain(10.5f)), "List.background", white, "List.foreground", text, "List.selectionBackground", col(AeroPalette.LIST_SEL), "List.selectionForeground", white, "List.border", eb(2, 2, 2, 2),
            "List.focusCellHighlightBorder", eb(0, 0, 0, 0), "List.cellRenderer", new UIDefaults.ActiveValue() { public Object createValue(UIDefaults tt) { return new aero.ui.AeroListCellRenderer(); } },
            // table
            "Table.font", plain, "Table.foreground", text, "Table.background", white, "Table.gridColor", AeroPalette.GRID, "Table.selectionForeground", col(AeroPalette.SEL_FG), "Table.selectionBackground", col(AeroPalette.SEL1),
            "Table.rowHeight", 22, "Table.scrollPaneBorder", new AeroBorders.LineBorder1(AeroPalette.GROUP_BORDER), "Table.focusCellHighlightBorder", eb(0, 6, 0, 6), "Table.cellNoFocusBorder", eb(0, 6, 0, 6),
            "Table.ascendingSortIcon", AeroIcons.triangle(SwingConstants.NORTH, 7, 4, AeroPalette.TEXT_MUTED), "Table.descendingSortIcon", AeroIcons.triangle(SwingConstants.SOUTH, 7, 4, AeroPalette.TEXT_MUTED),
            "TableHeader.font", bold, "TableHeader.foreground", text, "TableHeader.background", col(AeroPalette.TH1), "TableHeader.cellBorder", eb(0, 0, 0, 0),
            // tree
            "Tree.font", small, "Tree.background", white, "Tree.foreground", text, "Tree.textBackground", white, "Tree.textForeground", text, "Tree.selectionBackground", col(AeroPalette.TREE_SEL_BG), "Tree.selectionForeground", col(AeroPalette.TREE_SEL_FG),
            "Tree.selectionBorderColor", col(AeroPalette.TREE_SEL_BORDER), "Tree.rowHeight", 20, "Tree.paintLines", Boolean.FALSE, "Tree.drawsFocusBorderAroundIcon", Boolean.FALSE, "Tree.leftChildIndent", 8, "Tree.rightChildIndent", 10, "Tree.hash", col(AeroPalette.SEP),
            "Tree.openIcon", AeroIcons.folder(true), "Tree.closedIcon", AeroIcons.folder(false), "Tree.leafIcon", AeroIcons.javaFile(), "Tree.expandedIcon", AeroIcons.expandIcon(true), "Tree.collapsedIcon", AeroIcons.expandIcon(false),
            "Tree.cellRenderer", new UIDefaults.ActiveValue() { public Object createValue(UIDefaults tt) { return new aero.ui.AeroTreeCellRenderer(); } },
            // scroll
            "ScrollBar.width", 7, "ScrollBar.minimumThumbSize", new DimensionUIResource(18, 18), "ScrollBar.maximumThumbSize", new DimensionUIResource(4096, 4096), "ScrollBar.background", col(AeroPalette.SB_TRACK), "ScrollBar.track", col(AeroPalette.SB_TRACK),
            "ScrollBar.thumb", col(AeroPalette.SB1), "ScrollBar.incrementButtonGap", 0, "ScrollBar.decrementButtonGap", 0, "ScrollBar.squareButtons", Boolean.FALSE, "ScrollBar.thumbHeight", 14, "ScrollBar.thumbShadow", col(AeroPalette.SB_BORDER), "ScrollBar.thumbHighlight", white, "ScrollBar.thumbDarkShadow", col(AeroPalette.SB_BORDER),
            "ScrollPane.border", new AeroBorders.LineBorder1(AeroPalette.GROUP_BORDER), "ScrollPane.background", white, "ScrollPane.font", plain, "ScrollPane.viewportBorder", null,
            // slider / progress
            "Slider.font", AeroFonts.ui(AeroFonts.mono(9f)), "Slider.foreground", text, "Slider.background", panel, "Slider.focusInsets", ins(0, 0, 0, 0), "Slider.tickColor", col(AeroPalette.TEXT_FAINT), "Slider.trackWidth", 4, "Slider.horizontalSize", new DimensionUIResource(200, 25), "Slider.verticalSize", new DimensionUIResource(25, 200), "Slider.minimumHorizontalSize", new DimensionUIResource(36, 21), "Slider.minimumVerticalSize", new DimensionUIResource(21, 36),
            "ProgressBar.font", AeroFonts.ui(AeroFonts.bold(10f)), "ProgressBar.foreground", col(AeroPalette.PG1), "ProgressBar.background", col(AeroPalette.PG_TRACK), "ProgressBar.selectionForeground", text, "ProgressBar.selectionBackground", text,
            "ProgressBar.cycleTime", 1400, "ProgressBar.repaintInterval", 40, "ProgressBar.border", eb(0, 0, 0, 0), "ProgressBar.cellLength", 1, "ProgressBar.cellSpacing", 0,
            // split / tabs
            "SplitPane.dividerSize", 8, "SplitPane.border", eb(0, 0, 0, 0), "SplitPane.background", panel, "SplitPaneDivider.border", eb(0, 0, 0, 0), "SplitPane.oneTouchButtonSize", 8, "SplitPane.oneTouchOffset", 2,
            "TabbedPane.font", base, "TabbedPane.foreground", col(AeroPalette.TAB_FG), "TabbedPane.background", col(AeroPalette.TAB1), "TabbedPane.tabsOverlapBorder", Boolean.TRUE, "TabbedPane.contentOpaque", Boolean.FALSE, "TabbedPane.tabInsets", ins(4, 10, 4, 10),
            "TabbedPane.selectedTabPadInsets", ins(0, 0, 0, 0), "TabbedPane.tabAreaInsets", ins(4, 8, 0, 8), "TabbedPane.contentBorderInsets", ins(1, 1, 1, 1), "TabbedPane.tabRunOverlay", 0, "TabbedPane.textIconGap", 4, "TabbedPane.tabAreaBackground", col(AeroPalette.STRIP_BG),
            "TabbedPane.selected", col(AeroPalette.ACT0), "TabbedPane.light", col(AeroPalette.TAB_BORDER), "TabbedPane.highlight", white, "TabbedPane.shadow", col(AeroPalette.TAB_BORDER), "TabbedPane.darkShadow", col(AeroPalette.TAB_BORDER), "TabbedPane.focus", text,
            // tooltip
            "ToolTip.font", small, "ToolTip.foreground", text, "ToolTip.background", col(AeroPalette.TIP_BG), "ToolTip.border", new AeroBorders.TooltipBorder(), "ToolTip.hideAccelerator", Boolean.FALSE,
            // menus
            "MenuBar.font", plain, "MenuBar.background", col(AeroPalette.MB1), "MenuBar.foreground", text, "MenuBar.border", eb(0, 4, 1, 4), "MenuBar.windowBindings", new Object[]{"F10", "takeFocus"},
            "Menu.font", plain, "Menu.margin", ins(3, 8, 3, 8), "Menu.foreground", text, "Menu.background", col(AeroPalette.POP0), "Menu.selectionBackground", col(AeroPalette.MI_HOT1), "Menu.selectionForeground", col(AeroPalette.MI_HOT_FG), "Menu.border", eb(0, 0, 0, 0), "Menu.checkIcon", null, "Menu.arrowIcon", null, "Menu.menuPopupOffsetX", 0, "Menu.menuPopupOffsetY", 0, "Menu.submenuPopupOffsetX", -2, "Menu.submenuPopupOffsetY", -5, "Menu.crossMenuMnemonic", Boolean.TRUE,
            "MenuItem.font", plain, "MenuItem.margin", ins(3, 6, 3, 8), "MenuItem.foreground", text, "MenuItem.background", col(AeroPalette.POP0), "MenuItem.selectionBackground", col(AeroPalette.MI_HOT1), "MenuItem.selectionForeground", col(AeroPalette.MI_HOT_FG), "MenuItem.border", eb(0, 0, 0, 0), "MenuItem.checkIcon", null, "MenuItem.arrowIcon", null,
            "MenuItem.acceleratorFont", AeroFonts.ui(AeroFonts.mono(10f)), "MenuItem.acceleratorForeground", faint, "MenuItem.acceleratorSelectionForeground", faint, "MenuItem.acceleratorDelimiter", "+", "MenuItem.disabledForeground", faint,
            "CheckBoxMenuItem.font", plain, "CheckBoxMenuItem.margin", ins(3, 6, 3, 8), "CheckBoxMenuItem.foreground", text, "CheckBoxMenuItem.background", col(AeroPalette.POP0), "CheckBoxMenuItem.selectionBackground", col(AeroPalette.MI_HOT1), "CheckBoxMenuItem.selectionForeground", col(AeroPalette.MI_HOT_FG), "CheckBoxMenuItem.border", eb(0, 0, 0, 0), "CheckBoxMenuItem.checkIcon", null, "CheckBoxMenuItem.arrowIcon", null, "CheckBoxMenuItem.acceleratorFont", AeroFonts.ui(AeroFonts.mono(10f)), "CheckBoxMenuItem.acceleratorForeground", faint, "CheckBoxMenuItem.acceleratorSelectionForeground", faint,
            "RadioButtonMenuItem.font", plain, "RadioButtonMenuItem.margin", ins(3, 6, 3, 8), "RadioButtonMenuItem.foreground", text, "RadioButtonMenuItem.background", col(AeroPalette.POP0), "RadioButtonMenuItem.selectionBackground", col(AeroPalette.MI_HOT1), "RadioButtonMenuItem.selectionForeground", col(AeroPalette.MI_HOT_FG), "RadioButtonMenuItem.border", eb(0, 0, 0, 0), "RadioButtonMenuItem.checkIcon", null, "RadioButtonMenuItem.arrowIcon", null, "RadioButtonMenuItem.acceleratorFont", AeroFonts.ui(AeroFonts.mono(10f)), "RadioButtonMenuItem.acceleratorForeground", faint, "RadioButtonMenuItem.acceleratorSelectionForeground", faint,
            "PopupMenu.border", new AeroBorders.PopupBorder(), "PopupMenu.background", col(AeroPalette.POP0), "PopupMenu.foreground", text, "PopupMenu.font", plain, "PopupMenu.consumeEventOnClose", Boolean.FALSE,
            // toolbar
            "ToolBar.font", base, "ToolBar.background", col(AeroPalette.T2_1), "ToolBar.foreground", text, "ToolBar.border", eb(3, 8, 4, 8), "ToolBar.isRollover", Boolean.FALSE, "ToolBar.separatorSize", null,
            "ToolBar.dockingBackground", col(AeroPalette.T2_1), "ToolBar.floatingBackground", col(AeroPalette.T2_1), "ToolBar.dockingForeground", col(AeroPalette.FOCUS), "ToolBar.floatingForeground", col(AeroPalette.FOCUS),
            // internal frame / desktop
            "InternalFrame.border", new AeroBorders.InternalFrameBorder(), "InternalFrame.font", bold, "InternalFrame.background", col(AeroPalette.FRAME_BG), "InternalFrame.titleFont", bold, "InternalFrame.activeTitleBackground", col(AeroPalette.IF2), "InternalFrame.activeTitleForeground", col(AeroPalette.FRAME_TITLE_FG),
            "InternalFrame.inactiveTitleBackground", col(AeroPalette.STRIP_BG), "InternalFrame.inactiveTitleForeground", col(AeroPalette.TEXT_MUTED), "InternalFrame.paletteTitleHeight", 18, "InternalFrame.paletteCloseIcon", null,
            "InternalFrame.icon", AeroIcons.lucide("layout-grid", 13, AeroPalette.c(0x1e3a8a), 2f), "InternalFrame.closeIcon", null, "InternalFrame.maximizeIcon", null, "InternalFrame.iconifyIcon", null, "InternalFrame.minimizeIcon", null,
            "InternalFrame.windowBindings", null, "InternalFrame.borderColor", col(AeroPalette.FRAME_BORDER), "InternalFrame.borderShadow", col(AeroPalette.FRAME_BORDER), "InternalFrame.borderDarkShadow", col(AeroPalette.FRAME_BORDER), "InternalFrame.borderHighlight", white, "InternalFrame.borderLight", white,
            "DesktopIcon.border", eb(0, 0, 0, 0), "Desktop.background", col(AeroPalette.DESK1), "Desktop.ancestorInputMap", null,
            // root / option / file / color
            "RootPane.frameBorder", new AeroBorders.FrameBorder(), "RootPane.background", white, "RootPane.plainDialogBorder", new AeroBorders.FrameBorder(), "RootPane.informationDialogBorder", new AeroBorders.FrameBorder(), "RootPane.errorDialogBorder", new AeroBorders.FrameBorder(),
            "RootPane.colorChooserDialogBorder", new AeroBorders.FrameBorder(), "RootPane.fileChooserDialogBorder", new AeroBorders.FrameBorder(), "RootPane.questionDialogBorder", new AeroBorders.FrameBorder(), "RootPane.warningDialogBorder", new AeroBorders.FrameBorder(),
            "OptionPane.font", plain, "OptionPane.messageFont", AeroFonts.ui(AeroFonts.medium(11.5f)), "OptionPane.buttonFont", base, "OptionPane.background", panel, "OptionPane.foreground", text, "OptionPane.messageForeground", text, "OptionPane.border", eb(14, 16, 8, 16),
            "OptionPane.messageAreaBorder", eb(0, 0, 8, 0), "OptionPane.buttonAreaBorder", eb(10, 0, 0, 0), "OptionPane.minimumSize", new DimensionUIResource(260, 90), "OptionPane.isYesLast", Boolean.TRUE, "OptionPane.sameSizeButtons", Boolean.TRUE, "OptionPane.buttonPadding", 6, "OptionPane.buttonClickThreshhold", 500,
            "OptionPane.errorIcon", AeroIcons.status(JOptionPane.ERROR_MESSAGE, 32), "OptionPane.informationIcon", AeroIcons.status(JOptionPane.INFORMATION_MESSAGE, 32), "OptionPane.warningIcon", AeroIcons.status(JOptionPane.WARNING_MESSAGE, 32), "OptionPane.questionIcon", AeroIcons.status(JOptionPane.QUESTION_MESSAGE, 32),
            "FileChooser.font", plain, "FileChooser.newFolderIcon", AeroIcons.lucide("folder-plus", 16, new Color(0xb45309), 1.7f), "FileChooser.upFolderIcon", AeroIcons.lucide("folder-up", 16, new Color(0xb45309), 1.7f), "FileChooser.homeFolderIcon", AeroIcons.lucide("house", 16, AeroPalette.c(0x1d4ed8), 1.7f),
            "FileChooser.detailsViewIcon", AeroIcons.lucide("table", 16, AeroPalette.TEXT_DIM, 1.7f), "FileChooser.listViewIcon", AeroIcons.lucide("list", 16, AeroPalette.TEXT_DIM, 1.7f), "FileChooser.computerIcon", AeroIcons.lucide("monitor", 16, AeroPalette.TEXT_DIM, 1.7f),
            "FileChooser.hardDriveIcon", AeroIcons.lucide("hard-drive", 16, AeroPalette.TEXT_DIM, 1.7f), "FileChooser.floppyDriveIcon", AeroIcons.lucide("save", 16, AeroPalette.TEXT_DIM, 1.7f), "FileView.directoryIcon", AeroIcons.folder(false), "FileView.fileIcon", AeroIcons.document(), "FileView.computerIcon", AeroIcons.lucide("monitor", 16, AeroPalette.TEXT_DIM, 1.7f),
            "FileView.hardDriveIcon", AeroIcons.lucide("hard-drive", 16, AeroPalette.TEXT_DIM, 1.7f), "FileView.floppyDriveIcon", AeroIcons.lucide("save", 16, AeroPalette.TEXT_DIM, 1.7f),
            "ColorChooser.font", plain, "ColorChooser.background", panel, "ColorChooser.foreground", text, "ColorChooser.swatchesDefaultRecentColor", white,
        };
        t.putDefaults(d);
    }
}
