package com.yitai.constant;

/**
 * ClassName: SchedulerConstant
 * Package: com.yitai.constant
 * Description:
 *
 * @Author: mao
 * @Create 2023/12/1 18:34
 * @Version 1.0
 */
public class SchedulerConstant {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 默认 */
    public static final String MISFIRE_DEFAULT = "0";

    /** 立即触发执行 */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** 触发一次执行 */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** 不触发立即执行 */
    public static final String MISFIRE_DO_NOTHING = "3";
    /** 任务执行状态 */
    public static final Integer FAIL = -1;

    public static final Integer SUCCESS = 1;
}
