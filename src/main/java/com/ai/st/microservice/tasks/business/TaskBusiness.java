package com.ai.st.microservice.tasks.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ai.st.microservice.tasks.services.tracing.SCMTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.tasks.dto.CreateTaskMetadataDto;
import com.ai.st.microservice.tasks.dto.CreateTaskPropertyDto;
import com.ai.st.microservice.tasks.dto.CreateTaskStepDto;
import com.ai.st.microservice.tasks.dto.TaskCategoryDto;
import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.dto.TaskMemberDto;
import com.ai.st.microservice.tasks.dto.TaskMetadataDto;
import com.ai.st.microservice.tasks.dto.TaskMetadataPropertyDto;
import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.dto.TaskStepDto;
import com.ai.st.microservice.tasks.dto.TaskTypeStepDto;
import com.ai.st.microservice.tasks.entities.MetadataPropertyEntity;
import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.entities.TaskMetadataEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.entities.TaskStepEntity;
import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.services.ITaskMemberService;
import com.ai.st.microservice.tasks.services.ITaskService;
import com.ai.st.microservice.tasks.services.TaskCategoryService;
import com.ai.st.microservice.tasks.services.TaskTypeStepService;

@Component
public class TaskBusiness {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITaskService taskService;

    @Autowired
    private TaskStateBusiness taskStateBusiness;

    @Autowired
    private TaskCategoryService taskCategoryService;

    @Autowired
    private TaskTypeStepService taskTypeStepService;

    @Autowired
    private ITaskMemberService taskMemberService;

    public List<TaskDto> getAllTasks() {

        List<TaskDto> listTasksDto = new ArrayList<>();

        List<TaskEntity> listTasksEntity = taskService.getAllTasks();
        for (TaskEntity taskEntity : listTasksEntity) {
            TaskDto taskDto = entityParseDto(taskEntity);
            listTasksDto.add(taskDto);
        }

        return listTasksDto;
    }

    public TaskDto createTask(String name, String description, List<Long> users, Date deadline, List<Long> categories,
            List<CreateTaskMetadataDto> metadata, List<CreateTaskStepDto> steps) throws BusinessException {

        TaskDto taskDto;

        Date currentDate = new Date();

        // validate if the deadline is greater than the current date
        if (deadline != null) {
            if (!deadline.after(currentDate)) {
                throw new BusinessException("La fecha límite debe ser mayor a la fecha actual");
            }
        }

        // set task state
        TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_ASSIGNED);
        if (taskStateDto == null) {
            throw new BusinessException("No se ha encontrado el estado de la tarea.");
        }

        List<Long> listUsers = users.stream().distinct().collect(Collectors.toList());
        List<Long> listCategories = categories.stream().distinct().collect(Collectors.toList());

        // validate categories
        for (Long categoryId : listCategories) {
            TaskCategoryEntity categoryEntity = taskCategoryService.getCategoryById(categoryId);
            if (categoryEntity == null) {
                throw new BusinessException("No se ha encontrado la cagegoría.");
            }
        }

        // validate metadata
        if (metadata.size() > 0) {
            for (CreateTaskMetadataDto meta : metadata) {
                if (meta.getKey().isEmpty() || meta.getKey() == null) {
                    throw new BusinessException("Metadato inválido.");
                }
                if (meta.getProperties().size() == 0) {
                    throw new BusinessException("El metadato debe tener al menos una propiedad.");
                }
            }
        }

        // validate steps
        if (steps.size() > 0) {
            for (CreateTaskStepDto stepDto : steps) {
                if (stepDto.getTitle().isEmpty() || stepDto.getTitle() == null) {
                    throw new BusinessException("El título del paso es requerido.");
                }
                if (stepDto.getDescription().isEmpty() || stepDto.getDescription() == null) {
                    throw new BusinessException("La descripción del paso es requerido.");
                }
                if (stepDto.getTypeStepId() == null) {
                    throw new BusinessException("La tipo de paso es requerido.");
                }
                TaskTypeStepEntity typeStepEntity = taskTypeStepService.getTypeStepById(stepDto.getTypeStepId());
                if (typeStepEntity == null) {
                    throw new BusinessException("La tipo de paso no existe.");
                }
            }
        }

        try {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setName(name);
            taskEntity.setDescription(description);
            taskEntity.setDeadline(deadline);
            taskEntity.setCreatedAt(currentDate);

            TaskStateEntity taskStateEntity = new TaskStateEntity();
            taskStateEntity.setId(taskStateDto.getId());
            taskEntity.setTaskState(taskStateEntity);

            // set categories
            List<TaskCategoryEntity> categoriesEntity = new ArrayList<>();
            for (Long categoryId : listCategories) {
                TaskCategoryEntity category = new TaskCategoryEntity();
                category.setId(categoryId);
                categoriesEntity.add(category);
            }
            taskEntity.setCategories(categoriesEntity);

            // set metadata
            if (metadata.size() > 0) {
                List<TaskMetadataEntity> listMetadataEntity = new ArrayList<>();
                for (CreateTaskMetadataDto meta : metadata) {
                    TaskMetadataEntity metadataEntity = new TaskMetadataEntity();
                    metadataEntity.setKey(meta.getKey());
                    metadataEntity.setTask(taskEntity);

                    // set properties
                    List<MetadataPropertyEntity> propertiesEntity = new ArrayList<>();
                    for (CreateTaskPropertyDto propertyDto : meta.getProperties()) {
                        MetadataPropertyEntity propertyEntity = new MetadataPropertyEntity();
                        propertyEntity.setKey(propertyDto.getKey());
                        propertyEntity.setValue(propertyDto.getValue());
                        propertyEntity.setMetadata(metadataEntity);
                        propertiesEntity.add(propertyEntity);
                    }

                    metadataEntity.setProperties(propertiesEntity);

                    listMetadataEntity.add(metadataEntity);
                }
                taskEntity.setMetadata(listMetadataEntity);
            }

            if (steps.size() > 0) {

                List<TaskStepEntity> listStepsEntity = new ArrayList<>();
                for (CreateTaskStepDto stepDto : steps) {

                    TaskStepEntity stepEntity = new TaskStepEntity();
                    stepEntity.setCode("N/A");
                    stepEntity.setDescription(stepDto.getDescription());
                    stepEntity.setStatus(false);
                    stepEntity.setTask(taskEntity);
                    TaskTypeStepEntity typeStepEntity = taskTypeStepService.getTypeStepById(stepDto.getTypeStepId());
                    stepEntity.setTypeStep(typeStepEntity);

                    listStepsEntity.add(stepEntity);
                }

                taskEntity.setSteps(listStepsEntity);
            }

            TaskEntity newTaskEntity = taskService.createTask(taskEntity);

            for (Long userCode : listUsers) {
                this.addMemberToTask(newTaskEntity, userCode);
            }

            taskDto = this.getTaskById(newTaskEntity.getId());

        } catch (Exception e) {
            String messageError = String.format("Error creando tarea: %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido crear la tarea");
        }

        return taskDto;
    }

    public TaskDto getTaskById(Long id) {

        TaskDto taskDto = null;

        TaskEntity taskEntity = taskService.getById(id);

        if (taskEntity != null) {
            taskDto = entityParseDto(taskEntity);
        }

        return taskDto;
    }

    public List<TaskDto> getTasksByFilters(Long memberCode, List<Long> taskStates, List<Long> taskCategories) {

        List<TaskDto> listTasksDto = new ArrayList<>();

        List<TaskEntity> listTasksEntity;

        if (memberCode != null && taskStates != null && taskStates.size() > 0 && taskCategories != null
                && taskCategories.size() > 0) {

            // all filters
            listTasksEntity = taskService.getTasksByStatesAndMemberAndCategories(taskStates, taskCategories,
                    memberCode);

        } else if (memberCode != null && taskStates != null && taskStates.size() > 0) {

            // filter by states and member
            listTasksEntity = taskService.getTasksByStatesAndMember(taskStates, memberCode);

        } else if (memberCode != null && taskCategories != null && taskCategories.size() > 0) {

            // filter by categories and member
            listTasksEntity = taskService.getTasksByMemberAndCategories(taskCategories, memberCode);

        } else if (taskCategories != null && taskCategories.size() > 0 && taskStates != null && taskStates.size() > 0) {

            // filter by categories and state
            listTasksEntity = taskService.getTasksByStatesAndCategories(taskCategories, taskStates);

        } else if (memberCode != null) {

            // filter by member
            listTasksEntity = taskService.getTasksByMember(memberCode);

        } else if (taskStates != null && taskStates.size() > 0) {

            // filter by states
            listTasksEntity = taskService.getTasksByStates(taskStates);

        } else if (taskCategories != null && taskCategories.size() > 0) {

            // filter by categories
            listTasksEntity = taskService.getTasksByCategories(taskCategories);

        } else {

            // no filters
            listTasksEntity = taskService.getAllTasks();
        }

        for (TaskEntity taskEntity : listTasksEntity) {
            TaskDto taskDto = entityParseDto(taskEntity);
            listTasksDto.add(taskDto);
        }

        return listTasksDto;
    }

    public TaskDto closeTask(Long taskId) throws BusinessException {

        TaskDto taskDto;

        // verify task exists
        TaskEntity taskEntity = taskService.getById(taskId);
        if (taskEntity == null) {
            throw new BusinessException("No se ha encontrado la tarea");
        }

        Date currentDate = new Date();
        taskEntity.setClosingDate(currentDate);

        // set task state
        TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_CLOSED);
        if (taskStateDto == null) {
            throw new BusinessException("No se ha encontrado el estado de tarea");
        }
        TaskStateEntity taskStateEntity = new TaskStateEntity();
        taskStateEntity.setId(taskStateDto.getId());
        taskEntity.setTaskState(taskStateEntity);

        try {

            TaskEntity taskUpdatedEntity = taskService.updateTask(taskEntity);
            taskDto = getTaskById(taskUpdatedEntity.getId());

        } catch (Exception e) {
            String messageError = String.format("Error cerrando la tarea %d: %s", taskId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido cerrar la tarea.");
        }

        return taskDto;
    }

    public TaskDto startTask(Long taskId) throws BusinessException {

        TaskDto taskDto;

        // verify task exists
        TaskEntity taskEntity = taskService.getById(taskId);
        if (taskEntity == null) {
            throw new BusinessException("No se ha encontrado la tarea");
        }

        // set task state
        TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_STARTED);
        if (taskStateDto == null) {
            throw new BusinessException("No se ha encontrado el estado de tarea");
        }
        TaskStateEntity taskStateEntity = new TaskStateEntity();
        taskStateEntity.setId(taskStateDto.getId());
        taskEntity.setTaskState(taskStateEntity);

        try {

            TaskEntity taskUpdatedEntity = taskService.updateTask(taskEntity);
            taskDto = this.getTaskById(taskUpdatedEntity.getId());

        } catch (Exception e) {
            String messageError = String.format("Error iniciando la tarea %d: %s", taskId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido iniciar la tarea.");
        }

        return taskDto;
    }

    public TaskDto cancelTask(Long taskId, String reason) throws BusinessException {

        TaskDto taskDto;

        // verify task exists
        TaskEntity taskEntity = taskService.getById(taskId);
        if (taskEntity == null) {
            throw new BusinessException("No se ha encontrado la tarea");
        }

        // set task state
        TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_CANCELLED);
        if (taskStateDto == null) {
            throw new BusinessException("No se ha encontrado el estado de tarea");
        }
        TaskStateEntity taskStateEntity = new TaskStateEntity();
        taskStateEntity.setId(taskStateDto.getId());
        taskEntity.setTaskState(taskStateEntity);
        taskEntity.setReason(reason);

        try {

            TaskEntity taskUpdatedEntity = taskService.updateTask(taskEntity);
            taskDto = getTaskById(taskUpdatedEntity.getId());

        } catch (Exception e) {
            String messageError = String.format("Error cancelando la tarea %d: %s", taskId, e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido cancelar la tarea.");
        }

        return taskDto;
    }

    public TaskDto removeUserFromTask(Long taskId, Long memberCode) throws BusinessException {

        TaskDto taskDto;

        // verify task exists
        TaskEntity taskEntity = taskService.getById(taskId);
        if (taskEntity == null) {
            throw new BusinessException("No se ha encontrado la tarea");
        }

        // find member
        List<TaskMemberEntity> taskMembers = taskEntity.getMembers();
        TaskMemberEntity memberFound = taskMembers.stream()
                .filter(memberEntity -> memberEntity.getMemberCode().equals(memberCode)).findAny().orElse(null);
        if (memberFound == null) {
            throw new BusinessException("No se ha encontrado el miembro");
        }

        try {

            taskMemberService.removeMemberById(memberFound.getId());
            taskDto = this.getTaskById(taskId);

        } catch (Exception e) {
            String messageError = String.format("Error quitando el usuario %d de la tarea %d: %s", memberCode, taskId,
                    e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido quitar el usuario de la tarea.");
        }

        return taskDto;
    }

    public void addMemberToTask(TaskEntity taskEntity, Long memberCode) throws BusinessException {

        // find member
        List<TaskMemberEntity> taskMembers = taskEntity.getMembers();
        TaskMemberEntity memberFound = taskMembers.stream()
                .filter(memberEntity -> memberEntity.getMemberCode().equals(memberCode)).findAny().orElse(null);
        if (memberFound != null) {
            throw new BusinessException("El miembro ya se encuentra registrado en la tarea");
        }

        try {

            TaskMemberEntity taskMemberEntity = new TaskMemberEntity();
            taskMemberEntity.setMemberCode(memberCode);
            taskMemberEntity.setCreatedAt(new Date());
            taskMemberEntity.setTask(taskEntity);

            taskMemberService.addMemberToTask(taskMemberEntity);

        } catch (Exception e) {
            String messageError = String.format("Error agregando el usuario %d de la tarea %d: %s", memberCode,
                    taskEntity.getId(), e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new BusinessException("No se ha podido agregar el usuario a la tarea.");
        }

    }

    public TaskDto entityParseDto(TaskEntity taskEntity) {

        TaskDto taskDto = null;

        if (taskEntity != null) {
            taskDto = new TaskDto();

            taskDto.setId(taskEntity.getId());
            taskDto.setName(taskEntity.getName());
            taskDto.setDescription(taskEntity.getDescription());
            taskDto.setCreatedAt(taskEntity.getCreatedAt());
            taskDto.setDeadline(taskEntity.getDeadline());
            taskDto.setClosingDate(taskEntity.getClosingDate());
            taskDto.setReason(taskEntity.getReason());

            // set state
            TaskStateEntity taskStateEntity = taskEntity.getTaskState();
            if (taskStateEntity.getName() == null) {
                taskDto.setTaskState(taskStateBusiness.getById(taskStateEntity.getId()));
            } else {
                taskDto.setTaskState(new TaskStateDto(taskStateEntity.getId(), taskStateEntity.getName()));
            }

            // set members
            for (TaskMemberEntity taskMemberEntity : taskEntity.getMembers()) {
                TaskMemberDto member = new TaskMemberDto();
                member.setId(taskMemberEntity.getId());
                member.setMemberCode(taskMemberEntity.getMemberCode());
                member.setCreatedAt(taskMemberEntity.getCreatedAt());
                taskDto.getMembers().add(member);
            }

            // set categories
            for (TaskCategoryEntity categoryEntity : taskEntity.getCategories()) {
                TaskCategoryDto categoryDto = new TaskCategoryDto();
                categoryDto.setId(categoryEntity.getId());
                categoryDto.setName(categoryEntity.getName());
                taskDto.getCategories().add(categoryDto);
            }

            // set metadata
            for (TaskMetadataEntity metadata : taskEntity.getMetadata()) {
                TaskMetadataDto metadataDto = new TaskMetadataDto();
                metadataDto.setId(metadata.getId());
                metadataDto.setKey(metadata.getKey());

                for (MetadataPropertyEntity propertyEntity : metadata.getProperties()) {

                    metadataDto.getProperties().add(new TaskMetadataPropertyDto(propertyEntity.getId(),
                            propertyEntity.getKey(), propertyEntity.getValue()));
                }

                taskDto.getMetadata().add(metadataDto);
            }

            // set steps
            for (TaskStepEntity stepEntity : taskEntity.getSteps()) {
                TaskStepDto stepDto = new TaskStepDto();
                stepDto.setCode(stepEntity.getCode());
                stepDto.setDescription(stepEntity.getDescription());
                stepDto.setId(stepEntity.getId());
                stepDto.setStatus(stepEntity.getStatus());
                stepDto.setTypeStep(
                        new TaskTypeStepDto(stepEntity.getTypeStep().getId(), stepEntity.getTypeStep().getName()));
                taskDto.getSteps().add(stepDto);
            }

        }

        return taskDto;
    }

}
