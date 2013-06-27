import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eobjects.metamodel.DataContext;
import org.eobjects.metamodel.DataContextFactory;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.data.Row;
import org.eobjects.metamodel.query.FilterItem;
import org.eobjects.metamodel.query.OperatorType;
import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.query.SelectItem;
import org.eobjects.metamodel.query.builder.SatisfiedSelectBuilder;
import org.eobjects.metamodel.schema.Column;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;

public class Reformatter
{
	private DataContext	dataContext;

	public Table[] readExcel(String file)
	{
		File excel = new File(file);

		dataContext = DataContextFactory.createExcelDataContext(excel);

		Schema schema = dataContext.getDefaultSchema();

		Table[] tables = schema.getTables();
		return tables;
	}

	public Query queryBuilder(List<Column> selectors, Table table, FilterItem... filterItems)
	{
		SatisfiedSelectBuilder selectLvl;
		Query q;
		if (selectors == null || selectors.isEmpty())
		{
			selectLvl = dataContext.query().from(table).selectAll();
		}
		else
		{
			selectLvl = dataContext.query().from(table).select(selectors.toArray(new Column[0]));
		}
		if (filterItems == null || filterItems.length == 0)
		{
			q = selectLvl.toQuery();
		}
		else
		{
			q = selectLvl.where(filterItems).toQuery();
		}

		return q;

	}

	public FilterItem filterBuilder(String columnName, String operatorStr, Object val, Table table)
	{
		SelectItem item = new SelectItem(table.getColumnByName(columnName));
		OperatorType operator = null;
		if (operatorStr.equals("="))
		{
			operator = OperatorType.EQUALS_TO;
		}
		else if (operatorStr.equals("like"))
		{
			operator = OperatorType.LIKE;
		}
		else if (operatorStr.equals("<"))
		{
			operator = OperatorType.LESS_THAN;
		}
		else if (operatorStr.equals(">"))
		{
			operator = OperatorType.GREATER_THAN;
		}
		else if (operatorStr.equals("in"))
		{
			operator = OperatorType.IN;
		}

		FilterItem filter = new FilterItem(item, operator, val);
		return filter;

	}

	public List<Column> columnsBuilder(Table table, String... columnNames)
	{
		List<Column> columns = new ArrayList<Column>();

		for (String columnName : columnNames)
		{
			columns.add(table.getColumnByName(columnName));
		}

		return columns;
	}

	public DataSet getResult(Query q)
	{
		return dataContext.executeQuery(q);
	}

	public void printResult(Query q)
	{
		DataSet ds = getResult(q);
		while (ds.next())
		{
			Row row = ds.getRow();
			System.out.println(row);
		}

	}
}
