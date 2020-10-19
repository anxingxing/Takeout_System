package model;

import javax.swing.table.AbstractTableModel;

public class OrderTableModel extends AbstractTableModel{
	private Object[][] data;
	public OrderTableModel() {
		super();
	}
	public OrderTableModel(Object[][] data){
		super();
		this.data = data;
	}
	@Override
	public int getRowCount() {
		return data.length;
	}
	@Override
	public int getColumnCount() {
		return 4;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "序号";
		case 1:
			return "菜名";
		case 2:
			return "数量";
		case 3:
			return "价格";
		}
		return null;
	}
}
