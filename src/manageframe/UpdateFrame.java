package manageframe;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.MenuTableModel;
import util.JdbcTemplate;


public class UpdateFrame extends JFrame{
	private JdbcTemplate jdbcTemplate;
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 400;
	private int height = 300;
	//定义组件
	private JFrame frame;
	private JLabel idLabel;
	private JLabel nameLabel;
	private JLabel priceLabel;
	private JTextField idField;
	private JTextField nameField;
	private JTextField priceField;
	private JPanel centerPanel;
	private JButton updateButton;
	private JButton cancelButton;
	private JPanel southPanel;
	
	public UpdateFrame() {
		super();
	}
	public UpdateFrame(MenuTableModel menuTableModel,int rowNum) {
		jdbcTemplate = new JdbcTemplate();
		//创建组件
		//获得上一界面传的信息
		frame = new JFrame("修改菜单");
		idLabel = new JLabel("菜品序号：");
		idField = new JTextField();
		idField.setText(menuTableModel.getValueAt(rowNum, 0).toString());
		idField.setEditable(false);
		nameLabel = new JLabel("菜名：");
		nameField = new JTextField();
		nameField.setText((String)menuTableModel.getValueAt(rowNum, 1));
		priceLabel = new JLabel("价格：");
		priceField = new JTextField();
		priceField.setText(menuTableModel.getValueAt(rowNum, 2).toString());
		centerPanel = new JPanel();
		updateButton = new JButton("修改");
		cancelButton = new JButton("取消");
		southPanel = new JPanel();
	}
	//初始化界面
	private void init(){
		centerPanel.setLayout(null);
		idLabel.setBounds(30, 30, 80, 30);
		idField.setBounds(120, 30, 200, 30);
		nameLabel.setBounds(30, 80, 80, 30);
		nameField.setBounds(120, 80, 200, 30);
		priceLabel.setBounds(30, 130, 80, 30);
		priceField.setBounds(120, 130, 200, 30);
		
		centerPanel.add(idLabel);
		centerPanel.add(idField);
		centerPanel.add(nameLabel);
		centerPanel.add(nameField);
		centerPanel.add(priceLabel);
		centerPanel.add(priceField);
		
		southPanel.add(updateButton);
		southPanel.add(cancelButton);
		
		frame.add(centerPanel);
		frame.add(southPanel, BorderLayout.SOUTH);
	}
	//按钮监听器
	private void addEventHandler(){
		//更新数据库信息
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer id;
				String name;
				Double price;
				try {
					id = Integer.parseInt(idField.getText().trim());
					price = Double.parseDouble(priceField.getText().trim());
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(UpdateFrame.this, "信息格式错误");
					return;
				}
				name = nameField.getText().trim();
				if (null==name || "".equals(name)) {
					JOptionPane.showMessageDialog(UpdateFrame.this, "菜名不能为空");
					return;
				}
				String sql = new StringBuffer()
							.append("update t_menu ")
							.append("set name=?,price=? ")
							.append("where id=? ")
							.toString();
				jdbcTemplate.update(sql, new Object[] {name,price,id});
				JOptionPane.showMessageDialog(UpdateFrame.this, "修改成功");
				new ManageFrame().show();
				frame.dispose();
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
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
