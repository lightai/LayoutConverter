# LayoutConverter

### android下高性能的布局生成器：
- 编译时转换xml layout到java code，消除运行时转换到java的开销。
- 减少view树创建过程中，view的创建开销。

<br/>

### activity中使用举例
```java
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
```
<br/>

### 接入

#### 1、gradle参数配置
app的build.gradle配置编译参数：android -> defaultConfig ->javaCompileOptions，例如：
````groovy
javaCompileOptions {
    annotationProcessorOptions {
        arguments = [
                        // layout converter
                        attrsFilePaths : """$projectDir/src/main/res/values/attrs.xml;
                                                    ${projectDir.getParent()}/commons/src/main/res/values/attrs.xml;
                                                    ${projectDir.getParent()}/picker-view/src/main/res/values/wheel__attrs.xml;
                                                    ${projectDir.getParent()}/pull-to-refresh/res/values/attrs.xml;
                                                    ${projectDir.getParent()}/viewpageindicator/src/main/res/values/vpi__attrs.xml;
                                                    ${projectDir.getParent()}/widget/src/main/res/values/attrs.xml""".toString(),
                        viewsFilePaths : "$projectDir/layoutconverter/views.xml".toString(),
                        stylesFilePaths : """$projectDir/src/main/res/values/styles.xml;
                                                     $projectDir/src/main/res/values/styles-standard.xml""".toString(),
                        excludeLayoutRegex : ".*include.*"
                ]
        }       
}
````
其中
- attrsFilePaths：字符串（;分割路径），项目中所有view的attrs.xml文件路径；
- viewsFilePaths：字符串（;分割路径），view的继承关系xml文件；
- stylesFilePaths：字符串（;分割路径），项目中所有styles.xml文件路径；
- excludeLayoutRegex：正则字符案，配置忽略生成java代码的文件名。

<br/>

#### 2、添加annotation
```java
@LayoutBuildInfo(
        modulePackage="com.***.app",
        layoutInfoDir="D:\\android\\***\\app\\src\\main\\res\\layout")
public class MyApplication extends Application {
```
其中，
- modulePackage：你的包名；
- layoutInfoDir：需要转换的布局根目录，即app layout的目录。

<br/>

#### 3、添加gradle依赖
    compile 'com.netease.layoutconverter:compiler:1.0.0'
    compile 'com.netease.layoutconverter:androidLibrary:1.0.0'
    compile 'com.netease.layoutconverter:library:1.0.0'

<br/>

### 4、自定义view的属性配置
详情参考：[自定义view的属性配置](./自定义属性.md)