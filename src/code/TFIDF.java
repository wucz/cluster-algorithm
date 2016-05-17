package code;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;

public class TFIDF {
	private static List<String> FileList = new ArrayList<String>();

	public static void main(String[] arg) throws IOException {
		long time = System.currentTimeMillis();
		String file = "E:\\examples";
		Fileprocess files = new Fileprocess();
		files.readDir(file);
		FileList = files.FileList;

		HashMap<String, HashMap<String, Float>> tfidf = null;
		HashMap<String, HashMap<String, Float>> all_tf = tfAllFiles(file);
		System.out.println("tf计算执行耗时 : " + (System.currentTimeMillis() - time)
				/ 1000f + " 秒 ");
		System.out.println();
		HashMap<String, Float> idfs = idf(all_tf);
		System.out.println("idf计算执行耗时 : " + (System.currentTimeMillis() - time)
				/ 1000f + " 秒 ");
		System.out.println();
		tfidf = tf_idf(all_tf, idfs);
		System.out.println("tf-idf执行耗时 : "
				+ (System.currentTimeMillis() - time) / 1000f + " 秒 ");
		double[][] a = new double[FileList.size()][FileList.size()];
		// System.out.println(tfidf);
		// CosineSimilarAlgorithm b = new CosineSimilarAlgorithm(
		// tfidf.get("E:\\examples\\CN201310053237.1.txt"),
		// tfidf.get("E:\\examples\\CN201310082363.X.txt"));
		for (int i = 1; i < FileList.size(); i++) {
			for (int j = 1; j < FileList.size(); j++) {
				if (!FileList.get(i).equals(FileList.get(j))) {
					CosineSimilarAlgorithm b = new CosineSimilarAlgorithm(
							tfidf.get(FileList.get(i)), tfidf.get(FileList
									.get(j)));
					a[j][i] = b.sim();
				} else
					a[j][i] = 0;
				// System.out.println(FileList.get(i)+FileList.get(j)+":"+a[j][i]+" ");
			}
			System.out.print(" ");
		}
		/*
		 * ClusteringAlgorithm alg = new DefaultClusteringAlgorithm(); Cluster
		 * cluster = alg.performClustering(a, FileList.toArray(new
		 * String[FileList.size()]), new AverageLinkageStrategy());
		 * cluster.toConsole(0);
		 */// 层次聚类
		System.out.println("\r<br>相似度计算执行耗时 : "
				+ (System.currentTimeMillis() - time) / 1000f + " 秒 ");

	}

	// the list
	// of file

	public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords) {
		HashMap<String, Integer> resTF = new HashMap<String, Integer>();
		for (String word : cutwords) { // 计算词语在一篇文档中的出现次数
			if (resTF.get(word) == null) {
				resTF.put(word, 1);
				// System.out.println(word);
			} else {
				resTF.put(word, resTF.get(word) + 1);
				// System.out.println(word.toString());
			}
		}
		return resTF;
	}

	public static HashMap<String, Float> tf(ArrayList<String> cutwords) {// 计算词语在一篇文档中的词频
		HashMap<String, Float> resTF = new HashMap<String, Float>();

		int wordLen = cutwords.size();
		HashMap<String, Integer> intTF = normalTF(cutwords);

		Iterator iter = intTF.entrySet().iterator(); // iterator for that get
														// from TF
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();// 取hashmap中的一条记录
			resTF.put(entry.getKey().toString(),
					Float.parseFloat(entry.getValue().toString()) / wordLen);
			// System.out.println(entry.getKey().toString() + " = "
			// + Float.parseFloat(entry.getValue().toString()) / wordLen);
		}
		return resTF;
	}

	public static HashMap<String, HashMap<String, Integer>> normalTFAllFiles(
			String dirc) throws IOException {
		HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String, Integer>>();

		// FileList = Fileprocess.readDir(dirc);
		for (String file : FileList) {
			HashMap<String, Integer> dict = new HashMap<String, Integer>();// 一个文档中的相对词频
			ArrayList<String> cutwords = Fileprocess.cutwords(file); // get cut
																		// word
			dict = normalTF(cutwords);
			allNormalTF.put(file, dict);
		}
		return allNormalTF;
	}

	public static HashMap<String, HashMap<String, Float>> tfAllFiles(String dirc)
			throws IOException {
		HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
		// FileList = Fileprocess.readDir(dirc);

		for (String file : FileList) {
			HashMap<String, Float> dict = new HashMap<String, Float>();
			ArrayList<String> cutwords = Fileprocess.cutwords(Fileprocess
					.readfile(file)); // get cut words for one file
			dict = tf(cutwords);
			allTF.put(file, dict);
		}
		return allTF;
	}

	public static HashMap<String, Float> idf(
			HashMap<String, HashMap<String, Float>> all_tf) {
		HashMap<String, Float> resIdf = new HashMap<String, Float>();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		int docNum = FileList.size();
		// System.out.println("docNum=" + docNum + FileList);
		for (int i = 0; i < docNum; i++) {
			// System.out.println(i);
			HashMap<String, Float> temp = all_tf.get(FileList.get(i));// 使用之前计算tf值时已经遍历得到的文件列表

			Iterator iter = temp.entrySet().iterator();

			while (iter.hasNext()) {

				Map.Entry entry = (Map.Entry) iter.next();// 统计总的词频数量
				String word = entry.getKey().toString();
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}

			}
		}
		// System.out.println("IDF for every word is:");
		Iterator iter_dict = dict.entrySet().iterator();
		while (iter_dict.hasNext()) {
			Map.Entry entry = (Map.Entry) iter_dict.next();
			float value = (float) Math.log10(docNum
					/ Float.parseFloat(entry.getValue().toString()));// 计算相对词频
			resIdf.put(entry.getKey().toString(), value);
			// System.out.println(entry.getKey().toString() + " = " + value);
			// System.out.println(entry.getKey().toString() + " = " +
			// Float.parseFloat(entry.getValue().toString()));
		}
		return resIdf;
	}

	public static HashMap<String, HashMap<String, Float>> tf_idf(
			HashMap<String, HashMap<String, Float>> all_tf,
			HashMap<String, Float> idfs) {
		HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();

		int docNum = FileList.size();
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> tfidf = new HashMap<String, Float>();
			HashMap<String, Float> temp = all_tf.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				Float value = (float) Float.parseFloat(entry.getValue()
						.toString()) * idfs.get(word);
				tfidf.put(word, value);
			}
			hashmapsort(tfidf);
			resTfIdf.put(filepath, tfidf);
		}
		// System.out.println("TF-IDF for Every file is :");
		// DisTfIdf(resTfIdf);
		return resTfIdf;
	}

	public final static int words_size = 100; // 每篇文章关键词数量

	public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf) {
		Iterator iter1 = tfidf.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entrys = (Map.Entry) iter1.next();
			System.out.println("FileName: " + entrys.getKey().toString());
			System.out.print("{");
			HashMap<String, Float> temp = (HashMap<String, Float>) entrys
					.getValue();
			Iterator iter2 = temp.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry entry = (Map.Entry) iter2.next();
				System.out.print(entry.getKey().toString() + " = "
						+ entry.getValue().toString() + ", ");
			}
			System.out.println("}");
		}

	}

	public static void hashmapsort(HashMap<String, Float> map) {
		List<Map.Entry<String, Float>> entryList = new ArrayList<Map.Entry<String, Float>>(
				map.entrySet());
		Collections.sort(entryList, new Comparator<Map.Entry<String, Float>>() { // 重写collections中的compare方法
					public int compare(Map.Entry<String, Float> o1,
							Map.Entry<String, Float> o2) {
						// return (o1.getKey() - o2.getKey()); //根据key排序
						if ((o1.getValue() - o2.getValue()) < 0) // 有Bug　不能写＞于号
							return 1;// 根据value排序
						else
							return -1;
						// return
						// (o1.getKey()).toString().compareTo(o2.getKey());
					}
				});
		map.clear();
		int size = entryList.size();
		if (size > words_size) // 限制关键词数量，取前1000个
			size = words_size;
		for (int i = 0; i < size; i++) {
			map.put(entryList.get(i).getKey(), entryList.get(i).getValue());
		}
		// System.out.println(entryList);
	}
}