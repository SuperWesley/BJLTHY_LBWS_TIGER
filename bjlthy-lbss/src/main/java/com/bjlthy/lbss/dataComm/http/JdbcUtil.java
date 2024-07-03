package com.bjlthy.lbss.dataComm.http;

import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcUtil {

	private static Logger log = LogbackUtil.getLogger("JdbcUtil","JdbcUtil");

	/**
	 * 
	 * @author 张宁 @Description: 新增各工作面煤量信息 @return void 返回类型 @date 2022年7月9日
	 * 上午10:11:48 @throws
	 */
	public static void insertALLAreaSumWeight(LbssSumWeightDay weight) {

		// 注册驱动 使用驱动连接数据库
		Connection con = getCon();
		PreparedStatement stmt = null;
		try {
			// 4定义sql语句
			String sql = "insert into lbss_sumweight_area(working_area,day,total_volume,total_weight,density,gangueRatio,week,remark) values(?,?,?,?,?,?,?,?)";
			// 5为sql语句赋值
			stmt = con.prepareStatement(sql);
			stmt.setString(1, weight.getBelt_name());
			stmt.setString(2, weight.getDay());
			stmt.setString(3, weight.getTotalVolume()==null?"0.0":weight.getTotalVolume().toString());
			stmt.setString(4, weight.getTotalWeight()==null?"0.0":weight.getTotalWeight().toString());
			stmt.setString(5, weight.getDensity()==null?"0.0":weight.getDensity().toString());
			stmt.setString(6, weight.getGangueRatio()==null?"0.0":weight.getGangueRatio().toString());
			stmt.setString(7, weight.getWeek());
			stmt.setString(8, weight.getRemark());
			// 返回值代表收到影响的行数
			int result = stmt.executeUpdate();
			// 关闭
			stmt.close();
			con.close();
		} catch (Exception e) {
			log.error("insertALLAreaSumWeight-->",e);
		}
	}

	/**
	 * 
	 * @author 张宁 @Description: 更新各工作面煤量信息 @return void 返回类型 @date 2022年7月9日
	 * 上午10:11:24 @throws
	 */
	public static void updateALLAreaSumWeight(LbssSumWeightDay weight) {

		
		// 注册驱动 使用驱动连接数据库
		Connection con = getCon();
		PreparedStatement stmt = null;
		try {
			// 4定义sql语句
			String sql = "UPDATE  lbss_sumweight_area SET working_area=?,day=?,total_volume=?,total_weight=?,density=?,gangueRatio=?,week=?,remark=? where id=?";
			// 5为sql语句赋值
			stmt = con.prepareStatement(sql);
			stmt.setString(1, weight.getBelt_name());
			stmt.setString(2, weight.getDay());
			stmt.setString(3, weight.getTotalVolume().toString());
			stmt.setString(4, weight.getTotalWeight().toString());
			stmt.setString(5, weight.getDensity().toString());
			stmt.setString(6, weight.getGangueRatio().toString());
			stmt.setString(7, weight.getWeek());
			stmt.setString(8, weight.getRemark());
			stmt.setString(9, weight.getId().toString());
			// 返回值代表收到影响的行数
			int result = stmt.executeUpdate();
			System.out.println("更新成功");
			// 关闭
			stmt.close();
			con.close();
		} catch (Exception e) {
			log.error("updateALLAreaSumWeight-->",e);
		}
	}
	
	/**
	 * 获取数据库连接驱动
	 * @return
	 */
	public static Connection getCon(){
		// 注册驱动 使用驱动连接数据库
		Connection con = null;
		String opcurl = ConfigBean.endpointUrl.split(":")[1];
		try {
			// 1加载SQLserver驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 2创建连接
			String url = "jdbc:mysql:"+opcurl+":3306/lbws_balasu?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
			String username = "root";
			String password = "bjlthy";
			// 3获取连接对象
			con = DriverManager.getConnection(url, username, password);
			
		}catch (Exception e) {
			log.error("getCon-->",e);
		}
		return con;			
	}

	/**
	 * 根据工作面获取服务器数据库
	 * @param working_area
	 * @return
	 */
	public static JdbcTemplate getJdbcTemplate(String working_area){
		//获取服务地址
		String serverIp = DictionariesHelper.getDicStringValueByCode(working_area);
		String driver = "com.mysql.cj.jdbc.Driver";
		String url ="jdbc:mysql:"+serverIp+":3306/lbws_hongliulin?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";//连接地址
		String user ="root";//用户
		String password ="bjlthy";//密码

		DriverManagerDataSource dataSource=new DriverManagerDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(user);
		dataSource.setPassword(password);

		JdbcTemplate  jdbcTemplate=new JdbcTemplate(dataSource);

		return jdbcTemplate;
	}
	
}
