package com.yitai.context;

import com.yitai.entity.User;

/**
 * ClassName: BaseContext
 * Package: com.yitai.context
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/9/25 16:54
 * @Version: 1.0
 */
public class BaseContext {
    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user){
        threadLocal.set(user);
    }

    public static User getCurrentUser(){
        return threadLocal.get();
    }

//    public static void removeCurrentId(){
//        threadLocal.remove();
//    }

    public static void removeCurrentUser(){
        threadLocal.remove();
    }
}
