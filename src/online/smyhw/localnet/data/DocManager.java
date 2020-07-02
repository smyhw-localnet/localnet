package online.smyhw.localnet.data;

/**
 * 该类用于读取文档内容
 * @author smyhw
 *
 */
public class DocManager 
{
	/**
	 * 从指定JAR路径获取文档文本
	 * @param path文档路径
	 * @return 文档文本
	 */
	public String readFromJar(String path)
	{
		return new String();
	}
	
	/**
	 * 从指定文件路径获得文档文本
	 * @param path 文件路径
	 * @return 文档文本
	 */
	public String readFromFile(String path)
	{
		return new String();
	}
	
	/**
	 * 根据给定的名称获得文档
	 * @param path 文档名称
	 * @return 文档文本
	 */
	public String readFromRunTime(String path)
	{
		return new String();
	}
	
	/**
	 * 根据给定名称设定RunTime文档</br>
	 * RunTime文档会覆盖其他相同路径的文档</br>
	 * @param doc 文档文本
	 * @param path 文档名称
	 * @return 如果设置成功即返回true，否则返回false
	 */
	public boolean setRunTimeDoc(String doc,String path)
	{
		return false;
	}
	/**
	 * 从指定路径获取文档文本</br>
	 * 自动选择从JAR中获取或从文件获取</br>
	 * JAR中的文档优先级高于文件</br>
	 * RunTime中的文档优先级高于JAR
	 * @param path 文档路径
	 * @return 文档文本
	 */
	public String getDoc(String path)
	{
		return new String();
	}
}
