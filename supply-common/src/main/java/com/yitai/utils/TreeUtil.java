package com.yitai.utils;

import com.yitai.exception.ServiceException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * ClassName: TreeUtils
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 15:07
 * @Version: 1.0
 */
public class TreeUtil {
    public static <T> ArrayList<T> buildTree(List<T> list, Function<T, ?> getParentId){
        ArrayList<T> trees = new ArrayList<>();
        ArrayList<T> parents = new ArrayList<>();
        //寻找顶级父部门
        for (T item : list) {
            T parent = buildParent(item,list,getParentId);
            if(!parents.contains(parent)){
                parents.add(parent);
            }
        }
        //构建部门树
        for (T parent: parents){
            T tree = buildTrees(parent, list, getParentId);
            trees.add(tree);
        }
        return trees;
    }

    public static <T> ArrayList<T> buildTree(List<T> list, int state, Function<T, ?> getParentId){
        ArrayList<T> trees = new ArrayList<>();
        ArrayList<T> parents = new ArrayList<>();
        //寻找顶级父部门
        for (T item : list) {
            if((Long) getParentId.apply(item) == state){
                parents.add(item);
            }
        }
        //构建部门树
        for (T parent: parents){
            T tree = buildTrees(parent, list, getParentId);
            trees.add(tree);
        }
        return trees;
    }
    //寻找父节点
    private static <T> T buildParent(T item, List<T> list, Function<T,?> getParentId){
        try {
            T parent = item;
            for(T items: list) {
                Method getIdMethod = items.getClass().getMethod("getId");
                if (getParentId.apply(item).equals(getIdMethod.invoke(items))){
                    parent = buildParent(items, list, getParentId);
                }
            }
            return parent;
        } catch (Exception e) {
            throw new ServiceException("方法失效");
        }
            //如果存在，递归往上找父部门

    }

    private static <T> T buildTrees(T parent, List<T> list, Function<T,?> getParentId){
        try {
            Method setChildrenMethod = parent.getClass().getDeclaredMethod("setChildren", List.class);
            Method getIdMethod = parent.getClass().getDeclaredMethod("getId");
            List<T> children = new ArrayList<>();
            for (T item : list) {
                if(getIdMethod.invoke(parent).equals(getParentId.apply(item))){
                    children.add(buildTrees(item, list, getParentId));
                }
            }
            setChildrenMethod.invoke(parent, children);
            return parent;
        }catch (Exception e){
            throw new ServiceException("方法失效");
        }
    }
}
