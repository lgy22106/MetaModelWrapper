import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.schema.Table;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Reformatter r = new Reformatter();
		Table[] ts = r.readExcel("test.xls");
		Table t = ts[0];

		Query q = r.queryBuilder(null, t);// get all

		// Query q = r.queryBuilder(r.columnsBuilder(t, "Product"), t, r.filterBuilder("Product", "=", "WL", t));

		r.printResult(q);
	}
}
