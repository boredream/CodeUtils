# CodeUtils

代码工具,编译时非运行时(BufferKnife等框架属于运行时)工具
原理是根据需求生成String的代码字符串,然后利用FileIO流写入到文件中

# 已有核心功能包括
AndroidUtils:

1.自动遍历layout布局文件,生成findViewById的代码

2.自动根据item的布局生成一个最基础的BaseAdapter适配器类

3.提供2个颜色和圆角半径,自动生成圆角矩形的两个shape形状,并生成对应的selector

4.自动遍历文件中#RGB颜色,或DP/SP的大小,将其抽取到dimens.xml和colors.xml文件中


FileUtils:

1.遍历路径下全部文件

2.自动删除项目中无用文件(没有任何一个文件用使用过该文件)

3.获取项目代码行数(统计全部.xml和.java文件)


等等...
