package study.daydayup.wolf.business.org.biz.task.domain.repository;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import study.daydayup.wolf.business.org.api.task.domain.entity.Task;
import study.daydayup.wolf.business.org.api.task.dto.TaskId;
import study.daydayup.wolf.business.org.api.task.dto.TaskOption;
import study.daydayup.wolf.business.org.biz.task.converter.TaskConverter;
import study.daydayup.wolf.business.org.biz.task.dal.dao.TaskDAO;
import study.daydayup.wolf.business.org.biz.task.dal.dataobject.TaskDO;
import study.daydayup.wolf.business.org.biz.task.domain.entity.TaskEntity;
import study.daydayup.wolf.business.org.biz.task.domain.repository.task.*;
import study.daydayup.wolf.framework.layer.domain.Repository;

import javax.annotation.Resource;

/**
 * study.daydayup.wolf.business.org.biz.task.domain.repository
 *
 * @author Wingle
 * @since 2020/3/15 11:54 下午
 **/
@Component
public class TaskRepository implements Repository {
    @Resource
    protected TaskTradeRepository tradeRepository;
    @Resource
    protected TaskContactRepository contactRepository;
    @Resource
    protected TaskDetailRepository detailRepository;
    @Resource
    protected TaskStateLogRepository stateLogRepository;
    @Resource
    protected TaskAssignmentLogRepository assignmentLogRepository;

    @Resource
    protected TaskDAO taskDAO;

    public TaskEntity find(Long taskId, Long orgId) {
        return find(taskId, orgId, TaskOption.DEFAULT);
    }

    public TaskEntity find(@NonNull Long id, @NonNull Long orgId, @NonNull TaskOption option) {
        TaskId taskId = TaskId.builder()
                .taskId(id)
                .orgId(orgId)
                .option(option)
                .build();

        return find(taskId);
    }

    public TaskEntity find(@NonNull TaskId taskId) {
        taskId.valid();

        TaskDO taskDO = selectTask(taskId.getTaskId(), taskId.getOrgId());
        return findDetailsByTaskDo(taskDO, taskId);
    }

    public int add(TaskEntity entity) {
        Long taskId = insertTask(entity);
        if (0 == taskId) {
            return 0;
        }

        Task task = entity.getModel();
        detailRepository.add(task, taskId);
        contactRepository.add(task.getContact(), taskId);
        tradeRepository.add(task.getTrade(), taskId);

        return 1;
    }

    public int save(TaskEntity entity) {
        int status = updateTask(entity);
        if (status == 0) {
            return 0;
        }

        Task key = entity.getKey();
        Task changes = entity.getChanges();
        detailRepository.save(key, changes);
        contactRepository.save(key.getContact(), changes.getContact());
        tradeRepository.save(key.getTrade(), changes.getTrade());
        stateLogRepository.add(changes.getStateLog());
        assignmentLogRepository.add(changes.getAssignmentLog());

        return status;
    }

    private TaskDO selectTask(@NonNull Long taskId, @NonNull Long orgId) {
        return taskDAO.selectById(taskId, orgId);
    }

    private Long insertTask(TaskEntity entity) {
        if (entity == null || null == entity.getModel()) {
            return 0L;
        }

        TaskDO taskDO = TaskConverter.toDo(entity.getModel());
        if (taskDO == null) {
            return 0L;
        }

        int status = taskDAO.insertSelective(taskDO);
        if (status > 0) {
            return taskDO.getId();
        }
        return 0L;
    }

    private int updateTask(TaskEntity entity) {
        Task key = entity.getKey();
        Task changes = entity.getChanges();
        if (null == key || null == changes) {
            return 0;
        }

        TaskDO keyDO = TaskConverter.toDo(key);
        TaskDO changesDO = TaskConverter.toDo(changes);
        if (null == keyDO || null == changesDO) {
            return 0;
        }

        return taskDAO.updateByKey(changesDO, keyDO);
    }

    private TaskEntity findDetailsByTaskDo(TaskDO taskDO, TaskId taskId) {
        taskId.valid();
        if (taskDO == null) {
            return null;
        }

        TaskOption option = taskId.getOption();
        Task task = TaskConverter.toModel(taskDO);

        if (option.isWithContact()) {
            task.setContact(contactRepository.find(taskId));
        }
        if (option.isWithTrade()) {
            task.setTrade(tradeRepository.find(taskId));
        }
        if (option.isWithDetail()) {
            detailRepository.find(taskId, task);
        }

        return new TaskEntity(task, false);
    }
}
