package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.example.utils.IoCloseUtils;

public class ProcessBuilderTask implements Callable<String> {
	private Process process;
	private List<String> command = new ArrayList<String>();
	private BufferedReader stdOut;
	private BufferedReader stdErr;
	
	
	public ProcessBuilderTask(String commandExe, String workNum, String json)
	{
		this.command.add(commandExe);
		this.command.add(workNum);
		this.command.add(json);
	}
	
	
	@Override
	public String call() throws Exception {
		String s = null;
		boolean isResultOn = false;
		boolean isError = true;
		String resultJson = "ERROR";
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			process = processBuilder.start();
			System.out.println("ProcessBuilder start");
//			int exitValue = process.waitFor(); // 프로그램을 실행하고 끝날때 까지 기다리기
//			System.out.println("ProcessBuilder exitValue:" + exitValue);
			
			stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(),
					java.nio.charset.Charset.forName("UTF-8")));
			stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream(),
					java.nio.charset.Charset.forName("UTF-8")));
			System.out.println("BufferedReader create success");
			
			
			while ((s = stdOut.readLine()) != null) {
				
				System.out.println("DEBUG readLine length:" + s.length());
				
				if (isResultOn == false) {
					if (s.length() == 8 && "RESULT::".equals(s) == true) {
						isResultOn = true;
						System.out.println("결과 저장 시작");
					} else {
						System.out.println("DEBUG:" + s);
					}
				} else {
					isError = false;
					resultJson = s;
					System.out.println("결과 저장 성공 length:" + resultJson.length());
					isResultOn = false;
				}
			}
			
			System.out.println("readLine end");
			
			if (isError == true) {
				StringBuilder sb = new StringBuilder();
				while ((s = stdErr.readLine()) != null) {
					sb.append(s).append("\n");
				}
				System.out.println(sb.toString());
				
			} else {
				System.out.println("ProcessBuilder InputStreamReader success");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			normalExit();
		}
		
		return resultJson;
	}

	
	public void forceExit() {
		if (process != null) {
			try {
				process.destroy();
				process = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		IoCloseUtils.close(stdOut);
		IoCloseUtils.close(stdErr);
	}
	
	
	private void normalExit() {
		IoCloseUtils.close(stdOut);
		IoCloseUtils.close(stdErr);
		stdOut = null;
		stdErr = null;
		
		if (process != null) {
			try {
				process.destroy();
				process = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		command.clear();
		command = null;
	}
	
}
