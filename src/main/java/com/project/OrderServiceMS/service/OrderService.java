package com.project.OrderServiceMS.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.project.OrderServiceMS.DTO.OrderDetailsDTO;
import com.project.OrderServiceMS.DTO.ProductsOrderedDTO;
import com.project.OrderServiceMS.controller.OrderController;
import com.project.OrderServiceMS.entity.OrderDetailsEntity;
import com.project.OrderServiceMS.entity.ProductsOrderedEntity;
import com.project.OrderServiceMS.repository.OrderDetailsRepository;
import com.project.OrderServiceMS.repository.ProductsOrderedRepository;

@Service
public class OrderService {
	@Autowired
	private OrderDetailsRepository orderrepo;
	@Autowired
	private OrderDetailsEntity orderEntity;
	@Autowired
	private ProductsOrderedRepository orderProdsRepo;
	@Autowired
	private ProductsOrderedEntity productsOrdered;
	@Autowired
	public RestTemplate restTemplate;
	@Value("${userServiceUrl}")
	public String userServiceUrl;


		
		// This method is to view orders placed on products.(by buyerId)
		public ArrayList<OrderDetailsDTO> getAllOrders(Integer Buyerid){
			List<ProductsOrderedEntity> product = (List<ProductsOrderedEntity>) orderProdsRepo.findAll();
			ArrayList<OrderDetailsDTO> orders =new ArrayList<>();
			List<OrderDetailsEntity> ord= (List<OrderDetailsEntity>) orderrepo.findAll();
			for(OrderDetailsEntity ord1:ord) {
				if(ord1.getBuyerId().equals(Buyerid)) {
					ArrayList<ProductsOrderedDTO> orderedProducts =new ArrayList<>();
					for(ProductsOrderedEntity orderprod: product) {
						if(ord1.getOrderId().equals(orderprod.getOrderId())) {
							ProductsOrderedDTO prod =new ProductsOrderedDTO();
							prod.setOrderId(orderprod.getOrderId());
							prod.setPrice(orderprod.getPrice());
							prod.setProdId(orderprod.getProdId());
							prod.setQuantity(orderprod.getQuantity());
							prod.setSellerid(orderprod.getSellerid());
							prod.setStatus(orderprod.getStatus());
							orderedProducts.add(prod);
						}
						OrderDetailsDTO od= new OrderDetailsDTO();
						od.setAddress(ord1.getAddress());
						od.setAmount(ord1.getAmount());
						od.setBuyerId(ord1.getBuyerId()); 
						od.setDate(ord1.getDate());
						od.setOrderId(ord1.getOrderId());
						od.setStatus(ord1.getStatus());
						od.setOrderedProducts(orderedProducts);
						orders.add(od);
					}
				}
			}
			return orders;
		}
		
		public String deleteOrder(Integer orderId) {
			List<OrderDetailsEntity> ordersEntities=(List<OrderDetailsEntity>) orderrepo.findAll();
				// Deleting the order
			Integer sizeorder=ordersEntities.size();
				for(OrderDetailsEntity order: ordersEntities){
					if(order.getOrderId().equals(orderId)) {
						orderrepo.delete(order);					
					}
				}
				List<ProductsOrderedEntity> products=(List<ProductsOrderedEntity>) orderProdsRepo.findAll();
				Integer sizeproduct=products.size();
				// Deleting all the product ordered in that order
				for(ProductsOrderedEntity product:products) {
					if(product.getOrderId().equals(orderId)) {
							orderProdsRepo.delete(product);
					}}
				List<OrderDetailsEntity> orders=(List<OrderDetailsEntity>) orderrepo.findAll();
				List<ProductsOrderedEntity> prod=(List<ProductsOrderedEntity>) orderProdsRepo.findAll();
				
			if(sizeorder>orders.size()& sizeproduct>prod.size()){
				return "Order "+ orderId+ " is deleted successfully from the records";
			}else{
				return "Deletion cannot be done as the order with orderId "+orderId+ " does not exist";
			}
			
	}
	/*
	 * // To calculate the discount. public Integer[] CalculateDiscount(Integer
	 * buyerId) { String getrewardUrl=userServiceUrl+"rewardPoint/"+buyerId;
	 * 
	 * ResponseEntity<Integer> responseEntity =
	 * restTemplate.getForEntity(getrewardUrl, Integer.class);
	 * 
	 * Integer reward=responseEntity.getBody(); Integer discount=reward/4; return
	 * discount; }
	 * 
	 * public String placeOrder(OrderDetailsDTO order) {
	 * ArrayList<ProductsOrderedDTO> productsReceived=order.getOrderedProducts();
	 * double amount=0d; for(ProductsOrderedDTO product:productsReceived) {
	 * amount+=product.getPrice()*product.getQuantity(); } Integer discount=
	 * CalculateDiscount(order.getBuyerId());
	 * 
	 * String isPrivilegeUrl=userServiceUrl+"buyer/isPrivilege/"+order.getBuyerId();
	 * 
	 * ResponseEntity<Boolean> responseEntity1 =
	 * restTemplate.getForEntity(isPrivilegeUrl, Boolean.class);
	 * 
	 * Boolean isPrivileged=responseEntity1.getBody();
	 * 
	 * if(isPrivileged==true) { amount=amount-discount; }
	 * 
	 * order.setAmount(amount); order.setDate(new
	 * Date());order.setStatus("ORDER PLACED");
	 * 
	 * OrderDetailsEntity oe =new OrderDetailsEntity();
	 * oe.setAddress(order.getAddress()); oe.setAmount(amount);
	 * oe.setBuyerId(order.getBuyerId()); oe.setDate(order.getDate());
	 * oe.setOrderId(order.getOrderId()); oe.setStatus(order.getStatus());
	 * orderrepo.save(oe); //return "Order "+ orderId+
	 * " is deleted successfully from the records"; return isPrivilegeUrl +
	 * " is Added successfully in the records"; } }
	 */
	
		
		public Integer[] usingRewardPoints(Integer buyerId, int eligibleDiscount) {
	        String getrewardUrl=userServiceUrl+"rewardPoint/"+buyerId;
	        
	        ResponseEntity<Integer> responseEntity = restTemplate.getForEntity(getrewardUrl, Integer.class);
	       
	        Integer reward=responseEntity.getBody();
	        Integer discount=reward/4;
	        if(discount>eligibleDiscount) {
	            discount=eligibleDiscount;
	            reward=reward-eligibleDiscount*4;
	        }else {
	            reward=0;
	        }
	        Integer [] valuesArray=new Integer[2];
	        valuesArray[0]=discount;valuesArray[1]=reward;
	        return valuesArray;
		}
		
		
		public String updateStatus(Integer orderId,Integer prodId,String status) {
			Boolean flag=false;
			try {
			List<ProductsOrderedEntity> products=(List<ProductsOrderedEntity>) orderProdsRepo.findAll();
			for(int i=0;i<products.size();i++){
				ProductsOrderedEntity product=products.get(i);
				// Checking the orderId and ProdId before updating
				if(product.getOrderId().equals(orderId) && product.getProdId().equals(prodId)) {
					BeanUtils.copyProperties(product,productsOrdered);
					orderProdsRepo.delete(product);
					productsOrdered.setStatus(status);
					orderProdsRepo.save(productsOrdered);
					flag=true;
				}
			}
			}catch(Exception e){
				e.printStackTrace();
				return "not able to update contact";
			}
			if(flag){
				return "Order status updated successfully";
			}else{
				return "some error occured.Check for issues";
			}
		}
			
		
	/*
	 * // This method inserts an order into the orderdetails table and also each and
	 * every record of products that are ordered into productsordered table. public
	 * void placeOrder(OrderDetailsDTO order) {
	 * ArrayList<ProductsOrderedDTO>productsReceived=(ArrayList<ProductsOrderedDTO>)
	 * order.getOrderedProducts();
	 * 
	 * BigDecimal amount=new BigDecimal(0); for (int
	 * j=0;j<productsReceived.size();j++) { ProductsOrderedDTO
	 * product=productsReceived.get(j);
	 * amount=amount.add(product.getPrice().multiply(new
	 * BigDecimal(product.getQuantity()))); } Integer eligibleDiscount=new
	 * Integer(amount.multiply(new BigDecimal(0.1)).intValue()); // invoking
	 * usingRewardPoints method to get the discount Integer [] Integer []
	 * valuesArray=this.usingRewardPoints(order.getBuyerId(),,eligibleDiscount);
	 * BigDecimal discount=new BigDecimal(valuesArray[0]);
	 * 
	 * // Checking user is Priviledged or not String String
	 * isPrivilegeUrl=userServiceUrl+"buyer/isPrivilege/"+order.getBuyerId();
	 * 
	 * ResponseEntity<Boolean> responseEntity1 =
	 * restTemplate.getForEntity(isPrivilegeUrl, Boolean.class);
	 * 
	 * Boolean isPrivileged=responseEntity1.getBody();
	 * 
	 * // Adding all the individual products into the db Integer
	 * orderId=orderEntity.getOrderId(); productsReceived.forEach((ProductsOrdered
	 * prod)->{ prod.setOrderId(orderId); prod.setStatus("ORDER PLACED");
	 * //BeanUtils.copyProperties(prod, productsOrdered);
	 * orderProdsRepo.save(productsOrdered); });
	 * 
	 * // Calculating and Updating the reward points in the user service Integer
	 * newRewardPoints = new Integer(amount.intValue()/100)+valuesArray[1]; // 100
	 * ruppees equals 1 point String updateRewardPointsUrl = userServiceUrl
	 * +"rewardPoint/update/"+order.getBuyerId()+"/"+newRewardPoints;
	 * restTemplate.put(updateRewardPointsUrl,newRewardPoints,Integer.class); }
	 * 
	 * public String reOrder(OrderBean order) { try { ArrayList<Product>
	 * orderedProducts=new ArrayList<Product>(); Integer orderId=order.getOrderId();
	 * 
	 * // Finding all the products for that particular Order
	 * orderProdsRepo.findAll().forEach((product)->{
	 * if(product.getOrderId().equals(orderId)) {
	 * 
	 * Product prodBean=new Product(); BeanUtils.copyProperties(product, prodBean);
	 * 
	 * orderedProducts.add(prodBean);
	 * 
	 * } });
	 */
	 
//		This method inserts an order into the orderdetails table and also each and every record of products that are ordered into productsordered table.
		public void placeOrder(OrderDetailsDTO  order) {
			ArrayList<ProductsOrderedDTO> productsReceived=(ArrayList<ProductsOrderedDTO>) order.getOrderedProducts();
			
			BigDecimal amount=new BigDecimal(0);
			for (int j=0;j<productsReceived.size();j++) {
				ProductsOrderedDTO product=productsReceived.get(j);
				amount=amount.add(product.getPrice().multiply(new BigDecimal(product.getQuantity())));
			}
			Integer eligibleDiscount=new Integer(amount.multiply(new BigDecimal(0.1)).intValue());
			
	        // invoking usingRewardPoints method to get the discount
	        Integer [] valuesArray=this.usingRewardPoints(order.getBuyerId(),eligibleDiscount);
	        BigDecimal discount=new BigDecimal(valuesArray[0]);

			// Checking user is Priviledged or not
			String isPrivilegeUrl=userServiceUrl+"buyer/isPrivilege/"+order.getBuyerId();
		
			ResponseEntity<Boolean> responseEntity1 = restTemplate.getForEntity(isPrivilegeUrl, Boolean.class);
			
			Boolean isPrivileged=responseEntity1.getBody();
			
			
			// Based on isPrivileged, finding the shipping cost
			BigDecimal shippingCost=new BigDecimal(50);
			if(isPrivileged.equals(true)) {
				shippingCost=new BigDecimal(0);
			}
			amount=amount.subtract(discount);
			amount=amount.add(shippingCost);
			order.setAmount(amount);
			order.setDate(new Date());
			order.setStatus("ORDER PLACED");
			
			BeanUtils.copyProperties(order, orderEntity);
			orderrepo.save(orderEntity);
			
			// Adding all the individual products into the db				
			Integer orderId=orderEntity.getOrderId();
			productsReceived.forEach((ProductsOrderedDTO prod)->{
				prod.setOrderId(orderId);
				prod.setStatus("ORDER PLACED");
				BeanUtils.copyProperties(prod, productsOrdered);
				orderProdsRepo.save(productsOrdered);
			});
			
			// Calculating and Updating the reward points in the user service
			Integer newRewardPoints = new Integer(amount.intValue()/100)+valuesArray[1]; // 100 ruppees equals 1 point
			String updateRewardPointsUrl = userServiceUrl +"rewardPoint/update/"+order.getBuyerId()+"/"+newRewardPoints;
			restTemplate.put(updateRewardPointsUrl,newRewardPoints,Integer.class);
		}
	
//	  public String updateStatus(Integer orderId,Integer prodId,String status) {
//	  Boolean flag=false; 
//	  try {
//	  List<ProductsOrderedEntity> products=(List<ProductsOrderedEntity>) orderProdsRepo.findAll();
//	 for(int i=0;i<products.size();i++){ 
//	 ProductsOrderedEntity product=products.get(i); //Checking the orderId and ProdId before updating
//	  if(product.getOrderId().equals(orderId) && product.getProdId().equals(prodId)) {
//	  BeanUtils.copyProperties(product,productsOrdered);
//	  orderProdsRepo.delete(product); productsOrdered.setStatus(status);
//	  orderProdsRepo.save(productsOrdered); 
//	  flag=true; } } 
//	 }catch(Exception e){
//	  e.printStackTrace(); 
//	  return "Error in updating the order! Contact your Admin"; } 
//	  if(flag){
//	return  "Order status updated successfully"; }
//	  else{ 
//	return "Updation is not successful. Check for issues"; } }
	  
	 
	  }
	 
