---
theme: channing-cyan
highlight: androidstudio
---
# 编程解决一切烦恼

## Obsidian搭建个人笔记
最近在使用`Obsidian`搭建个人云笔记
![Snipaste_2023-09-24_13-41-15.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7105083ecfcb4f47bb5999cb7ba3bfed~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=1545&h=983&s=164831&e=png&b=f9f9f9)

虽然我用`腾讯云COS`图床+`gitee`实现云备份，但是`Android`平台的`Obsidian`,备份比较麻烦,还好我都是在电脑端做笔记，手机只是作为阅读工具。

那就简单写一个局域网文件夹同步工具。

## 局域网文件互传

Windows和Android之间实现局域网内文件互传有以下几种协议

### HTTP 协议
**优点**:
- 实现简单,客户端和服务器都有成熟的库  
- 安全性较好,支持HTTPS加密  
- 可以传输不同类型的数据,包括文件、文本等

`缺点`:  
- 传输效率比Socket等协议低  
- 需要自行处理大文件分片上传和下载

### Socket 协议
**优点**:  
- 传输效率高,特别适合传输大文件  
- 建立连接简单快速

`缺点`:  
- 需要处理粘包问题,协议较为复杂  
- 没有加密,安全性差  
- 需要处理网络状态变化等异常

### SFTP 协议
**优点**:
- 安全性好,基于SSH通道传输  
- 支持直接映射为本地磁盘访问

`缺点`:  
- 实现较复杂,需要找到可用的SFTP库  
- 传输效率比Socket低

### WebSocket 协议
**优点**:
- 传输效率高,支持双向通信  
- 接口简单统一

`缺点`:  
- 需要处理连接状态,实现较为复杂  
- 没有加密,安全性较差

综合来说,使用`HTTP`或`Socket`都是不错的选择

## WebSocket
但是最后我选择了`WebSocket`，原因是 `Socket`在处理接收的时候，要考虑缓冲区的大小和计算`json`结尾的标识，要做到按行读取比较麻烦，而且`WebSocket`与`Socket`实现这个简单的功能，性能差别可以忽略不计，处理不但很容易导致数据污染以及丢失。还有一点是打算可以同时实现`剪贴板同步功能`。所以`HTTP`或`Socket`我选择`WebSocket`实现起来简单，

我开始是用`Socket`实现的，后来换了`WebSocket`，两者速度无差别，后者简直舒服多了。

## 思路

> 使用python 将windows的目标文件夹压缩成zip格式，发送至Android端, Android端,接收完成之后MD5校验文件完整性，之后解压zip到当前目录下，最后删除zip文件


## 定义json格式和功能标识码

### 为每个功能定义标识码

```kotlin
enum class SocketType(val type: String, val msg: String) {
    FILE_SYNC("FILE_SYNC", "文件同步"),
    FOLDER_SYNC("FOLDER_SYNC", "文件夹同步"),
    CLIPBOARD_SYNC("CLIPBOARD_SYNC", "剪贴板同步"),

    HEARTBEAT("HEARTBEAT", "心跳"),

    FILE_SENDING("FILE_SENDING", "发送中"),
    FOLDER_SYNCING("FOLDER_SYNCING", "文件夹同步中"),
    FILE_SENDEND("FILE_SENDEND", "发送完成");
}
```

### 用于文件传输过程中表示文件发送进度的模型类
```kotlin
data class FileSendingDot(
    val fileName: String,
    val bufferSize: Int,
    val total: Long,
    val sent: Long,
    val data: String
)
```

## Python服务器端实现
### 创建websocket服务端

使用`Python`的`asyncio`和`websockets`模块实现了一个异步的`WebSocket`服务器,通过异步事件循环来处理客户端的连接和通信。

```python
import asyncio
import websockets

start_server = websockets.serve(handle_client, "", 9999)
asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()
```

#### 解析同步请求,操作本地文件夹

```python
json_obj = json.loads(data)
        type_value = json_obj["type"]
        data_value = json_obj["data"]

        if type_value == "FILE_SYNC":
            await send_file(websocket,"FILE_SENDING", file_path)
```

#### 利用循环分块读取文件并通过WebSocket发送每个数据块,同时构造消息对象封装文件信息


```python
file_data = f.read(buffer_size)
            sent_size += len(file_data)
            # 发送数据块,包含序号和数据
            send_file_data = base64.b64encode(file_data).decode()
            file_seading_data = {
                "fileName": filename,
                "bufferSize":buffer_size,
                "total": total_size,
                "sent": sent_size,
                "data": send_file_data,
            }
            msg = {
                "type": type,
                "msg": "发送中",
                "data": json.dumps(file_seading_data),
            }
            await ws.send(json.dumps(msg))
```

# 安卓客户端端实现

## 请求所有文件访问权限
```kotlin

va launcher = registerForActivityResult(
ActivityResultContracts.StartActivityForResult()) { result ->
// 权限已授权 or 权限被拒绝
}

private fun checkAndRequestAllFilePermissions() {

    //检查权限
    if (!Environment.isExternalStorageManager()) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.setData(Uri.parse("package:$packageName"))
        launcher.launch(intent)
    }
}
```
# 自定义保存路径

## 选择文件夹
`rememberLauncherForActivityResult() `创建一个`ActivityResultLauncher`,用于启动并获取文件夹选择的回调结果。

```kotlin
val selectFolderResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { data ->
        val uri = data.data?.data
        if (uri != null) {
            intentChannel.trySend(ViewIntent.SelectFolder(uri))
        } else {
            ToastModel("选择困难! ƪ(˘⌣˘)ʃ", ToastModel.Type.Info).showToast()
        }
    }
```
## Uri的path
```kotlin
fun Uri.toFilePath(): String {

    val uriPath = this.path ?: return ""
    val path = uriPath.split(":")[1]
    return Environment.getExternalStorageDirectory().path + "/" + path
}
```
![video2gif_20230924_154332.gif](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/58a084ae40444abb8ccd4edad33fa7db~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=472&h=1024&s=2387898&e=gif&f=130&b=ffffff)


## okhttp实现websocket


```kotlin

private val client = OkHttpClient.Builder().build()

//通过callbackFlow封装,实现流式API
fun connect() =
   createSocketFlow()
       .onEach {
          LogX.i("WebSocket", "收到消息 $it")
       }.retry(reconnectInterval)

private fun createSocketFlow(): Flow<String> = callbackFlow {
    val request = Request.Builder()
        .url("ws://192.168.0.102:9999")
        .build()

    val listener = object : WebSocketListener() {
       ...接收消息的回调
    }
    socket = client.newWebSocket(request, listener)
   //心跳机制
   launchHeartbeat()
    awaitClose { socket?.cancel() }
}.flowOn(Dispatchers.IO)

//服务端发送数据
fun send(message: String) {
    socket?.send(message)
}
```

## 接收文件

使用 `Base64.decode() `方法将 `base64` 数据解码成字节数组 `fileData`
```kotlin
val fileName = dot.fileName
val file = File(AppSystemSetManage.fileSavePath, fileName)
val fileData = Base64.decode(dot.data, Base64.DEFAULT)
```
- 接着就是使用IO数据流 `OutputStream` 加上`自定义的路径` 一顿操作 就得到zip文件了
- 最后解压zip到当前文件夹

## 接收文件

# 显示发送进度

> 从FileSendingDot对象中取出已发送数据量sent和总数据量total。
> 可以实时获取文件传输的进度

用`drawBehind`在后面绘制矩形实现进度条占位。根据进度计算矩形宽度,实现进度填充效果。不会遮挡子组件,很简洁地实现自定义进度条。
```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            val fraction = progress * size.width
            drawRoundRect(
                color = progressColor,
                size = Size(width = fraction, height = size.height),
                cornerRadius = CornerRadius(12.dp.toPx()),
                alpha = 0.9f,
            )
        }
```

```kotlin
@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
    onClick: () -> Unit = {}
) {
    val progressColor = WordsFairyTheme.colors.themeAccent
    //通过判断progress的值来决定是否显示加载
    val load = progress > 0F

    val textColor = if (load) WordsFairyTheme.colors.themeUi else WordsFairyTheme.colors.textPrimary
    OutlinedCard(
        modifier = modifier,
        onClick = onClick,
        colors =
        CardDefaults.cardColors(WordsFairyTheme.colors.itemBackground),
        border = BorderStroke(1.dp, textColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val fraction = progress * size.width
                    drawRoundRect(
                        color = progressColor,
                        size = Size(width = fraction, height = size.height),
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        alpha = 0.9f,
                    )
                },
            content = {
                Row {
                    Title(
                        title = title, Modifier.padding(16.dp),
                        color = textColor
                    )
                    Spacer(Modifier.weight(1f))
                    if (load)
                        Title(
                            title = "${(progress * 100).toInt()}%", Modifier.padding(16.dp),
                            color = textColor
                        )
                }
            }
        )
    }
}
```
## 效果图
![video2gif_20230924_153337.gif](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f9b27bacb95241fca02d6dc5c52e05f3~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=853&h=480&s=925816&e=gif&f=109&b=ffffff)

# 完整代码


