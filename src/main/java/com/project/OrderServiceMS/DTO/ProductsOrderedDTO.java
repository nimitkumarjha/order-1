package com.project.OrderServiceMS.DTO;

import java.math.BigDecimal;




public class ProductsOrderedDTO {
	private Integer orderId;
	private Integer prodId;
	private Integer quantity;
	private BigDecimal price;
	private Integer sellerid;
	private String status;
	
	public Integer getQuantity() {
		return quantity;
	}
	public Integer getSellerid() {
		return sellerid;
	}
	public void setSellerid(Integer sellerid) {
		this.sellerid = sellerid;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "Product [orderId=" + orderId + ", prodId=" + prodId + ", quantity=" + quantity + ", price=" + price
				+ ", sellerid=" + sellerid + ", status=" + status + "]";
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public Integer getProdId() {
		return prodId;
	}
	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}	
}
