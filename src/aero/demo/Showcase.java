package aero.demo;

import aero.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/** Rebuilds the design (ShopKeeper QuickBill - Win7 Aero Glass test suite) with the Aero LAF. */
public class Showcase {
    static GridBagConstraints gc(int x, int y, int w, double wx) { GridBagConstraints c = new GridBagConstraints(); c.gridx = x; c.gridy = y; c.gridwidth = w; c.weightx = wx; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(2, 3, 2, 3); c.anchor = GridBagConstraints.WEST; return c; }
    static JLabel lbl(String s) { JLabel l = new JLabel(s); l.setFont(AeroFonts.bold(10f)); l.setForeground(AeroPalette.TEXT_2); return l; }
    static JPanel group(String title, LayoutManager lm) { JPanel p = new JPanel(lm); p.setOpaque(false); p.setBorder(new CompoundBorderX(AeroBorders.group(title))); return p; }
    static class CompoundBorderX extends javax.swing.border.CompoundBorder { CompoundBorderX(javax.swing.border.Border b) { super(b, BorderFactory.createEmptyBorder(0, 0, 0, 0)); } }

    public static final String TITLE = "ShopKeeper QuickBill\u2122 - [Complete Java Swing & Aero Glass Test Suite v4.5]";
    public static void populate(JRootPane f) {
        // ---- menu bar
        JMenuBar mb = new JMenuBar();
        for (String s : new String[]{"File", "Edit", "View", "Components", "Windows"}) { JMenu m = new JMenu(s); m.setMnemonic(s.charAt(0)); m.add(new JMenuItem("Placeholder")); mb.add(m); }
        JMenu laf = new JMenu("Look & Feel"); laf.setMnemonic('F');
        JRadioButtonMenuItem a = new JRadioButtonMenuItem("FlatLaf Aero Glass", true), b = new JRadioButtonMenuItem("Windows Classic 98"), c = new JRadioButtonMenuItem("Nimbus Modern Vector");
        for (JRadioButtonMenuItem r : new JRadioButtonMenuItem[]{a, b, c}) r.putClientProperty("JRadioButtonMenuItem.checkStyle", true);
        a.setAccelerator(KeyStroke.getKeyStroke("F5")); b.setAccelerator(KeyStroke.getKeyStroke("ctrl 1")); c.setAccelerator(KeyStroke.getKeyStroke("ctrl 2"));
        ButtonGroup bg = new ButtonGroup(); bg.add(a); bg.add(b); bg.add(c); laf.add(a); laf.add(b); laf.add(c); laf.addSeparator();
        JMenu tints = new JMenu("Aero Tint Palettes"); ButtonGroup tg = new ButtonGroup();
        for (AeroPalette.Tint t : AeroPalette.Tint.values()) { JRadioButtonMenuItem ti = new JRadioButtonMenuItem(t.label + (t == AeroPalette.Tint.SKY_BLUE ? " (Active)" : ""), t == AeroPalette.Tint.SKY_BLUE); ti.putClientProperty("JRadioButtonMenuItem.checkStyle", true); ti.addActionListener(e -> AeroLookAndFeel.applyTint(t)); tg.add(ti); tints.add(ti); }
        tints.addSeparator(); JMenuItem hex = new JMenuItem("Custom Hex Tint..."); hex.setAccelerator(KeyStroke.getKeyStroke("alt T"));
        hex.addActionListener(e -> { Color k = JColorChooser.showDialog(null, "Custom Tint", Color.decode("#70A8D2")); if (k != null) AeroLookAndFeel.applyCustomTint(k); }); tints.add(hex);
        laf.add(tints); laf.addSeparator();
        JCheckBoxMenuItem glass = new JCheckBoxMenuItem("Enable Glass Blur (DWM)", true), shadows = new JCheckBoxMenuItem("Show Drop Shadows", true), compact = new JCheckBoxMenuItem("Compact Native Mode");
        glass.setAccelerator(KeyStroke.getKeyStroke("ctrl G")); compact.addActionListener(e -> AeroLookAndFeel.setCompact(compact.isSelected()));
        laf.add(glass); laf.add(shadows); laf.add(compact); laf.addSeparator();
        JMenuItem hdr = new JMenuItem("Tab Placement"); hdr.setEnabled(false); hdr.putClientProperty("JMenuItem.header", true); laf.add(hdr);
        JRadioButtonMenuItem top = new JRadioButtonMenuItem("Top Tabs", true), bot = new JRadioButtonMenuItem("Bottom Tabs"); ButtonGroup pg = new ButtonGroup(); pg.add(top); pg.add(bot); laf.add(top); laf.add(bot); laf.addSeparator();
        JMenuItem closeAll = new JMenuItem("Close All Panels"); closeAll.setAccelerator(KeyStroke.getKeyStroke("alt F4")); closeAll.putClientProperty("JMenuItem.destructive", true); laf.add(closeAll);
        mb.add(laf); JMenu help = new JMenu("Help"); help.setMnemonic('H'); help.add(new JMenuItem("About")); mb.add(help);
        mb.add(Box.createHorizontalGlue());
        JButton tokens = AeroButtons.create("6834  Tokens Available", "warning"); tokens.setIcon(AeroIcons.lucide("coins", 12, new Color(0xd97706))); tokens.setFont(AeroFonts.medium(10.5f)); tokens.setFocusable(false); tokens.setMargin(new Insets(0, 6, 0, 6)); tokens.putClientProperty("JButton.noMinHeight", "true"); mb.add(tokens);
        JLabel mdi = new JLabel("  MDI Multi-Pane Active  "); mdi.setFont(AeroFonts.medium(10f)); mdi.setForeground(AeroPalette.TEXT_MUTED); mb.add(mdi);
        f.setJMenuBar(mb);

        // ---- ribbon + secondary toolbar
        JToolBar ribbon = new JToolBar(); ribbon.putClientProperty("JToolBar.style", "ribbon");
        JLabel brand = new JLabel("<html><span style='color:#0284c7'><b><i>ShopKeeper</i></b></span><span style='color:#0f172a'><b>Quick</b></span><span style='color:#d97706'><b><i>Bill</i></b></span><sup style='font-size:7px;color:#d97706'>TM</sup></html>");
        brand.setFont(AeroFonts.bold(18f)); ribbon.add(brand); ribbon.add(Box.createHorizontalGlue());
        ribbon.add(tb("Billing", "pen-tool", Color.WHITE, "primary")); ribbon.add(Box.createHorizontalStrut(6)); ribbon.add(tb("Report", "pie-chart", new Color(0xe11d48), null)); ribbon.add(Box.createHorizontalStrut(6));
        ribbon.add(tb("Parties", "users", new Color(0xb45309), null)); ribbon.add(Box.createHorizontalStrut(6)); ribbon.add(tb("Products", "package", new Color(0x1d4ed8), null)); ribbon.add(Box.createHorizontalStrut(6));
        ribbon.add(tb(null, "wrench", AeroPalette.TEXT_DIM, null)); ribbon.add(Box.createHorizontalStrut(6)); ribbon.add(tb(null, "info", AeroPalette.TEXT_DIM, null));
        JToolBar sec = new JToolBar();
        sec.add(tb("New", "file-plus", new Color(0x0284c7), null)); sec.add(Box.createHorizontalStrut(6)); sec.add(tb(null, "save", new Color(0x2563eb), null)); sec.add(Box.createHorizontalStrut(6));
        sec.add(tb(null, "folder", new Color(0xf59e0b), null)); sec.add(Box.createHorizontalStrut(6)); sec.add(tb("Hold", "flag", new Color(0xe11d48), "danger")); sec.addSeparator();
        sec.add(lbl("Mode ")); JComboBox<String> mode = new JComboBox<>(new String[]{"Sale", "Return", "Estimate"}); mode.setMaximumSize(new Dimension(96, 23)); sec.add(mode); sec.add(Box.createHorizontalStrut(10));
        sec.add(lbl("Bill No ")); JTextField bill = new JTextField("2026_11-0001", 11); bill.setEditable(false); bill.putClientProperty("JTextField.style", "info"); bill.setFont(AeroFonts.monoBold(11f)); bill.setForeground(new Color(0x1e3a8a)); bill.setMaximumSize(new Dimension(112, 23)); sec.add(bill); sec.add(Box.createHorizontalStrut(10));
        sec.add(lbl("Bill Date ")); JTextField date = new JTextField("08/11/2026", 8); date.setEditable(false); date.setMaximumSize(new Dimension(96, 23)); sec.add(date); JButton dots = AeroButtons.create("...", "standard"); dots.setMargin(new Insets(1, 5, 1, 5)); sec.add(dots);
        sec.add(Box.createHorizontalGlue()); sec.add(lbl("Price Level ")); JComboBox<String> pl = new JComboBox<>(new String[]{"Retail", "Wholesale", "Dealer"}); pl.setMaximumSize(new Dimension(96, 23)); sec.add(pl);
        JPanel north = new JPanel(); north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS)); north.add(ribbon); north.add(sec);

        // ---- MDI workspace
        JDesktopPane desk = new JDesktopPane();
        JInternalFrame f1 = new JInternalFrame("Standard Controls & Swing Component Matrix (JSplitPane & Advanced Controls)", true, true, true, true); f1.setBounds(8, 8, 760, 645); f1.setContentPane(matrix(f)); f1.setVisible(true);
        JInternalFrame f2 = new JInternalFrame("JTable - SKU Ledger / Real-time Grid View", true, true, true, true); f2.setBounds(772, 8, 500, 440); f2.setContentPane(tablePanel()); f2.setVisible(true);
        JInternalFrame f3 = new JInternalFrame("JColorChooser & Aero Glass Tint Engine", true, true, false, false); f3.setBounds(772, 455, 500, 197); f3.setContentPane(tintPanel()); f3.setVisible(true);
        desk.add(f1); desk.add(f2); desk.add(f3); try { f2.setSelected(true); } catch (Exception e) { }

        AeroStatusBar st = new AeroStatusBar();
        st.addLeft(AeroStatusBar.dot(new Color(0x10b981), "Ready")).addLeft(AeroStatusBar.separator()).addLeft(AeroStatusBar.text("DB: H2")).addLeft(AeroStatusBar.separator()).addLeft(AeroStatusBar.dot(new Color(0x10b981), "Db Status: Connected")).addLeft(AeroStatusBar.separator()).addLeft(AeroStatusBar.text("User: admin [Admin Master]")).addLeft(AeroStatusBar.separator());
        JProgressBar mem = new JProgressBar(0, 100); mem.setValue(44); mem.putClientProperty("JProgressBar.style", "mini"); mem.setPreferredSize(new Dimension(64, 8)); st.addLeft(mem).addLeft(AeroStatusBar.text("44% Memory"));
        JComboBox<String> theme = new JComboBox<>(new String[]{"FlatLaf Windows 7 Aero Glass", "Windows Classic 98 / 2000", "Nimbus Modern Vector"}); theme.setFont(AeroFonts.plain(10f)); st.addRight(AeroStatusBar.text("Theme:")).addRight(theme).addRight(AeroStatusBar.separator()).addRight(AeroStatusBar.text("\u00A9 J Soft Bharath"));

        f.getContentPane().add(north, BorderLayout.NORTH); f.getContentPane().add(desk, BorderLayout.CENTER); f.getContentPane().add(st, BorderLayout.SOUTH);
    }
    static JButton tb(String text, String icon, Color ic, String style) { JButton b = new JButton(text, AeroIcons.lucide(icon, 13, ic)); if (style != null) AeroButtons.style(b, style); b.setFocusable(false); if (text == null) b.setMargin(new Insets(2, 8, 2, 8)); b.setMaximumSize(b.getPreferredSize()); return b; }

    static JComponent matrix(Object owner) {
        JTabbedPane tabs = new JTabbedPane(); 
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); sp.setOneTouchExpandable(true); sp.setBorder(null);
        // left: tree + list
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Project Workspace"), src = new DefaultMutableTreeNode("src/com/billing");
        src.add(new DefaultMutableTreeNode("AeroLookAndFeel.java")); src.add(new DefaultMutableTreeNode("TableGridModel.java")); src.add(new DefaultMutableTreeNode("JSplitPaneView.java")); root.add(src);
        DefaultMutableTreeNode r1 = new DefaultMutableTreeNode("resources/aero"); r1.add(new DefaultMutableTreeNode("x")); root.add(r1); DefaultMutableTreeNode r2 = new DefaultMutableTreeNode("lib/flatlaf-core"); r2.add(new DefaultMutableTreeNode("y")); root.add(r2);
        JTree tree = new JTree(root); tree.setRootVisible(true); tree.expandRow(0); tree.expandRow(1); tree.setSelectionRow(2);
        JList<String> list = new JList<>(new String[]{"Beverages & Juices", "Bakery & Snacks", "Dairy & Frozen Foods", "Organic Cereals & Pulses", "Personal Care & Hygiene", "Household Cleaning"}); list.setSelectedIndices(new int[]{0, 2});
        JPanel left = new JPanel(new GridBagLayout()); left.setOpaque(true); left.setBackground(Color.WHITE); left.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        GridBagConstraints c = new GridBagConstraints(); c.fill = GridBagConstraints.BOTH; c.weightx = 1; c.weighty = 1; c.gridx = 0; c.gridy = 0; c.insets = new Insets(0, 0, 8, 0);
        JScrollPane ts = new JScrollPane(tree); ts.setBorder(new CompoundBorderX(AeroBorders.group("JTree Hierarchy"))); left.add(ts, c);
        c.gridy = 1; c.weighty = 0; c.insets = new Insets(0, 0, 0, 0); JScrollPane ls = new JScrollPane(list); ls.setBorder(new CompoundBorderX(AeroBorders.group("JList Multi-Selection"))); ls.setPreferredSize(new Dimension(200, 150)); left.add(ls, c);
        left.setPreferredSize(new Dimension(215, 400)); sp.setLeftComponent(left);
        // right column
        JPanel col = new JPanel(new GridBagLayout()); col.setOpaque(true); col.setBackground(AeroPalette.PANEL_BG); col.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        GridBagConstraints g = new GridBagConstraints(); g.gridx = 0; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(0, 0, 8, 0); int row = 0;
        // A: buttons
        JPanel A = group("Glossy Aero Buttons & FlatLaf Segmented Controls", new GridBagLayout());
        A.add(AeroButtons.create("Standard Button", "standard"), gc(0, 0, 1, 1)); A.add(AeroButtons.create("Primary Default", "primary"), gc(1, 0, 1, 1)); A.add(AeroButtons.create("Commit (F9)", "success"), gc(2, 0, 1, 1)); A.add(AeroButtons.create("Cancel / Abort", "error"), gc(3, 0, 1, 1));
        A.add(AeroButtons.create("Warning", "warning"), gc(0, 1, 1, 1)); A.add(AeroButtons.create("Info", "info"), gc(1, 1, 1, 1)); A.add(AeroButtons.create("Accent / Print", "accent"), gc(2, 1, 1, 1)); JButton dis = AeroButtons.create("Disabled State", "standard"); dis.setEnabled(false); A.add(dis, gc(3, 1, 1, 1));
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); row3.setOpaque(false); row3.add(AeroButtons.segmented(0, "Day", "Week", "Month")); JPopupMenu pm = new JPopupMenu(); pm.add(new JMenuItem("Save As...")); pm.add(new JMenuItem("Save All")); row3.add(AeroButtons.split("Save Document", "standard", pm));
        JCheckBox ck = new JCheckBox("Auto Add [Alt+A]", true); row3.add(ck); JRadioButton rb1 = new JRadioButton("Retail", true), rb2 = new JRadioButton("Wholesale"); ButtonGroup rg = new ButtonGroup(); rg.add(rb1); rg.add(rb2); row3.add(rb1); row3.add(rb2); row3.add(AeroButtons.link("Link button"));
        A.add(row3, gc(0, 2, 4, 1)); g.gridy = row++; col.add(A, g);
        // B: inputs
        JPanel B = group("Interactive Data Inputs & Spinners", new GridBagLayout());
        B.add(lbl("Code"), gc(0, 0, 1, 0)); B.add(lbl("Product Name"), gc(1, 0, 2, 1)); B.add(lbl("Unit / MRP"), gc(3, 0, 1, 0)); B.add(lbl("Quantity"), gc(4, 0, 1, 0));
        B.add(new JTextField("8901023", 7), gc(0, 1, 1, 0)); B.add(new JTextField("Aashirvaad Shudh Chakki Atta 5kg"), gc(1, 1, 2, 1));
        JPanel um = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0)); um.setOpaque(false); JTextField unit = new JTextField("Nos", 2); unit.setEditable(false); JTextField mrp = new JTextField("285.00", 5); mrp.setHorizontalAlignment(JTextField.RIGHT); um.add(unit); um.add(mrp); B.add(um, gc(3, 1, 1, 0));
        JTextField qty = new JTextField("1", 4); qty.setHorizontalAlignment(JTextField.CENTER); qty.putClientProperty("JTextField.style", "highlight"); qty.setFont(AeroFonts.bold(11f)); B.add(qty, gc(4, 1, 1, 0)); B.add(AeroButtons.create("+ Add Item", "success"), gc(5, 1, 1, 0));
        B.add(lbl("Password Field"), gc(0, 2, 2, 0)); B.add(lbl("Discount Rate %"), gc(2, 2, 1, 0)); B.add(lbl("Date [DD/MM/YYYY]"), gc(3, 2, 2, 0)); B.add(lbl("Category Tag Filter"), gc(5, 2, 1, 0));
        JPasswordField pw = new JPasswordField("secret1234"); B.add(pw, gc(0, 3, 2, 0));
        JSpinner disc = new JSpinner(new SpinnerNumberModel(5.8, 0.0, 100.0, 0.5)); disc.setEditor(new JSpinner.NumberEditor(disc, "0.00")); B.add(disc, gc(2, 3, 1, 0));
        JSpinner dt = new JSpinner(new SpinnerDateModel()); dt.setEditor(new JSpinner.DateEditor(dt, "dd/MM/yyyy")); B.add(dt, gc(3, 3, 2, 0));
        AeroTagField tags = new AeroTagField(); tags.addTag("Groceries"); B.add(tags, gc(5, 3, 1, 0));
        JComboBox<String> ecb = new JComboBox<>(new String[]{"Retail Standard", "Wholesale", "Dealer"}); ecb.setEditable(true); B.add(lbl("JComboBox (Editable)"), gc(0, 4, 2, 0)); B.add(ecb, gc(0, 5, 2, 0));
        JTextField tip = new JTextField("Hover for Tip"); tip.setToolTipText("<html><b style='color:#1e3a8a'>Shortcut:</b> Press [F12] to Quick Print Invoice</html>"); B.add(lbl("JToolTip Callout Preview"), gc(2, 4, 3, 0)); B.add(tip, gc(2, 5, 3, 0));
        g.gridy = row++; col.add(B, g);
        // C: sliders
        JPanel C = group("Rich Sliders Matrix (Major/Minor Ticks & Dual Thumb)", new GridBagLayout());
        JSlider s1 = new JSlider(0, 100, 75); s1.setMajorTickSpacing(25); s1.setMinorTickSpacing(5); s1.setPaintTicks(true); s1.setPaintLabels(true); C.add(s1, gc(0, 0, 1, 1));
        AeroRangeSlider rs = new AeroRangeSlider(0, 1000, 150, 850); C.add(rs, gc(0, 1, 1, 1));
        JSlider vs = new JSlider(JSlider.VERTICAL, 0, 100, 65); vs.setMajorTickSpacing(25); vs.setPaintTicks(true); vs.setPaintLabels(true); GridBagConstraints vg = gc(1, 0, 1, 0); vg.gridheight = 2; vg.fill = GridBagConstraints.NONE; vs.setPreferredSize(new Dimension(48, 70)); C.add(vs, vg);
        g.gridy = row++; col.add(C, g);
        // D: progress
        JPanel D = group("JProgressBar & Activity Indicators", new GridBagLayout());
        JProgressBar p1 = new JProgressBar(0, 100); p1.setValue(72); D.add(lbl("Determinate (Cloud Ledger Sync): 72% Completed"), gc(0, 0, 1, 1)); D.add(p1, gc(0, 1, 1, 1));
        JProgressBar p2 = new JProgressBar(); p2.setIndeterminate(true); D.add(lbl("Indeterminate Background Worker"), gc(0, 2, 1, 1)); D.add(p2, gc(0, 3, 1, 1));
        JProgressBar p3 = new JProgressBar(0, 100); p3.setValue(88); p3.putClientProperty("JProgressBar.style", "segmented"); D.add(lbl("Warning / Memory Usage Segment: 88% (High Thrash)"), gc(0, 4, 1, 1)); D.add(p3, gc(0, 5, 1, 1));
        g.gridy = row++; col.add(D, g);
        JScrollPane rsp = new JScrollPane(col); rsp.setBorder(null); rsp.getViewport().setBackground(AeroPalette.PANEL_BG); sp.setRightComponent(rsp); sp.setDividerLocation(215);
        tabs.addTab("Standard Matrix", sp); tabs.addTab("Sliders & Ticks", new JPanel()); tabs.addTab("Spinners & Combos", new JPanel()); tabs.addTab("Tree & List Split", new JPanel());
        JPanel foot = new JPanel(new BorderLayout()); foot.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8)); foot.setBackground(AeroPalette.c(0xe8f0f8)); foot.setOpaque(true);
        JPanel fl = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)); fl.setOpaque(false); fl.add(AeroStatusBar.text("JTabbedPane Bottom:")); JTabbedPane bt = new JTabbedPane(JTabbedPane.BOTTOM); fl.add(AeroButtons.segmented(0, "View 1", "View 2"));
        JPanel fr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0)); fr.setOpaque(false); fr.add(AeroButtons.create("Reset All Components", "standard")); fr.add(AeroButtons.create("Save Component State", "primary"));
        foot.add(fl, BorderLayout.WEST); foot.add(fr, BorderLayout.EAST);
        JPanel all = new JPanel(new BorderLayout()); all.add(tabs, BorderLayout.CENTER); all.add(foot, BorderLayout.SOUTH); return all;
    }

    static JComponent tablePanel() {
        String[] cols = {"S.No", "Flow", "Code", "Product Name", "MRP", "Qty", "Rate", "Value"};
        Object[][] rows = {{"01", "BILL", "89010304", "Fortune Sunlite Refined Sunflower Oil 1L", 155.00, 2, 135.00, 270.00}, {"02", "BILL", "89020411", "India Gate Basmati Rice Feast 5kg", 495.00, 1, 440.00, 440.00},
            {"03", "BILL", "89010588", "Tata Salt Vacuum Evaporated Iodized 1kg", 28.00, 4, 25.00, 100.00}, {"04", "BILL", "89012490", "Premium Unpolished Toor Dal 2kg", 340.00, 2, 310.00, 620.00}, {"05", "BILL", "89012300", "Aashirvaad Shudh Chakki Atta 5kg", 285.00, 1, 260.00, 260.00}};
        JTable t = new JTable(new DefaultTableModel(rows, cols) { public boolean isCellEditable(int r, int c) { return false; } public Class<?> getColumnClass(int c) { return getValueAt(0, c).getClass(); } });
        int[] w = {36, 44, 70, 220, 54, 34, 54, 60}; for (int i = 0; i < w.length; i++) t.getColumnModel().getColumn(i).setPreferredWidth(w[i]); t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        t.setAutoCreateRowSorter(true); t.setRowSelectionInterval(2, 2);
        DefaultTableCellRenderer nr = new DefaultTableCellRenderer() { { setHorizontalAlignment(RIGHT); } protected void setValue(Object v) { setText(v instanceof Number && v instanceof Double ? String.format("%.2f", v) : String.valueOf(v)); } }; t.setDefaultRenderer(Double.class, nr);
        JScrollPane sc = new JScrollPane(t); sc.setBorder(null); sc.setColumnHeaderView(t.getTableHeader());
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(true); p.setBackground(Color.WHITE);
        JPanel filt = new JPanel(new BorderLayout()); filt.setBackground(AeroPalette.c(0xeaf1f8)); filt.setOpaque(true); filt.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        JPanel fl = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0)); fl.setOpaque(false); fl.add(lbl("Filter By:")); JLabel all = new JLabel(" ALL "); all.setOpaque(true); all.setBackground(new Color(0x2563eb)); all.setForeground(Color.WHITE); all.setFont(AeroFonts.bold(10f)); fl.add(all);
        fl.add(AeroButtons.hyperlink("In Stock", () -> { })); fl.add(AeroButtons.hyperlink("Low Inventory", () -> { })); filt.add(fl, BorderLayout.WEST); JLabel cnt = AeroStatusBar.text("5 of 142 items visible"); filt.add(cnt, BorderLayout.EAST);
        JPanel sum = new JPanel(new BorderLayout()); sum.putClientProperty("JPanel.style", "toolbar"); sum.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        JPanel sl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); sl.setOpaque(false); sl.add(AeroStatusBar.text("Items: 5")); JLabel sv = new JLabel(" 145.00 "); sv.setOpaque(true); sv.setBackground(new Color(0xa7f3d0)); sv.setForeground(new Color(0x064e3b)); sv.setFont(AeroFonts.monoBold(10f)); sl.add(AeroStatusBar.text("Savings:")); sl.add(sv); sl.add(AeroStatusBar.text("Tax: 13.50"));
        sum.add(sl, BorderLayout.WEST); JLabel gross = new JLabel("Gross: 1,690.00"); gross.setFont(AeroFonts.bold(11f)); sum.add(gross, BorderLayout.EAST);
        JPanel act = new JPanel(new BorderLayout()); act.setOpaque(true); act.setBackground(AeroPalette.PANEL_BG); act.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        JPanel al = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)); al.setOpaque(false); JButton x1 = AeroButtons.create("Export Excel", "standard"), x2 = AeroButtons.create("CSV", "standard"); al.add(x1); al.add(x2);
        JButton pr = AeroButtons.create("Print Invoice (F12)", AeroIcons.lucide("printer", 12, Color.WHITE), "accent"); act.add(al, BorderLayout.WEST); act.add(pr, BorderLayout.EAST);
        JPanel south = new JPanel(new BorderLayout()); south.add(sum, BorderLayout.NORTH); south.add(act, BorderLayout.SOUTH);
        p.add(filt, BorderLayout.NORTH); p.add(sc, BorderLayout.CENTER); p.add(south, BorderLayout.SOUTH); return p;
    }

    static JComponent tintPanel() {
        JPanel p = new JPanel(new GridBagLayout()); p.setOpaque(true); p.setBackground(new Color(0xfbfcfd)); p.setBorder(BorderFactory.createEmptyBorder(8, 8, 6, 8));
        GridBagConstraints c = new GridBagConstraints(); c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.gridwidth = 3;
        JPanel sw = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0)); sw.setOpaque(false); sw.add(lbl("Swatches:  ")); for (int k : new int[]{0x38bdf8, 0x2dd4bf, 0x6366f1, 0xfbbf24, 0xfb7185, 0x10b981, 0x64748b, 0xf1f5f9}) { final int kk = k; JButton b = new JButton() { protected void paintComponent(Graphics g0) { Graphics2D g = AeroPaint.g2(g0); g.setColor(new Color(kk)); g.fill(AeroPaint.rr(0, 0, getWidth(), getHeight(), 2)); AeroPaint.border(g, 0, 0, getWidth(), getHeight(), 2, new Color(0x94a3b8)); g.dispose(); } }; b.setPreferredSize(new Dimension(14, 14)); b.putClientProperty("JButton.noMinHeight", "true"); b.setFocusable(false); sw.add(b); }
        p.add(sw, c);
        String[] n = {"Red (R)", "Green (G)", "Blue (B)"}; int[] v = {112, 168, 210}; Color[] tc = {new Color(0xdc2626), new Color(0x16a34a), new Color(0x2563eb)};
        for (int i = 0; i < 3; i++) { JPanel cp = new JPanel(new BorderLayout()); cp.setOpaque(false); JLabel l = lbl(n[i]); JLabel val = new JLabel("" + v[i]); val.setFont(AeroFonts.monoBold(10f)); val.setForeground(tc[i]); cp.add(l, BorderLayout.WEST); cp.add(val, BorderLayout.EAST);
            JSlider s = new JSlider(0, 255, v[i]); s.putClientProperty("JSlider.trackColor", tc[i].brighter()); JPanel cc = new JPanel(new BorderLayout()); cc.setOpaque(false); cc.add(cp, BorderLayout.NORTH); cc.add(s, BorderLayout.CENTER);
            GridBagConstraints g = new GridBagConstraints(); g.gridx = i; g.gridy = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(10, 4, 0, 4); p.add(cc, g); }
        JPanel bot = new JPanel(new BorderLayout()); bot.setOpaque(false); JPanel hl = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0)); hl.setOpaque(false); hl.add(lbl("Hex:")); JTextField hx = new JTextField("#70A8D2", 7); hl.add(hx);
        JPanel hr = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0)); hr.setOpaque(false); hr.add(AeroStatusBar.text("Opacity:")); JSlider op = new JSlider(0, 100, 85); op.setPreferredSize(new Dimension(70, 18)); hr.add(op); hr.add(AeroStatusBar.text("85%")); hr.add(AeroButtons.create("Cancel", "standard")); hr.add(AeroButtons.create("Apply Theme", "primary"));
        bot.add(hl, BorderLayout.WEST); bot.add(hr, BorderLayout.EAST); GridBagConstraints g = new GridBagConstraints(); g.gridx = 0; g.gridy = 2; g.gridwidth = 3; g.weightx = 1; g.weighty = 1; g.fill = GridBagConstraints.HORIZONTAL; g.anchor = GridBagConstraints.SOUTH; g.insets = new Insets(8, 0, 0, 0); p.add(bot, g);
        return p;
    }

    public static void main(String[] a) throws Exception {
        AeroLookAndFeel.install();
        SwingUtilities.invokeAndWait(() -> { JFrame f = new JFrame(TITLE); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); populate(f.getRootPane()); f.setSize(1280, 840); f.setLocationRelativeTo(null); f.setVisible(true); });
    }
}
