package com.ai.st.microservice.tasks.models.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.models.repositories.TaskMemberRepository;

@Service
public class TaskMemberService implements ITaskMemberService {

    @Autowired
    private TaskMemberRepository taskMemberRepository;

    @Override
    @Transactional
    public void removeMemberById(Long memberId) {
        taskMemberRepository.deleteById(memberId);
    }

    @Override
    @Transactional
    public void removeMemberByCode(Long memberCode) {
        taskMemberRepository.deleteByMemberCode(memberCode);
    }

    @Override
    @Transactional
    public TaskMemberEntity addMemberToTask(TaskMemberEntity memberEntity) {
        return taskMemberRepository.save(memberEntity);
    }

}
