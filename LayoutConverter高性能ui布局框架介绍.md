## 引言

ui性能优化业内有很多成熟的实践，不过这些方案大都专注view树创建之后的过程，即view树的measure、layout、draw阶段。但是view树创建过程即LayoutInflater.inflate()却花费了大量的时间，稍微复杂的布局inflate过程就需要100+ms，严重超出了单帧16ms的绘制时间。为了解决这个问题我开发了LayoutConverter框架，可以把view树创建速度提升10倍（相对inflate）。

## LayoutInflater的问题

实际项目中，我们一般通过写布局xml文件，然后通过LayoutInflater的inflate方法创建view树。这种方案的优点本文就不再赘述，这里只分析LayoutInflater的性能问题。

回顾一下LayoutInflater的inflate过程：XmlPullParser打开编译的二进制布局xml文件，通过XmlPullParser解析view树，对于这个树的每个view节点，反射创建view对象，解析view节点的xml属性设置view对象对应的属性。

相对于java代码的方式创建view树，LayoutInflater有两个性能开销：

- XmlPullParser解析布局xml
- 反射创建view对象

## View对象的创建开销

其实LayoutInflater生成view树的过程中，大部分时间都消耗在view的构造函数上，具体就是这个函数上：TypedArray a = context.obtainStyledAttributes()，即解析view当前主题下的属性。

LayoutInflater的最大的性能瓶颈就是：view对象的创建速度。优化对象的创建速度有几种方案：

- view对象复用


- view对象提前创建


- view对象clone

最好的方案是app全局复用view对象，整个app是一个RecyclerView，这样可以大大避免view对象的创建开销。但是这种方案实现起来比较复杂，LayoutConverter 1.0版本并没有采用这种方案。

LayoutConverter目前采用的是“后台线程提前创建view对象”的方式，实现比较简单，且效果显著。

而clone的方式，由于android框架没有实现view的clone接口，我们只能做view对象的浅clone，内存性能比”view对象提前创建“的方式好一些，实现比”view对象复用“简单一些，比较中庸，但是后面会上”view对象复用“的方案，所以这种中间方案就不采用了。

## LayoutConverter方案

布局xml的方式无法优化view对象的创建开销，而LayoutInflater的性能开销也只有通过java代码手工写布局解决，所以想要优化view树的创建速度只有通过java代码的方式创建布局，但是这样又会牺牲开发效率。

我们知道，布局xml已经有了view树的所有信息。如果编译时去解析布局xml获取view树的信息，然后通过apt处理器自动生成java代码，运行时view树的创建LayoutInflater.inflate替换为java代码，就不用改变ui的开发流程，兼顾了性能优化与开发效率。

所以开发了LayoutConverter框架去解决这个性能与开发效率的问题。

## Activity中使用举例

```
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // activity_main.xml布局编译时生成对应的ActivityMain.java
        final View contentView = ActivityMain.from(this).inflate();
        setContentView(contentView);
        
        // setContentView(R.layout.activity_main);
    }
}
​```
```

如上图，LayoutConverter框架apt编译时自动把布局文件转换成对应的java类，即activity_main.xml自动生成ActivityMain.java源码。使用中只要把LayoutInflater.inflate(R.layout.activity_main)替换为ActivityMain.from(this).inflate()就ok了，接入非常方便，但是却能极大的提升ui的性能。


## 关于性能

LayoutConverter对比LayoutInflater理论性能可以提升10倍，当前实际benchamark性能提升是5倍左右。主要是项目刚完成功能开发，细节以及性能优化还需要完善。

LayoutConverter可以提升整个app的流畅度：Activity、fragment使用时可以提升界面的切换速度，ListView使用时可以减少ListView滑动时UI卡顿问题。

## 关于扩展性

LayoutConverter支持android框架所有的veiw、widget。考虑到实际项目会自定义很多widget，为了支持这些自定义的widget，LayoutConverter定义了一套代码模版，开发人员通过配置代码模版来扩展自定义widget的（非标准属性）支持。

生成java代码时，view的属性设置代码默认是setXXX方法，你也可以修改attrs.xml文件配置怎么去生成view属性的设置代码，例如:

```xml
<attr name="minHeight" format="dimension" code="$view.setMinimumHeight($value);"/>
<attr name="textColor" format="reference|color" valueClass="ColorStateList" code="$view.setTextColor($value);"/>
```

其中，

- code属性：配置怎样生成view对应属性的设置代码。
  其中
   > 1、code="set"或者不定义code属性表示使用默认set方法；
   > 2、code="ignore"表示不去设置对应view的属性


- $view: view对象
- $value: view对象对应属性的值
- valueClass属性：配置$value对象的class，不配置的话根据format属性去生成$value