package com.itwork.farm_plant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itwork.farm_plant.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 作业任务Mapper接口
 */
public interface TaskMapper extends BaseMapper<Task> {
    /**
     * 删除任务所有关联的雇员关系
     * @param taskId 任务ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM plant_task_employee WHERE task_id = #{taskId}")
    int deleteAllTaskEmployees(@Param("taskId") Long taskId);

    /**
     * 根据任务ID查询参与的雇员ID列表
     */
    @Select("SELECT employee_id FROM plant_task_employee WHERE task_id = #{taskId}")
    List<Long> getEmployeeIdsByTaskId(@Param("taskId") Long taskId);

    /**
     * 为任务添加雇员
     */
    @Insert("INSERT INTO plant_task_employee " +
            "(task_id, employee_id, create_by, create_time, update_by, update_time) VALUES " +
            "(#{taskId}, #{employeeId}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime})")
    int insertTaskEmployees(@Param("taskId") Long taskId,
                            @Param("employeeId") Long employeeId,
                            @Param("createBy") String createBy,
                            @Param("createTime") LocalDateTime createTime,
                            @Param("updateBy") String updateBy,
                            @Param("updateTime") LocalDateTime updateTime);

    /**
     * 删除任务的指定雇员
     */
    @Delete("DELETE FROM plant_task_employee WHERE task_id = #{taskId} AND employee_id = #{employeeId}")
    int deleteTaskEmployee(@Param("taskId") Long taskId, @Param("employeeId") Long employeeId);
}

