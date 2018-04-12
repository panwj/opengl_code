# opengl_code
学习opengl


## opengl es 比较赞的学习链接

比较详细
http://www.cnblogs.com/Anita9002/category/682865.html

https://blog.csdn.net/column/details/android-opengl.html

https://blog.piasy.com/2016/06/07/Open-gl-es-android-2-part-1/





## opengl es 重要方法

、、、、java
glVertexPointer

public void glVertexPointer(int size,int type,int stride,Buffer pointer)
、、、、

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
