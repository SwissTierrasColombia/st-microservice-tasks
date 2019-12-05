package com.ai.st.microservice.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ai.st.microservice.tasks.entities.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

	@Override
	List<TaskEntity> findAll();

	@Query("SELECT t FROM TaskEntity t JOIN TaskMemberEntity tm ON t.id = tm.task.id AND tm.memberCode = :memberCode WHERE t.taskState.id = :taskStateId")
	List<TaskEntity> getTasksByMemberAndState(@Param("memberCode") Long memberCode,
			@Param("taskStateId") Long taskStateId);

	@Query("SELECT t FROM TaskEntity t JOIN TaskMemberEntity tm ON t.id = tm.task.id AND tm.memberCode = :memberCode")
	List<TaskEntity> getTasksByMember(@Param("memberCode") Long memberCode);
	
	@Query("SELECT t FROM TaskEntity t WHERE t.taskState.id = :taskStateId")
	List<TaskEntity> getTasksByState(@Param("taskStateId") Long taskStateId);

}
