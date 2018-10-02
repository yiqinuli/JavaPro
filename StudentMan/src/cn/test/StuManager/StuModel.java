package cn.test.StuManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


import com.mysql.jdbc.RowData;


public class StuModel extends AbstractTableModel{

	//rowData用来存放行数据，columNames存放列名
	Vector rowData,columnNames;
	//定义操作数据库需要的组件
	PreparedStatement ps=null;
	Connection ct=null;
	ResultSet rs=null;
	public void init(String sql ) {
		if (sql==""||sql.equals(null)) {
			sql="select * from stu";
		}
		//中间
		columnNames=new Vector<>();
		//设置列名
		columnNames.add("学号");
		columnNames.add("名字");
		columnNames.add("性别");
		columnNames.add("年龄");
		columnNames.add("籍贯");
		columnNames.add("系别");
		
		rowData=new Vector<>();
		//rowData可以存放多行
		try {
			//1.加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			//2.得到连接
			ct=DriverManager.getConnection("jdbc:sqlserver://localhost:1433", "sa", "qin18865510816");
			ps=ct.prepareStatement(sql);
			rs=ps.executeQuery();
			while (rs.next()) {
				Vector hang=new Vector<>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				hang.add(rs.getString(5));
				hang.add(rs.getString(6));
				//加入rowData
				rowData.add(hang);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	//构造函数，用于初始化我们的数据模型
	public StuModel(String sql) {
		// TODO Auto-generated constructor stub
		this.init(sql);
	}
	//构造函数
	 public StuModel() {
		// TODO Auto-generated constructor stub
		 this.init("");
	}
		//得到共有多少列
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}
@Override
public String getColumnName(int column) {
	// TODO Auto-generated method stub
	return (String)this.columnNames.get(column);
}
//得到共有多少行
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}
	//得到某行某列的数据
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return ((Vector)this.rowData.get(rowIndex)).get(columnIndex);
	}

}
