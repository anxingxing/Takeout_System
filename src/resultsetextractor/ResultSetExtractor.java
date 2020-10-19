package resultsetextractor;

import java.sql.ResultSet;

//结果集提取器
public interface ResultSetExtractor {

	public Object extractData(ResultSet rs)throws Exception;
}
