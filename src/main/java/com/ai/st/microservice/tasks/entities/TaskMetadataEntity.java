package com.ai.st.microservice.tasks.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tasks_metadata", schema = "tasks")
public class TaskMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "key", nullable = false, length = 255)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    private TaskEntity task;

    @OneToMany(mappedBy = "metadata", cascade = CascadeType.ALL)
    private List<MetadataPropertyEntity> properties = new ArrayList<MetadataPropertyEntity>();

    public TaskMetadataEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MetadataPropertyEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<MetadataPropertyEntity> properties) {
        this.properties = properties;
    }

}
