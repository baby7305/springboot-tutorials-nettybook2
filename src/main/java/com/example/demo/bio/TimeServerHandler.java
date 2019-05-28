/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.bio;

import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;

/**
 * @author Administrator
 * @version 1.0
 * @date 2014年2月14日
 */
public class TimeServerHandler implements Runnable {

	private Socket socket;

	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			byte[] bytes = new byte[1024];
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
			out = new PrintWriter(outputStream, true);
			String currentTime = null;
			String body = null;
			while (true) {

				//读取数据（阻塞）
				int read = inputStream.read(bytes);
				if (read != -1) {
					body = new String(bytes, 0, read);
					System.out.println(body);
				} else {
					break;
				}

				System.out.println("The time server receive order : " + body);
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? ZonedDateTime.now().toString() : "BAD ORDER";
				out.println(currentTime);
			}

		} catch (Exception e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (this.socket != null) {
				try {
					this.socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				this.socket = null;
			}
		}
	}
}
