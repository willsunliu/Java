package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import model.DataModel;
import model.ViewModel;

public class MainFrame {

	// 年
	int year;
	// 月
	int month;
	// 日
	int day;

	// 数据库操作的封装类
	private DataModel dataModel;

	// 主窗口
	JFrame jf = new JFrame();

	// 菜单模块
	JPanel menuPanel = new JPanel();

	// 主模块
	BorderLayoutPanel mainPanel = new BorderLayoutPanel(5, 5);

	JTextArea ta = new JTextArea(10, 40);

	// 菜单的List
	JList<String> menuItemList = new JList<String>(ViewModel.menuItemNames);

	public MainFrame(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * 初始化
	 */
	public void init() {

		// jf.setLayout(new BorderLayout(10, 10));

		year = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR);
		month = Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH) + 1;
		day = Calendar.getInstance(Locale.CHINA).get(Calendar.DATE);

		createMenuView();
		generateMainPanel(ViewModel.NEW_NOTE);

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setMinimumSize(new Dimension(800, 550));
		jf.pack();
	}

	/**
	 * 生成主模块
	 * 
	 * @param panelType
	 *            从菜单获取的主菜单类型
	 */
	private void generateMainPanel(int panelType) {

		// 将当前显示的内容清理掉
		if (mainPanel != null) {
			mainPanel.removeAll();
			jf.repaint();
		}

		switch (panelType) {
		case ViewModel.NEW_NOTE: // 打印新单
			createNewNoteView();
			break;
		case ViewModel.QUERRY_NOTE: // 查询旧单
			createQuerryNoteView();
			break;
		case ViewModel.PRINT_MONTHLY_NOTE: // 打印月结单
			createPrintMonthlyNoteView();
			break;
		case ViewModel.ADD_CLIENT_INFO: // 添加客户资料
			createAddClientInfoView();
			break;
		case ViewModel.ADD_GOOD_INFO: // 添加货品
			createAddGoodInfoView();
			break;

		default:
			break;
		}

		// 显示更新过后的内容
		jf.add(mainPanel);
		jf.setVisible(true);
	}

	JTable clientTable;
	MyDefaultClientTableModel clientModel;
	String selectedClientTableValue;
	JTextField clientField;
	JTextField phoneField;
	JTextField addrField;

	private void createAddClientInfoView() {

		clientModel = new MyDefaultClientTableModel();

		// 布局添加模块
		JLabel clientLabel = new JLabel("客户");
		clientField = new JTextField(4);
		JLabel phoneLabel = new JLabel("电话");
		phoneField = new JTextField(2);
		JLabel addrLabel = new JLabel("地址");
		addrField = new JTextField(2);
		Box addFunctionBox = Box.createVerticalBox();
		JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 判断客户名称，电话或者地址是否为空
					if (!(clientField.getText().isEmpty()
							|| phoneField.getText().isEmpty() || addrField
							.getText().isEmpty())) {

						// 判断输入的客户是否已存在
						if (!dataModel.compareValue(
								DataModel.CLIENT_TABLE_NAME,
								DataModel.CLIENT_NAME_LABEL,
								clientField.getText())) {
							if (isPhoneNumber(phoneField.getText())) {
								dataModel.insertValue(
										DataModel.CLIENT_TABLE_NAME,
										clientField.getText(),
										addrField.getText(),
										phoneField.getText());
								dataModel
										.querryAll(DataModel.CLIENT_TABLE_NAME);
								((MyDefaultClientTableModel) clientTable
										.getModel()).fireTableDataChanged();
							} else {
								showMessageDialog(
										ViewModel.PHONE_FORMAT_INCORRECT_TITLE,
										ViewModel.PHONE_FORMAT_INCORRECT_MESSAGE,
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							showMessageDialog(
									ViewModel.CLIENT_EXIST_WARNING_TITLE,
									ViewModel.CLIENT_EXIST_WARNING_MESSAGE,
									JOptionPane.WARNING_MESSAGE);

						}
					} else {
						showMessageDialog(
								ViewModel.CLIENT_NAME_OR_PHONE_OR_ADDR_IS_EMPTY_TITLE,
								ViewModel.CLIENT_NAME_OR_PHONE_OR_ADDR_IS_EMPTY_MESSAGE,
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		Box clientBox = Box.createHorizontalBox();
		clientBox.add(clientLabel);
		clientBox.add(Box.createRigidArea(new Dimension(10, 5)));
		clientBox.add(clientField);
		addFunctionBox.add(clientBox);
		addFunctionBox.add(Box.createRigidArea(new Dimension(0, 5)));
		Box phoneBox = Box.createHorizontalBox();
		phoneBox.add(phoneLabel);
		phoneBox.add(Box.createRigidArea(new Dimension(10, 5)));
		phoneBox.add(phoneField);
		addFunctionBox.add(phoneBox);
		addFunctionBox.add(Box.createRigidArea(new Dimension(0, 5)));
		Box addrBox = Box.createHorizontalBox();
		addrBox.add(addrLabel);
		addrBox.add(Box.createRigidArea(new Dimension(10, 5)));
		addrBox.add(addrField);
		addFunctionBox.add(addrBox);
		addFunctionBox.add(Box.createRigidArea(new Dimension(0, 5)));
		Box addButtonBox = Box.createHorizontalBox();
		addButtonBox.add(addButton);
		addFunctionBox.add(addButtonBox);
		addFunctionBox.add(Box.createRigidArea(new Dimension(0, 5)));

		// 布局删除模块
		Box buttonBox = Box.createHorizontalBox();
		JButton deleteButton = new JButton("删除");
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRow = clientTable.getSelectedRow();
				System.out.println(selectedRow);
				// 判断有没有选中数据
				if (selectedRow == -1) {
					// 没有选中数据的话提醒选中数据再点击删除
					showMessageDialog(ViewModel.NO_SELECTED_ROW_TITLE,
							ViewModel.NO_SELECTED_ROW_MESSAGE,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 提示用户是否确定要删除数据
					String client = (String) clientTable.getValueAt(
							clientTable.getSelectedRow(), 0);
					String addr = (String) clientTable.getValueAt(
							clientTable.getSelectedRow(), 1);
					String phone = (String) clientTable.getValueAt(
							clientTable.getSelectedRow(), 2);
					int dialogResult = JOptionPane.showConfirmDialog(jf,
							ViewModel.DELETE_ROW_MESSAGE + "\n[客户: " + client
									+ "  地址: " + addr + "  电话: " + phone
									+ "]\n", ViewModel.DELETE_ROW_MESSAGE,
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					// 点解确定
					if (dialogResult == JOptionPane.YES_OPTION) {
						// 删除数据
						try {
							dataModel.deleteValue(DataModel.CLIENT_TABLE_NAME,
									DataModel.CLIENT_NAME_LABEL, client,
									DataModel.CLIENT_ADDR_LABEL, addr,
									DataModel.CLIENT_PHONE_LABEL, phone);
							dataModel.querryAll(DataModel.CLIENT_TABLE_NAME);
							((MyDefaultClientTableModel) clientTable.getModel())
									.fireTableDataChanged();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		// buttonBox.add(Box.createHorizontalGlue());
		buttonBox.setBorder(new EmptyBorder(5, 0, 5, 0));
		buttonBox.add(deleteButton);

		Box topBox = Box.createVerticalBox();
		topBox.add(addFunctionBox);
		topBox.add(buttonBox);
		mainPanel.add(topBox, BorderLayout.NORTH);

		// 添加客户列表
		clientTable = new JTable(clientModel);
		clientTable.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				System.out.println("table changed");
				// System.out.println("last row: " + e.getLastRow());
				// System.out.println("first row: " + e.getFirstRow());
				// System.out.println("column: " + e.getColumn());
				System.out.println();
			}
		});
		clientTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				selectedClientTableValue = (String) clientTable.getValueAt(
						clientTable.getSelectedRow(),
						clientTable.getSelectedColumn());
				System.out.println(selectedClientTableValue);
			}
		});

		JScrollPane scrollPane = null;
		scrollPane = new JScrollPane(clientTable);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

	}

	class MyDefaultClientTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyDefaultClientTableModel() {
			super();
		}

		@Override
		public int getColumnCount() {
			int count = 0;
			try {
				count = dataModel.countTableColumn(DataModel.CLIENT_TABLE_NAME);
			} catch (SQLException e) {
				count = 0;
				e.printStackTrace();
			}
			System.out.println(count);
			return count;
		}

		@Override
		public int getRowCount() {
			int count = 0;
			try {
				count = dataModel.countTableRow(DataModel.CLIENT_TABLE_NAME);
			} catch (SQLException e) {
				count = 0;
				e.printStackTrace();
			}
			System.out.println(count);
			return count;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String value = null;
			try {
				value = dataModel.getValueAt(DataModel.CLIENT_TABLE_NAME,
						rowIndex + 1, columnIndex + 1);
			} catch (SQLException e) {
				value = "";
				e.printStackTrace();
			}
			return value;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			if (!selectedClientTableValue.equals((String) value)) {
				if (2 == column) {
					if (isPhoneNumber((String) value)) {
						int result = JOptionPane.showConfirmDialog(jf,
								"确定要修改    " + selectedClientTableValue
										+ "  为    " + value + "  吗？",
								"确定需要修改？", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (JOptionPane.YES_OPTION == result) {
							String name = (String) clientTable.getValueAt(row,
									0);
							try {
								dataModel.setGoodValueAt(
										DataModel.CLIENT_TABLE_NAME,
										DataModel.CLIENT_PHONE_LABEL,
										(String) value,
										DataModel.CLIENT_NAME_LABEL, name);
								// dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						showMessageDialog(
								ViewModel.PHONE_FORMAT_INCORRECT_TITLE,
								ViewModel.PHONE_FORMAT_INCORRECT_MESSAGE,
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (1 == column) {
					int result = JOptionPane.showConfirmDialog(jf, "确定要修改    "
							+ selectedClientTableValue + "  为    " + value
							+ "  吗？", "确定需要修改？", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (JOptionPane.YES_OPTION == result) {
						String name = (String) clientTable.getValueAt(row, 0);
						try {
							dataModel.setGoodValueAt(
									DataModel.CLIENT_TABLE_NAME,
									DataModel.CLIENT_ADDR_LABEL,
									(String) value,
									DataModel.CLIENT_NAME_LABEL, name);
							// dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (0 == column) {
					try {
						if (!dataModel.compareValue(
								DataModel.CLIENT_TABLE_NAME,
								DataModel.CLIENT_NAME_LABEL, (String) value)) {
							String client = (String) clientTable.getValueAt(
									row, column);
							int result = JOptionPane.showConfirmDialog(jf,
									"确定要修改    " + selectedClientTableValue
											+ "  为    " + value + "  吗？",
									"确定需要修改？", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							if (JOptionPane.YES_OPTION == result) {
								try {
									dataModel
											.setGoodValueAt(
													DataModel.CLIENT_TABLE_NAME,
													DataModel.CLIENT_NAME_LABEL,
													(String) value,
													DataModel.CLIENT_NAME_LABEL,
													client);
									// dataModel
									// .querryAll(DataModel.GOOD_TABLE_NAME);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			// super.setValueAt(value, row, column);
		}

		@Override
		public String getColumnName(int columnIndex) {
			return ViewModel.clientFormColumnTitle[columnIndex];
		}
	}

	private void createPrintMonthlyNoteView() {
		// TODO Auto-generated method stub

	}

	private JTable oldFormRecordTable; 
	
	private void createQuerryNoteView() {
		Box topBox = Box.createVerticalBox();
		oldFormRecordTable = new JTable(new OldFormRecordTableModel());
		oldFormRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		oldFormRecordTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent event) {
				int count = event.getClickCount();
				if (2 == count) {
					try {
						initSelectedForm();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		JScrollPane recordScrollPane = new JScrollPane(newFormRecordTable);
		recordScrollPane.setPreferredSize(new Dimension(400, 207));
		topBox.add(recordScrollPane);
		
		mainPanel.add(topBox, BorderLayout.NORTH);
	}
	
	private class OldFormRecordTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}

	/**
	 * 创建菜单模块
	 */
	private void createMenuView() {

		// 给菜单添加鼠标时间
		menuItemList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				switch (menuItemList.getSelectedIndex()) {
				case 0:
					generateMainPanel(ViewModel.NEW_NOTE);
					break;
				case 1:
					generateMainPanel(ViewModel.QUERRY_NOTE);
					break;
				case 2:
					generateMainPanel(ViewModel.PRINT_MONTHLY_NOTE);
					break;
				case 3:
					generateMainPanel(ViewModel.ADD_CLIENT_INFO);
					break;
				case 4:
					generateMainPanel(ViewModel.ADD_GOOD_INFO);
					break;
				case 5:
					System.exit(0);
					break;

				default:
					break;
				}
			}
		});

		// 设置List的文字居中显示
		menuItemList.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public int getHorizontalAlignment() {
				// TODO Auto-generated method stub
				return CENTER;
			}

		});
		// 菜单字体
		menuItemList.setFont(new Font(null, Font.BOLD, 18));
		// 可视的行数
		menuItemList.setVisibleRowCount(6);
		// 每行的宽度
		menuItemList.setFixedCellWidth(120);
		// 每行的高度
		menuItemList.setFixedCellHeight(80);
		// 默认选中的项
		menuItemList.setSelectedIndex(0);
		// 设置选中模式，此处只允许单选
		menuItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		menuPanel.add(menuItemList);
		menuPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		jf.add(menuPanel, BorderLayout.WEST);
	}

	private static final int CONTRACT = 0;
	private static final int GOOD = 1;
	private static final int QUANTITY = 2;

	private void initSelectedForm() throws SQLException {

		int selectedRow = newFormRecordTable.getSelectedRow() + 1;

		// 取消formTable的编辑状态
		if (formTable.isEditing()) {
			formTable.getCellEditor().stopCellEditing();
		}

		// 清除formTable中的内容
		for (int i = 0; i < formTable.getRowCount(); i++) {
			for (int j = 0; j < formTable.getColumnCount(); j++) {
				formTable.setValueAt("", i, j);
			}
		}

		// 获取表格中的数据
		String data = dataModel.getValueAt(DataModel.FORM_TABLE_NAME,
				selectedRow, 5);
		String[] datas = null;
		datas = data.split(",");
		System.out.println(datas.toString());

		// 填充表格
		int lineCount = datas.length / 3;
		for (int i = 0; i < lineCount; i++) {
			formTable.setValueAt(datas[i * 3 + CONTRACT], i, 0);
			formTable.setValueAt(datas[i * 3 + GOOD], i, 1);
			formTable.setValueAt(datas[i * 3 + QUANTITY], i, 2);
		}

		// 填充其他内容
		numField.setText(dataModel.getValueAt(DataModel.FORM_TABLE_NAME,
				selectedRow, 1));
		dateField.setText(dataModel.getValueAt(DataModel.FORM_TABLE_NAME,
				selectedRow, 3));
		clientComboBox.setSelectedItem(dataModel.getValueAt(
				DataModel.FORM_TABLE_NAME, selectedRow, 2));
	}

	/**
	 * 创建打印新单模块
	 */
	JTable formTable;
	JTable newFormRecordTable;
	JTextField numField;
	JTextField dateField;
	JComboBox<String> clientComboBox;

	private void createNewNoteView() {

		// 初始化流水号
		int lastRow = 0;
		String serialNum = "";

		try {
			lastRow = dataModel.countTableRow(DataModel.FORM_TABLE_NAME);
			if (lastRow != 0) {
				serialNum = dataModel.getValueAt(DataModel.FORM_TABLE_NAME,
						lastRow, 1);
				String prefix = String.valueOf(year);
				serialNum = serialNum.substring(4);
				int tmp = Integer.parseInt(serialNum);
				tmp += 1;
				serialNum = String.valueOf(prefix + String.format("%07d", tmp));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Box topBox = Box.createVerticalBox();
		newFormRecordTable = new JTable(new NewFormRecordTableModel());
		newFormRecordTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		newFormRecordTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent event) {
				int count = event.getClickCount();
				if (2 == count) {
					try {
						initSelectedForm();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		JScrollPane recordScrollPane = new JScrollPane(newFormRecordTable);
		recordScrollPane.setPreferredSize(new Dimension(400, 207));
		topBox.add(recordScrollPane);

		Box clientBox = Box.createHorizontalBox();
		clientBox.setBorder(new EmptyBorder(10, 0, 0, 0));
		JLabel clientLabel = new JLabel(ViewModel.labelClient);
		String[] clientNames = null;
		try {
			clientNames = dataModel.getAllValueInColumn(
					DataModel.CLIENT_TABLE_NAME, DataModel.CLIENT_NAME_LABEL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		clientComboBox = new JComboBox<String>(clientNames);
		clientBox.add(clientLabel);
		clientBox.add(Box.createRigidArea(new Dimension(10, 0)));
		clientBox.add(clientComboBox);
		topBox.add(clientBox);

		Box infoBox = Box.createHorizontalBox();
		infoBox.setBorder(new EmptyBorder(10, 0, 0, 0));
		JLabel numLabel = new JLabel("流水账号");
		numField = new JTextField(20);
		numField.setEditable(false);
		numField.setText(serialNum);
		JLabel dateLabel = new JLabel(ViewModel.labelDate);
		dateField = new JTextField(8);
		dateField.setText(String.format("%02d", day) + "/"
				+ String.format("%02d", month) + "/" + year);
		infoBox.add(numLabel);
		infoBox.add(Box.createRigidArea(new Dimension(10, 0)));
		infoBox.add(numField);
		infoBox.add(Box.createRigidArea(new Dimension(30, 0)));
		infoBox.add(dateLabel);
		infoBox.add(Box.createRigidArea(new Dimension(10, 0)));
		infoBox.add(dateField);
		infoBox.add(Box.createVerticalGlue());
		topBox.add(infoBox, BorderLayout.NORTH);

		Box bottomBox = Box.createVerticalBox();
		JButton printButton = new JButton(ViewModel.labelPrint);
		printButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String formNum = numField.getText();
				String formClient = (String) clientComboBox.getSelectedItem();
				String formDate = dateField.getText();
				String formTotal = String.valueOf(formTable.getValueAt(9, 4));

				String data = "";
				String value = null;
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 3; j++) {
						value = (String) formTable.getValueAt(i, j);
						if (value == null || value.isEmpty()
								|| value.equals("") || value.equals(" ")) {
							data += " ";
						} else {
							data += value;
						}
						if (j + 1 < 3) {
							data += ",";
						}
					}
					if (i + 1 < 9) {
						data += ",";
					}
				}
				System.out.println(data);
				try {
					if (dataModel.compareValue(DataModel.FORM_TABLE_NAME,
							DataModel.FORM_NUM_LABEL, formNum)) {
						int result = JOptionPane.showConfirmDialog(jf,
								ViewModel.SERIAL_NUM_EXIST_MESSAGE,
								ViewModel.SERIAL_NUM_EXIST_TITLE,
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (JOptionPane.YES_OPTION == result) {
							dataModel.deleteValue(DataModel.FORM_TABLE_NAME,
									DataModel.FORM_NUM_LABEL, formNum);
							dataModel.insertValue(DataModel.FORM_TABLE_NAME,
									formNum, formClient, formDate, formTotal,
									data);
						}
					} else {
						dataModel.insertValue(DataModel.FORM_TABLE_NAME,
								formNum, formClient, formDate, formTotal, data);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				((NewFormRecordTableModel) newFormRecordTable.getModel())
						.fireTableDataChanged();
			}
		});
		bottomBox.add(printButton);

		formTable = new JTable(new MyFormTableModel());
		for (int i = 0; i < formTable.getRowCount(); i++) {
			for (int j = 0; j < formTable.getColumnCount(); j++) {
				formTable.setValueAt("", i, j);
			}
		}
		formTable.setRowSelectionAllowed(false);
		String[] goodNames = null;
		try {
			goodNames = dataModel.getAllValueInColumn(
					DataModel.GOOD_TABLE_NAME, DataModel.GOOD_NAME_LABEL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JComboBox<String> goodComboBox = new JComboBox<>(goodNames);
		goodComboBox.setMaximumRowCount(10);
		TableColumn goodColumn = formTable.getColumnModel().getColumn(1);
		goodColumn.setCellEditor(new DefaultCellEditor(goodComboBox));
		setFormTableBackground();

		mainPanel.add(topBox, BorderLayout.NORTH);
		mainPanel.add(bottomBox, BorderLayout.SOUTH);
		JScrollPane formScrollPane = new JScrollPane(formTable);
		formScrollPane.setPreferredSize(new Dimension(400, 100));
		mainPanel.add(formScrollPane, BorderLayout.CENTER);
	}

	/**
	 * 设置送货单表格的显示格式
	 */
	private void setFormTableBackground() {
		TableColumnModel tcm = formTable.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			TableColumn tc = tcm.getColumn(i);
			tc.setCellRenderer(new DefaultTableCellRenderer() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {

					if (3 != column && 4 != column) {
						if (9 == row) {
							setBackground(Color.LIGHT_GRAY);
						} else {
							setBackground(Color.WHITE);
						}
					} else {
						setBackground(Color.LIGHT_GRAY);
						setAlignmentX(Component.CENTER_ALIGNMENT);
					}
					setHorizontalAlignment(SwingConstants.CENTER);
					return super.getTableCellRendererComponent(table, value,
							isSelected, hasFocus, row, column);
				}

			});
		}
	}

	/**
	 * 纪录表格的model
	 * 
	 * @author IT01
	 * 
	 */
	private class NewFormRecordTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			int count = 0;
			count = ViewModel.recordFormColumnTitle.length;
			return count;
		}

		@Override
		public int getRowCount() {
			int count = 0;
			try {
				count = dataModel.countTableRow(DataModel.FORM_TABLE_NAME);
			} catch (SQLException e) {
				count = 0;
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public String getColumnName(int column) {
			// TODO Auto-generated method stub
			return ViewModel.recordFormColumnTitle[column];
		}

		@Override
		public Object getValueAt(int row, int column) {
			try {
				return dataModel.getValueAt(DataModel.FORM_TABLE_NAME, row + 1,
						column + 1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return super.getValueAt(row, column);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public void fireTableDataChanged() {
			newFormRecordTable.getSelectionModel().setSelectionInterval(
					newFormRecordTable.getRowCount() - 1,
					newFormRecordTable.getRowCount() - 1);
			super.fireTableDataChanged();
		}

	}

	/**
	 * 送货单表格的model
	 * 
	 * @author IT01
	 * 
	 */
	private class MyFormTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public String getColumnName(int columnIndex) {
			return ViewModel.noteColumnTitle[columnIndex];
		}

		@Override
		public Object getValueAt(int row, int column) {

			// 根据第2列ComboBox的内容，确定第4列的单价
			if (1 == column && 9 != row) {
				String value = (String) super.getValueAt(row, column);
				if (null != value) {
					if (value.equals(" ") || value.equals("")) {
						super.setValueAt("", row, column + 2);
					} else {
						try {
							String price = dataModel.getValueAt(
									DataModel.GOOD_TABLE_NAME,
									DataModel.GOOD_PRICE_LABEL,
									DataModel.GOOD_NAME_LABEL, value);
							super.setValueAt(
									String.format("%.2f",
											Float.parseFloat(price)), row,
									column + 2);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					super.setValueAt("", row, column + 2);
				}
			}

			if (2 == column && 9 != row) {
				String value = (String) super.getValueAt(row, column);
				if (null != value) {
					if (value.equals(" ") || value.equals("")) {
						super.setValueAt("", row, column);
					}
				} else {
					super.setValueAt("", row, column);
				}
			}

			// 根据单价和数量计算单个货物的总价
			if ((4 == column && 9 != row)
					&& (null != super.getValueAt(row, 2)
							&& !((String) super.getValueAt(row, 2)).equals(" ") && !((String) super
								.getValueAt(row, 2)).equals(""))
					&& (null != super.getValueAt(row, 3)
							&& !((String) super.getValueAt(row, 3)).equals(" ") && !((String) super
								.getValueAt(row, 3)).equals(""))) {
				float price = Float.parseFloat((String) super
						.getValueAt(row, 3));
				float quantity = Float.parseFloat((String) super.getValueAt(
						row, 2));
				String sum = String.format("%.2f", price * quantity);
				super.setValueAt(sum, row, 4);
			}

			// 将第10行第4列的内容设置为“合计”
			if (9 == row && 3 == column) {
				super.setValueAt(ViewModel.labelTotal, row, column);
			}

			// 将所有货物加起来的总价填写在这里
			if (9 == row && 4 == column) {
				float total = 0.f;
				float item = 0.f;
				for (int i = 0; i < 9; i++) {
					if (super.getValueAt(i, column) != null
							&& (!((String) super.getValueAt(i, column))
									.isEmpty() && !((String) super.getValueAt(
									i, column)).equals(" "))) {
						item = Float.parseFloat((String) super.getValueAt(i,
								column));
					} else {
						item = 0.00f;
					}
					total += item;
				}
				super.setValueAt(String.format("%.2f", total), row, column);
			}

			return super.getValueAt(row, column);
		}

		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (2 == column
					&& (!((String) aValue).equals("") && !((String) aValue)
							.equals(" "))) {
				if (isFloat((String) aValue)) {
					super.setValueAt(
							String.format("%.2f",
									Float.parseFloat((String) aValue)), row,
							column);
				} else {
					showMessageDialog(ViewModel.QUANTITY_IS_NOT_A_NUMBER_TITLE,
							ViewModel.QUANTITY_IS_NOT_A_NUMBER_MESSAGE,
							JOptionPane.ERROR_MESSAGE);
				}
			} else if (aValue != null && aValue.equals("<空>")) {
				super.setValueAt(" ", row, column);
				super.setValueAt(" ", row, column + 1);
			} else {
				super.setValueAt(aValue, row, column);
			}
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			// 第4列和第5列所有行，以及第10行所有列不能编辑
			if (3 == column || 4 == column) {
				return false;
			} else {
				if (9 == row) {
					return false;
				}
			}

			return super.isCellEditable(row, column);
		}

		@Override
		public void fireTableDataChanged() {
			// TODO Auto-generated method stub
			System.out.println("tttttttttttt");
			super.fireTableDataChanged();
		}

	}

	/**
	 * 创建添加货品的试图模块
	 */
	JTextField goodField;
	JTextField priceField;
	JTable goodTable;
	MyDefaultGoodTableModel goodModel;
	String selectedGoodTableValue;

	private void createAddGoodInfoView() {

		goodModel = new MyDefaultGoodTableModel();

		// 布局添加模块
		JLabel goodLabel = new JLabel("货物");
		goodField = new JTextField(4);
		JLabel priceLabel = new JLabel("单价");
		priceField = new JTextField(2);
		Box addFunctionBox = Box.createHorizontalBox();
		JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// 判断货物名称或者价格是否为空
					if (!(goodField.getText().isEmpty() || priceField.getText()
							.isEmpty())) {

						if (isFloat(priceField.getText())) {
							// 判断输入的货物是否已存在
							if (!dataModel.compareValue(
									DataModel.GOOD_TABLE_NAME,
									DataModel.GOOD_NAME_LABEL,
									goodField.getText())) {
								dataModel.insertValue(
										DataModel.GOOD_TABLE_NAME,
										goodField.getText(),
										priceField.getText());
								dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
								((MyDefaultGoodTableModel) goodTable.getModel())
										.fireTableDataChanged();
							} else {
								showMessageDialog(
										ViewModel.GOOD_EXIST_WARNING_TITLE,
										ViewModel.GOOD_EXIST_WARNING_MESSAGE,
										JOptionPane.WARNING_MESSAGE);
							}
						} else {
							showMessageDialog(
									ViewModel.PRICE_IS_NOT_A_NUMBER_TITLE,
									ViewModel.PRICE_IS_NOT_A_NUMBER_MESSAGE,
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						showMessageDialog(
								ViewModel.GOOD_NAME_OR_PRICE_IS_EMPTY_TITLE,
								ViewModel.GOOD_NAME_OR_PRICE_IS_EMPTY_MESSAGE,
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		addFunctionBox.add(Box.createHorizontalGlue());
		addFunctionBox.add(goodLabel);
		addFunctionBox.add(Box.createRigidArea(new Dimension(10, 5)));
		addFunctionBox.add(goodField);
		addFunctionBox.add(Box.createRigidArea(new Dimension(10, 10)));
		addFunctionBox.add(priceLabel);
		addFunctionBox.add(Box.createRigidArea(new Dimension(10, 5)));
		addFunctionBox.add(priceField);
		addFunctionBox.add(Box.createRigidArea(new Dimension(10, 10)));
		addFunctionBox.add(addButton);

		// 布局删除模块
		Box buttonBox = Box.createHorizontalBox();
		JButton deleteButton = new JButton("删除");
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int selectedRow = goodTable.getSelectedRow();
				System.out.println(selectedRow);
				// 判断有没有选中数据
				if (selectedRow == -1) {
					// 没有选中数据的话提醒选中数据再点击删除
					showMessageDialog(ViewModel.NO_SELECTED_ROW_TITLE,
							ViewModel.NO_SELECTED_ROW_MESSAGE,
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 提示用户是否确定要删除数据
					String name = (String) goodTable.getValueAt(
							goodTable.getSelectedRow(), 0);
					String price = (String) goodTable.getValueAt(
							goodTable.getSelectedRow(), 1);
					int dialogResult = JOptionPane.showConfirmDialog(jf,
							ViewModel.DELETE_ROW_MESSAGE + "\n[货物: " + name
									+ "  单价: " + price + "]\n",
							ViewModel.DELETE_ROW_MESSAGE,
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					// 点解确定
					if (dialogResult == JOptionPane.YES_OPTION) {
						// 删除数据
						try {
							dataModel.deleteValue(DataModel.GOOD_TABLE_NAME,
									DataModel.GOOD_NAME_LABEL, name, "format("
											+ DataModel.GOOD_PRICE_LABEL
											+ ",2)", "format(" + price + ",2)");
							dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
							((MyDefaultGoodTableModel) goodTable.getModel())
									.fireTableDataChanged();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		// buttonBox.add(Box.createHorizontalGlue());
		buttonBox.setBorder(new EmptyBorder(5, 0, 5, 0));
		buttonBox.add(deleteButton);

		Box topBox = Box.createVerticalBox();
		topBox.add(addFunctionBox);
		topBox.add(buttonBox);
		mainPanel.add(topBox, BorderLayout.NORTH);

		// 添加货物列表
		goodTable = new JTable(goodModel);
		goodTable.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				System.out.println("table changed");
				// System.out.println("last row: " + e.getLastRow());
				// System.out.println("first row: " + e.getFirstRow());
				// System.out.println("column: " + e.getColumn());
				System.out.println();
			}
		});
		goodTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				selectedGoodTableValue = (String) goodTable.getValueAt(
						goodTable.getSelectedRow(),
						goodTable.getSelectedColumn());
				System.out.println(selectedGoodTableValue);
			}
		});
		JScrollPane scrollPane = new JScrollPane(goodTable);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * 
	 * 货品的TableModel，从数据库的sh_data的good_table获取数据
	 * 
	 * @author IT01
	 * 
	 */
	private class MyDefaultGoodTableModel extends DefaultTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyDefaultGoodTableModel() {
			super();
		}

		@Override
		public int getColumnCount() {
			int count = 0;
			try {
				count = dataModel.countTableColumn(DataModel.GOOD_TABLE_NAME);
			} catch (SQLException e) {
				count = 0;
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public int getRowCount() {
			int count = 0;
			try {
				count = dataModel.countTableRow(DataModel.GOOD_TABLE_NAME);
			} catch (SQLException e) {
				count = 0;
				e.printStackTrace();
			}
			return count;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			String value = null;
			try {
				// dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
				value = dataModel.getValueAt(DataModel.GOOD_TABLE_NAME,
						rowIndex + 1, columnIndex + 1);
			} catch (SQLException e) {
				value = "";
				e.printStackTrace();
			}
			return value;
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			if (!selectedGoodTableValue.equals(value)) {
				if (1 == column) {
					if (isFloat((String) value)) {
						int result = JOptionPane.showConfirmDialog(jf,
								"确定要修改    " + selectedGoodTableValue
										+ "  为    " + value + "  吗？",
								"确定需要修改？", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (JOptionPane.YES_OPTION == result) {
							String name = (String) goodTable.getValueAt(row,
									column - 1);
							try {
								dataModel.setGoodValueAt(
										DataModel.GOOD_TABLE_NAME,
										DataModel.GOOD_PRICE_LABEL,
										(String) value,
										DataModel.GOOD_NAME_LABEL, name);
								// dataModel.querryAll(DataModel.GOOD_TABLE_NAME);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						showMessageDialog(
								ViewModel.PRICE_IS_NOT_A_NUMBER_TITLE,
								ViewModel.PRICE_IS_NOT_A_NUMBER_MESSAGE,
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					try {
						if (!dataModel.compareValue(DataModel.GOOD_TABLE_NAME,
								DataModel.GOOD_NAME_LABEL, (String) value)) {
							String name = (String) goodTable.getValueAt(row,
									column);
							int result = JOptionPane.showConfirmDialog(jf,
									"确定要修改    " + selectedGoodTableValue
											+ "  为    " + value + "  吗？",
									"确定需要修改？", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							if (JOptionPane.YES_OPTION == result) {
								try {
									dataModel.setGoodValueAt(
											DataModel.GOOD_TABLE_NAME,
											DataModel.GOOD_NAME_LABEL,
											(String) value,
											DataModel.GOOD_NAME_LABEL, name);
									// dataModel
									// .querryAll(DataModel.GOOD_TABLE_NAME);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			// super.setValueAt(value, row, column);
		}

		@Override
		public String getColumnName(int columnIndex) {
			return ViewModel.goodFormColumnTitle[columnIndex];
		}

	}

	/**
	 * 显示警告框
	 * 
	 * @param title
	 * @param msg
	 * @param type
	 */
	private void showMessageDialog(String title, String msg, int type) {
		JOptionPane.showMessageDialog(jf, msg, title, type);
	}

	/**
	 * 判断字符串是否是浮点数
	 * 
	 * @param str
	 * @return
	 */
	private boolean isFloat(String str) {
		if (str.matches("^\\d+(\\.\\d{1,2})?$")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	private boolean isNumberic(String str) {
		if (str.matches("^\\d+$")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符串是否是电话号码
	 * 
	 * @param str
	 * @return
	 */
	private boolean isPhoneNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (!(Character.isDigit(ch) || '#' == ch || '*' == ch)) {
				return false;
			}
		}
		return true;
	}
}
