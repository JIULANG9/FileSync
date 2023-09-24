import asyncio
import websockets
import os
from pathlib import Path
import pyperclip
import json
import base64
import zipfile
import math

FILE_BUFFER_MIN = 1024
FILE_BUFFER_MAX = 1024 * 1024 # 1MB

file_path = "E:\\xy\\FruitSugarContentDetection.zip"
folder_path = "E:\\Note\\Obsidian"
zip_path = "E:\\Note\\Obsidian.zip"

async def send_file(ws,type, filepath):
    # 获取文件名
    filename = os.path.basename(filepath)
    total_size = os.path.getsize(filepath)
    sent_size = 0
    if total_size < FILE_BUFFER_MAX * 10:
        buffer_size = math.ceil(total_size / 100)
    else:
        buffer_size = FILE_BUFFER_MAX

    with open(filepath, "rb") as f:
        while sent_size < total_size:
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
            print((sent_size / total_size) * 100)
    # 发送结束标志
    endmsg = {"type": "FILE_SENDEND", "msg": "发送完成", "data": "发送完成"}
    await ws.send(json.dumps(endmsg))

async def handle_client(websocket, path):
    # 用户连接时打印日志
    print("用户连接")

    async for data in websocket:
        print(data)
        json_obj = json.loads(data)
        type_value = json_obj["type"]
        data_value = json_obj["data"]

        if type_value == "FILE_SYNC":
            await send_file(websocket,"FILE_SENDING", file_path)

        if type_value == "FOLDER_SYNC":
            zip_folder(folder_path, zip_path)
            await send_file(websocket,"FOLDER_SYNCING", zip_path)

        if type_value == "CLIPBOARD_SYNC":
            pyperclip.copy(data_value)
            print(data_value)
        if type_value == "HEARTBEAT":
            dictionary_data = {
                "type": "HEARTBEAT",
                "msg": "hi",
                "data": "",
            }
            await websocket.send(json.dumps(dictionary_data))

    # 用户断开时打印日志
    print("用户断开")


def zip_folder(folder_path, zip_path):
    with zipfile.ZipFile(zip_path, "w", zipfile.ZIP_DEFLATED) as zipf:
        for root, _, files in os.walk(folder_path):
            for file in files:
                file_path = os.path.join(root, file)
                zipf.write(file_path, arcname=os.path.relpath(file_path, folder_path))


start_server = websockets.serve(handle_client, "", 9999)

asyncio.get_event_loop().run_until_complete(start_server)
asyncio.get_event_loop().run_forever()
