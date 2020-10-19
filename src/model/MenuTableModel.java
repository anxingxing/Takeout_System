package model;

import javax.swing.table.AbstractTableModel;

public class MenuTableModel extends AbstractTableModel{
	private Object[][] data;
	public MenuTableModel() {
		super();
	}
	public MenuTableModel(Object[][] data) {
		super();
		this.data = data;
	}
	@Override
	public int getRowCount() {
		return data.length;
	}
	@Override
	public int getColumnCount() {
		return 3;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	//表格的列名
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "菜单序号";
		case 1:
			return "菜名";
		case 2:
			return "单价";
		}
		return null;
	}
	

}
