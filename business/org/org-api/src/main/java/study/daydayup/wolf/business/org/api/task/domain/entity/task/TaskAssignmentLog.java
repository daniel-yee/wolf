package study.daydayup.wolf.business.org.api.task.domain.entity.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.daydayup.wolf.framework.layer.api.Model;

import java.time.LocalDateTime;

/**
 * study.daydayup.wolf.business.org.api.task.domain.entity.task
 *
 * @author Wingle
 * @since 2020/3/18 11:15 下午
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentLog implements Model {
    private Long orgId;
    private Long taskId;
    private Long projectId;

    private Long assigner;
    private Long sourceOwner;
    private Long targetOwner;

    private Integer sourceVersion;
    private Integer targetVersion;

    private String memo;
    private String tags;

    private Integer deleteFlag;
    private Long editor;
    private LocalDateTime createdAt;
}
