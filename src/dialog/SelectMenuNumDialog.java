package dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.MenuTableModel;
import util.JdbcTemplate;
import clientframe.ClientFrame;

public class SelectMenuNumDialog extends JDialog {
	//获得显示器大小
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 400;
	private int height = 420;
	private JdbcTemplate jdbcTemplate;
	//定义组件
	private JLabel orderIdLabel;
	private JTextField orderIdField;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel priceLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField priceField;
	private JLabel numLabel;
	private JButton minusButton;
	private JTextField numField;
	private JButton plusButton;
	private JLabel sumPriceLabel;
	private JTextField sumPriceField;
	private JButton confirmButton;
	private JButton cancelButton;
	public SelectMenuNumDialog(ClientFrame clientFrame, boolean modal, MenuTableModel menuTableModel, int rowNum, Integer id) {
		super(clientFrame, modal);
		//创建菜单
		jdbcTemplate = new JdbcTemplate();
		orderIdLabel = new JLabel("订单号");
		orderIdField = new JTextField(id+"");
		orderIdField.setEditable(false);
		idLabel = new JLabel("菜品序号：");
		idField = new JTextField();
		idField.setText(menuTableModel.getValueAt(rowNum, 0).toString());
		idField.setEditable(false);
		nameLabel = new JLabel("菜名：");
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setText((String)menuTableModel.getValueAt(rowNum, 1));
		priceLabel = new JLabel("单价：");
		priceField = new JTextField();
		priceField.setEditable(false);
		priceField.setText(menuTableModel.getValueAt(rowNum, 2).toString());
		numLabel = new JLabel("菜品数量：");
		minusButton = new JButton("-");
		numField = new JTextField("1");
		numField.setEditable(false);
		plusButton = new JButton("+");
		sumPriceLabel = new JLabel("价格：");
		sumPriceField = new JTextField();
		sumPriceField.setText(menuTableModel.getValueAt(rowNum, 2).toString());
		sumPriceField.setEditable(false);
		confirmButton = new JButton("添加到订单");
		cancelButton = new JButton("取消");
		
		init();
		addEventHandler();
		
		this.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		this.setVisible(true);
		
	}
	//初始化界面
	private void init(){
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		orderIdLabel.setBounds(30, 30, 80, 30);
		orderIdField.setBounds(120, 30, 200, 30);
		idLabel.setBounds(30, 80, 80, 30);
		idField.setBounds(120, 80, 200, 30);
		nameLabel.setBounds(30, 130, 80, 30);
		nameField.setBounds(120, 130, 200, 30);
		priceLabel.setBounds(30, 180, 80, 30);
		priceField.setBounds(120, 180, 200, 30);
		numLabel.setBounds(30, 230, 80, 30);
		minusButton.setBounds(120, 230, 50, 30);
		numField.setBounds(175, 230, 40, 30);
		plusButton.setBounds(220, 230, 50, 30);
		sumPriceLabel.setBounds(30, 280, 80, 30);
		sumPriceField.setBounds(120, 280, 200, 30);
		centerPanel.add(orderIdLabel);
		centerPanel.add(orderIdField);
		centerPanel.add(idLabel);
		centerPanel.add(idField);
		centerPanel.add(nameLabel);
		centerPanel.add(nameField);
		centerPanel.add(priceLabel);
		centerPanel.add(priceField);
		centerPanel.add(numLabel);
		centerPanel.add(minusButton);
		centerPanel.add(numField);
		centerPanel.add(plusButton);
		centerPanel.add(sumPriceLabel);
		centerPanel.add(sumPriceField);
		JPanel southPanel = new JPanel();
		southPanel.add(confirmButton);
		southPanel.add(cancelButton);
		
		this.add(centerPanel);
		this.add(southPanel, BorderLayout.SOUTH);
	}
	//按钮监听器
	private void addEventHandler(){
		minusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(numField.getText().trim());
				if (num==1) {
					minusButton.setEnabled(false);
				}else{
					numField.setText(num-1+"");
					double price = Double.parseDouble(priceField.getText());
					sumPriceField.setText(price*(num-1)+"");
				}
			}
		});
		plusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				minusButton.setEnabled(true);
				int num = Integer.parseInt(numField.getText().trim());
				numField.setText(num+1+"");
				double price = Double.parseDouble(priceField.getText());
				sumPriceField.setText(price*(num+1)+"");
			}
		});
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int menu_id = Integer.parseInt(idField.getText().trim());
				String name = nameField.getText().trim();
				int num = Integer.parseInt(numField.getText().trim());
				double price = Double.parseDouble(sumPriceField.getText().trim());
				int order_id = Integer.parseInt(orderIdField.getText().trim());
				String sql2 = new StringBuffer()
							.append("insert into t_item(menu_id, name, num ,price, order_id) ")
							.append("values (?, ?, ?, ?, ?) ")
							.toString();
				jdbcTemplate.update(sql2, new Object[] {menu_id,name,num,price,order_id});
				JOptionPane.showMessageDialog(SelectMenuNumDialog.this, "添加成功");
				SelectMenuNumDialog.this.dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectMenuNumDialog.this.dispose();
			}
		});
	}
}
