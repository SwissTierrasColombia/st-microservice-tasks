package com.ai.st.microservice.tasks.services;

import com.ai.st.microservice.tasks.entities.TaskMemberEntity;

public interface ITaskMemberService {

	public void removeMemberById(Long memberId);

	public void removeMemberByCode(Long memberCode);

	public TaskMemberEntity addMemberToTask(TaskMemberEntity memberEntity);

}
