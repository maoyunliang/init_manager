package com.yitai.utils;

import com.yitai.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * ClassName: TreeUtils
 * Package: com.yitai.utils
 * Description:
 *
 * @Author: 毛云亮
 * @Create: 2023/10/20 15:07
 * @Version: 1.0
 */
@Slf4j
public class TreeUtil {

    public static <T> ArrayList<T> buildTree(List<T> list, Function<T, ?> getParentId, Function<T, ?> getSortNo){
        ArrayList<T> trees = new ArrayList<>();
        ArrayList<T> parents = new ArrayList<>();
        //对初始列表做排序
        Object sortNo = getSortNo.apply(list.get(0));
        if (sortNo instanceof String ){
            list.sort(Comparator.comparing(e ->  (String)getSortNo.apply(e)));
        }else{
            list.sort(Comparator.comparingLong(e -> (long) getSortNo.apply(e)));
        }

        //寻找顶级父部门
        for (T item : list) {
            T parent = buildParent(item,list,getParentId);
            if(!parents.contains(parent)){
                parents.add(parent);
            }
        }
        if (sortNo instanceof String){
            parents.sort(Comparator.comparing(e -> (String) getSortNo.apply(e)));
        }else{
            parents.sort(Comparator.comparingLong(e -> (long) getSortNo.apply(e)));
        }

        //构建部门树
        for (T parent: parents){
            T tree = buildTrees(parent, list, getParentId);
            trees.add(tree);
        }
        return trees;
    }
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
                if (getParentId.apply(item)!= null && getParentId.apply(item).equals(getIdMethod.invoke(items))){
                    parent = buildParent(items, list, getParentId);
                }
            }
            return parent;
        } catch (Exception e) {
            log.error("build tree error",e);
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

    /**
     * 线性列表转树形列表
     *
     * @param source      数据源
     * @param checkRoot   如何判断是根节点,接收一个参数，当前节点
     * @param checkParent 如何判断是父节点，接收两个参数分别为根节点、当前节点
     * @param getChildren 如何拿到子节点列表
     * @param setChildren 如何设置子节点列表
     * @param <T>         节点类型
     * @return 树形列表
     */
    public static <T> List<T> formatTree(List<T> source, Predicate<T> checkRoot,
                                         BiPredicate<T, T> checkParent, Function<T, List<T>> getChildren,
                                         BiConsumer<T, List<T>> setChildren) {
        List<T> tree = new ArrayList<>();
        List<T> children = new ArrayList<>();
        for (T node : source) {
            if (checkRoot.test(node)) {
                tree.add(node);
            } else {
                children.add(node);
            }
        }
        for (T root : tree) {
            recur(root, children, checkParent, getChildren, setChildren);
        }
        return tree;
    }
    private static <T> void recur(T rootNode, List<T> children,
                                  BiPredicate<T, T> checkParent, Function<T, List<T>> getChildren, BiConsumer<T, List<T>> setChildren) {
        for (T node : children) {
            if (checkParent.test(rootNode, node)) {
                // 说明为此根的子节点
                List<T> list = getChildren.apply(rootNode);
                if (list == null) list = new ArrayList<>();
                list.add(node);
                setChildren.accept(rootNode, list);
                recur(node, children, checkParent, getChildren, setChildren);
            }
        }
    }

    public static <T> List<T> formatList(List<T> source,  Function<T, List<T>> getChildren,
                                         BiConsumer<T, List<T>> setChildren) {
        List<T> list = new ArrayList<>();
        for (T node : source) {
            list.add(node);
            recur(list, getChildren.apply(node), getChildren, setChildren);
            setChildren.accept(node, null);
        }
        return list;
    }

    private static <T> void recur(List<T> list, List<T> children,
                                  Function<T, List<T>> getChildren, BiConsumer<T, List<T>> setChildren) {
        if (children == null) return;
        for (T node : children) {
            list.add(node);
            List<T> c = getChildren.apply(node);
            if (c != null && c.size() > 0) {
                recur(list, c, getChildren, setChildren);
                setChildren.accept(node, null);
            }
        }
    }
}
