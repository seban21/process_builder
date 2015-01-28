package com.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.codehaus.jackson.map.ObjectMapper;

import com.example.model.ChildVo;
import com.example.model.RootVo;
import com.example.utils.ThreadFactoryUtils;

public class Main {

	public final static int CORE_POOL_SIZE = 16;
	public final static int MAX_POOL_SIZE = 16;
	public final static long KEEP_ALIVE_TIME = 3L;
	public final static long RUN_TIMEOUT_SEC = 1L; // 실행 타임아웃 지정
	
	// 컴파일된 실행 파일 위치
	public final static String COMMAND_EXE = "/bengine/bin/Start.exe";

	
	public static void main(String[] args) throws Exception {

		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
				MAX_POOL_SIZE * 10);

		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, queue, new ThreadFactoryUtils(
						"PROCESS_BUILDER", false, Thread.NORM_PRIORITY));
		
		ObjectMapper om = new ObjectMapper();
		
		RootVo rootVo = new RootVo();
		rootVo.rootA = "qqqqqqqqqqqqqqqqqqqqqqqqqq";
		ChildVo childVo = new ChildVo();
		rootVo.childVo = childVo;
		childVo.childA = "sssssssssssssssssssss";
		childVo.childB = 11;
		String paramJson = om.writeValueAsString(rootVo);

		String resultJson = null;
		ProcessBuilderTask task = new ProcessBuilderTask(COMMAND_EXE, "1", paramJson);
		try {
			Future<String> future = threadPoolExecutor.submit(task);
			resultJson = future.get(RUN_TIMEOUT_SEC, TimeUnit.SECONDS);
		} catch (RejectedExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			task.forceExit(); // 타임아웃시 강제 종료
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("ERROR".equals(resultJson) == true) {
			System.out.println("error");
			threadPoolExecutor.shutdownNow();
			return;
		}

		System.out.println("RESULT:" + resultJson);
		
		RootVo resultRootVo = om.readValue(resultJson, RootVo.class);
		System.out.println("RootA:" + resultRootVo.rootA);
		System.out.println("childA:" + resultRootVo.childVo.childA);
		System.out.println("childB:" + resultRootVo.childVo.childB);
		
		threadPoolExecutor.shutdownNow();
	}

}
