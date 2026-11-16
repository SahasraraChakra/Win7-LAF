# Win7-LAF - Windows 7/Vista Aero Glass look and feel for Java Swing


#### Important Note : Purely AI coded. Bugs and Errors may exist. Please use with caution !!!

## Screens

![DShowcase](screens/win7-laf_showcase.png)


Standalone `BasicLookAndFeel` (no FlatLaf dependency), built from the `stitch_win7-laf` designs. Package `aero`, Java 11+ (tested Java 21; no preview APIs).

```java
public static void main(String[] a) {
    AeroLookAndFeel.install();          // LAF + custom Aero titlebar on every JFrame/JDialog
    SwingUtilities.invokeLater(() -> { JFrame f = new JFrame("My App - [subtitle]"); ... f.setVisible(true); });
}
```
Add `win7-laf.jar` to the classpath (Inter font + Lucide icons are bundled inside). NetBeans: Libraries > Add JAR.

## Title bar (exact from design CSS)
30px high, 8px padding, 16px "SK" badge (or your frame icon via `setIconImage`), bold app name + semibold subtitle (split on the first " - "), white glow,
minimise/maximise 28x19, close 44x21 red, 1px glass border, rounded 6px window, 5px resize band, drag, double-click maximise, right-click system menu.
Badge text: `UIManager.put("Aero.titleBadgeText","AB")`.

## Button styles
`AeroButtons.style(btn, "primary")` or `btn.putClientProperty("JButton.style","primary")`
standard, primary, accent (print), success, error, danger (hold), warning, info, flat, link, outline, segment, caption, close.
Aliases: default/ribbon, print, cancel/abort, hold, commit/add, toolbar/ghost, secondary/neutral.
Segmented groups `AeroButtons.segmented(...)`, split button `AeroButtons.split(...)`, icon buttons `AeroButtons.icon("save", tip, style)`.
Custom: `AeroButtonStyle.register("brand", state -> new Spec()...)`.

## Client properties
JButton.style, JButton.segmentPosition | JTextField.style (info, highlight) | JComponent.outline (error, warning, success) | JTextField.placeholderText,
showClearButton, leadingIcon, trailingIcon | JPasswordField.showRevealButton | JCheckBox.indeterminate | JLabel.style (title, muted, error, success...) |
JPanel.style (ribbon, toolbar, statusbar, glass, header, group, tabbody, navy) | JToolBar.style (ribbon) | JProgressBar.style (blue, warning, error, segmented, mini) |
JSlider.trackColor, JSlider.trackFill | JMenuItem.header, JMenuItem.destructive | JRadioButtonMenuItem.checkStyle

## Runtime theming
`AeroLookAndFeel.applyTint(Tint.SEA_TEAL)`, `applyCustomTint(Color)`, `setCompact(true)`.

## Extra components (not in stock Swing)
AeroRangeSlider (dual thumb), AeroTagField (chips), AeroStatusBar, AeroBorders.group("Title").

## All 43 Swing UI delegates are implemented
Button, ToggleButton, CheckBox, RadioButton, CheckBoxMenuItem, RadioButtonMenuItem, MenuItem, Menu, MenuBar, PopupMenu, PopupMenuSeparator, Separator, ToolBarSeparator, ToolBar,
TextField, PasswordField, FormattedTextField, TextArea, TextPane, EditorPane, ComboBox, List, ScrollBar, ScrollPane, Slider, Spinner, SplitPane, TabbedPane, Table, TableHeader, Tree,
ProgressBar, Label, Panel, Viewport, ToolTip, OptionPane, FileChooser, ColorChooser, InternalFrame, DesktopPane, DesktopIcon, RootPane.

Run `java -cp win7-laf.jar aero.demo.Showcase` (the design rebuilt) or `aero.demo.Gallery` (headless catalogue render).
