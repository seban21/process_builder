package com.example.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class IoCloseUtils {

	public static void close(BufferedReader io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
		}
	}

	public static void close(BufferedWriter io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
		}
	}
}
