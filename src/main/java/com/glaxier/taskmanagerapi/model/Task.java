package com.glaxier.taskmanagerapi.model;

import com.glaxier.taskmanagerapi.model.pojo.UpdateTask;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Document
@Getter
@Setter
@ToString
public class Task {

    @Id
    private String id;

    @NotBlank(message = "Description required")
    private String description;
    private Boolean completed = false;

    private String userId;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public void updateTask(UpdateTask updateTask) {
        this.description = updateTask.getDescription() == null ? this.description : updateTask.getDescription();
        this.completed = updateTask.getCompleted() == null ? this.completed : updateTask.getCompleted();
    }
}
