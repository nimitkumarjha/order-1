package com.project.OrderServiceMS.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
@Entity
@IdClass(ProductsOrderedEntity.class)
@Table(name="productsordered")
public class ProductsOrderedEntity implements Serializable{
	
	@Id
	@Column(name="ORDERID")
	private Integer orderId;
	
	@Id
	@Column(name="PRODID")
	private Integer prodId;
	
	@Column(name="SELLERID")
	private Integer sellerid;
	
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@Column(name="STATUS")
	private String status;

	@Column(name="PRICE")
    private BigDecimal price;
    
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

	public Integer getQuantity() {
		return quantity;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	

	public Integer getSellerid() {
		return sellerid;
	}

	public void setSellerid(Integer sellerid) {
		this.sellerid = sellerid;
	}

	@Override
	public String toString() {
		return "ProductsOrdered [orderId=" + orderId + ", prodId=" + prodId + ", sellerid=" + sellerid + ", quantity="
				+ quantity + ", status=" + status + "]";
	}

	

}
