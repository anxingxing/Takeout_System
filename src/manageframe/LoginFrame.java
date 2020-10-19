package manageframe;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import resultsetextractor.ManagerResultSetExtractor;
import start.MainFrame;
import util.JdbcTemplate;
import entity.Manager;

public class LoginFrame extends JFrame{
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 400;
	private int height = 280;
	private JdbcTemplate jdbcTemplate;
	//定义组件
	private JFrame frame;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton cancelButton;
	//从数据库中查询管理员表
	private List<Manager> select(){
		String sql = new StringBuffer()
					.append("select * ")
					.append("from t_manager ")
					.toString();
		return (List<Manager>) jdbcTemplate.query(sql, null, new ManagerResultSetExtractor());
		
	}
	public LoginFrame() {
		jdbcTemplate = new JdbcTemplate();
		//创建组件
		frame = new JFrame("管理员登录");
		usernameLabel = new JLabel("用户名：");
		usernameField = new JTextField();
		passwordLabel = new JLabel("密   码：");
		passwordField = new JPasswordField();
		loginButton = new JButton("登录");
		cancelButton = new JButton("取消");
		
	}
	//初始化界面
	private void init(){
		frame.setLayout(null);
		frame.add(usernameLabel);
		frame.add(usernameField);
		frame.add(passwordLabel);
		frame.add(passwordField);
		frame.add(loginButton);
		frame.add(cancelButton);
	}
	//设置窗口组件样式
	private void setStyle(){
		usernameLabel.setBounds(50, 50, 60, 25);
		usernameField.setBounds(130, 50, 200, 25);
		passwordLabel.setBounds(50, 100, 60, 25);
		passwordField.setBounds(130, 100, 200, 25);
		loginButton.setBounds(140, 150, 60, 40);
		cancelButton.setBounds(220, 150, 60, 40);
	}
	private void addEventHandler(){
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean flag = true;
				//从数据库中得到管理员表
				List<Manager> managers = select();
				//得到用户输入的信息
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				//判断用户名和密码是否为空
				if ("".equals(username) || "".equals(password)) {
					JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码不能为空！");
					return;
				}
				for (Manager manager : managers) {
					if (username.equals(manager.getName()) && password.equals(manager.getPassword())) {
						flag = true;
						break;
					}else{
						flag = false;
					}
				}
				if(flag){
					new ManageFrame().show();
					frame.dispose();
				}else{
					JOptionPane.showMessageDialog(LoginFrame.this, "登录信息错误！");
					return;
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new MainFrame().show();
				frame.dispose();
			}
		});
		
	}
	public void show(){
		init();
		setStyle();
		addEventHandler();
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
