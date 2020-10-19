package clientframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import model.MenuTableModel;
import resultsetextractor.MenuListResultSetExtractor;
import util.JdbcTemplate;
import dialog.OrderDialog;
import dialog.SelectMenuNumDialog;
import entity.MenuList;

/**
 * 客户界面
 */
public class ClientFrame extends JFrame{
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 600;
	private int height = 400;
	//进入客户界面直接显示菜单
	private JdbcTemplate jdbcTemplate;
	//定义组件
	private JFrame frame;
	private JLabel label;
	private JLabel idLabel;
	private JLabel queryLabel;
	private JTextField queryField;
	private JButton queryButton;
	private JPanel northPanel;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton addButton;
	private JButton orderButton;
	private JButton cancelButton;
	private JPanel southPanel;
	public ClientFrame() {
		//创建组件
		jdbcTemplate = new JdbcTemplate();
		frame = new JFrame("客户");
		label = new JLabel("     ID：");
		queryLabel = new JLabel("请输入查找关键字：");
		queryField = new JTextField(20);
		queryButton = new JButton("查找");
		northPanel = new JPanel();
		addButton = new JButton("选择");
		orderButton = new JButton("查看订单");
		cancelButton = new JButton("返回");
		southPanel = new JPanel();
	}
	//从数据库中得到菜单
	private List<MenuList> select(){
		String sql = new StringBuffer()
					.append("select * ")
					.append("from t_menu ")
					.toString();
		return (List<MenuList>) jdbcTemplate.query(sql, null, new MenuListResultSetExtractor());
	}
	private Date time;
	private String no;
	private Integer id;
	private MenuTableModel menuTableModel;
	//初始化界面
	private void init(){
		//将菜单以表格形式显示
		List<MenuList> lists = select();
		Object[][] data = new Object[lists.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = lists.get(i).getId();
			data[i][1] = lists.get(i).getName();
			data[i][2] = lists.get(i).getPrice();
		}
		SimpleDateFormat noFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		time = new Date();
		//生成订单号
		no = noFormat.format(time)+(int)(Math.random()*1000)+"";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//生成订单创建时间
		String date = dateFormat.format(time);
		String sql = new StringBuffer()
		.append("insert into t_order(no,create_date) ")
		.append("values(?,?) ")
		.toString();
		id=jdbcTemplate.getPrimaryKey(sql, no,date);
		
		idLabel = new JLabel(no);
		menuTableModel = new MenuTableModel(data);
		northPanel.add(queryLabel);
		northPanel.add(queryField);
		northPanel.add(queryButton);
		northPanel.add(label);
		northPanel.add(idLabel);
		
		table = new JTable(menuTableModel);
		scrollPane = new JScrollPane(table);
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(scrollPane);
		
		southPanel.add(addButton);
		southPanel.add(orderButton);
		southPanel.add(cancelButton);
		frame.add(southPanel, BorderLayout.SOUTH);
	}
	//按钮监听器的方法
	private void addEventHandler(){
		queryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = queryField.getText().trim();
				String sql = new StringBuffer()
						.append("select * ")
						.append("from t_menu ")
						.append("where name like ? ")
						.toString();
				List<MenuList> lists = (List<MenuList>) jdbcTemplate.query(sql, new Object[] {"%"+name+"%"}, new MenuListResultSetExtractor());
				Object[][] data = new Object[lists.size()][3];
				for (int i = 0; i < data.length; i++) {
					data[i][0] = lists.get(i).getId();
					data[i][1] = lists.get(i).getName();
					data[i][2] = lists.get(i).getPrice();
				}
				menuTableModel = new MenuTableModel(data);
				table.setModel(menuTableModel);
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNum = table.getSelectedRow();
				if (rowNum==-1) {
					JOptionPane.showMessageDialog(ClientFrame.this, "请选择一行");
					return;
				}else{
					new SelectMenuNumDialog(ClientFrame.this, true, menuTableModel, rowNum, id);
				}
			}
		});
		orderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new OrderDialog(ClientFrame.this,true,id,no,time);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql = new StringBuffer()
						.append("delete from t_order ")
						.append("where id=? ")
						.toString();
				jdbcTemplate.update(sql, new Object[] {id});
				frame.dispose();
			}
		});
	}
	//显示界面
	public void show(){
		init();
		addEventHandler();
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
