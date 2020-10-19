package manageframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import start.MainFrame;
import util.JdbcTemplate;
import entity.MenuList;

public class ManageFrame extends JFrame{
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 500;
	private int height = 400;
	private JdbcTemplate jdbcTemplate;
	
	//定义组件
	private JFrame frame;
	private JLabel queryLabel;
	private JTextField queryField;
	private JButton queryButton;
	private JButton exitButton;
	private JPanel northPanel;
	private JButton addButton;
	private JButton deleteButton;
	private JButton updateButton;
	private JPanel southPanel;
	private JTable table;
	private JScrollPane scrollPane;
	
	public ManageFrame() {
		jdbcTemplate = new JdbcTemplate();
		//创建组件
		frame = new JFrame("管理员");
		queryLabel = new JLabel("请输入查找关键字：");
		queryField = new JTextField(20);
		queryButton = new JButton("查找");
		exitButton = new JButton("登出");
		northPanel = new JPanel();
		addButton = new JButton("增加");
		deleteButton = new JButton("删除");
		updateButton = new JButton("修改");
		southPanel = new JPanel();
	}
	private List<MenuList> select(){
		String sql = new StringBuffer()
					.append("select * ")
					.append("from t_menu ")
					.toString();
		return (List<MenuList>) jdbcTemplate.query(sql, null, new MenuListResultSetExtractor());
		
	}
	private MenuTableModel menuTableModel;
	//初始化界面
	public void init(){
		//从数据库中获得菜单信息并显示在界面中
		List<MenuList> lists = select();
		Object[][] data = new Object[lists.size()][3];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = lists.get(i).getId();
			data[i][1] = lists.get(i).getName();
			data[i][2] = lists.get(i).getPrice();
		}
		menuTableModel = new MenuTableModel(data);
		northPanel.add(queryLabel);
		northPanel.add(queryField);
		northPanel.add(queryButton);
		northPanel.add(exitButton);
		southPanel.add(addButton);
		southPanel.add(deleteButton);
		southPanel.add(updateButton);
		table = new JTable(menuTableModel);
		scrollPane = new JScrollPane(table);
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(scrollPane);
		frame.add(southPanel, BorderLayout.SOUTH);
	}
	//按钮监听器
	private void addEventHandler(){
		//模糊查询
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
		//添加菜单
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				List<MenuList> lists = select();
				new AddMenuFrame(lists).show();
				frame.dispose();
			}
		});
		//删除菜单
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer rowNum = table.getSelectedRow();
				if (rowNum == -1) {
					JOptionPane.showMessageDialog(ManageFrame.this, "请选择一行");
					return;
				}else{
					Integer id = (Integer) menuTableModel.getValueAt(rowNum, 0);
					String sql = new StringBuffer()
								.append("delete from t_menu ")
								.append("where id=? ")
								.toString();
					jdbcTemplate.update(sql, new Object[] {id});
					JOptionPane.showMessageDialog(ManageFrame.this, "删除成功");
					new ManageFrame().show();
					frame.dispose();
			}
			}
		});
		//修改菜单
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//选中一行
				int rowNum = table.getSelectedRow();
				if (rowNum==-1) {
					JOptionPane.showMessageDialog(ManageFrame.this, "请选择一行");
					return;
				}else{
					//将选中的表格信息传到下一界面
					new UpdateFrame(menuTableModel, rowNum).show();
					frame.dispose();
				}
			}
		});
		//退出
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new MainFrame().show();
				frame.dispose();
			}
		});
	}
	public void show(){
		init();
		addEventHandler();
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
