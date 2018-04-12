# opengl_code
学习opengl


## opengl es 比较赞的学习链接

比较详细
http://www.cnblogs.com/Anita9002/category/682865.html

https://blog.csdn.net/column/details/android-opengl.html

https://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/





## opengl es 重要方法

方法1
```java
glVertexPointer

public void glVertexPointer(int size,int type,int stride,Buffer pointer)
```

功能：

定义一个顶点坐标矩阵。

详细：

glVertexPointer指明当渲染时一个顶点坐标矩阵的存储单元和数据。

当一个顶点矩阵被指明时，size, type, stride和pointer保存为客户端状态。

如果顶点矩阵功能启用，当调用glDrawArrays方法或glDrawElements方法时会使用。想要启用或禁止顶点矩阵，使用glEnableClientState或glDisableClientState方法，并以GL_VERTEX_ARRAY为参数。顶点矩阵初始为禁止，调用glDrawArrays方法或glDrawElements方法时无效。

调用glDrawArrays方法根据事先指明的点和顶点属性矩阵创建一系列图元（都有相同的类型）。调用glDrawElements方法根据顶点索引和顶点属性创建一系列图元。

注意：

glVertexPointer在一般版本中是在客户端的。

错误：

如果size不是2, 3或者4，将产生GL_INVALID_VALUE。

如果type不是允许的值，将产生GL_INVALID_ENUM。

如果stride是负值，将产生GL_INVALID_VALUE。

pointer必须是直接缓存，并且类型与type指明的类型相同。

参数：

size——每个顶点的坐标维数，必须是2, 3或者4，初始值是4。

type——指明每个顶点坐标的数据类型，允许的符号常量有GL_BYTE, GL_SHORT, GL_FIXED和GL_FLOAT，初始值为GL_FLOAT。

stride——指明连续顶点间的位偏移，如果为0，顶点被认为是紧密压入矩阵，初始值为0。

pointer——指明顶点坐标的缓冲区，如果为null，则没有设置缓冲区。

抛出：

java.lang.IllegalStateException——如果是OpenGL ES 1.1并且VBOs可用。

java.lang.IllegalArgumentException——如果pointer不是直接缓存。

方法2:
```java
glViewport

public void glViewport(int x,int y,int width,int height)
```

功能：

设置一个视口

详细：

glViewport指明ｘ、ｙ从标准设备坐标到窗口坐标的仿射变换，使(xnd, ynd)为标准设备坐标，然后窗口坐标(xw, yw)由下列公式计算：

xw = ( xnd + 1 ) width/2 + x

yw = ( ynd + 1 ) height/2 + y

视口宽、高的范围区间视版本而定，想查询此区间可使用方法glGetIntegerv，并以GL_MAX_VIEWPORT_DIMS为参数。

错误：

如果宽、高为负数，将产生GL_INVALID_VALUE

参数：

ｘ——指明视口矩形的左下角ｘ坐标，初始值为０。

ｙ——指明视口矩形的左下角ｙ坐标，初始值为０。

width——指明视口的宽，如果ＧＬ上下文首次附于一个surface则宽、高为这个surface大小。

height——指明视口的高，如果ＧＬ上下文首次附于一个surface则宽、高为这个surface大小。


方法3:
```java
glMatrixMode

public void glMatrixMode(int mode)
```

功能：

指明哪个矩阵是当前矩阵。

详细：

glMatrixMode设置当前矩阵模式，mode允许的值有：

GL_MODELVIEW——应用视图矩阵堆的后续矩阵操作。

GL_PROJECTION——应用投射矩阵堆的后续矩阵操作。

GL_TEXTURE——应用纹理矩阵堆的后续矩阵操作。

GL_MATRIX_PALETTE_OES（OES_matrix_palette 扩展）——启用矩阵调色板堆栈扩展，并应用矩阵调色板堆栈后续矩阵操作。

错误：

如果mode不是一个允许的值，将产生一个GL_INVALID_ENUM。

参数：

mode——指明哪一个堆允许后续的矩阵操作。允许的值有L_MODELVIEW,GL_PROJECTION和GL_TEXTURE，在有OES_matrix_palette扩展时，GL_MATRIX_PALETTE_OES也被允许，初始值是GL_MODELVIEW。


方法4:
```java
glLoadIdentity

public void glLoadIdentity()
```

功能：

用特征矩阵代替当前矩阵。

详细：

glLoadIdentity使特征矩阵代替当前矩阵。语义上等价于调用glLoadMatrix方法并以特征矩阵为参数。

( 1       0       0       0 )

( 0       1       0      0 )

( 0       0       1      0 )

( 0       0       0      1 )

但在一些情况下它更高效。
