package resultsetextractor;
/**
 * �����������ȡ��
 */
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.Item;

public class OrderResultSetExtractor implements ResultSetExtractor{

	@Override
	public Object extractData(ResultSet rs) throws Exception {
		List<Item> items = new ArrayList<Item>();
		while(rs.next()){
			Item item = new Item();
			item.setId(rs.getInt("menu_id"));
			item.setName(rs.getString("name"));
			item.setNum(rs.getInt("num"));
			item.setPrice(rs.getDouble("price"));
			items.add(item);
		}
		return items;
	}

}
