package com.chuhui.primeminister.collector.plugin;


import com.chuhui.primeminister.collector.ClassDescInfoHolder;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public abstract class AbstractPrimeMinisterPlugin {


    static Map<ClassLoader,ClassPool> loaderPoolMap=new ConcurrentHashMap<>();


    public abstract boolean isSupported(ClassDescInfoHolder classDescInfoHolder);


    public byte[] weaving(ClassDescInfoHolder holder) {



        // 相当于,接下来要进行更改的所有class file,都缓存在一个ClassPool里面
        // 不能使用这种方式
//        ClassPool classPool = ClassPool.getDefault();
        // 不同的类加载器,创建不同的classPool

        ClassLoader loader = holder.getLoader();
        ClassPool classPool = loaderPoolMap.get(loader);
        if(Objects.isNull(classPool)){
            classPool=new ClassPool(true);
            loaderPoolMap.put(loader,classPool);
        }

        try {
            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(holder.getClassfileBuffer()));
            ctClass.defrost();
            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
            if (null != declaredMethods && declaredMethods.length > 0) {
                for (CtMethod method : declaredMethods) {
                   if(hasBefore(method)){
                       String s = beforeLogic(method);
                       method.insertBefore(s);
                   }
                }
                /**
                 * problem
                 * 如果A加载器,已经加载了AClass,并且,探针已经修改了这个AClass内部的行为
                 * 这时候,B加载器,又一次加载了AClass,探针还能再次修改这个AClass吗?
                 * 在回答这个问题之前,需要明确,当前B加载器加载的AClass,是原始的,没有被探针修改过的Class,还是被探针修改过了的Class?
                 *
                 * 很明显啊,还是那个被探针修改过的类..
                 * 怎么解决这个问题呢?
                 * 很明显啊,
                 *
                 * 如果一个类被同一个类加载器重新加载...
                 *
                 *
                 */

                return ctClass.toBytecode();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


    protected String logicTemplate(){
        return "{ try{ %s; }catch(Exception e){e.printStackTrace();} }";
    }

    protected abstract  boolean hasBefore(CtMethod method);

    protected abstract  String beforeLogic(CtMethod method);

    protected abstract boolean hasAfter(CtMethod method);

    protected abstract  String afterLogic(CtMethod method);

}
