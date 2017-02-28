package zzjz.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import zzjz.bean.EntityAttributeBean;
import zzjz.bean.EntityConfigBean;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态加载XML数据工具类
 * @author 梅宏振
 *
 */
public class XmlAutoLoad {
	
	private static Logger LOGGER = Logger.getLogger(XmlAutoLoad.class);

	/**
	 * 根据配置信息和XML数据文件加载数据
	 * @param str
	 * @param configFileName
	 * @param isFilePath 0:字符串,1:文件路径
	 * @param entityID
	 * @return
	 */
	public List<Object> loadData(String str, String configFileName, int isFilePath, String entityID) {
		LOGGER.debug("根据配置信息和XML数据文件加载数据.");
		//加载配置信息
		EntityConfigBean config = loadConfig(configFileName, entityID);
		if(config==null) {
			return null;
		}
		String xmlContent = "";
		if(isFilePath==1) {
			FileUtil fileUtil = new FileUtil();
			xmlContent = fileUtil.readLineFromFile(str);
		} else {
			xmlContent = str;
		}
		if(StringUtils.isBlank(xmlContent)) {
			LOGGER.debug("加载xml内容失败!");
			return null;
		} else {
			LOGGER.debug("XML:" + xmlContent);
		}
		
		Document doc = null;
		Element rootElement = null;
		Reader in = new StringReader(xmlContent);
		try {
			doc = new SAXBuilder().build(in);
			rootElement = doc.getRootElement();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return parseData(config, rootElement);
	}
	
	/**
	 * 加载配置文件并保存到EntityConfigBean中
	 * @param cofnigFile
	 * @param entityID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EntityConfigBean loadConfig(String configFileName,String entityID) {
		LOGGER.debug("加载XML配置信息.configFileName=" + configFileName+",entityID=" + entityID);
		EntityConfigBean configBean = null;
		//加载配置文件内容
		String xml = loadResource(configFileName);
		if(StringUtils.isBlank(xml)) {
			LOGGER.debug("加载配置文件失败!");
			return null;
		}
		Document doc = null;
		Reader in = new StringReader(xml);
		try {
			doc = new SAXBuilder().build(in);
			Element root = doc.getRootElement();
			List<Element > elements = root.getChildren("bean");
			for(Element element : elements) {
				if(entityID.equals(element.getAttributeValue("id"))) {
					configBean = setAttribute(element);
					//TODO 应该直接返回
				}
			}
					
		} catch(Exception e) {
			LOGGER.debug("加载XML配置信息失败!");
			e.printStackTrace();
		}
		
		return configBean;
	}

	/**
	 * 根据配置信息和XML元素返回数据
	 * @param config
	 * @param rootElement
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<Object> parseData(EntityConfigBean config, Element rootElement) {
		ArrayList<Object> result = new ArrayList<Object>();
		List<EntityAttributeBean> attributes = config.getAttributes();
		Class<?> cls = null;
		try {
			cls = Class.forName(config.getClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Object object = null;
		Class stringTypes[] = new Class[1];
		stringTypes[0] = String.class;
		Object[] paramValues = new String[1];
		Element tempElement = rootElement;
		while(tempElement.getChildren().size()>0) {
			List<Element> attributeElements = tempElement.getChildren();
			tempElement = (Element) attributeElements.get(0);
			//读取该节点的属性
			EntityAttributeBean attribute = getAttributeByName(attributes, tempElement.getName());
			//判断是否是实体类(如果是,创建对象,否则继续循环加载下一层)
			if(attribute!=null && attribute.isEntity()) {
				for(Element entityElement : attributeElements) {
					try {
						object = cls.newInstance();
						List<Element> es = entityElement.getChildren() ;
						setAttributeValue(attributes, cls, object, stringTypes,
								paramValues, es); 
						result.add(object);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return result;
	}

	/**
	 * 把XML元素的值设置到实体类中
	 * @param attributes
	 * @param cls
	 * @param object
	 * @param stringTypes
	 * @param paramValues
	 * @param es
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("rawtypes")
	private void setAttributeValue(List<EntityAttributeBean> attributes,
			Class<?> cls, Object object, Class[] stringTypes,
			Object[] paramValues, List<Element> es)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		EntityAttributeBean tempAttributeBean;
		for(Element element : es) {
			tempAttributeBean = getAttributeByName(attributes, element.getName());
			if(tempAttributeBean!=null) {
				if(!tempAttributeBean.isList()) {
					Method method = cls.getMethod("set"+StringUtil.upperFirstChar(tempAttributeBean.getAttribute()), stringTypes);
					paramValues[0] = element.getValue();
					method.invoke(object, paramValues);
				} else {
					//如果是List类型，需特殊处理(递归调用加载下一层)
					ArrayList<Object> subResult = parseData(tempAttributeBean.getSubBean(),element);
					Class listTypes[] = new Class[1];
					listTypes[0] = List.class;
					Method method = cls.getMethod("set"+StringUtil.upperFirstChar(tempAttributeBean.getAttribute()), listTypes);
					Object[] listValues = new List[1];
					listValues[0] = subResult;
					method.invoke(object, listValues);
				}
			}
			
		}
	} 
	
	/**
	 * 设置配置信息属性值
	 * @param element
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private EntityConfigBean setAttribute(Element element) {
		EntityConfigBean configBean;
		configBean = new EntityConfigBean();
		configBean.setId(element.getAttributeValue("id"));
		configBean.setClassName(element.getAttributeValue("class"));
		List<Element> attributes = element.getChildren();
		List<EntityAttributeBean> attributeList = new ArrayList<EntityAttributeBean>();
		EntityAttributeBean attributeBean = null;
		for(Element attribute : attributes) {
			attributeBean = new EntityAttributeBean();
			attributeBean.setName(attribute.getAttributeValue("name"));
			attributeBean.setAttribute(attribute.getAttributeValue("attribute"));
			attributeBean.setEntity(("true").equals(attribute.getAttributeValue("isEntity"))?true:false);
			String isListValue = attribute.getAttributeValue("isList");
			attributeBean.setIsList(("true").equals(isListValue)?true:false);
			if(("true").equals(isListValue)) { //如果是List类型的特殊处理
				EntityConfigBean subConfigBean = setAttribute(attribute);
				attributeBean.setSubBean(subConfigBean);
			}
			attributeList.add(attributeBean);
		}
		configBean.setAttributes(attributeList);
		
		return configBean;
	}
	
	/**
	 * 根据属性名获取属性实体
	 * @param attributes
	 * @param name
	 * @return
	 */
	private static EntityAttributeBean getAttributeByName(List<EntityAttributeBean> attributes, String name) {
		EntityAttributeBean attributeBean = null;
		for(EntityAttributeBean attribute : attributes) {
			if(name.equals(attribute.getName())) {
				return attribute;
			}
		}
		return attributeBean;
	}
	
	/**
     * 加载配置文件内容
     * @param path
     * @return
     */
    /*public String loadResource(String path) {
    	if(StringUtils.isBlank(path)) {
    		return "";
    	}
		String value = "";
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if(is==null) {
			LOGGER.debug("配置文件不存在!");
			return "";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			String tempString = "";
			while ((tempString = reader.readLine()) != null) {
				value = value + tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return value;
	}*/
    
    /**
     * 加载配置文件内容
     * @param path
     * @return
     */
	public String loadResource(String path) {
    	if(StringUtils.isBlank(path)) {
    		return "";
    	}
		String value = "";
		
		/*InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		if(is==null) {
			LOGGER.debug("配置文件不存在!");
			return "";
		}*/
		BufferedReader reader;
		try {
			//FileInputStream fis = new FileInputStream(path);
			InputStream is = new FileInputStream(path);
			//注！指定编码，防止乱码
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String tempString = "";
			
			while ((tempString = reader.readLine()) != null) {
				value = value + tempString;
			}
			reader.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println(">>>>path=" + path);
		System.out.println(">>>>value=" + value);
		return value;
	}
	
	public static void main(String[] args) {
		XmlAutoLoad loader = new XmlAutoLoad();
		System.out.println(">>>>" + loader.loadResource("D:\\unit.xml"));
		//List<Object> result = loader.loadData("c:\\user.txt", "config.xml", 1, "synuser");
		//System.out.println("加载结果: result size = " + result==null?0:result.size());
	}

}
