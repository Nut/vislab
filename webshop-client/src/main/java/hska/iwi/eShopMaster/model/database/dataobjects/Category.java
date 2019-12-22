package hska.iwi.eShopMaster.model.database.dataobjects;

/**
 * This class contains details about categories.
 */
public class Category implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	// private Set<Product> products = new HashSet<Product>(0);
	private Long[] products = new Long[] {};

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, Long[] products) {
		this.name = name;
		this.products = products;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long[] getProducts() {
		return this.products;
	}

	public void setProducts(Long[] products) {
		this.products = products;
	}

}
