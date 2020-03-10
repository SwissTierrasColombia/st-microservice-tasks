package com.ai.st.microservice.tasks.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tasks_metadata_properties", schema = "tasks")
public class MetadataPropertyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "key", nullable = false, length = 255)
	private String key;

	@Column(name = "value", nullable = false, length = 255)
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metadata_id", referencedColumnName = "id", nullable = false)
	private TaskMetadataEntity metadata;

	public MetadataPropertyEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public TaskMetadataEntity getMetadata() {
		return metadata;
	}

	public void setMetadata(TaskMetadataEntity metadata) {
		this.metadata = metadata;
	}

}
