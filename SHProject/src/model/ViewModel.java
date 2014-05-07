package model;

public class ViewModel {

	public static final String[] menuItemNames = new String[] { "打印新单", "查询旧单",
			"打印月结单", "添加客户资料", "添加货品", "退出" };

	public static final String labelNoteNum = "单号";
	public static final String labelDate = "日期 （dd/mm/yyyy）";
	public static final String labelSum = "金额";
	public static final String labelClient = "客户";
	public static final String labelContract = "合同";
	public static final String labelGood = "货名";
	public static final String labelQuantity = "数量";
	public static final String labelUnitPrice = "单价";
	public static final String labelConsigneeSignature = "收货人签名";
	public static final String labelTotal = "合计";
	public static final String labelPrint = "打印";

	public static final String[] noteColumnTitle = new String[] { "合同", "货名",
			"数量 (0.00)", "单价 (0.00)", "金额 (0.00)" };

	public static final String[] goodFormColumnTitle = new String[] { "货物",
			"单价" };

	public static final String[] clientFormColumnTitle = new String[] { "客户",
			"地址", "电话" };

	public static final String[] recordFormColumnTitle = new String[] { "编号",
			"客户", "日期", "合计" };

	public static Object[][] tableData = {
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 },
			new Object[] { "李清照", 29, "女", 29, 29 } };

	public static final int NEW_NOTE = 0;
	public static final int QUERRY_NOTE = 1;
	public static final int PRINT_MONTHLY_NOTE = 2;
	public static final int ADD_CLIENT_INFO = 3;
	public static final int ADD_GOOD_INFO = 4;

	public static final String GOOD_EXIST_WARNING_TITLE = "货物已存在";
	public static final String GOOD_EXIST_WARNING_MESSAGE = "此货物已存在，不能添加。\n你可以通过表格修改信息";

	public static final String GOOD_NAME_OR_PRICE_IS_EMPTY_TITLE = "货物名或单价为空";
	public static final String GOOD_NAME_OR_PRICE_IS_EMPTY_MESSAGE = "货物名或单价为空，请正确输入货物名称和单价";

	public static final String PRICE_IS_NOT_A_NUMBER_TITLE = "单价不为数字";
	public static final String PRICE_IS_NOT_A_NUMBER_MESSAGE = "单价只能输入数字，最多带2位小数";

	public static final String QUANTITY_IS_NOT_A_NUMBER_TITLE = "数量不为数字";
	public static final String QUANTITY_IS_NOT_A_NUMBER_MESSAGE = "数量只能输入整数";

	public static final String NO_SELECTED_ROW_TITLE = "没有选中的记录";
	public static final String NO_SELECTED_ROW_MESSAGE = "请在表中选择一行记录";

	public static final String DELETE_ROW_TITLE = "确定删除数据？";
	public static final String DELETE_ROW_MESSAGE = "确定要删除以下数据吗？\n";

	public static final String CLIENT_NAME_OR_PHONE_OR_ADDR_IS_EMPTY_TITLE = "客户，电话或者地址为空";
	public static final String CLIENT_NAME_OR_PHONE_OR_ADDR_IS_EMPTY_MESSAGE = "客户，电话或者地址为空，请正确输入客户资料";

	public static final String CLIENT_EXIST_WARNING_TITLE = "客户已存在";
	public static final String CLIENT_EXIST_WARNING_MESSAGE = "此客户已存在，不能添加。\n你可以通过表格修改信息";

	public static final String PHONE_FORMAT_INCORRECT_TITLE = "电话格式不正确";
	public static final String PHONE_FORMAT_INCORRECT_MESSAGE = "电话格式不正确，电话号码只能由 0-9，#，* 这几个字符组成";
	
	public static final String SERIAL_NUM_EXIST_TITLE = "此流水号已存在";
	public static final String SERIAL_NUM_EXIST_MESSAGE = "此流水号，已存在，是否要替换？";
}
