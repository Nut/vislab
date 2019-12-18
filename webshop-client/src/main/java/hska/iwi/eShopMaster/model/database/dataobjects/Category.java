package hska.iwi.eShopMaster.model.database.dataobjects;

import javax.persistence.*;

/**
 * This class contains details about categories.
 */
@Entity
@Table(name = "category")
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	public Long[] getProducts() {
		return this.products;
	}

	public void setProducts(Long[] products) {
		this.products = products;
	}

}
