package com.ai.st.microservice.tasks.models.services;

import com.ai.st.microservice.tasks.entities.TaskMemberEntity;

public interface ITaskMemberService {

    void removeMemberById(Long memberId);

    void removeMemberByCode(Long memberCode);

    TaskMemberEntity addMemberToTask(TaskMemberEntity memberEntity);

}
