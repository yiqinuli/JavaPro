package cn.test.StuManager;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.mysql.jdbc.PreparedStatement;

/**
 * 
 * @author lihua
 *学生管理系统 mini1
 *1.查询
 *2.添加
 */
public class StuMange extends JFrame implements ActionListener{
//定义组件
	JPanel jp1,jp2;
	JLabel jl1;
	JButton jb1,jb2,jb3,jb4;
	JTable jt;
	JScrollPane jsp;
	JTextField jtf;
	StuModel sm;
	//定义操作数据需要的组件
	java.sql.PreparedStatement ps=null;
	Connection ct;
	ResultSet rs=null;
	public static void main(String[] args) {
		try {
			//将当前窗体的外观设置为所在操作系统的外观
       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new StuMange();		
	}
	//构造函数
   public StuMange() {
		// TODO Auto-generated constructor stub
	   jp1=new JPanel();
	   jtf=new JTextField(10);
	   jb1=new JButton("查询");
	   jb1.addActionListener(this);
	   jl1=new JLabel("请输入名字");
	   //把各个空间加入队
	   jp1.add(jl1);
	   jp1.add(jtf);
	   jp1.add(jb1);
	   
	   jp2=new JPanel();
	   jb2=new JButton("添加");
	   jb2.addActionListener(this);
	   jb3=new JButton("修改");
	   jb3.addActionListener(this);
	   jb4=new JButton("删除");
	   jb4.addActionListener(this);
	   //把各个按钮加入到jp2中
	   jp2.add(jb2);
	   jp2.add(jb3);
	   jp2.add(jb4);
	   //创建一个数据对象模型
	   sm=new StuModel();
	   //初始化JTabel
	   jt=new JTable(sm);
	   //初始化jsp 
	   jsp=new JScrollPane(jt);
	   //将jsp放入jframe
	   this.add(jsp);
	   this.add(jp1,"North");
	   this.add(jp2,"South");
	   
	   this.setSize(400,300);
	   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource()==jb1) {
			//因为把对表的数据封装到StuModel中，我们就可以比较简单的完成查询
			String name =this.jtf.getText();
			//写一个SQL语句
			String sql ="select * from stu where stuName='"+name+"'";
			//构建新的数据模型类，并更新
			sm=new StuModel(sql);
			//更新JTabel
			jt.setModel(sm);
		}
		//用户点击添加时
		else if (e.getSource()==jb2) {
			StuAddDialog sa=new StuAddDialog(this, "添加学生信息", true);
			//重新再获得数据模型
			//构建新的数据类型，并更新
			sm =new StuModel();
			//更新JTable
			jt.setModel(sm);
		}
		//用户修改数据
		else if (e.getSource()==jb3) {
			int rowNum=this.jt.getSelectedRow();
			if (rowNum==-1) {
				//提示
				JOptionPane.showMessageDialog(this, "请选择一行", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//显示修改对话框			
			new StuUpDialog(this, "修改用户信息",true , sm, rowNum);
			//更新数据模型
			sm=new StuModel();
			//更新JTable
			jt.setModel(sm);
			//用户点击删除时，删除一条选中的数据
		}
		else if (e.getSource()==jb4) {
			//1.得到学生的ID号
			//getSelectedRow会返回用户点中得行
			//如果该用户一行都没有选择，就会返回-1
			int rowNum=this.jt.getSelectedRow();
			if (rowNum==-1) {
				//提示
				JOptionPane.showMessageDialog(this, "请选择一行", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//得到学生编号
			String stuId=(String)sm.getValueAt(rowNum, 0);
			//连接数据库，完成删除任务
			try {
				//1.加载驱动
				Class.forName("com.mysql.jdbc.Driver");
				//2.得到连接
				ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "sa", "qin18865510816");
			    ps=ct.prepareStatement("delete from stu where stuid=?");
			   ps.setString(1, stuId);
			   ps.executeUpdate();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}finally {
				try {
					if (rs!=null) {
						rs.close();
					}
					if (ps!=null) {
						ps.close();
					}
					if (ct!=null) {
						ct.close();
					}
				} catch (SQLException e1) {
					// TODO: handle exception
					e1.printStackTrace();
				}
				
			}
			//更新数据模型
			sm =new StuModel();
			//更新JTable
			jt.setModel(sm);
		}
	}

}
