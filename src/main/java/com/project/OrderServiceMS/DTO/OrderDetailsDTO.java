package com.project.OrderServiceMS.DTO;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.OrderServiceMS.entity.OrderDetailsEntity;



public class OrderDetailsDTO {
	private Integer orderId;
	private Integer buyerId;
	private BigDecimal amount;
	private Date date;
	private String address;
	private String status;
	private ArrayList<ProductsOrderedDTO> orderedProducts;
	
	public ArrayList<ProductsOrderedDTO> getOrderedProducts() {
		return orderedProducts;
	}
	public void setOrderedProducts(ArrayList<ProductsOrderedDTO> orderedProducts) {
		this.orderedProducts = orderedProducts;
	}
	public OrderDetailsDTO() {
		
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal bigDecimal) {
		this.amount = bigDecimal;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "OrderBean [orderId=" + orderId + ", buyerId=" + buyerId + ", amount=" + amount + ", date=" + date
				+ ", address=" + address + ", status=" + status + ", orderedProducts=" + orderedProducts + "]";
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	// Converts Entity into DTO
		public static OrderDetailsDTO valueOf(OrderDetailsEntity order) {
			OrderDetailsDTO OrderDetailsDTO = new OrderDetailsDTO();
			OrderDetailsDTO.setAddress(order.getAddress());
			OrderDetailsDTO.setAmount(order.getAmount());
			OrderDetailsDTO.setBuyerId(order.getBuyerId());
			OrderDetailsDTO.setOrderId(order.getBuyerId());
			OrderDetailsDTO.setDate(order.getDate());
			OrderDetailsDTO.setStatus(order.getStatus());

			//custDTO.setCurrentPlan(planDTO);
			
			return OrderDetailsDTO;
		}

		// Converts DTO into Entity
		public OrderDetailsEntity createEntity() {
			OrderDetailsEntity order = new OrderDetailsEntity();
			order.setAddress(this.getAddress());
			order.setAmount(this.getAmount());
			order.setBuyerId(this.getBuyerId());
			order.setOrderId(this.getBuyerId());
			order.setDate(this.getDate());
			order.setStatus(this.getStatus());
			//cust.setPlanId(this.getCurrentPlan().planId);
			return order;
		}
		
	
	
}
