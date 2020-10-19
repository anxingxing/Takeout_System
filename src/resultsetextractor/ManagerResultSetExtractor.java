package resultsetextractor;
/**
 * 管理员结果集提取器
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.Manager;

public class ManagerResultSetExtractor implements ResultSetExtractor{

	public Object extractData(ResultSet rs) throws Exception {
		List<Manager> managers = new ArrayList<Manager>();
		while(rs.next()){
			Manager manager = new Manager();
			manager.setId(rs.getInt("id"));
			manager.setName(rs.getString("name"));
			manager.setPassword(rs.getString("password"));
			managers.add(manager);
		}
		return managers;
	}

}
