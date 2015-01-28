package com.example.utils;

import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryUtils implements ThreadFactory {
	private final AtomicInteger number = new AtomicInteger(1);
	private String name;
	private boolean daemon;
	private int priority;

	
	public ThreadFactoryUtils(String name, boolean daemon, int priority) {
		this.name = name;
		this.daemon = daemon;
		this.priority = priority;
	}

	
	@Override
	public Thread newThread(final Runnable runnable) {
		MessageFormat messageFormat = new MessageFormat("{0}-{1}");
		Thread t = new Thread(runnable, messageFormat.format(new Object[] { name,
				String.format("%1$03d", number.getAndIncrement()) }));
		t.setDaemon(daemon);
		t.setPriority(priority);
		return t;
	}
}
