package com.chenlb.mmseg4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.io.Serializable;

/**
 * 词典类. 词库目录单例模式.<br/>
 * 保存单字与其频率,还有词库.<br/>
 * 有检测词典变更的接口，外部程序可以使用 {@link #wordsFileIsChange()} 和 {@link #reload()} 来完成检测与加载的工作.
 *
 * @author chenlb 2009-2-20 下午11:34:29
 */
public class Dictionary implements Serializable {

	//private static final Logger log = Logger.getLogger(Dictionary.class.getName());
	private volatile Map<Character, CharNode> dict;
	private volatile Map<Character, Object> unit;	//单个字的单位

	protected void finalize() throws Throwable {
		/*
		 * 使 class reload 的时也可以释放词库
		 */
		destroy();
	}
	/**
	 * 销毁, 释放资源. 此后此对像不再可用.
	 */
	void destroy() {
		dict = null;
		unit = null;
	}
	public Dictionary(Map<Character, CharNode> dictToSet, Map<Character, Object> unitToSet) {
		dict = dictToSet;
		unit = unitToSet;
	}
	/**
	 * 全新加载词库，没有成功加载会回滚。<P/>
	 * 注意：重新加载时，务必有两倍的词库树结构的内存，默认词库是 50M/个 左右。否则抛出 OOM。
	 * @return 是否成功加载
	 */
	public synchronized void reload(File dicPath) throws IOException {
	}

	/**
	 * word 能否在词库里找到
	 * @author chenlb 2009-3-3 下午11:10:45
	 */
	public boolean match(String word) {
		if(word == null || word.length() < 2) {
			return false;
		}
		CharNode cn = dict.get(word.charAt(0));
		return search(cn, word.toCharArray(), 0, word.length()-1) >= 0;
	}

	public CharNode head(char ch) {
		return dict.get(ch);
	}

	/**
	 * sen[offset] 后 tailLen 长的词是否存在.
	 * @see CharNode#indexOf(char[], int, int)
	 * @author chenlb 2009-4-8 下午11:13:49
	 */
	public int search(CharNode node, char[] sen, int offset, int tailLen) {
		if(node != null) {
			return node.indexOf(sen, offset, tailLen);
		}
		return -1;
	}

	public int maxMatch(char[] sen, int offset) {
		CharNode node = dict.get(sen[offset]);
		return maxMatch(node, sen, offset);
	}

	public int maxMatch(CharNode node, char[] sen, int offset) {
		if(node != null) {
			return node.maxMatch(sen, offset+1);
		}
		return 0;
	}

	public ArrayList<Integer> maxMatch(CharNode node, ArrayList<Integer> tailLens, char[] sen, int offset) {
		tailLens.clear();
		tailLens.add(0);
		if(node != null) {
			return node.maxMatch(tailLens, sen, offset+1);
		}
		return tailLens;
	}

	public boolean isUnit(Character ch) {
		return unit.containsKey(ch);
	}


	/**
	 * 仅仅用来观察词库.
	 */
	public Map<Character, CharNode> getDict() {
		return dict;
	}


}
