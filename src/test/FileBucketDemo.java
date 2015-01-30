package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.UpYun;

/**
 * �ļ���ռ��demo
 */
public class FileBucketDemo {

	// ����ǰ�����ú�������������
	private static final String BUCKET_NAME = "lydia";
	private static final String USER_NAME = "lizhi";
	private static final String USER_PWD = "lizhishishabi";
	/** ��Ŀ¼ */
	private static final String DIR_ROOT = "/";
	/** �༶Ŀ¼ */
	private static final String DIR_MORE = "/1/2/3/";
	/** Ŀ¼�� */
	private static final String FOLDER_NAME = "tmp";
	/** �ϴ���upyun���ļ��� */
	private static final String FILE_NAME = "test.txt";

	/** ���ش��ϴ��Ĳ����ļ� */
	private static final String SAMPLE_TXT_FILE = System
			.getProperty("user.dir") + "/test.txt";

	private static UpYun upyun = null;

	static {
		File txtFile = new File(SAMPLE_TXT_FILE);

		if (!txtFile.isFile()) {
			System.out.println("���ش��ϴ��Ĳ����ļ������ڣ�");
		}
	}

	public static void main(String[] args) throws Exception {

		// ��ʼ���ռ�
		upyun = new UpYun(BUCKET_NAME, USER_NAME, USER_PWD);

		// ****** ��ѡ���� begin ******

		// �л� API �ӿڵ���������㣬Ĭ��Ϊ�Զ�ʶ������
		// upyun.setApiDomain(UpYun.ED_AUTO);

		// �������ӳ�ʱʱ�䣬Ĭ��Ϊ30��
		// upyun.setTimeout(60);

		// �����Ƿ���debugģʽ��Ĭ�ϲ�����
		upyun.setDebug(true);

		// ****** ��ѡ���� end ******

		// 1.����Ŀ¼����������ʽ
		testMkDir();

		// 2.�ϴ��ļ���ͼƬ�ռ���ļ��ϴ���ο� PicBucketDemo.java
		testWriteFile();

		// 3.��ȡ�ļ���Ϣ
		testGetFileInfo();

		// 4.��ȡĿ¼
		testReadDir();

		// 5.��ȡ�ռ�ռ�ô�С
		testGetBucketUsage();

		// 7.��ȡ�ļ�/�����ļ�
		testReadFile();

		// 8.ɾ���ļ�
		testDeleteFile();

		// 9.ɾ��Ŀ¼
		testRmDir();
	}

	/**
	 * ��ȡ�ռ�ռ�ô�С
	 */
	public static void testGetBucketUsage() {

		long usage = upyun.getBucketUsage();

		System.out.println("�ռ���ʹ������" + usage + "B");
		System.out.println();
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @throws IOException
	 */
	public static void testWriteFile() throws IOException {

		// Ҫ�ϴ��Ĵ���������
		String content = "test content";

		// Ҫ����upyun����ļ�·��
		String filePath = DIR_ROOT + FILE_NAME;
		// Ҫ����upyun����ļ�·�����༶Ŀ¼
		String filePath2 = DIR_MORE + FILE_NAME;

		/*
		 * �ϴ�����1���ı�����ֱ���ϴ�
		 */
		boolean result1 = upyun.writeFile(filePath, content);
		System.out.println("1.�ϴ� " + filePath + isSuccess(result1));

		/*
		 * �ϴ�����2���ı�����ֱ���ϴ������Զ���������Ŀ¼�����10����
		 */
		boolean result2 = upyun.writeFile(filePath2, content, true);
		System.out.println("2.�ϴ� " + filePath2 + isSuccess(result2));

		/*
		 * �ϴ�����3������������ģʽ�ϴ��ļ�����ʡ�ڴ棩�����Զ���������Ŀ¼�����10����
		 */
		File file = new File(SAMPLE_TXT_FILE);
		boolean result3 = upyun.writeFile(filePath, file, true);
		System.out.println("3.�ϴ� " + filePath + isSuccess(result3));

		/*
		 * �ϴ�����4���Դ��ϴ����ļ����� MD5 ֵ��ȷ���ϴ��� Upyun ���ļ��������Ժ���ȷ��
		 */
		File file4 = new File(SAMPLE_TXT_FILE);
		// ���ô��ϴ��ļ��� Content-MD5 ֵ
		// ��������Ʒ�����յ����ļ�MD5ֵ���û����õĲ�һ�£����ر� 406 NotAcceptable ����
		upyun.setContentMD5(UpYun.md5(file4));

		boolean result4 = upyun.writeFile(filePath, file4, true);
		System.out.println("4.�ϴ� " + filePath + isSuccess(result4));
		System.out.println();

	}

	/**
	 * ��ȡ�ļ���Ϣ
	 */
	public static void testGetFileInfo() {

		// upyun�ռ��´��ڵ��ļ���·��
		String filePath = DIR_ROOT + FILE_NAME;

		System.out.println(filePath + " ���ļ���Ϣ��" + upyun.getFileInfo(filePath));
		System.out.println();
	}

	/**
	 * ��ȡ�ļ�/�����ļ�
	 * 
	 * @throws IOException
	 */
	public static void testReadFile() throws IOException {

		// upyun�ռ��´��ڵ��ļ���·��
		String filePath = DIR_ROOT + FILE_NAME;

		/*
		 * ����1��ֱ�Ӷ�ȡ�ı�����
		 */
		String datas = upyun.readFile(filePath);
		System.out.println(filePath + " ���ļ�����:" + datas);

		/*
		 * ����2�������ļ�������������ģʽ�����ļ�����ʡ�ڴ棩
		 */
		// Ҫд��ı�����ʱ�ļ�
		File file = File.createTempFile("upyunTempFile_", "");

		// ��upyun�ռ��µ��ļ����ص����ص���ʱ�ļ�
		boolean result = upyun.readFile(filePath, file);
		System.out.println(filePath + " ����" + isSuccess(result) + "�����浽 "
				+ file.getAbsolutePath());
		System.out.println();
	}

	/**
	 * ɾ���ļ�
	 */
	public static void testDeleteFile() {

		// upyun�ռ��´��ڵ��ļ���·��
		String filePath = DIR_ROOT + FILE_NAME;

		// ɾ���ļ�
		boolean result = upyun.deleteFile(filePath);

		System.out.println(filePath + " ɾ��" + isSuccess(result));
		System.out.println();
	}

	/**
	 * ����Ŀ¼
	 */
	public static void testMkDir() {

		// ����1������һ��Ŀ¼
		String dir1 = DIR_ROOT + FOLDER_NAME;

		boolean result1 = upyun.mkDir(dir1);
		System.out.println("����Ŀ¼��" + dir1 + isSuccess(result1));

		// ����2�������༶Ŀ¼���Զ���������Ŀ¼�����10����
		String dir2 = DIR_MORE + FOLDER_NAME;

		boolean result2 = upyun.mkDir(dir2, true);
		System.out.println("�Զ������༶Ŀ¼��" + dir2 + isSuccess(result2));
		System.out.println();
	}

	/**
	 * ��ȡĿ¼�µ��ļ��б�
	 */
	public static void testReadDir() {

		// �������Ի�������Ŀ¼·��
		String dirPath = DIR_ROOT;

		// ��ȡĿ¼�б������� List �� NULL
		List<UpYun.FolderItem> items = upyun.readDir(dirPath);

		if (null == items) {
			System.out.println("'" + dirPath + "'Ŀ¼��û���ļ���");

		} else {

			for (int i = 0; i < items.size(); i++) {
				System.out.println(items.get(i));
			}

			System.out.println("'" + dirPath + "'Ŀ¼�ܹ��� " + items.size()
					+ " ���ļ���");
		}

		System.out.println();
	}

	/**
	 * ɾ��Ŀ¼
	 */
	public static void testRmDir() {

		// ��ɾ����Ŀ¼������ڣ�����Ŀ¼���Ѳ������κ��ļ�����Ŀ¼
		String dirPath = DIR_MORE + FOLDER_NAME;

		boolean result = upyun.rmDir(dirPath);

		System.out.println("ɾ��Ŀ¼��" + dirPath + isSuccess(result));
		System.out.println();
	}

	private static String isSuccess(boolean result) {
		return result ? " �ɹ�" : " ʧ��";
	}
}
