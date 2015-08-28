CodeUtils
=======

代码工具,一键自动生成完整代码。

JavaProject ,属于编译时(ButterKnife等框架属于运行时)工具。

原理是根据功能需要生成代码文本,然后利用FileIO流写入到文件中。

最终生成的代码和手写基本一模一样,因此基本上无任何学习成本。



已有核心功能包括
=======

`AndroidUtils:`

* 自动遍历layout布局文件,生成findViewById的代码

* 自动根据item的布局生成一个最基础的BaseAdapter适配器类

* 提供2个颜色和圆角半径,自动生成圆角矩形的两个shape形状,并生成对应的selector

* 自动遍历文件中#RGB颜色,或DP/SP的大小,将其抽取到dimens.xml和colors.xml文件中


`FileUtils:`

* 遍历路径下全部文件

* 自动删除项目中无用文件(没有任何一个文件用使用过该文件)

* 获取项目代码行数(统计全部.xml和.java文件)


`其他:`

* 自动根据服务器返回的json字符串生成对应的类文件

等等...


导入方法
=======
注意,以JavaProject的方式导入
Eclipse 中右键 Import -> General -> Existing Project into Workspace
选择地址Finish确认


使用方法
=======

在eclipse中导入成 JavaProject

具体方法的使用以最常用的功能为例,步骤如下

(其他功能直接看注释就可以,所有方法都已添加详细的注释)

#### 自动生成Activity代码 (autoCreateActivity)

　1. 布局文件xml部分自己手动编码

　2. 手动复制layout内容到代码工具Android/layout.xml中

　3. 在代码工具的Main类的main方法中,使用无参数方法 autoCreateActivity

　4. 打开代码工具Android/Activity.java,刷新一下

　5. 将自动生成的内容复制回项目中

　注意: 有参数型方法会直接修改目标文件,无法用eclipse进行撤回,需要配合svn使用

#### 自动生成Adapter代码 (autoCreateAdapter)

　与自动生成Activity代码同理,布局拷贝至item.layout,代码最后会生成在Android/Adapter.java中

#### 自动生成Json字符串对应的JavaBean类 (parseJson2Java)

　1. 将json字符串复制到代码工具的Json文件夹中jsonString.txt文件中

　2. 在代码工具的Main类的main方法中,使用方法JsonUtils.parseJson2Java

　3. 打开代码工具的Json文件夹中的JsonBean.java文件,刷新一下

　4. 将JsonBean.java中生成的javabean复制到项目里修改下类名称即可

　之后就可以直接用gson等工具直接把json字符串解析成我们生成的javabean根据自己的需要去使用了
