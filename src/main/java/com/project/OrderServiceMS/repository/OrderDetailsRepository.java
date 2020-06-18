package com.project.OrderServiceMS.repository;

import org.springframework.data.repository.CrudRepository;

import com.project.OrderServiceMS.entity.OrderDetailsEntity;

public interface OrderDetailsRepository extends CrudRepository<OrderDetailsEntity, Integer>{

	static OrderDetailsEntity getOne(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

}
