package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Order implements Serializable{
	private String no;//订单号
	private Integer id;
	private Date create_data;
	private Double price;
	private Integer count;
	private Set<Item> items = new HashSet<Item>();
	public Order() {
		super();
	}
	public void addItem(Item item){
		items.add(item);
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreate_data() {
		return create_data;
	}
	public void setCreate_data(Date create_data) {
		this.create_data = create_data;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Set<Item> getItems() {
		return items;
	}
	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
}
