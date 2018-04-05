/**
 * 
 */
package simpledb;

/**
 * @author Facundo Manuel Quiroga
 * Oct 28, 2008
 * 
 */
public class Table {
	
	DbFile file;
	TupleDesc tupleDesc;
	String name;
	
	
	public Table(DbFile file, TupleDesc tupleDesc, String name) {
		super();
		this.file = file;
		this.tupleDesc = tupleDesc;
		this.name = name;
	}
	
	public DbFile getFile() {
		return this.file;
	}
	public void setFile(DbFile file) {
		this.file = file;
	}
	public TupleDesc getTupleDesc() {
		return this.tupleDesc;
	}
	public void setTupleDesc(TupleDesc tupleDesc) {
		this.tupleDesc = tupleDesc;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
