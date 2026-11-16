package aero.demo;

import aero.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.tree.*;

/** Every Swing component rendered with the Aero LAF (used as a smoke test + visual catalogue). */
public class Gallery {
    static JPanel card(String t, JComponent c) { JPanel p = new JPanel(new BorderLayout()); p.setBorder(AeroBorders.group(t)); p.add(c); return p; }
    public static JComponent build() {
        JPanel root = new JPanel(new GridLayout(0, 3, 8, 8)); root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // all button styles
        JPanel bs = new JPanel(new GridLayout(0, 4, 4, 4));
        for (String s : new String[]{"standard", "primary", "accent", "success", "error", "danger", "warning", "info", "flat", "link", "outline"}) bs.add(AeroButtons.create(s, s));
        JButton dis = AeroButtons.create("disabled", "primary"); dis.setEnabled(false); bs.add(dis);
        JToggleButton tg = AeroButtons.toggle("toggle on", "standard"); tg.setSelected(true); bs.add(tg); bs.add(AeroButtons.toggle("toggle off", "standard"));
        bs.add(AeroButtons.icon("save", "Save", "flat")); bs.add(AeroButtons.icon("trash", "Delete", "error")); bs.add(AeroButtons.segmented(1, "A", "B", "C"));
        root.add(card("JButton / JToggleButton styles", bs));
        JPanel cb = new JPanel(new GridLayout(0, 1)); JCheckBox c1 = new JCheckBox("Checked", true), c2 = new JCheckBox("Unchecked"), c3 = new JCheckBox("Disabled"); c3.setEnabled(false); 
        JCheckBox ind = new JCheckBox("Indeterminate"); ind.putClientProperty("JCheckBox.indeterminate", true); JRadioButton r1 = new JRadioButton("Radio on", true), r2 = new JRadioButton("Radio off"), r3 = new JRadioButton("Radio disabled"); r3.setEnabled(false);
        ButtonGroup g = new ButtonGroup(); g.add(r1); g.add(r2); for (JComponent c : new JComponent[]{c1, c2, ind, c3, r1, r2, r3}) cb.add(c); root.add(card("JCheckBox / JRadioButton", cb));
        JPanel tf = new JPanel(new GridLayout(0, 1, 0, 4)); tf.add(new JTextField("JTextField")); JTextField ph = new JTextField(); ph.putClientProperty("JTextField.placeholderText", "Placeholder..."); tf.add(ph);
        JTextField er = new JTextField("error outline"); er.putClientProperty("JComponent.outline", "error"); tf.add(er); JTextField wn = new JTextField("warning outline"); wn.putClientProperty("JComponent.outline", "warning"); tf.add(wn);
        JTextField ok = new JTextField("success outline"); ok.putClientProperty("JComponent.outline", "success"); tf.add(ok); tf.add(new JPasswordField("password")); JTextField dsb = new JTextField("disabled"); dsb.setEnabled(false); tf.add(dsb);
        JTextField cl = new JTextField("clear button"); cl.putClientProperty("JTextField.showClearButton", true); tf.add(cl); JFormattedTextField ft = new JFormattedTextField(java.text.NumberFormat.getInstance()); ft.setValue(1234.5); tf.add(ft);
        root.add(card("Text fields", tf));
        JPanel co = new JPanel(new GridLayout(0, 1, 0, 4)); co.add(new JComboBox<>(new String[]{"JComboBox", "Two", "Three"})); JComboBox<String> ec = new JComboBox<>(new String[]{"Editable", "B"}); ec.setEditable(true); co.add(ec);
        JComboBox<String> dc = new JComboBox<>(new String[]{"Disabled"}); dc.setEnabled(false); co.add(dc); co.add(new JSpinner(new SpinnerNumberModel(5, 0, 10, 1))); co.add(new JSpinner(new SpinnerListModel(new String[]{"Alpha", "Beta"})));
        JProgressBar pb = new JProgressBar(0, 100); pb.setValue(60); pb.setStringPainted(true); co.add(pb); JProgressBar pw = new JProgressBar(0, 100); pw.setValue(40); pw.putClientProperty("JProgressBar.style", "warning"); co.add(pw); JProgressBar pe = new JProgressBar(0, 100); pe.setValue(80); pe.putClientProperty("JProgressBar.style", "error"); co.add(pe);
        root.add(card("Combo / Spinner / Progress", co));
        JTextArea ta = new JTextArea("JTextArea\nin a scroll pane\n\n\n\n\n\n\n\n\nmore lines"); root.add(card("JTextArea + JScrollPane", new JScrollPane(ta)));
        JEditorPane ep = new JEditorPane("text/html", "<html><body style='font-family:Inter'><h3>JEditorPane</h3><p>HTML <b>content</b> and <a href='#'>link</a></p></body></html>"); root.add(card("JEditorPane", new JScrollPane(ep)));
        JTextPane tp = new JTextPane(); tp.setText("JTextPane styled text"); root.add(card("JTextPane", new JScrollPane(tp)));
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT); tabs.addTab("Left 1", new JLabel("  body")); tabs.addTab("Left 2", new JLabel("  body")); tabs.addTab("Disabled", new JLabel("x")); tabs.setEnabledAt(2, false); root.add(card("JTabbedPane (LEFT)", tabs));
        JTabbedPane tb = new JTabbedPane(JTabbedPane.BOTTOM); tb.addTab("Bottom 1", new JLabel("  body")); tb.addTab("Bottom 2", new JLabel("  body")); root.add(card("JTabbedPane (BOTTOM)", tb));
        JTabbedPane tr = new JTabbedPane(JTabbedPane.RIGHT); tr.addTab("Right 1", new JLabel("  body")); tr.addTab("Right 2", new JLabel("  body")); root.add(card("JTabbedPane (RIGHT)", tr));
        JTable t = new JTable(new Object[][]{{"a", 1, true}, {"b", 2, false}}, new Object[]{"Name", "N", "Flag"}) ; t.setAutoCreateRowSorter(true); JScrollPane ts = new JScrollPane(t); ts.setColumnHeaderView(t.getTableHeader()); root.add(card("JTable", ts));
        JTree tree = new JTree(); tree.expandRow(1); root.add(card("JTree (default model)", new JScrollPane(tree)));
        JList<String> l = new JList<>(new String[]{"One", "Two", "Three", "Four"}); l.setSelectedIndex(1); root.add(card("JList", new JScrollPane(l)));
        JPanel sl = new JPanel(new GridLayout(0, 1)); JSlider s1 = new JSlider(0, 100, 30); s1.setPaintTicks(true); s1.setMajorTickSpacing(25); s1.setMinorTickSpacing(5); sl.add(s1); JSlider s2 = new JSlider(0, 100, 60); s2.setEnabled(false); sl.add(s2); sl.add(new AeroRangeSlider(0, 100, 20, 70)); root.add(card("JSlider", sl));
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JLabel(" top"), new JLabel(" bottom")); sp.setOneTouchExpandable(true); sp.setDividerLocation(40); root.add(card("JSplitPane (vertical)", sp));
        JPanel misc = new JPanel(new GridLayout(0, 1)); misc.add(new JLabel("JLabel plain")); JLabel mu = new JLabel("muted label"); mu.putClientProperty("JLabel.style", "muted"); misc.add(mu); JLabel er2 = new JLabel("error label"); er2.putClientProperty("JLabel.style", "error"); misc.add(er2);
        JLabel tl = new JLabel("title label"); tl.putClientProperty("JLabel.style", "title"); misc.add(tl); misc.add(new JSeparator()); JLabel tt = new JLabel("tooltip (hover)"); tt.setToolTipText("Aero tooltip"); misc.add(tt); misc.add(new JScrollBar(JScrollBar.HORIZONTAL)); root.add(card("JLabel / JSeparator / JScrollBar", misc));
        JPanel pn = new JPanel(new GridLayout(0, 1, 0, 3)); for (String st : new String[]{"ribbon", "toolbar", "statusbar", "glass", "header", "group"}) { JPanel q = new JPanel(); q.putClientProperty("JPanel.style", st); q.add(new JLabel("JPanel.style = " + st)); pn.add(q); } root.add(card("Panel styles", pn));
        JPanel opt = new JPanel(new GridLayout(0, 1, 0, 3));
        for (int type : new int[]{JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE}) { JOptionPane op = new JOptionPane("Option pane message", type, JOptionPane.OK_CANCEL_OPTION); op.setPreferredSize(new Dimension(260, 80)); opt.add(op); }
        root.add(card("JOptionPane", opt));
        JColorChooser cc = new JColorChooser(new Color(0x70a8d2)); root.add(card("JColorChooser", cc));
        JFileChooser fc = new JFileChooser(new java.io.File("/home")); fc.setControlButtonsAreShown(true); root.add(card("JFileChooser", fc));
        JToolBar tbr = new JToolBar(); tbr.add(AeroButtons.create("Tool", "standard")); tbr.addSeparator(); tbr.add(AeroButtons.icon("printer", "p", "flat")); tbr.add(new JToggleButton("Tog")); JPanel tw = new JPanel(new BorderLayout()); tw.add(tbr, BorderLayout.NORTH); root.add(card("JToolBar", tw));
        return new JScrollPane(root);
    }
    static void layout(Component c) { if (c instanceof Container) { ((Container) c).doLayout(); for (Component k : ((Container) c).getComponents()) layout(k); } }
    public static void main(String[] a) throws Exception {
        System.setProperty("java.awt.headless", "true"); AeroLookAndFeel.install();
        SwingUtilities.invokeAndWait(() -> { try {
            JComponent g = build(); JScrollPane sp = (JScrollPane) g; Component v = sp.getViewport().getView(); v.setSize(1500, 3300);
            layout(v); layout(v); v.setSize(1500, v.getPreferredSize().height); layout(v); layout(v);
            BufferedImage img = new BufferedImage(1500, v.getHeight(), BufferedImage.TYPE_INT_ARGB); Graphics2D g2 = img.createGraphics(); g2.setColor(AeroPalette.PANEL_BG); g2.fillRect(0, 0, 1500, img.getHeight()); v.paint(g2); g2.dispose(); ImageIO.write(img, "png", new java.io.File(a[0]));
        } catch (Throwable e) { e.printStackTrace(); } }); System.exit(0);
    }
}
