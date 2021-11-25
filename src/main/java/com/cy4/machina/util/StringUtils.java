/**
 * This file is part of the Machina Minecraft (Java Edition) mod and is licensed under the MIT license.
 * If you want to contribute please join https://discord.com/invite/x9Mj63m4QG.
 * More information can be found on Github: https://github.com/Cy4Shot/MACHINA
 */

package com.cy4.machina.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class StringUtils {

	public static final String TREE_V = "\u2502";
	public static final String TREE_H = "\u2500";
	public static final String TREE_F = "\u251c";
	public static final String TREE_L = "\u2514";

	public static final Charset utf8Charset = Charset.forName("UTF-8");
	public static final Charset defaultCharset = Charset.defaultCharset();

	public static void printlnUtf8(String msg) {
		try {
			byte[] sourceBytes = msg.getBytes("UTF-8");
			String data = new String(sourceBytes, defaultCharset.name());

			PrintStream out = new PrintStream(System.out, true, utf8Charset.name());
			out.println(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
