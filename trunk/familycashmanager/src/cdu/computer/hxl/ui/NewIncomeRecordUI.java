package cdu.computer.hxl.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;

import cdu.computer.hxl.db.DBCRUDHandler;
import cdu.computer.hxl.db.impl.DefaultDBCRUDHandler;
import cdu.computer.hxl.factory.ObjectFactory;
import cdu.computer.hxl.service.BankService;
import cdu.computer.hxl.service.IncomeService;
import cdu.computer.hxl.util.ThreadExecutorUtils;

public class NewIncomeRecordUI extends BaseJDialog {

	private static final long serialVersionUID = 6375647562062306231L;
	private static final IncomeService incomeService = (IncomeService) ObjectFactory
			.getInstance("incomeService");
	private JPanel contentPanel = null;
	private JTextField amountTextField = null;
	private JTextField timeTextField = null;
	private BaseJComboBox saveComboBox = null;
	private BaseJComboBox sourceComboBox = null;
	private JTextArea remarkTextArea = null;
	private JButton submitbnt = null;
	private JButton clearbtn = null;

	public NewIncomeRecordUI(BaseJFrame owner) {
		super(owner, "���������¼", true);
		initUI();
	}

	@Override
	protected void initUI() {

		setSize(400, 350);
		contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		getContentPane().setLayout(null);

		JLabel titleLabel = new JLabel("\u6DFB\u52A0\u6536\u5165\u8BB0\u5F55");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(145, 10, 83, 15);
		getContentPane().add(titleLabel);

		JLabel moneyLabel = new JLabel("\u91D1\u989D\uFF1A");
		moneyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		moneyLabel.setBounds(51, 47, 54, 15);
		getContentPane().add(moneyLabel);

		amountTextField = new JTextField();
		amountTextField.setHorizontalAlignment(SwingConstants.LEFT);
		amountTextField.setBounds(145, 44, 83, 21);
		getContentPane().add(amountTextField);
		// amountTextField.setColumns(10);

		JLabel sourceLabel = new JLabel("\u6765\u6E90\uFF1A");
		sourceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		sourceLabel.setBounds(51, 83, 54, 15);
		getContentPane().add(sourceLabel);

		sourceComboBox = new BaseJComboBox();
		// sourceComboBox.setModel(new DefaultComboBoxModel(new String[] {
		// "\u5DE5\u8D44", "\u5916\u5305", "\u5176\u5B83" }));
		final DefaultComboBoxModel sourcemodel = new DefaultComboBoxModel();
		sourceComboBox.setModel(sourcemodel);
		sourceComboBox.setBounds(145, 80, 83, 21);
		getContentPane().add(sourceComboBox);

		JLabel saveLabel = new JLabel("\u5B58\u5165\uFF1A");
		saveLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		saveLabel.setBounds(51, 167, 54, 15);
		getContentPane().add(saveLabel);

		saveComboBox = new BaseJComboBox();
		// saveComboBox.setModel(new DefaultComboBoxModel(
		// new String[] { "\u5DE5\u5546\u94F6\u884C" }));
		saveComboBox.setBounds(145, 164, 83, 21);
		final DefaultComboBoxModel savemodel = new DefaultComboBoxModel();
		saveComboBox.setModel(savemodel);
		getContentPane().add(saveComboBox);

		JLabel timeLabel = new JLabel("\u65F6\u95F4\uFF1A");
		timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		timeLabel.setBounds(51, 120, 54, 15);
		getContentPane().add(timeLabel);

		timeTextField = new JTextField();
		timeTextField.setBounds(145, 117, 140, 21);
		getContentPane().add(timeTextField);
		timeTextField.setColumns(10);

		JLabel remarkLabel = new JLabel("\u5907\u6CE8\uFF1A");
		remarkLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		remarkLabel.setBounds(51, 223, 54, 15);
		getContentPane().add(remarkLabel);
		JScrollPane scrollPanel = new JScrollPane();
		scrollPanel.setSize(140, 52);
		scrollPanel.setLocation(145, 204);

		remarkTextArea = new JTextArea();

		remarkTextArea.setLineWrap(true);
		remarkTextArea.setRows(2);
		remarkTextArea.setColumns(10);
		// remarkTextArea.setBounds(145, 204, 138, 50);
		scrollPanel.setViewportView(remarkTextArea);

		getContentPane().add(scrollPanel);

		submitbnt = new JButton("����");
		submitbnt.setBounds(145, 266, 66, 23);

		submitbnt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				final double money = Double.parseDouble(amountTextField
						.getText());
				final String time = timeTextField.getText();

				@SuppressWarnings("unchecked")
				final Integer saveid = (Integer) ((Map<String, Object>) saveComboBox
						.getSelectedItem()).get("rowid");

				@SuppressWarnings("unchecked")
				final Integer sourceid = (Integer) ((Map<String, Object>) sourceComboBox
						.getSelectedItem()).get("rowid");

				final String remark = remarkTextArea.getText();
				new ThreadExecutorUtils() {

					@Override
					protected void task() {
						getOwner().setStatusText("���ڱ��������¼...");

						Map<String, Object> data = new HashMap<String, Object>();
						data.put("amount", money);
						data.put("remark", remark);
						data.put("bankid", saveid);
						data.put("sourceid", sourceid);
						data.put("date", time);

						incomeService.addIncomeItem(data);

						getOwner().setStatusText("����ɹ�");
					}
				}.exec();
				setVisible(false);
			}
		});

		getContentPane().add(submitbnt);

		clearbtn = new JButton("\u6E05\u7A7A");
		clearbtn.setBounds(219, 266, 66, 23);
		getContentPane().add(clearbtn);

		new ThreadExecutorUtils() {

			@Override
			protected void task() {
				BankService bService = (BankService) ObjectFactory
						.getInstance("bankService");
				List<Map<String, Object>> rankdata = bService.loadAllBank();
				int size = rankdata.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> mm = rankdata.get(i);
					mm.put("name", mm.get("bankname"));
					mm.remove("bankname");
					savemodel.addElement(mm);
				}
			}
		}.exec();

		new ThreadExecutorUtils() {

			@Override
			protected void task() {
				List<Map<String, Object>> incomeList = incomeService
						.loadIncomeCategoryForList(null);
				int size = incomeList.size();
				for (int i = 0; i < size; i++) {
					Map<String, Object> mm = incomeList.get(i);
					mm.put("name", mm.get("categoryname"));
					mm.remove("categoryname");
					sourcemodel.addElement(mm);
				}
			}
		}.exec();

		super.initUI();
	}
}