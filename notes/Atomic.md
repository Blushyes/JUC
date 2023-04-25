# 原子变量

Java中的原子变量是指**一类具有原子性操作的变量**，可以保证在多线程并发访问时操作的正确性。Java中提供了一组原子类型，包括AtomicInteger、AtomicBoolean、AtomicReference等。

原子变量可以使用原子操作来保证多线程访问的原子性，这些操作是由硬件提供支持的，使用锁等高层次机制来实现。原子变量支持一些常见的操作，如增加、减少、比较交换等，这些操作具有原子性，不会被中断或同时被多个线程执行。

使用原子变量可以避免线程同步问题，提高程序的性能，尤其在高并发环境下。值得注意的是，虽然使用原子变量可以保证操作的原子性，但并不能保证线程安全，仍然需要开发人员在编写代码时注意线程安全问题。

## CAS

CAS，即比较并交换（Compare And Swap）是一种无锁算法，用于同步多线程对共享数据进行并发读写的操作。CAS操作只有在内存值和预期值（期望值）相同时才会进行更新，否则不进行任何操作，因此是一种无锁算法。

CAS操作通常使用一个包含预期值、新值和操作地址的三个参数，当以原子操作的方式比较地址处的内存值是否等于预期值时，如果相等，则更新为新值，否则操作失败。在CAS操作期间，整个过程是原子性、不可中断、不会被其他线程干扰的，因此CAS操作是一种非常高效的并发控制，适用于并发量较高时的场景。

在Java中，AtomicInteger、AtomicLong、AtomicBoolean和AtomicReference等原子类都封装了CAS的操作，使用这些类能够方便地实现线程安全的操作。CAS是无锁算法，使用起来较为复杂，但是相比锁机制，它能够提供更高的并发度和性能。

## 原子整数

假设我有一个原子变量：

~~~java
AtomicInteger value = new AtomicInteger(0);
~~~

我想要对它进行修改，得用compareAndSet()方法：

~~~java
boolean compareAndSet(Integer expectedValue, Integer newValue)
~~~

其中两个参数分别为：

- **expectedValue**：期待值，cas方法内部会再去获取一下这个原子变量的值，并与期待值进行对比，如果与期待值相同，则说明没有被其他线程修改，则完成修改并返回true；否则，说明被其他线程修改过，返回false。
- **newValue**：目标值，要修改成的值。

那么，假设我需要对value进行加一操作（并且必须完成），则需要这样实现：

~~~java
for (;;) {
    // 首先通过get方法获取值
    int expectedValue = value.get();

    // cas函数会重新获取value的值并expectValue作对比，若相同，则替换成newValue，否则返回false
    if (value.compareAndSet(expectedValue, expectedValue + 1)) {
        break;
    }
}
~~~

如果修改一直无法成功，那么就会一直循环，直到修改成功为止；这样可以避免因为加锁导致的频繁的上下文切换（频繁的上下文切换非常耗时间）。

当然，AtomicInteger内置了许多常用的操作如：

- addAndGet(int num)：加上num
- incrementAndGet()：加1，相当于addAndGet(1)
- ......

不过值得注意的是updateAndGet方法，可以对值进行任意修改并返回，需要传入一个lambda函数。

事实上就是对上面代码的一层封装，简化了代码（无需自己写循环和判断，只需要关心数值怎么变就行）。

比如上面的加一操作可以简化为：

~~~java
value.updateAndGet(val -> val + 1);
~~~

## 原子引用

针对引用类型进行封装的原子变量，如AtomicRefrence<BigDecimal>，AtomicStampedRefrence<BigDecimal>，AtomicMarkableReference<BigDecimal>。

通过`AtomicDemo3`测试可知，**原子引用并不会让对成员变量的操作也变为原子操作，它仅仅可以让对原子引用这个引用变量的更改变成一个原子操作**。

注意！

**AtomicRefrence并不能感知到别的线程对共享变量的修改。**

一个比较经典的例子就是**ABA**问题：

即某个线程读到变量的状态为A，然后变量的状态由其他线程经历了类似的修改：A->B->A，最终导致该线程再次读取变量时，并没有感知到变量被改变过，CAS操作成功。

当然，这样的操作往往不会造成很大的影响，但是也有可能造成影响。

如果实在需要感知到变量是否被修改过，则可以引入一个版本号，每次进行修改就对版本号进行变更，线程在执行CAS操作的时候不仅需要比较值，还需要比较版本号是否一致，即使用AtomicStampedRefrence类。

当然有时候，我们并不关注具体的版本号，只想知道是否被修改过，那么完全可以用一个布尔值来存储是否被修改，即使用AtomicMarkableRefrence类。

## 原子数组

AtomicIntegerArray

AtomicLongArray

AtomicRefrenceArray

保证数组的增删查改是原子操作。

## 字段更新器

AtomicRefrenceFieldUpdater

AtomicIntegerFieldUpdater

AtomicLongFieldUpdater

对某个对象的成员变量进行保护。

注意以下两点：

- **只能配合volatile修饰的字段使用。**
- **通过工厂方法创建对象。**
- 只能保护非private字段

## 原子累加器

LongAdder

效率比AtomicLong的自增要好。

**初始化从零开始。**