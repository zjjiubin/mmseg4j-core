package com.chenlb.mmseg4j;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DictionaryTest {

	private void printMemory() {
		Runtime rt = Runtime.getRuntime();
		long total = rt.totalMemory();
		long free = rt.freeMemory();
		long max = rt.maxMemory();
		System.out.println(String.format("total=%dk, free=%dk, max=%dk, use=%dk", total/1024, free/1024, max/1024, (total-free)/1024));
	}

	@Test
	public void testloadDicMemoryUse() {
		printMemory();
		DictionaryBuilder b = DictionaryBuilder.getInstance();
		b.createDic();
		printMemory();
	}

	@Test
	public void testloadDic() {
		DictionaryBuilder dic = DictionaryBuilder.getInstance();
		Dictionary dic1 = dic.createDic();
		Dictionary dic2 = dic.createDic();
		Assert.assertTrue(dic1 == dic2);

		dic1.destroy();
		//reload
		//dic2 = Dictionary.getInstance();
		//Assert.assertTrue(dic != dic2);
		dic2.destroy();
	}

	@Test
	public void testloadDicByPath() {
		DictionaryBuilder builder1 = DictionaryBuilder.getInstance("src");
		DictionaryBuilder builder2 = DictionaryBuilder.getInstance("./src");
		Dictionary dic = builder1.createDic();
		Dictionary dic2 = builder2.createDic();
		Assert.assertTrue(dic == dic2);

		Assert.assertFalse(dic.match("自定义词"));

		dic.destroy();
	}

	@Test
	public void testloadMultiDic() {
		DictionaryBuilder b = DictionaryBuilder.getInstance();
		Dictionary dic = b.createDic();

		Assert.assertTrue(dic.match("自定义词"));
	}

	@Test
	public void testMatch() {
		DictionaryBuilder b = DictionaryBuilder.getInstance();
		Dictionary dic = b.createDic();

		Assert.assertTrue(dic.match("词典"));

		Assert.assertFalse(dic.match("人个"));
		Assert.assertFalse(dic.match("三个人"));

		Assert.assertFalse(dic.match(""));
		Assert.assertFalse(dic.match("人"));

	}

	@Test
	public void testFileHashCode() throws IOException {
		File f = new File("data");
		File f1 = new File("./data");
		Assert.assertFalse(f.equals(f1));

		f1 = f.getAbsoluteFile();
		Assert.assertFalse(f.equals(f1));

		Assert.assertTrue(f.getCanonicalFile().equals(f1.getCanonicalFile()));

		f1 = new File("data");
		Assert.assertTrue(f.equals(f1));
	}
}
