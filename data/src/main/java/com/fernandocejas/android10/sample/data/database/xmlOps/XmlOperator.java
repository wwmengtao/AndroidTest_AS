package com.fernandocejas.android10.sample.data.database.xmlOps;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.entity.UserEntityNT;
import com.fernandocejas.android10.sample.domain.interactor.GetUserNTList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * XmlOperator：解析assets目录下的玩家教程xml文件
 */
public class XmlOperator {
	private static final String TAG = "XmlOperator_";
	public static final String NoviceAssetsXmlDir = "novicetutorial"+File.separator+"xmlfiles";//玩家教程存储xml文件的Assets根目录
	public static final String NoviceAssetsPicDir = "novicetutorial"+File.separator+"title1_pics";//玩家教程存储图片的Assets根目录
	private XmlPullParser mXmlPullParser = null;//读取xml文件
	private String ioEncoding=null;
	private InputStream mInputStream=null;
	private Context mContext=null;
	private AssetManager mAssetManager = null;
	private Collection<UserEntityNT> mUserEntityNTCollection = null;
	//
	public XmlOperator(Context context){
		mContext=context.getApplicationContext();
		ioEncoding = "UTF-8";//"ISO-8859-1","UTF-16BE","UTF-16LE"
		mXmlPullParser = Xml.newPullParser();
		mAssetManager = mContext.getAssets();
		mUserEntityNTCollection = new ArrayList<>();
	}

	/**
	 * 读取xml文件中的数据写入数据列表并且返回
	 * @param params
	 * @return
	 */
	public Collection<UserEntityNT> UserEntityNTCollectionXml(GetUserNTList.Params params){
		if(null == params)return null;
		mUserEntityNTCollection.clear();
		//如果是一级、二级菜单文件那么就解析xml文件
		if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1 ||
				params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2){
			readFromXmlFile(params);
		}
		ALog.visitCollection(TAG, mUserEntityNTCollection);//浏览mUserEntityCollection中的数据内容
		return mUserEntityNTCollection;
	}

	public void readFromXmlFile(GetUserNTList.Params params){
		ALog.Log(TAG+"readFromXmlFile");
		String fileName = NoviceAssetsXmlDir + File.separator + params.getFileName();
		try {
			mInputStream = mAssetManager.open(fileName);
			mXmlPullParser.setInput(new BufferedInputStream(mInputStream), ioEncoding);
			filterBeforeRootElement(mXmlPullParser, params);
			parseXmlFirstElement(mXmlPullParser, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(null!=mInputStream){
				try {
					mInputStream.close();
					mInputStream=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * filterBeforeRootElement：过滤掉达到指定标签rootElementName之前的所有内容
	 * @param parser
	 * @param params
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void filterBeforeRootElement(XmlPullParser parser, GetUserNTList.Params params)
			throws XmlPullParserException, IOException{
		String rootElementName = null;
		if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1){
			rootElementName = XmlItemTags.LEVEL1_ITEM_TAGS.ROOT_ELEMENT_TAG;
		}else if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2){
			rootElementName = XmlItemTags.LEVEL2_ITEM_TAGS.ROOT_ELEMENT_TAG;
		}

		int type;
		//首先过滤掉非标签类事件
		while ((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
			;
		}
		//已经到了END_DOCUMENT
		if (type != XmlPullParser.START_TAG) {
			throw new XmlPullParserException("No start tag");
		}
		//已经到了START_TAG
		if (!parser.getName().equals(rootElementName)) {
			throw new XmlPullParserException("Unexpected start tag: "+parser.getName()+", expected: " + rootElementName);
		}
	}

	/**
	 * parseXmlFirstElement：解析xml文件中firstElement所包含的子标签内容
	 * @param parser
	 * @param params
	 */
	public void parseXmlFirstElement(XmlPullParser parser, GetUserNTList.Params params){
		String firstEleTag = null;
		if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1){
			firstEleTag = XmlItemTags.LEVEL1_ITEM_TAGS.FIRST_ELEMENT_TAG;
		}else if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2){
			firstEleTag = XmlItemTags.LEVEL2_ITEM_TAGS.FIRST_ELEMENT_TAG;
		}
		int type;
		try {
			while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (type) {
					case XmlPullParser.START_TAG:
						if (parser.getName().equals(firstEleTag)) {
							parseTagItem(parser,firstEleTag, params);
						}
						break;
					case XmlPullParser.END_TAG:
						break;
					default:
						break;
				}
			}//end while
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}

	/**
	 * parseTagItem：解析xml文件中第一标签内的其他标签元素
	 * @param parser
	 * @param FIRST_TAG_NAME 第一标签名称
	 * @param params
	 * @throws XmlPullParserException
	 * @throws IOException
	 * @throws ParseException
	 */
	private void parseTagItem(XmlPullParser parser, final String FIRST_TAG_NAME, GetUserNTList.Params params) throws XmlPullParserException,IOException,ParseException {
		ALog.Log(TAG + "parseTagItem: "+FIRST_TAG_NAME);
		int type;
		UserEntityNT mUserEntityNT = new UserEntityNT();
		String tag = null,value = null;
		for (;;) {
			type = parser.next();
			switch (type){
				case XmlPullParser.START_TAG:
					tag = parser.getName();
					break;
				case XmlPullParser.TEXT:
					if(null == tag)break;
					value = parser.getText().trim();
					ALog.Log("tag: "+tag+" val: "+value);
					if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL1){
						XmlItemTags.LEVEL1_ITEM_TAGS.setTagValue(mUserEntityNT,tag,value);
					}else if(params.getDataType() == GetUserNTList.Params.DataType.COLLECTION_DATA_LEVEL2){
						XmlItemTags.LEVEL2_ITEM_TAGS.setTagValue(mUserEntityNT,tag,value);
					}
					tag = null;
					break;
				case XmlPullParser.END_TAG:
					if(FIRST_TAG_NAME.equals(parser.getName())){
						mUserEntityNTCollection.add(mUserEntityNT);
						return;
					}
					break;
			}//end switch
		}//end for(;;)
	}//end parseTagItem
}
