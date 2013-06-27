import java.io.File;
import java.util.List;
import java.util.Map;

import org.eobjects.metamodel.DataContextFactory;
import org.eobjects.metamodel.UpdateCallback;
import org.eobjects.metamodel.UpdateScript;
import org.eobjects.metamodel.UpdateableDataContext;
import org.eobjects.metamodel.create.TableCreationBuilder;
import org.eobjects.metamodel.insert.RowInsertionBuilder;
import org.eobjects.metamodel.schema.Schema;

public class Writer
{
	private UpdateableDataContext	dataContext;

	public Writer()
	{

	}

	public void openFile(String fileStr)
	{
		File file = new File(fileStr);
		dataContext = DataContextFactory.createExcelDataContext(file);

	}

	public void writeHeader(final String[] headerList, final String tableName)
	{

		final Schema schema = dataContext.getDefaultSchema();
		dataContext.executeUpdate(new UpdateScript()
			{
				public void run(UpdateCallback callback)
				{
					TableCreationBuilder tcb = callback.createTable(schema, tableName);

					for (String header : headerList)
					{
						tcb.withColumn(header);
					}
					tcb.execute();
				}
			});
	}

	public void writeData(final List<Map<String, Object>> data, final String tableName)
	{

		dataContext.executeUpdate(new UpdateScript()
			{

				public void run(UpdateCallback callback)
				{
					RowInsertionBuilder rib = callback.insertInto(tableName);

					for (int index = 0; index < data.size(); index++)
					{
						for (String key : data.get(index).keySet())
						{
							rib.value(key, data.get(index).get(key));
						}
						rib.execute();
					}
				}
			});
	}

	public void addRow(final Map<String, Object> row, final String tableName)
	{
		dataContext.executeUpdate(new UpdateScript()
			{

				public void run(UpdateCallback callback)
				{
					RowInsertionBuilder rib = callback.insertInto(tableName);

					for (String key : row.keySet())
					{
						rib.value(key, row.get(key));
					}
					rib.execute();
				}
			});

	}
}
