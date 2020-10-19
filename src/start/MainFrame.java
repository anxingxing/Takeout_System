package start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import manageframe.LoginFrame;
import clientframe.ClientFrame;

/**
 * 餐馆点餐系统主界面
 */
public class MainFrame extends JFrame{
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	private int WIDTH = dimension.width/2;
	private int HEIGHT = dimension.height/2;
	private int width = 400;
	private int height = 300;
	//定义组件
	private JFrame frame;
	private JPanel northPanel;
	private JLabel welcomeLabel;
	private JPanel centerPanel;
	private JButton clientButton;
	private JButton managerButton;
	private JPanel southPanel;
	private JLabel bottomLabel;
	public MainFrame() {
		//创建组件
		frame = new JFrame("选择角色界面");
		welcomeLabel = new JLabel("欢迎使用订餐外卖系统");
		northPanel = new JPanel();
		clientButton = new JButton("客户");
		centerPanel = new JPanel();
		managerButton = new JButton("管理员");
		bottomLabel = new JLabel("@第37组");
		southPanel = new JPanel();
	}
	//初始化
	private void init(){
		northPanel.add(welcomeLabel);
		centerPanel.setLayout(null);
		centerPanel.add(clientButton);
		centerPanel.add(managerButton);
		southPanel.add(bottomLabel);
		frame.add(northPanel, BorderLayout.NORTH);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(southPanel, BorderLayout.SOUTH);
	}
	//设置样式
	private void setStyle(){
		welcomeLabel.setFont(new Font("黑体", Font.BOLD+Font.ITALIC, 30));
		clientButton.setBounds(100, 60, 80, 50);
		managerButton.setBounds(250, 60, 80, 50);
	}
	private void addEventHandler(){
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int op = JOptionPane.showConfirmDialog(frame, "是否退出程序？", "确认退出", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION) {
					System.exit(0);
				}else{
					return;
				}
			}
			
		});
		clientButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ClientFrame().show();
				frame.dispose();
			}
		});
		managerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginFrame().show();
				frame.dispose();
			}
		});
	}
	//显示
	public void show(){
		init();
		setStyle();
		addEventHandler();
		frame.setBounds(WIDTH-width/2, HEIGHT-height/2, width, height);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
