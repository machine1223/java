package com.pairwinter.jvm;

import utils.UsageUtils;

class Singletion{
    /*
    第9行，类加载完成，初始化时，会执行构造函数，所以counter1和counter2都会增长，但是由于counter2也被赋值，所以程序往下走的时候会将0设置给counter2，最终的结果是counter1=1；counter2=0;
    请注意，初始化是为静态类属性设置值。这里先设置了singletion，然后又设置了counter2。
    如果把第9行移到第16行，则counter2的值就被构造函数更改了。此时的结果是counter1=1；counter2=1；
     */
    private static Singletion singletion = new Singletion();

    public static int counter1;
    public static int counter2 = 0;

//    private static Singletion singletion = new Singletion();

    private Singletion(){
        counter1++;
        counter2++;
    }
    public static Singletion getInstence(){
        return singletion;
    }
}

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-24
 * Time: 上午7:58
 * <p>Java虚拟机与程序的生命周期</p>
 * <p>在如下几种情况下，JVM将结束生命周期</p>
 * <ul>
 *     <li>System.exit()</li>
 *     <li>程序正常执行结束</li>
 *     <li>程序在执行过程中遇到了异常或者错误而异常终止</li>
 *     <li>操作系统出现问题导致JVM终止</li>
 * </ul>
 * <p><b style="color:red">类的加载、连接与初始化</b></p>
 * <ul>
 *     <li>
 *         加载：<br/>
 *         将类A的.class文件读取到内存，将其放在运行时数据区的方法区内，
 *         然后在堆区创建一个java.lang.Class对象(A.class)，用来封装类A在方法区内的数据结构。 <br/>
 *         类的加载的最终产品是位于堆区中的Class对象。<br/>
 *         Class对象封装了类在方法区内的数据结构，并且向java程序员提供了访问方法区内的数据结构的接口。（即反射的接口）
 *         <table>
 *             <tr>
 *                 <td>java程序</td>
 *                 <td>
 *                     调用Class对象的方法<br/>
 *                     --------------------------&gt;<br/>
 *                     比如getInstance()方法
*                  </td>
 *                 <td>
 *                     堆区<br/>
 *                     描述Worker类的Class对象 -------------&gt;<br/>
 *                     描述Car类的Class对象 ----------------&gt;
 *                 </td>
 *                 <td>
 *                     方法区<br/>
 *                     Worker类的数据结构<br/>
 *                     Car类的数据结构
 *                 </td>
 *             </tr>
 *         </table>
 *         <p>加载方式：</p>
 *         <ul>
 *             <li>从本地系统中直接加载</li>
 *             <li>通过网络加载.class文件</li>
 *             <li>从zip，jar中加载</li>
 *             <li>从专有数据库中提取.class文件</li>
 *             <li>将java源文件动态编译为.class文件</li>
 *         </ul>
 *         <p>
 *             <b>类的加载器</b><br/>
 *             <ul>
 *                 <li>
 *                     JVM提供了三种类的加载器
 *                     <ul>
 *                         <li>根类加载器：使用C++编写，程序员无法在java代码中使用</li>
 *                         <li>扩展加载器：使用java代码实现</li>
 *                         <li>系统加载器（应用加载器）：使用java代码实现</li>
 *                     </ul>
 *                 </li>
 *                 <li>
 *                     用户自定义类加载器
 *                     <ul>
 *                         <li>定义java.lang.ClassLoader的子类</li>
 *                         <li>用户可以定制类的加载方式</li>
 *                     </ul>
 *                 </li>
 *             </ul>
 *
 *         </p>
 *     </li>
 *     <li>连接:
 *          <ul>
 *              <li>验证：确保被加载的类的正确性。</li>
 *              <li>准备：为类的<b>静态变量</b>分配内存，并将其初始化为默认值。</li>
 *              <li>解析：把类中的符号引用转为直接引用。</li>
 *          </ul>
 *     </li>
 *     <li>初始化：为类的静态变量赋予正确的初始值。</li>
 * </ul>
 * <p>Java 程序对类的使用方式可分为两种：</p>
 * <ul>
 *     <li>主动使用：六种情况
 *          <ul>
 *              <li>创建类的实例(new)</li>
 *              <li>访问类或者接口的静态变量，或者对该静态变量赋值</li>
 *              <li>调用了的静态方法</li>
 *              <li>反射（Class.forName(classpath)）</li>
 *              <li>初始化一个类的子类</li>
 *              <li>Java虚拟机启动时被表明为启动类的类（程序的入口，带有main方法的类）</li>
 *          </ul>
 *     </li>
 *     <li>被动使用</li>
 * </ul>
 * <p>所有的java虚拟机实现必须在每个类或者接口被java程序“首次主动使用”时才初始化他们。</p>
 *
 * <p></p>
 */
public class ClassLoader {
    public static void main(String[] args) {
        testSingletion();
        try{
            testClassLoader();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void testSingletion(){
        Singletion singletion = Singletion.getInstence();
        //第6行注释，解释了为什么会有当前的结果。
        UsageUtils.println("counter1: %d",Singletion.counter1);    // 1
        UsageUtils.println("counter2: %d",Singletion.counter2);    // 0
    }

    /**
     * 类加载器并不需要等到某个类被首次主动使用的时候才加载它。  <br/>
     * <ul>
     *     <li>
     *         <code>JVM</code>规范允许类加载器在预料某个类将要被使用时就预先加载它，<br/>
     *         如果在预先加载的过程中.class文件缺失或者存在错误，类加载器必须在程序首次主动使用该类时才报告错误.
     *         (LinkageError错误)
 *         </li>
     *     <li>
     *         如果这个类一直没有被程序主动使用，那么类加载器就不会报告这个错误。
     *     </li>
     * </ul>
     *
     * @throws Exception
     */
    public static void testClassLoader() throws Exception{
        Class clazz = Class.forName("java.lang.String");
        //clazz.getClassLoader()的值为null,说明是根类加载器加载的String类
        UsageUtils.println("java.lang.String的加载器是：%s",clazz.getClassLoader());
        Class c = Class.forName("com.pairwinter.jvm.C");
        //sun.misc.Launcher$AppClassLoader@3798f5e7这个是App加载器，即应用加载器。
        UsageUtils.println("C的加载器是：%s",c.getClassLoader());
    }

}

class C{

}
