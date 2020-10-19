package resultsetextractor;
/**
 * 菜单结果集提取器
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.MenuList;

public class MenuListResultSetExtractor implements ResultSetExtractor{

	@Override
	public Object extractData(ResultSet rs) throws Exception {
		List<MenuList> menus = new ArrayList<MenuList>();
		while(rs.next()){
		MenuList menu = new MenuList();
		menu.setId(rs.getInt("id"));
		menu.setName(rs.getString("name"));
		menu.setPrice(rs.getDouble("price"));
		menus.add(menu);
		}
		return menus;
	}

}
