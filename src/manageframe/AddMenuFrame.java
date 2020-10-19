package manageframe;
/**
 * 添加菜单
 */
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
import javax.swing.JTextField;

import entity.MenuList;
import util.JdbcTemplate;


public class AddMenuFrame extends JFrame{
	//获得显示器大小
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 400;
	private int height = 300;
	private JdbcTemplate jdbcTemplate;
	//定义组件
	private JFrame frame;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel priceLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField priceField;
	private JButton addButton;
	private JButton cancelButton;
	private JPanel southPanel;
	private JPanel centerPanel;
	
	private Integer id=0;
	private String name=null;
	private Double price=0.0;
	private Integer[] ids ;
	
	public AddMenuFrame(List<MenuList> lists) {
		jdbcTemplate = new JdbcTemplate();
		//创建组件
		frame = new JFrame("添加菜单");
		idLabel = new JLabel("菜品序号：");
		nameLabel = new JLabel("菜名：");
		priceLabel = new JLabel("单价：");
		idField = new JTextField();
		nameField = new JTextField();
		priceField = new JTextField();
		addButton = new JButton("添加");
		cancelButton = new JButton("取消");
		southPanel = new JPanel();
		centerPanel = new JPanel();
		//获得数据库中已有的菜单id，用于查重
		ids = new Integer[lists.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i]=lists.get(i).getId();
		}
	}
	//初始化界面
	private void init(){
		idLabel.setBounds(30, 30, 80, 30);
		idField.setBounds(120, 30, 200, 30);
		nameLabel.setBounds(30, 80, 80, 30);
		nameField.setBounds(120, 80, 200, 30);
		priceLabel.setBounds(30, 130, 80, 30);
		priceField.setBounds(120,130, 200, 30);
		centerPanel.setLayout(null);
		centerPanel.add(idLabel);
		centerPanel.add(idField);
		centerPanel.add(nameLabel);
		centerPanel.add(nameField);
		centerPanel.add(priceLabel);
		centerPanel.add(priceField);
		southPanel.add(addButton);
		southPanel.add(cancelButton);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.add(centerPanel);
	}
	//按钮监听器
	private void addEventHandler(){
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//捕获转换异常，并提示
					id = Integer.parseInt(idField.getText().trim());
					price = Double.parseDouble(priceField.getText().trim());
					if (null==id || "".equals(id)) {
						return; 
					}
					if (null==price || "".equals(price)) {
						return; 
					}
					for (int i = 0; i < ids.length; i++) {
						if (id==ids[i]) {
							JOptionPane.showMessageDialog(AddMenuFrame.this, "菜单序号"+id+"已存在，不能插入");
							return;
						}
					}
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(AddMenuFrame.this, "信息格式错误\n菜品序号和价格必须为数字");
					return;
				}
				name = nameField.getText();
				if (null==name || "".equals(name.trim())) {
					JOptionPane.showMessageDialog(AddMenuFrame.this, "菜名不能为空");
					return; 
				}else if (name!=null) {
					String sql = new StringBuffer()
					.append("insert into t_menu(id, name, price) ")
					.append("values(?,?,?) ")
					.toString();
					jdbcTemplate.update(sql, new Object[] {id, name, price});
					JOptionPane.showMessageDialog(AddMenuFrame.this, "添加成功");
					//初始化
					idField.setText("");
					nameField.setText("");
					priceField.setText("");
					return;
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ManageFrame().show();
				frame.dispose();
			}
		});
	}
	public void show(){
		init();
		addEventHandler();
		frame.setVisible(true);
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
}
