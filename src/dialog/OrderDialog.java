package dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import model.OrderTableModel;
import resultsetextractor.OrderResultSetExtractor;
import util.JdbcTemplate;
import clientframe.ClientFrame;
import entity.Item;

public class OrderDialog extends JDialog {
	//获得显示器像素大小
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 600;
	private int height = 400;
	private JdbcTemplate jdbcTemplate;
	//定义组件
	private JLabel orderLabel;
	private JTextField orderField;
	private JLabel createdateLabel;
	private JTextField createdateField;
	private JPanel northPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel pricelLabel;
	private JTextField priceField;
	private JButton commitButton;
	private JButton deleteButton;
	private JButton returnButton;
	private JPanel southPanel;
	
	private Integer order_id;
	private String order_no;
	private OrderTableModel model;
	public OrderDialog(ClientFrame clientFrame, boolean modal, Integer id, String no, Date time) {
		super(clientFrame, modal);
		jdbcTemplate = new JdbcTemplate();
		order_id = id;
		order_no = no;
		orderLabel = new JLabel("订单号");
		orderField = new JTextField(order_no);
		orderField.setEditable(false);
		createdateLabel = new JLabel("创建日期");
		SimpleDateFormat noFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = noFormat.format(time);
		createdateField = new JTextField(date);
		createdateField.setEditable(false);
		northPanel = new JPanel();
		pricelLabel = new JLabel("总价:");
		priceField = new JTextField();
		commitButton = new JButton("提交");
		deleteButton = new JButton("删除");
		returnButton = new JButton("返回");
		southPanel = new JPanel();
		
		init();
		addEventHandler();
		//窗口位置及大小,显示窗口
		this.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		this.setVisible(true);
	}
	//从数据库中获得订单明细表，并显示
	private List<Item> select(){
		String sql = new StringBuffer()
				.append("select * ")
				.append("from t_item ")
				.append("where order_id=? ")
				.toString();
		return (List<Item>) jdbcTemplate.query(sql, new Object[] {order_id}, new OrderResultSetExtractor());
	}
	//初始化界面
	private void init(){
		Double price = 0.0;
		List<Item> items = select();
		Object[][] data = new Object[items.size()][4];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = items.get(i).getId();
			data[i][1] = items.get(i).getName();
			data[i][2] = items.get(i).getNum();
			data[i][3] = items.get(i).getPrice();
			price = price+items.get(i).getPrice();//显示的订单中的所有菜类总价
		}
		//向订单表中插入订单价格
		String sql = new StringBuffer()
				.append("update t_order ")
				.append("set price=? ")
				.append("where id=? ")
				.toString();
		jdbcTemplate.update(sql, new Object[] {price,order_id});
		model = new OrderTableModel(data);
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		northPanel.add(orderLabel);
		northPanel.add(orderField);
		northPanel.add(createdateLabel);
		northPanel.add(createdateField);
		
		priceField.setEditable(false);
		priceField.setText(price+"");
		southPanel.add(pricelLabel);
		southPanel.add(priceField);
		southPanel.add(commitButton);
		southPanel.add(deleteButton);
		southPanel.add(returnButton);
		this.add(northPanel, BorderLayout.NORTH);
		this.add(scrollPane);
		this.add(southPanel, BorderLayout.SOUTH);
	}
	//按钮监听器
	private void addEventHandler(){
		commitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(OrderDialog.this, "提交成功！打印订单，请等待。。。");
				JOptionPane.showMessageDialog(OrderDialog.this, "打印成功！正在退出。。。");
				System.exit(0);
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer rowNum = table.getSelectedRow();
				if (rowNum == -1) {
					JOptionPane.showMessageDialog(OrderDialog.this, "请选择一行");
					return;
				}else{
					Integer id = (Integer) table.getValueAt(rowNum, 0);
					String sql = new StringBuffer()
								.append("delete from t_item ")
								.append("where id=? ")
								.toString();
					jdbcTemplate.update(sql, new Object[] {id});
					JOptionPane.showMessageDialog(OrderDialog.this, "删除成功");
					OrderDialog.this.dispose();
			}
			}
		});
		returnButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OrderDialog.this.dispose();
			}
		});
	}
}
