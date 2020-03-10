package com.ai.st.microservice.tasks.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.tasks.entities.TaskMemberEntity;

public interface TaskMemberRepository extends CrudRepository<TaskMemberEntity, Long> {
	
	Long deleteByMemberCode(Long memberCode);
	

}
