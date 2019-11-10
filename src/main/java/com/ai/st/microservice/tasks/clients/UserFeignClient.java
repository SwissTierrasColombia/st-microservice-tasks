package com.ai.st.microservice.tasks.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ai.st.microservice.tasks.dto.UserDto;

@FeignClient(name = "st-microservice-administration")
public interface UserFeignClient {

	@GetMapping("/api/administration/users/{id}")
	public UserDto findById(@PathVariable Long id);

}
